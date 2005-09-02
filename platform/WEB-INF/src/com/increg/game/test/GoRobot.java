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
package com.increg.game.test;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JDialog;

import com.increg.game.ui.AireMain;
import com.increg.game.ui.test.AfficheRobot;

/**
 * @author guyot_e
 * 
 */
public class GoRobot extends AireMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		GoRobot goRobot = new GoRobot();
		goRobot.init();

	}
	
	/* (non-Javadoc)
	 * @see com.increg.game.ui.AireMain#init()
	 */
	public void init() {
		// Définition des logs
        logger = Logger.getLogger("com.increg");
        try {
            // Fait le ménage avant toute autre ajout
            Handler[] tabHandler = logger.getHandlers();
            for (int i = 0; i < tabHandler.length; i++)  {
                if (tabHandler[i] instanceof FileHandler) {
                    FileHandler fh2 = (FileHandler) tabHandler[i];
                    fh2.close();
                    logger.removeHandler(fh2);
                    System.out.println("Fermeture FileHandler");
                }
                else {
                    System.out.println("Handler = " + tabHandler[i].getClass().getName());
                }
            }
            // Ajout notre traitement des log
            fh = new FileHandler("%h/InCrEG_Game.log", 5000000, 1, true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            // Par défaut : Pas trop de log
            logger.setLevel(Level.INFO);
            if (System.getProperty("LogDebug") != null) {
            	// Force le niveau de log
                logger.setLevel(Level.parse(System.getProperty("LogDebug")));
            }
        }
        catch (SecurityException e1) {
            e1.printStackTrace();
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
		
		// Lancement de la fenêtre
		JDialog dlg = new AfficheRobot(this);
	}

	/**
	 * @see java.applet.Applet#getParameter(String)
	 */
	public String getParameter(String name) {
		if (name.equals("config")) {
			return "file://config.xml";
		}
		else {
			logger.log(Level.FINEST, "Demande du parametre " + name);
		}
		return "";
	}

}
