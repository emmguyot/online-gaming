/*
 * Created on 10 juin 2003
 *
 */
package com.increg.game.ui.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.HeadlessException;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.DefaultColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Manu
 *
 * Fenêtre de choix de couleur
 */
public class ChoixCouleur extends JFrame {

    /**
     * Composant de choix de couleur
     */    
    protected JColorChooser chooser;

    /**
     * Panel de prévisualisation
     */
    protected PreviewPanel panel;

    /**
     * Parent de cette fenêtre
     */
    protected ChangeListener parent;

    /**
     * @param aParent Parent à l'écoute des modifs
     * @param titre Titre de la fenêtre
     * @param currentColor Couleur actuelle
     * @throws HeadlessException .
     */ 
    public ChoixCouleur(ChangeListener aParent, String titre, String currentColor) throws HeadlessException {
        super(titre);
        
        chooser = new JColorChooser(Color.decode(currentColor));
        panel = new PreviewPanel();
        parent = aParent;
        chooser.setPreviewPanel(panel);

        AbstractColorChooserPanel[] panels = chooser.getChooserPanels();
        // Ne conserve que le premier onglet
        for (int i = 1; i < panels.length; i++) {
            chooser.removeChooserPanel(panels[i]);
        }

        chooser.getSelectionModel().addChangeListener(parent);
        chooser.getSelectionModel().addChangeListener(panel);
        chooser.setColor(Color.decode(currentColor));

        setContentPane(chooser);
        setResizable(false);
        pack();

    }


    /**
     * @return Couleur sélectionnée au format #RRGGBB
     */
    public String getColor() {
        String chCouleur = "#";
        chCouleur += toHex2digit(chooser.getColor().getRed());
        chCouleur += toHex2digit(chooser.getColor().getGreen());
        chCouleur += toHex2digit(chooser.getColor().getBlue());
        return chCouleur;
    }

    /**
     * Converti un nombre en hexa sur 2 digits
     * @param v Valeur à convertir
     * @return chaine hexa
     */
    private String toHex2digit (int v) {
        String octet = "0" + Integer.toHexString(v);
        
        return octet.substring(octet.length() - 2);
    }
    
    /**
     * @author Manu
     *
     * Panel de prévisu 
     */
    public class PreviewPanel extends JPanel implements ChangeListener {
        
        /**
         * Texte à afficher
         */
        protected JLabel texte;
        
        /**
         * Constructeur
         */
        public PreviewPanel() {
            super(new BorderLayout());
            setBackground(Color.WHITE);
            texte = new JLabel("                Votre message                ");
            add(texte);
        }

        /**
         * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
         */
        public void stateChanged(ChangeEvent e) {
            texte.setForeground(((DefaultColorSelectionModel) e.getSource()).getSelectedColor());
        }
    }
    /**
     * @see java.awt.Container#removeAll()
     */
    public void removeAll() {
        if (chooser != null) {
            chooser.getSelectionModel().removeChangeListener(parent);
            chooser.getSelectionModel().removeChangeListener(panel);
        }
        super.removeAll();
    }

}
