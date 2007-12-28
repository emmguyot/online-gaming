/*
 * Created on 18 mai 2003
 *
 */
package com.increg.game.ui.component.chat;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.net.MalformedURLException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.increg.game.client.AireMainModel;
import com.increg.game.client.Chat;
import com.increg.game.client.SalleModel;
import com.increg.game.client.SmileySon;
import com.increg.game.ui.component.ImageComboBox;

/**
 * @author Manu
 *
 * Classe regroupant l'ensemble des contrôles du chat d'une partie
 */
public class ChatPanelPartie extends ChatPanel {

    /**
     * Salle associée au chat
     */
    protected SalleModel salle;
   
    /**
     * Bouton pour insérer un son
     */
    protected ImageComboBox comboChatSon;
    
    /**
     * Constructeur
     * @param aParent parent d'un point de vue graphique
     * @param a Aire
     * @param aSalle Partie liée au Chat
     */
    public ChatPanelPartie(JPanel aParent, AireMainModel a, SalleModel aSalle) {
        super(aParent, a);
        
        // Conserve les paramètres
        salle = aSalle;
    }
    
    /**
     * Positionne les propriétés potentiellement variantes
     *
     */
    protected void setChatEntryProperties() {
        chatEntry.setFont(aire.getSkinConfig().getSFont().deriveFont(Font.PLAIN, aire.getSkinConfig().getSFont().getSize2D() - 2));
        chatEntry.setBounds(aire.getSkinConfig().getSChatSaisie());
        chatEntry.setBackground(aire.getSkinConfig().getSChatSaisieBgColor());
    }
    
    /**
     * Positionne les propriétés potentiellement variantes
     *
     */
    protected void setChatSendProperties() {
        try {
            boutonChatSend.setIcon(aire.getImageIcon(aire.getSkinConfig().getSChatSendImg()));
        }
        catch (MalformedURLException e) {
            aire.getLogger().severe("Image send Chat erronée");
        }
        boutonChatSend.setBounds(aire.getSkinConfig().getSChatSend());
        boutonChatSend.setBackground(aire.getSkinConfig().getRBoutonGlobalColor());
        if (!aire.getSkinConfig().isRBoutonGlocalBorder()) {
            boutonChatSend.setBorder(BorderFactory.createEmptyBorder());
        }
    }

    /**
     * Positionne les propriétés potentiellement variantes
     *
     */
    protected void setChatSmileyProperties() {
        try {
            comboChatSmiley.setIcon(aire.getImageIcon(aire.getSkinConfig().getSChatSmileyImg()));
        }
        catch (MalformedURLException e) {
            aire.getLogger().severe("Image couleur Chat erronée");
        }
        comboChatSmiley.setBounds(aire.getSkinConfig().getSChatSmiley());
        comboChatSmiley.setBackground(aire.getSkinConfig().getRBoutonGlobalColor());
        if (!aire.getSkinConfig().isRBoutonGlocalBorder()) {
            comboChatSmiley.setBorder(BorderFactory.createEmptyBorder());
        }
    }
    
    /**
     * Positionne les propriétés potentiellement variantes
     *
     */
    protected void setChatSonProperties() {
        try {
            comboChatSon.setIcon(aire.getImageIcon(aire.getSkinConfig().getSChatSonImg()));
        }
        catch (MalformedURLException e) {
            aire.getLogger().severe("Image son Chat erronée");
        }
        comboChatSon.setBounds(aire.getSkinConfig().getSChatSon());
        comboChatSon.setBackground(aire.getSkinConfig().getRBoutonGlobalColor());
        if (!aire.getSkinConfig().isRBoutonGlocalBorder()) {
            comboChatSon.setBorder(BorderFactory.createEmptyBorder());
        }
    }
    
    /**
     * Positionne les propriétés potentiellement variantes
     *
     */
    protected void setChatCouleurProperties() {
        try {
            boutonChatCouleur.setIcon(aire.getImageIcon(aire.getSkinConfig().getSChatCouleurImg()));
        }
        catch (MalformedURLException e) {
            aire.getLogger().severe("Image couleur Chat erronée");
        }
        boutonChatCouleur.setBounds(aire.getSkinConfig().getSChatCouleur());
        boutonChatCouleur.setBackground(aire.getSkinConfig().getRBoutonGlobalColor());
        if (!aire.getSkinConfig().isRBoutonGlocalBorder()) {
            boutonChatCouleur.setBorder(BorderFactory.createEmptyBorder());
        }
    }
    
    /**
     * Positionne les propriétés potentiellement variantes
     *
     */
    protected void setScrollChatProperties() {
        baliseFont = "<font face=\"" + aire.getSkinConfig().getSFont().getFamily() 
                                + "\" size=\"3\">";
        scrollChat.setBounds(aire.getSkinConfig().getSChatView());
        chatView.setBackground(aire.getSkinConfig().getSChatViewBgColor());
    }

    /**
     * @see com.increg.game.ui.component.ChatPanel#getChatSecret()
     */
    protected JButton getChatSecret() {
        return null;
    }


    /**
     * Construit le bouton son
     * @return bouton créé
     */
    protected ImageComboBox getChatSon() {
        Vector<SmileySon> son = Chat.getSon();
        String[] items = new String[son.size()];
        String[] url = new String[son.size()];
        int cpt = 0;
        for (int i = 0; i < son.size(); i++) {
            SmileySon aSon = son.get(i);

            items[cpt] = aSon.getCode();
            url[cpt] = aSon.getImage();
            cpt++;
        }
          
        comboChatSon = new ImageComboBox(items, url, aire, "Sons");
        setChatSonProperties();
        comboChatSon.setMargin(new Insets(0, 0, 0, 0));
        comboChatSon.setToolTipText("Sons");
        comboChatSon.addActionListener(this);
        
        return comboChatSon;
    }

    /**
     * Positionne les propriétés pour attirer l'attention du Joueur
     * @param isWarning True pour attirer l'attention, false sinon
     */
    public void setWarning(boolean isWarning) {
        
        if (isWarning) {
            chatEntry.setBackground(Color.RED);
        }
        else {
            chatEntry.setBackground(aire.getSkinConfig().getSChatSaisieBgColor());
        }
    }

    /**
     * @see java.awt.Container#removeAll()
     */
    public void removeAll() {
        if (comboChatSon != null) {
            comboChatSon.removeActionListener(this);
        }
        super.removeAll();
        if (comboChatSon != null) {
            comboChatSon.removeAll();
            comboChatSon.dispose();
            comboChatSon = null;
        }
    }

    /* ***************************************
     * Gestion des événements utilisateurs
     * *************************************** */
     
    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if ((e.getSource() == chatEntry) 
                || (e.getSource() == boutonChatSend)) {
            // Envoi le chat général (pas à un joueur, pas lié à une partie)
            if (chatEntry.getText().trim().length() > 0) {
                sendChat(salle.getMyPartie().getIdentifiant());
            }
        }
        else if ((e.getSource() == comboChatSmiley) || (e.getSource() == comboChatSon)) {
            // Affiche la liste des smileys disponibles
            if (firstMessage) {
                chatEntry.setText("");
                firstMessage = false;
            }
            chatEntry.setText(chatEntry.getText() + e.getActionCommand());
            // Envoi direct du chat pour les sons
            if (e.getSource() == comboChatSon) {
                sendChat(salle.getMyPartie().getIdentifiant());
            }
        }
        else if ((e.getSource() == boutonChatCouleur)) {
            // Affiche la liste des couleurs disponibles
            openColorChooser();
        }
    }
    /**
     * @see com.increg.game.ui.component.chat.ChatPanel#getNbLigneVisible()
     */
    protected int getNbLigneVisible() {
        return 10;
    }

    /**
     * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
     */
    public void focusGained(FocusEvent e) {
        super.focusGained(e);
        // Averti la fenêtre des smileys que c'est là que vont les smileys
        comboChatSon.setActiveListener(this);
    }

}
