/*
 * Created on 09/11/03
 *
 */
package com.increg.game.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.increg.commun.DBSession;
import com.increg.game.bean.belote.PartieBeloteBean;
import com.increg.serveur.servlet.ConnectedServlet;

/**
 * @author Manu
 *
 * Affiche les parties jouées sur une période
 * Particularité : Aucune session n'est requise
 */
public class AffPartie extends ConnectedServlet {

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
        
        String debut = request.getParameter("Debut");
        String fin = request.getParameter("Fin");

        DBSession dbConnect = null;
        dbConnect = (DBSession) getServletContext().getAttribute("dbSession");

        try {
            // Charge son historique
            // Recherche le joueur sur ce pseudo
            String reqSQL =
                "select * from partie, joueur where partie.pseudo[1] = joueur.pseudo "
                    + "and dtDebut >= " + DBSession.quoteWith(debut, '\'')
                    + "and dtDebut <= " + DBSession.quoteWith(fin, '\'')
                    + "order by dtDebut desc";
            Vector res = new Vector();

            // Interroge la Base
            try {
                ResultSet aRS = dbConnect.doRequest(reqSQL);

                while (aRS.next()) {
                    res.add(PartieBeloteBean.getPartieBeloteBean(aRS));
                }
                aRS.close();
            }
            catch (Exception e) {
                System.err.println("AffPartie : " + e.toString());
            }

            request.setAttribute("lstPartie", res);
            
            forward(request, response, "/affPartie.jsp");
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
