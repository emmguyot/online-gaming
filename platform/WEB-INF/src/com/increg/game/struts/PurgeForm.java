/*
 * Formulaire pour la purge des données et l'optimisation de la base
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

import java.text.ParseException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 * @author Manu
 *
 */
public class PurgeForm extends AdminForm {

	/**
	 * Date jusqu'à laquelle la purge est faite
	 */
	protected String dtPurge;
	
	/**
	 * Nombre de parties en base
	 */
	protected Integer nbPartieTot;
	
	/**
	 * Nombre de joueurs en base
	 */
	protected Integer nbJoueurs;
	
	/**
	 * Date de la plus vieille partie
	 */
	protected Date dtVieillePartie;
		
	/**
	 * Date du joueur ne s'étant pas connecté depuis le plus de temps
	 */
	protected Date dtVieuxJoueur;

	
	/* (non-Javadoc)
	 * @see com.increg.game.struts.AdminForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		super.reset(arg0, arg1);
		
		dtVieuxJoueur = null;
		dtVieillePartie = null;
		nbJoueurs = null;
		nbPartieTot = null;
		dtPurge = "";
	}

	/* (non-Javadoc)
	 * @see com.increg.game.struts.AdminForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
		ActionErrors errors = super.validate(arg0, arg1);
		
		if (errors.isEmpty()) {
			try {
				Date dt = FORMAT.parse(dtPurge);
			} catch (ParseException e) {
				errors.add("dtPurge", new ActionMessage("errors.invalid", "La date"));
			}
		}

		return errors;
	}

	/**
	 * @return Returns the dtPurge.
	 */
	public String getDtPurge() {
		return dtPurge;
	}

	/**
	 * @param dtPurge The dtPurge to set.
	 */
	public void setDtPurge(String dtPurge) {
		this.dtPurge = dtPurge;
	}

	/**
	 * @return Returns the dtVieillePartie.
	 */
	public Date getDtVieillePartie() {
		return dtVieillePartie;
	}

	/**
	 * @param dtVieillePartie The dtVieillePartie to set.
	 */
	public void setDtVieillePartie(Date dtVieillePartie) {
		this.dtVieillePartie = dtVieillePartie;
	}

	/**
	 * @return Returns the dtVieuxJoueur.
	 */
	public Date getDtVieuxJoueur() {
		return dtVieuxJoueur;
	}

	/**
	 * @param dtVieuxJoueur The dtVieuxJoueur to set.
	 */
	public void setDtVieuxJoueur(Date dtVieuxJoueur) {
		this.dtVieuxJoueur = dtVieuxJoueur;
	}

	/**
	 * @return Returns the nbJoueurs.
	 */
	public Integer getNbJoueurs() {
		return nbJoueurs;
	}

	/**
	 * @param nbJoueurs The nbJoueurs to set.
	 */
	public void setNbJoueurs(Integer nbJoueurs) {
		this.nbJoueurs = nbJoueurs;
	}

	/**
	 * @return Returns the nbPartieTot.
	 */
	public Integer getNbPartieTot() {
		return nbPartieTot;
	}

	/**
	 * @param nbPartieTot The nbPartieTot to set.
	 */
	public void setNbPartieTot(Integer nbPartieTot) {
		this.nbPartieTot = nbPartieTot;
	}
}
