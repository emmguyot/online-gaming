/*
 * 
 * Copyright (C) 2003-2005 Emmanuel Guyot <See emmguyot on SourceForge> 
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation; either 
 * version 2 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; 
 * if not, write to the 
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 */
package com.increg.game.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Vector;
import java.util.zip.DataFormatException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.increg.util.HTMLencoder;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Chat {
    
    /**
     * Smiley disponibles (init par le decoder)
     * Chaque élément du vecteur est un Smiley
     */
    protected static Vector smiley;
    
    /**
     * Sons disponibles (init par le decoder)
     * Chaque élément du vecteur est un SmileySon
     */
    protected static Vector son;
    
    /**
     * Texte du chat
     */
    protected String text;
    
    /**
     * Joueur qui a écrit
     */
    protected Joueur joueurOrig;
    
    /**
     * Joueur destinataire (dans le cas de message privé)
     */
    protected Joueur joueurDest;
    
    /**
     * Partie où ce message a été écrit
     */
    protected Partie partie;
    
    /**
     * Style du chat (Couleur, graisse, ...) au format HTML
     */
    protected String style;
    
    /**
     * Constructeur
     */
    public Chat() {
        text = "";
        style = "";
    }

    /**
     * @return Texte du chat
     */
    public String getText() {
        return text;
    }

    /**
     * @param string Texte du chat
     */
    public void setText(String string) {
        text = string;
    }

    /**
     * @return  Joueur destinataire (dans le cas de message privé)
     */
    public Joueur getJoueurDest() {
        return joueurDest;
    }

    /**
     * @return qui a écrit
     */
    public Joueur getJoueurOrig() {
        return joueurOrig;
    }

    /**
     * @return Partie où ce message a été écrit
     */
    public Partie getPartie() {
        return partie;
    }

    /**
     * @param joueur Joueur destinataire (dans le cas de message privé)
     */
    public void setJoueurDest(Joueur joueur) {
        joueurDest = joueur;
    }

    /**
     * @param joueur qui a écrit
     */
    public void setJoueurOrig(Joueur joueur) {
        joueurOrig = joueur;
    }

    /**
     * @param partie Partie où ce message a été écrit
     */
    public void setPartie(Partie partie) {
        this.partie = partie;
    }

    /**
     * @return Style du chat (Couleur, graisse, ...) au format HTML
     */
    public String getStyle() {
        return style;
    }

    /**
     * @param debut début ou fin du style
     * @return Style du chat (Couleur, graisse, ...) au format HTML
     */
    public String getStyle(boolean debut) {
        if (debut) {
            return getStyle();
        }
        else {
            // Analyse pour fermer les balises
            String antiStyle = "";
            StringBuffer balise = new StringBuffer();
            boolean inBalise = false;
            for (int i = 0; i < style.length(); i++) {
                if (style.charAt(i) == '<') {
                    inBalise = true;
                }
                else if ((style.charAt(i) == '>') || (style.charAt(i) == ' ')) {
                    if (inBalise) {
                        antiStyle = "</" + balise + ">" + antiStyle;
                        balise.setLength(0);
                    }
                    inBalise = false;
                }
                else if (inBalise) {
                    balise = balise.append(style.charAt(i));
                }
            }
            if (inBalise) {
                antiStyle = "</" + balise + ">" + antiStyle;
            }
            return antiStyle;
        }
    }

    /**
     * @param string Style du chat (Couleur, graisse, ...) au format HTML
     */
    public void setStyle(String string) {
        style = string;
    }

    /**
     * @return ensemble des Smiley possible
     */
    public static Vector getSmiley() {
        return smiley;
    }

    /**
     * @return ensemble des sons possibles
     */
    public static Vector getSon() {
        return son;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        
        // Formate le texte du chat
        StringBuffer msg = new StringBuffer(HTMLencoder.htmlEncode(getText()));
        
        // On n'utilise pas les regexp car il faudrait excaper les smileys.
        // De plus, en terme de perf la methode suivante doit être mieux
        for (int i = 0; i < smiley.size(); i++) {
            Smiley aSmiley = (Smiley) smiley.get(i);

            int pos = msg.indexOf(aSmiley.getCode());
            while (pos >= 0) {
                msg = msg.replace(pos, 
                                  pos + (aSmiley.getCode()).length(), 
                                  "<img src=\"" + aSmiley.getImage() + "\">");
                pos = msg.indexOf(aSmiley.getCode(), pos);
            }
        }

        // Ajoute l'origine
        // Constitue la chaine complète à ajouter
        StringBuffer msgTot = new StringBuffer();
        if ((getJoueurOrig() != null) && (getJoueurOrig().getPseudo().length() > 0)) {
            msgTot = msgTot.append("<i><b>");
            if (getJoueurOrig().getPrivilege() >= Joueur.MODERATEUR_PRIVILEGE) {
                // Le pseudo est affiché en 
                msgTot.append("<font color=\"#0000FF\">").append(getJoueurOrig().getPseudo()).append("</font>");
            }
            else {
                msgTot.append(getJoueurOrig().getPseudo());
            }
            msgTot.append("</b></i>");
            if ((getJoueurDest() != null) && (getJoueurDest().getPseudo().length() > 0)) {
                msgTot.append(" dit en secret à ").append(getJoueurDest().getPseudo());
                
            }
            msgTot.append(" : ");
        }
        msgTot = msgTot.append(getStyle(true)); 
        msgTot = msgTot.append(msg.toString());
        msgTot = msgTot.append(getStyle(false));
        
        return msgTot.toString();
    }

    /**
     * Retourne le dernier son de ce chat
     * Dernier car impossible de jouer plusieurs sons en même temps sans faire cacophonie
     * @return Sons à jouer, null si aucun
     */
    public String getSound() {

        StringBuffer msg = new StringBuffer(HTMLencoder.htmlEncode(getText()));
        String dernierSon = null; 
        
        // On n'utilise pas les regexp car il faudrait excaper les smileys.
        // De plus, en terme de perf la methode suivante doit être mieux
        for (int i = 0; i < son.size(); i++) {
            SmileySon aSmiley = (SmileySon) son.get(i);

            int pos = msg.indexOf(aSmiley.getCode());
            while (pos >= 0) {
                // Un son de trouvé
                dernierSon = aSmiley.getSon();
                pos = msg.indexOf(aSmiley.getCode(), pos + 1);
            }
        }
        
        return dernierSon;
    }
    
    /**
     * Décode la configuration XML
     * @param configXml Contenu XML du fichier de config
     * @throws DataFormatException Si le format n'est pas correct
     */
    public static synchronized void decodeSmiley(String configXml) throws DataFormatException {
        
        // Initialisations pour la lecture XML
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        
        parserFactory.setValidating(true);
        parserFactory.setNamespaceAware(true);
        
        SAXParser parser = null;
        try {
            parser = parserFactory.newSAXParser();
        }
        catch (ParserConfigurationException e) {
            System.err.println("DecodeSmiley::Erreur technique (ParserConfigurationException) à la création du parser XML : " + e);
            e.printStackTrace();
            throw new DataFormatException("Erreur technique");
        }
        catch (SAXException e) {
            System.err.println("DecodeSmiley::Erreur technique (SAXException) à la création du parser XML : " + e);
            e.printStackTrace();
            throw new DataFormatException("Erreur technique");
        }
        
        try {
            InputStream is = new ByteArrayInputStream(configXml.getBytes());
            Chat dummyChat = new Chat();
            SmileyDecoder decoder = dummyChat.new SmileyDecoder();
            parser.parse(is, decoder);
        }
        catch (IOException e) {
            System.err.println("DecodeSmiley::Erreur technique (IOException) au decryptage du fichier XML : " + e);
            e.printStackTrace();
            throw new DataFormatException("Erreur technique");
            
        }
        catch (SAXException e) {
            System.err.println("DecodeSmiley::Erreur technique (SAXException) au decryptage du fichier XML : " + e);
            e.printStackTrace();
            throw new DataFormatException("Erreur format");
        }
    }

    /**
     * Décode la configuration XML
     * @param configXml Contenu XML du fichier de config
     * @throws DataFormatException Si le format n'est pas correct
     */
    public static synchronized void decodeSon(String configXml) throws DataFormatException {
        
        // Initialisations pour la lecture XML
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        
        parserFactory.setValidating(true);
        parserFactory.setNamespaceAware(true);
        
        SAXParser parser = null;
        try {
            parser = parserFactory.newSAXParser();
        }
        catch (ParserConfigurationException e) {
            System.err.println("DecodeSon::Erreur technique (ParserConfigurationException) à la création du parser XML : " + e);
            e.printStackTrace();
            throw new DataFormatException("Erreur technique");
        }
        catch (SAXException e) {
            System.err.println("DecodeSon::Erreur technique (SAXException) à la création du parser XML : " + e);
            e.printStackTrace();
            throw new DataFormatException("Erreur technique");
        }
        
        try {
            InputStream is = new ByteArrayInputStream(configXml.getBytes());
            Chat dummyChat = new Chat();
            SonDecoder decoder = dummyChat.new SonDecoder();
            parser.parse(is, decoder);
        }
        catch (IOException e) {
            System.err.println("DecodeSon::Erreur technique (IOException) au decryptage du fichier XML : " + e);
            e.printStackTrace();
            throw new DataFormatException("Erreur technique");
            
        }
        catch (SAXException e) {
            System.err.println("DecodeSon::Erreur technique (SAXException) au decryptage du fichier XML : " + e);
            e.printStackTrace();
            throw new DataFormatException("Erreur format");
        }
    }

    /* ***************************************
     * 
     * *************************************** */
    /**
     * Classe interprétant le flux XML
     */
    public class SmileyDecoder extends DefaultHandler {

        /**
         * Constructeur
         *
         */
        public SmileyDecoder() {        
        }

        /**
         * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
         */
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

            try {
                if (qName.equals("smiley")) {
                    String code = attributes.getValue("code"); 
                    String img = attributes.getValue("img"); 
                    String imgLow = attributes.getValue("imgLow"); 
                    
                    URL theURL = null;
        
                    if (img.charAt(0) == '/') {
                        theURL = this.getClass().getResource(img);
                        if (theURL != null) {
                            img = theURL.toString();
                        }
                    } 
                    
                    if (imgLow.charAt(0) == '/') {
                        theURL = this.getClass().getResource(imgLow);
                        if (theURL != null) {
                            imgLow = theURL.toString();
                        }
                    } 

                    smiley.add(new Smiley(code, img, imgLow));
                }
            }
            catch (NullPointerException e) {
                System.err.println("Erreur de paramétrage :");
                e.printStackTrace();
                throw(e);
            }
        }
        
        /**
         * @see org.xml.sax.ContentHandler#startDocument()
         */
        public void startDocument() throws SAXException {

            smiley = new Vector();
            
            super.startDocument();
        }

    }

    /* ***************************************
     * 
     * *************************************** */
    /**
     * Classe interprétant le flux XML
     */
    public class SonDecoder extends DefaultHandler {

        /**
         * Constructeur
         *
         */
        public SonDecoder() {        
        }

        /**
         * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
         */
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

            try {
                if (qName.equals("son")) {
                    String code = attributes.getValue("code"); 
                    String img = attributes.getValue("img"); 
                    String url = attributes.getValue("url"); 
                    
                    URL theURL = null;
                    if (img.charAt(0) == '/') {
                        theURL = this.getClass().getResource(img);
                        if (theURL != null) {
                            img = theURL.toString();
                        }
                    } 
                    
                    theURL = null;
                    if (url.charAt(0) == '/') {
                        theURL = String.class.getResource(url);
                        if (theURL != null) {
                            url = theURL.toString();
                        }
                    } 
                    
                    son.add(new SmileySon(code, img, url));
                }
            }
            catch (NullPointerException e) {
                System.err.println("Erreur de paramétrage :");
                e.printStackTrace();
                throw(e);
            }
        }
        
        /**
         * @see org.xml.sax.ContentHandler#startDocument()
         */
        public void startDocument() throws SAXException {

            son = new Vector();
            
            super.startDocument();
        }

    }
}
