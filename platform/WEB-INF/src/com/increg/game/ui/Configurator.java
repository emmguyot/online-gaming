/*
 * Created on 1 mai 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Stack;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * Classe pour decrypter le XML
 * @author Manu
 *
 */
public class Configurator extends DefaultHandler {

    /**
     * Nom du client
     */
    private String client;

    /**
     * Url du site du client
     */

    private String clientUrl;
    
    /**
     * Eléments rencontrés : Donne la hiérarchie
     */
    protected Stack elements;

    /**
     * Compteur des occurences de joueurs
     */
    protected int cptJoueur;
        
    /* ***************************************
     * Pour l'aire (Commence par un r)
     * *************************************** */
    /**
     * Fond de l'aire
     */
    protected String rFond;
    
    /**
     * Fonte à utiliser pour l'aire
     */
    protected Font rFont;
    
    /**
     * Couleur des boutons
     */
    protected Color rBoutonGlobalColor;

    /**
     * Border sur les boutons ?
     */
    protected boolean rBoutonGlocalBorder;

    /**
     * Fond de la liste des parties
     */
    protected String rFondListe;
    
    /**
     * Position et taille de la liste des parties
     */
    protected Rectangle rListePartie;
    
    /**
     * Position et taille du texte dans la liste des parties
     */
    protected Rectangle rTexteDsListePartie;
    
    /**
     * Couleur du texte dans la liste des parties
     */
    protected Color rTexteDsListePartieColor;
    
    /**
     * Libellé du texte dans la liste des parties
     */
    protected String rTexteFixeDsListePartie;
    
    /**
     * Position et taille du bouton du joueur Nord dans la liste des parties
     */
    protected Rectangle rJoueurNBouton;

    /**
     * Position et taille du bouton du joueur Sud dans la liste des parties
     */
    protected Rectangle rJoueurSBouton;

    /**
     * Position et taille du bouton du joueur Est dans la liste des parties
     */
    protected Rectangle rJoueurEBouton;

    /**
     * Position et taille du bouton du joueur Ouest dans la liste des parties
     */
    protected Rectangle rJoueurOBouton;

    /**
     * Image du bouton du joueur Nord dans la liste des parties
     */
    protected String rJoueurNBoutonImg;

    /**
     * Image du bouton du joueur Sud dans la liste des parties
     */
    protected String rJoueurSBoutonImg;

    /**
     * Image du bouton du joueur Est dans la liste des parties
     */
    protected String rJoueurEBoutonImg;

    /**
     * Image du bouton du joueur Ouest dans la liste des parties
     */
    protected String rJoueurOBoutonImg;

    /**
     * Position et taille de l'avatar du joueur Nord dans la liste des parties
     */
    protected Rectangle rJoueurNAvatar;

    /**
     * Position et taille de l'avatar du joueur Sud dans la liste des parties
     */
    protected Rectangle rJoueurSAvatar;

    /**
     * Position et taille de l'avatar du joueur Est dans la liste des parties
     */
    protected Rectangle rJoueurEAvatar;

    /**
     * Position et taille de l'avatar du joueur Ouest dans la liste des parties
     */
    protected Rectangle rJoueurOAvatar;

    /**
     * Position et taille du nom du joueur Nord dans la liste des parties
     */
    protected Rectangle rJoueurNNom;

    /**
     * Position et taille du nom du joueur Sud dans la liste des parties
     */
    protected Rectangle rJoueurSNom;

    /**
     * Position et taille du nom du joueur Est dans la liste des parties
     */
    protected Rectangle rJoueurENom;

    /**
     * Position et taille du nom du joueur Ouest dans la liste des parties
     */
    protected Rectangle rJoueurONom;

    /**
     * Image du cadena de la liste des parties
     */
    private String rImgCadenasListePartie;

    /**
     * Position et taille image du cadena de la liste des parties
     */
    private Rectangle rCadenasListePartie;

    /**
     * Image pour les parties d'un tournoi de la liste des parties
     */
    private String rImgTournoiListePartie;

    /**
     * Position et taille image pour les parties d'un tournoi de la liste des parties
     */
    private Rectangle rTournoiListePartie;

    /**
     * Bouton de création de partie
     */
    protected Rectangle rBoutonCreation;
    
    /**
     * Image de création de partie
     */
    protected String rBoutonCreationImg;

    /**
     * Bouton de fermeture de l'aire
     */
    protected Rectangle rBoutonFermeture;
    
    /**
     * Image de fermeture de l'aire
     */
    protected String rBoutonFermetureImg;

    /**
     * Bouton des options
     */
    protected Rectangle rBoutonOption;
    
    /**
     * Image des options
     */
    protected String rBoutonOptionImg;

    /**
     * Bouton pour les modérateurs
     */
    protected Rectangle rBoutonAdmin;
    
    /**
     * Image pour les modérateurs
     */
    protected String rBoutonAdminImg;

    /**
     * Position et taille de la liste des joueurs
     */
    protected Rectangle rListeJoueur;
    
    /**
     * Couleur de fond de la liste des joueurs
     */
    protected Color rListeJoueurBgColor;
    
    /**
     * Couleur du texte de la liste des joueurs
     */
    protected Color rListeJoueurColor;
    
    /**
     * Position et taille de la zone de saisie du chat
     */
    protected Rectangle rChatSaisie;
    
    /**
     * Couleur de fond de la zone de saisie du chat
     */
    protected Color rChatSaisieBgColor;
    
    /**
     * Position et taille de la zone d'affichage du chat
     */
    protected Rectangle rChatView;

    /**
     * Couleur de fond de la zone d'afficahge du chat
     */
    protected Color rChatViewBgColor;
    
    /**
     * Fonte à utiliser pour la liste des parties
     */
    protected Font rListeFont;
    
    /**
     * Position et taille du bouton d'accès au lien
     */
    protected Rectangle rLienSite1;
    
    /**
     * Position et taille du bouton d'accès au lien
     */
    protected Rectangle rLienSite2;
    
    /**
     * Couleur du texte d'accès au lien
     */
    protected Color rLienSite1Color;
    
    /**
     * Couleur du texte d'accès au lien
     */
    protected Color rLienSite2Color;
    
    /**
     * Position et taille du bouton d'envoi du chat
     */
    protected Rectangle rChatSend;
    
    /**
     * Image du send
     */
    protected String rChatSendImg;

    /**
     * Position et taille du bouton des smileys
     */
    protected Rectangle rChatSmiley;
    
    /**
     * Url contenant tous les smiley
     */
    protected String rChatSmileyUrl;

    /**
     * Image des couleurs
     */
    protected String rChatSmileyImg;

    /**
     * Position et taille du bouton des couleurs
     */
    protected Rectangle rChatCouleur;
    
    /**
     * Image des couleurs
     */
    protected String rChatCouleurImg;

    /**
     * Position et taille du bouton secret
     */
    protected Rectangle rChatSecret;
    
    /**
     * Image des secrets
     */
    protected String rChatSecretImg;

    /**
     * Fonte à utiliser pour les titres
     */
    protected Font rTitreFont;
    
    /**
     * Vecteur de Position et taille des titres
     */
    protected Vector rTitrePos;
    
    /**
     * Vecteur des titres
     */
    protected Vector rTitre;
    
    /* ***************************************
     * Pour la salle (commence par s)
     * *************************************** */
    /**
     * Fond de la salle
     */
    protected String sFond;
    
    /**
     * Taille de la salle
     */
    protected Dimension sTailleSalle;

    /**
     * Fonte à utiliser pour la salle
     */
    protected Font sFont;
    
    /**
     * Position et taille de l'avatar du joueur Nord
     */
    protected Rectangle sJoueurNAvatar;

    /**
     * Position et taille de l'avatar du joueur Sud
     */
    protected Rectangle sJoueurSAvatar;

    /**
     * Position et taille de l'avatar du joueur Est
     */
    protected Rectangle sJoueurEAvatar;

    /**
     * Position et taille de l'avatar du joueur Ouest
     */
    protected Rectangle sJoueurOAvatar;

    /**
     * Position et taille du nom du joueur Nord
     */
    protected Rectangle sJoueurNNom;

    /**
     * Position et taille du nom du joueur Sud
     */
    protected Rectangle sJoueurSNom;

    /**
     * Position et taille du nom du joueur Est
     */
    protected Rectangle sJoueurENom;

    /**
     * Position et taille du nom du joueur Ouest
     */
    protected Rectangle sJoueurONom;

    /**
     * Emplacement du texte du preneur
     */
    private Rectangle sPreneur;
     
    /**
     * Position et taille des scores Sud = Nous
     */
    protected Rectangle sScoreS;

    /**
     * Position et taille des scores Ouest = Eux
     */
    protected Rectangle sScoreO;
     
    /**
     * Position et taille des scores Est
     */
    protected Rectangle sScoreE;

    /**
     * Position et taille des scores Nord
     */
    protected Rectangle sScoreN;

    /**
     * Couleur des scores Sud = Nous
     */
    protected Color sScoreSColor;

    /**
     * Couleur des scores Ouest = Eux
     */
    protected Color sScoreOColor;
     
    /**
     * Couleur des scores Est
     */
    protected Color sScoreEColor;

    /**
     * Couleur des scores Nord
     */
    protected Color sScoreNColor;

    /**
     * Position et taille de la zone des événements
     */
    protected Rectangle sEvent;

    /**
     * Couleur de fond de l'évent
     */
    protected Color sEventBgColor;

    /**
     * Couleur du texte de l'évent
     */
    protected Color sEventColor;

    /**
     * Position et taille de la zone de saisie du chat
     */
    protected Rectangle sChatSaisie;
    
    /**
     * Couleur de fond de la zone de saisie du chat
     */
    protected Color sChatSaisieBgColor;
    
    /**
     * Position et taille de la zone d'affichage du chat
     */
    protected Rectangle sChatView;

    /**
     * Couleur de fond de la zone d'affichage du chat
     */
    protected Color sChatViewBgColor;
    
    /**
     * Image du send
     */
    protected String sChatSendImg;
    
    /**
     * Position et taille du bouton d'envoi du chat
     */
    protected Rectangle sChatSend;
    
    /**
     * Taille des cartes (verticale)
     */
    protected Dimension sCarte;

    /**
     * Position et taille premier dos de distribution
     */
    private Rectangle sDistribution2;

    /**
     * Position et taille second dos de distribution ou de l'atout
     */
    private Rectangle sDistribution1;

    /**
     * Image du dos des cartes
     */
    private String sDos;

    /**
     * Position et taille du curseur de coupe
     */
    private Rectangle sCurseur;

    /**
     * Taille et pos du bouton de validation (Coupe et distrib)
     */
    private Rectangle sValide;
    
    /**
     * Image du bouton de validation (Coupe et distrib)
     */
    private String sValideImg;
    
    /**
     * Taille et pos du bouton de choix de 2 cartes en premier
     */
    private Rectangle sBouton2Cartes;
    
    /**
     * Taille et pos du bouton de choix de 3 cartes en premier
     */
    private Rectangle sBouton3Cartes;

    /**
     * Image du bouton de choix de 2 cartes en premier
     */
    private String sBouton2CartesImg;
    
    /**
     * Image du bouton de choix de 3 cartes en premier
     */
    private String sBouton3CartesImg;

    /**
     * Point origine du repère polaire du jeu de carte
     */
    private Point sRepereJeu;

    /**
     * Angle en radian séparant les cartes du jeu 
     */
    private double sAlphaJeu;

    /**
     * Rayon du cercle où sont posées les cartes
     */
    private int sRayonJeu;
    
    /**
     * Ensemble des boutons de prise
     */
    private Vector sBoutonPrise;
    
    /**
     * Ensemble des cartes sur le tapis
     */
    private Vector sTapis;
    
    /**
     * Ensemble des plis sur le tapis
     */
    private Vector sPlis;

    /**
     * Ensemble des images des plis sur le tapis
     */
    private Vector sImagePlis;

    /**
     * Position et taille de la sphere
     */
    private Rectangle sSphere;

    /**
     * Image Aiguille de la sphere
     */
    private String sSphereAiguille;

    /**
     * Image de fond de la sphere
     */
    private String sSphereFond;

    /**
     * Fonte du texte de la sphere
     */
    private Font sSphereText;

    /**
     * Couleur du texte non selectionné de la sphere
     */
    private Color sSphereColor;
    /**
     * Couleur du texte selectionné du donneur de la sphere
     */
    private Color sSphereColorDSel;
    /**
     * Couleur du texte du maitre selectionné de la sphere
     */
    private Color sSphereColorMSel;

    /**
     * Position et taille du bouton Dernier Plis
     */
    private Rectangle sBoutonPlis;

    /**
     * Image du bouton Dernier Plis
     */
    private String sBoutonPlisImg;

    /**
     * Position et taille du bouton des smileys
     */
    protected Rectangle sChatSmiley;
    
    /**
     * Url contenant tous les smiley
     */
    protected String sChatSmileyUrl;

    /**
     * Image du bouton smileys
     */
    protected String sChatSmileyImg;

    /**
     * Position et taille du bouton des sons
     */
    protected Rectangle sChatSon;
    
    /**
     * Url contenant tous les sons
     */
    protected String sChatSonUrl;

    /**
     * Image du bouton son
     */
    protected String sChatSonImg;

    /**
     * Position et taille du bouton des couleurs
     */
    protected Rectangle sChatCouleur;
    
    /**
     * Image des couleurs
     */
    protected String sChatCouleurImg;

    /**
     * Position et taille du bouton secret
     */
    protected Rectangle sChatSecret;
    
    /**
     * Image des couleurs
     */
    protected String sChatSecretImg;

    /* ***************************************
     * Méthodes
     * *************************************** */

    /**
     * Constructeur
     */
    public Configurator() {
        
        // Initialise les paramètres
        rFond = "";
        rFondListe = "";
        rFont = null;
        elements = new Stack();
        cptJoueur = 0;
        sBoutonPrise = new Vector(5);
        sTapis = new Vector(4);
        sPlis = new Vector(2);
        sImagePlis = new Vector(2);
        rTitre = new Vector(3);
        rTitrePos = new Vector(3);
    }
    
    /**
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            super.startElement(uri, localName, qName, attributes);
            
            elements.push(qName);
            String qualifiedName = elements.toString();
            
            if (qualifiedName.equals("[skin]")) {
                client = attributes.getValue("client");
                clientUrl = attributes.getValue("url");
            }
            if (qualifiedName.equals("[skin, aire]")) {
                // Attribut fond : Alimentation des champs
                rFond = attributes.getValue("fond");
                cptJoueur = 0;
            }
            else if (qualifiedName.equals("[skin, aire, font]")) {
                rFont = new Font(attributes.getValue("face"), Integer.parseInt(attributes.getValue("style")), Integer.parseInt(attributes.getValue("size")));
            }
            else if (qualifiedName.equals("[skin, aire, titre, font]")) {
                rTitreFont = new Font(attributes.getValue("face"), Integer.parseInt(attributes.getValue("style")), Integer.parseInt(attributes.getValue("size")));
            }
            else if (qualifiedName.equals("[skin, aire, titre, text]")) {
                // Coo du titre
                rTitrePos.add(getPosAndSize(attributes));
                rTitre.add(attributes.getValue("msg"));
            }
            else if (qualifiedName.equals("[skin, aire, boutonGlobal]")) {
                rBoutonGlobalColor = Color.decode(attributes.getValue("bgcolor"));
                rBoutonGlocalBorder = Boolean.valueOf(attributes.getValue("border")).booleanValue();
            }
            else if (qualifiedName.equals("[skin, aire, listePartie]")) {
                // Balise fond de la liste
                rFondListe = attributes.getValue("fond");
                // Coo de la liste
                rListePartie = getPosAndSize(attributes);
            }
            else if (qualifiedName.equals("[skin, aire, listePartie, text]")) {
                // Coo du texte
                rTexteDsListePartie = getPosAndSize(attributes);
                rTexteDsListePartieColor = Color.decode(attributes.getValue("color"));
            }
            else if (qualifiedName.equals("[skin, aire, listePartie, text, font]")) {
                rListeFont = new Font(attributes.getValue("face"), Integer.parseInt(attributes.getValue("style")), Integer.parseInt(attributes.getValue("size")));
            }
            else if (qualifiedName.equals("[skin, aire, listePartie, joueur]")) {
                // Compte les joueurs
                cptJoueur++;
            }
            else if (qualifiedName.equals("[skin, aire, listePartie, joueur, bouton]")) {
                // Coo du bouton en fonction du joueur
                switch (cptJoueur) {
                    case 1 :
                        rJoueurSBouton = getPosAndSize(attributes);
                        rJoueurSBoutonImg = attributes.getValue("img");
                        break;
            
                    case 2 :
                        rJoueurOBouton = getPosAndSize(attributes);
                        rJoueurOBoutonImg = attributes.getValue("img");
                        break;
            
                    case 3 :
                        rJoueurNBouton = getPosAndSize(attributes);
                        rJoueurNBoutonImg = attributes.getValue("img");
                        break;
            
                    case 4 :
                        rJoueurEBouton = getPosAndSize(attributes);
                        rJoueurEBoutonImg = attributes.getValue("img");
                        break;
            
                    default :
                        // Pas prévu...
                        break;
                }
            }
            else if (qualifiedName.equals("[skin, aire, listePartie, joueur, avatar]")) {
                // Coo de l'avatar en fonction du joueur
                switch (cptJoueur) {
                    case 1 :
                        rJoueurSAvatar = getPosAndSize(attributes);
                        break;
            
                    case 2 :
                        rJoueurOAvatar = getPosAndSize(attributes);
                        break;
            
                    case 3 :
                        rJoueurNAvatar = getPosAndSize(attributes);
                        break;
            
                    case 4 :
                        rJoueurEAvatar = getPosAndSize(attributes);
                        break;
            
                    default :
                        // Pas prévu...
                        break;
                }
            }
            else if (qualifiedName.equals("[skin, aire, listePartie, joueur, nom]")) {
                // Coo du nom en fonction du joueur
                switch (cptJoueur) {
                    case 1 :
                        rJoueurSNom = getPosAndSize(attributes);
                        break;
            
                    case 2 :
                        rJoueurONom = getPosAndSize(attributes);
                        break;
            
                    case 3 :
                        rJoueurNNom = getPosAndSize(attributes);
                        break;
            
                    case 4 :
                        rJoueurENom = getPosAndSize(attributes);
                        break;
            
                    default :
                        // Pas prévu...
                        break;
                }
            }
            else if (qualifiedName.equals("[skin, aire, listePartie, cadenas]")) {
                // Coo de l'image
                rCadenasListePartie = getPosAndSize(attributes);
                // Img
                rImgCadenasListePartie = attributes.getValue("img");
                
            }
            else if (qualifiedName.equals("[skin, aire, listePartie, tournoi]")) {
                // Coo de l'image
                rTournoiListePartie = getPosAndSize(attributes);
                // Img
                rImgTournoiListePartie = attributes.getValue("img");
                
            }
            else if (qualifiedName.equals("[skin, aire, lien, copyright1]")) {
                // Coo du bouton
                rLienSite1 = getPosAndSize(attributes);
                rLienSite1Color = Color.decode(attributes.getValue("color"));
            }
            else if (qualifiedName.equals("[skin, aire, lien, copyright2]")) {
                // Coo du bouton
                rLienSite2 = getPosAndSize(attributes);
                rLienSite2Color = Color.decode(attributes.getValue("color"));
            }
            else if (qualifiedName.equals("[skin, aire, bouton, creation]")) {
                // Coo du bouton
                rBoutonCreation = getPosAndSize(attributes);
                rBoutonCreationImg = attributes.getValue("img");
            }
            else if (qualifiedName.equals("[skin, aire, bouton, fermeture]")) {
                // Coo du bouton
                rBoutonFermeture = getPosAndSize(attributes);
                rBoutonFermetureImg = attributes.getValue("img");
            }
            else if (qualifiedName.equals("[skin, aire, bouton, option]")) {
                // Coo du bouton
                rBoutonOption = getPosAndSize(attributes);
                rBoutonOptionImg = attributes.getValue("img");
            }
            else if (qualifiedName.equals("[skin, aire, bouton, admin]")) {
                // Coo du bouton
                rBoutonAdmin = getPosAndSize(attributes);
                rBoutonAdminImg = attributes.getValue("img");
            }
            else if (qualifiedName.equals("[skin, aire, listeJoueur]")) {
                // Coo de la liste
                rListeJoueur = getPosAndSize(attributes);
                // Couleur
                if ((attributes.getValue("bgcolor") == null) || (attributes.getValue("bgcolor").length() == 0)) {
                    rListeJoueurBgColor = new Color(0, 0, 0, 0);
                }
                else {
                    rListeJoueurBgColor = Color.decode(attributes.getValue("bgcolor"));
                } 
                rListeJoueurColor = Color.decode(attributes.getValue("color"));
            }
            else if (qualifiedName.equals("[skin, aire, chat, saisie]")) {
                // Coo de la liste
                rChatSaisie = getPosAndSize(attributes);
                // Couleur
                if ((attributes.getValue("bgcolor") == null) || (attributes.getValue("bgcolor").length() == 0)) {
                    rChatSaisieBgColor = new Color(0, 0, 0, 0);
                }
                else {
                    rChatSaisieBgColor = Color.decode(attributes.getValue("bgcolor"));
                } 
            }
            else if (qualifiedName.equals("[skin, aire, chat, view]")) {
                // Coo de la liste
                rChatView = getPosAndSize(attributes);
                // Couleur
                if ((attributes.getValue("bgcolor") == null) || (attributes.getValue("bgcolor").length() == 0)) {
                    rChatViewBgColor = new Color(0, 0, 0, 0);
                }
                else {
                    rChatViewBgColor = Color.decode(attributes.getValue("bgcolor"));
                } 
            }
            else if (qualifiedName.equals("[skin, aire, chat, valid]")) {
                // Coo de la liste
                rChatSend = getPosAndSize(attributes);
                rChatSendImg = attributes.getValue("img");
            }
            else if (qualifiedName.equals("[skin, aire, chat, smiley]")) {
                // Coo de la liste
                rChatSmiley = getPosAndSize(attributes);
                rChatSmileyUrl = attributes.getValue("url");
                rChatSmileyImg = attributes.getValue("img");
            }
            else if (qualifiedName.equals("[skin, aire, chat, couleur]")) {
                // Coo de la liste
                rChatCouleur = getPosAndSize(attributes);
                rChatCouleurImg = attributes.getValue("img");
            }
            else if (qualifiedName.equals("[skin, aire, chat, secret]")) {
                // Coo de la liste
                rChatSecret = getPosAndSize(attributes);
                rChatSecretImg = attributes.getValue("img");
            }
            // Fin Aire / Début Salle  *************************************************
            else if (qualifiedName.equals("[skin, salle]")) {
                // Attribut fond : Alimentation des champs
                sFond = attributes.getValue("fond");
                sTailleSalle = getSize(attributes);
                cptJoueur = 0;
            }
            else if (qualifiedName.equals("[skin, salle, font]")) {
                sFont = new Font(attributes.getValue("face"), Integer.parseInt(attributes.getValue("style")), Integer.parseInt(attributes.getValue("size")));
            }
            else if (qualifiedName.equals("[skin, salle, joueur]")) {
                // Compte les joueurs
                cptJoueur++;
            }
            else if (qualifiedName.equals("[skin, salle, joueur, avatar]")) {
                // Coo de l'avatar en fonction du joueur
                switch (cptJoueur) {
                    case 1 :
                        sJoueurSAvatar = getPosAndSize(attributes);
                        break;
            
                    case 2 :
                        sJoueurOAvatar = getPosAndSize(attributes);
                        break;
            
                    case 3 :
                        sJoueurNAvatar = getPosAndSize(attributes);
                        break;
            
                    case 4 :
                        sJoueurEAvatar = getPosAndSize(attributes);
                        break;
            
                    default :
                        // Pas prévu...
                        break;
                }
            }
            else if (qualifiedName.equals("[skin, salle, joueur, nom]")) {
                // Coo du nom en fonction du joueur
                switch (cptJoueur) {
                    case 1 :
                        sJoueurSNom = getPosAndSize(attributes);
                        break;
            
                    case 2 :
                        sJoueurONom = getPosAndSize(attributes);
                        break;
            
                    case 3 :
                        sJoueurNNom = getPosAndSize(attributes);
                        break;
            
                    case 4 :
                        sJoueurENom = getPosAndSize(attributes);
                        break;
            
                    default :
                        // Pas prévu...
                        break;
                }
            }
            else if (qualifiedName.equals("[skin, salle, joueur, score]")) {
                // Coo du nom en fonction du joueur
                switch (cptJoueur) {
                    case 1 :
                        sScoreS = getPosAndSize(attributes);
                        sScoreSColor = Color.decode(attributes.getValue("color"));
                        break;
            
                    case 2 :
                        sScoreO = getPosAndSize(attributes);
                        sScoreOColor = Color.decode(attributes.getValue("color"));
                        break;
            
                    case 3 :
                        sScoreN = getPosAndSize(attributes);
                        sScoreNColor = Color.decode(attributes.getValue("color"));
                        break;
            
                    case 4 :
                        sScoreE = getPosAndSize(attributes);
                        sScoreEColor = Color.decode(attributes.getValue("color"));
                        break;
            
                    default :
                        // Pas prévu...
                        break;
                }
            }
            else if (qualifiedName.equals("[skin, salle, joueur, plis]")) {
                // Position et taille des plis sur le tapis
                sPlis.add(getPosAndSize(attributes));
                sImagePlis.add(attributes.getValue("img"));
            }
            else if (qualifiedName.equals("[skin, salle, joueur, tapis]")) {
                // Position et taille des cartes du tapis
                sTapis.add(getPosAndSize(attributes));
            }
            else if (qualifiedName.equals("[skin, salle, preneur]")) {
                sPreneur = getPosAndSize(attributes);
            }
            else if (qualifiedName.equals("[skin, salle, event]")) {
                sEvent = getPosAndSize(attributes);
                if ((attributes.getValue("bgcolor") == null) || (attributes.getValue("bgcolor").length() == 0)) {
                    sEventBgColor = new Color(0, 0, 0, 0);
                }
                else {
                    sEventBgColor = Color.decode(attributes.getValue("bgcolor"));
                } 
                sEventColor = Color.decode(attributes.getValue("color"));
            }
            else if (qualifiedName.equals("[skin, salle, chat, saisie]")) {
                // Coo de la liste
                sChatSaisie = getPosAndSize(attributes);
                // Couleur
                if ((attributes.getValue("bgcolor") == null) || (attributes.getValue("bgcolor").length() == 0)) {
                    sChatSaisieBgColor = new Color(0, 0, 0, 0);
                }
                else {
                    sChatSaisieBgColor = Color.decode(attributes.getValue("bgcolor"));
                } 
            }
            else if (qualifiedName.equals("[skin, salle, chat, view]")) {
                // Coo de la liste
                sChatView = getPosAndSize(attributes);
                // Couleur
                if ((attributes.getValue("bgcolor") == null) || (attributes.getValue("bgcolor").length() == 0)) {
                    sChatViewBgColor = new Color(0, 0, 0, 0);
                }
                else {
                    sChatViewBgColor = Color.decode(attributes.getValue("bgcolor"));
                } 
            }
            else if (qualifiedName.equals("[skin, salle, chat, valid]")) {
                // Coo de la liste
                sChatSend = getPosAndSize(attributes);
                sChatSendImg = attributes.getValue("img");
            }
            else if (qualifiedName.equals("[skin, salle, carte]")) {
                // Taille des cartes
                sCarte = getSize(attributes);
            }
            else if (qualifiedName.equals("[skin, salle, carte, dos]")) {
                // Dos des cartes
                sDos = attributes.getValue("img");
            }
            else if (qualifiedName.equals("[skin, salle, distribution1]")) {
                // Taille des cartes
                sDistribution1 = getPosAndSize(attributes);
            }
            else if (qualifiedName.equals("[skin, salle, distribution2]")) {
                // Taille des cartes
                sDistribution2 = getPosAndSize(attributes);
            }
            else if (qualifiedName.equals("[skin, salle, curseur]")) {
                // Taille et pos du curseur de coupe
                sCurseur = getPosAndSize(attributes);
            }
            else if (qualifiedName.equals("[skin, salle, validation]")) {
                // Taille et pos du bouton de validation (Coupe et distrib)
                sValide = getPosAndSize(attributes);
                sValideImg = attributes.getValue("img");
            }
            else if (qualifiedName.equals("[skin, salle, bouton2Cartes]")) {
                // Taille et pos du bouton de choix de 2 cartes en premier
                sBouton2Cartes = getPosAndSize(attributes);
                sBouton2CartesImg = attributes.getValue("img");
            }
            else if (qualifiedName.equals("[skin, salle, bouton3Cartes]")) {
                // Taille et pos du bouton de choix de 3 cartes en premier
                sBouton3Cartes = getPosAndSize(attributes);
                sBouton3CartesImg = attributes.getValue("img");
            }
            else if (qualifiedName.equals("[skin, salle, jeu]")) {
                // Position de l'origine du repère polaire
                // Coordonnée polaire dans le repère de la carte (Point en bas à gauche)
                sRepereJeu = getPos(attributes);
                sAlphaJeu = Double.parseDouble(attributes.getValue("a"));
                sRayonJeu = Integer.parseInt(attributes.getValue("r"));
            }
            else if (qualifiedName.equals("[skin, salle, prise]")) {
                // Position et taille du bouton de prise
                sBoutonPrise.clear();
            }
            else if (qualifiedName.equals("[skin, salle, prise, bouton]")) {
                // Position et taille du bouton de prise
                sBoutonPrise.add(getPosAndSize(attributes));
            }
            else if (qualifiedName.equals("[skin, salle, sphere]")) {
                // Position et taille de la sphere
                sSphere = getPosAndSize(attributes);
            }
            else if (qualifiedName.equals("[skin, salle, sphere, aiguille]")) {
                // Image aiguille
                sSphereAiguille = attributes.getValue("img");
            }
            else if (qualifiedName.equals("[skin, salle, sphere, fond]")) {
                // Image fond de sphere
                sSphereFond = attributes.getValue("img");
            }
            else if (qualifiedName.equals("[skin, salle, sphere, font]")) {
                // Image fond de sphere
                sSphereText = new Font(attributes.getValue("face"), 
                                        Integer.parseInt(attributes.getValue("style")), 
                                        Integer.parseInt(attributes.getValue("size")));
                sSphereColor = Color.decode(attributes.getValue("normal"));
                sSphereColorDSel = Color.decode(attributes.getValue("donneur"));
                sSphereColorMSel = Color.decode(attributes.getValue("maitre"));
            }
            else if (qualifiedName.equals("[skin, salle, prise, bouton]")) {
                // Position et taille du bouton de prise
                sBoutonPrise.add(getPosAndSize(attributes));
            }
            else if (qualifiedName.equals("[skin, salle, boutonPlis]")) {
                // Taille et pos du bouton d'affichage des derniers plis
                sBoutonPlis = getPosAndSize(attributes);
                sBoutonPlisImg = attributes.getValue("img");
            }
            else if (qualifiedName.equals("[skin, salle, chat, smiley]")) {
                // Coo de la liste
                sChatSmiley = getPosAndSize(attributes);
                sChatSmileyUrl = attributes.getValue("url");
                sChatSmileyImg = attributes.getValue("img");
            }
            else if (qualifiedName.equals("[skin, salle, chat, couleur]")) {
                // Coo de la liste
                sChatCouleur = getPosAndSize(attributes);
                sChatCouleurImg = attributes.getValue("img");
            }
            else if (qualifiedName.equals("[skin, salle, chat, secret]")) {
                // Coo de la liste
                sChatSecret = getPosAndSize(attributes);
                sChatSecretImg = attributes.getValue("img");
            }
            else if (qualifiedName.equals("[skin, salle, chat, son]")) {
                // Coo de la liste
                sChatSon = getPosAndSize(attributes);
                sChatSonUrl = attributes.getValue("url");
                sChatSonImg = attributes.getValue("img");
            }

        }
        catch (NumberFormatException e) {
            System.err.println("Erreur de conversion :");
            e.printStackTrace();
            throw(e);
        }
        catch (NullPointerException e) {
            System.err.println("Erreur de paramétrage :");
            e.printStackTrace();
            throw(e);
        }


    }

    /**
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        
        if (!((String) elements.pop()).equals(qName)) {
            // Pas bon
            throw new SAXException("Hiérarchie non respectée pour " + elements.toString());
        }
    }

    /**
     * @see org.xml.sax.ContentHandler#endDocument()
     */
    public void endDocument() throws SAXException {
        super.endDocument();
        // RAZ des variables inutiles
        elements = null;
    }
    
    /**
     * Calcule le rectangle à partir des attributs x, y, w, h
     * @param attributes Attributs correspondants
     * @return Rectangle créé
     */
    protected Rectangle getPosAndSize(Attributes attributes) {
        return new Rectangle(Integer.parseInt(attributes.getValue("x")), 
                            Integer.parseInt(attributes.getValue("y")), 
                            Integer.parseInt(attributes.getValue("w")), 
                            Integer.parseInt(attributes.getValue("h")));
    }

    /**
     * Calcule le rectangle à partir des attributs w, h
     * @param attributes Attributs correspondants
     * @return Rectangle créé
     */
    protected Dimension getSize(Attributes attributes) {
        return new Dimension(Integer.parseInt(attributes.getValue("w")), 
                            Integer.parseInt(attributes.getValue("h")));
    }

    /**
     * Calcule le rectangle à partir des attributs x, y
     * @param attributes Attributs correspondants
     * @return Rectangle créé
     */
    protected Point getPos(Attributes attributes) {
        return new Point(Integer.parseInt(attributes.getValue("x")), 
                            Integer.parseInt(attributes.getValue("y")));
    }

    /* ***************************************
     * Getters
     * *************************************** */
     
    /**
     * @return fond de l'aire
     */
    public String getRFond() {
        return rFond;
    }

    /**
     * @return Fond de la liste des parties
     */
    public String getRFondListe() {
        return rFondListe;
    }

    /**
     * @return Font de l'aire
     */
    public Font getRFont() {
        return rFont;
    }

    /**
     * @return Font de la liste de parties
     */
    public Font getRListeFont() {
        return rListeFont;
    }

    /**
     * @return Position et taille de l'avatar du joueur Est dans la liste des parties
     */
    public Rectangle getRJoueurEAvatar() {
        return rJoueurEAvatar;
    }

    /**
     * @return Position et taille du bouton du joueur Est dans la liste des parties
     */
    public Rectangle getRJoueurEBouton() {
        return rJoueurEBouton;
    }

    /**
     * @return Position et taille du nom du joueur Est dans la liste des parties
     */
    public Rectangle getRJoueurENom() {
        return rJoueurENom;
    }

    /**
     * @return Position et taille de l'avatar du joueur Nord dans la liste des parties
     */
    public Rectangle getRJoueurNAvatar() {
        return rJoueurNAvatar;
    }

    /**
     * @return Position et taille du bouton du joueur Nord dans la liste des parties
     */
    public Rectangle getRJoueurNBouton() {
        return rJoueurNBouton;
    }

    /**
     * @return Position et taille du nom du joueur Nord dans la liste des parties
     */
    public Rectangle getRJoueurNNom() {
        return rJoueurNNom;
    }

    /**
     * @return Position et taille de l'avatar du joueur Ouest dans la liste des parties
     */
    public Rectangle getRJoueurOAvatar() {
        return rJoueurOAvatar;
    }

    /**
     * @return Position et taille du bouton du joueur Ouest dans la liste des parties
     */
    public Rectangle getRJoueurOBouton() {
        return rJoueurOBouton;
    }

    /**
     * @return Position et taille du nom du joueur Ouest dans la liste des parties
     */
    public Rectangle getRJoueurONom() {
        return rJoueurONom;
    }

    /**
     * @return Position et taille de l'avatar du joueur Sud dans la liste des parties
     */
    public Rectangle getRJoueurSAvatar() {
        return rJoueurSAvatar;
    }

    /**
     * @return Position et taille du bouton du joueur Sud dans la liste des parties
     */
    public Rectangle getRJoueurSBouton() {
        return rJoueurSBouton;
    }

    /**
     * @return Position et taille du nom du joueur Sud dans la liste des parties
     */
    public Rectangle getRJoueurSNom() {
        return rJoueurSNom;
    }

    /**
     * @return Position et taille de la liste des parties
     */
    public Rectangle getRListePartie() {
        return rListePartie;
    }

    /**
     * @return Position et taille du texte dans la liste des parties
     */
    public Rectangle getRTexteDsListePartie() {
        return rTexteDsListePartie;
    }

    /**
     * @return Libellé du texte dans la liste des parties
     */
    public String getRTexteFixeDsListePartie() {
        return rTexteFixeDsListePartie;
    }

    /**
     * @return Position et taille du bouton pour créer une partie
     */
    public Rectangle getRBoutonCreation() {
        return rBoutonCreation;
    }

    /**
     * @return Position et taille du bouton pour quitter l'aire
     */
    public Rectangle getRBoutonFermeture() {
        return rBoutonFermeture;
    }

    /**
     * @return Position et taille du bouton pour les options
     */
    public Rectangle getRBoutonOption() {
        return rBoutonOption;
    }

    /**
     * @return Position et taille du bouton pour les actions modérateurs
     */
    public Rectangle getRBoutonAdmin() {
        return rBoutonAdmin;
    }

    /**
     * @return Position et taille de la liste des joueurs
     */
    public Rectangle getRListeJoueur() {
        return rListeJoueur;
    }

    /**
     * @return la couleur de fond de la liste des joueurs
     */
    public Color getRListeJoueurBgColor() {
        return rListeJoueurBgColor;
    }

    /**
     * @return Position et taille de la zone de saisie du chat
     */
    public Rectangle getRChatSaisie() {
        return rChatSaisie;
    }

    /**
     * @return Position et taille de la zone d'affichage du chat
     */
    public Rectangle getRChatView() {
        return rChatView;
    }

    /**
     * @return Position et taille du bouton d'accès au lien
     */
    public Rectangle getRLienSite1() {
        return rLienSite1;
    }
    
    /**
     * @return Position et taille du bouton d'accès au lien
     */
    public Rectangle getRLienSite2() {
        return rLienSite2;
    }
    
    /**
     * @return Position et taille du bouton d'envoi du chat
     */
    public Rectangle getRChatSend() {
        return rChatSend;
    }
    
    /**
     * @return Position et taille du bouton d'envoi du chat
     */
    public String getRChatSendImg() {
        return rChatSendImg;
    }
    
    /**
     * @return Fond de la salle
     */
    public String getSFond() {
        return sFond;
    }

    /**
     * @return Fonte à utiliser pour la salle
     */
    public Font getSFont() {
        return sFont;
    }

    /**
     * @return Position et taille de l'avatar du joueur Est
     */
    public Rectangle getSJoueurEAvatar() {
        return sJoueurEAvatar;
    }

    /**
     * @return Position et taille du nom du joueur Est
     */
    public Rectangle getSJoueurENom() {
        return sJoueurENom;
    }

    /**
     * @return Position et taille de l'avatar du joueur Nord
     */
    public Rectangle getSJoueurNAvatar() {
        return sJoueurNAvatar;
    }

    /**
     * @return Position et taille du nom du joueur Nord
     */
    public Rectangle getSJoueurNNom() {
        return sJoueurNNom;
    }

    /**
     * @return Position et taille de l'avatar du joueur Ouest
     */
    public Rectangle getSJoueurOAvatar() {
        return sJoueurOAvatar;
    }

    /**
     * @return Position et taille du nom du joueur Ouest
     */
    public Rectangle getSJoueurONom() {
        return sJoueurONom;
    }

    /**
     * @return Position et taille de l'avatar du joueur Sud
     */
    public Rectangle getSJoueurSAvatar() {
        return sJoueurSAvatar;
    }

    /**
     * @return Position et taille du nom du joueur Sud
     */
    public Rectangle getSJoueurSNom() {
        return sJoueurSNom;
    }

    /**
     * @return Nom du client en cours
     */
    public String getClient() {
        return client;
    }

    /**
     * @return Url du client en cours
     */
    public String getClientUrl() {
        return clientUrl;
    }

    /**
     * @return Position et taille image du cadena de la liste des parties
     */
    public Rectangle getRCadenasListePartie() {
        return rCadenasListePartie;
    }

    /**
     * @return Image du cadena de la liste des parties
     */
    public String getRImgCadenasListePartie() {
        return rImgCadenasListePartie;
    }

    /**
     * @return Emplacement du texte du preneur
     */
    public Rectangle getSPreneur() {
        return sPreneur;
    }

    /**
     * @return Position et taille des scores Sud = Nous
     */
    public Rectangle getSScoreO() {
        return sScoreO;
    }

    /**
     * @return Position et taille des scores Ouest = Eux
     */
    public Rectangle getSScoreS() {
        return sScoreS;
    }

    /**
     * @return Position et taille des scores Nord
     */
    public Rectangle getSScoreN() {
        return sScoreN;
    }

    /**
     * @return Position et taille des scores Est
     */
    public Rectangle getSScoreE() {
        return sScoreE;
    }

    /**
     * @return Position et taille de la zone des événements
     */
    public Rectangle getSEvent() {
        return sEvent;
    }

    /**
     * @return Couleur de fond de l'évent
     */
    public Color getSEventBgColor() {
        return sEventBgColor;
    }

    /**
     * @return Position et taille de la zone de saisie du chat
     */
    public Rectangle getSChatSaisie() {
        return sChatSaisie;
    }

    /**
     * @return Position et taille de la zone d'affichage du chat
     */
    public Rectangle getSChatView() {
        return sChatView;
    }

    /**
     * @return Position et taille du bouton d'envoi du chat
     */
    public Rectangle getSChatSend() {
        return sChatSend;
    }
    
    /**
     * @return Position et taille du bouton d'envoi du chat
     */
    public String getSChatSendImg() {
        return sChatSendImg;
    }
    
    /**
     * @return Couleur des boutons
     */
    public Color getRBoutonGlobalColor() {
        return rBoutonGlobalColor;
    }

    /**
     * @return Taille des cartes (verticale)
     */
    public Dimension getSCarte() {
        return sCarte;
    }

    /**
     * @return Position et taille premier dos de distribution et carte d'atout
     */
    public Rectangle getSDistribution1() {
        return sDistribution1;
    }

    /**
     * @return Position et taille deuxième dos de distribution
     */
    public Rectangle getSDistribution2() {
        return sDistribution2;
    }

    /**
     * @return Image du dos
     */
    public String getSDos() {
        return sDos;
    }

    /**
     * @return Position et taille du curseur de coupe
     */
    public Rectangle getSCurseur() {
        return sCurseur;
    }

    /**
     * @return Taille et pos du bouton de validation (Coupe et distrib)
     */
    public Rectangle getSValide() {
        return sValide;
    }

    /**
     * @return Taille et pos du bouton de choix de 2 cartes en premier
     */
    public Rectangle getSBouton2Cartes() {
        return sBouton2Cartes;
    }

    /**
     * @return Taille et pos du bouton de choix de 3 cartes en premier
     */
    public Rectangle getSBouton3Cartes() {
        return sBouton3Cartes;
    }

    /**
     * @return Angle en radian séparant les cartes du jeu
     */
    public double getSAlphaJeu() {
        return sAlphaJeu;
    }

    /**
     * @return Rayon du cercle où sont posées les cartes
     */
    public int getSRayonJeu() {
        return sRayonJeu;
    }

    /**
     * @return Point origine du repère polaire du jeu de carte
     */
    public Point getSRepereJeu() {
        return sRepereJeu;
    }

    /**
     * @return Ensemble des boutons de prise
     */
    public Vector getSBoutonPrise() {
        return sBoutonPrise;
    }
    /**
     * @param i Indice du bouton voulu
     * @return Ensemble des boutons de prise
     */
    public Rectangle getSBoutonPrise(int i) {
        return (Rectangle) sBoutonPrise.get(i);
    }

    /**
     * @return Ensemble des cartes sur le tapis
     */
    public Vector getSTapis() {
        return sTapis;
    }
    /**
     * @param i indice de la carte
     * @return Ensemble des cartes sur le tapis
     */
    public Rectangle getSTapis(int i) {
        return (Rectangle) sTapis.get(i);
    }

    /**
     * @return Ensemble des plis sur le tapis
     */
    public Vector getSPlis() {
        return sPlis;
    }

    /**
     * @param i indice du joueur
     * @return Ensemble des plis sur le tapis
     */
    public Rectangle getSPlis(int i) {
        return (Rectangle) sPlis.get(i);
    }

    /**
     * @return Ensemble des Images des plis sur le tapis
     */
    public Vector getSImagePlis() {
        return sImagePlis;
    }

    /**
     * @param i indice du joueur
     * @return Ensemble des Images des plis sur le tapis
     */
    public String getSImagePlis(int i) {
        return (String) sImagePlis.get(i);
    }

    /**
     * @return Position et taille de la sphere
     */
    public Rectangle getSSphere() {
        return sSphere;
    }

    /**
     * @return Image Aiguille de la sphere
     */
    public String getSSphereAiguille() {
        return sSphereAiguille;
    }

    /**
     * @return Image de fond de la sphere
     */
    public String getSSphereFond() {
        return sSphereFond;
    }

    /**
     * @return Fonte du texte non selectionné de la sphere
     */
    public Font getSSphereText() {
        return sSphereText;
    }

    /**
     * @return Couleur du texte non selectionné de la sphere
     */
    public Color getSSphereColor() {
        return sSphereColor;
    }
    /**
     * @return Couleur du texte selectionné du donneur de la sphere
     */
    public Color getSSphereColorDSel() {
        return sSphereColorDSel;
    }
    /**
     * @return Couleur du texte du maitre selectionné de la sphere
     */
    public Color getSSphereColorMSel() {
        return sSphereColorMSel;
    }

    /**
     * @return Position et taille du bouton Dernier Plis
     */
    public Rectangle getSBoutonPlis() {
        return sBoutonPlis;
    }

    /**
     * @return Position et taille du bouton des couleurs
     */
    public Rectangle getRChatCouleur() {
        return rChatCouleur;
    }

    /**
     * @return Position et taille du bouton secret
     */
    public Rectangle getRChatSecret() {
        return rChatSecret;
    }

    /**
     * @return Image du bouton secret
     */
    public String getRChatSecretImg() {
        return rChatSecretImg;
    }

    /**
     * @return Position et taille du bouton des smileys
     */
    public Rectangle getRChatSmiley() {
        return rChatSmiley;
    }

    /**
     * @return Image du bouton smiley
     */
    public String getRChatSmileyUrl() {
        return rChatSmileyUrl;
    }

    /**
     * @return Position et taille du bouton des couleurs
     */
    public Rectangle getSChatCouleur() {
        return sChatCouleur;
    }

    /**
     * @return Position et taille du bouton secret
     */
    public Rectangle getSChatSecret() {
        return sChatSecret;
    }

    /**
     * @return Image du bouton secret
     */
    public String getSChatSecretImg() {
        return sChatSecretImg;
    }

    /**
     * @return Position et taille du bouton des smileys
     */
    public Rectangle getSChatSmiley() {
        return sChatSmiley;
    }

    /**
     * @return Url des smileys
     */
    public String getSChatSmileyUrl() {
        return sChatSmileyUrl;
    }

    /**
     * @return Image du bouton smiley
     */
    public String getRChatCouleurImg() {
        return rChatCouleurImg;
    }

    /**
     * @return Image du bouton smiley
     */
    public String getRChatSmileyImg() {
        return rChatSmileyImg;
    }

    /**
     * @return Image du bouton couleurs
     */
    public String getSChatCouleurImg() {
        return sChatCouleurImg;
    }

    /**
     * @return Image du bouton smiley
     */
    public String getSChatSmileyImg() {
        return sChatSmileyImg;
    }

    /**
     * @return Vecteur des titres
     */
    public Vector getRTitre() {
        return rTitre;
    }

    /**
     * @return Fonte à utiliser pour les titres
     */
    public Font getRTitreFont() {
        return rTitreFont;
    }

    /**
     * @return Vecteur de Position et taille des titres
     */
    public Vector getRTitrePos() {
        return rTitrePos;
    }

    /**
     * @param i Indice du titre voulu
     * @return Coo et Pos du titre
     */
    public Rectangle getRTitrePos(int i) {
        return (Rectangle) rTitrePos.get(i);
    }

    /**
     * @param i Indice du titre voulu
     * @return Texte du titre
     */
    public String getRTitre(int i) {
        return (String) rTitre.get(i);
    }


    /**
     * @return Rectangle de position et taille du son
     */
    public Rectangle getSChatSon() {
        return sChatSon;
    }

    /**
     * @return Image d'icone pour le son
     */
    public String getSChatSonImg() {
        return sChatSonImg;
    }

    /**
     * @return Url pour ouvrir les sons
     */
    public String getSChatSonUrl() {
        return sChatSonUrl;
    }

    /**
     * @return Image pour les parties d'un tournoi de la liste des parties
     */
    public String getRImgTournoiListePartie() {
        return rImgTournoiListePartie;
    }

    /**
     * @return Position et taille image pour les parties d'un tournoi de la liste des parties
     */
    public Rectangle getRTournoiListePartie() {
        return rTournoiListePartie;
    }

    /**
     * @return Images pour le bouton modérateur
     */
    public String getRBoutonAdminImg() {
        return rBoutonAdminImg;
    }

    /**
     * @return Images pour la création de parties
     */
    public String getRBoutonCreationImg() {
        return rBoutonCreationImg;
    }

    /**
     * @return Images pour le bouton de fermeture
     */
    public String getRBoutonFermetureImg() {
        return rBoutonFermetureImg;
    }

    /**
     * @return Images pour le bouton options
     */
    public String getRBoutonOptionImg() {
        return rBoutonOptionImg;
    }

    /**
     * @return Couleur de fond de la zone de saisie du chat
     */
    public Color getRChatSaisieBgColor() {
        return rChatSaisieBgColor;
    }

    /**
     * @return Couleur de fond de la zone d'affichage du chat
     */
    public Color getRChatViewBgColor() {
        return rChatViewBgColor;
    }

    /**
     * @return Couleur de fond de la zone de saisie du chat
     */
    public Color getSChatSaisieBgColor() {
        return sChatSaisieBgColor;
    }

    /**
     * @return Couleur de fond de la zone d'affichage du chat
     */
    public Color getSChatViewBgColor() {
        return sChatViewBgColor;
    }

    /**
     * @return Couleur du texte de la liste des joueurs
     */
    public Color getRListeJoueurColor() {
        return rListeJoueurColor;
    }

    /**
     * @return Couleur du texte dans la liste des parties
     */
    public Color getRTexteDsListePartieColor() {
        return rTexteDsListePartieColor;
    }

    /**
     * @return Taille de la salle
     */
    public Dimension getSTailleSalle() {
        return sTailleSalle;
    }

    /**
     * @return Image du bouton du joueur Est dans la liste des parties
     */
    public String getRJoueurEBoutonImg() {
        return rJoueurEBoutonImg;
    }

    /**
     * @return Image du bouton du joueur Nord dans la liste des parties
     */
    public String getRJoueurNBoutonImg() {
        return rJoueurNBoutonImg;
    }

    /**
     * @return Image du bouton du joueur Ouest dans la liste des parties
     */
    public String getRJoueurOBoutonImg() {
        return rJoueurOBoutonImg;
    }

    /**
     * @return Image du bouton du joueur Sud dans la liste des parties
     */
    public String getRJoueurSBoutonImg() {
        return rJoueurSBoutonImg;
    }

    /**
     * @return Couleur du texte d'accès au lien
     */
    public Color getRLienSite1Color() {
        return rLienSite1Color;
    }

    /**
     * @return Couleur du texte d'accès au lien
     */
    public Color getRLienSite2Color() {
        return rLienSite2Color;
    }

    /**
     * @return Border sur les boutons ?
     */
    public boolean isRBoutonGlocalBorder() {
        return rBoutonGlocalBorder;
    }

    /**
     * @return Couleur de fond de l'évent
     */
    public Color getSEventColor() {
        return sEventColor;
    }

    /**
     * @return Couleur des scores Est
     */
    public Color getSScoreEColor() {
        return sScoreEColor;
    }

    /**
     * @return Couleur des scores Nord
     */
    public Color getSScoreNColor() {
        return sScoreNColor;
    }

    /**
     * @return Couleur des scores Ouest = Eux
     */
    public Color getSScoreOColor() {
        return sScoreOColor;
    }

    /**
     * @return Couleur des scores Sud = Nous
     */
    public Color getSScoreSColor() {
        return sScoreSColor;
    }

    /**
     * @return Image du bouton de choix de 2 cartes en premier
     */
    public String getSBouton2CartesImg() {
        return sBouton2CartesImg;
    }

    /**
     * @return Image du bouton de choix de 3 cartes en premier
     */
    public String getSBouton3CartesImg() {
        return sBouton3CartesImg;
    }

    /**
     * @return Image du bouton Dernier Plis
     */
    public String getSBoutonPlisImg() {
        return sBoutonPlisImg;
    }

    /**
     * @return Image du bouton de validation (Coupe et distrib)
     */
    public String getSValideImg() {
        return sValideImg;
    }

}