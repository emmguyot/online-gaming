/*
 * Created on 9 mai 2003
 *
 */
package com.increg.game.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

import com.increg.game.client.AireMainModel;
import com.increg.game.client.PerfoMeter;

/**
 * @author Manu
 *
 * Classe permettant de choisir les options de l'aire 
 */
public class AireOptions extends JDialog implements ActionListener {

    /**
     * Modele de l'aire
     */
    protected AireMainModel aire;
         
    /**
     * Bouton par défaut = Ok
     */
    protected JButton okButton;

    /**
     * Bouton annuler
     */
    protected JButton annulerButton;

    /**
     * Images animées
     */
    protected JCheckBox affichageAnime;

    /**
     * Constructeur : A partie d'une partie existante
     * @param a aire d'appel
     */
    public AireOptions (AireMainModel a) {

        // initialise les attributs
        aire = a;
        
        setTitle("Options de l'aire");
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Ajoute les éléments à la fenêtre
        addEveryComponents();
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        // Ajoute la gestion des événements
        
        // Cette fenêtre n'est pas modale
        setModal(false);
        setResizable(false);
        setVisible(true);
    }
    
    /**
     * Ajoute tous les composants d'affichage
     *
     */
    public void addEveryComponents() {
        
        getContentPane().add(Box.createVerticalStrut(12));
        getContentPane().add(getLibDialogBox());
        getContentPane().add(Box.createVerticalStrut(11));
        getContentPane().add(getAffichageAnimeBox());
        getContentPane().add(Box.createVerticalStrut(17));
        getContentPane().add(getActionBox());
        getContentPane().add(Box.createVerticalStrut(11));
    }

    /**
     * @return Box contenant les boutons d'actions
     */
    private Box getActionBox() {
        Box actionBox = Box.createHorizontalBox();
        okButton = new JButton("Ok");
        okButton.setFont(aire.getSkinConfig().getRFont().deriveFont(1));
        annulerButton = new JButton("Annuler");
        annulerButton.setFont(aire.getSkinConfig().getRFont().deriveFont(1));
        
        actionBox.add(Box.createGlue());
        actionBox.add(okButton);
        actionBox.add(Box.createHorizontalStrut(20));
        actionBox.add(annulerButton);
        actionBox.add(Box.createHorizontalStrut(11));

        // Ajoute la gestion des événements
        okButton.addActionListener(this);
        annulerButton.addActionListener(this);
        
        return actionBox;
    }

    /**
     * @return Box contenant la phrase d'intro
     */
    private Box getLibDialogBox() {
        Box libDialogBox = Box.createHorizontalBox();
        JLabel label = new JLabel("Définissez les options de l'aire : ");
        label.setFont(aire.getSkinConfig().getRFont());
        
        libDialogBox.add(Box.createHorizontalStrut(12));
        libDialogBox.add(label);
        libDialogBox.add(Box.createHorizontalStrut(11));

        return libDialogBox;
    }


    /**
     * @return Box contenant les éléments de saisie du nom de la partie
     */
    private Component getAffichageAnimeBox() {
        Box nomPartieBox = Box.createHorizontalBox();

        affichageAnime = new JCheckBox("Affichage des images animées");
        affichageAnime.setFont(aire.getSkinConfig().getRFont());
        
        nomPartieBox.add(Box.createHorizontalStrut(24));
        nomPartieBox.add(affichageAnime);
        nomPartieBox.add(Box.createGlue());

        // Initialisation
        affichageAnime.setSelected(PerfoMeter.isHautePerf());
        
        return nomPartieBox;
    }

    /* ***************************************
     * Gestion des événements
     * *************************************** */
    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == okButton) {
            // Affectation des options
            if (affichageAnime.isSelected()) {
                PerfoMeter.forceHautePerf();
            }
            else {
                PerfoMeter.forceFaiblePerf();
            }
            aire.resetSmiley();
            removeAll();
            dispose();
        }
        else if (e.getSource() == annulerButton) {
            // Fermeture de la fenêtre
            removeAll();
            dispose();
        }
        
    }

    /**
     * @see java.awt.Container#removeAll()
     */
    public void removeAll() {

        if (okButton != null) {
            okButton.removeActionListener(this);
        }
        if (annulerButton != null) {
            annulerButton.removeActionListener(this);
        }
        super.removeAll();
    }

}
