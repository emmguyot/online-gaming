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
public class CouleurBelote extends Couleur {

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
     * Hauteur d'un Valet
     */
    public static final int VALET = NEUF + 1;
    /**
     * Hauteur d'un Dame
     */
    public static final int DAME = VALET + 1;
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
     * 
     */
    public CouleurBelote() {
        super();
    }

    /**
     * @param aHauteur Hauteur
     * @param aCouleur Couleur
     */
    public CouleurBelote(int aHauteur, int aCouleur) {
        super(aHauteur, aCouleur);
    }

    /**
     * @param aCarte carte à muter
     */
    public CouleurBelote(AtoutBelote aCarte) {
        super(aCarte.getHauteur(), aCarte.getCouleur());
        
        // Il faut convertir les hauteurs qui ne sont pas égales
        switch (aCarte.getHauteur()) {
            case AtoutBelote.SEPT :
                hauteur = CouleurBelote.SEPT;
                break;
            case AtoutBelote.HUIT :
                hauteur = CouleurBelote.HUIT;
                break;
            case AtoutBelote.NEUF :
                hauteur = CouleurBelote.NEUF;
                break;
            case AtoutBelote.DIX :
                hauteur = CouleurBelote.DIX;
                break;
            case AtoutBelote.VALET :
                hauteur = CouleurBelote.VALET;
                break;
            case AtoutBelote.DAME :
                hauteur = CouleurBelote.DAME;
                break;
            case AtoutBelote.ROI :
                hauteur = CouleurBelote.ROI;
                break;
            case AtoutBelote.AS :
                hauteur = CouleurBelote.AS;
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
            case CouleurBelote.SEPT :
                newHauteur = CouleurStd.SEPT;
                break;
            case CouleurBelote.HUIT :
                newHauteur = CouleurStd.HUIT;
                break;
            case CouleurBelote.NEUF :
                newHauteur = CouleurStd.NEUF;
                break;
            case CouleurBelote.DIX :
                newHauteur = CouleurStd.DIX;
                break;
            case CouleurBelote.VALET :
                newHauteur = CouleurStd.VALET;
                break;
            case CouleurBelote.DAME :
                newHauteur = CouleurStd.DAME;
                break;
            case CouleurBelote.ROI :
                newHauteur = CouleurStd.ROI;
                break;
            case CouleurBelote.AS :
                newHauteur = CouleurStd.AS;
                break;
            default :
                break;
        }
        return new CouleurStd(newHauteur, couleur);
    }
    
    /**
     * @see com.increg.game.bean.Carte#getValeur()
     */
    public double getValeur() {
        if (hauteur <= NEUF) {
            return 0;
        }
        else if (hauteur == VALET) {
            return 2;
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
        // Cas non prévu
        System.err.println("Carte couleur de valeur non prévue :" + hauteur);
        return -1;
    }


    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o) {
        
        // Rend la comparaison symétrique
        if (o instanceof AtoutBelote) {
            return -((AtoutBelote) o).compareTo(this);
        }
        else if (o instanceof CouleurStd) {
            return getCouleurStd().compareTo(o);
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
