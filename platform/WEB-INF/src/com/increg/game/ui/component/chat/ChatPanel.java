/*
 * Created on 18 mai 2003
 *
 */
package com.increg.game.ui.component.chat;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.MalformedURLException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.increg.game.client.AireMainModel;
import com.increg.game.client.Chat;
import com.increg.game.client.Smiley;
import com.increg.game.ui.SecretChat;
import com.increg.game.ui.component.ChoixCouleur;
import com.increg.game.ui.component.ImageComboBox;
import com.increg.game.ui.component.SoundPlayer;

/**
 * @author Manu
 *
 * Classe regroupant l'ensemble des contrôles du chat
 */
public class ChatPanel extends JComponent implements ActionListener, MouseListener, ChangeListener, FocusListener, WindowListener, KeyListener {

    /**
     * Nombre de message historique à conserver
     */
    protected static final int TAILLE_HISTORIQUE = 50;
    
    /**
     * Composant graphique Parent 
     */
    protected JPanel parent;
    
    /**
     * Aire principale
     */
    protected AireMainModel aire;

    /**
     * Balise font en HTML
     */    
    protected String baliseFont;

    /**
     * Chaine à ajouter au Chat au prochain refresh
     */
    protected String pendingChat;
    
    /**
     * Elémént de scrolling pour le chat
     */
    protected JScrollPane scrollChat;

    /**
     * Zone de chat proprement dite
     */
    protected JTextPane chatView;

    /**
     * Zone de chat de saisie
     */
    protected JTextField chatEntry;

    /**
     * Bouton pour envoyer le chat 
     */
    protected JButton boutonChatSend;
    
    /**
     * Bouton pour insérer un smiley
     */
    protected ImageComboBox comboChatSmiley;
    
    /**
     * Bouton pour changer la couleur
     */
    protected JButton boutonChatCouleur;
    
    /**
     * Bouton pour envoyer en secret
     */
    protected JButton boutonChatSecret;
    
    /**
     * Couleur d'écriture
     */
    protected static String currentColor;

    /**
     * Fenêtre de choix de couleur
     */
    protected ChoixCouleur frameChooser;

    /**
     * Joueur destinataire du message secret : Null si message globale
     */
    protected String destinataire;
    
    /**
     * Le destinataire n'est que pour une fois
     */
    protected boolean resetDestinataire;
    
    /**
     * Indicateur de premier message
     */
    protected boolean firstMessage;
    
    /**
     * Historique des messages saisis
     */
    protected static Vector historique;
    
    /**
     * Position dans l'historique des messages saisis
     */
    protected static int posHistorique;

    /**
     * Le son est actif sur ce chat ?
     */
    protected boolean sonActif;

    /**
     * Indicateur si l'init est terminée
     */
    protected boolean initDone;        

    /**
     * Constructeur
     * @param aParent parent d'un point de vue graphique
     * @param a Aire
     */
    public ChatPanel(JPanel aParent, AireMainModel a) {
        // Conserve les paramètres
        parent = aParent;
        aire = a;
        
        // Init des attributs
        pendingChat = "";
        initDone = false;
        destinataire = null;
        resetDestinataire = false;
        firstMessage = true;
        if (currentColor == null) {
            currentColor = a.getMyJoueur().getCouleur();
        }
        if (historique == null) {
            historique = new Vector(TAILLE_HISTORIQUE);
            posHistorique = 0;
        }
        
        // Zone de chat : Saisie
        JComponent aComp = getChatEntry();
        if (aComp != null) { 
            aParent.add(aComp);
        }
        aComp = getChatSend();
        if (aComp != null) { 
            aParent.add(aComp);
        }
        aComp = getChatSmiley();
        if (aComp != null) { 
            aParent.add(aComp);
        }
        aComp = getChatCouleur();
        if (aComp != null) { 
            aParent.add(aComp);
        }
        aComp = getChatSecret();
        if (aComp != null) { 
            aParent.add(aComp);
        }
        aComp = getChatSon();
        if (aComp != null) { 
            aParent.add(aComp);
            sonActif = true;
        }
        else {
            sonActif = false;
        }
        
        // Zone de chat : Affichage
        aComp = getScrollChat();
        if (aComp != null) { 
            aParent.add(aComp);
        }
    }
    
    /**
     * Construit le champ de saisie du chat
     * @return champ créé
     */
    protected JTextField getChatEntry() {    
        chatEntry = new JTextField("Tapez votre message ici");
        setChatEntryProperties();
        chatEntry.addActionListener(this);
        chatEntry.addFocusListener(this);
        chatEntry.addKeyListener(this);
        
        return chatEntry;
    }

    /**
     * Positionne les propriétés potentiellement variantes
     *
     */
    protected void setChatEntryProperties() {
        chatEntry.setFont(aire.getSkinConfig().getRFont());
        chatEntry.setBounds(aire.getSkinConfig().getRChatSaisie());
        chatEntry.setBackground(aire.getSkinConfig().getRChatSaisieBgColor());
    }
    
    /**
     * Construit le bouton de fermeture
     * @return bouton créé
     */
    protected JButton getChatSend() {    
        boutonChatSend = new JButton();
        setChatSendProperties();
        boutonChatSend.setMargin(new Insets(0, 0, 0, 0));
        boutonChatSend.addActionListener(this);
        
        return boutonChatSend;
    }

    /**
     * Positionne les propriétés potentiellement variantes
     *
     */
    protected void setChatSendProperties() {
        try {
            boutonChatSend.setIcon(aire.getImageIcon(aire.getSkinConfig().getRChatSendImg()));
        }
        catch (MalformedURLException e) {
            aire.getLogger().severe("Image send Chat erronée");
        }
        boutonChatSend.setBounds(aire.getSkinConfig().getRChatSend());
        boutonChatSend.setBackground(aire.getSkinConfig().getRBoutonGlobalColor());
        if (!aire.getSkinConfig().isRBoutonGlocalBorder()) {
            boutonChatSend.setBorder(BorderFactory.createEmptyBorder());
        }
    }
    
    /**
     * Construit le bouton smileys
     * @return bouton créé
     */
    protected ImageComboBox getChatSmiley() {
        
        Vector smiley = Chat.getSmiley();
        String[] items = new String[smiley.size()];
        String[] url = new String[smiley.size()];
        int cpt = 0;
        for (int i = 0; i < smiley.size(); i++) {
            Smiley aSmiley = (Smiley) smiley.get(i);

            items[cpt] = aSmiley.getCode();
            url[cpt] = aSmiley.getImage();
            cpt++;
        }
          
        comboChatSmiley = new ImageComboBox(items, url, aire, "Smileys du chat");
        setChatSmileyProperties();
        comboChatSmiley.setMargin(new Insets(0, 0, 0, 0));
        comboChatSmiley.setToolTipText("Smileys");
        comboChatSmiley.addActionListener(this);
        
        return comboChatSmiley;
    }

    /**
     * Construit le bouton son
     * @return bouton créé
     */
    protected ImageComboBox getChatSon() {
        return null;
    }
    
    /**
     * Positionne les propriétés potentiellement variantes
     *
     */
    protected void setChatSmileyProperties() {
        try {
            comboChatSmiley.setIcon(aire.getImageIcon(aire.getSkinConfig().getRChatSmileyImg()));
        }
        catch (MalformedURLException e) {
            aire.getLogger().severe("Image couleur Chat erronée");
        }
        comboChatSmiley.setBounds(aire.getSkinConfig().getRChatSmiley());
        comboChatSmiley.setBackground(aire.getSkinConfig().getRBoutonGlobalColor());
        if (!aire.getSkinConfig().isRBoutonGlocalBorder()) {
            comboChatSmiley.setBorder(BorderFactory.createEmptyBorder());
        }
    }
    
    /**
     * Construit le bouton de couleur
     * @return bouton créé
     */
    protected JButton getChatCouleur() {    
        boutonChatCouleur = new JButton();
        setChatCouleurProperties();
        boutonChatCouleur.setToolTipText("Couleur du texte");
        boutonChatCouleur.setMargin(new Insets(0, 0, 0, 0));
        boutonChatCouleur.addActionListener(this);
        
        return boutonChatCouleur;
    }

    /**
     * Positionne les propriétés potentiellement variantes
     *
     */
    protected void setChatCouleurProperties() {
        try {
            boutonChatCouleur.setIcon(aire.getImageIcon(aire.getSkinConfig().getRChatCouleurImg()));
        }
        catch (MalformedURLException e) {
            aire.getLogger().severe("Image couleur Chat erronée");
        }
        boutonChatCouleur.setBounds(aire.getSkinConfig().getRChatCouleur());
        boutonChatCouleur.setBackground(aire.getSkinConfig().getRBoutonGlobalColor());
        if (!aire.getSkinConfig().isRBoutonGlocalBorder()) {
            boutonChatCouleur.setBorder(BorderFactory.createEmptyBorder());
        }
    }
    
    /**
     * Construit le bouton secret
     * @return bouton créé
     */
    protected JButton getChatSecret() {    
        boutonChatSecret = new JButton();
        setChatSecretProperties();
        boutonChatSecret.setToolTipText("Message secret");
        boutonChatSecret.setMargin(new Insets(0, 0, 0, 0));
        boutonChatSecret.addActionListener(this);
        
        return boutonChatSecret;
    }

    /**
     * Positionne les propriétés potentiellement variantes
     *
     */
    protected void setChatSecretProperties() {
        try {
            boutonChatSecret.setIcon(aire.getImageIcon(aire.getSkinConfig().getRChatSecretImg()));
        }
        catch (MalformedURLException e) {
            aire.getLogger().severe("Image secret Chat erronée");
        }
        boutonChatSecret.setBounds(aire.getSkinConfig().getRChatSecret());
        boutonChatSecret.setBackground(aire.getSkinConfig().getRBoutonGlobalColor());
        if (!aire.getSkinConfig().isRBoutonGlocalBorder()) {
            boutonChatSecret.setBorder(BorderFactory.createEmptyBorder());
        }
    }
    
    /**
     * Construit le champ d'affichage du chat
     * @return champ créé
     */
    protected JScrollPane getScrollChat() {    
        scrollChat = new javax.swing.JScrollPane();
        scrollChat.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollChat.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        chatView = new JTextPane();
        chatView.setEditable(false);
        chatView.setContentType("text/html");
        
        chatView.setEditorKit(new HTMLWrappedEditorKit());
        
        setScrollChatProperties();

        chatView.setText("<html><body>" + baliseFont + "<br></font></body></html>");
    
        scrollChat.setViewportView(chatView);
        
        // C'est le cache qui gère les chargements
        chatView.getDocument().putProperty("imageCache", aire.getLiaisonSrv().getCacheHandler());        

        return scrollChat;
    }

    /**
     * Positionne les propriétés potentiellement variantes
     *
     */
    protected void setScrollChatProperties() {
        baliseFont = "<font face=\"" + aire.getSkinConfig().getRFont().getFamily() 
                                + "\" size=\"4\">";
        scrollChat.setBounds(aire.getSkinConfig().getRChatView());
        chatView.setBackground(aire.getSkinConfig().getRChatViewBgColor());
   }

    /**
     * Les smileys ont changé 
     */
    public void resetSmiley() {
        Vector smiley = Chat.getSmiley();
        String[] items = new String[smiley.size()];
        String[] url = new String[smiley.size()];
        int cpt = 0;
        for (int i = 0; i < smiley.size(); i++) {
            Smiley aSmiley = (Smiley) smiley.get(i);

            items[cpt] = aSmiley.getCode();
            url[cpt] = aSmiley.getImage();
            cpt++;
        }
        comboChatSmiley.reset(items, url);
    }

    /**
     * Ajoute un chat en attente du prochain refresh
     * @param aChat Chat à ajouter
     */
    public void addPendingChat(Chat aChat) {

        // Mise en forme en dehors pour limiter la section critique
        String msgTot = aChat.toString();
        String son = aChat.getSound();
        
        // Gère les remplacements de fonte
        int pos = msgTot.indexOf("<font");
        while (pos >= 0) {
            
            // Intercale une fin de fonte (de la précédente)
            msgTot = msgTot.substring(0, pos) + "</font>" + msgTot.substring(pos);
            
            /**
             * Force la police pour contourner le Bug Java
             */
            int posFin = msgTot.indexOf(">", pos + 7);
            int posFace = msgTot.indexOf("face=", pos + 7);
            
            if ((posFace == -1) || (posFace > posFin)) {
                // Ajoute la face et la taille
                msgTot = msgTot.substring(0, posFin) + baliseFont.substring(5) + msgTot.substring(posFin + 1);
            }
            
            int pos2 = msgTot.indexOf("</font>", pos + 1);
            
            // Intercale le début de fonte 
            msgTot = msgTot.substring(0, pos2 + 7) + baliseFont + msgTot.substring(pos2 + 7);
            
            pos = msgTot.indexOf("<font", pos2 + 8);
        }

        synchronized (pendingChat) {
            // Un espace à la fin pour que la dernière balise fonte ne soit pas supprimée
            pendingChat = pendingChat + "<br>" + msgTot + "&nbsp;"; 
        }
        
        if ((son != null) 
                && (initDone)
                && ((sonActif) || (aChat.getJoueurOrig().isModerateur()) || (aChat.getJoueurOrig().isSystem()))) {
            Thread toRun = new Thread(new SoundPlayer(aire, son));
            toRun.setPriority(Thread.MIN_PRIORITY);
            toRun.start();
        }
        // System.out.println(pendingChat);
    }

    /**
     * Créé et ouvre la fenêtre de choix de la couleur
     *
     */
    protected void openColorChooser() {
        
        if (frameChooser == null) {
            frameChooser = new ChoixCouleur(this, "Couleur du texte", currentColor);
            frameChooser.setLocationRelativeTo(null);
            frameChooser.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frameChooser.addWindowListener(this);
        }
        
        if (frameChooser.isVisible()) {
            frameChooser.hide();
        }
        else {
            frameChooser.show();
            // Au cas où la fenêtre a été mise en icône
            frameChooser.setExtendedState(JFrame.NORMAL);
        }
    }

    /**
     * Envoi du Chat
     * @param idPartie Identifiant de la partie
     */
    protected void sendChat(int idPartie) {
        aire.sendChat(chatEntry.getText().trim(), currentColor, destinataire, idPartie);
        historique.add(chatEntry.getText().trim());
        if (historique.size() >= TAILLE_HISTORIQUE) {
            historique.remove(0);
        }
        posHistorique = historique.size();
        chatEntry.setText(""); 
        if (resetDestinataire) {
            destinataire = null;
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
                sendChat(0);
            }
        }
        else if ((e.getSource() == comboChatSmiley)) {
            // Affiche la liste des smileys disponibles
            if (firstMessage) {
                chatEntry.setText("");
                firstMessage = false;
            }
            chatEntry.setText(chatEntry.getText() + e.getActionCommand());
        }
        else if ((e.getSource() == boutonChatCouleur)) {
            // Affiche la liste des couleurs disponibles
            openColorChooser();
        }
        else if ((e.getSource() == boutonChatSecret)) {
            // Affiche la liste des joueurs connectés
            SecretChat secretDialog = new SecretChat(aire, aire.getLstJoueur());
            SecretChat.SecretChatModel model = secretDialog.getModel();
            secretDialog.show();
            
            destinataire = model.getPseudo();
            resetDestinataire = model.isOneOnly();
            
            secretDialog.removeAll();
            secretDialog.dispose();
            secretDialog = null; 
        }
    }

    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e) {
    }

    /**
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Mise à jour des données en attente => Affichage
     */
    public void refresh() {    
        synchronized (pendingChat) {
            if (pendingChat.length() > 0) {
                String contenu = chatView.getText();
                int pos = contenu.lastIndexOf("</font>");
                if (pos != -1) {
                    // System.out.println("Avant : " + chatView.getText());
                    /**
                     * Optimise la chaîne et coupe par rapport au nombre de lignes
                     */
                    StringBuffer newChat = new StringBuffer(contenu.substring(0, pos) + pendingChat + "</font></body></html>");
                    int nbBlanc = 0;
                    for (int i = 0; i < newChat.length(); i++) {
                        if (newChat.charAt(i) == ' ') {
                            nbBlanc++;
                        }
                        else {
                            if (nbBlanc > 1) {
                                // Supprime les n blancs par un seul
                                newChat = newChat.delete(i - (nbBlanc - 1), i);
                                i -= (nbBlanc - 1);
                            }
                            nbBlanc = 0;
                        }
                    }
                    // Compte les lignes
                    pos = newChat.lastIndexOf("<br>");
                    int nbLigne = 0;
                    while ((pos != -1) && (nbLigne <= getNbLigneVisible())) {
                        nbLigne++;
    
                        if (nbLigne > getNbLigneVisible()) {
                            // Il faut couper
                            int firstPos = newChat.indexOf("<br>");
                            newChat = newChat.delete(firstPos, pos);
                        }
                        
                        pos = newChat.lastIndexOf("<br>", pos - 1);
                    }

                    // Affichage proprement dit
                    chatView.setText(newChat.toString());
                    if (chatView.isVisible()) {
                        chatView.moveCaretPosition(chatView.getDocument().getLength());
                        chatView.setSelectionStart(chatView.getDocument().getLength());
                    }
                    pendingChat = "";
                    
                }
                else {
                    aire.getLogger().warning("Chat mal formaté");
                }
            }
            // Dès qu'un refresh est fait, l'init est fini
            initDone = true;
        }
    }

    /**
     * @return Nombre de chats visibles
     */
    protected int getNbLigneVisible() {
        return 30;
    }

    /**
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged(ChangeEvent e) {
        currentColor = frameChooser.getColor();
        boutonChatCouleur.setForeground(Color.decode(currentColor));
    }

    /**
     * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
     */
    public void focusGained(FocusEvent e) {
        if (firstMessage) {
            chatEntry.setText("");
            firstMessage = false;
        }
        // Averti la fenêtre des smileys que c'est là que vont les smileys
        comboChatSmiley.setActiveListener(this);
    }

    /**
     * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
     */
    public void focusLost(FocusEvent e) {
    }

    /**
     * @see java.awt.Container#removeAll()
     */
    public void removeAll() {
        if (scrollChat != null) {
            scrollChat.setViewportView(null);
        }
        if (chatView != null) {
            chatView.setDocument(chatView.getEditorKit().createDefaultDocument());
        }
        if (chatEntry != null) {
            chatEntry.removeActionListener(this);
            chatEntry.removeFocusListener(this);
        }
        if (boutonChatSend != null) {
            boutonChatSend.removeActionListener(this);
        }
        if (comboChatSmiley != null) {
            comboChatSmiley.removeActionListener(this);
        }
        if (boutonChatCouleur != null) {
            boutonChatCouleur.removeActionListener(this);
        }
        if (boutonChatSecret != null) {
            boutonChatSecret.removeActionListener(this);
        }
        if (frameChooser != null) {
            frameChooser.removeWindowListener(this);
            frameChooser.removeAll();
            frameChooser.dispose();
            frameChooser = null;
        }
        if (comboChatSmiley != null) {
            comboChatSmiley.removeAll();
            comboChatSmiley.dispose();
            comboChatSmiley = null;
        }
        super.removeAll();
    }

    /**
     * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
     */
    public void windowOpened(WindowEvent e) {
    }

    /**
     * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
     */
    public void windowClosing(WindowEvent e) {
        if (e.getSource() == frameChooser) {
            frameChooser.removeWindowListener(this);
            frameChooser = null;
        }
    }

    /**
     * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
     */
    public void windowClosed(WindowEvent e) {
    }

    /**
     * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
     */
    public void windowIconified(WindowEvent e) {
    }

    /**
     * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
     */
    public void windowDeiconified(WindowEvent e) {
    }

    /**
     * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
     */
    public void windowActivated(WindowEvent e) {
    }

    /**
     * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
     */
    public void windowDeactivated(WindowEvent e) {
    }

    /**
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    public void keyTyped(KeyEvent e) {
    }

    /**
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(KeyEvent e) {
        // Gestion de l'historique
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (posHistorique > 0) {
                chatEntry.setText((String) historique.get(--posHistorique)); 
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (posHistorique < (historique.size() - 1)) {
                chatEntry.setText((String) historique.get(++posHistorique)); 
            }
        }
    }

    /**
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    public void keyReleased(KeyEvent e) {
    }

}
