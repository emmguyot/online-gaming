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
     * Constante repr�sentant la couleur PIQUE
     */    
    public static final int PIQUE = 0; 

    /**
     * Constante repr�sentant la couleur COEUR
     */    
    public static final int COEUR = PIQUE + 1; 

    /**
     * Constante repr�sentant la couleur TREFLE
     */    
    public static final int TREFLE = COEUR + 1; 

    /**
     * Constante repr�sentant la couleur CARREAU
     */    
    public static final int CARREAU = TREFLE + 1; 

    /**
     * Constante repr�sentant la valeur retourn�e si deux couleurs diff�rentes sont compar�es
     */    
    public static final int COULEUR_DIFF = 100; 

    /**
     * Constante repr�sentant le nombre de couleurs existantes
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
     * Constructeur par d�faut
     *
     */
    public Couleur() {
    }
    
    /**
     * Constructeur d'une carte pr�d�finie
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
     * Retour +COULEUR_DIFF ou -COULEUR_DIFF si couleur diff�rente
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
            // M�me couleur, c'est la hauteur qui dit
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
     * Retourne la cha�ne de caract�re correspondante � la couleur
     * @param couleur couleur demand�e
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
