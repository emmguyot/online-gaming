/*
 * Created on 14 juin 2003
 *
 */
package com.increg.game.client;

/**
 * @author Manu
 *
 * Annonce d'un jeu
 */
public abstract class Annonce implements Comparable {

    /**
     * Valeur de l'annonce
     */
    protected int valeur;
    /**
     * Description textuelle de l'annonce
     */
    protected String description;
    /**
     * Type d'annonce
     */
    protected int type;
    /**
     * Cartes formant l'annonce
     */
    protected Carte[] cartes;
    /**
     * Indique si l'annonce a été faite
     */
    protected boolean annonceFaite;
    /**
     * Indicateur si l'annonce est valide
     */
    protected boolean valide;

    /**
     * Constructeur
     */
    public Annonce() {
        super();
        description = "";
        annonceFaite = false;
    }
    
    /**
     * @return Description textuelle de l'annonce
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return Type d'annonce
     */
    public int getType() {
        return type;
    }

    /**
     * @return Valeur de l'annonce
     */
    public int getValeur() {
        return valeur;
    }

    /**
     * @return Valeur pondérée de l'annonce, définissant un ordre absolu des annonces
     */
    public int getValeurPonderee() {
        return valeur;
    }

    /**
     * @param string Description textuelle de l'annonce
     */
    public void setDescription(String string) {
        description = string;
    }

    /**
     * @param i Type d'annonce
     */
    public void setType(int i) {
        type = i;
    }

    /**
     * @param i Valeur de l'annonce
     */
    public void setValeur(int i) {
        valeur = i;
    }

    /**
     * @return Cartes formant l'annonce
     */
    public Carte[] getCartes() {
        return cartes;
    }

    /**
     * @param cartes Cartes formant l'annonce
     */
    public void setCartes(Carte[] cartes) {
        this.cartes = cartes;
    }

    /**
     * @return Indique si l'annonce a été faite
     */
    public boolean isAnnonceFaite() {
        return annonceFaite;
    }

    /**
     * @param b Indique si l'annonce a été faite
     */
    public void setAnnonceFaite(boolean b) {
        annonceFaite = b;
    }

    /**
     * @return Indicateur si l'annonce est valide
     */
    public boolean isValide() {
        return valide;
    }

    /**
     * @param b Indicateur si l'annonce est valide
     */
    public void setValide(boolean b) {
        valide = b;
    }

}
