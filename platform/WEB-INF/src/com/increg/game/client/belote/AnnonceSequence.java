/*
 * Created on 18 juin 2003
 *
 */
package com.increg.game.client.belote;

import com.increg.game.client.Couleur;
import com.increg.game.client.CouleurStd;

/**
 * @author guyot_e
 *
 * Sequence de cartes formant une annonce
 */
public abstract class AnnonceSequence extends AnnonceBelote {

    /**
     * Création de l'annonce Belote / Rebelote
     * @param cartes Cartes formant l'annonce
     */
    public AnnonceSequence (Couleur[] cartes) {
        super();
        setCartes(cartes);
        atout = cartes[0] instanceof AtoutBelote;
    }

    /**
     * @see com.increg.game.client.Annonce#getValeurPonderee()
     */
    public int getValeurPonderee() {
        /**
         *  Les séquences sont déterminées par la hauteur de la plus grande tête
         *  A égalité, les séquences à l'atout sont supérieures à la même sequence qui n'est pas à l'atout
         */
        int valPond = getValeur();
        for (int i = 0; i < cartes.length; i++) {
            if (cartes[i] != null) {
                if (cartes[i] instanceof AtoutBelote) {
                    valPond += ((AtoutBelote) cartes[i]).getCouleurStd().getHauteur();
                }
                else if (cartes[i] instanceof CouleurBelote) {
                    valPond += ((CouleurBelote) cartes[i]).getCouleurStd().getHauteur();
                }
                else {
                    valPond += cartes[i].getHauteur();
                }
            }
        }
        if (atout) {
            valPond++;
        }
        return valPond;
    }

    /**
     * Calcule la hauteur max de la sequence
     * @param cartes formant la sequence
     * @return indice par rapport au tableau fourni
     */
    protected int getHauteurMax(Couleur[] cartes) {
        // Cherche la hauteur max
        int hauteurMax = -1;
        int iMax = -1;
        for (int i = 0; i < cartes.length; i++) {
            CouleurStd carteStd = null;
            if (cartes[i] != null) { 
                if (cartes[i] instanceof AtoutBelote) {
                    carteStd = ((AtoutBelote) cartes[i]).getCouleurStd();
                }
                else if (cartes[i] instanceof CouleurBelote) {
                    carteStd = ((CouleurBelote) cartes[i]).getCouleurStd();
                }
                if (carteStd.getHauteur() > hauteurMax) {
                    hauteurMax = carteStd.getHauteur();
                    iMax = i;
                }
            }
        }
        
        return iMax;
    }

}
