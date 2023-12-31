/*
 * Application lancant le robot de test de l'aire de jeu
 * Copyright (C) 2005-2005 Emmanuel Guyot <See emmguyot on SourceForge>
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
package com.increg.game.client.test;

import com.increg.game.client.AireMainModel;
import com.increg.game.client.Joueur;
import com.increg.game.ui.AireMain;

/**
 * @author guyot_e
 *
 */
public class Robot extends AireMainModel {

	public Robot(AireMain a, Joueur j) {
		super(a, j);
		// TODO Auto-generated constructor stub
	}

	// Gestion des interactions serveurs
	// Multi instance � v�rifier
	// Affichage 'succin' dans AfficheRobot
	// Initiative d'actions (Chat, Jeu, ...)
}
