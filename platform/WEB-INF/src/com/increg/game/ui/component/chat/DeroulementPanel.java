/*
 * Created on 1 juin 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.ui.component.chat;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.increg.game.client.AireMainModel;
import com.increg.game.ui.component.ImageComboBox;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DeroulementPanel extends ChatPanel {

    /**
     * Constructeur
     * @param aParent parent d'un point de vue graphique
     * @param a Aire
     */
    public DeroulementPanel(JPanel aParent, AireMainModel a) {
        super(aParent, a);
    }

    /**
     * @see com.increg.game.ui.component.ChatPanel#getChatEntry()
     */
    protected JTextField getChatEntry() {
        return null;
    }

    /**
     * @see com.increg.game.ui.component.ChatPanel#getChatSend()
     */
    protected JButton getChatSend() {
        return null;
    }

    /**
     * Construit le champ d'affichage du chat
     * @return champ créé
     */
    protected JScrollPane getScrollChat() {    
        scrollChat = new javax.swing.JScrollPane();
        scrollChat.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollChat.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollChat.setBackground(aire.getSkinConfig().getSEventBgColor());
        
        chatView = new JTextPane();
        chatView.setBackground(aire.getSkinConfig().getSEventBgColor());
        chatView.setForeground(aire.getSkinConfig().getSEventColor());
        chatView.setEditable(false);
        chatView.setContentType("text/html");
        
        chatView.setEditorKit(new HTMLWrappedEditorKit());
        
        baliseFont = "<font face=\"" + aire.getSkinConfig().getSFont().getFamily() 
                                + "\" size=\"3\">";
        
        String codeCouleur = "000000" + Integer.toHexString(aire.getSkinConfig().getSEventColor().getRGB());
        codeCouleur = "#" + codeCouleur.substring(codeCouleur.length() - 6);
        
        chatView.setText("<html><body color=\"" + codeCouleur + "\">" + baliseFont + "<br></font></body></font>");

        scrollChat.setViewportView(chatView);
        
        // Fond Transparent
        scrollChat.getViewport().setOpaque(aire.getSkinConfig().getSEventBgColor().getAlpha() != 0);
        chatView.setOpaque(aire.getSkinConfig().getSEventBgColor().getAlpha() != 0);
        scrollChat.setOpaque(aire.getSkinConfig().getSEventBgColor().getAlpha() != 0);

        scrollChat.setBounds(aire.getSkinConfig().getSEvent());

        return scrollChat;
    }

    /**
     * @see com.increg.game.ui.component.ChatPanel#getChatCouleur()
     */
    protected JButton getChatCouleur() {
        return null;
    }

    /**
     * @see com.increg.game.ui.component.ChatPanel#getChatSecret()
     */
    protected JButton getChatSecret() {
        return null;
    }

    /**
     * @see com.increg.game.ui.component.ChatPanel#getChatSmiley()
     */
    protected ImageComboBox getChatSmiley() {
        return null;
    }

    /**
     * @see com.increg.game.ui.component.chat.ChatPanel#getNbLigneVisible()
     */
    protected int getNbLigneVisible() {
        return 10;
    }

}
