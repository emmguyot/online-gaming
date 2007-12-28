/*
 * Created on 10 juin 2003
 *
 */
package com.increg.game.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

import com.increg.game.client.AireMainModel;
import com.increg.game.client.Joueur;

/**
 * @author Manu
 *
 * Permet d'envoyer des messages secrets...
 */
public class SecretChat extends JDialog implements ActionListener {

    /**
     * Case à cocher indiquant si la sélection du destinataire vaut pour plusieurs messages
     */
    private JCheckBox oneMessageOnly;

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
     * Liste des joueurs
     */
    protected JComboBox joueur;

    /**
     * Liste des joueurs
     */
    protected TreeSet<String> lstJoueur;

    /**
     * Modèle contenant les données d'échange
     */
    protected SecretChatModel model;

    /**
     * @param a Aire
     * @param aLstJoueur liste des joueurs
     */
    public SecretChat(AireMainModel a, Vector<Joueur> aLstJoueur) {
        super();

        // Sauvegarde les attributs
        aire = a;
        lstJoueur = new TreeSet<String>();
        for (int i = 0; i < aLstJoueur.size(); i++) {
            lstJoueur.add(aLstJoueur.get(i).getPseudo());
        }

        setTitle("Message secret");
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Ajoute les éléments à la fenêtre
        addEveryComponents();
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        model = new SecretChatModel();

        // Ajoute la gestion des événements

        // Cette fenêtre n'est pas modale
        setModal(true);
        setResizable(false);
    }

    /**
     * Ajoute tous les composants d'affichage
     *
     */
    public void addEveryComponents() {

        getContentPane().add(Box.createVerticalStrut(12));
        getContentPane().add(getJoueurBox());
        getContentPane().add(Box.createVerticalStrut(11));
        getContentPane().add(getOneMessageOnlyBox());
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
     * @return Box contenant les éléments de saisie du joueur destinataire
     */
    private Box getJoueurBox() {
        Box joueurBox = Box.createHorizontalBox();
        JLabel label = new JLabel();
        label.setFont(aire.getSkinConfig().getRFont());
        label.setText("Destinataire : ");

        joueur = new JComboBox(lstJoueur.toArray());
        joueur.setFont(aire.getSkinConfig().getRFont());
        joueur.setMaximumSize(new Dimension(120, 20));
        joueur.setPreferredSize(new Dimension(120, 20));

        joueurBox.add(Box.createHorizontalStrut(24));
        joueurBox.add(label);
        joueurBox.add(joueur);
        joueurBox.add(Box.createGlue());

        return joueurBox;
    }

    /**
     * @return Box contenant les éléments de saisie du type de belote
     */
    private Box getOneMessageOnlyBox() {
        Box oneMessageOnlyBox = Box.createHorizontalBox();
        oneMessageOnly = new JCheckBox("Uniquement le prochain message", true);
        oneMessageOnly.setFont(aire.getSkinConfig().getRFont());

        oneMessageOnlyBox.add(Box.createHorizontalStrut(24));
        oneMessageOnlyBox.add(oneMessageOnly);
        oneMessageOnlyBox.add(Box.createHorizontalStrut(11));

        return oneMessageOnlyBox;
    }

    /**
     * @return Modèle contenant les données d'échange
     */
    public SecretChatModel getModel() {
        return model;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == okButton) {

            model.setPseudo((String) joueur.getSelectedItem());
            model.setOneOnly(oneMessageOnly.isSelected());

            dispose();
        }
        else if (e.getSource() == annulerButton) {
            // Fermeture de la fenêtre
            model.setPseudo(null);
            model.setOneOnly(false);
            dispose();
        }

    }

    /**
     *
     * @author Manu
     *
     * Model de la fenêtre
     */
    public class SecretChatModel {

        /**
         * Joueur destinataire
         */
        protected String pseudo;

        /**
         * Un seul message à ce destinataire ?
         */
        protected boolean oneOnly;

        /**
         * @return Un seul message à ce destinataire ?
         */
        public boolean isOneOnly() {
            return oneOnly;
        }

        /**
         * @return Joueur destinataire
         */
        public String getPseudo() {
            return pseudo;
        }

        /**
         * @param b Un seul message à ce destinataire ?
         */
        public void setOneOnly(boolean b) {
            oneOnly = b;
        }

        /**
         * @param string Joueur destinataire
         */
        public void setPseudo(String string) {
            pseudo = string;
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
