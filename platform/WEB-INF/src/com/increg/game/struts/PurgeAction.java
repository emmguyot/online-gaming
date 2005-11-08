/*
 * Gestion de la purge des données et de l'optimisation de la base
 * Copyright (C) 2005 Emmanuel Guyot <See emmguyot on SourceForge>
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
package com.increg.game.struts;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.increg.commun.DBSession;
import com.increg.game.bean.GameSession;

/**
 * @author Manu
 *
 */
public class PurgeAction extends AdminAction {

	/* (non-Javadoc)
	 * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward purge(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Alimentation des données : Pour affichage
		PurgeForm purgeForm = (PurgeForm) form;
		
        DBSession dbConnect = ((GameSession) request.getSession().getAttribute("mySession")).getMyDBSession();
        
        String reqSQL = "select min(dtDebut) from Partie";
        ResultSet rs = dbConnect.doRequest(reqSQL);
        if (rs.next()) {
        	purgeForm.setDtVieillePartie(rs.getTimestamp(1));
        }
        
        reqSQL = "select min(dtModif) from Joueur";
        rs = dbConnect.doRequest(reqSQL);
        if (rs.next()) {
        	purgeForm.setDtVieuxJoueur(rs.getTimestamp(1));
        }

        reqSQL = "select count(*) from Joueur";
        rs = dbConnect.doRequest(reqSQL);
        if (rs.next()) {
        	purgeForm.setNbJoueurs(new Integer(rs.getInt(1)));
        }

        reqSQL = "select count(*) from Partie";
        rs = dbConnect.doRequest(reqSQL);
        if (rs.next()) {
        	purgeForm.setNbPartieTot(new Integer(rs.getInt(1)));
        }

		return mapping.findForward("page");
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward purgePartie(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		PurgeForm purgeForm = (PurgeForm) form;
		
        DBSession dbConnect = ((GameSession) request.getSession().getAttribute("mySession")).getMyDBSession();
        
		ActionMessages errors = form.validate(mapping, request);
		if (errors.isEmpty()) {
	        String reqSQL = "delete from partie where dtdebut < " + DBSession.quoteWith(purgeForm.dtPurge, '\'');
	        int[] nb = dbConnect.doExecuteSQL(new String[] { reqSQL });
	        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("purge.partie", new Integer(nb[0])));
		}
        
        saveErrors(request, errors);
        return purge(mapping, form, request, response);
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward purgeJoueur(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		PurgeForm purgeForm = (PurgeForm) form;
		
        DBSession dbConnect = ((GameSession) request.getSession().getAttribute("mySession")).getMyDBSession();
        
		ActionMessages errors = form.validate(mapping, request);
		if (errors.isEmpty()) {
	        String reqSQL = "delete from joueur where dtmodif < " + DBSession.quoteWith(purgeForm.dtPurge, '\'');
	        int[] nb = dbConnect.doExecuteSQL(new String[] { reqSQL });
	        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("purge.joueur", new Integer(nb[0])));
		}
        
        saveErrors(request, errors);
        return purge(mapping, form, request, response);
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward optimise(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
		DBSession dbConnect = ((GameSession) request.getSession().getAttribute("mySession")).getMyDBSession();

		ActionMessages errors = new ActionMessages();

		// Nettoyage de la base
        try {
            dbConnect.doExecuteSQL(new String[] {"vacuum"});
        }
        catch (SQLException e) {
        	errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.detail", e));
        }

        // Optimisation de la base
        try {
            dbConnect.doExecuteSQL(new String[] {"vacuum analyze"});
        }
        catch (SQLException e) {
        	errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.detail", e));
        }

        saveErrors(request, errors);
        return purge(mapping, form, request, response);
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward menu(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward("menu");
	}

}
