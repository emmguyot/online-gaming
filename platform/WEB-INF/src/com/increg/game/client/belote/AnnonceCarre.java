/*
 * Created on 17 juin 2003
 *
 */
package com.increg.game.client.belote;

import com.increg.game.client.Couleur;

/**
 * @author guyot_e
 *
 * Annonce d'un carr� � la belote
 */
public class AnnonceCarre extends AnnonceBelote {

    /**
     * Constructeur d'un carr�
     * @param cartes cartes composant le carr�
     */
    public AnnonceCarre (Couleur[] cartes) {
        super();
        String hauteur = cartes[0].getLibHauteur();
        if ("aeiouyAEIOUY".indexOf(hauteur.charAt(0)) >= 0) {
            setDescription("Carr� d'" + hauteur);
        }
        else {
            setDescription("Carr� de " + hauteur);
        }
        setType(TYPE_CARRE);
        setCartes(cartes);
        setValeur();
    }
    
    /**
     * @see com.increg.game.client.Annonce#getValeurPonderee()
     */
    public int getValeurPonderee() {
        return getValeur() * cartes[0].getHauteur();
    }

}
