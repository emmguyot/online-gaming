/*
 * Created on 14 juin 2003
 *
 */
package com.increg.game.client.belote;

import com.increg.game.client.Annonce;
import com.increg.game.client.CouleurStd;

/**
 * @author Manu
 *
 * Annonce d'un jeu de Belote
 */
public class AnnonceBelote extends Annonce {


    /**
     * Type d'annonce Tierce : 3 cartes successives
     */
    public static final int TYPE_TIERCE = 0;
    /**
     * Type d'annonce Quatrième : 4 cartes successives
     */
    public static final int TYPE_QUATRIEME = TYPE_TIERCE + 1;
    /**
     * Type d'annonce Cent : 5 cartes successives
     */
    public static final int TYPE_CENT = TYPE_QUATRIEME + 1;
    /**
     * Type d'annonce Carré : 4 cartes identiques
     */
    public static final int TYPE_CARRE = TYPE_CENT + 1;

    /**
     * Annonce Belote / Rebelote
     */
    public static final int TYPE_BELOTE = 10;
    /**
     * Nombre de cartes formant la Belote / Rebelote
     */
    public static final int NB_CARTES_BELOTE = 2;
    /**
     * Nombre de cartes formant un carré
     */
    public static final int NB_CARTES_CARRE = 4;
    /**
     * Nombre de cartes formant un cent
     */
    public static final int NB_CARTES_CENT = 5;
    /**
     * Nombre de cartes formant un 50
     */
    public static final int NB_CARTES_QUATRIEME = 4;
    /**
     * Nombre de cartes formant une tierce
     */
    public static final int NB_CARTES_TIERCE = 3;
    
    /**
     * L'annonce est à l'atout ?
     */
    protected boolean atout;
    
    /**
     * 
     */
    public AnnonceBelote() {
        super();
        atout = false;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o) {
        return getValeurPonderee() - ((Annonce) o).getValeurPonderee();
    }

    /**
     * Calcule la valeur de l'annonce
     */
    public void setValeur() {
        switch (type) {
            case TYPE_BELOTE :
                valeur = 20;
                break;

            case TYPE_CARRE : {
                CouleurStd carteSimple;
                if (cartes[0] instanceof AtoutBelote) {
                    carteSimple = ((AtoutBelote) cartes[0]).getCouleurStd(); 
                }
                else {
                    carteSimple = ((CouleurBelote) cartes[0]).getCouleurStd(); 
                }
                switch (carteSimple.getHauteur()) {
                    case CouleurStd.AS :
                        valeur = 100;
                        break;
                    case CouleurStd.DIX :
                        valeur = 100;
                        break;
                    case CouleurStd.NEUF :
                        valeur = 150;
                        break;
                    case CouleurStd.HUIT :
                        valeur = 0;
                        break;
                    case CouleurStd.SEPT :
                        valeur = 0;
                        break;
                    case CouleurStd.VALET :
                        valeur = 200;
                        break;
                    case CouleurStd.DAME :
                        valeur = 100;
                        break;
                    case CouleurStd.ROI :
                        valeur = 100;
                        break;
                    default :
                        break;
                }
                break;
            }

            case TYPE_TIERCE :
                valeur = 20;
                break;

            case TYPE_QUATRIEME :
                valeur = 50;
                break;

            case TYPE_CENT :
                valeur = 100;
                break;

            default :
                break;
        }
    }

    /**
     * @return L'annonce est à l'atout ?
     */
    public boolean isAtout() {
        return atout;
    }

    /**
     * @param b L'annonce est à l'atout ?
     */
    public void setAtout(boolean b) {
        atout = b;
    }

}
