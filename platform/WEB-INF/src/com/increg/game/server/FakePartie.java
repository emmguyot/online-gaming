/*
 * Pseudo partie utilis�e lors des rechargements depuis la base.
 * Cette partie est n�cessairement incompl�te
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
package com.increg.game.server;

import java.util.Map;

import com.increg.game.client.Partie;

/**
 * @author Manu
 *
 */
public class FakePartie extends Partie {

	/**
	 * Constructeur
	 */
	public FakePartie() {
		super();
	}

	/**
	 * @see com.increg.game.client.Partie#impacteCoupe(int)
	 */
	public void impacteCoupe(int pos) {
	}

	/**
	 * @see com.increg.game.client.Partie#setScoreMaxPartie(java.util.Map)
	 */
	public void setScoreMaxPartie(Map<Integer, String> lstParam) {
	}

}
