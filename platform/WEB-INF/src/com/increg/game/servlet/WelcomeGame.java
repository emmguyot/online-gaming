package com.increg.game.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.zip.DataFormatException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.increg.commun.exception.FctlException;
import com.increg.commun.exception.UnauthorisedUserException;
import com.increg.game.bean.GameEnvironment;
import com.increg.game.bean.GameSession;

/**
 * Servlet d'acc�s � l'aire : Initialisation de la session
 * Creation date: 14 avr. 2003
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class WelcomeGame extends HttpServlet {

    /**
     * Process incoming HTTP GET requests 
     * 
     * @param request Object that encapsulates the request to the servlet 
     * @param response Object that encapsulates the response from the servlet
     * @throws IOException ...
     * @throws ServletException ...
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        performTask(request, response);

    }

    /**
     * Process incoming HTTP POST requests 
     * 
     * @param request Object that encapsulates the request to the servlet 
     * @param response Object that encapsulates the response from the servlet
     * @throws IOException ...
     * @throws ServletException ...
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        performTask(request, response);

    }

    /**
     * Returns the servlet info string.
     * @return Info
     */
    public String getServletInfo() {

        return super.getServletInfo();

    }
    /**
     * Initializes the servlet.
     */
    public void init() {
        // Initialise l'environnement
        getServletContext().setAttribute("Env", new GameEnvironment());
    }

    /**
     * Process incoming requests for information
     * 
     * @param request Object that encapsulates the request to the servlet 
     * @param response Object that encapsulates the response from the servlet
     */
    public void performTask(HttpServletRequest request, HttpServletResponse response) {

        try {
            GameEnvironment env = (GameEnvironment) getServletContext().getAttribute("Env");
            // Cr�ation de la session
            boolean sessionOk = false;
            HttpSession mySession = request.getSession(true);

            String fichConfig = GameSession.DEFAULT_CONFIG;

            try {
                request.setCharacterEncoding("UTF8");
            }
            catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
                // Tant pis
            }

            /**
             * Controle de la validit� de la connexion
             * Dans l'URL : pseudo et crc
             * Le crc est calcul� � partir du pseudo, de la date du jour
             */
            String pseudo = request.getParameter("Pseudo");
            String crc = request.getParameter("id");
            // !TODO! Suppression du param�tre de d�buggage
            String InCrEGDebug = request.getParameter("InCrEGDebug");
            GameSession myGame = null;

            // V�rification de la pr�sence des param�tres
            boolean paramOk = (pseudo != null) && (crc != null) && (pseudo.length() > 0) && (crc.length() > 0);
            if (!paramOk) {
                System.err.println("Warning : Param�tres invalides pseudo=>" + pseudo + "< id=>" + crc + "<");
            }
             
            if (paramOk && (!mySession.isNew())) {
                // La session existe d�j� : V�rification
                myGame = (GameSession) mySession.getAttribute("mySession");
                
                if ((myGame != null) 
                        && (myGame.getMyJoueur() != null) 
                        && (myGame.getMyJoueur().getPseudo().equals(pseudo))) {
                    // R�utilisation de la session
                    sessionOk = true;
                    // Positionne l'offset de Chat
                    myGame.setLastChatSeen(env.getChatOffset());
                    myGame.setFrozen(true);
                    System.err.println("R�utilisation de la session de " + pseudo);
                }
                else {
                    // Recr�ation
                    mySession.invalidate();
                    mySession = request.getSession(true);
                    System.err.println("Reset de la session");
                }
                
            }

            if (paramOk && (!sessionOk)) {
                /**
                 * Controle de la limite des connexions
                 */        
                ResourceBundle resConfig = ResourceBundle.getBundle(fichConfig);
                if (env.getLstJoueur().size() >= Integer.parseInt(resConfig.getString("maxCnx"))) {
                    System.err.println("Warning : D�pacement de capacit� pour >" + pseudo + "<"); 
                }
                else {
            
                    try {
                        // Affectation du Bean Session
                        myGame = new GameSession(getServletContext(), fichConfig, pseudo, crc);
                    
                        // Recharge les donn�es du joueur � partir du site Beloteux
                        // Eventuellement l�ve une exception si le joueur n'a plus le droit
                        // Pr�pare l'URL d'appel
                        StringBuffer fullURL = new StringBuffer(myGame.getBaseURL());
                        fullURL = fullURL.append("?Pseudo=")
                                         .append(URLEncoder.encode(pseudo, "UTF8"))
                                         .append("&id=")
                                         .append(myGame.getSecurity().getCRC(pseudo)); 
                        URL curURL = new URL(fullURL.toString());
                        HttpURLConnection aCon = (HttpURLConnection) curURL.openConnection();
                        // Pas de cache : Sinon ca sert � rien
                        aCon.setUseCaches(false);
                        aCon.connect();
    
                        if (aCon.getResponseCode() == 200) {
                            InputStream restauStream = aCon.getInputStream();
                            try {
                                myGame.getMyJoueur().reloadJoueur(restauStream);
                            
                                if (myGame.getMyJoueur().getCdJoueur() == 0) {
                                    // Cr�ation du nouveau joueur
                                    myGame.getMyJoueur().create(myGame.getMyDBSession());
                                }
                                else {
                                    // Sauvegarde le joueur mise � jour
                                    myGame.getMyJoueur().maj(myGame.getMyDBSession());
                                }
                            }
                            catch (DataFormatException e) {
                                System.err.println("Format du fichier invalide => Refus de la connexion");
                                throw new UnauthorisedUserException();
                            }
                            catch (SQLException e) {
                                System.err.println("Erreur � la mise � jour du joueur : " + e.toString());
                                // Ignore l'erreur
                            }
                            catch (FctlException e) {
                                System.err.println("Erreur � la mise � jour du joueur : " + e.toString());
                                // Ignore l'erreur
                            }
                            finally {
                                restauStream.close();
                                aCon.disconnect();
                                aCon = null;
                            }
                        }
                        else {
                            // Erreur = Pas le droit
                            aCon.disconnect();
                            aCon = null;
                            throw new UnauthorisedUserException();
                        }
    
                        // Positionne l'offset de Chat
                        myGame.setLastChatSeen(env.getChatOffset());
                        mySession.setAttribute("mySession", myGame);
                    }
                    catch (UnauthorisedUserException e) {
                        System.err.println("Warning : Acc�s invalide pseudo=>" + pseudo + "< id=>" + crc + "<");
                        if (myGame != null) {
                            myGame.setMyJoueur(null);
                        }
                    }
                    catch (Throwable theException) {
                        // Erreurs non intercept�es ?
                        System.err.println("Erreur dans WelcomeGame : ");
                        theException.printStackTrace();
                    }
                }
            }

            if ((myGame != null) && (myGame.getMyJoueur() != null)) {
                // Affiche la page principale
                /** 
                 * Forward pour conserver l'unit� de transaction 
                 * et pour pouvoir passer des param�tres via Bean Request
                 **/
                if (InCrEGDebug != null) {
                    getServletConfig().getServletContext().getRequestDispatcher("/MainDebug.jsp").forward(request, response);
                }
                else {
                    getServletConfig().getServletContext().getRequestDispatcher("/verifJava.jsp").forward(request, response);
                }
            }
            else {
                // Forward sur page par d�faut
                mySession.invalidate();
                if (myGame != null) {
                    response.sendRedirect(myGame.getDefaultRedirect().toExternalForm());
                }
                else {
                    response.sendRedirect(new GameSession().getDefaultRedirect().toExternalForm());
                }
            }
        }
        catch (Throwable theException) {
            System.err.println("Erreur dans WelcomeGame : ");
            theException.printStackTrace();
        }
    }
}