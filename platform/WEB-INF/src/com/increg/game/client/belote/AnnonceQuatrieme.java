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
public class AnnonceQuatrieme extends AnnonceSequence {

    /**
     * Création de l'annonce Belote / Rebelote
     * @param cartes Cartes formant l'annonce
     */
    public AnnonceQuatrieme(Couleur[] cartes) {
        super(cartes);
        
        int iMax = getHauteurMax(cartes);
        String hauteur = cartes[iMax].getLibHauteur();
        setDescription("50 (" + hauteur + ")");
        setType(AnnonceBelote.TYPE_QUATRIEME);
        setValeur();
    }

}
