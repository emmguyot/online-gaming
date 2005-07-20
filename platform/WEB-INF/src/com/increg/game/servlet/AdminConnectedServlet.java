/*
 * Created on 06/01/04
 *
 */
package com.increg.game.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.increg.commun.DBSession;
import com.increg.game.bean.GameEnvironment;
import com.increg.game.bean.GameSession;
import com.increg.game.bean.JoueurBean;
import com.increg.game.client.Joueur;
import com.increg.serveur.servlet.ConnectedServlet;

/**
 * @author Manu
 *
 * Gestion de l'aire de jeu
 * Particularité : Aucune session n'est requise
 */
public abstract class AdminConnectedServlet extends ConnectedServlet {

    /**
     * @see com.increg.game.servlet.ConnectedServlet#performTask(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    final public void performTask(HttpServletRequest request, HttpServletResponse response) {

    	try {
	        String pseudo = request.getParameter("Pseudo");
	
	        GameEnvironment env = (GameEnvironment) getServletContext().getAttribute("Env");
	        DBSession dbConnect = null;
	        dbConnect = (DBSession) getServletContext().getAttribute("dbSession");
	        boolean erreur = false;
	
	        // Recherche le joueur sur ce pseudo
	        JoueurBean anAdmin = JoueurBean.getJoueurBeanFromPseudo(dbConnect, pseudo);
	        
	        // Vérification supplémentaire pour plus de sécurité : Le joueur doit être connecté dans l'aire de jeu
	        HttpSession session = request.getSession();
	        GameSession mySession = (GameSession) session.getAttribute("mySession");
	        boolean joueurDansAire = ((mySession != null) 
	                            && (mySession.getMyJoueur() != null)
	                            && (mySession.getMyJoueur().getPseudo().equals(pseudo)));
	        
	        // Vérification que le joueur est bien modérateur
	        if ((anAdmin != null) && (anAdmin.getPrivilege() >= Joueur.MODERATEUR_PRIVILEGE) && joueurDansAire) {
	        	performTaskAdmin(request, response);
	        }
	        else {
	            try {
	                forward(request, response, "/error.html");
	            }
	            catch (IOException e) {
	                e.printStackTrace();
	            }
	            catch (ServletException e) {
	                e.printStackTrace();
	            }
	        }

    	}
    	catch (Exception e) {
    		e.printStackTrace();
            try {
                forward(request, response, "/error.html");
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
            catch (ServletException e2) {
                e2.printStackTrace();
            }
		}
    }

    /**
     * Effectue une tache en tant qu'admin
     * @param request Requete J2EE
     * @param response Réponse J2EE
     */
    public abstract void performTaskAdmin(HttpServletRequest request, HttpServletResponse response);

}
