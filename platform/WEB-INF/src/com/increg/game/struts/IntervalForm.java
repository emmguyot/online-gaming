/*
 * One line to give the program's name and a brief idea of what it does.
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 * @author Manu
 *
 */
public class IntervalForm extends AdminForm {

	protected String debut;
	protected String fin;
	protected List lignes;
	/**
	 * @return Returns the debut.
	 */
	public String getDebut() {
		return debut;
	}
	/**
	 * @param debut The debut to set.
	 */
	public void setDebut(String debut) {
		this.debut = debut;
	}
	/**
	 * @return Returns the fin.
	 */
	public String getFin() {
		return fin;
	}
	/**
	 * @param fin The fin to set.
	 */
	public void setFin(String fin) {
		this.fin = fin;
	}
	/* (non-Javadoc)
	 * @see com.increg.game.struts.AdminForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);
		
		debut = FORMAT.format(new Date());
		fin = FORMAT.format(new Date());
		lignes = new ArrayList();
	}
	
	/* (non-Javadoc)
	 * @see com.increg.game.struts.AdminForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = super.validate(mapping, request);

		if (errors.isEmpty()) {
			try {
				Date dtDebut = FORMAT.parse(debut);
				Date dtFin = FORMAT.parse(fin);
				if (dtFin.before(dtDebut)) {
					errors.add("debut", new ActionMessage("error.debutFin"));
				}
			} catch (ParseException e) {
				errors.add("debut", new ActionMessage("errors.invalid", "La date"));
			}
		}
		
		return errors;
	}
	/**
	 * @return Returns the lignes.
	 */
	public List getLignes() {
		return lignes;
	}
	/**
	 * @param lignes The lignes to set.
	 */
	public void setLignes(List lignes) {
		this.lignes = lignes;
	}
	
	
}
