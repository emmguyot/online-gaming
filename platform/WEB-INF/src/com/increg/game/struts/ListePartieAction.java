/*
 * Affichage de l'historique des parties
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
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.increg.commun.DBSession;
import com.increg.game.bean.GameSession;
import com.increg.game.bean.belote.PartieBeloteBean;

/**
 * @author Manu
 *
 */
public class ListePartieAction extends AdminAction {

	/* (non-Javadoc)
	 * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward affPartie(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		IntervalForm intervalForm = (IntervalForm) form;
		
		ActionMessages errors = form.validate(mapping, request);
		if (errors.isEmpty()) {
	        DBSession dbConnect = ((GameSession) request.getSession().getAttribute("mySession")).getMyDBSession();
	        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	
            // Charge son historique
            // Recherche le joueur sur ce pseudo
            String reqSQL = "select * from partie ";
            reqSQL += " where dtDebut >= " + DBSession.quoteWith(intervalForm.getDebut(), '\'');
            reqSQL += " and dtDebut <= " + DBSession.quoteWith(intervalForm.getFin(), '\'');
            reqSQL += " order by dtDebut desc";
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
            	errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.detail", e));
            }
            intervalForm.setLignes(res);
		}
		
        saveErrors(request, errors);
		return mapping.findForward("page");
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward menu(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward("menu");
	}
}
