/*
 * Created on 26 avr. 2003
 *
 * Classe modèle de l'aire de jeu
 */
package com.increg.game.client;

import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPInputStream;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.increg.game.client.belote.AnnonceBelote;
import com.increg.game.client.belote.CouleurBelote;
import com.increg.game.client.belote.EtatPartieBelote;
import com.increg.game.client.belote.JeuBelote;
import com.increg.game.client.belote.PartieBelote;
import com.increg.game.client.belote.PartieBeloteClassique;
import com.increg.game.client.belote.PartieBeloteModerne;
import com.increg.game.net.ServerCall;
import com.increg.game.net.ServerCallRequester;
import com.increg.game.ui.AfficheAire;
import com.increg.game.ui.AireMain;
import com.increg.game.ui.Configurator;

/**
 * @author Manu
 *
 * Classe modèle de l'aire de jeu
 */
public class AireMainModel implements ServerCallRequester {

    /* ***************************************
     * Constantes
     * *************************************** */
    /**
     * Initialisation de la classe
     */
    public static final int ETAT_INIT = 0;
    /**
     * Initialisation de la classe
     */
    public static final int ETAT_INIT_P2 = ETAT_INIT + 1;
    /**
     * Fonctionnement normal de la classe
     */
    public static final int ETAT_INIT_P3 = ETAT_INIT_P2 + 1;
    /**
     * Fonctionnement normal de la classe
     */
    public static final int ETAT_OK = ETAT_INIT_P3 + 1;
    /**
     * Fin de l'aire de jeu
     */
    public static final int QUIT = ETAT_OK + 1;

    /* ***************************************
     * Attributs
     * *************************************** */
    /**
     * Applet appelante
     */
    private AireMain parent;
    
    /**
     * Joueur connecté
     */
    private Joueur myJoueur;
    
    /**
     * Classe assurant la liaison avec le serveur
     */
    private ServerCall liaisonSrv;

    /**
     * Dernier identifiant reçu du serveur
     */
    private int lastReceivedId;
        
    /**
     * Liste des joueurs connectés
     */
    private Vector lstJoueur;

    /**
     * Liste des parties
     */
    private Vector lstPartie;

    /**
     * Liste des salles
     */
    private Vector lstSalle;

    /**
     * Liste des salles a supprimer dès que possible
     */
    private Vector lstSalleASuppr;

    /**
     * Liste des salles a créer dès que possible
     */
    private Vector lstSalleACreer;

    /**
     * Etat en cours du model
     */
    private int etat;
    
    /**
     * Configuration graphique globale
     */
    private Configurator skinConfig;
    
    /**
     * Vue correspondante (Une seule)
     */
    private AfficheAire view;
    
    /**
     * Message d'erreur
     */
    private String msgErreur;

    /**
     * Parseur XML utiliser pour le refresh
     */    
    private SAXParser parser;

    /* ***************************************
     * Constructeurs
     * *************************************** */
    /**
     * Constructeur
     * @param a Applet Aire
     * @param j Joueur connecté
     */
    public AireMainModel(AireMain a, Joueur j) {
        
        // Garde les attributs
        myJoueur = j;
        parent = a;
        
        // Initialise les autres éléments
        liaisonSrv = new ServerCall(a, this);
        lstJoueur = new Vector();
        lstPartie = new Vector();
        lstSalle = new Vector();
        lstSalleASuppr = new Vector();
        lstSalleACreer = new Vector();
        etat = ETAT_INIT;
        view = null;
        lastReceivedId = -1;
    }

    /**
     * Procédure d'initialisation chargeant les éléments indispensables
     */
    public void init () {
        // Chargement du fichier de configuration XML
        String config = parent.getParameter("config");
        
        etat = ETAT_INIT;
        liaisonSrv.load(config, this);
        
        // La récupération d'une situation du moment est automatique
        JFrame.setDefaultLookAndFeelDecorated(true);
    }
    
    /**
     * Construit la vue
     * @param configXml fichier de config
     */
    public void buildView(String configXml) {
        
        switch (etat) {
            case ETAT_INIT :
                break;

            case ETAT_INIT_P2 : {
                // Chargement de la config
                try {
                    decodeConfig(configXml);
                }
                catch (DataFormatException e) {
                    parent.getLogger().severe("Décodage erroné");
                }
        
                // Chargement des configurations externes
                liaisonSrv.load(getSkinConfig().getRChatSmileyUrl(), this);

                break;
            }
            
            case ETAT_INIT_P3 : {
                // Chargement de la config des smiley
                try {
                    Chat.decodeSmiley(configXml);
                }
                catch (DataFormatException e) {
                    parent.getLogger().severe("Décodage erroné");
                }
        
        
                // Chargement des configurations externes
                liaisonSrv.load(getSkinConfig().getSChatSonUrl(), this);

                break;
            }
            
            case ETAT_OK : {
                // Chargement de la config des sons
                try {
                    Chat.decodeSon(configXml);
                }
                catch (DataFormatException e) {
                    parent.getLogger().severe("Décodage erroné");
                }
        
                // Attente pour l'affichage du retour : Lance l'init applicative
                liaisonSrv.load("doAction.srv?Action=init", this);

                // Création de la vue en dehors de Swing pour ne pas le faire attendre
                // L'affichage sera fait dans Swing
                view = new AfficheAire(AireMainModel.this, parent.getWidth(), parent.getHeight());
                
                // Construit la fenêtre principale
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        public void run() {
                            parent.setLayout(new BorderLayout());
                            parent.add(view, BorderLayout.CENTER);
                            // Force la mise à jour car la fenêtre a déjà été affichée
                            parent.validate();
                        }
                        });
                }
                catch (InterruptedException e) {
                    // Ignore cette erreur
                }
                catch (InvocationTargetException e) {
                    parent.getLogger().severe("Erreur dans le refresh" + e.toString());
                    e.getCause().printStackTrace();
                }
                break;
            }

            default :
                break;
        }
    }

    /**
     * Décode la configuration XML
     * @param configXml Contenu XML du fichier de config
     * @throws DataFormatException Si le format n'est pas correct
     */
    public synchronized void decodeConfig(String configXml) throws DataFormatException {
        
        // Initialisations pour la lecture XML
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        
        parserFactory.setValidating(true);
        parserFactory.setNamespaceAware(true);
        
        SAXParser parser = null;
        try {
            parser = parserFactory.newSAXParser();
        }
        catch (ParserConfigurationException e) {
            System.err.println("Erreur technique (ParserConfigurationException) à la création du parser XML : " + e);
            e.printStackTrace();
            throw new DataFormatException("Erreur technique");
        }
        catch (SAXException e) {
            System.err.println("Erreur technique (SAXException) à la création du parser XML : " + e);
            e.printStackTrace();
            throw new DataFormatException("Erreur technique");
        }
        
        try {
            InputStream is = new ByteArrayInputStream(configXml.getBytes());
            skinConfig = new Configurator();
            parser.parse(is, skinConfig);
        }
        catch (IOException e) {
            parent.getLogger().severe("Erreur technique (IOException) au decryptage du fichier XML : " + e);
            e.printStackTrace();
            throw new DataFormatException("Erreur technique");
            
        }
        catch (SAXException e) {
            parent.getLogger().severe("Erreur technique (SAXException) au decryptage du fichier XML : " + e);
            e.printStackTrace();
            throw new DataFormatException("Erreur format");
        }
    }

    /* **************************************************************************** */
    /* Getter & Setter                                                              */
    /* **************************************************************************** */

    /**
     * @return Joueur connecté
     */
    public Joueur getMyJoueur() {
        return myJoueur;
    }

    /**
     * @param joueur Joueur connecté
     */
    public void setMyJoueur(Joueur joueur) {
        myJoueur = joueur;
    }

    /**
     * @return Liaison avec le serveur à utiliser pour tout échange
     */
    public ServerCall getLiaisonSrv() {
        return liaisonSrv;
    }

    /**
     * @param call Liaison avec le serveur à utiliser pour tout échange
     */
    public void setLiaisonSrv(ServerCall call) {
        liaisonSrv = call;
    }

    /**
     * Retourne un joueur connecté
     * @param indice Numéro du joueur demandé
     * @return Joueur (Peut être null)
     */
    public Joueur getJoueur(int indice) {
        if ((indice < 0) || (indice >= lstJoueur.size())) {
            return null;
        }
        return (Joueur) lstJoueur.get(indice);
    }
    
    /**
     * Retourne une partie à afficher (en cours, finie, ...)
     * @param indice Numéro de la partie demandée
     * @return Partie (Peut être null)
     */
    public Partie getPartie(int indice) {
        if ((indice < 0) || (indice >= lstPartie.size())) {
            return null;
        }
        return (Partie) lstPartie.get(indice);
    }

    /**
     * 
     * @return Nombre de partie en cours, finie, ...
     */
    public int getNbPartie() {
        return lstPartie.size();    
    }

    /**
     * @return Config complète
     */
    public Configurator getSkinConfig() {
        return skinConfig;
    }

    /**
     * @return Liste des joueurs
     */
    public Vector getLstJoueur() {
        return lstJoueur;
    }

    /**
     * @return Liste des parties
     */
    public Vector getLstPartie() {
        return lstPartie;
    }

    /**
     * @return Applet principale
     */
    public AireMain getParent() {
        return parent;
    }

    /**
     * @return  Dernier identifiant reçu du serveur
     */
    public int getLastReceivedId() {
        return lastReceivedId;
    }

    /**
     * @param i  Dernier identifiant reçu du serveur
     */
    public void setLastReceivedId(int i) {
        lastReceivedId = i;
    }

    /**
     * @return logger à utiliser
     */
    public Logger getLogger() {
        return parent.getLogger();
    }

    /**
     * @return Etat de l'aire
     */
    public int getEtat() {
        return etat;
    }

    /* **************************************************************************** */
    /* Méthodes fonctionnelles                                                      */
    /* **************************************************************************** */

    /**
     * Chargemement d'un son pour le jouer
     * @param nomSon Son à charger
     * @return Son chargé
     * @throws MalformedURLException Si le nom n'est pas valide
     */
    public AudioClip getAudioClip(String nomSon) throws MalformedURLException {
        return getAudioClip(nomSon, true);
    }
    
    /**
     * Chargemement d'un son pour le jouer
     * @param nomSon Son à charger
     * @param doLoad Indique si le chargement doit être lancé si besoin (sinon utilisation du cache)
     * @return Son chargé ou null si pas le son en cache et le chargement n'est pas demandé
     * @throws MalformedURLException Si le nom n'est pas valide
     */
    public AudioClip getAudioClip(String nomSon, boolean doLoad) throws MalformedURLException {
        return liaisonSrv.getAudioClip(nomSon, doLoad);
    }
    
    /**
     * 
     * @param nomImage Image à charger
     * @return Image en cours de chargement
     * @throws MalformedURLException Si le nom n'est pas valide
     */
    public Image getImage(String nomImage) throws MalformedURLException {
        return liaisonSrv.getImage(nomImage);
    }
    
    /**
     * @param nomImage Image à charger
     * @return Image en cours de chargement
     * @throws MalformedURLException Si le nom n'est pas valide
     */
    public ImageIcon getImageIcon(String nomImage) throws MalformedURLException {
        return getLiaisonSrv().getImageIcon(nomImage);
    }

    /**
     * @param nomImage Image à charger
     * @return Image en cours de chargement
     * @throws MalformedURLException Si le nom n'est pas valide
     */
    public Icon getRelativeImageIcon(String nomImage) throws MalformedURLException {
        return getLiaisonSrv().getRelativeImageIcon(nomImage);
    }
    
    /**
     * Création d'une nouvelle salle
     * @param newPartie Partie préinitialisée
     */
    public void createMySalle(PartieBelote newPartie) {

        // Fini l'initialisation
        newPartie.setOwner(myJoueur);
        if (newPartie.getTitre().length() == 0) {
            // Titre par défaut
            newPartie.setTitre("Partie de " + myJoueur.getPseudo());
        }
        newPartie.addParticipant(myJoueur, Joueur.SUD);
        // Ajoute à la liste des parties
        ajoutePartie(newPartie);

        // Ca peut être un peu long
        view.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        view.updateParties();

        // Création du modèle de salle
        createSalle(newPartie);
        
        // Notifie le serveur de la création
        try {
            String url = "doAction.srv?Action=addPartie";
            url = url + "&Pseudo=" + URLEncoder.encode(myJoueur.getPseudo(), "UTF8");
            url = url + "&Titre=" + URLEncoder.encode(newPartie.getTitre(), "UTF8");
            if (newPartie instanceof PartieBeloteClassique) {
                url = url + "&Type=C";
            }
            else {
                url = url + "&Type=M";
            }
            if (newPartie.isAnnonce()) {
                url = url + "&Annonce=O";
            }
            else {
                url = url + "&Annonce=N";
            }
            url = url + "&MDP=" + URLEncoder.encode(newPartie.getMotDePasse(), "UTF8");
            if ((newPartie.getMyTournoi() != null) && (newPartie.getMyTournoi().getIdentifiant() > 0)) {
                url = url + "&idTournoi=" + newPartie.getMyTournoi().getIdentifiant();
            }
            liaisonSrv.load(url, this);
        }
        catch (UnsupportedEncodingException e) {
            parent.getLogger().severe("Problème d'encodage de l'URL");
        }
    }

    /**
     * Ajoute une partie à la liste des parties tout en conservant l'ordre 
     * @param newPartie partie à ajouter
     */
    private void ajoutePartie(PartieBelote newPartie) {
        // Toujours au début
        lstPartie.add(0, newPartie);
    }

    /**
     * Création d'une salle pour la voir
     * @param aPartie partie à afficher
     */
    protected void createSalle(PartieBelote aPartie) {
        
        // Ca peut être un peu long
        view.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        SalleModel model = new SalleModel(this, aPartie, myJoueur);
        lstSalle.add(model);
        
        if (SwingUtilities.isEventDispatchThread()) {
            // Crée la vue
            model.buildView();
        }
        else {
            lstSalleACreer.add(model);
        }
    }

    /**
     * Envoi un message Chat
     * @param message Message saisi
     * @param couleur Couleur du texte
     * @param pseudoDest Destinataire (null si global)
     * @param idPartie Identifiant de la partie si message au sein d'une partie (0 sinon)
     */
    public void sendChat(String message, String couleur, String pseudoDest, int idPartie) {
        try {
            
            // Effectue l'affichage en local
            Chat aChat = new Chat();
            aChat.setText(message);
            aChat.setJoueurOrig(myJoueur);
            if (pseudoDest != null) {
                Joueur origD = new Joueur();
                origD.setPseudo(pseudoDest);
                aChat.setJoueurDest(origD);
            }
            // Ne positionne la couleur que pour qq chose de différent du noir par défaut
            if (!couleur.equals("#000000")) {
                aChat.setStyle("<font color=\"" + couleur + "\">");
            }
            if (idPartie != 0) {
                // Recherche la partie dans la liste des parties
                for (int j = 0; j < getLstPartie().size(); j++) {
                    Partie aPartie = (Partie) getLstPartie().get(j); 
                    if (aPartie.getIdentifiant() == idPartie) {
                        aChat.setPartie(aPartie);
                        
                        SalleModel salle = findSalle(aPartie);
                            
                        if (salle != null) {
                            salle.addPendingChat(aChat);
                            salle.refresh();
                        }
                    }
                }

            }
            else {
                view.addPendingChat(aChat);
                view.refresh();
            }
            
            StringBuffer url = new StringBuffer();
            url = url.append("doAction.srv?Action=addChat&Pseudo=");
            url = url.append(URLEncoder.encode(myJoueur.getPseudo(), "UTF8")); 

            if (pseudoDest != null) {
                // Message privé
                url = url.append("&To=" + URLEncoder.encode(pseudoDest, "UTF8"));
            }
            
            if (idPartie != 0) {
                // Message au sein d'une partie
                url = url.append("&Partie=" + idPartie);
            }
            url = url.append("&Msg=" + URLEncoder.encode(message, "UTF8"));
            
            url = url.append("&Couleur=" + URLEncoder.encode(couleur, "UTF8"));
            
            // C'est parti
            liaisonSrv.load(url.toString(), this);
        }
        catch (UnsupportedEncodingException e) {
            parent.getLogger().severe("Problème d'encodage de l'URL");
        }
    }
    
    /**
     * Quitte l'aire de jeu en avertissant le serveur
     */
    public void quitte() {
        try {
            liaisonSrv.load("doAction.srv?Action=quit&Pseudo=" + URLEncoder.encode(myJoueur.getPseudo(), "UTF8"), this);
        }
        catch (UnsupportedEncodingException e) {
            parent.getLogger().severe("Problème d'encodage de l'URL");
        }
        
        etat = QUIT;
    }
    
    /**
     * Ouverture de la page d'historique du joueur
     * @param pseudo Pseudo du joueur à afficher
     */
    public void ouvrePageJoueur(String pseudo) {
        if (pseudo != null) {
            try {
                ouvrePopup("affJoueur.srv?Pseudo=" + URLEncoder.encode(pseudo, "UTF8"), "joueurInCrEG");
            }
            catch (UnsupportedEncodingException e) {
                parent.getLogger().severe("Problème d'encodage de l'URL");
            }
        }
    }

    /**
     * Ouverture de la page d'administration de l'aire
     */
    public void ouvreAdmin() {
        try {
            ouvrePopup("admin.srv?Pseudo=" + URLEncoder.encode(myJoueur.getPseudo(), "UTF8"), "adminInCrEG");
        }
        catch (UnsupportedEncodingException e) {
            parent.getLogger().severe("Problème d'encodage de l'URL");
        }
    }

    /**
     * Ouverture une page quelconque
     * @param url Url à ouvrir
     * @param target nom de fenêtre
     */
    public void ouvrePopup(String url, String target) {
        try {
            parent.getAppletContext().showDocument(new URL(url), target);
        }
        catch (MalformedURLException e) {
            try {
                parent.getAppletContext().showDocument(new URL(parent.getDocBase() + "/" + url), target);
            }
            catch (MalformedURLException e2) {
                parent.getLogger().severe("Url " + url + " invalide");
            }
        }
    }

    /**
     * Un joueur rejoint une partie
     * @param numPartie Indice de la partie dans la liste des parties
     * @param numJoueur Indice du joueur
     */
    public void joueurRejointPartie(int numPartie, int numJoueur) {
        
        // Vérification si privée
        Partie aPartie = (Partie) lstPartie.get(numPartie);
        String mdpSaisi = "";
        if (aPartie.isPrivate()) {
            // Demande le mot de passe
            mdpSaisi = JOptionPane.showInputDialog(view, "Cette partie est privée. Veuillez saisir le mot de passe :");
        }
        
        if (mdpSaisi != null) {
            // Pas d'annulation
            // Envoie la demande
            try {
                liaisonSrv.load("doAction.srv?Action=rejointPartie&Pseudo=" + URLEncoder.encode(myJoueur.getPseudo(), "UTF8") 
                                + "&idPartie=" + aPartie.getIdentifiant()
                                + "&place=" + numJoueur
                                + "&mdp=" + URLEncoder.encode(mdpSaisi, "UTF8"), this);
            }
            catch (UnsupportedEncodingException e) {
                parent.getLogger().severe("Problème d'encodage de l'URL");
            }
        }
    }
    
    /**
     * Les smileys ont été changés 
     */
    public void resetSmiley() {
        view.resetSmiley();
    }
    
    /**
     * Affiche une partie ou change le focus si elle est déjà ouverte
     * @param numPartie Numéro de la partie
     */
    public void affichePartie(int numPartie) {

        Partie aPartie = (Partie) lstPartie.get(numPartie);

        // Fenêtre déjà ouverte ?
        SalleModel salle = findSalle(aPartie);
        
        if (salle == null) {
            // Vérification si privée
            String mdpSaisi = "";
            if (aPartie.isPrivate()) {
                // Demande le mot de passe
                mdpSaisi = JOptionPane.showInputDialog(view, "Cette partie est privée. Veuillez saisir le mot de passe :");
            }
        
            if (mdpSaisi != null) {
                // Pas d'annulation
                // Envoie la demande
                try {
                    liaisonSrv.load("doAction.srv?Action=regardePartie&Pseudo=" + URLEncoder.encode(myJoueur.getPseudo(), "UTF8") 
                                    + "&idPartie=" + aPartie.getIdentifiant()
                                    + "&mdp=" + URLEncoder.encode(mdpSaisi, "UTF8"), this);
                }
                catch (UnsupportedEncodingException e) {
                    parent.getLogger().severe("Problème d'encodage de l'URL");
                }
            }
        }
        else {
            // La salle est déjà ouverte
            salle.setFocus();
        }
    }
    
    /**
     * Recherche la salle correspondante à la partie
     * @param aPartie partie concernée
     * @return salle trouvée
     */
    private SalleModel findSalle(Partie aPartie) {
        SalleModel salle = null;
        boolean found = false;
        
        for (int i = 0; !found && (i < lstSalle.size()); i++) {
            salle = (SalleModel) lstSalle.get(i);
            if (salle.getMyPartie() == aPartie) {
                found = true;
            }
        }
        if (!found) {
            return null;
        }
        else {
            return salle;
        }
    }

    /**
     * Dispatch le retour du serveur aux fenêtres / Model pour mise à jour
     * @param fluxRefresh contenu retourné par le serveur
     * @return dernier identifiant interprété
     */
    private synchronized int dispatchRefresh(InputStream fluxRefresh) {
        
        int cr = 0;
        
        // TODO Debug EG A suppr
        byte dataBytes[] = new byte[4096];
        int byteRead = 0;
        ByteArrayOutputStream resultat = new ByteArrayOutputStream(4096);
        byteRead = 0;
        while (byteRead != -1) {
            try {
                byteRead = fluxRefresh.read(dataBytes, 0, 4096);
            }
            catch (IOException e1) {
                e1.printStackTrace();
                byteRead = -1;
            }
    
            // Recopie dans la liste
            if (byteRead != -1) {
                resultat.write(dataBytes, 0, byteRead);
            }
        }
        ByteArrayInputStream fluxByte = new ByteArrayInputStream(resultat.toByteArray());
        // Debug EG
        
        // Optimisation : Rien dedans = Rien à faire        
        if (resultat.size() > 4) {
            // Initialisations pour la lecture XML
            if (parser == null) {
                SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        
                parserFactory.setValidating(false);
                parserFactory.setNamespaceAware(false);
        
                try {
                    parser = parserFactory.newSAXParser();
                }
                catch (ParserConfigurationException e) {
                    parent.getLogger().severe("Erreur technique (ParserConfigurationException) à la création du parser XML : " + e);
                    e.printStackTrace();
                    // Ignore le refresh du coup
                    return lastReceivedId;
                }
                catch (SAXException e) {
                    parent.getLogger().severe("Erreur technique (SAXException) à la création du parser XML : " + e);
                    e.printStackTrace();
                    // Ingore le refresh du coup
                    return lastReceivedId;
                }
            }
        
            try {
                Refresher aRefresh = new Refresher();
                // Log Optimisation Vitesse
                parent.getLogger().finest(System.currentTimeMillis() + " : Avant parse");
                parser.parse(fluxByte, aRefresh);
                // Log Optimisation Vitesse
                parent.getLogger().finest(System.currentTimeMillis() + " : Après parse");
                cr = aRefresh.getLastReceivedId();    
            }
            catch (IOException e) {
                parent.getLogger().severe("Erreur technique (IOException) au decryptage du fichier XML : " + e);
                parent.getLogger().severe("Reçu : \n" + resultat.toString());
                e.printStackTrace();
                // Ingore le refresh du coup
                return lastReceivedId;
            }
            catch (SAXException e) {
                parent.getLogger().severe("Erreur technique (SAXException) au decryptage du fichier XML : " + e);
                parent.getLogger().severe("Reçu : \n" + resultat.toString());
                e.getException().printStackTrace();
                // Ingore le refresh du coup
                return lastReceivedId;
            }
        
            /**
             *  Mise à jour des vues
             */
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        // Informe la vue
                        // Log Optimisation Vitesse
                        parent.getLogger().finest(System.currentTimeMillis() + " : Début refresh graphique");
                        while (lstSalleASuppr.size() > 0) {
                            SalleModel salle = (SalleModel) lstSalleASuppr.remove(0);
                            salle.suppressionSalle();
                        }
                        while (lstSalleACreer.size() > 0) {
                            SalleModel salle = (SalleModel) lstSalleACreer.remove(0);
                            salle.buildView();
                        }
                        for (int i = 0; i < lstSalle.size(); i++) {
                            SalleModel salle = (SalleModel) lstSalle.get(i);
                            salle.refresh();
                        }
                        if (view != null) {
                            view.refresh();
                        }
                        // Log Optimisation Vitesse
                        parent.getLogger().finest(System.currentTimeMillis() + " : Fin Refresh graphique");
                    }
                    });
            }
            catch (InterruptedException e) {
                // Ignore cette erreur
            }
            catch (InvocationTargetException e) {
                parent.getLogger().severe("Erreur dans le refresh" + e.toString());
                e.getCause().printStackTrace();
            }

            if (msgErreur != null) {
                int cut = msgErreur.indexOf("|");
                if (cut != -1) {
                    // Message pour une partie
                    String idPartie = msgErreur.substring(0, cut);
                    PartieBelote aPartie = null;
                    boolean found = false;
                    for (int i = 0; !found && (i < lstPartie.size()); i++) {
                        aPartie = (PartieBelote) lstPartie.get(i); 
                        if (aPartie.getIdentifiant() == Integer.parseInt(idPartie)) {
                            found = true;
                        }
                    }
                    if (found) {
                        SalleModel aSalle = findSalle(aPartie);
                        JOptionPane.showMessageDialog(aSalle.getView(), msgErreur.substring(cut), "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(view, msgErreur.substring(cut + 1), "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else {
                    // Message général
                    JOptionPane.showMessageDialog(view, msgErreur, "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        
        }
        else {
            cr = lastReceivedId;
        }
        
        return cr;
    }


    /**
     * Une salle se ferme 
     * @param salle Salle qui ferme
     */
    public void fermetureSalle(SalleModel salle) {
        
        if (salle.getMyPartie().getIdentifiant() > 0) {
            // Averti le serveur que le joueur est sortie de la salle
            try {
                liaisonSrv.load("doAction.srv?Action=quittePartie&Pseudo=" + URLEncoder.encode(myJoueur.getPseudo(), "UTF8") 
                                + "&idPartie=" + salle.getMyPartie().getIdentifiant(), this);
            }
            catch (UnsupportedEncodingException e) {
                parent.getLogger().severe("Problème d'encodage de l'URL");
            }
        }
        else {
            salle.suppressionSalle();
        }
    }

    /**
     * Supprime la salle de la liste et la fenêtre proprement dite
     * @param aPartie partie concernée
     */    
    protected void suppressionSalle(Partie aPartie) {
        SalleModel salle = findSalle(aPartie);
        if (salle != null) {
            // Recherche dans la liste pour la supprimer 
            boolean found = false;
            found = false;
            for (int i = 0; !found && (i < lstSalle.size()); i++) {
                if (lstSalle.get(i) == salle) {
                    found = true;
                    // Suppression ==> GC doit faire la suite
                    lstSalle.remove(i);
                    if (SwingUtilities.isEventDispatchThread()) {
                        salle.suppressionSalle();
                    }
                    else {
                        lstSalleASuppr.add(salle);
                    }
                }
            }
        }
    }

    /**
     * Fermeture violente de l'aire
     *
     */
    public void dropAll() {
        if (liaisonSrv != null) {
            liaisonSrv.finalize();
            liaisonSrv = null;
        }
        if (view != null) {
            view.removeAll();
            view = null;
            parent.removeAll();
        }
        
        for (int i = 0; i < lstSalle.size(); i++) {
            SalleModel salle = (SalleModel) lstSalle.get(i);
            salle.suppressionSalle();
        }
        
        lstSalle.clear();
        lstPartie.clear();
        
        // Charge la page de fin
        ouvrePopup(parent.getDocBase() + "/" + "bye.html", "_top");
        
    }

    /**
     * @see com.increg.game.net.ServerCallRequester#notifyLoadEnd(java.net.URL, byte[])
     */
    public int notifyLoadEnd(URL currentUrl, byte[] content) {
        
        // Fin de chargement de quoi ?
        if ((etat == ETAT_INIT) && (currentUrl.getFile().indexOf(".xml") >= 0)) {
            // Fin du chargement du fichier de config
            etat++;
            buildView(new String(content));
        }
        else if ((etat == ETAT_INIT_P2) && (currentUrl.getFile().indexOf(".xml") >= 0)) {
            // Fin du chargement du fichier de config
            etat++;
            buildView(new String(content));
        }
        else if ((etat == ETAT_INIT_P3) && (currentUrl.getFile().indexOf(".xml") >= 0)) {
            // Fin du chargement du fichier de config
            etat++;
            buildView(new String(content));
        }
        else if (etat == QUIT) {
            // C'est la fin
            ouvrePopup(parent.getDocBase() + "/" + "bye.html", "_top");
        }
        else if ((currentUrl.getFile().indexOf("refreshAll") != 0) 
                  || (currentUrl.getFile().indexOf("doAction.srv") != 0)) {
            // Récupération de l'état
            try {
                // Log Optimisation Vitesse
                parent.getLogger().finest(System.currentTimeMillis() + " : Avant dispatch");
                lastReceivedId = dispatchRefresh(new GZIPInputStream(new ByteArrayInputStream(content)));
                // Log Optimisation Vitesse
                parent.getLogger().finest(System.currentTimeMillis() + " : Après dispatch");
            }
            catch (IOException e) {
                e.printStackTrace();
                // Pas de rappel...
                parent.destroy();
            }
        }
        return lastReceivedId;
    }

    /* ***************************************
     * 
     * *************************************** */
    /**
     * Classe interprétant le flux
     */
    public class Refresher extends DefaultHandler {

        /**
         * Eléments rencontrés : Donne la hiérarchie
         */
        protected Stack elements;

        /**
         * Dernier identifiant reçu
         */
        protected int lastReceivedId;
        
        /**
         * Compteur des occurences de joueurs
         */
        protected int cptJoueur;

        /**
         * Indice du joueur connecté dans la partie en cours
         */
        protected int indiceJoueur;
        
        /**
         * Liste temporaire des joueurs
         */
        protected Vector lstInterneJoueur;

        /**
         * Liste temporaire des parties nouvelles
         */
        protected Vector lstInternePartie;
        /**
         * Liste temporaire des parties mise à jour (pour gestion des suppressions)
         */
        protected TreeSet lstPartieMaj;
        /**
         * Partie en cours de rechargement
         */
        protected PartieBelote partieEnCours;
        /**
         * Indicateur si la partie en cours doit être mise à jour
         */
        protected boolean updatePartie;
        /**
         * Annonce en cours de chargement
         */
        protected AnnonceBelote annonceEnCours;
        /**
         * Indice de la carte suivante de l'annonce
         */
        private int iCarteAnnonce;

        
        /**
         * Constructeur
         *
         */
        public Refresher() {        
            elements = new Stack();
        }

        /**
         * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
         */
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

            try {
                elements.push(qName);
                String qualifiedName = elements.toString();
                
                if (qualifiedName.equals(XML_TAGS_STATUS)) {
                    msgErreur = attributes.getValue(XML_ATT_ERROR); 
                }
                else if (qualifiedName.equals(XML_TAGS_CACHE)) {
                    // Activation du cache Joueur + Parties
                    lstInterneJoueur.addAll(lstJoueur);
                    lstPartieMaj.addAll(lstPartie);
                }
                else if (qualifiedName.equals(XML_TAGS_JOUEUR)) {
                    Joueur aJoueur = new Joueur();
                    try {
                        aJoueur.setAvatarHautePerf(new URL(attributes.getValue(XML_ATT_AVATAR)));
                        aJoueur.setAvatarFaiblePerf(new URL(attributes.getValue(XML_ATT_AVATAR)));
                    }
                    catch (MalformedURLException e) {
                        // Ignore l'erreur : Pas d'avatar
                    }
                    aJoueur.setPseudo(attributes.getValue(XML_ATT_PSEUDO));
                    if (attributes.getValue(XML_ATT_MODERATEUR) != null) {
                        aJoueur.setPrivilege(Joueur.MODERATEUR_PRIVILEGE);
                    }
                    
                    if (aJoueur.equals(myJoueur)) {
                        // Conserve le privilège du joueur
                        myJoueur.setPrivilege(aJoueur.getPrivilege());
                    }
                    lstInterneJoueur.add(aJoueur);
                }
                else if (qualifiedName.equals(XML_TAGS_PARTIE)) {
                    PartieBelote aPartie = null;
                    updatePartie = true;

                    // Recherche la partie correspondante par rapport à son identifiant
                    boolean found = false;
                    for (int i = 0; !found && (i < lstPartie.size()); i++) {
                        aPartie = (PartieBelote) lstPartie.get(i); 
                        if (aPartie.getIdentifiant() == Integer.parseInt(attributes.getValue(XML_ATT_ID))) {
                            found = true;
                        }
                    }
                    
                    if ((!found) && (myJoueur.getPseudo().equals(attributes.getValue(XML_ATT_PSEUDO)))) {
                        // Recherche une partie non numérotée (par rapport à son propriétaire) 
                        for (int i = 0; !found && (i < lstPartie.size()); i++) {
                            aPartie = (PartieBelote) lstPartie.get(i);
                            if ((aPartie.getIdentifiant() == 0) 
                                && (aPartie.getOwner().getPseudo().equals(myJoueur.getPseudo()))
                                && (aPartie.getTitre().equals(attributes.getValue(XML_ATT_TITRE)))) {  
                                // Trouvé
                                found = true;
                                aPartie.setIdentifiant(Integer.parseInt(attributes.getValue(XML_ATT_ID)));
                            }
                        }
                    }
                    
                    if (!found) {
                        // C'est une nouvelle partie : Positionnes les attributs invariables de la partie
                        if (attributes.getValue(XML_ATT_TYPE).equals("C")) {
                            aPartie = new PartieBeloteClassique();
                        }
                        else {
                            aPartie = new PartieBeloteModerne();
                        }
                        aPartie.setTitre(attributes.getValue(XML_ATT_TITRE));
                        // Recherche le propriétaire de la salle parmi la liste des joueurs de la salle
                        boolean joueurFound = false;
                        for (int i = 0; !joueurFound && (i < lstInterneJoueur.size()); i++) {
                            Joueur aJoueur = (Joueur) lstInterneJoueur.get(i);
                            if (aJoueur.getPseudo().equals(attributes.getValue(XML_ATT_PSEUDO))) {
                                aPartie.setOwner(aJoueur);
                                joueurFound = true;
                            }
                        }
                        aPartie.setAnnonce(attributes.getValue(XML_ATT_ANNONCE).equals("O"));
                        aPartie.setIdentifiant(Integer.parseInt(attributes.getValue(XML_ATT_ID)));
                        aPartie.setMotDePasse(attributes.getValue(XML_ATT_MDP));
                        if (attributes.getValue(XML_ATT_TOURNOI) != null) {
                            Tournoi aTournoi = new Tournoi();
                            aTournoi.setIdentifiant(Integer.parseInt(attributes.getValue(XML_ATT_TOURNOI)));
                            aPartie.setMyTournoi(aTournoi);
                        }
                        
                        lstInternePartie.add(aPartie);
                    }
                
                    // Gestion du Step
                    if (aPartie.getStep() > Long.parseLong(attributes.getValue(XML_ATT_STEP))) {
                        // Il y a eu maj locale entre temps : Cette partie ne doit pas être mise à jour
                        updatePartie = false;
                    }
                    
                    
                    if (updatePartie) {
                        
                        // Maj du step
                        aPartie.setStep(Long.parseLong(attributes.getValue(XML_ATT_STEP)));
                        
                        // Partie Optionnelle en fonction si la salle est ouverte
                        if (attributes.getValue(XML_ATT_ETAT) != null) {
                            // Charge l'état
                            aPartie.getEtat().setEtat(Integer.parseInt(attributes.getValue(XML_ATT_ETAT)));
                            aPartie.getEtat().setJoueur(Integer.parseInt(attributes.getValue(XML_ATT_JOUEUR)));
                        
                        }
                        if (attributes.getValue(XML_ATT_PRENEUR) != null) {
                            // Recherche le preneur de la partie parmi la liste des joueurs de la salle

                            aPartie.setPreneur(Integer.parseInt(attributes.getValue(XML_ATT_PRENEUR)));
                            aPartie.setAtout(Integer.parseInt(attributes.getValue(XML_ATT_ATOUT)));
                            aPartie.setOuvreur(Integer.parseInt(attributes.getValue(XML_ATT_OUVREUR)));
                            aPartie.setDernierRamasse(Integer.parseInt(attributes.getValue(XML_ATT_RAMASSE)));
                        }
                        else {
                            aPartie.setPreneur(-1);
                            aPartie.setAtout(-1);
                            aPartie.setOuvreur(-1);
                            aPartie.setDernierRamasse(-1);
                        }
                        if (attributes.getValue(XML_ATT_COUPEUR) != null) {
                            aPartie.getEtat().setJoueurCoupe(Integer.parseInt(attributes.getValue(XML_ATT_COUPEUR)));
                        }

                        // RAZ du compteur de joueur
                        cptJoueur = 0;
                        indiceJoueur = 0;
                        // RAZ des spectateurs pour ajout ensuite
                        aPartie.getSpectateurs().clear();
                        aPartie.getJeu().getTapis().clear();
                        if ((aPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_COUPE_JEU)
                            || (aPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_DISTRIBUTION)
                            || (aPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_FIN_DONNE)
                            || (aPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_FIN_PARTIE)
                            || (aPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_NON_DEMARRE)
                            || (aPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_TOUR_ENCHERE1)
                            || (aPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_TOUR_ENCHERE1_PRIS)
                            || (aPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_TOUR_ENCHERE2)
                            || (aPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_TOUR_ENCHERE2_PRIS)) {
                        
                            // Reset des annonces
                            for (int i = 0; i < aPartie.getJeu().getAnnonces().length; i++) {
                                aPartie.getJeu().getAnnonces()[i].clear();
                            }
                        }
                    }

                    partieEnCours = aPartie;
                    lstPartieMaj.add(aPartie);
                    
                }
                else if (qualifiedName.equals(XML_TAGS_PARTIE_JOUEUR) && updatePartie) {
                    // Récupération des joueurs de la partie
                    if (attributes.getValue(XML_ATT_PSEUDO) != null) {
                        // Place le nouveau joueur en le recherchant dans
                        boolean joueurFound = false;
                        for (int i = 0; !joueurFound && (i < lstInterneJoueur.size()); i++) {
                            Joueur aJoueur = (Joueur) lstInterneJoueur.get(i);
                            if (aJoueur.getPseudo().equals(attributes.getValue(XML_ATT_PSEUDO))) {
                                partieEnCours.addParticipant(aJoueur, cptJoueur);
                                joueurFound = true;
                            }
                        }
                        
                        // Recherche si le joueur est celui connecté : 
                        // Si c'est le cas, vérification que la fenêtre est ouverte => Sinon ouvre là
                        if (myJoueur.getPseudo().equals(attributes.getValue(XML_ATT_PSEUDO))) {
                            SalleModel salle = findSalle(partieEnCours);
                            if (salle == null) {
                                // Il faut créer la salle
                                createSalle(partieEnCours);
                            }
                            indiceJoueur = cptJoueur;
                        }
                    }
                    else {
                        // Plus de joueur
                        partieEnCours.addParticipant(null, cptJoueur);
                    }
                    cptJoueur++;
                }
                else if (qualifiedName.equals(XML_TAGS_PARTIE_SPECTATEUR) && updatePartie) {
                    // Mise à jour des spectateurs par annulation / remplacement
                    boolean joueurFound = false;
                    for (int i = 0; !joueurFound && (i < lstInterneJoueur.size()); i++) {
                        Joueur aJoueur = (Joueur) lstInterneJoueur.get(i);
                        if (aJoueur.getPseudo().equals(attributes.getValue(XML_ATT_PSEUDO))) {
                            partieEnCours.addSpectateur(aJoueur);
                            joueurFound = true;
                        }
                    }
                    
                    // Recherche si le joueur est celui connecté : 
                    // Si c'est le cas, vérification que la fenêtre est ouverte => Sinon ouvre là
                    if (myJoueur.getPseudo().equals(attributes.getValue(XML_ATT_PSEUDO))) {
                        SalleModel salle = findSalle(partieEnCours);
                        if (salle == null) {
                            // Il faut créer la salle
                            createSalle(partieEnCours);
                        }
                    }
                
                }
                else if (qualifiedName.equals(XML_TAGS_PARTIE_SCORE) && updatePartie) {
                    // Score
                    partieEnCours.addScore(Integer.parseInt(attributes.getValue(XML_ATT_VALEUR)), Integer.parseInt(attributes.getValue(XML_ATT_POS)));
                }
                else if (qualifiedName.equals(XML_TAGS_PARTIE_SCORE_DONNE) && updatePartie) {
                    // Score
                    partieEnCours.setScoreDonne(Integer.parseInt(attributes.getValue(XML_ATT_POS)), Integer.parseInt(attributes.getValue(XML_ATT_VALEUR)));
                }
                else if (qualifiedName.equals(XML_TAGS_PARTIE_JEU) && updatePartie) {
                    // Raz de la main actuelle
                    partieEnCours.getJeu().getMains(indiceJoueur).clear();
                }
                else if (qualifiedName.equals(XML_TAGS_PARTIE_JEU_CARTE) && updatePartie) {
                    // Une carte du jeu
                    partieEnCours.getJeu().getMains(indiceJoueur)
                                .add(partieEnCours.buildCarte(Integer.parseInt(attributes.getValue(XML_ATT_HAUTEUR)), 
                                                        Integer.parseInt(attributes.getValue(XML_ATT_COULEUR))));
                }
                else if (qualifiedName.equals(XML_TAGS_PARTIE_TAS_CARTE) && updatePartie) {
                    // La carte au dessu du tas
                    partieEnCours.getJeu().getTas().clear();
                    partieEnCours.getJeu().getTas().add(new CouleurBelote(Integer.parseInt(attributes.getValue(XML_ATT_HAUTEUR)), 
                                                        Integer.parseInt(attributes.getValue(XML_ATT_COULEUR))));
                }
                else if (qualifiedName.equals(XML_TAGS_PARTIE_TAPIS_CARTE) && updatePartie) {
                    // Une carte du jeu
                    partieEnCours.getJeu().getTapis()
                                .add(partieEnCours.buildCarte(Integer.parseInt(attributes.getValue(XML_ATT_HAUTEUR)), 
                                                        Integer.parseInt(attributes.getValue(XML_ATT_COULEUR))));
                }
                else if (qualifiedName.equals(XML_TAGS_PARTIE_PLIS) && updatePartie) {
                    
                    for (int i = 0; i < partieEnCours.getParticipant().length; i++) {
                        partieEnCours.getJeu().getPlis(i).clear();
                    }
                }
                else if (qualifiedName.equals(XML_TAGS_PARTIE_PLIS_PRESENCE) && updatePartie) {
                    // Ajoute une carte quelconque
                    partieEnCours.getJeu().getPlis(Integer.parseInt(attributes.getValue(XML_ATT_POS))).add(new Couleur(0, 0));
                }
                else if (qualifiedName.equals(XML_TAGS_PARTIE_PLIS_CARTE) && updatePartie) {
                    // Une carte du jeu
                    partieEnCours.getJeu().getPlis(partieEnCours.getDernierRamasse() % 2)
                                .add(partieEnCours.buildCarte(Integer.parseInt(attributes.getValue(XML_ATT_HAUTEUR)), 
                                                        Integer.parseInt(attributes.getValue(XML_ATT_COULEUR))));
                }
                else if (qualifiedName.equals(XML_TAGS_PARTIE_ANNONCE) && updatePartie) {
                    // Annonce
                    if (partieEnCours.getJeu().getAnnonces()[Integer.parseInt(attributes.getValue(XML_ATT_JOUEUR))].size() 
                            > Integer.parseInt(attributes.getValue(XML_ATT_INDICE))) {
                        // Annonce déjà chargée... : Ignorée
                        annonceEnCours = null;
                    }
                    else {
                        annonceEnCours = new AnnonceBelote();
                        annonceEnCours.setType(Integer.parseInt(attributes.getValue(XML_ATT_TYPE)));
                        annonceEnCours.setAnnonceFaite(false);
                        annonceEnCours.setCartes(new Couleur[AnnonceBelote.NB_CARTES_CENT]);
                        partieEnCours.getJeu().getAnnonces()[Integer.parseInt(attributes.getValue(XML_ATT_JOUEUR))]
                                .add(annonceEnCours);
                        iCarteAnnonce = 0;
                    }
                    
                }
                else if (qualifiedName.equals(XML_TAGS_PARTIE_ANNONCE_CARTE) && updatePartie) {
                    // carte de l'annonce
                    if (annonceEnCours != null) {
                        annonceEnCours.getCartes()[iCarteAnnonce++] = partieEnCours.buildCarte(Integer.parseInt(attributes.getValue(XML_ATT_HAUTEUR)), 
                                                            Integer.parseInt(attributes.getValue(XML_ATT_COULEUR)));
                    }
                }
                else if (qualifiedName.equals(XML_TAGS_CHAT)) {
                    // Réception de ligne de chat : A ventiler sur les parties, en message secret, ...
                    Chat aChat = new Chat();
                    
                    if (attributes.getValue(XML_ATT_FROM) != null) {
                        Joueur origJ = new Joueur();
                        origJ.setPseudo(attributes.getValue(XML_ATT_FROM));
                        aChat.setJoueurOrig(origJ);
                        
                        // Positionne le joueur à partir de la liste de joueur
                        // permet de conserver ses privilèges
                        int pos = lstInterneJoueur.indexOf(origJ);
                        if (pos != -1) {
                            aChat.setJoueurOrig((Joueur) lstInterneJoueur.get(pos));
                        }
                    }
                    aChat.setText(attributes.getValue(XML_ATT_TEXT));
                    aChat.setStyle(attributes.getValue(XML_ATT_STYLE));
                    if (attributes.getValue(XML_ATT_TO) != null) {
                        // Message secret
                        Joueur origD = new Joueur();
                        origD.setPseudo(attributes.getValue(XML_ATT_TO));
                        aChat.setJoueurDest(origD);
                    }
                    if (attributes.getValue(XML_ATT_PARTIE) != null) {
                        // Message au sein d'une partie
                        String idPartie = attributes.getValue(XML_ATT_PARTIE);
                        PartieBelote aPartie = null;
                        
                        boolean found = false;
                        for (int i = 0; !found && (i < lstPartie.size()); i++) {
                            aPartie = (PartieBelote) lstPartie.get(i); 
                            if (aPartie.getIdentifiant() == Integer.parseInt(idPartie)) {
                                found = true;
                            }
                        }
                
                        if (found) {                
                            aChat.setPartie(aPartie);
                            SalleModel salle = findSalle(aPartie);
                            
                            if (salle != null) {
                                salle.addPendingChat(aChat);
                                lastReceivedId = Integer.parseInt(attributes.getValue(XML_ATT_ID));
                            }
                        }
                        else {
                            parent.getLogger().fine("Salle non trouvée pour message=" + aChat.getText());
                        }
                    }
                    else {
                        // Message général
                        if (view != null) {
                            view.addPendingChat(aChat);
                            lastReceivedId = Integer.parseInt(attributes.getValue(XML_ATT_ID));
                        } // Sinon le message reviendra la fois prochaine : La fenêtre sera créée
                    }
                }
            }
            catch (NumberFormatException e) {
                parent.getLogger().severe("Erreur de conversion (" + uri + "," + localName + "," + qName + "," + attributes + ") :");
                e.printStackTrace();
                throw(e);
            }
            catch (NullPointerException e) {
                parent.getLogger().severe("Erreur programme (" + uri + "," + localName + "," + qName + "," + attributes + ") :");
                e.printStackTrace();
                throw(e);
            }
        }

        /**
         * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
         */
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);

            String qualifiedName = elements.toString();

            if (qualifiedName.equals(XML_TAGS_PARTIE) && updatePartie) {
                // Ne trie qu'en début de tour, après c'est fait une fois pour toute par le serveur
                JeuBelote theJeu = (JeuBelote) partieEnCours.getJeu();
                if ((partieEnCours.getEtat().getEtat() == EtatPartieBelote.ETAT_TOUR_ENCHERE1)         
                    || (partieEnCours.getEtat().getEtat() == EtatPartieBelote.ETAT_TOUR_ENCHERE2)
                    || (partieEnCours.getEtat().getEtat() == EtatPartieBelote.ETAT_TOUR_ENCHERE1_PRIS)
                    || (partieEnCours.getEtat().getEtat() == EtatPartieBelote.ETAT_TOUR_ENCHERE2_PRIS)) {
    
                    // Tri les jeux juste pour l'affichage
                    theJeu.triMains();
                }
            }        
        
            if (!((String) elements.pop()).equals(qName)) {
                // Pas bon
                throw new SAXException("Hiérarchie non respectée pour " + elements.toString());
            }
        }


        /**
         * @see org.xml.sax.ContentHandler#endDocument()
         */
        public void endDocument() throws SAXException {
            // Bascule les données 
            
            if (lstInterneJoueur.size() == 0) {
                // Aucun joueur : Perte de la connexion
                dropAll();
            }
            lstJoueur = lstInterneJoueur;
            
            // Effectue les suppressions et les fermetures de fenêtre
            for (int i = 0; i < lstPartie.size(); i++) {
                PartieBelote aPartie = (PartieBelote) lstPartie.get(i);
                if ((aPartie.getIdentifiant() != 0) && (!lstPartieMaj.contains(aPartie))) {
                    // Supprime la salle si elle était encore affichée
                    suppressionSalle((Partie) lstPartie.remove(i));
                    // Pallie au fait que la taille sera diminée et que donc tout sera décallé
                    i--; 
                }
                else {
                    // La partie est encore active
                    // Vérification si le joueur à un role dans la partie : Si non, la fenêtre doit être fermée
                    if (!aPartie.joueurVoitPartie(myJoueur)) {
                        // Supprime la salle si elle était encore affichée
                        suppressionSalle(aPartie);
                    }
                }
            }
            // Ajoute les nouvelles parties au début
            for (int i = 0; i < lstInternePartie.size(); i++) {
                ajoutePartie((PartieBelote) lstInternePartie.get(i));
            }
            lstInterneJoueur = null;
            lstInternePartie = null;
            lstPartieMaj = null;
            partieEnCours = null;
            elements = null;
        }

        /**
         * @see org.xml.sax.ContentHandler#startDocument()
         */
        public void startDocument() throws SAXException {
            // Initialise les variables de bascule
            lstInterneJoueur = new Vector();
            lstInternePartie = new Vector();
            lstPartieMaj = new TreeSet();
            lastReceivedId = AireMainModel.this.lastReceivedId;
        }

        /**
         * @return Dernier identifiant interprété
         */
        public int getLastReceivedId() {
            return lastReceivedId;
        }

        /**
         * @param i Dernier identifiant interprété
         */
        public void setLastReceivedId(int i) {
            lastReceivedId = i;
        }

    }

    /* ***************************************
     * Constantes pour les tags et attributs du XML
     * *************************************** */
    
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAG_STATUS = "s";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAG_JOUEUR = "j";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAG_CACHE = "ca";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAG_PARTIE = "g";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAG_SCORE = "x";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAG_SPECTATEUR = "sp";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAG_JEU = "i";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAG_CARTE = "k";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAG_TAS = "ta";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAG_TAPIS = "t";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAG_PLIS = "p";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAG_PRESENCE = "r";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAG_ANNONCE = "a";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAG_SCORE_DONNE = "d";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAG_CHAT = "c";
    
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAGS_STATUS = "[" + XML_TAG_STATUS + "]";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAGS_PARTIE = "[" + XML_TAG_STATUS + ", " + XML_TAG_PARTIE + "]";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAGS_PARTIE_JOUEUR = "[" + XML_TAG_STATUS + ", " + XML_TAG_PARTIE + ", " + XML_TAG_JOUEUR + "]";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAGS_PARTIE_SPECTATEUR = "[" + XML_TAG_STATUS + ", " + XML_TAG_PARTIE + ", " + XML_TAG_SPECTATEUR + "]";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAGS_PARTIE_SCORE = "[" + XML_TAG_STATUS + ", " + XML_TAG_PARTIE + ", " + XML_TAG_SCORE + "]";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAGS_PARTIE_SCORE_DONNE = "[" + XML_TAG_STATUS + ", " + XML_TAG_PARTIE + ", " + XML_TAG_SCORE_DONNE + "]";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAGS_PARTIE_JEU = "[" + XML_TAG_STATUS + ", " + XML_TAG_PARTIE + ", " + XML_TAG_JEU + "]";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAGS_PARTIE_JEU_CARTE = "[" + XML_TAG_STATUS + ", " + XML_TAG_PARTIE + ", " + XML_TAG_JEU + ", " + XML_TAG_CARTE + "]";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAGS_PARTIE_TAS_CARTE = "[" + XML_TAG_STATUS + ", " + XML_TAG_PARTIE + ", " + XML_TAG_TAS + ", " + XML_TAG_CARTE + "]";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAGS_PARTIE_TAPIS_CARTE = "[" + XML_TAG_STATUS + ", " + XML_TAG_PARTIE + ", " + XML_TAG_TAPIS + ", " + XML_TAG_CARTE + "]";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAGS_PARTIE_PLIS = "[" + XML_TAG_STATUS + ", " + XML_TAG_PARTIE + ", " + XML_TAG_PLIS + "]";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAGS_PARTIE_PLIS_PRESENCE = "[" + XML_TAG_STATUS + ", " + XML_TAG_PARTIE + ", " + XML_TAG_PLIS + ", " + XML_TAG_PRESENCE + "]";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAGS_PARTIE_PLIS_CARTE = "[" + XML_TAG_STATUS + ", " + XML_TAG_PARTIE + ", " + XML_TAG_PLIS + ", " + XML_TAG_CARTE + "]";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAGS_PARTIE_ANNONCE = "[" + XML_TAG_STATUS + ", " + XML_TAG_PARTIE + ", " + XML_TAG_ANNONCE + "]";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAGS_PARTIE_ANNONCE_CARTE = "[" + XML_TAG_STATUS + ", " + XML_TAG_PARTIE + ", " + XML_TAG_ANNONCE + ", " + XML_TAG_CARTE + "]";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAGS_JOUEUR = "[" + XML_TAG_STATUS + ", " + XML_TAG_JOUEUR + "]";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAGS_CACHE = "[" + XML_TAG_STATUS + ", " + XML_TAG_CACHE + "]";
    /**
     * Label des tag du fichier XML de refresh 
     */ 
    public static final String XML_TAGS_CHAT = "[" + XML_TAG_STATUS + ", " + XML_TAG_CHAT + "]";

    /**
     * Label des attributs du fichier XML de refresh 
     */ 
    public static final String XML_ATT_ERROR = "er";
    /**
     * Label des attributs du fichier XML de refresh 
     */ 
    public static final String XML_ATT_PSEUDO = "p";
    /**
     * Label des attributs du fichier XML de refresh 
     */ 
    public static final String XML_ATT_AVATAR = "a";
    /**
     * Label des attributs du fichier XML de refresh 
     */ 
    public static final String XML_ATT_MODERATEUR = "mo";
    /**
     * Label des attributs du fichier XML de refresh 
     */ 
    public static final String XML_ATT_ID = "i";
    /**
     * Label des attributs du fichier XML de refresh 
     */ 
    public static final String XML_ATT_STEP = "b";
    /**
     * Label des attributs du fichier XML de refresh 
     */ 
    public static final String XML_ATT_TITRE = "ti";
    /**
     * Label des attributs du fichier XML de refresh 
     */ 
    public static final String XML_ATT_TYPE = "ty";
    /**
     * Label des attributs du fichier XML de refresh 
     */ 
    public static final String XML_ATT_ANNONCE = "an";
    /**
     * Label des attributs du fichier XML de refresh 
     */ 
    public static final String XML_ATT_MDP = "m";
    /**
     * Label des attributs du fichier XML de refresh 
     */ 
    public static final String XML_ATT_TOURNOI = "tr";
    /**
     * Label des attributs du fichier XML de refresh 
     */ 
    public static final String XML_ATT_PRENEUR = "pr";
    /**
     * Label des attributs du fichier XML de refresh 
     */ 
    public static final String XML_ATT_ATOUT = "at";
    /**
     * Label des attributs du fichier XML de refresh 
     */ 
    public static final String XML_ATT_ETAT = "e";
    /**
     * Label des attributs du fichier XML de refresh 
     */ 
    public static final String XML_ATT_JOUEUR = "j";
    /**
     * Label des attributs du fichier XML de refresh 
     */ 
    public static final String XML_ATT_OUVREUR = "ou";
    /**
     * Label des attributs du fichier XML de refresh 
     */ 
    public static final String XML_ATT_RAMASSE = "ra";
    /**
     * Label des attributs du fichier XML de refresh 
     */ 
    public static final String XML_ATT_COUPEUR = "co";
    /**
     * Label des attributs du fichier XML de refresh 
     */ 
    public static final String XML_ATT_POS = "n";
    /**
     * Label des attributs du fichier XML de refresh 
     */ 
    public static final String XML_ATT_VALEUR = "v";
    /**
     * Label des attributs du fichier XML de refresh 
     */ 
    public static final String XML_ATT_HAUTEUR = "h";
    /**
     * Label des attributs du fichier XML de refresh 
     */ 
    public static final String XML_ATT_COULEUR = "c";
    /**
     * Label des attributs du fichier XML de refresh 
     */ 
    public static final String XML_ATT_INDICE = "in";
    /**
     * Label des attributs du fichier XML de refresh 
     */ 
    public static final String XML_ATT_TEXT = "t";
    /**
     * Label des attributs du fichier XML de refresh 
     */ 
    public static final String XML_ATT_STYLE = "s";
    /**
     * Label des attributs du fichier XML de refresh 
     */ 
    public static final String XML_ATT_FROM = "f";
    /**
     * Label des attributs du fichier XML de refresh 
     */ 
    public static final String XML_ATT_TO = "d";
    /**
     * Label des attributs du fichier XML de refresh 
     */ 
    public static final String XML_ATT_PARTIE = "g";

    /* ***************************************
     * Paramètres dans l'URL
     * *************************************** */
    /**
     * Dernier identifiant de chat vu
     */
    public static final String URL_LAST_ID = "l";
    /**
     * Indicateur si les performances du client sont bonnes
     */
    public static final String URL_HAUTE_PERF = "p";
    
}
