/*
 * Created on 17 jun 2003
 *
 */
package com.increg.game.client;


import com.increg.game.client.belote.AtoutBelote;
import com.increg.game.client.belote.CouleurBelote;

/**
 * @author Manu
 *
 * Carte de couleur "normale" : Ordonnée de manière classique
 */
public class CouleurStd extends Couleur {

    /**
     * Hauteur d'un 7
     */
    public static final int SEPT = 0;
    /**
     * Hauteur d'un 8
     */
    public static final int HUIT = SEPT + 1;
    /**
     * Hauteur d'un 9
     */
    public static final int NEUF = HUIT + 1;
    /**
     * Hauteur d'un 10
     */
    public static final int DIX = NEUF + 1;
    /**
     * Hauteur d'un Valet
     */
    public static final int VALET = DIX + 1;
    /**
     * Hauteur d'un Dame
     */
    public static final int DAME = VALET + 1;
    /**
     * Hauteur d'un ROI
     */
    public static final int ROI = DAME + 1;
    /**
     * Hauteur d'un As
     */
    public static final int AS = ROI + 1;

    /**
     * 
     */
    public CouleurStd() {
        super();
    }

    /**
     * @param aHauteur Hauteur
     * @param aCouleur Couleur
     */
    public CouleurStd(int aHauteur, int aCouleur) {
        super(aHauteur, aCouleur);
    }

    /**
     * @see com.increg.game.bean.Carte#getValeur()
     * Non significatif
     */
    public double getValeur() {
        return -1;
    }


    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o) {
        
        // Rend la comparaison symétrique
        if (o instanceof AtoutBelote) {
            return super.compareTo(((AtoutBelote) o).getCouleurStd());
        }
        else if (o instanceof CouleurBelote) {
            return super.compareTo(((CouleurBelote) o).getCouleurStd());
        }
        return super.compareTo(o);
    }

    /**
     * @see com.increg.game.client.Carte#getLibHauteur()
     */
    public String getLibHauteur() {
        switch (hauteur) {
            case SEPT :
                return "sept";
            case HUIT :
                return "huit";
            case NEUF :
                return "neuf";
            case DIX :
                return "dix";
            case VALET :
                return "valet";
            case DAME :
                return "dame";
            case ROI :
                return "roi";
            case AS :
                return "as";
            default :
                return super.getLibHauteur();
        }
    }

}
