/*
 * Created on 7 avr. 2003
 *
 * Applet principale de l'aire de jeu
 */
package com.increg.game.ui;
import java.applet.Applet;
import java.awt.Graphics;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.SwingUtilities;

import com.increg.game.client.AireMainModel;
import com.increg.game.client.Joueur;

/**
 * @author Manu
 *
 * Applet principale représentant l'aire de jeu, 
 * ce qui ouvre l'accès à la salle de jeu d'une partie
 **/
public class AireMain extends Applet {

    /**
     * Logger global
     */
    protected Logger logger;
    
    /**
     * Handler pour le log
     */
    protected FileHandler fh;

    /**
     * Aire
     */
    protected AireMainModel model;
    
    
    /**
     * @return Information sur l'applet
     */
    public String getAppletInfo() {
        return "Aire de jeu de Belote - Développée par InCrEG sarl (2003)";
    }

    /**
     * @return les informations sur les paramètres possibles
     */
    public String[][] getParameterInfo() {
        String[][] info = {
            // Parameter Name     Kind of Value   Description
            {"pseudo",     "String",          "Nom du joueur"},
            {"avatar",        "URL",          "Icône de l'avatar"},
            {"config",        "URL",          "Fichier de configuration"},
            {"couleur",    "String",          "Couleur d'écriture"},
            {"sessionId",  "String",          "Session J2EE"}
        };
        return info;
    }    

    /**
     * @see java.awt.Component#update(java.awt.Graphics)
     */
    public void update(Graphics g) {
        // Assure que toute la zone est repainte
        paint(g);
    }

    /**
     * @see java.applet.Applet#init()
     */
    public void init() {
        super.init();
        
        logger = Logger.getLogger("com.increg");
        try {
            // Fait le ménage avant toute autre ajout
            Handler[] tabHandler = logger.getHandlers();
            for (int i = 0; i < tabHandler.length; i++)  {
                if (tabHandler[i] instanceof FileHandler) {
                    FileHandler fh2 = (FileHandler) tabHandler[i];
                    fh2.close();
                    logger.removeHandler(fh2);
                    System.out.println("Fermeture FileHandler");
                }
                else {
                    System.out.println("Handler = " + tabHandler[i].getClass().getName());
                }
            }
            // Ajout notre traitement des log
            fh = new FileHandler("%h/InCrEG_Game.log", 5000000, 1, true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            logger.setLevel(Level.ALL);
            if (System.getProperty("LogDebug") != null) {
                logger.setLevel(Level.parse(System.getProperty("LogDebug")));
            }
            if (System.getProperty("NoLogDebug") != null) {
                logger.setLevel(Level.INFO);
            }
        }
        catch (SecurityException e1) {
            e1.printStackTrace();
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }

        /**
         * Récupération des paramètres
         */
        String pseudo = getParameter("pseudo");
        URL avatar = null;
        try {
            avatar = new URL(getParameter("avatar"));
        }
        catch (MalformedURLException e) {
            // Ignore l'erreur
        }
        URL avatarLow = null;
        try {
           avatarLow = new URL(getParameter("avatarLow"));
        }
        catch (MalformedURLException e) {
           // Ignore l'erreur
        }
        String couleur = getParameter("couleur");
        
        // Création du joueur
        Joueur me = new Joueur();
        me.setPseudo(pseudo);
        me.setAvatarHautePerf(avatar);
        me.setAvatarFaiblePerf(avatarLow);
        me.setCouleur(couleur);
        
        // Création du modèle de salle
        model = new AireMainModel(this, me);
        
        setVisible(true);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                model.init();
            }
            });
    }

    /**
     * @see java.applet.Applet#start()
     */
    public void start() {
        super.start();
    }

    /**
     * @see java.applet.Applet#stop()
     */
    public void stop() {
        super.stop();
    }

    /**
     * Ouvre une nouvelle fenêtre à partir de l'url passée (relative)
     * @param url url relative à ouvrir
     */
    public void openDocument(String url) {
        try {
            getAppletContext().showDocument(new URL(getDocBase() + "/" + url), "_blank");
        }
        catch (MalformedURLException e) {
            logger.severe("openDocument::URL invalide : >" + url + "<");
        }
    }
    
    /**
     * Retourne le répertoire de base de l'aire de jeu
     * @return chaîne correspondante
     */
    public String getDocBase() {
        /**
         * Contourne une erreur de documentation de Java (au moins)
         */        
        String docBase = getDocumentBase().toString();
        if (docBase.endsWith(".srv") 
                || docBase.endsWith(".jsp") 
                || (docBase.indexOf("jsessionid") != -1)) {
            docBase = docBase.substring(0, docBase.lastIndexOf('/'));
        }

        return docBase;
    }

    /**
     * @return Logger InCrEG
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * @param logger Logger InCrEG
     */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     * @see java.applet.Applet#destroy()
     */
    public void destroy() {
        super.destroy();
        model.dropAll();
        fh.close();
        logger.removeHandler(fh);
    }

    /**
     * @see java.awt.Component#paint(java.awt.Graphics)
     */
    public void paint(Graphics g) {
        super.paint(g);
        if ((model == null) || (model.getEtat() != AireMainModel.ETAT_OK)) {
            g.drawString("Chargement en cours, veuillez patienter ...", 20, 130);
            g.drawString("http://online-gaming.sourceforge.net", 20, 410);
            g.drawString("Logiciel sous licence GPL (http://www.opensource.org)", 20, 430);
            g.drawString("Copyright 2003-2004 Valérie Guyot et Emmanuel Guyot", 20, 450);
            g.drawString("Ce logiciel n'offre ABSOLUMENT AUCUNE GARANTIE;", 20, 490);
            g.drawString("Ce logiciel est gratuit et nous vous encourageons à le redistribuer selon les termes de la licence GPL", 20, 510);
        }
    }

}
