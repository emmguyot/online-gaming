/*
 * Gestion de l'exclusion de l'aire
 * Copyright (C) 2005-2011 Emmanuel Guyot <See emmguyot on SourceForge>
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

import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.increg.commun.DBSession;
import com.increg.game.bean.GameEnvironment;
import com.increg.game.bean.GameSession;
import com.increg.game.bean.JoueurBean;
import com.increg.game.bean.PartieBean;
import com.increg.game.client.Partie;

/**
 * @author Manu
 *
 */
public class ExclureAction extends AdminAction {

	/* (non-Javadoc)
	 * @see com.increg.game.struts.AdminAction#exclure(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward exclure(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ExclureForm exclureForm = (ExclureForm) form;

        GameEnvironment env = (GameEnvironment) getServlet().getServletContext().getAttribute("Env");
		DBSession dbConnect = (DBSession) request.getAttribute("DBSession");

        Vector<PartieBean> lstToutesParties = (Vector<PartieBean>) env.getLstPartie().clone();
        
        initData(exclureForm, dbConnect, lstToutesParties);
        exclureForm.setLstJoueur((Vector<JoueurBean>) env.getLstJoueur().clone());
        
        return mapping.findForward("page");
	}
	
	/* (non-Javadoc)
	 * @see com.increg.game.struts.AdminAction#exclure(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward exAire(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		ExclureForm exclureForm = (ExclureForm) form;
		ActionMessages errors = new ActionMessages();

        GameEnvironment env = (GameEnvironment) getServlet().getServletContext().getAttribute("Env");
		DBSession dbConnect = (DBSession) request.getAttribute("DBSession");

        Vector<PartieBean> lstToutesParties = (Vector<PartieBean>) env.getLstPartie().clone();
        
        initData(exclureForm, dbConnect, lstToutesParties);

        if (exclureForm.getAJoueur() != null) {
            // Supprime le joueur de la liste
            env.sortieJoueur(exclureForm.getAJoueur());
        	errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("exclure.aire.ok"));
            log.info("Exclusion de l'aire de : " + exclureForm.getAJoueur().getPseudo() + " par " + exclureForm.getPseudo());            
        }
        else if (exclureForm.getAJoueurPartie() != null) {
            // Supprime le joueur de la liste
            env.sortieJoueur(exclureForm.getAJoueurPartie());
        	errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("exclure.aire.ok"));
            log.info("Exclusion de l'aire de : " + exclureForm.getAJoueurPartie().getPseudo() + " par " + exclureForm.getPseudo());            
        }
        else {
        	errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("exclure.joueurManquant"));
        }
        
        // Maj après pour tenir compte de l'exclusion
        exclureForm.setLstJoueur((Vector<JoueurBean>) env.getLstJoueur().clone());
        
        // TODO Envoi sur URL exclue.php?pseudo=...
        
        saveErrors(request, errors);
        return mapping.findForward("page");
	}
	
	
	/* (non-Javadoc)
	 * @see com.increg.game.struts.AdminAction#exclure(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward exPartie(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ExclureForm exclureForm = (ExclureForm) form;
		ActionMessages errors = new ActionMessages();

        GameEnvironment env = (GameEnvironment) getServlet().getServletContext().getAttribute("Env");
		DBSession dbConnect = (DBSession) request.getAttribute("DBSession");

        Vector<PartieBean> lstToutesParties = (Vector<PartieBean>) env.getLstPartie().clone();
        
        initData(exclureForm, dbConnect, lstToutesParties);
            
        if ((exclureForm.getAJoueur() != null) && (exclureForm.getAPartie() != null)) {
            // Le joueur sort de la salle
            env.joueurSeLeve(exclureForm.getAPartie().getMyPartie(), exclureForm.getAJoueur());
            exclureForm.getLstPartie().remove(exclureForm.getAPartie());
    
        	errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("exclure.partie.ok"));
            log.info("Exclusion de la partie " + exclureForm.getAPartie().getMyPartie().getTitre() + "("
            		+ exclureForm.getAPartie().getMyPartie().getIdentifiant() + ") de : " 
            		+ exclureForm.getAJoueur().getPseudo() + " par "
            		+ exclureForm.getPseudo());
        }
        else if ((exclureForm.getAJoueurPartie() != null) && (exclureForm.getAPartieComp() != null)) {
            // Le joueur sort de la salle
            env.joueurSeLeve(exclureForm.getAPartieComp().getMyPartie(), exclureForm.getAJoueurPartie());
            exclureForm.getLstPartie().remove(exclureForm.getAPartieComp());
    
        	errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("exclure.partie.ok"));
            log.info("Exclusion de la partie " + exclureForm.getAPartieComp().getMyPartie().getTitre() + "("
            		+ exclureForm.getAPartieComp().getMyPartie().getIdentifiant() + ") de : "
            		+ exclureForm.getAJoueurPartie().getPseudo() + " par "
            		+ exclureForm.getPseudo());
        }
        else {
        	errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("exclure.JoueurSalleManquant"));
        }
        

        // Maj après pour tenir compte de l'exclusion
        exclureForm.setLstJoueur((Vector<JoueurBean>) env.getLstJoueur().clone());
        
        // TODO Envoi sur URL exclue.php?pseudo=...
        
        saveErrors(request, errors);
        return mapping.findForward("page");
	}

	/**
	 * Initialise les données listes et entités
	 * @param exclureForm
	 * @param dbConnect
	 * @param lstToutesParties
	 */
	private void initData(ExclureForm exclureForm, DBSession dbConnect, Vector<PartieBean> lstToutesParties) {
		if (!StringUtils.isEmpty(exclureForm.getJoueur())) {
            exclureForm.setAJoueur(JoueurBean.getJoueurBeanFromPseudo(dbConnect, exclureForm.getJoueur()));
        }
        if (exclureForm.getAJoueur() != null) {
            // Chargement de la liste de ses parties
            Enumeration<PartieBean> enumToutesParties = lstToutesParties.elements();
            while (enumToutesParties.hasMoreElements()) {
                PartieBean aPartie2 = enumToutesParties.nextElement();
                
                if (aPartie2.getMyPartie().joueurVoitPartie(exclureForm.getAJoueur())) {
                    exclureForm.getLstPartie().add(aPartie2);
                    
                    if (!StringUtils.isEmpty(exclureForm.getPartie()) 
                    		&& (aPartie2.getMyPartie().getTitre().equals(exclureForm.getPartie()))) {
                        exclureForm.setAPartie(aPartie2);
                    }
                }
            }
        }

        if (!StringUtils.isEmpty(exclureForm.getPartieComp())) {
            // Chargement de la liste de ses parties
            Enumeration<PartieBean> enumToutesParties = lstToutesParties.elements();
            while (enumToutesParties.hasMoreElements()) {
                PartieBean aPartie2 = enumToutesParties.nextElement();
                
                if (aPartie2.getMyPartie().getTitre().equals(exclureForm.getPartieComp())) {
                    exclureForm.setAPartieComp(aPartie2);
                }
            }
        }
        if (exclureForm.getAPartieComp() != null) {
            // Chargement de la liste des joueurs de la partie
            Partie aPartie2 = exclureForm.getAPartieComp().getMyPartie();
            for (int i = 0; i < aPartie2.getParticipant().length; i++) {
                if (aPartie2.getParticipant(i) != null) {
                	exclureForm.getLstJoueurPartie().add(aPartie2.getParticipant(i));
                }
            }
            exclureForm.getLstJoueurPartie().addAll(aPartie2.getSpectateurs());
        }
        if (!StringUtils.isEmpty(exclureForm.getJoueurPartie())) {
        	exclureForm.setAJoueurPartie(JoueurBean.getJoueurBeanFromPseudo(dbConnect, exclureForm.getJoueurPartie()));
        }
        exclureForm.setLstPartieComp(lstToutesParties);
	}

	/* (non-Javadoc)
	 * @see com.increg.game.struts.AdminAction#menu(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward menu(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward("menu");
	}

}
