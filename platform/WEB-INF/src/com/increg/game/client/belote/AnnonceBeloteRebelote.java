/*
 * Created on 17 juin 2003
 *
 */
package com.increg.game.client.belote;

import com.increg.game.client.Couleur;

/**
 * @author guyot_e
 *
 * Annonce Belote / Rebelote
 */
public class AnnonceBeloteRebelote extends AnnonceBelote {

    /**
     * Création de l'annonce Belote / Rebelote
     * @param cartes Cartes formant l'annonce
     */
    public AnnonceBeloteRebelote (Couleur[] cartes) {
        super();
        setDescription("Belote et Rebelote");
        setType(AnnonceBelote.TYPE_BELOTE);
        setCartes(cartes);
        setValeur();
    }
}
