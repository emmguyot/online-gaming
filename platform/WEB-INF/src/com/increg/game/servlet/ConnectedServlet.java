/*
 * Servlet Mère de TOUTES les servlets de l'aire de jeu
 * Assure que l'utilisateur s'est correctement connecté et fait le ménage d'une transaction à une autre
 * Copyright (C) 2001-2011 Emmanuel Guyot <See emmguyot on SourceForge> 
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation; either 
 * version 2 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; 
 * if not, write to the 
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 */
package com.increg.game.servlet;

import java.io.IOException;

import com.increg.commun.DBSession;
import com.increg.game.bean.GameSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public abstract class ConnectedServlet extends HttpServlet {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -8941863679957644460L;

	/**
     * RAZ des points pouvant bloquer (Transaction d'une session, ...)
     * Creation date: (20/09/2001 21:04:54)
     * @param request Requête en cours de traitement
     */
    protected void cleanUp(HttpServletRequest request) {

		DBSession dbConnect = (DBSession) request.getAttribute("DBSession");

        dbConnect.cleanTransaction();
    }
    /**
     * Process incoming HTTP GET requests 
     * 
     * @param request Object that encapsulates the request to the servlet 
     * @param response Object that encapsulates the response from the servlet
     * @throws ServletException ? 
     * @throws IOException ?
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (verifCnx(request, response)) {
            cleanUp(request);
            performTask(request, response);
        }

    }
    /**
     * Process incoming HTTP POST requests 
     * 
     * @param request Object that encapsulates the request to the servlet 
     * @param response Object that encapsulates the response from the servlet
     * @throws IOException .
     * @throws ServletException .
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (verifCnx(request, response)) {
            cleanUp(request);
            performTask(request, response);

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

    /**
     * Returns the servlet info string.
     * @return the servlet info string.
     */
    public String getServletInfo() {

        return super.getServletInfo();

    }

    /**
     * Initializes the servlet.
     * A l'instanciation (1er accès à cette servlet pour tous)
     */
    public void init() {
        // insert code to initialize the servlet here

    }

    /**
     * Process incoming requests for information
     * 
     * @param request Object that encapsulates the request to the servlet 
     * @param response Object that encapsulates the response from the servlet
     */
    public abstract void performTask(HttpServletRequest request, HttpServletResponse response);

    /**
     * Vérifie la connexion
     * Creation date: (08/07/2001 12:50:37)
     * @param request Requête en cours de traitement
     * @param response Réponse en cours d'élaboration
     * @return boolean
     */
    protected boolean verifCnx(HttpServletRequest request, HttpServletResponse response) {

        HttpSession mySession = request.getSession(false);
        if ((mySession == null) 
                || (mySession.getAttribute("mySession") == null) 
                || (((GameSession) mySession.getAttribute("mySession")).getMyJoueur() == null)) {
            try {
                forward(request, response, "/error.html");
            }
            catch (Exception e) {
                System.err.println("Erreur à la vérification de connexion : " + e.toString());
                try {
                    response.sendError(500);
                }
                catch (Exception e2) {
                    System.err.println("Erreur au sendError : " + e2.toString());
                }
            }
            return false;
        }
        else {
            if (request.getAttribute("noRAZ") == null) {
                // RAZ des messages
                ((GameSession) mySession.getAttribute("mySession")).setMessage("Erreur", (String) null);
                ((GameSession) mySession.getAttribute("mySession")).setMessage("Info", (String) null);
            }
        }
        return true;
    }
}
