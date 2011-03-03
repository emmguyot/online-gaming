/*
 * Fenêtre de suivi des robots de test de l'aire de jeu
 * Copyright (C) 2001-2005 Emmanuel Guyot <See emmguyot on SourceForge>
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
package com.increg.game.ui.test;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.increg.game.test.GoRobot;

/**
 * @author guyot_e
 *
 */
public class AfficheRobot extends JDialog implements ActionListener, KeyListener {

	/**
	 * Classe parente
	 */
	protected GoRobot parent;
	protected JButton pauseButton;
	protected JButton quitButton;
	private JLabel nbRobotLabel;
	private JTextField nbRobotEntry;
	private JLabel nbRobotActif2Label;
	private JLabel nbRobotActifLabel;
	
	/**
	 * Constructeur du dialogue des parametres et suivi des robots
	 * @param robot
	 */
	public AfficheRobot(GoRobot robot) {
		parent = robot;
		
        setTitle("Robot de test de l'aire de jeu InCrEG");
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Ajoute les éléments à la fenêtre
        addEveryComponents();
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
        
        // Ajoute la gestion des événements
        
        // Cette fenêtre n'est pas modale
        setModal(false);
        setResizable(true);
        setVisible(true);
	}

    /**
     * Ajoute tous les composants d'affichage
     *
     */
    public void addEveryComponents() {
        
        getContentPane().add(Box.createVerticalStrut(12));
        getContentPane().add(getNbRobotsBox());
        getContentPane().add(Box.createVerticalStrut(11));
        //getContentPane().add(getLogOptionBox());
        getContentPane().add(Box.createVerticalStrut(11));
        //getContentPane().add(getLogBox());
        getContentPane().add(Box.createVerticalStrut(17));
        getContentPane().add(getActionBox());
        getContentPane().add(Box.createVerticalStrut(11));
    }

	/**
	 * Construit la ligne de saisie des robots actifs
	 * @return
	 */
    private Box getNbRobotsBox() {
        Box box = Box.createHorizontalBox();
		
        nbRobotLabel = new JLabel("Nombre de robots :");
		nbRobotEntry = new JTextField("0");
        nbRobotActif2Label = new JLabel("Actifs :");
        nbRobotActifLabel = new JLabel("0");
        
        box.add(Box.createHorizontalStrut(11));
        box.add(nbRobotLabel);
        box.add(Box.createHorizontalStrut(11));
        box.add(nbRobotEntry);
        box.add(Box.createHorizontalStrut(11));
        box.add(nbRobotActif2Label);
        box.add(Box.createHorizontalStrut(11));
        box.add(nbRobotActifLabel);
        box.add(Box.createGlue());

        // Ajoute la gestion des événements
        nbRobotEntry.addKeyListener(this);
		
		return box;
	}

	/**
     * @return Box contenant les boutons d'actions
     */
    private Box getActionBox() {
        Box actionBox = Box.createHorizontalBox();
        pauseButton = new JButton("Pause");
        quitButton = new JButton("Quitter");
        
        actionBox.add(Box.createGlue());
        actionBox.add(pauseButton);
        actionBox.add(Box.createHorizontalStrut(20));
        actionBox.add(quitButton);
        actionBox.add(Box.createHorizontalStrut(11));

        // Ajoute la gestion des événements
        pauseButton.addActionListener(this);
        quitButton.addActionListener(this);
        
        return actionBox;
    }

	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == quitButton) {
			dispose();
			System.exit(0);
			return;
		}
		else if (e.getSource() == pauseButton) {
			// TODO Mets la pause
		}
		
	}

	public void keyPressed(KeyEvent e) {
		// Rien
	}

	public void keyReleased(KeyEvent e) {
		// Rien
		
	}

	public void keyTyped(KeyEvent e) {
		// Traitement du CR : Validation de la saisie
		
	}

	/**
	 * Fonctionnalités :
	 * - Détermine le nb de robot à lancer
	 * - Affiche un log des actions robots (Redirection possible / figer possible)
	 * - Lancement des robots proprement dits
	 * - Choix du scénario : classes dérivées des robots
	 */
	
}
