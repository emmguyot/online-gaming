/*
 * Created on 5 avr. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.client;

/**
 * @author Manu
 *
 * Cartes d'atout
 */
public class Atout extends Carte {

    /**
     * Couleur de la carte : 1, 2, 3, ...
     */
    protected int couleur;

    /**
     * Constante représentant la couleur PIQUE
     */    
    public static final int PIQUE = 0; 

    /**
     * Constante représentant la couleur COEUR
     */    
    public static final int COEUR = PIQUE + 1; 

    /**
     * Constante représentant la couleur TREFLE
     */    
    public static final int TREFLE = COEUR + 1; 

    /**
     * Constante représentant la couleur CARREAU
     */    
    public static final int CARREAU = TREFLE + 1; 

    /**
     * Constructeur par défaut
     *
     */
    public Atout() {
    }
    
    /**
     * Constructeur d'une carte prédéfinie
     * @param aHauteur Hauteur de la carte
     * @param aCouleur Couleur de la carte
     */
    public Atout(int aHauteur, int aCouleur) {
        super(aHauteur);
        couleur = aCouleur;
    }

    /**
     * Constructeur d'une carte d'atout à partir d'une carte "simple"
     * @param aCarte carte d'origine à muter
     */
    public Atout(Couleur aCarte) {
        super(aCarte.getHauteur());
        couleur = aCarte.getCouleur();
    }

    /**
     * @return couleur de la carte
     */
    public int getCouleur() {
        return couleur;
    }

    /**
     * @param i couleur de la carte
     */
    public void setCouleur(int i) {
        couleur = i;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     * Retour +10 ou -10 si couleur différente
     */
    public int compareTo(Object o) {
        Couleur autreCouleur = (Couleur) o; 
        if (couleur > autreCouleur.getCouleur()) {
            return +10;
        }
        else if (couleur < autreCouleur.getCouleur()) {
            return -10;
        }
        else {
            // Même couleur, c'est la hauteur qui dit
            return super.compareTo(o);
        }
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        return (this.compareTo(obj) == 0);
    }

    /**
     * @see java.lang.Object#clone()
     */
    protected Object clone() throws CloneNotSupportedException {
        Couleur myClone = (Couleur) super.clone();

        myClone.setCouleur(getCouleur());
        
        return myClone;
    }

    /**
     * @see com.increg.game.client.Carte#getValeur()
     */
    public double getValeur() {
        return 0;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "A" + Integer.toString(couleur) + super.toString();
    }

    /**
     * @see com.increg.game.client.Carte#getLibHauteur()
     */
    public String getLibHauteur() {
        return Integer.toString(hauteur);
    }
}
