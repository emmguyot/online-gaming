/*
 * Formulaire pour l'exclusion de joueurs de l'aire
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

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.increg.game.bean.JoueurBean;
import com.increg.game.bean.PartieBean;

/**
 * @author Manu
 *
 */
public class ExclureForm extends AdminForm {

	/**
	 * Joueur sélectionné parmis tous les joueurs
	 */
    String joueur;
    /**
     * Partie sélectionnée pour le joueur
     */
    String partie;

    /**
     * Partie sélectionnée parmis toutes
     */
    String partieComp;
    /**
     * Joueur sélectionné dans les parties
     */
    String joueurPartie;

    /**
     * Joueurs connectés
     */
    Vector lstJoueur;
    /**
     * Joueur sélectionné
     */
    JoueurBean aJoueur;
    /**
     * Parties visibles par le joueur
     */
    Vector lstPartie;
    /**
     * Partie sélectionnée
     */
    PartieBean aPartie;

    /**
     * Liste des parties
     */
    Vector lstPartieComp;
    /**
     * Partie sélectionnée
     */
    PartieBean aPartieComp;
    /**
     * Liste des joueurs de la partie
     */
    Vector lstJoueurPartie;
    /**
     * Joueur sélectionné dans la partie
     */
    JoueurBean aJoueurPartie;

	/* (non-Javadoc)
	 * @see com.increg.game.struts.AdminForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		// TODO Auto-generated method stub
		super.reset(arg0, arg1);
		
		lstJoueur = new Vector();
		aJoueur = null;
		lstPartie = new Vector();
		aPartie = null;
		lstPartieComp = new Vector();
		aPartieComp = null;
		lstJoueurPartie = new Vector();
		aJoueurPartie = null;
		
		joueur = "";
		partie = "";
		partieComp = "";
		joueurPartie = "";
	}

	/* (non-Javadoc)
	 * @see com.increg.game.struts.AdminForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
		// TODO Auto-generated method stub
		return super.validate(arg0, arg1);
	}

	/**
	 * @return Returns the aJoueur.
	 */
	public JoueurBean getAJoueur() {
		return aJoueur;
	}

	/**
	 * @param joueur The aJoueur to set.
	 */
	public void setAJoueur(JoueurBean joueur) {
		aJoueur = joueur;
	}

	/**
	 * @return Returns the aJoueurPartie.
	 */
	public JoueurBean getAJoueurPartie() {
		return aJoueurPartie;
	}

	/**
	 * @param joueurPartie The aJoueurPartie to set.
	 */
	public void setAJoueurPartie(JoueurBean joueurPartie) {
		aJoueurPartie = joueurPartie;
	}

	/**
	 * @return Returns the aPartie.
	 */
	public PartieBean getAPartie() {
		return aPartie;
	}

	/**
	 * @param partie The aPartie to set.
	 */
	public void setAPartie(PartieBean partie) {
		aPartie = partie;
	}

	/**
	 * @return Returns the aPartieComp.
	 */
	public PartieBean getAPartieComp() {
		return aPartieComp;
	}

	/**
	 * @param partieComp The aPartieComp to set.
	 */
	public void setAPartieComp(PartieBean partieComp) {
		aPartieComp = partieComp;
	}

	/**
	 * @return Returns the joueur.
	 */
	public String getJoueur() {
		return joueur;
	}

	/**
	 * @param joueur The joueur to set.
	 */
	public void setJoueur(String joueur) {
		this.joueur = joueur;
	}

	/**
	 * @return Returns the joueurPartie.
	 */
	public String getJoueurPartie() {
		return joueurPartie;
	}

	/**
	 * @param joueurPartie The joueurPartie to set.
	 */
	public void setJoueurPartie(String joueurPartie) {
		this.joueurPartie = joueurPartie;
	}

	/**
	 * @return Returns the lstJoueur.
	 */
	public Vector getLstJoueur() {
		return lstJoueur;
	}

	/**
	 * @param lstJoueur The lstJoueur to set.
	 */
	public void setLstJoueur(Vector lstJoueur) {
		this.lstJoueur = lstJoueur;
	}

	/**
	 * @return Returns the lstJoueurPartie.
	 */
	public Vector getLstJoueurPartie() {
		return lstJoueurPartie;
	}

	/**
	 * @param lstJoueurPartie The lstJoueurPartie to set.
	 */
	public void setLstJoueurPartie(Vector lstJoueurPartie) {
		this.lstJoueurPartie = lstJoueurPartie;
	}

	/**
	 * @return Returns the lstPartie.
	 */
	public Vector getLstPartie() {
		return lstPartie;
	}

	/**
	 * @param lstPartie The lstPartie to set.
	 */
	public void setLstPartie(Vector lstPartie) {
		this.lstPartie = lstPartie;
	}

	/**
	 * @return Returns the lstPartieComp.
	 */
	public Vector getLstPartieComp() {
		return lstPartieComp;
	}

	/**
	 * @param lstPartieComp The lstPartieComp to set.
	 */
	public void setLstPartieComp(Vector lstPartieComp) {
		this.lstPartieComp = lstPartieComp;
	}

	/**
	 * @return Returns the partie.
	 */
	public String getPartie() {
		return partie;
	}

	/**
	 * @param partie The partie to set.
	 */
	public void setPartie(String partie) {
		this.partie = partie;
	}

	/**
	 * @return Returns the partieComp.
	 */
	public String getPartieComp() {
		return partieComp;
	}

	/**
	 * @param partieComp The partieComp to set.
	 */
	public void setPartieComp(String partieComp) {
		this.partieComp = partieComp;
	}

}
