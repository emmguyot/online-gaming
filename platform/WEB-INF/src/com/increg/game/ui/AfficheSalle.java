/*
 * Created on 24 avr. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import com.increg.game.client.Carte;
import com.increg.game.client.Chat;
import com.increg.game.client.Couleur;
import com.increg.game.client.Joueur;
import com.increg.game.client.SalleModel;
import com.increg.game.client.belote.EtatPartieBelote;
import com.increg.game.client.belote.PartieBelote;
import com.increg.game.ui.component.CarteComponent;
import com.increg.game.ui.component.SphereComponent;
import com.increg.game.ui.component.chat.ChatPanelPartie;
import com.increg.game.ui.component.chat.DeroulementPanel;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AfficheSalle extends JPanel implements WindowListener, ActionListener, MouseListener, MouseMotionListener {

    /* ***************************************
     * Constantes
     * *************************************** */
    /**
     * Chaine affichée si le joueur n'est pas connu
     */
    private static final String UNKNOWN = "?"; 

    /* ***************************************
     * Attributs
     * *************************************** */
    /**
     * Modèle salle contenant les infos à afficher
     */
    protected SalleModel salle;


    /**
     * Dernier état vu et pris en compte : Pour éviter de bégayer
     */
    private int lastEtat;
    
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
     * Texte représentant les noms des joueurs
     */        
    private JLabel[] pseudoJoueur;
    
    /**
     * Label représentant l'avatar des joueurs
     */        
    private JLabel[] avatarJoueur;

    /**
     * Elément permettant le scrolling des événements
     */
    private DeroulementPanel scrollEvent;    
    
    /**
     * Score des équipes
     */
    private JLabel[] score;
    
    /**
     * Texte indiquant le preneur et à quoi
     */
    private JLabel preneur;
    
    /**
     * Décallage des joueurs entre l'affichage et le reste
     */
    private int offsetJoueur;

    /**
     * Chat de la partie
     */
    private ChatPanelPartie chat; 
    
    /**
     * Tas de carte pour la distribution / coupe
     */
    private CarteComponent[] tasDistribution;
    
    /**
     * Curseur de coupe du jeu
     */   
    private JSlider curseurCoupe;
     
    /**
     * Image pour le dos des cartes
     */
    protected ImageIcon dosCarte;

    /**
     * Bouton de validation
     */
    protected JButton boutonValide;

    /**
     * Bouton de choix du nb de cartes en premier
     */
    protected JButton bouton2Cartes;

    /**
     * Bouton de choix du nb de cartes en premier
     */
    protected JButton bouton3Cartes;

    /**
     * Cartes du jeu
     */
    private CarteComponent[] cartesJeu;

    /**
     * Boutons pour la prise
     */
    protected JButton[] boutonsPrise;
        
    /**
     * Tapis de jeu
     */
    private CarteComponent[] tapis;

    /**
     * Plis ramassés
     */
    private CarteComponent[] plis;

    /**
     * Nombre de cartes sur le tapis
     */
    private int nbCarteTapis;
    
    /**
     * Indicateur si une fenêtre de dialogue est ouverte
     */
    private boolean dialogOpen;

    /**
     * Sphere des Donneurs, Maitres et joueurs
     */
    private SphereComponent sphere;

    /**
     * Bouton pour voir le dernier plis
     */
    private JButton boutonPlis;

    /**
     * Position de la souris au moment de l'appui du bouton 1
     */
    private Point positionSouris;

    /**
     * Flag si les cartes ont déjà été ramassée
     */
    private boolean ramassageDone;

    /**
     * Composant visible pour le Refresh : Optimisation
     */     
    private Vector lstVisible;
    /**
     * Composant invisible pour le Refresh : Optimisation
     */     
    private Vector lstInvisible;

    /**
     * Timer de ramassage automatique
     */
    private Timer autoRamasse;
    /**
     * Durée de ramassage automatique des cartes
     */
    static private int tempoRamasse = -1;
    
    /**
     * Cache du joueur ouvreur
     */
    private int ouvreurCourant;

    /* ***************************************
     * Méthodes
     * *************************************** */
    /**
     * Constructeur
     * @param s Salle
     * @param w Largeur de la fenêtre
     * @param h Hauteur de la fenêtre
     */
    public AfficheSalle(SalleModel s, int w, int h) {
        
        // Init le JPanel
        super(true);
            
        // Garde les attributs
        salle = s;
        myWidth = w;
        myHeight = h;
        score = new JLabel[PartieBelote.NB_JOUEUR];
        pseudoJoueur = new JLabel[PartieBelote.NB_JOUEUR];
        avatarJoueur = new JLabel[PartieBelote.NB_JOUEUR];
        tasDistribution = new CarteComponent[2];
        cartesJeu = new CarteComponent[9]; // 9 Car si 2 couleurs consécutives, il y a un décallage
        tapis = new CarteComponent[PartieBelote.NB_JOUEUR];
        plis = new CarteComponent[PartieBelote.NB_JOUEUR / 2];
        dialogOpen = false;
        lastEtat = -1;
        nbCarteTapis = 0;
        lstVisible = new Vector();
        lstInvisible = new Vector();
        
        // Chargement des éléments
        try {
            imageFd = salle.getImage(salle.getSkinConfig().getSFond());
        }
        catch (MalformedURLException e) {
            salle.getLogger().severe("URL fond salle invalide");
        }
        
        try {
            dosCarte = salle.getImageIcon(salle.getSkinConfig().getSDos());
        }
        catch (MalformedURLException e) {
            salle.getLogger().severe("URL du dos invalide");
        }
        
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        System.runFinalization();
        System.gc();
    }

    /**
     * Initialise les éléments graphiques et affiche la salle
     */
    public void init() {
        // Pas de layout pour positionner directement les éléments
        setLayout(null);
        setOpaque(true);
        
        // Ajout tous les éléments
        addEveryComponents();
        
        setVisible(true);
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    
    /**
     * Ajoute tous les composants d'affichage
     *
     */
    protected void addEveryComponents() {

        for (int i = 0; i < pseudoJoueur.length; i++) {
            add(getPseudoJoueur(i));
        }
    
        for (int i = 0; i < avatarJoueur.length; i++) {
            add(getAvatarJoueur(i));
        }

        addScrollEvent();

        for (int i = 0; i < score.length; i++) {
            add(getScore(i));
        }
    
        add(getPreneur());
        
        addTasDistribution();
        add(getCurseurCoupe());
        add(getBoutonValide());
        
        add(getBouton2Cartes());
        add(getBouton3Cartes());
        
        addCartesJeu();
        addBoutonsPrise();
        addTapis();
        addPlis();
        
        addChat();
        
        add(getSphere());
        add(getBoutonPlis());
    }

    /**
     * Crée le composant sphere
     * @return composant créé
     */
    private SphereComponent getSphere() {
        sphere = new SphereComponent(this, pseudoJoueur.length);
        
        sphere.setBounds(salle.getSkinConfig().getSSphere());
        try {
            sphere.setAiguille(salle.getRelativeImage(salle.getSkinConfig().getSSphereAiguille()));
        }
        catch (MalformedURLException e) {
            salle.getLogger().severe("Chemin de l'aiguille invalide : " + salle.getSkinConfig().getSSphereAiguille());
        }
        try {
            sphere.setFond(salle.getRelativeImage(salle.getSkinConfig().getSSphereFond()));
        }
        catch (MalformedURLException e) {
            salle.getLogger().severe("Chemin invalide : " + salle.getSkinConfig().getSSphereFond());
        }

        sphere.setTextFont(salle.getSkinConfig().getSSphereText());
        sphere.setTextColorDSel(salle.getSkinConfig().getSSphereColorDSel());
        sphere.setTextColor(salle.getSkinConfig().getSSphereColor());
        sphere.setTextColorMSel(salle.getSkinConfig().getSSphereColorMSel());
        
        return sphere;
    }
    
    /**
     * Affiche le preneur
     * @return Composant créé
     */
    private Component getPreneur() {
        preneur = new JLabel();
        preneur.setText ("");
        preneur.setFont(salle.getSkinConfig().getSFont().deriveFont(Font.BOLD));
        preneur.setForeground(Color.WHITE);
        preneur.setBounds(salle.getSkinConfig().getSPreneur());
        return preneur;
    }

    /**
     * Affiche les scores
     * @param numJoueur score concerné
     * @return Composant créé
     */
    private Component getScore(int numJoueur) {
        // Texte de présentation de la partie
        score[numJoueur] = new JLabel();
        score[numJoueur].setFont(salle.getSkinConfig().getSFont());
        score[numJoueur].setForeground(Color.BLACK);
        Rectangle position;
        Color couleur;
        switch (numJoueur) {
            case Joueur.EST :
                position = salle.getSkinConfig().getSScoreE();
                couleur = salle.getSkinConfig().getSScoreEColor();
                break;

            case Joueur.NORD :
                position = salle.getSkinConfig().getSScoreN();
                couleur = salle.getSkinConfig().getSScoreNColor();
                break;

            case Joueur.SUD :
                position = salle.getSkinConfig().getSScoreS();
                couleur = salle.getSkinConfig().getSScoreSColor();
                break;

            case Joueur.OUEST :
                position = salle.getSkinConfig().getSScoreO();
                couleur = salle.getSkinConfig().getSScoreOColor();
                break;

            default :
                position = null;
                couleur = null;
                break;
        }
        score[numJoueur].setBounds(position);
        score[numJoueur].setForeground(couleur);
        score[numJoueur].getInsets(new Insets(0, 0, 0, 0));
        return score[numJoueur];
    }

    /**
     * Construit la zone de défilement pour afficher les événements
     */
    protected void addScrollEvent() {
        scrollEvent = new DeroulementPanel(this, salle.getAire());
    }

    /**
     * Affiche l'avatar du joueur
     * @param numJoueur Indice du joueur 
     * @return Composant créé
     */
    private Component getAvatarJoueur(int numJoueur) {
        // Texte de présentation de la partie
        avatarJoueur[numJoueur] = new JLabel();
        Rectangle position;
        switch (numJoueur) {
            case Joueur.EST :
                position = salle.getSkinConfig().getSJoueurEAvatar();
                break;

            case Joueur.NORD :
                position = salle.getSkinConfig().getSJoueurNAvatar();
                break;

            case Joueur.SUD :
                position = salle.getSkinConfig().getSJoueurSAvatar();
                break;

            case Joueur.OUEST :
                position = salle.getSkinConfig().getSJoueurOAvatar();
                break;

            default :
                position = null;
                break;
        }
        avatarJoueur[numJoueur].setBounds(position);
        avatarJoueur[numJoueur].getInsets(new Insets(0, 0, 0, 0));
        avatarJoueur[numJoueur].setHorizontalAlignment(SwingConstants.CENTER);
        avatarJoueur[numJoueur].setVerticalAlignment(SwingConstants.CENTER);
        return avatarJoueur[numJoueur];
    }

    /**
     * Affiche le pseudo d'un des joueurs
     * @param numJoueur Indice du joueur
     * @return Composant créé
     */
    private Component getPseudoJoueur(int numJoueur) {
        // Texte de présentation de la partie
        pseudoJoueur[numJoueur] = new JLabel();
        pseudoJoueur[numJoueur].setFont(salle.getSkinConfig().getSFont().deriveFont(Font.BOLD));
        pseudoJoueur[numJoueur].setForeground(Color.WHITE);
        Rectangle position;
        switch (numJoueur) {
            case Joueur.EST :
                position = salle.getSkinConfig().getSJoueurENom();
                pseudoJoueur[numJoueur].setHorizontalAlignment(SwingConstants.RIGHT);
                break;

            case Joueur.NORD :
                position = salle.getSkinConfig().getSJoueurNNom();
                pseudoJoueur[numJoueur].setHorizontalAlignment(SwingConstants.CENTER);
                break;

            case Joueur.SUD :
                position = salle.getSkinConfig().getSJoueurSNom();
                pseudoJoueur[numJoueur].setHorizontalAlignment(SwingConstants.CENTER);
                break;

            case Joueur.OUEST :
                position = salle.getSkinConfig().getSJoueurONom();
                pseudoJoueur[numJoueur].setHorizontalAlignment(SwingConstants.LEFT);
                break;

            default :
                position = null;
                break;
        }
        pseudoJoueur[numJoueur].setBounds(position);
                        
        return pseudoJoueur[numJoueur];
    }

    /**
     * Ajoute le chat
     */
    protected void addChat() {
        chat = new ChatPanelPartie(this, salle.getAire(), salle);
    }


    /**
     * Création des tas de distribution
     */
    private void addTasDistribution() {
        
        tasDistribution[0] = new CarteComponent(this, (Carte) salle.getMyPartie().getJeu().getTas().get(0), 0d);
        tasDistribution[0].setImageDos(dosCarte.getImage());
        tasDistribution[0].setBounds(salle.getSkinConfig().getSDistribution1());
        tasDistribution[0].setVisible(false);
        add(tasDistribution[0]);

        tasDistribution[1] = new CarteComponent(this, (Carte) salle.getMyPartie().getJeu().getTas().get(0), 0d);
        tasDistribution[1].setImageDos(dosCarte.getImage());
        tasDistribution[1].setFace(false);
        tasDistribution[1].setBounds(salle.getSkinConfig().getSDistribution2());
        tasDistribution[1].setVisible(false);
        add(tasDistribution[1]);

    }

    /**
     * Construit le pointeur de saisie de la coupe
     * @return curseur de saisie de la coupe
     */
    private JSlider getCurseurCoupe() {
        
        curseurCoupe = new JSlider(1, 31, (int) (10 + Math.random() * 21));
        curseurCoupe.setBounds(salle.getSkinConfig().getSCurseur());
        curseurCoupe.setBackground(new Color(0, 0, 0, 0));
        curseurCoupe.setOpaque(false);
        curseurCoupe.setVisible(false);
        return curseurCoupe;
    }

    /**
     * Construit le bouton Valider
     * @return bouton construit
     */
    private JButton getBoutonValide() {
        boutonValide = new JButton();
        if (salle.getSkinConfig().getSValideImg() == null) {
            boutonValide.setText("Ok");
            boutonValide.setFont(salle.getSkinConfig().getSFont().deriveFont(Font.PLAIN));
            boutonValide.setBackground(salle.getSkinConfig().getRBoutonGlobalColor());
        }
        else {
            try {
                boutonValide.setIcon(salle.getImageIcon(salle.getSkinConfig().getSValideImg()));
            }
            catch (MalformedURLException e) {
                salle.getLogger().severe("Image bouton valide erronée");
            }
            boutonValide.setToolTipText("Ok");
        }
        if (!salle.getSkinConfig().isRBoutonGlocalBorder()) {
            boutonValide.setBorder(BorderFactory.createEmptyBorder());
        }
        boutonValide.setBounds(salle.getSkinConfig().getSValide());
        boutonValide.getInsets(new Insets(0, 0, 0, 0));
        boutonValide.setVisible(false);
        boutonValide.addActionListener(this);
        return boutonValide;
    }

    /**
     * Construit le bouton pour voir les plus
     * @return bouton construit
     */
    private JButton getBoutonPlis() {
        boutonPlis = new JButton();
        if (salle.getSkinConfig().getSBoutonPlisImg() == null) {
            boutonPlis.setText("Dernier pli...");
            boutonPlis.setBackground(salle.getSkinConfig().getRBoutonGlobalColor());
            boutonPlis.setFont(salle.getSkinConfig().getSFont().deriveFont(Font.PLAIN, salle.getSkinConfig().getSFont().getSize() - 2));
        }
        else {
            try {
                boutonPlis.setIcon(salle.getImageIcon(salle.getSkinConfig().getSBoutonPlisImg()));
            }
            catch (MalformedURLException e) {
                salle.getLogger().severe("Image bouton dernier plis erronée");
            }
            boutonPlis.setToolTipText("Dernier pli...");
        }
        if (!salle.getSkinConfig().isRBoutonGlocalBorder()) {
            boutonPlis.setBorder(BorderFactory.createEmptyBorder());
        }
        boutonPlis.setBounds(salle.getSkinConfig().getSBoutonPlis());
        boutonPlis.getInsets(new Insets(0, 0, 0, 0));
        boutonPlis.setVisible(false);
        boutonPlis.addActionListener(this);
        return boutonPlis;
    }
    
    /**
     * Construit le bouton de choix de 2 cartes en premier
     * @return bouton construit
     */
    private JButton getBouton2Cartes() {
        bouton2Cartes = new JButton();
        if (salle.getSkinConfig().getSBouton2CartesImg() == null) {
            bouton2Cartes.setText("2 & 3");
            bouton2Cartes.setBackground(salle.getSkinConfig().getRBoutonGlobalColor());
            bouton2Cartes.setFont(salle.getSkinConfig().getSFont().deriveFont(Font.PLAIN));
        }
        else {
            try {
                bouton2Cartes.setIcon(salle.getImageIcon(salle.getSkinConfig().getSBouton2CartesImg()));
            }
            catch (MalformedURLException e) {
                salle.getLogger().severe("Image bouton 2 cartes erronée");
            }
        }
        if (!salle.getSkinConfig().isRBoutonGlocalBorder()) {
            bouton2Cartes.setBorder(BorderFactory.createEmptyBorder());
        }
        bouton2Cartes.setToolTipText("Distribution de 2 puis de 3 cartes");
        bouton2Cartes.setBounds(salle.getSkinConfig().getSBouton2Cartes());
        bouton2Cartes.getInsets(new Insets(0, 0, 0, 0));
        bouton2Cartes.setVisible(false);
        bouton2Cartes.addActionListener(this);
        return bouton2Cartes;
    }

    /**
     * Construit le bouton de choix de 3 cartes en premier
     * @return bouton construit
     */
    private JButton getBouton3Cartes() {
        bouton3Cartes = new JButton();
        if (salle.getSkinConfig().getSBouton3CartesImg() == null) {
            bouton3Cartes.setText("3 & 2");
            bouton3Cartes.setBackground(salle.getSkinConfig().getRBoutonGlobalColor());
            bouton3Cartes.setFont(salle.getSkinConfig().getSFont().deriveFont(Font.PLAIN));
        }
        else {
            try {
                bouton3Cartes.setIcon(salle.getImageIcon(salle.getSkinConfig().getSBouton3CartesImg()));
            }
            catch (MalformedURLException e) {
                salle.getLogger().severe("Image bouton valide erronée");
            }
            bouton3Cartes.setToolTipText("Ok");
        }
        if (!salle.getSkinConfig().isRBoutonGlocalBorder()) {
            bouton3Cartes.setBorder(BorderFactory.createEmptyBorder());
        }
        bouton3Cartes.setToolTipText("Distribution de 3 puis de 2 cartes");
        bouton3Cartes.setBounds(salle.getSkinConfig().getSBouton3Cartes());
        bouton3Cartes.getInsets(new Insets(0, 0, 0, 0));
        bouton3Cartes.setVisible(false);
        bouton3Cartes.addActionListener(this);
        return bouton3Cartes;
    }

    /**
     * Création des cartes du jeu
     */
    private void addCartesJeu() {
        
        CarteComponent.setHauteurCarte(salle.getSkinConfig().getSCarte().height);
        CarteComponent.setLargeurCarte(salle.getSkinConfig().getSCarte().width);
        
        for (int i = 0; i < cartesJeu.length; i++) {
            cartesJeu[i] = new CarteComponent(this, new Couleur(), 
                                        salle.getSkinConfig().getSRepereJeu().x,
                                        salle.getSkinConfig().getSRepereJeu().y,
                                        salle.getSkinConfig().getSAlphaJeu() * (3 - i),
                                        salle.getSkinConfig().getSRayonJeu());
            cartesJeu[i].setImageDos(dosCarte.getImage());
            cartesJeu[i].setVisible(false);
            add(cartesJeu[i]);
        }
    }

    /**
     * Création du tapis 
     */
    private void addTapis() {
        
        for (int i = 0; i < tapis.length; i++) {
            tapis[i] = new CarteComponent(this, new Couleur(),  0);
            tapis[i].setBounds(salle.getSkinConfig().getSTapis(i));
            tapis[i].setImageDos(dosCarte.getImage());
            tapis[i].setFace(true);
            tapis[i].setVisible(false);
            add(tapis[i]);
        }
    }

    /**
     * Création d'un des plis
     * @param iPlis plis à créer
     */
    private void addPlis(int iPlis) {

        double angle;
        Image imagePlis = null;
        if (salle.getSkinConfig().getSImagePlis(iPlis) != null) {
            // Une image pour représenter un pli
            try {
                imagePlis = salle.getImage(salle.getSkinConfig().getSImagePlis(iPlis));
            }
            catch (MalformedURLException e) {
                salle.getLogger().severe("Image pli erronée");
            }
        }
        else {
            imagePlis = dosCarte.getImage();
        }

        boolean imageDebout = (imagePlis.getWidth(this) < imagePlis.getHeight(this));
        if (imageDebout 
            && (salle.getSkinConfig().getSPlis(iPlis).getWidth() < salle.getSkinConfig().getSPlis(iPlis).getHeight())) {        
            // Les 2 sont debouts
            angle = 0;
        }
        else {
            angle = -Math.PI / 2;
        }

        plis[iPlis] = new CarteComponent(this, new Couleur(), angle);
        plis[iPlis].setBounds(salle.getSkinConfig().getSPlis(iPlis));
        plis[iPlis].setImageDos(imagePlis);
        plis[iPlis].setFace(false);
        plis[iPlis].setVisible(false);
        add(plis[iPlis]);
    }

    /**
     * Création des plis 
     */
    private void addPlis() {
        addPlis(0);
        addPlis(1);
    }

    /**
     * Création des boutons de prise
     */
    protected abstract void addBoutonsPrise();
    
    /**
     * @return Nombre de boutons pour la prise
     */
    protected int getNbBoutonPrise() {
        return 5;
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
            
            // Dessine les cartes : Pour être sur qu'elles sont dans le bon ordre
            for (int i = 0; i < nbCarteTapis; i++) {
                int pos = toLocalIndice(i + ouvreurCourant);
                g.translate(tapis[pos].getLocation().x, 
                            tapis[pos].getLocation().y);
                tapis[pos].paint(graphics);
                g.translate(-tapis[pos].getLocation().x, 
                            -tapis[pos].getLocation().y);
            }
        }
    }

    /**
     * Rafraichit les données affichées
     * @param offset décallage des joueurs permettant d'être toujours en sud
     */
    public void refresh(int offset) {

        int maitre = -1;
        
        // Sauvegarde la valeur pour les actions à répercuter
        offsetJoueur = offset;

        // Log Optimisation Vitesse
        salle.getLogger().finest(System.currentTimeMillis() + " : Début refresh salle");
        PartieBelote myPartie = salle.getMyPartie();

        int nbJoueur = pseudoJoueur.length;

        for (int i = 0; i < pseudoJoueur.length; i++) {
            if (myPartie.getParticipant(i) != null) {
                pseudoJoueur[toLocalIndice(i)].setText(myPartie.getParticipant(i).getPseudo());
            }
            else {
                pseudoJoueur[toLocalIndice(i)].setText(UNKNOWN);
            }
        }

        for (int i = 0; i < avatarJoueur.length; i++) {
            if (myPartie.getParticipant(i) != null) {
                ImageIcon avatarImg;
                try {
                    avatarImg = salle.getImageIcon(myPartie.getParticipant(i).getAvatar().toString());
                    if ((avatarJoueur[toLocalIndice(i)].getWidth() != avatarImg.getIconWidth())
                            || (avatarJoueur[toLocalIndice(i)].getHeight() != avatarImg.getIconHeight())) {
                        // Retaille l'image afin qu'il n'y ait pas à la transformer, ce qui améliore les perfs
                        avatarJoueur[toLocalIndice(i)].setSize(avatarImg.getIconWidth(), avatarImg.getIconHeight());
                    }
                    avatarJoueur[toLocalIndice(i)].setIcon(avatarImg);
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            else {
                avatarJoueur[toLocalIndice(i)].setIcon(null);
            }
        }

        // Score par équipe
        for (int i = 0; i < (pseudoJoueur.length / 2); i++) {
            score[(i - offset + nbJoueur) % (nbJoueur / 2)].setText(Integer.toString(myPartie.getCurrentScore(i)));
        }
    
        if ((myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_JOUE_CARTE)
                || (myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_FIN_TOUR)
                || (myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_TOUR_ENCHERE1_PRIS)
                || (myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_TOUR_ENCHERE2_PRIS)) {
            // Affiche le preneur
            if (myPartie.getParticipant(myPartie.getPreneur()) != null) {
                preneur.setText(myPartie.getParticipant(myPartie.getPreneur()).getPseudo() + " prend à " + myPartie.atoutToString());
            }
            else {
                preneur.setText("? prend à " + myPartie.atoutToString());
            }
        }
        else {
            preneur.setText("");
        }

        if (ramassageDone 
                || (toLocalIndice(myPartie.getEtat().getJoueur()) == 0) 
                || (myPartie.getPositionJoueur(salle.getMyJoueur()) < 0)) {
            // N'effectue cette partie que si aucun ramassage est en cours
            // Sauf si le joueur doit faire qq chose ou c'est un spectateur         
            /**
             * Première phase : Coupe
             */
            if (myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_COUPE_JEU) { 
                // Il faut couper : Présente le dos des cartes avec le slider pour déterminer où
            
                if ((myPartie.getPositionJoueur(salle.getMyJoueur()) >= 0) 
                    && (toLocalIndice(myPartie.getEtat().getJoueur()) == 0)) {
                    // Seulement si c'est le joueur connecté qui doit le faire
                    lstVisible.add(curseurCoupe);
                    lstVisible.add(boutonValide);
                }
                else {
                    lstInvisible.add(curseurCoupe);
                    lstInvisible.add(boutonValide);
                }
                            
                Couleur aCarte = (Couleur) myPartie.getJeu().getTas().get(0);
                tasDistribution[0].setMyCarte(aCarte);
                try {
                    tasDistribution[0].setImageCarte(salle.getRelativeImage("/images/" + aCarte.toString() + ".gif"));
                }
                catch (MalformedURLException e) {
                    salle.getLogger().severe("Chemin de la carte invalide : " + "/images/" + aCarte.toString() + ".gif");
                }
                tasDistribution[0].setFace(false);
                lstVisible.add(tasDistribution[0]);
                lstVisible.add(tasDistribution[1]);
            }
            else {
                // Cache les éléments
                lstInvisible.add(tasDistribution[0]);
                lstInvisible.add(tasDistribution[1]);
                lstInvisible.add(curseurCoupe);
                lstInvisible.add(boutonValide);
            }

            /**
             * Deuxième phase : Distribution
             */
            if ((myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_DISTRIBUTION) 
                && (toLocalIndice(myPartie.getEtat().getJoueur()) == 0)
                && (myPartie.getPositionJoueur(salle.getMyJoueur()) >= 0)) {
                // Il faut savoir si on commence par 2 ou 3 cartes
                // Seulement si c'est le joueur connecté qui doit le faire
                // Affiche les boutons de choix
                lstVisible.add(bouton2Cartes);
                lstVisible.add(bouton3Cartes);
            }
            else {            
                // Cache les éléments
                lstInvisible.add(bouton2Cartes);
                lstInvisible.add(bouton3Cartes);
                bouton2Cartes.setEnabled(true);
                bouton3Cartes.setEnabled(true);
            }

            /**
             * Troisième phase : Partie 1 choix enchère
             */
            if (((myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_TOUR_ENCHERE1) 
                || (myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_TOUR_ENCHERE1_PRIS)
                || (myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_TOUR_ENCHERE2)
                || (myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_TOUR_ENCHERE2_PRIS))) {

                if (myPartie.getPositionJoueur(salle.getMyJoueur()) >= 0) {
                    // Affiche les boutons de prise : Si c'est à sud de dire
                    if (toLocalIndice(myPartie.getEtat().getJoueur()) == 0) {
                        for (int i = 0; i < getNbBoutonPrise(); i++) {
                            lstVisible.add(boutonsPrise[i]);
                            if (i != 0) {
                                // Grise le bouton si besoin 
                                boutonsPrise[i].setEnabled(myPartie.peutPrendreA(i - 1));
                            }
                        }
                    }
                    else {
                        for (int i = 0; i < getNbBoutonPrise(); i++) {
                            lstInvisible.add(boutonsPrise[i]);
                            boutonsPrise[i].setEnabled(true);
                        }
                    }
        
                }
                
                // Affiche la carte proposée
                Couleur aCarte = (Couleur) myPartie.getJeu().getTas().get(0);
                tasDistribution[0].setMyCarte(aCarte);
                try {
                    tasDistribution[0].setImageCarte(salle.getRelativeImage("/images/" + aCarte.toString() + ".gif"));
                }
                catch (MalformedURLException e) {
                    salle.getLogger().severe("Chemin de la carte invalide : " + "/images/" + aCarte.toString() + ".gif");
                }
                tasDistribution[0].setFace(true);
                lstVisible.add(tasDistribution[0]);
                lstInvisible.remove(tasDistribution[0]);
            }
            else {
                for (int i = 0; i < getNbBoutonPrise(); i++) {
                    lstInvisible.add(boutonsPrise[i]);
                    boutonsPrise[i].setEnabled(true);
                }
                // Inutile de cachez tasDistribution[0] car cela est fait par ailleurs => Sinon perturbations
            }
        
            /**
             * Troisième phase : Partie 2 affichage jeu
             */
            if ((myPartie.getPositionJoueur(salle.getMyJoueur()) >= 0)
                && ((myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_TOUR_ENCHERE1) 
                || (myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_TOUR_ENCHERE1_PRIS)
                || (myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_TOUR_ENCHERE2)
                || (myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_TOUR_ENCHERE2_PRIS)
                || (myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_JOUE_CARTE)
                || (myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_FIN_TOUR)
                || (myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_FIN_DONNE))) {

                int nbCarte = myPartie.getJeu().getMains(toServeurIndice(0)).size();
                int centrage = (8 - nbCarte) / 2;

                // Cache les premières cartes            
                for (int i = 0; i < centrage; i++) {
                    lstInvisible.add(cartesJeu[i]);
                }
            
                int lastCouleur = -1;
                for (int i = 0; i < nbCarte; i++) {
                    Couleur aCarte = (Couleur) myPartie.getJeu().getMains(toServeurIndice(0)).get(i);
                
                    if ((lastCouleur != aCarte.getCouleur()) && ((lastCouleur % 2) == (aCarte.getCouleur() % 2))) {
                        // Décallage car 2 couleurs identiques se suivent
                        lstInvisible.add(cartesJeu[i + centrage]);
                        centrage++;
                    }
                    try {
                        cartesJeu[i + centrage].setImageCarte(salle.getRelativeImage("/images/" + aCarte.toString() + ".gif"));
                    }
                    catch (MalformedURLException e) {
                        salle.getLogger().severe("Chemin de la carte invalide : " + "/images/" + aCarte.toString() + ".gif");
                    }
                    cartesJeu[i + centrage].setMyCarte(aCarte);
                    cartesJeu[i + centrage].setFace(true);
                    lstVisible.add(cartesJeu[i + centrage]);
                    lastCouleur = aCarte.getCouleur();
                }
            
                // Cache les dernières cartes            
                for (int i = centrage + nbCarte; i < cartesJeu.length; i++) {
                    lstInvisible.add(cartesJeu[i]);
                }
            
                // Cache l'avatar et le pseudo de SUD une fois les cartes distribuées
                lstInvisible.add(avatarJoueur[0]);
                lstInvisible.add(pseudoJoueur[0]);
            }
            else {
                // Restaure les éléments
                lstVisible.add(avatarJoueur[0]);
                lstVisible.add(pseudoJoueur[0]);
                for (int i = 0; i < cartesJeu.length; i++) {
                    lstInvisible.add(cartesJeu[i]);
                }
            }

            /**
             * Troisième phase : Partie 3 affichage dernier pli
             */
            if ((myPartie.getPositionJoueur(salle.getMyJoueur()) >= 0)
                && ((myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_JOUE_CARTE)
                || (myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_FIN_TOUR)
                || (myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_FIN_DONNE))) {

                // Affiche les plis si nécessaire
                for (int i = 0; i < PartieBelote.NB_JOUEUR / 2; i++) {
                    if (myPartie.getJeu().getPlis(i).size() > 0) {
                        lstVisible.add(plis[toLocalIndice(i) % 2]);
                        lstVisible.add(boutonPlis);
                    }
                }
            
            }
            else {
                // Restaure les éléments
                lstInvisible.add(plis[0]);
                lstInvisible.add(plis[1]);
                lstInvisible.add(boutonPlis);
            }

            /**
             * 4ème phase : On joue !
             */
            if ((myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_JOUE_CARTE)
                || (myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_FIN_TOUR)) {
                
                ouvreurCourant = myPartie.getOuvreur();
                for (int i = 0; i < myPartie.getJeu().getTapis().size(); i++) {
                    Couleur aCarte = (Couleur) myPartie.getJeu().getTapis().get(i);
            
                    try {
                        tapis[toLocalIndice(i + ouvreurCourant)].setImageCarte(salle.getRelativeImage("/images/" + aCarte.toString() + ".gif"));
                    }
                    catch (MalformedURLException e) {
                        salle.getLogger().severe("Chemin de la carte invalide : " + "/images/" + aCarte.toString() + ".gif");
                    }
                    tapis[toLocalIndice(i + ouvreurCourant)].setMyCarte(aCarte);
                    tapis[toLocalIndice(i + ouvreurCourant)].setFace(true);
                
                    // Les éléments sont toujours invisibles : C'est le paint qui les affiche
                }
                nbCarteTapis = myPartie.getJeu().getTapis().size();
            }
            else {
                nbCarteTapis = 0;
            }

            /**
             * Ne positionne le maitre qu'en fin de tour
             */
            if (myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_FIN_TOUR) {
                maitre = toLocalIndice(myPartie.getEtat().getJoueur());
            }
            // Log Optimisation Vitesse
            salle.getLogger().finest(System.currentTimeMillis() + " : Fin refresh part1");
            sphere.setState(toLocalIndice((myPartie.getEtat().getJoueurCoupe() + 1) % pseudoJoueur.length), 
                            maitre, 
                            toLocalIndice(myPartie.getEtat().getJoueur()));

        }

        
        scrollEvent.refresh();
        chat.refresh();
        // Log Optimisation Vitesse
        salle.getLogger().finest(System.currentTimeMillis() + " : Fin refresh part2");

        /**
         * Affichage groupé pour limiter les problèmes de multithread avec Swing
         */
        for (int i = 0; i < lstInvisible.size(); i++) {
            ((JComponent) lstInvisible.get(i)).setVisible(false);
        }
        for (int i = 0; i < lstVisible.size(); i++) {
            ((JComponent) lstVisible.get(i)).setVisible(true);
        }
        lstVisible.clear();
        lstInvisible.clear();
             
        revalidate();
        repaint();

        // Log Optimisation Vitesse
        salle.getLogger().finest(System.currentTimeMillis() + " : Avant dialog");
        
        // Le reste n'est que pour les joueurs : Pas pour les spectateurs
        if (myPartie.getPositionJoueur(salle.getMyJoueur()) >= 0) {
            if ((myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_FIN_DONNE) 
                    && (myPartie.getEtat().getEtat() != lastEtat)) {

/** Suppression de la Popup d'annonce de fin de donne                
                if (!dialogOpen) {
                    // Affiche le score
                    dialogOpen = true;
                    String msg = "";
                    int scorePreneur = myPartie.getScoreDonne()[myPartie.getPreneur() % 2];
                    int scoreAutre = myPartie.getScoreDonne()[(myPartie.getPreneur() + 1) % 2]; 
                    if (scorePreneur > scoreAutre) {
                        msg = "La prise est gagnée : " + scorePreneur + " contre " + scoreAutre; 
                    }
                    else if (scorePreneur < scoreAutre) {
                        msg = "La prise est perdue : " + scorePreneur + " contre " + scoreAutre; 
                    }
                    else {
                        msg = "La prise est en litige : " + scorePreneur + " contre " + scoreAutre; 
                    }
                    JOptionPane.showMessageDialog(this, msg, "Fin de la donne", JOptionPane.INFORMATION_MESSAGE); */

                    // Passe à la donne suivate
                    salle.nextDonne();

/**                } */
            }
            else {
                dialogOpen = false;
            }
    
            if ((myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_FIN_PARTIE)
                    && (myPartie.getEtat().getEtat() != lastEtat)) {
                
                if (!dialogOpen) {
                    // Affiche le score et demande si on rejoue : Pour tous les joueurs
                    dialogOpen = true;
                    String msg = "";
                    int scoreNous = myPartie.getCurrentScore(toServeurIndice(0) % 2);
                    int scoreEux = myPartie.getCurrentScore(toServeurIndice(1) % 2); 
                    if (scoreNous > scoreEux) {
                        msg = "La partie est gagnée : " + scoreNous + " contre " + scoreEux; 
                    }
                    else if (scoreNous < scoreEux) {
                        msg = "La partie est perdue : " + scoreNous + " contre " + scoreEux; 
                    }
                    else {
                        msg = "La partie est à égalité : " + scoreNous + " contre " + scoreEux; 
                    }
                    msg = msg + ". Voulez-vous rejouer avec ces mêmes joueurs ?";
                    salle.replay(JOptionPane.showConfirmDialog(this, msg, "Fin de la partie", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
                }
            }
            else {
                dialogOpen = false;
            }
    
            // Le timer de ramasse est systématique : Ce n'est pas le maitre qui commande
            if ((myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_FIN_TOUR) 
                    || (myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_FIN_PARTIE)) {
                if (myPartie.getEtat().getEtat() != lastEtat) {
                    // Ramassage automatique des cartes
                    ramassageDone = false;
                    if (autoRamasse == null) {
                        if (tempoRamasse < 0) {
                            // Chargement pour la première fois de la tempo de ramassage
                            ResourceBundle resConfig = ResourceBundle.getBundle("configAire");
                            tempoRamasse = Integer.parseInt(resConfig.getString("tempoRamassage"));
                        }
                        autoRamasse = new Timer(tempoRamasse, this);
                        autoRamasse.setRepeats(false);
                        autoRamasse.setCoalesce(true);
                    }
                    if (!autoRamasse.isRunning()) {
                        autoRamasse.start();
                    }
                }
                // Sinon laisse le ramassage auto comme il est
            }
            else {
                ramassageDone = true;
                if ((autoRamasse != null) && (autoRamasse.isRunning())) {
                    autoRamasse.stop();
                }
                
            }

            // Indicateur si c'est à sud de jouer   
            if ((myPartie.getEtat().getEtat() != EtatPartieBelote.ETAT_NON_DEMARRE) 
                    && (myPartie.getEtat().getEtat() != EtatPartieBelote.ETAT_FIN_PARTIE)) {         
                chat.setWarning(toLocalIndice(myPartie.getEtat().getJoueur()) == 0);
            }
            else {
                chat.setWarning(false);
            }
        }
                
        /**
         * A la fin : Conserve l'état qui vient d'être traité
         */        
        lastEtat = myPartie.getEtat().getEtat();
        
        // Restaure le pointeur
        setCursor(Cursor.getDefaultCursor());
    }

    /**
     * Calcul l'indice d'un joueur en local
     * @param i Indice au niveau du serveur
     * @return Indice converti
     */
    protected int toLocalIndice(int i) {
        int nbJoueur = pseudoJoueur.length;
         
        return (i - offsetJoueur + nbJoueur) % nbJoueur;
    }

    /**
     * Calcul l'indice d'un joueur local en serveur
     * @param i Indice au niveau du local
     * @return Indice converti
     */
    protected int toServeurIndice(int i) {
        int nbJoueur = pseudoJoueur.length;
         
        return (i + offsetJoueur) % nbJoueur;
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
        
        // Demande de confirmation : Seulement si c'est un joueur
        if ((salle.getMyPartie().getIdentifiant() > 0) && (salle.isJoueurActif())) {
            if (JOptionPane.showConfirmDialog(this, "Vous allez quitter cette partie. Etes-vous sûr ?", "Quitter une partie", JOptionPane.YES_NO_OPTION)
                    == JOptionPane.YES_OPTION) {
                // Quitte
                salle.quitte();
            }
        }
        else {
            salle.quitte();
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
     * Ajoute un chat à afficher
     * @param aChat chat à afficher
     */
    public void addPendingChat(Chat aChat) {
        if (aChat.getJoueurOrig() == null) {
            scrollEvent.addPendingChat(aChat);
        }
        else {
            chat.addPendingChat(aChat);
        }
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == boutonValide) {
            salle.setCoupe(curseurCoupe.getValue());
            curseurCoupe.setValue((int) (10 + Math.random() * 21));
        }
        else if (e.getSource() == bouton2Cartes) {
            bouton2Cartes.setEnabled(false);
            bouton3Cartes.setEnabled(false);
            salle.setDistribution(2);
        }
        else if (e.getSource() == bouton3Cartes) {
            bouton2Cartes.setEnabled(false);
            bouton3Cartes.setEnabled(false);
            salle.setDistribution(3);
        }
        else if (e.getSource() == boutonPlis) {
            salle.affichePlis();
        }
        else if (e.getSource() == autoRamasse) {
            // Ramasse par fin du Timer
            if ((!ramassageDone) 
                && (salle.getMyPartie().getEtat().getEtat() == EtatPartieBelote.ETAT_FIN_TOUR)
                && (toLocalIndice(salle.getMyPartie().getEtat().getJoueur()) == 0)) {
                // Effectue le ramassage
                ramassageDone = true;
                salle.setRamasseJeu(1);
            }
            ramassageDone = true;
            // Raffiche l'écran
            refresh(offsetJoueur);
        }
        else {
            // Recherche parmis les boutons de prise
            boolean found = false;
            for (int i = 0 ; i < getNbBoutonPrise(); i++) {
                if (e.getSource() == boutonsPrise[i]) {
                    if (i == 0) {
                        salle.setNonDecision();
                        found = true;
                    }
                    else {
                        salle.setDecision(i - 1);
                        found = true;
                    }
                }
            }
            
            if (found) {
                for (int i = 0 ; i < getNbBoutonPrise(); i++) {
                    boutonsPrise[i].setEnabled(false);
                }                
            }
        }
    }

    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent e) {
        // Traitement dans mouseReleased        
    }

    /**
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            // Mémorise le point pressé
            positionSouris = e.getPoint();
        }
    }

    /**
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            // Log Optimisation Vitesse
            salle.getLogger().finest(System.currentTimeMillis() + " : Clic souris");
            // Traite comme un clic
            boolean found = false;
            // Vérification que c'est bien à son tour de jouer
            if (((salle.getMyPartie().getEtat().getEtat() == EtatPartieBelote.ETAT_JOUE_CARTE)
                    || (salle.getMyPartie().getEtat().getEtat() == EtatPartieBelote.ETAT_FIN_TOUR))
                && (toLocalIndice(salle.getMyPartie().getEtat().getJoueur()) == 0)) {
                /**
                 * Recherche le premiere en fonction de la visibilité : Ordre inverse nécessaire
                 */
                for (int i = 0; (i < cartesJeu.length); i++) {
                    cartesJeu[i].setInverse(false);
                }
                for (int i = 0; !found && (i < cartesJeu.length); i++) {
                    // Si même carte que le clic initial
                    if ((cartesJeu[i].isOnCarte(positionSouris)) 
                            && (cartesJeu[i].isOnCarte(e.getPoint()))) {
                        // Ramassage à ignorer
                        ramassageDone = true;
                        if ((autoRamasse != null) && (autoRamasse.isRunning())) {
                            autoRamasse.stop();
                        }
                        // Joue la carte
                        salle.setCarteJouee((Couleur) cartesJeu[i].getMyCarte());
                        found = true;
                    }
                }
            }
            if (!found
                && (salle.getMyPartie().getDernierRamasse() >= 0)
                && (plis[toLocalIndice(salle.getMyPartie().getDernierRamasse()) % 2].getBounds()
                    .contains(positionSouris))
                && (plis[toLocalIndice(salle.getMyPartie().getDernierRamasse()) % 2].getBounds()
                        .contains(e.getPoint()))
                ) {
                // Affiche le dernier plis (clic sur le tas de carte ramassé)
                salle.affichePlis();
                found = true;
            }
            if (!found 
                && (salle.getMyPartie().getEtat().getEtat() == EtatPartieBelote.ETAT_FIN_TOUR)
                && (toLocalIndice(salle.getMyPartie().getEtat().getJoueur()) == 0)) {
                // Ramasse les cartes
                if (!ramassageDone) {
                    ramassageDone = true;
                    salle.setRamasseJeu(2);
                }
                // Arrête le timer
                if ((autoRamasse != null) && (autoRamasse.isRunning())) {
                    autoRamasse.stop();
                }
                // Raffiche l'écran
                refresh(offsetJoueur);
                found = true;
            }
        }
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
     * @see java.awt.Container#removeAll()
     */
    public void removeAll() {
        removeMouseListener(this);
        if (boutonValide != null) {
            boutonValide.removeActionListener(this);
        }
        if (boutonPlis != null) {
            boutonPlis.removeActionListener(this);
        }
        if (bouton2Cartes != null) {
            bouton2Cartes.removeActionListener(this);
        }
        if (bouton3Cartes != null) {
            bouton3Cartes.removeActionListener(this);
        }
        if (chat != null) {
            chat.removeAll();
            chat = null;
        }
        if (scrollEvent != null) {
            scrollEvent.removeAll();
            scrollEvent = null;
        }

        super.removeAll();
    }

    /**
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     */
    public void mouseDragged(MouseEvent e) {
    }

    /**
     * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     */
    public void mouseMoved(MouseEvent e) {
        if (((salle.getMyPartie().getEtat().getEtat() == EtatPartieBelote.ETAT_JOUE_CARTE)
                || (salle.getMyPartie().getEtat().getEtat() == EtatPartieBelote.ETAT_FIN_TOUR))
            && (toLocalIndice(salle.getMyPartie().getEtat().getJoueur()) == 0)) {
            /**
             * Recherche le premiere en fonction de la visibilité : Ordre inverse nécessaire
             */
            boolean changed[] = new boolean[cartesJeu.length];
            boolean found = false;
            for (int i = 0; (i < cartesJeu.length); i++) {
                if (cartesJeu[i].isInverse()) {
                    cartesJeu[i].setInverse(false);
                    changed[i] = true;
                }
                else {
                    changed[i] = false;;
                }
            }
            for (int i = 0; !found && (i < cartesJeu.length); i++) {
                if (cartesJeu[i].isOnCarte(e.getPoint())) {
                    // Joue la carte
                    if (!found && salle.getMyPartie().peutJouer((Couleur) cartesJeu[i].getMyCarte())) {
                        cartesJeu[i].setInverse(true);
                        changed[i] = !changed[i];
                        found = true;
                    }
                }
            }
            for (int i = 0; (i < cartesJeu.length); i++) {
                if (changed[i]) {
                    cartesJeu[i].repaint();
                } 
            }
        }
    }

}


