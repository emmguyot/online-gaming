/*
 * Created on 22 juin 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.client;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Tournoi {

    /**
     * Identifiant du tournoi (chrono sans signification)
     */
    protected int identifiant;
    
    // TODO Ajouter les autres éléments caractéristiques des tournois
    
    /**
     * Constructeur
     */
    public Tournoi() {
        super();
        identifiant = 0;
    }

    /**
     * @return Identifiant du tournoi (chrono sans signification)
     */
    public int getIdentifiant() {
        return identifiant;
    }

    /**
     * @param i Identifiant du tournoi (chrono sans signification)
     */
    public void setIdentifiant(int i) {
        identifiant = i;
    }

}
