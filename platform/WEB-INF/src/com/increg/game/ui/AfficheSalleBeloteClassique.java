/*
 * Created on 6 juin 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.ui;

import java.awt.Insets;
import java.awt.Rectangle;
import java.net.MalformedURLException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import com.increg.game.client.SalleModel;

/**
 * @author Manu
 *
 * Affichage d'une salle de belote classique
 */
public class AfficheSalleBeloteClassique extends AfficheSalle {

    /**
     * Constructeur
     * @param s Salle
     * @param w Largeur de la fen�tre
     * @param h Hauteur de la fen�tre
     */
    public AfficheSalleBeloteClassique(SalleModel s, int w, int h) {
        super(s, w, h);
        boutonsPrise = new JButton[getNbBoutonPrise()];
    }

    /**
     * Cr�ation des boutons de prise
     */
    protected void addBoutonsPrise() {
        String[] texte = {"non.gif", "pique.gif", "coeur.gif", "trefle.gif", "carreau.gif"};
        String[] hint = {"Passe", "Pique", "Coeur", "Tr�fle", "Carreau"};
        
        Vector bounds = salle.getSkinConfig().getSBoutonPrise();
        
        /**
         * NB_BOUTON_PRISE boutons de prise pour la belote classique
         */
        for (int i = 0; i < getNbBoutonPrise(); i++) {
            boutonsPrise[i] = new JButton();
            try {
                boutonsPrise[i].setIcon(salle.getRelativeImageIcon("/images/" + texte[i]));
            }
            catch (MalformedURLException e) {
                salle.getLogger().severe("Chemin de la carte invalide : " + "/images/" + texte[i]);
            }
            boutonsPrise[i].setBackground(salle.getSkinConfig().getRBoutonGlobalColor());
            boutonsPrise[i].setToolTipText(hint[i]);
            boutonsPrise[i].setBounds((Rectangle) bounds.get(i));
            boutonsPrise[i].getInsets(new Insets(0, 0, 0, 0));
            boutonsPrise[i].setVisible(false);
            if (!salle.getSkinConfig().isRBoutonGlocalBorder()) {
                boutonsPrise[i].setBorder(BorderFactory.createEmptyBorder());
            }
            boutonsPrise[i].addActionListener(this);
            add(boutonsPrise[i]);
        }
    }

    /**
     * @return Nombre de boutons pour la prise
     */
    protected int getNbBoutonPrise() {
        return 5;
    }


    /**
     * @see java.awt.Container#removeAll()
     */
    public void removeAll() {
        for (int i = 0; i < getNbBoutonPrise(); i++) {
            if (boutonsPrise[i] != null) {
                boutonsPrise[i].removeActionListener(this);
            }
        }
        super.removeAll();
    }

}
