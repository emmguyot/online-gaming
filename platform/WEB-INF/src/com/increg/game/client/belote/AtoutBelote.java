/*
 * Created on 5 avr. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.client.belote;

import com.increg.game.client.Couleur;
import com.increg.game.client.CouleurStd;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class AtoutBelote extends Couleur {

    /**
     * Hauteur d'un 7
     */
    public static final int SEPT = 0;
    /**
     * Hauteur d'un 8
     */
    public static final int HUIT = SEPT + 1;
    /**
     * Hauteur d'un Dame
     */
    public static final int DAME = HUIT + 1;
    /**
     * Hauteur d'un ROI
     */
    public static final int ROI = DAME + 1;
    /**
     * Hauteur d'un 10
     */
    public static final int DIX = ROI + 1;
    /**
     * Hauteur d'un As
     */
    public static final int AS = DIX + 1;
    /**
     * Hauteur d'un 9
     */
    public static final int NEUF = AS + 1;
    /**
     * Hauteur d'un Valet
     */
    public static final int VALET = NEUF + 1;

    /**
     * Valeur retournée si il y a coupe
     */
    public static final int ATOUT_COUPE = 500;

    /**
     * 
     */
    public AtoutBelote() {
        super();
    }

    /**
     * @param aCarte carte à muter
     */
    public AtoutBelote(CouleurBelote aCarte) {
        super(aCarte.getHauteur(), aCarte.getCouleur());

        // Il faut convertir les hauteurs qui ne sont pas égales
        switch (aCarte.getHauteur()) {
            case CouleurBelote.SEPT :
                hauteur = AtoutBelote.SEPT;
                break;
            case CouleurBelote.HUIT :
                hauteur = AtoutBelote.HUIT;
                break;
            case CouleurBelote.NEUF :
                hauteur = AtoutBelote.NEUF;
                break;
            case CouleurBelote.DIX :
                hauteur = AtoutBelote.DIX;
                break;
            case CouleurBelote.VALET :
                hauteur = AtoutBelote.VALET;
                break;
            case CouleurBelote.DAME :
                hauteur = AtoutBelote.DAME;
                break;
            case CouleurBelote.ROI :
                hauteur = AtoutBelote.ROI;
                break;
            case CouleurBelote.AS :
                hauteur = AtoutBelote.AS;
                break;
            default :
                break;
        }
    }

    /**
     * @return carte standard obtenue
     */
    public CouleurStd getCouleurStd() {
        int newHauteur = 0;

        // Il faut convertir les hauteurs qui ne sont pas égales
        switch (hauteur) {
            case AtoutBelote.SEPT :
                newHauteur = CouleurStd.SEPT;
                break;
            case AtoutBelote.HUIT :
                newHauteur = CouleurStd.HUIT;
                break;
            case AtoutBelote.NEUF :
                newHauteur = CouleurStd.NEUF;
                break;
            case AtoutBelote.DIX :
                newHauteur = CouleurStd.DIX;
                break;
            case AtoutBelote.VALET :
                newHauteur = CouleurStd.VALET;
                break;
            case AtoutBelote.DAME :
                newHauteur = CouleurStd.DAME;
                break;
            case AtoutBelote.ROI :
                newHauteur = CouleurStd.ROI;
                break;
            case AtoutBelote.AS :
                newHauteur = CouleurStd.AS;
                break;
            default :
                break;
        }

        return new CouleurStd(newHauteur, couleur);
    }

    /**
     * @param aHauteur Hauteur
     * @param aCouleur Couleur
     */
    public AtoutBelote(int aHauteur, int aCouleur) {
        super(aHauteur, aCouleur);
    }

    /**
     * @see com.increg.game.bean.Carte#getValeur()
     */
    public double getValeur() {
        if (hauteur <= HUIT) {
            return 0;
        }
        else if (hauteur == DAME) {
            return 3;
        }
        else if (hauteur == ROI) {
            return 4;
        }
        else if (hauteur == DIX) {
            return 10;
        }
        else if (hauteur == AS) {
            return 11;
        }
        else if (hauteur == NEUF) {
            return 14;
        }
        else if (hauteur == VALET) {
            return 20;
        }
        // Cas non prévu
        System.err.println("Carte atout de valeur non prévue :" + hauteur);
        return -1;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     * Retour +ATOUT_COUPE ou +COULEUR_DIFF ou -COULEUR_DIFF si couleur différente
     */
    public int compareTo(Object o) {
        if (o instanceof AtoutBelote) {
            // Comparaison de 2 atouts
            return super.compareTo(o);
        }
        else if (o instanceof CouleurStd) {
            return -getCouleurStd().compareTo(o);
        }
        else if (((Couleur) o).getCouleur() == couleur) {
            // Même couleur mais pas atout
            return super.compareTo(new AtoutBelote((CouleurBelote) o));
        }
        else {
            // L'atout est plus fort
            return +ATOUT_COUPE;
        }
    }

    /**
     * @see java.lang.Object#toString()
     * Du fait du changement d'ordre, il faut surcharger cette méthode : Elle doit convertir les hauteurs
     */
    public String toString() {
        // Il faut convertir les hauteurs qui ne sont pas égales
        int hauteurEquiv = -1;
        switch (hauteur) {
            case AtoutBelote.SEPT :
                hauteurEquiv = CouleurBelote.SEPT;
                break;
            case AtoutBelote.HUIT :
                hauteurEquiv = CouleurBelote.HUIT;
                break;
            case AtoutBelote.NEUF :
                hauteurEquiv = CouleurBelote.NEUF;
                break;
            case AtoutBelote.DIX :
                hauteurEquiv = CouleurBelote.DIX;
                break;
            case AtoutBelote.VALET :
                hauteurEquiv = CouleurBelote.VALET;
                break;
            case AtoutBelote.DAME :
                hauteurEquiv = CouleurBelote.DAME;
                break;
            case AtoutBelote.ROI :
                hauteurEquiv = CouleurBelote.ROI;
                break;
            case AtoutBelote.AS :
                hauteurEquiv = CouleurBelote.AS;
                break;
            default :
                break;
        }
        return "C" + Integer.toString(couleur) + "H" + hauteurEquiv;
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
