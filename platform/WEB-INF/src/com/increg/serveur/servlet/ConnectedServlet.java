package com.increg.serveur.servlet;

import com.increg.commun.*;
import javax.servlet.*;
import java.io.*;
import javax.servlet.http.*;
/**
 * Servlet générique permettant de suivre les connexions
 * Creation date: (08/07/2001 12:48:48)
 * @author: Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public abstract class ConnectedServlet extends javax.servlet.http.HttpServlet {
/**
 * RAZ des points pouvant bloquer (Transaction d'une session, ...)
 * Creation date: (20/09/2001 21:04:54)
 */
protected void cleanUp(HttpServletRequest request) {
	
}
/**
 * Process incoming HTTP GET requests 
 * 
 * @param request Object that encapsulates the request to the servlet 
 * @param response Object that encapsulates the response from the servlet
 */
public void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {

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
 */
public void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
	throws javax.servlet.ServletException, java.io.IOException {

	if (verifCnx(request, response)) {
		cleanUp(request);
		performTask(request, response);

	}
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2001 14:24:43)
 * @param urlDest String Adresse destination du forward
 */
public void forward(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, String urlDest) throws IOException, ServletException
{
	getServletConfig().getServletContext().getRequestDispatcher(urlDest).forward(request, response);
}
/**
 * Returns the servlet info string.
 */
public String getServletInfo() {

	return super.getServletInfo();

}
/**
 * Initializes the servlet.
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
public abstract void performTask(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response);
/**
 * Vérifie la connexion
 * Creation date: (08/07/2001 12:50:37)
 * @return boolean
 */
protected boolean verifCnx(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {

	HttpSession mySession = request.getSession(true);

	if ((mySession.isNew()) || (mySession.getAttribute("myCount") == null)) {
		CountSession aCount = new CountSession(getServletContext());
		mySession.setAttribute("myCount", aCount);
	}
	return true;
}
}
