/*
 * Created on 22 mai 2003
 *
 */
package com.increg.game.client;

import com.increg.game.client.belote.CouleurBelote;

/**
 * @author Manu
 *
 * Jeu de 32 Cartes
 */
public class Jeu32Cartes extends JeuCartes {

    /**
     * Constructeur
     * @param nbJoueur Nombre de joueurs
     */
    public Jeu32Cartes(int nbJoueur) {
        super(32, nbJoueur);
        
        // Alimentation du jeu initial
        for (int i = 0; i < 8; i++) {
            for (int j = 0 ; j < 4; j++) {
                jeuInit.add(new CouleurBelote(i, j));
            }
        }
        
        // Initialise le tas
        melangeTas();
    }
    
    /**
     * Distribue le tas 
     * @param phase Phase correspondante
     * @param nbDebut Nombre de cartes au début 2 ou 3 par exemple
     * @param premierJoueur Premier joueur à servir
     * @param preneur preneur (si influence la distribution)
     */
    public void distribue(int phase, int nbDebut, int premierJoueur, int preneur) {
        
    }
}
