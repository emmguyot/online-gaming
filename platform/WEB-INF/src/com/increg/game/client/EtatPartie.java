/*
 * Created on 20 mai 2003
 *
 */
package com.increg.game.client;

/**
 * @author Manu
 *
 * Etat d'une partie
 * Une partie dispose de plusieurs états qui peuvent être déclinés par joueur
 */
public abstract class EtatPartie {


    /**
     * Valeur de l'état proprement dit
     */
    protected int etat;
    
    /**
     * Rang du joueur lié à cet état. Peut-être nul si l'état est global
     */
    protected int joueur;
    
    /**
     * Rang du joueur qui a coupé en dernier
     */
    protected int joueurCoupe;
    
    /**
     * Rang du joueur qui a ouvert le jeu
     */
    protected int joueurOuvre;
    
    /**
     * Rang du joueur qui a pris dans le tour de prise
     */
    protected int joueurPreneur;

    /**
     * Indicateur si la partie est démarrée
     */
    protected boolean started;    
    
    /**
     * Changement d'état basique : Passe au suivant sans se poser de question
     * @param action action provoquant le changement d'état
     * @param aJoueur Joueur qui fait l'action
     * @return nouvel etat 
     */
    public abstract int etatSuivant(int action, int aJoueur);
    
    /**
     * @return Valeur de l'état proprement dit
     */
    public int getEtat() {
        return etat;
    }

    /**
     * @return Joueur lié à cet état. Peut-être nul si l'état est global
     */
    public int getJoueur() {
        return joueur;
    }

    /**
     * @param i Valeur de l'état proprement dit
     */ 
    public void setEtat(int i) {
        etat = i;
    }

    /**
     * @param joueur Joueur lié à cet état. Peut-être nul si l'état est global
     */
    public void setJoueur(int joueur) {
        this.joueur = joueur;
    }

    /**
     * @return Rang du joueur qui a coupé en dernier
     */
    public int getJoueurCoupe() {
        return joueurCoupe;
    }

    /**
     * @return Rang du joueur qui a ouvert le jeu
     */
    public int getJoueurOuvre() {
        return joueurOuvre;
    }

    /**
     * @param i Rang du joueur qui a coupé en dernier
     */
    public void setJoueurCoupe(int i) {
        joueurCoupe = i;
    }

    /**
     * @param i Rang du joueur qui a ouvert le jeu
     */
    public void setJoueurOuvre(int i) {
        joueurOuvre = i;
    }

    /**
     * @return Rang du joueur qui a pris dans le tour de prise
     */
    public int getJoueurPreneur() {
        return joueurPreneur;
    }

    /**
     * @param i Rang du joueur qui a pris dans le tour de prise
     */
    public void setJoueurPreneur(int i) {
        joueurPreneur = i;
    }

    /**
     * @return Indicateur si la partie est démarrée ou pas
     */
    public boolean isStarted() {
        return started;
    }

}
