/*
 * Created on 18 juin 2003
 *
 */
package com.increg.game.client.belote;

import com.increg.game.client.Couleur;

/**
 * @author guyot_e
 *
 * Sequence de 5 cartes
 */
public class AnnonceCent extends AnnonceSequence {

    /**
     * Création de l'annonce Belote / Rebelote
     * @param cartes Cartes formant l'annonce
     */
    public AnnonceCent(Couleur[] cartes) {
        super(cartes);
        
        int iMax = getHauteurMax(cartes);
        String hauteur = cartes[iMax].getLibHauteur();
        setDescription("100 (" + hauteur + ")");
        setType(AnnonceBelote.TYPE_CENT);
        setValeur();
    }

}
