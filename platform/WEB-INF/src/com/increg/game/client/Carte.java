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
 * Classe générique représentant une carte
 */
public abstract class Carte implements Comparable, Cloneable {

    /**
     * Hauteur de la carte : 0 à 10 puis 11, 12, 13, ...
     */
    protected int hauteur;

    /* **************************************************************************** */
    
    /**
     * Constructeur par défaut
     */
    public Carte() {
    }
    
    /**
     * Contructeur direct
     * @param aHauteur hauteur de la carte
     */
    public Carte(int aHauteur) {
        hauteur = aHauteur;
    }
    /**
     * Calcule la valeur de la carte et la retourne
     * @return Valeur de la carte
     */
    public abstract double getValeur();

    /**
     * @return hauteur de la carte
     */
    public int getHauteur() {
        return hauteur;
    }

    /**
     * @param i Hauteur de la carte
     */
    public void setHauteur(int i) {
        hauteur = i;
    }

    /**
     * @see java.lang.Object#clone()
     */
    protected Object clone() throws CloneNotSupportedException {
        Carte myClone = (Carte) super.clone();

        myClone.setHauteur(getHauteur());
        
        return myClone;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o) {
        Carte autreCarte = (Carte) o; 
        if (hauteur > autreCarte.getHauteur()) {
            return +1;
        }
        else if (hauteur < autreCarte.getHauteur()) {
            return -1;
        }
        else {
            return 0;
        }
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        return (this.compareTo(obj) == 0);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "H" + Integer.toString(hauteur);
    }

    /**
     * @return le libellé de la hauteur de la carte
     */
    public abstract String getLibHauteur(); 
}
