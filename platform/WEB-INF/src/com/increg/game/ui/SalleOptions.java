/*
 * Created on 9 mai 2003
 *
 */
package com.increg.game.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.increg.game.client.AireMainModel;
import com.increg.game.client.Tournoi;
import com.increg.game.client.belote.PartieBelote;
import com.increg.game.client.belote.PartieBeloteClassique;
import com.increg.game.client.belote.PartieBeloteModerne;

/**
 * @author Manu
 *
 * Classe permettant de choisir ou d'afficher les options d'une nouvelle partie 
 */
public class SalleOptions extends JDialog implements ActionListener {

    /**
     * Partie correspondante aux options
     */
    protected PartieBelote myPartie;

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
     * Nom de la partie
     */
    protected JTextField nomPartie;

    /**
     * Radio pour la belote moderne
     */
    protected JRadioButton typeBeloteModerne;

    /**
     * Radio pour la belote classique
     */
    protected JRadioButton typeBeloteClassique;

    /**
     * Utilisation des annonces
     */
    protected JCheckBox choixAnnonce;

    /**
     * Tournoi activé ?
     */
    protected JCheckBox tournoi;

    /**
     * Mot de passe pour les parties privées
     */
    protected JTextField motDePasse;

    /**
     * Constructeur : A partie d'une partie existante
     * @param a aire d'appel
     * @param aPartie Partie pour laquelle les options vont être définies ou affichées
     */
    public SalleOptions (AireMainModel a, PartieBelote aPartie) {

        // initialise les attributs
        myPartie = aPartie;
        aire = a;
        
        setTitle("Nouvelle partie");
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Ajoute les éléments à la fenêtre
        addEveryComponents();
        pack();
        setLocationRelativeTo(null);
        if (myPartie.getIdentifiant() > 0) {
            // Simple visu
            getRootPane().setDefaultButton(annulerButton);
        }
        else {
            getRootPane().setDefaultButton(okButton);
        }
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
        getContentPane().add(getNomPartieBox());
        getContentPane().add(Box.createVerticalStrut(11));
        getContentPane().add(getChoixTypeBeloteBox());
        getContentPane().add(getChoixAnnonceBox());
        getContentPane().add(Box.createVerticalStrut(11));
        getContentPane().add(getMotDePasseBox());
        getContentPane().add(Box.createVerticalStrut(11));
        getContentPane().add(getTournoiBox());
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
        if (myPartie.getIdentifiant() == 0) {
            // Définition des params
            actionBox.add(okButton);
            actionBox.add(Box.createHorizontalStrut(20));
        }
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
        JLabel label = new JLabel("Sélectionnez les options de cette partie : ");
        label.setFont(aire.getSkinConfig().getRFont());
        
        libDialogBox.add(Box.createHorizontalStrut(12));
        libDialogBox.add(label);
        libDialogBox.add(Box.createHorizontalStrut(11));

        return libDialogBox;
    }

    /**
     * @return Box contenant les éléments de saisie du mot de passe
     */
    private Box getMotDePasseBox() {
        Box motDePasseBox = Box.createHorizontalBox();
        JLabel label = new JLabel("Mot de passe : ");
        label.setFont(aire.getSkinConfig().getRFont());
        motDePasse = new JTextField();
        motDePasse.setFont(aire.getSkinConfig().getRFont());
        motDePasse.setPreferredSize(new Dimension(70, 20));
        motDePasse.setMaximumSize(new Dimension(70, 20));
        
        motDePasseBox.add(Box.createHorizontalStrut(24));
        motDePasseBox.add(label);
        motDePasseBox.add(motDePasse);
        motDePasseBox.add(Box.createGlue());

        // Initialise
        if (myPartie.isPrivate()) {
            motDePasse.setText("******");
        }
        
        return motDePasseBox;
    }

    /**
     * @return Box contenant les éléments de saisie de la case à cocher Annonce ?
     */
    private Box getChoixAnnonceBox() {
        Box choixAnnonceBox = Box.createHorizontalBox();
        choixAnnonce = new JCheckBox("Avec annonces");
        choixAnnonce.setFont(aire.getSkinConfig().getRFont());
        
        choixAnnonceBox.add(Box.createHorizontalStrut(24));
        choixAnnonceBox.add(choixAnnonce);
        choixAnnonceBox.add(Box.createGlue());

        // Initialise
        if (myPartie.isAnnonce()) {
            choixAnnonce.setSelected(true);
        }

        return choixAnnonceBox;
    }

    /**
     * @return Box contenant les éléments de saisie de la case à cocher Tournoi ?
     */
    private Box getTournoiBox() {
        Box tournoiBox = Box.createHorizontalBox();
        tournoi = new JCheckBox("Partie du tournoi");
        tournoi.setFont(aire.getSkinConfig().getRFont());
        
        tournoiBox.add(Box.createHorizontalStrut(24));
        tournoiBox.add(tournoi);
        tournoiBox.add(Box.createGlue());

        // Initialise
        if ((myPartie.getMyTournoi() != null) 
                && (myPartie.getMyTournoi().getIdentifiant() > 0)) {
            tournoi.setSelected(true);
        }

        return tournoiBox;
    }

    /**
     * @return Box contenant les éléments de saisie du type de belote
     */
    private Box getChoixTypeBeloteBox() {
        Box choixTypeBeloteBox = Box.createHorizontalBox();
        ButtonGroup groupRadio = new ButtonGroup();
        typeBeloteModerne = new JRadioButton("Moderne");
        typeBeloteClassique = new JRadioButton("Classique");
        typeBeloteModerne.setFont(aire.getSkinConfig().getRFont());
        typeBeloteClassique.setFont(aire.getSkinConfig().getRFont());
        groupRadio.add(typeBeloteClassique);
        groupRadio.add(typeBeloteModerne);
        
        choixTypeBeloteBox.add(Box.createHorizontalStrut(24));
        choixTypeBeloteBox.add(typeBeloteClassique);
        choixTypeBeloteBox.add(typeBeloteModerne);
        choixTypeBeloteBox.add(Box.createGlue());

        // Initialise
        if (myPartie instanceof PartieBeloteModerne) {
            groupRadio.setSelected(typeBeloteModerne.getModel(), true);
        }
        else {
            groupRadio.setSelected(typeBeloteClassique.getModel(), true);
        }

        return choixTypeBeloteBox;
    }

    /**
     * @return Box contenant les éléments de saisie du nom de la partie
     */
    private Component getNomPartieBox() {
        Box nomPartieBox = Box.createHorizontalBox();
        JLabel label = new JLabel();
        label.setFont(aire.getSkinConfig().getRFont());
        label.setText("Nom de la partie : ");

        nomPartie = new JTextField(myPartie.getTitre());
        nomPartie.setFont(aire.getSkinConfig().getRFont());
        nomPartie.setMaximumSize(new Dimension(100, 20));
        nomPartie.setPreferredSize(new Dimension(100, 20));
        
        nomPartieBox.add(Box.createHorizontalStrut(24));
        nomPartieBox.add(label);
        nomPartieBox.add(nomPartie);
        nomPartieBox.add(Box.createGlue());

        // Initialise
        nomPartie.setText(myPartie.getTitre());
        
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
            // Création réelle de la partie
            if ((typeBeloteModerne.isSelected())
                && (!(myPartie instanceof PartieBeloteModerne))) {
                // Il faut recréer l'objet
                myPartie = new PartieBeloteModerne();
            }
            if ((typeBeloteClassique.isSelected())
                && (!(myPartie instanceof PartieBeloteClassique))) {
                // Il faut recréer l'objet
                myPartie = new PartieBeloteClassique();
            }
            myPartie.setTitre(nomPartie.getText());
            myPartie.setMotDePasse(motDePasse.getText());
            myPartie.setAnnonce(choixAnnonce.isSelected());
            if (tournoi.isSelected()) {
                Tournoi aTournoi = new Tournoi();
                // TODO : Mettre l'identifiant du tournoi
                aTournoi.setIdentifiant(1);
                myPartie.setMyTournoi(aTournoi);
            }
            
            // création par le model
            aire.createMySalle(myPartie);
            
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
