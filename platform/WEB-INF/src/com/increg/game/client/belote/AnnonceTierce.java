/*
 * Created on 18 juin 2003
 *
 */
package com.increg.game.client.belote;

import com.increg.game.client.Couleur;

/**
 * @author guyot_e
 *
 * Sequence de 3 cartes
 */
public class AnnonceTierce extends AnnonceSequence {

    /**
     * Création de l'annonce Belote / Rebelote
     * @param cartes Cartes formant l'annonce
     */
    public AnnonceTierce(Couleur[] cartes) {
        super(cartes);
        
        int iMax = getHauteurMax(cartes);
        String hauteur = cartes[iMax].getLibHauteur();
        setDescription("Tierce (" + hauteur + ")");
        setType(AnnonceBelote.TYPE_TIERCE);
        setValeur();
    }


}
