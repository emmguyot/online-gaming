/*
 * Formulaire pour le paramétrage de l'aire
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

/**
 * @author Manu
 *
 */
public class ParamAireForm extends AdminForm {

	/**
	 * Paramètre modifié
	 */
	protected String cdParam;
	/**
	 * Paramètre modifié
	 */
	protected String libParam;
    /**
     * Valeur du paramètre
     */
	protected String valParam;

    /**
     * Liste des paramètres existants : Instance de la liste = sous classe Param
     */
	protected Vector lstParam;

	/* (non-Javadoc)
	 * @see com.increg.game.struts.AdminForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		super.reset(arg0, arg1);
		
		cdParam = "";
		valParam = "";
		libParam = "";
		lstParam = new Vector();
	}

	/* (non-Javadoc)
	 * @see com.increg.game.struts.AdminForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
		return super.validate(arg0, arg1);
	}

	/**
	 * @return Returns the cdParam.
	 */
	public String getCdParam() {
		return cdParam;
	}

	/**
	 * @param cdParam The cdParam to set.
	 */
	public void setCdParam(String cdParam) {
		this.cdParam = cdParam;
	}

	/**
	 * @return Returns the lstParam.
	 */
	public Vector getLstParam() {
		return lstParam;
	}

	/**
	 * @param lstParam The lstParam to set.
	 */
	public void setLstParam(Vector lstParam) {
		this.lstParam = lstParam;
	}

	/**
	 * @return Returns the libParam.
	 */
	public String getLibParam() {
		return libParam;
	}

	/**
	 * @param libParam The libParam to set.
	 */
	public void setLibParam(String libParam) {
		this.libParam = libParam;
	}

	/**
	 * @return Returns the valParam.
	 */
	public String getValParam() {
		return valParam;
	}

	/**
	 * @param valParam The valParam to set.
	 */
	public void setValParam(String valParam) {
		this.valParam = valParam;
	}

}
