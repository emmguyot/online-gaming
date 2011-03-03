package com.increg.serveur.servlet;

import com.increg.commun.*;
import com.increg.commun.exception.NoDatabaseException;

import javax.servlet.*;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 * Compteur de connexion et gestion de la connexion à la base de données
 * Creation date: (31/12/2001 18:26:48)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class CountSession implements HttpSessionBindingListener {
	/**
     * Contexte 
	 */
    protected javax.servlet.ServletContext srvCtxt;
    /**
     * CountSession constructor comment.
     */
    public CountSession() {
    	super();
    }
    /**
     * CountSession constructor comment.
     * @param newSrvCtxt Nouveau contexte
     */
    public CountSession(ServletContext newSrvCtxt) {
    	super();
    	setSrvCtxt(newSrvCtxt);
    }
    /**
     * Insert the method's description here.
     * Creation date: (31/12/2001 19:20:24)
     * @return javax.servlet.ServletContext
     */
    public javax.servlet.ServletContext getSrvCtxt() {
    	return srvCtxt;
    }
    /**
     * Insert the method's description here.
     * Creation date: (31/12/2001 19:20:24)
     * @param newSrvCtxt javax.servlet.ServletContext
     */
    public void setSrvCtxt(javax.servlet.ServletContext newSrvCtxt) {
    	srvCtxt = newSrvCtxt;
    }
    /**
     * valueBound method comment.
     * @param arg1 Evénement
     */
    public synchronized void valueBound(HttpSessionBindingEvent arg1) {
    
    	Integer count = (Integer) srvCtxt.getAttribute("Count");
    
    	if (count == null) {
    		count = new Integer(0);
    		// Connexion à la base
    		DBSession dbConnect;
			try {
				dbConnect = new DBSession();
	    		srvCtxt.setAttribute("dbSession", dbConnect);
			} catch (NoDatabaseException e) {
				e.printStackTrace();
			}
    //		System.out.println ("Connexion base");
    	}
    	srvCtxt.setAttribute("Count", new Integer(count.intValue() + 1));
    
    //	System.out.println (new Date().toString() + " Bound : " + srvCtxt.getAttribute("Count"));
    }
    /**
     * valueUnbound method comment.
     * @param arg1 Evénement
     */
    public synchronized void valueUnbound(HttpSessionBindingEvent arg1) {
    
    	Integer count = (Integer) srvCtxt.getAttribute("Count");
    
    	if ((count == null) || (count.intValue() <= 1)) {
    		// dernière connexion : Libere la connexion à la base
    		srvCtxt.removeAttribute("dbSession");
    		srvCtxt.removeAttribute("Count");
    //		System.out.println ("RAZ Connexion base");
    	}
    	else {
    		srvCtxt.setAttribute("Count", new Integer(count.intValue() - 1));
    	}
    
    //	System.out.println (new Date().toString() + " Unbound : " + srvCtxt.getAttribute("Count"));
    	
    }
}
