/*
 * Created on 6 mai 2003
 *
 */
package com.increg.game.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.increg.commun.DBSession;
import com.increg.game.bean.JoueurBean;
import com.increg.game.bean.PartieBean;
import com.increg.game.client.belote.PartieBelote;
import com.increg.serveur.servlet.ConnectedServlet;

/**
 * @author Manu
 *
 * Affiche les données d'un joueurs
 * Particularité : Aucune session n'est requise
 */
public class AffJoueur extends ConnectedServlet {

    /**
     * @see com.increg.game.servlet.ConnectedServlet#performTask(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void performTask(HttpServletRequest request, HttpServletResponse response) {
        
        try {
            request.setCharacterEncoding("UTF8");
        }
        catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            // Tant pis
        }
        
        String pseudo = request.getParameter("Pseudo");
        String offset = request.getParameter("offset");
        int intOffset = 0;
        if (offset != null) {
            intOffset = Integer.parseInt(offset);
        }

        DBSession dbConnect = null;
        dbConnect = (DBSession) getServletContext().getAttribute("dbSession");

        // Recherche le joueur sur ce pseudo
        JoueurBean aJoueur = JoueurBean.getJoueurBeanFromPseudo(dbConnect, pseudo);
        
        try {
            if (aJoueur != null) {
                // Charge son historique
                aJoueur.getHistorique(dbConnect);
                request.setAttribute("nbParties", new Integer(aJoueur.getHistorique().size()));
                
                // Calcul des gains
                int nbGainSrv = 0;
                Iterator partieIter = aJoueur.getHistorique().iterator();
                while (partieIter.hasNext()) {
                    PartieBean aPartieBean = (PartieBean) partieIter.next();
                    PartieBelote aPartie = (PartieBelote) aPartieBean.getMyPartie();
                    if (aPartie.getScoreTotal(0) > aPartie.getScoreTotal(1)) {
                        if ((aPartie.getParticipant(0).getPseudo().equals(request.getParameter("Pseudo")))
                                || (aPartie.getParticipant(2).getPseudo().equals(request.getParameter("Pseudo")))) {
                            nbGainSrv++;
                        }
                    }
                    else {
                        if ((aPartie.getParticipant(1).getPseudo().equals(request.getParameter("Pseudo")))
                                || (aPartie.getParticipant(3).getPseudo().equals(request.getParameter("Pseudo")))) {
                            nbGainSrv++;
                        }
                    }
                }
                
                request.setAttribute("nbGain", new Integer(nbGainSrv));
                request.setAttribute("Joueur", aJoueur);

                // Ne retient que les historiques dans la fourchette
                int deb = intOffset;
                int fin = intOffset + 20;
                if (deb < 0) {
                    deb = 0;
                }
                if (fin >= aJoueur.getHistorique().size()) {
                    fin = aJoueur.getHistorique().size();
                }
                aJoueur.setHistorique(new Vector(aJoueur.getHistorique().subList(deb, fin)));
                
                forward(request, response, "/affJoueur.jsp");
            }
        }
        catch (ServletException e) {
            try {
                e.printStackTrace();
                forward(request, response, "/error.html");
            }
            catch (Exception e2) {
                //
            }
        }
        catch (IOException e) {
            try {
                e.printStackTrace();
                forward(request, response, "/error.html");
            }
            catch (Exception e2) {
                //
            }
        }
        catch (NullPointerException e) {
            try {
                e.printStackTrace();
                forward(request, response, "/error.html");
            }
            catch (Exception e2) {
                //
            }
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
