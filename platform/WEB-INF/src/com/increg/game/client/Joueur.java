/*
 * Created on 6 avr. 2003
 *
 * Joueur d'une partie
 */
package com.increg.game.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import java.util.zip.DataFormatException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Manu
 *
 * class d'un joueur
 */
public class Joueur extends PerfoMeter implements Comparable {

    /* ********************
     * Constantes
     * *********************/
    /**
     * Code du joueur de SUD
     */
    public static final int SUD = 0;
    /**
     * Code du joueur de OEUST
     */
    public static final int OUEST = SUD + 1;
    /**
     * Code du joueur de NORD
     */
    public static final int NORD = OUEST + 1;
    /**
     * Code du joueur de EST
     */
    public static final int EST = NORD + 1;
    /**
     * Nombre maximum de joueur dans une partie
     */
    public static final int NB_MAX_JOUEUR = Joueur.EST + 1;
    
    /**
     * Equivalent de root ;-)
     */
    public static final int MAX_PRIVILEGE = 99;

    /**
     * Peut bouller les joueurs...
     */
    public static final int MODERATEUR_PRIVILEGE = 80;

    /* ***************************************
     * Attributs
     * *************************************** */
    /**
     * Pseudo du joueur
     */
    protected String pseudo = "anonyme";

    /**
     * Avatar du joueur : URL de l'image
     */
    protected URL avatarHautePerf = null;

    /**
     * Avatar du joueur : URL de l'image
     */
    protected URL avatarFaiblePerf = null;

    /**
     * Niveau de privilège
     * 0 Aucun
     * 1, 2, ... 
     */
    protected int privilege;

    /**
     * Couleur du texte du joueur
     */
    protected String couleur;
    
    /**
     * Historique des parties 
     */
    protected Vector historique;

    /**
     * Joueur Système
     */
    protected static Joueur system = null;
    
    /* ************************************************************************** */    

    /**
     * Constructeur d'un joueur anonyme
     */
    public Joueur() {
        super();
        historique = new Vector();
        privilege = 0;
        couleur = "#000000";
    }

    /**
     * @return pseudo du joueur
     */
    public String getPseudo() {
        return pseudo;
    }

    /**
     * @param string pseudo du joueur
     */
    public void setPseudo(String string) {
        pseudo = string;
    }

    /**
     * @return URL de l'avatar du joueur
     */
    public URL getAvatarFaiblePerf() {
        return avatarFaiblePerf;
    }

    /**
     * @param url URL de l'avatar du joueur
     */
    public void setAvatarFaiblePerf(URL url) {
        avatarFaiblePerf = url;
    }

    /**
     * @return URL de l'avatar du joueur
     */
    public URL getAvatarHautePerf() {
        return avatarHautePerf;
    }

    /**
     * @param url URL de l'avatar du joueur
     */
    public void setAvatarHautePerf(URL url) {
        avatarHautePerf = url;
    }

    /**
     * Donne l'image en fonction de la performance de la machine
     * @return image
     */
    public URL getAvatar() {
        if (isHautePerf()) {
            return getAvatarHautePerf();
        }
        else {
            return getAvatarFaiblePerf();
        }
    }

    /**
     * @return Historique des parties
     */
    public Vector getHistorique() {
        return historique;
    }

    /**
     * @param vector Historique des parties
     */
    public void setHistorique(Vector vector) {
        historique = vector;
    }

    /**
     * @return niveau de privilège
     */
    public int getPrivilege() {
        return privilege;
    }

    /**
     * @param i Niveau de privilège
     */
    public void setPrivilege(int i) {
        privilege = i;
    }

    /**
     * @return Couleur du texte du joueur
     */
    public String getCouleur() {
        return couleur;
    }

    /**
     * @param string Couleur du texte du joueur
     */
    public void setCouleur(String string) {
        couleur = string;
    }

    /**
     * Indique si le joueur est modérateur ou mieux
     * @return true si c'est le cas
     */
    public boolean isModerateur() {
        return (privilege >= Joueur.MODERATEUR_PRIVILEGE);
    }

    /**
     * Indique si le joueur est un joueur système (virtuel)
     * @return true si c'est le cas
     */
    public boolean isSystem() {
        return (getPseudo() != null) && (getPseudo().length() == 0);
    }

    /**
     * Chargement du joueur à partir d'un flux XML
     * Méthode synchronisée car les parsers XML ne sont pas thread safe
     * @param is InputStream dans lequel seront lues les données
     * @throws DataFormatException Si le format n'est pas correct
     */
    public synchronized void reloadJoueur(InputStream is) throws DataFormatException {
        
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
            throw new DataFormatException("Erreur technique");
        }
        catch (SAXException e) {
            System.err.println("Erreur technique (SAXException) à la création du parser XML : " + e);
            throw new DataFormatException("Erreur technique");
        }
        
        try {
            parser.parse(is, new JoueurHandler(this));
        }
        catch (IOException e) {
            System.err.println("Erreur technique (IOException) au decryptage du fichier XML : " + e);
            throw new DataFormatException("Erreur réseau");
            
        }
        catch (SAXException e) {
            System.err.println("Erreur technique (SAXException) au decryptage du fichier XML : " + e);
            throw new DataFormatException("Erreur format");
        }
    }

    /**
     * Classe pour decrypter le XML
     * @author Manu
     *
     */
    protected class JoueurHandler extends DefaultHandler {
 
        /**
         * Joueur à mettre à jour
         */       
        private Joueur myJoueur;
        
        /**
         * Constructeur
         * @param theJoueur Joueur à mettre à jour
         */
        public JoueurHandler(Joueur theJoueur) {
            myJoueur = theJoueur;
        }
        
        /**
         * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
         */
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            
            if (qName.equals("joueur")) {
                // Balise joueur : Alimentation des champs
                myJoueur.setPseudo(attributes.getValue("pseudo"));
                try {
                    myJoueur.setAvatarHautePerf(new URL(attributes.getValue("avatar")));
                }
                catch (MalformedURLException e) {
                    System.err.println("Mauvaise URL pour l'avatar : " + attributes.getValue("avatar"));
                    myJoueur.setAvatarHautePerf(null);
                }
                try {
                    if (attributes.getValue("avatarLow") != null) {
                        myJoueur.setAvatarFaiblePerf(new URL(attributes.getValue("avatarLow")));
                    }
                    else {
                        System.err.println("URL low pour l'avatar non fournie");
                        myJoueur.setAvatarFaiblePerf(new URL(attributes.getValue("avatar")));
                    }
                }
                catch (MalformedURLException e) {
                    System.err.println("Mauvaise URL pour l'avatar : " + attributes.getValue("avatarLow"));
                    myJoueur.setAvatarFaiblePerf(null);
                }
                if ((attributes.getValue("profil") != null) && (attributes.getValue("profil").equals("complet"))) {
                    myJoueur.setPrivilege(MAX_PRIVILEGE);
                }
                else if ((attributes.getValue("profil") != null) && (attributes.getValue("profil").equals("moderateur"))) {
                    myJoueur.setPrivilege(MODERATEUR_PRIVILEGE);
                }
                else {
                    myJoueur.setPrivilege(0);
                }
            }
            else {
                System.err.println("Tag " + qName + " non géré");
                throw new SAXNotRecognizedException("Tag " + qName + " non géré");
            }
        }

    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        return (compareTo(obj) == 0);
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o) {
        return pseudo.compareTo(((Joueur) o).getPseudo());
    }

    /**
     * @return le joueur system
     */
    public static synchronized Joueur getSystem() {

        if (system == null) {        
            system = new Joueur();        
            system.setPseudo("");
        }
        return system;
    }

}
