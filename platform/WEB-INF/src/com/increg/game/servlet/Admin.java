/*
 * Created on 06/01/04
 *
 */
package com.increg.game.servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.increg.commun.DBSession;
import com.increg.game.bean.GameEnvironment;
import com.increg.game.bean.JoueurBean;
import com.increg.game.bean.PartieBean;
import com.increg.game.client.Joueur;
import com.increg.game.client.Partie;
import com.increg.serveur.servlet.ConnectedServlet;

/**
 * @author Manu
 *
 * Gestion de l'aire de jeu
 * Particularité : Aucune session n'est requise
 */
public class Admin extends ConnectedServlet {

    /**
     * @see com.increg.game.servlet.ConnectedServlet#performTask(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void performTask(HttpServletRequest request, HttpServletResponse response) {
        
        String pseudo = request.getParameter("Pseudo");
        String action = request.getParameter("Action");
        String joueur = request.getParameter("Joueur");
        String partie = request.getParameter("Partie");
        String joueurPartie = request.getParameter("JoueurPartie");
        String partieComp = request.getParameter("PartieComp");

        GameEnvironment env = (GameEnvironment) getServletContext().getAttribute("Env");
        DBSession dbConnect = null;
        dbConnect = (DBSession) getServletContext().getAttribute("dbSession");
        boolean erreur = false;

        // Recherche le joueur sur ce pseudo
        JoueurBean anAdmin = JoueurBean.getJoueurBeanFromPseudo(dbConnect, pseudo);
        
        // Vérification que le joueur est bien modérateur
        if ((anAdmin != null) && (anAdmin.getPrivilege() >= Joueur.MODERATEUR_PRIVILEGE)) {

            String msg = null;
            JoueurBean aJoueur = null;
            PartieBean aPartie = null;
            JoueurBean aJoueurPartie = null;
            PartieBean aPartieComp = null;
            Vector lstPartie = new Vector();
            Vector lstJoueurPartie = new Vector();
            Vector lstToutesParties = (Vector) env.getLstPartie().clone();
            
            if (joueur != null) {
                aJoueur = JoueurBean.getJoueurBeanFromPseudo(dbConnect, joueur);
            }
            if (aJoueur != null) {
                // Chargement de la liste de ses parties
                Enumeration enumToutesParties = lstToutesParties.elements();
                while (enumToutesParties.hasMoreElements()) {
                    PartieBean aPartie2 = (PartieBean) enumToutesParties.nextElement();
                    
                    if (aPartie2.getMyPartie().joueurVoitPartie(aJoueur)) {
                        lstPartie.add(aPartie2);
                        
                        if ((partie != null) && (aPartie2.getMyPartie().getTitre().equals(partie))) {
                            aPartie = aPartie2;
                        }
                    }
                }
            }
            if (partieComp != null) {
                // Chargement de la liste de ses parties
                Enumeration enumToutesParties = lstToutesParties.elements();
                while (enumToutesParties.hasMoreElements()) {
                    PartieBean aPartie2 = (PartieBean) enumToutesParties.nextElement();
                    
                    if ((partieComp != null) && (aPartie2.getMyPartie().getTitre().equals(partieComp))) {
                        aPartieComp = aPartie2;
                    }
                }
            }
            if (aPartieComp != null) {
                // Chargement de la liste des joueurs de la partie
                Partie aPartie2 = aPartieComp.getMyPartie();
                for (int i = 0; i < aPartie2.getParticipant().length; i++) {
                    if (aPartie2.getParticipant(i) != null) {
                        lstJoueurPartie.add(aPartie2.getParticipant(i));
                    }
                }
                lstJoueurPartie.addAll(aPartie2.getSpectateurs());
            }
            if (joueurPartie != null) {
                aJoueurPartie = JoueurBean.getJoueurBeanFromPseudo(dbConnect, joueurPartie);
            }
            
            if ((action == null) || (action.equals("Refresh"))) {
                // Premier chargement
                
            }
            else if (action.equals("ExAire")) {
                
                if (aJoueur != null) {
                    // Supprime le joueur de la liste
                    env.sortieJoueur(aJoueur);
                    msg = "Exclusion de l'aire effectuée";  
                    System.out.println("Exclusion de l'aire de : " + aJoueur.getPseudo());            
                }
                else if (aJoueurPartie != null) {
                    // Supprime le joueur de la liste
                    env.sortieJoueur(aJoueurPartie);
                    msg = "Exclusion de l'aire effectuée";  
                    System.out.println("Exclusion de l'aire de : " + aJoueurPartie.getPseudo());            
                }
                else {
                    msg = "Un joueur doit être sélectionné";  
                }
                
                // TODO Envoi sur URL exclue.php?pseudo=...

            }
            else if (action.equals("ExPartie")) {
                if ((aJoueur != null) && (aPartie != null)) {
                    // Le joueur sort de la salle
                    env.joueurSeLeve(aPartie.getMyPartie(), aJoueur);
                    lstPartie.remove(aPartie);
            
                    msg = "Exclusion de la partie effectuée";                
                    System.out.println("Exclusion de la partie " + aPartie.getMyPartie().getIdentifiant() + " de : " + aJoueur.getPseudo());
                }
                else if ((aJoueurPartie != null) && (aPartieComp != null)) {
                    // Le joueur sort de la salle
                    env.joueurSeLeve(aPartieComp.getMyPartie(), aJoueurPartie);
                    lstPartie.remove(aPartieComp);
            
                    msg = "Exclusion de la partie effectuée";                
                    System.out.println("Exclusion de la partie " + aPartieComp.getMyPartie().getIdentifiant() + " de : " + aJoueurPartie.getPseudo());
                }
                else {
                    msg = "Un joueur et une salle doivent être sélectionnés d'un coté ou de l'autre";
                }
            }
            else {
                // Action inconnue
                erreur = true;
                msg = "Action inconnue";
            }
            
            if (!erreur) {
                // Chargement de la liste des joueurs
                request.setAttribute("lstJoueur", env.getLstJoueur().clone());
                request.setAttribute("lstPartie", lstPartie);
                request.setAttribute("lstJoueurPartie", lstJoueurPartie);
                request.setAttribute("lstPartieComp", lstToutesParties);
                request.setAttribute("joueur", aJoueur);
                request.setAttribute("partie", aPartie);
                request.setAttribute("partieComp", aPartieComp);
                request.setAttribute("joueurPartie", aJoueurPartie);
            }
            request.setAttribute("Message", msg);                
        }
        else {
            erreur = true;
        }
        
        try {
            if (!erreur) {
                forward(request, response, "/admin.jsp");
            }
            else {
                forward(request, response, "/error.html");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ServletException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fonction utilitaire pour simplifier les forward
     * @param request Requête à forwarder
     * @param response Réponse en cours d'élaboration
     * @param urlDest String Adresse destination du forward
     * @throws IOException ?
     * @throws ServletException ?
     */
    public void forward(HttpServletRequest request, HttpServletResponse response, String urlDest) throws IOException, ServletException {
        getServletConfig().getServletContext().getRequestDispatcher(urlDest).forward(request, response);
    }

}
