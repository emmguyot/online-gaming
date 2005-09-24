/*
 * Created on 26 avr. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.client;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import com.increg.game.client.belote.AnnonceBelote;
import com.increg.game.client.belote.EtatPartieBelote;
import com.increg.game.client.belote.PartieBelote;
import com.increg.game.client.belote.PartieBeloteClassique;
import com.increg.game.client.belote.PartieBeloteModerne;
import com.increg.game.net.ServerCall;
import com.increg.game.ui.AffichePlis;
import com.increg.game.ui.AfficheSalle;
import com.increg.game.ui.AfficheSalleBeloteClassique;
import com.increg.game.ui.AfficheSalleBeloteModerne;
import com.increg.game.ui.Configurator;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SalleModel {

    /**
     * Applet appelante
     */
    private AireMainModel aire;

    /**
     * Joueur connecté
     */
    private Joueur myJoueur;

    /**
     * Liste des joueurs de la partie
     */
    private Joueur[] lstJoueur;

    /**
     * Partie associée à la salle
     */
    private PartieBelote myPartie;

    /**
     * fenêtre principale de la vue
     */
    private JFrame fenetre;

    /**
     * Vue de la salle
     */
    private AfficheSalle view;

    /**
     * Une requète a été envoyée ?
     */
    private boolean requestSent;

    /**
     * Chat en attente d'affichage (quand la vue n'est pas encore créée)
     */
    private Vector lstPendingChat;

    /**
     * Constructeur
     * @param s Modèle de l'aire appelante
     * @param p Partie associée à la salle
     * @param j Joueur connecté
     */
    public SalleModel(AireMainModel s, PartieBelote p, Joueur j) {

        // Garde les attributs
        myJoueur = j;
        aire = s;
        myPartie = p;

        // Initialise les autres éléments
        lstJoueur = new Joueur[Joueur.NB_MAX_JOUEUR];
        lstPendingChat = new Vector();
        view = null;
        requestSent = false;
    }

    /**
     * Construit la vue
     *
     */
    public void buildView() {

        // Construit la fenêtre principale
        fenetre = new JFrame(myPartie.toString());
        fenetre.getContentPane().setLayout(new BorderLayout());
        Dimension tailleSalle = new Dimension(aire.getSkinConfig().getSTailleSalle().width, aire.getSkinConfig().getSTailleSalle().height);
        if (myPartie instanceof PartieBeloteClassique) {
            view = new AfficheSalleBeloteClassique(this, tailleSalle.width, tailleSalle.height);
        }
        else if (myPartie instanceof PartieBeloteModerne) {
            view = new AfficheSalleBeloteModerne(this, tailleSalle.width, tailleSalle.height);
        }
        else {
            System.err.println("Type de partie inconnu");
        }
        view.init();
        fenetre.getContentPane().add(view, BorderLayout.CENTER);
        view.setMinimumSize(tailleSalle);
        view.setMaximumSize(tailleSalle);
        view.setPreferredSize(tailleSalle);
        view.setSize(tailleSalle);
        fenetre.pack();
        fenetre.setLocationRelativeTo(null);
        fenetre.setResizable(false);

        fenetre.addWindowListener(view);
        fenetre.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        Iterator chatIter = lstPendingChat.iterator();
        while (chatIter.hasNext()) {
            Chat aChat = (Chat) chatIter.next();
            view.addPendingChat(aChat);
            chatIter.remove();
        }

        fenetre.setVisible(true);

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
        return aire.getLiaisonSrv();
    }

    /**
     * Retourne un joueur de la table (par rapport à son indice)
     * @param indice Numéro du joueur demandé
     * @return Joueur (Peut être null)
     */
    public Joueur getJoueur(int indice) {
        if ((indice < 0) || (indice >= lstJoueur.length)) {
            return null;
        }
        return lstJoueur[indice];
    }

    /**
     * @return Config complète
     */
    public Configurator getSkinConfig() {
        return aire.getSkinConfig();
    }

    /**
     * @return Partie associée à la salle
     */
    public PartieBelote getMyPartie() {
        return myPartie;
    }

    /**
     * @param belote Partie associée à la salle
     */
    public void setMyPartie(PartieBelote belote) {
        myPartie = belote;
    }

    /**
     * @return Aire de jeu
     */
    public AireMainModel getAire() {
        return aire;
    }

    /**
     * @return logger à utiliser
     */
    public Logger getLogger() {
        return aire.getLogger();
    }

    /**
     * @return Vue graphique
     */
    public AfficheSalle getView() {
        return view;
    }

    /* **************************************************************************** */
    /* Méthodes fonctionnelles                                                      */
    /* **************************************************************************** */

    /**
     * @param nomImage Image à charger
     * @return Image en cours de chargement
     * @throws MalformedURLException Si le nom n'est pas valide
     */
    public Image getImage(String nomImage) throws MalformedURLException {
        return getLiaisonSrv().getImage(nomImage);
    }

    /**
     *
     * @param nomImage Image à charger (en chemin relatif)
     * @return Image en cours de chargement
     * @throws MalformedURLException Si le nom n'est pas valide
     */
    public Image getRelativeImage(String nomImage) throws MalformedURLException {
        return getLiaisonSrv().getRelativeImage(nomImage);
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
     * Met la vue en avant plan
     */
    public void setFocus() {
        view.transferFocus();
    }

    /**
     * @return Indique si le joueur connecté a un role actif dans la partie
     */
    public boolean isJoueurActif() {
        return (myPartie.getPositionJoueur(myJoueur) >= 0);
    }

    /**
     * Fermeture de la salle !
     */
    public void quitte() {

        // Averti l'aire : La fermeture se fera au retour
        aire.fermetureSalle(this);

    }

    /**
     * Supprime la fenêtre pour une suppression de la sale
     */
    public void suppressionSalle() {
        if (view != null) {
            fenetre.removeWindowListener(view);
            view.removeAll();
            view = null;
        }
        fenetre.removeAll();
        fenetre.dispose();
        fenetre = null;
    }

    /**
     * Ajoute un chat à la partie
     * @param aChat chat à afficher
     */
    public void addPendingChat(Chat aChat) {
        if (view != null) {
            view.addPendingChat(aChat);
        }
        else {
            lstPendingChat.add(aChat);
        }
    }

    /**
     * Le joueur vient de couper : Informe le serveur et enchaine...
     * @param pos Position de la coupe
     */
    public void setCoupe(int pos) {
        // Averti le serveur
        try {
            if (!requestSent) {

                // Fait l'action en avance de phase, mais vérifie l'état avant
                if (myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_COUPE_JEU) {
                    myPartie.impacteCoupe(pos);
                }

                view.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                getLiaisonSrv().load("doAction.srv?Action=coupe&Pseudo=" + URLEncoder.encode(myJoueur.getPseudo(), "UTF8")
                                + "&idPartie=" + myPartie.getIdentifiant() + "&pos=" + Integer.toString(pos), aire);
                refresh();

                // Flag qu'une requête est partie et qu'il faut attendre le retour avant de faire autre-chose
                requestSent = true;
            }
        }
        catch (UnsupportedEncodingException e) {
            System.err.println("Problème d'encodage de l'URL");
        }
    }

    /**
     * Le joueur vient de choisir combien de carte il distribue en premier
     * @param nbPremier Nombre de carte en premier
     */
    public void setDistribution(int nbPremier) {
        // Averti le serveur
        try {
            if (!requestSent) {

                // IMPOSSIBLE de Faire l'action en avance de phase, car le jeu complet n'est pas connu

                view.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                getLiaisonSrv().load("doAction.srv?Action=distrib&Pseudo=" + URLEncoder.encode(myJoueur.getPseudo(), "UTF8")
                                + "&idPartie=" + myPartie.getIdentifiant() + "&nb=" + Integer.toString(nbPremier), aire);
                // Flag qu'une requête est partie et qu'il faut attendre le retour avant de faire autre-chose
                requestSent = true;
            }
        }
        catch (UnsupportedEncodingException e) {
            System.err.println("Problème d'encodage de l'URL");
        }
    }

    /**
     * Le joueur ne veut pas prendre
     */
    public void setNonDecision() {
        // Ne joueur n'en veut pas
        try {
            if (!requestSent) {

                // Fait l'action en avance de phase, mais vérifie l'état avant
                if ((myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_TOUR_ENCHERE1)
                        || (myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_TOUR_ENCHERE1_PRIS)
                        || (myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_TOUR_ENCHERE2)
                        || (myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_TOUR_ENCHERE2_PRIS)) {
                    myPartie.impactePrise(-1);
                }

                view.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                getLiaisonSrv().load("doAction.srv?Action=decision&Pseudo=" + URLEncoder.encode(myJoueur.getPseudo(), "UTF8")
                                + "&idPartie=" + myPartie.getIdentifiant(), aire);
                refresh();

                // Flag qu'une requête est partie et qu'il faut attendre le retour avant de faire autre-chose
                requestSent = true;
            }
        }
        catch (UnsupportedEncodingException e) {
            System.err.println("Problème d'encodage de l'URL");
        }
    }

    /**
     * Le joueur prend
     * @param numCouleur Couleur choisie
     */
    public void setDecision(int numCouleur) {
        // Le joueur n'en veut pas
        try {
            if (!requestSent) {

                // IMPOSSIBLE de faire l'action en avance de phase, le jeu complet n'est pas connu

                view.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                getLiaisonSrv().load("doAction.srv?Action=decision&Pseudo=" + URLEncoder.encode(myJoueur.getPseudo(), "UTF8")
                            + "&idPartie=" + myPartie.getIdentifiant() + "&couleur=" + numCouleur, aire);
                // Flag qu'une requête est partie et qu'il faut attendre le retour avant de faire autre-chose
                requestSent = true;
            }
        }
        catch (UnsupportedEncodingException e) {
            System.err.println("Problème d'encodage de l'URL");
        }
    }

    /**
     * Une carte vient d'être jouée
     * @param carte carte jouée
     */
    public void setCarteJouee(Couleur carte) {

        // Log Optimisation Vitesse
        if (aire.getLogger().isLoggable(Level.FINEST)) {
        	aire.getLogger().finest(System.currentTimeMillis() + " : CarteJouee Début");
        }
        // Vérification de la validité de la carte
        if (myPartie.peutJouer(carte)) {
            try {
                if (!requestSent) {
                    // =========== Similaire à DoAction.doJoueCarte ===============
                    if (myPartie.getParticipant(myPartie.getEtat().getJoueur()).equals(myJoueur)) {
                        // Log Optimisation Vitesse
                        if (aire.getLogger().isLoggable(Level.FINEST)) {
                        	aire.getLogger().finest(System.currentTimeMillis() + " : Action to be queued");
                        }

                        // Exécute l'action directement pour gagner en réactivité
                        if (myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_FIN_TOUR) {
                            // Ramasse d'abord
                            myPartie.impacteRamasse();
                        }
                        if (myPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_JOUE_CARTE) {
                            Couleur aCarte = myPartie.buildCarte(carte.getHauteur(), carte.getCouleur());
                            myPartie.impacteJoueCarte(aCarte);
                        }

                        view.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        getLiaisonSrv().load("doAction.srv?Action=joueCarte&Pseudo=" + URLEncoder.encode(myJoueur.getPseudo(), "UTF8")
                                        + "&idPartie=" + myPartie.getIdentifiant() + "&c=" + carte.getCouleur() + "&h=" + carte.getHauteur(), aire);
                        refresh();
                        // Flag qu'une requête est partie et qu'il faut attendre le retour avant de faire autre-chose
                        requestSent = true;
                    }
                }
            }
            catch (UnsupportedEncodingException e) {
                aire.getLogger().severe("Problème d'encodage de l'URL");
            }


        }
        else {
            JOptionPane.showMessageDialog(view, "Vous ne pouvez pas jouer cette carte", "Jeu d'une carte", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Ramasse le jeu
     * @param debugRamasse indicateur de la source du ramassage
     */
    public void setRamasseJeu(int debugRamasse) {
        try {
            if (!requestSent) {
                view.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                getLiaisonSrv().load("doAction.srv?Action=ramasseJeu&dR=" + debugRamasse + "&Pseudo=" + URLEncoder.encode(myJoueur.getPseudo(), "UTF8")
                                + "&idPartie=" + myPartie.getIdentifiant(), aire);
                // Flag qu'une requête est partie et qu'il faut attendre le retour avant de faire autre-chose
                requestSent = true;
            }
            else {
                System.err.println("Ramassage ignoré");
            }
        }
        catch (UnsupportedEncodingException e) {
            System.err.println("Problème d'encodage de l'URL");
        }

    }

    /**
     * Passe à la donne suivante
     */
    public void nextDonne() {
        try {
            if (!requestSent) {
                view.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                getLiaisonSrv().load("doAction.srv?Action=scoreVu&Pseudo=" + URLEncoder.encode(myJoueur.getPseudo(), "UTF8")
                                + "&idPartie=" + myPartie.getIdentifiant(), aire);
                // Flag qu'une requête est partie et qu'il faut attendre le retour avant de faire autre-chose
                requestSent = true;
            }
        }
        catch (UnsupportedEncodingException e) {
            System.err.println("Problème d'encodage de l'URL");
        }

    }

    /**
     * Affiche le dernier plis ramassé
     */
    public void affichePlis() {
        AffichePlis plis = new AffichePlis(fenetre, "Dernier pli", myPartie.getJeu().getPlis()[myPartie.getDernierRamasse() % 2].subList(0, PartieBelote.NB_JOUEUR), this);
        // Décalle pour voir le jeu
        plis.setLocationRelativeTo(fenetre);
        plis.setLocation(plis.getX() + plis.getWidth(), plis.getY() + plis.getHeight());
        plis.setVisible(true);
   }

    /**
     * Le joueur indique s'il veut rejouer
     * @param b le joueur veut rejouer ?
     */
    public void replay(boolean b) {
        try {
            if (!requestSent) {
                view.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                getLiaisonSrv().load("doAction.srv?Action=replay&Pseudo=" + URLEncoder.encode(myJoueur.getPseudo(), "UTF8")
                                + "&idPartie=" + myPartie.getIdentifiant() + "&want=" + b, aire);
                // Flag qu'une requête est partie et qu'il faut attendre le retour avant de faire autre-chose
                requestSent = true;
            }
        }
        catch (UnsupportedEncodingException e) {
            System.err.println("Problème d'encodage de l'URL");
        }
    }
    /**
     * Met à joue la vue
     */
    public void refresh() {

        // Reset le lock sur la requete en cours
        requestSent = false;

        // Calcule l'offset
        int offset = myPartie.getPositionJoueur(myJoueur);
        if (offset < 0) {
            offset = 0;
        }

        synchronized (myPartie) {
            /**
             * L'état de la partie a été mis à jour au rechargement
             */

            /**
             * Affichage
             */
            if (view != null) {
                view.refresh(offset);

                // Affiche les annonces si besoin
                int nbAnnonce = 0;
                for (int j = 0; j < PartieBelote.NB_JOUEUR; j++) {
                    for (int iAnnonce = 0; iAnnonce < myPartie.getJeu().getAnnonces()[j].size(); iAnnonce++) {
                        AnnonceBelote anAnnonce = (AnnonceBelote) myPartie.getJeu().getAnnonces()[j].get(iAnnonce);
                        if (!anAnnonce.isAnnonceFaite()) {
                            List listCartes = new ArrayList();

                            for (int iCarte = 0; iCarte < anAnnonce.getCartes().length; iCarte++) {
                                listCartes.add(anAnnonce.getCartes()[iCarte]);
                            }
                            anAnnonce.setAnnonceFaite(true);
                            AffichePlis plis = new AffichePlis(fenetre, "Annonce de " + myPartie.getParticipant()[j].getPseudo(),
                                            listCartes, this);
                            plis.setLocationRelativeTo(fenetre);
                            // Décalle la fenêtre
                            switch (nbAnnonce) {

                            case 0 :
                                plis.setLocation(plis.getX() + plis.getWidth(), plis.getY());
                                break;

                            case 1 :
                                plis.setLocation(plis.getX() + plis.getWidth(), plis.getY() + plis.getHeight());
                                break;

                            case 2 :
                                plis.setLocation(plis.getX(), plis.getY() + plis.getHeight());
                                break;

                            case 3 :
                                plis.setLocation(plis.getX() - plis.getWidth(), plis.getY() + plis.getHeight());
                                break;

                            case 4 :
                                plis.setLocation(plis.getX() - plis.getWidth(), plis.getY());
                                break;

                            default :
                                // Au cas ou...
                                plis.setLocation(plis.getX() - plis.getWidth(), plis.getY() - plis.getHeight());
                                break;
                            }
                            plis.setVisible(true);
                            nbAnnonce++;
                        }
                    }
                }
            }
            else {
                // Problème
                // Simule une sortie de table pour revenir normalement
                quitte();
            }
        }
    }

}
