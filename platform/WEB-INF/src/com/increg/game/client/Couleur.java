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
 * Classe des cartes de couleurs "simples"
 */
public class Couleur extends Carte {

    /* ***************************************
     * Constantes
     * *************************************** */
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
     * Constante représentant la valeur retournée si deux couleurs différentes sont comparées
     */    
    public static final int COULEUR_DIFF = 100; 

    /**
     * Constante représentant le nombre de couleurs existantes
     */    
    public static final int NB_COULEUR = 4; 

    /* ***************************************
     * Attributs
     * *************************************** */
    /**
     * Couleur de la carte : 1, 2, 3, ...
     */
    protected int couleur;

    /**
     * Constructeur par défaut
     *
     */
    public Couleur() {
    }
    
    /**
     * Constructeur d'une carte prédéfinie
     * @param aHauteur Hauteur de la carte
     * @param aCouleur Couleur de la carte
     */
    public Couleur(int aHauteur, int aCouleur) {
        super(aHauteur);
        couleur = aCouleur;
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
     * Retour +COULEUR_DIFF ou -COULEUR_DIFF si couleur différente
     */
    public int compareTo(Object o) {
        
        Couleur autreCouleur = (Couleur) o; 
        if (couleur > autreCouleur.getCouleur()) {
            return +COULEUR_DIFF;
        }
        else if (couleur < autreCouleur.getCouleur()) {
            return -COULEUR_DIFF;
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
        return "C" + Integer.toString(couleur) + super.toString();
    }

    /**
     * Retourne la chaîne de caractère correspondante à la couleur
     * @param couleur couleur demandée
     * @return chaine correspondante
     */
    public static String toString(int couleur) {
        switch (couleur) {
            case PIQUE :
                return "Pique";
            case TREFLE :
                return "Trefle";
            case COEUR :
                return "Coeur";
            case CARREAU :
                return "Carreau";
            default :
                return "?";
        }
    }

    /**
     * @see com.increg.game.client.Carte#getLibHauteur()
     */
    public String getLibHauteur() {
        return Integer.toString(hauteur);
    }
}
