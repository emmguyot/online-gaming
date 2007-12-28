/*
 * Created on 24 avr. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.util.ResourceBundle;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import com.increg.game.client.AireMainModel;
import com.increg.game.client.Chat;
import com.increg.game.client.Joueur;
import com.increg.game.client.Partie;
import com.increg.game.client.belote.PartieBeloteClassique;
import com.increg.game.ui.component.PartiesTable;
import com.increg.game.ui.component.chat.ChatPanel;
import com.increg.util.StringCharOnlyComp;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class AfficheAire extends JPanel implements ActionListener, MouseListener {

    /* ***************************************
     * Constantes
     * *************************************** */
    /**
     * Indicateur que le joueur est en partie
     */
    private static final char EN_PARTIE = '<';
    /**
     * Indicateur que le joueur est un modérateur
     */
    private static final char MODERATEUR = '*';
    
    /* ***************************************
     * Attributs
     * *************************************** */
    /**
     * Modèle Aire contenant les infos à afficher
     */
    private AireMainModel aire;

    /**
     * Largeur de l'applet
     */
    private int myWidth;
    /**
     * Hauteur de l'applet
     */
    private int myHeight;
    
    /**
     * Image de fond d'écran
     */
    private BufferedImage imageFdBuff;
    
    /**
     * Image de fond d'écran non bufferisée : Sa valeur est fugitive au démarrage. Après c'est null
     */
    private Image imageFd;

    /**
     * Elément de scrolling des parties 
     */
    private JScrollPane scrollPartie;
    
    /**
     * Partie intérieure du scoll des parties
     */
    private PartiesTable lstPartieGraphique;

    /**
     * Bouton permettant la création d'une salle 
     */
    private JButton boutonCreation;
    
    /**
     * Bouton permettant de quitter l'aire
     */
    private JButton boutonFermeture;
    
    /**
     * Bouton permettant de fixer les options
     */
    private JButton boutonOption;
    
    /**
     * Bouton permettant pour les modérateurs
     */
    private JButton boutonAdmin;
    
    /**
     * Elément de scrolling pour les joueurs
     */
    private JScrollPane scrollJoueur;
    
    /**
     * Table des joueurs
     */
    private JList tableJoueur;

    /**
     * Zone de copyright1
     */
    private JLabel copyright1;

    /**
     * Zone de copyright2
     */
    private JLabel copyright2;

    /**
     * Balise entrante pour la font du chat
     */
    protected String baliseFont; 
    
    /**
     * Panel contenant l'ensemble du Chat
     */
    protected ChatPanel chat;
    
    /**
     * Titres
     */
    private Vector<JLabel> titres;

    /**
     * Constructeur
     * @param a Model d'Aire
     * @param w Largeur de la fenêtre
     * @param h Hauteur de la fenêtre
     */
    public AfficheAire(AireMainModel a, int w, int h) {
        
        // Init le JPanel
        super(true);
            
        // Garde les attributs
        aire = a;
        myWidth = w;
        myHeight = h;
        
        // Chargement de l'image de fond
        try {
            imageFd = aire.getImage(aire.getSkinConfig().getRFond());
        }
        catch (MalformedURLException e) {
            a.getLogger().severe("URL fond invalide");
        }
        
        // Pas de layout pour positionner directement les éléments
        setLayout(null);
        setOpaque(true);

        // Ajout tous les éléments
        addEveryComponents();
        
//        setVisible(true);
    }
    
    /**
     * Ajoute tous les composants d'affichage
     *
     */
    protected void addEveryComponents() {
        
        // Scrolling des parties
        add(getScrollPartie());
        lstPartieGraphique.setNbItem(0);
        
        // Scrolling des joueurs
        add(getScrollJoueur());
        
        // Boutons
        add(getBoutonCreation());
        add(getBoutonFermeture());
        add(getBoutonOption());
        add(getBoutonAdmin());
        
        // Chat
        addChat();
                
        // Copyrights
        add(getCopyright1());
        add(getCopyright2());
        
        addTitres();
    }
    
    /**
     * Construit la zone de scrolling pour les parties
     * @return zone de scolling
     */
    protected JScrollPane getScrollPartie() {
        scrollPartie = new javax.swing.JScrollPane();
        scrollPartie.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPartie.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        try {
            lstPartieGraphique = new PartiesTable(this, aire.getImage(aire.getSkinConfig().getRFondListe()), aire);
        }
        catch (MalformedURLException e) {
            aire.getLogger().severe("URL fond de liste invalide");
        }
        scrollPartie.setViewportView(lstPartieGraphique);
        // Fond Transparent
        scrollPartie.getViewport().setOpaque(false);
        lstPartieGraphique.setOpaque(false);
        scrollPartie.setOpaque(false);
                
        scrollPartie.setBounds(aire.getSkinConfig().getRListePartie());
        
        return scrollPartie;
    }

    /**
     * Construit la zone de scrolling pour les joueurs
     * @return zone de scolling
     */
    protected JScrollPane getScrollJoueur() {
        scrollJoueur = new javax.swing.JScrollPane();
        scrollJoueur.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollJoueur.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollJoueur.setBackground(aire.getSkinConfig().getRListeJoueurBgColor());
        
        tableJoueur = new JList();
        tableJoueur.setFont(aire.getSkinConfig().getRFont());
        tableJoueur.setForeground(aire.getSkinConfig().getRListeJoueurColor());
        tableJoueur.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableJoueur.setBackground(aire.getSkinConfig().getRListeJoueurBgColor());
        
        // Fond Transparent
        scrollJoueur.getViewport().setOpaque(aire.getSkinConfig().getRListeJoueurBgColor().getAlpha() != 0);
        tableJoueur.setOpaque(aire.getSkinConfig().getRListeJoueurBgColor().getAlpha() != 0);
        scrollJoueur.setOpaque(aire.getSkinConfig().getRListeJoueurBgColor().getAlpha() != 0);
        
        scrollJoueur.setViewportView(tableJoueur);
        
        scrollJoueur.setBounds(aire.getSkinConfig().getRListeJoueur());
        
        tableJoueur.addMouseListener(this);
        
        return scrollJoueur;
    }

    /**
     * Construit le bouton de création
     * @return bouton créé
     */
    protected JButton getBoutonCreation() {    
        boutonCreation = new JButton();
        if (aire.getSkinConfig().getRBoutonCreationImg() == null) {
            boutonCreation.setText("Créer...");
            boutonCreation.setFont(aire.getSkinConfig().getRFont().deriveFont(Font.BOLD));
            boutonCreation.setBackground(aire.getSkinConfig().getRBoutonGlobalColor());
        }
        else {
            try {
                boutonCreation.setIcon(aire.getImageIcon(aire.getSkinConfig().getRBoutonCreationImg()));
            }
            catch (MalformedURLException e) {
                aire.getLogger().severe("Image bouton création erronée");
            }
            boutonCreation.setToolTipText("Créer...");
        }
        if (!aire.getSkinConfig().isRBoutonGlocalBorder()) {
            boutonCreation.setBorder(BorderFactory.createEmptyBorder());
        }
        boutonCreation.setBounds(aire.getSkinConfig().getRBoutonCreation());
        boutonCreation.setMargin(new Insets(0, 0, 0, 0));
        boutonCreation.addActionListener(this);
        
        return boutonCreation;
    }
    
    /**
     * Construit le bouton de fermeture
     * @return bouton créé
     */
    protected JButton getBoutonFermeture() {    
        boutonFermeture = new JButton();
        if (aire.getSkinConfig().getRBoutonFermetureImg() == null) {
            boutonFermeture.setText("Quitter...");
            boutonFermeture.setFont(aire.getSkinConfig().getRFont().deriveFont(Font.BOLD));
            boutonFermeture.setBackground(aire.getSkinConfig().getRBoutonGlobalColor());
        }
        else {
            try {
                boutonFermeture.setIcon(aire.getImageIcon(aire.getSkinConfig().getRBoutonFermetureImg()));
            }
            catch (MalformedURLException e) {
                aire.getLogger().severe("Image bouton Fermeture erronée");
            }
            boutonFermeture.setToolTipText("Quitter...");
        }
        if (!aire.getSkinConfig().isRBoutonGlocalBorder()) {
            boutonFermeture.setBorder(BorderFactory.createEmptyBorder());
        }
        boutonFermeture.setBounds(aire.getSkinConfig().getRBoutonFermeture());
        boutonFermeture.setMargin(new Insets(0, 0, 0, 0));
        boutonFermeture.addActionListener(this);
        
        return boutonFermeture;
    }
    
    /**
     * Construit le bouton des options
     * @return bouton créé
     */
    protected JButton getBoutonOption() {    
        boutonOption = new JButton();
        if (aire.getSkinConfig().getRBoutonOptionImg() == null) {
            boutonOption.setText("Options...");
            boutonOption.setFont(aire.getSkinConfig().getRFont().deriveFont(Font.BOLD));
            boutonOption.setBackground(aire.getSkinConfig().getRBoutonGlobalColor());
        }
        else {
            try {
                boutonOption.setIcon(aire.getImageIcon(aire.getSkinConfig().getRBoutonOptionImg()));
            }
            catch (MalformedURLException e) {
                aire.getLogger().severe("Image bouton Options erronée");
            }
            boutonOption.setToolTipText("Options...");
        }
        if (!aire.getSkinConfig().isRBoutonGlocalBorder()) {
            boutonOption.setBorder(BorderFactory.createEmptyBorder());
        }
        boutonOption.setBounds(aire.getSkinConfig().getRBoutonOption());
        boutonOption.setMargin(new Insets(0, 0, 0, 0));
        boutonOption.addActionListener(this);
        
        return boutonOption;
    }
    
    /**
     * Construit le bouton pour les modérateurs
     * @return bouton créé
     */
    protected JButton getBoutonAdmin() {    
        boutonAdmin = new JButton();
        if (aire.getSkinConfig().getRBoutonAdminImg() == null) {
            boutonAdmin.setText("Admin...");
            boutonAdmin.setFont(aire.getSkinConfig().getRFont().deriveFont(Font.BOLD));
            boutonAdmin.setBackground(aire.getSkinConfig().getRBoutonGlobalColor());
        }
        else {
            try {
                boutonAdmin.setIcon(aire.getImageIcon(aire.getSkinConfig().getRBoutonAdminImg()));
            }
            catch (MalformedURLException e) {
                aire.getLogger().severe("Image bouton modérateur erronée");
            }
            boutonAdmin.setToolTipText("Admin...");
        }
        if (!aire.getSkinConfig().isRBoutonGlocalBorder()) {
            boutonAdmin.setBorder(BorderFactory.createEmptyBorder());
        }
        boutonAdmin.setBounds(aire.getSkinConfig().getRBoutonAdmin());
        boutonAdmin.setMargin(new Insets(0, 0, 0, 0));
        boutonAdmin.addActionListener(this);
        boutonAdmin.setVisible(false);
        
        return boutonAdmin;
    }
    
    /**
     * Construit les titres
     */
    protected void addTitres() {    

        titres = new Vector<JLabel>(aire.getSkinConfig().getRTitre().size());
        
        for (int i = 0; i < aire.getSkinConfig().getRTitre().size(); i++) {
            JLabel titre = new JLabel(aire.getSkinConfig().getRTitre(i));
            titre.setBounds(aire.getSkinConfig().getRTitrePos(i));         
            titre.setFont(aire.getSkinConfig().getRTitreFont());
            titre.setForeground(Color.BLACK);
            
            add(titre);
            titres.add(titre);
        }
    }
    
    /**
     * Construit panel des copyrights
     * @return champ créé
     */
    protected JLabel getCopyright1() {    

        copyright1 = new JLabel(aire.getSkinConfig().getClient());
        copyright1.setBounds(aire.getSkinConfig().getRLienSite1());         
        copyright1.setFont(aire.getSkinConfig().getRFont().deriveFont(Font.BOLD));
        copyright1.setForeground(aire.getSkinConfig().getRLienSite1Color());
        
        copyright1.addMouseListener(this);
        return copyright1;
    }
    
    /**
     * Construit panel des copyrights
     * @return champ créé
     */
    protected JLabel getCopyright2() {    
        
        copyright2 = new JLabel("InCrEG");
        copyright2.setBounds(aire.getSkinConfig().getRLienSite2());
        copyright2.setHorizontalAlignment(SwingConstants.RIGHT);         
        copyright2.setFont(aire.getSkinConfig().getRFont().deriveFont(Font.BOLD));
        copyright2.setForeground(aire.getSkinConfig().getRLienSite2Color());

        copyright2.addMouseListener(this);
        return copyright2;
    }

    /**
     * Ajoute le chat
     */
    protected void addChat() {
        chat = new ChatPanel(this, aire);
    }

    /**
     * Les smileys ont changé 
     */
    public void resetSmiley() {
        chat.resetSmiley();
    }

    /**
     * Affiche le fond de la fenêtre
     * @see java.awt.Component#paintComponent(java.awt.Graphics)
     */
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;

        if (imageFdBuff == null) {
            // Bufferise l'image        
            imageFdBuff = (BufferedImage) this.createImage(getWidth(), getHeight());
            Graphics2D gBuff = imageFdBuff.createGraphics();
            if (gBuff.drawImage(imageFd, 0, 0, this)) {
                // Libère la mémoire
                imageFd = null;
            }
            else {
                // Pas encore chargée
                imageFdBuff = null;
            }
        }
        if (imageFdBuff != null) {
            g.drawImage(imageFdBuff, 0, 0, this);
        }
    }

    /* ***************************************
     * Gestion des événements : 1 seul à la fois
     * *************************************** */

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == boutonCreation) {
            new SalleOptions(aire, new PartieBeloteClassique());
        }
        else if (e.getSource() == boutonOption) {
            new AireOptions(aire);
        }
        else if (e.getSource() == boutonFermeture) {
            // Confirmation
            if (JOptionPane.showConfirmDialog(this, "Vous allez quitter l'aire de jeu et ses parties. Etes-vous sûr ?", "Quitter", 
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                // Bye bye
                aire.quitte();            
            }
        }
        else if (e.getSource() == boutonAdmin) {
            aire.ouvreAdmin();
        }
        aire.getLogger().fine("Event :" + e);
    }

    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == copyright1) {
            aire.ouvrePopup(aire.getSkinConfig().getClientUrl(), "_blank");
        }
        else if (e.getSource() == copyright2) {
            ResourceBundle resConfig = ResourceBundle.getBundle("configAire");
            String url = resConfig.getString("urlCopyright2");
            if (url != null) {
                aire.ouvrePopup(url, "_blank");
            }
        }
    }

    /**
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e) {
        if ((e.getSource() == tableJoueur) && (e.getClickCount() > 1)) {
            // Double clic dans la liste des joueurs
            String pseudo = (String) tableJoueur.getSelectedValue();
            if (pseudo != null) {
	            if (pseudo.charAt(0) == EN_PARTIE) {
	                // Supprime le chevron devant le joueur qui est en partie
	                pseudo = pseudo.substring(2);
	            }
	            if (pseudo.charAt(pseudo.length() - 1) == MODERATEUR) {
	                // Supprime l'étoile des modérateurs
	                pseudo = pseudo.substring(0, pseudo.length() - 2);
	            }
	            aire.ouvrePageJoueur(pseudo);
            }
        }
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
     * Rafraichit les données affichées
     */
    public void refresh() {

        // Les joueurs
        Vector<Joueur> lstJoueur = aire.getLstJoueur();
        TreeSet<String> tableauJoueur = new TreeSet<String>(new StringCharOnlyComp());
        for (int i = 0; i < lstJoueur.size(); i++) {
            Joueur aJoueur = (Joueur) lstJoueur.get(i);
            String pseudo = aJoueur.getPseudo();
            
            boolean estDansPartie = false;
            // Le joueur est dans une partie ?
            for (int iPartie = 0; (iPartie < aire.getLstPartie().size()) && !estDansPartie; iPartie++) {
                Partie aPartie = (Partie) aire.getLstPartie().get(iPartie); 
                if (aPartie.getPositionJoueur(aJoueur) >= 0) {
                    estDansPartie = true;
                }
            }

            if (aJoueur.getPrivilege() >= Joueur.MODERATEUR_PRIVILEGE) {
                // Marque les modérateurs dans la liste
                pseudo = pseudo + " " + MODERATEUR;
            }

            if (estDansPartie) {
                tableauJoueur.add(EN_PARTIE + " " + pseudo);
            }
            else {
                tableauJoueur.add(pseudo);
            }
            
        }
        tableJoueur.setListData(tableauJoueur.toArray());

        if ((!boutonAdmin.isVisible()) && (aire.getMyJoueur().getPrivilege() >= Joueur.MODERATEUR_PRIVILEGE)) {
            boutonAdmin.setVisible(true);
        }
        
        updateParties();
        
        chat.refresh();

        validate();
        invalidate();
        
        // Restaure le pointeur
        setCursor(Cursor.getDefaultCursor());

    }

    /**
     * Mise à jour des éléments pour les parties
     *
     */
    public void updateParties() {
        lstPartieGraphique.updateParties();
    }

    /**
     * Ajoute des phrases au chat
     * @param aChat Chat à ajouter
     */
    public void addPendingChat(Chat aChat) {
        chat.addPendingChat(aChat);
    }
    

    /**
     * @see java.awt.Container#removeAll()
     */
    public void removeAll() {

        if (tableJoueur != null) {
            tableJoueur.removeMouseListener(this);
        }
        if (boutonCreation != null) {
            boutonCreation.removeActionListener(this);
        }
        if (boutonFermeture != null) {
            boutonFermeture.removeActionListener(this);
        }
        if (copyright1 != null) {
            copyright1.removeMouseListener(this);
        }
        if (copyright2 != null) {
            copyright2.removeMouseListener(this);
        }
        if (chat != null) {
            chat.removeAll();
            chat = null;
        }
        if (lstPartieGraphique != null) {
            lstPartieGraphique.removeAll();
            lstPartieGraphique = null;
        }
    
        super.removeAll();
    }

}
