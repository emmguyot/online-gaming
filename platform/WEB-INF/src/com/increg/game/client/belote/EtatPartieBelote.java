/*
 * Created on 20 mai 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.client.belote;

import com.increg.game.client.EtatPartie;

/**
 * @author Manu
 *
 * Etats d'une partie de belote
 */
public class EtatPartieBelote extends EtatPartie {

    /**
     * Nombre de carte jouée dans la partie
     */
    private int nbCarteJouee;
    
    /* ***************************************
     * Etats de la belote
     * *************************************** */
    /**
     * Partie non démarrée : Attente de tous les joueurs
     */
    public static final int ETAT_NON_DEMARRE = 0;
    /**
     * Il faut couper le jeu pour un joueur unique
     */
    public static final int ETAT_COUPE_JEU = ETAT_NON_DEMARRE + 1;
    /**
     * Il faut distribuer le jeu pour un joueur unique 
     */
    public static final int ETAT_DISTRIBUTION = ETAT_COUPE_JEU + 1;
    /**
     * Premier tour d'enchère pour un joueur
     */
    public static final int ETAT_TOUR_ENCHERE1 = ETAT_DISTRIBUTION + 1;
    /**
     * 2nd tour d'enchère pour un joueur
     */
    public static final int ETAT_TOUR_ENCHERE2 = ETAT_TOUR_ENCHERE1 + 1;
    /**
     * 1er tour d'enchère pour un joueur, mais qq à pris
     */
    public static final int ETAT_TOUR_ENCHERE1_PRIS = ETAT_TOUR_ENCHERE2 + 1;
    /**
     * 2nd tour d'enchère pour un joueur mais qq à pris
     */
    public static final int ETAT_TOUR_ENCHERE2_PRIS = ETAT_TOUR_ENCHERE1_PRIS + 1;
    /**
     * Jeu de la carte par un joueur
     */
    public static final int ETAT_JOUE_CARTE = ETAT_TOUR_ENCHERE2_PRIS + 1;
    /**
     * Fin du tour global
     */
    public static final int ETAT_FIN_TOUR = ETAT_JOUE_CARTE + 1;
    /**
     * Fin de la donne 
     */
    public static final int ETAT_FIN_DONNE = ETAT_FIN_TOUR + 1;
    /**
     * Fin de la partie global
     */
    public static final int ETAT_FIN_PARTIE = ETAT_FIN_DONNE + 1;
    
    /* ***************************************
     * Actions possibles
     * *************************************** */
    
    /**
     * Initialisation de la partie
     */
    public static final int ACTION_INITIALISE = 0;
    /**
     * Un joueur a coupé
     */
    public static final int ACTION_COUPE = ACTION_INITIALISE + 1;
    /**
     * Un joueur a distribué
     */
    public static final int ACTION_DISTRIBUE = ACTION_COUPE + 1;
    /**
     * Un joueur a pris mais surchère possible 
     */
    public static final int ACTION_PREND_SURCHERE_POSSIBLE = ACTION_DISTRIBUE + 1;
    /**
     * Un joueur a pris mais sans surchère possible 
     */
    public static final int ACTION_PREND = ACTION_PREND_SURCHERE_POSSIBLE + 1;
    /**
     * Un joueur a passé 
     */
    public static final int ACTION_PASSE = ACTION_PREND + 1;
    /**
     * Un joueur a joué une carte 
     */
    public static final int ACTION_JOUE = ACTION_PASSE + 1;
    /**
     * Un joueur a ramassé le plis 
     */
    public static final int ACTION_RAMASSE = ACTION_JOUE + 1;
    /**
     * Un joueur / Equipe gagne  
     */
    public static final int ACTION_GAGNE = ACTION_RAMASSE + 1;
    /**
     * Un joueur se lève  
     */
    public static final int ACTION_QUITTE = ACTION_GAGNE + 1;

    /**
     * Constructeur
     *
     */
    public EtatPartieBelote() {
        
        etat = ETAT_NON_DEMARRE;
        // Joueur au hazard
        joueur = (int) (Math.random() * PartieBelote.NB_JOUEUR);
        joueurCoupe = joueur;
        joueurOuvre = joueur;
        joueurPreneur = -1;
        nbCarteJouee = 0; 
        started = false;
    }

    /**
     * @param joueur Joueur lié à cet état. Peut-être nul si l'état est global
     */
    public void setJoueur(int joueur) {
        this.joueur = joueur % PartieBelote.NB_JOUEUR;
    }

    /**
     * @see com.increg.game.bean.EtatPartie#etatSuivant(int, Joueur)
     */
    public int etatSuivant(int action, int aJoueur) {

        // Mini automate à état fini
        switch (action) {
            case ACTION_INITIALISE : {
                started = true;
                etat = ETAT_COUPE_JEU;
                joueur = aJoueur;
                joueurCoupe = joueur;
                joueurPreneur = -1;
                break;
            }

            case ACTION_COUPE : {
                int nextJoueur = (joueur + 1) % PartieBelote.NB_JOUEUR;
                joueurCoupe = aJoueur; // Sauvegarde qui a coupé
                joueur = nextJoueur;
                etat = ETAT_DISTRIBUTION;
                nbCarteJouee = 0;
                joueurPreneur = -1;
                break;
            }

            case ACTION_DISTRIBUE : {
                int nextJoueur = (joueur + 1) % PartieBelote.NB_JOUEUR;
                joueur = nextJoueur;
                joueurOuvre = nextJoueur;
                etat = ETAT_TOUR_ENCHERE1;                 
                break;
            }
                
            case ACTION_PREND_SURCHERE_POSSIBLE : {
                int nextJoueur = (joueur + 1) % PartieBelote.NB_JOUEUR;
                joueur = nextJoueur;
                switch (etat) {
                    case ETAT_TOUR_ENCHERE1: {
                        etat = ETAT_TOUR_ENCHERE2_PRIS;
                        break;
                    }
                    case ETAT_TOUR_ENCHERE1_PRIS: {
                        etat = ETAT_TOUR_ENCHERE2_PRIS;
                        break;
                    }
                    case ETAT_TOUR_ENCHERE2_PRIS: {
                        // Rien à faire
                        break;
                    }
                    case ETAT_TOUR_ENCHERE2: {
                        etat = ETAT_TOUR_ENCHERE2_PRIS;
                        break;
                    }
                    default :
                        System.err.println("etatSuivant : Transition inconnue action " + action + " etat " + etat);
                        break;
                }
                joueurPreneur = aJoueur;
                break;
            }
                
            case ACTION_PASSE : {
                int nextJoueur = (joueur + 1) % PartieBelote.NB_JOUEUR;
                joueur = nextJoueur;
                if ((nextJoueur == joueurOuvre) 
                    || (nextJoueur == joueurPreneur)) {
                    // Fin du tour
                    switch (etat) {
                        case ETAT_TOUR_ENCHERE1: {
                            etat = ETAT_TOUR_ENCHERE2;
                            break;
                        }
                        case ETAT_TOUR_ENCHERE1_PRIS: {
                            etat = ETAT_TOUR_ENCHERE2_PRIS;
                            if ((nextJoueur == joueurOuvre) 
                                && (nextJoueur == joueurPreneur)) {
                                // Cas particulier de l'ouvreur qui prend : Pas de deuxième tour
                                // Continue en séquence pour jouer
                            }
                            else {
                                break;
                            }
                        }
                        case ETAT_TOUR_ENCHERE2_PRIS: {
                            // !!! Séquence depuis le cas précédent
                            // On joue : Seulement si tout le monde a eu la possibilité de surenchérire
                            if (nextJoueur == joueurPreneur) {
                                etat = ETAT_JOUE_CARTE; 
                                joueur = (joueurCoupe + 2) % PartieBelote.NB_JOUEUR;
                            }
                            // Sinon rien à faire...
                            break;
                        }
                        case ETAT_TOUR_ENCHERE2: {
                            // Personne à pris
                            etat = ETAT_COUPE_JEU;
                            joueur = (joueurCoupe + 1) % PartieBelote.NB_JOUEUR;
                            break;
                        }
                        default :
                            System.err.println("etatSuivant : Transition inconnue action " + action + " etat " + etat);
                            break;
                    }
                }
                
                break;
            }
                
            case ACTION_PREND : {
                // On commence à jouer
                etat = ETAT_JOUE_CARTE;
                joueur = (joueurCoupe + 2) % PartieBelote.NB_JOUEUR;
                joueurPreneur = aJoueur;
                break;
            }
                
            case ACTION_JOUE : {
                int nextJoueur = (joueur + 1) % PartieBelote.NB_JOUEUR;
                if (nextJoueur == joueurOuvre) {
                    // Fin du tour
                    switch (etat) {
                        case ETAT_JOUE_CARTE: {
                            etat = ETAT_FIN_TOUR;
                            nbCarteJouee++;
                            break;
                        }
                        default :
                            System.err.println("etatSuivant : Transition inconnue action " + action + " etat " + etat);
                            break;
                    }
                }
                joueur = nextJoueur;
                    
                break;
            }
                
            case ACTION_RAMASSE : {
                // Le joueur passé en paramètre est celui qui doit jouer
                etat = ETAT_JOUE_CARTE;
                joueur = aJoueur;
                joueurOuvre = aJoueur;
                if (nbCarteJouee == 8) {
                    etat = ETAT_FIN_DONNE;
                }
                break;
            }
                
            case ACTION_GAGNE :{
                // Le joueur a gagné une donne
                etat = ETAT_COUPE_JEU;
                joueur = (joueurCoupe + 1) % PartieBelote.NB_JOUEUR;
                joueurCoupe = joueur;
                break;
            }
                
            case ACTION_QUITTE :
                etat = ETAT_FIN_PARTIE;
                started = false;
                break;
                
            default :
                break;
        }        
        
        
        return etat;
    }

}
