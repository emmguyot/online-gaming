/*
 * Created on 24 avr. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.client.belote.test;

import com.increg.game.client.belote.EtatPartieBelote;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class EtatPartieBeloteTest extends TestCase {


    /**
     * Teste l'enchainement des états de belote
     *
     */
    public void testEnchainement() {
        EtatPartieBelote etat = new EtatPartieBelote();
        
        etat.setEtat(EtatPartieBelote.ETAT_NON_DEMARRE);        
        Assert.assertEquals(etat.getEtat(), EtatPartieBelote.ETAT_NON_DEMARRE);

        etat.etatSuivant(EtatPartieBelote.ACTION_INITIALISE, 2);
        Assert.assertEquals(etat.getEtat(), EtatPartieBelote.ETAT_COUPE_JEU);
        Assert.assertEquals(etat.getJoueur(), 2);

        etat.etatSuivant(EtatPartieBelote.ACTION_COUPE, 2);
        Assert.assertEquals(etat.getEtat(), EtatPartieBelote.ETAT_DISTRIBUTION);
        Assert.assertEquals(etat.getJoueur(), 3);

        etat.etatSuivant(EtatPartieBelote.ACTION_DISTRIBUE, 3);
        Assert.assertEquals(etat.getEtat(), EtatPartieBelote.ETAT_TOUR_ENCHERE1);
        Assert.assertEquals(etat.getJoueur(), 0);

        etat.etatSuivant(EtatPartieBelote.ACTION_PASSE, 0);
        Assert.assertEquals(etat.getEtat(), EtatPartieBelote.ETAT_TOUR_ENCHERE1);
        Assert.assertEquals(etat.getJoueur(), 1);

        etat.etatSuivant(EtatPartieBelote.ACTION_PASSE, 1);
        Assert.assertEquals(etat.getEtat(), EtatPartieBelote.ETAT_TOUR_ENCHERE1);
        Assert.assertEquals(etat.getJoueur(), 2);

        etat.etatSuivant(EtatPartieBelote.ACTION_PREND, 2);
        Assert.assertEquals(etat.getEtat(), EtatPartieBelote.ETAT_JOUE_CARTE);
        Assert.assertEquals(etat.getJoueur(), 0);

        etat.etatSuivant(EtatPartieBelote.ACTION_JOUE, 54);
        Assert.assertEquals(etat.getEtat(), EtatPartieBelote.ETAT_JOUE_CARTE);
        Assert.assertEquals(etat.getJoueur(), 1);

        etat.etatSuivant(EtatPartieBelote.ACTION_JOUE, 54);
        Assert.assertEquals(etat.getEtat(), EtatPartieBelote.ETAT_JOUE_CARTE);
        Assert.assertEquals(etat.getJoueur(), 2);

        etat.etatSuivant(EtatPartieBelote.ACTION_JOUE, 54);
        Assert.assertEquals(etat.getEtat(), EtatPartieBelote.ETAT_JOUE_CARTE);
        Assert.assertEquals(etat.getJoueur(), 3);

        etat.etatSuivant(EtatPartieBelote.ACTION_JOUE, 55);
        Assert.assertEquals(etat.getEtat(), EtatPartieBelote.ETAT_FIN_TOUR);
        Assert.assertEquals(etat.getJoueur(), 0);

        etat.etatSuivant(EtatPartieBelote.ACTION_RAMASSE, 2);
        Assert.assertEquals(etat.getEtat(), EtatPartieBelote.ETAT_JOUE_CARTE);
        Assert.assertEquals(etat.getJoueur(), 2);

        etat.etatSuivant(EtatPartieBelote.ACTION_JOUE, 54);
        Assert.assertEquals(etat.getEtat(), EtatPartieBelote.ETAT_JOUE_CARTE);
        Assert.assertEquals(etat.getJoueur(), 3);

        etat.etatSuivant(EtatPartieBelote.ACTION_JOUE, 54);
        Assert.assertEquals(etat.getEtat(), EtatPartieBelote.ETAT_JOUE_CARTE);
        Assert.assertEquals(etat.getJoueur(), 0);

        etat.etatSuivant(EtatPartieBelote.ACTION_JOUE, 54);
        Assert.assertEquals(etat.getEtat(), EtatPartieBelote.ETAT_JOUE_CARTE);
        Assert.assertEquals(etat.getJoueur(), 1);

        etat.etatSuivant(EtatPartieBelote.ACTION_JOUE, 55);
        Assert.assertEquals(etat.getEtat(), EtatPartieBelote.ETAT_FIN_TOUR);
        Assert.assertEquals(etat.getJoueur(), 2);

        etat.etatSuivant(EtatPartieBelote.ACTION_GAGNE, 55);
        Assert.assertEquals(etat.getEtat(), EtatPartieBelote.ETAT_COUPE_JEU);
        Assert.assertEquals(etat.getJoueur(), 3);
    }

    /**
     * Teste l'enchainement des états de belote
     *
     */
    public void testEnchainement2() {
        EtatPartieBelote etat = new EtatPartieBelote();
        
        etat.setEtat(EtatPartieBelote.ETAT_NON_DEMARRE);        
        Assert.assertEquals(etat.getEtat(), EtatPartieBelote.ETAT_NON_DEMARRE);

        etat.etatSuivant(EtatPartieBelote.ACTION_INITIALISE, 2);
        Assert.assertEquals(etat.getEtat(), EtatPartieBelote.ETAT_COUPE_JEU);
        Assert.assertEquals(etat.getJoueur(), 2);

        etat.etatSuivant(EtatPartieBelote.ACTION_COUPE, 2);
        Assert.assertEquals(etat.getEtat(), EtatPartieBelote.ETAT_DISTRIBUTION);
        Assert.assertEquals(etat.getJoueur(), 3);

        etat.etatSuivant(EtatPartieBelote.ACTION_DISTRIBUE, 3);
        Assert.assertEquals(etat.getEtat(), EtatPartieBelote.ETAT_TOUR_ENCHERE1);
        Assert.assertEquals(etat.getJoueur(), 0);

        etat.etatSuivant(EtatPartieBelote.ACTION_PREND_SURCHERE_POSSIBLE, 0);
        Assert.assertEquals(etat.getEtat(), EtatPartieBelote.ETAT_TOUR_ENCHERE2_PRIS);
        Assert.assertEquals(etat.getJoueur(), 1);
        Assert.assertEquals(etat.getJoueurPreneur(), 0);

        etat.etatSuivant(EtatPartieBelote.ACTION_PASSE, 1);
        Assert.assertEquals(etat.getEtat(), EtatPartieBelote.ETAT_TOUR_ENCHERE2_PRIS);
        Assert.assertEquals(etat.getJoueur(), 2);
        Assert.assertEquals(etat.getJoueurPreneur(), 0);

        etat.etatSuivant(EtatPartieBelote.ACTION_PASSE, 2);
        Assert.assertEquals(etat.getEtat(), EtatPartieBelote.ETAT_TOUR_ENCHERE2_PRIS);
        Assert.assertEquals(etat.getJoueur(), 3);
        Assert.assertEquals(etat.getJoueurPreneur(), 0);

        etat.etatSuivant(EtatPartieBelote.ACTION_PASSE, 3);
        Assert.assertEquals(etat.getEtat(), EtatPartieBelote.ETAT_JOUE_CARTE);
        Assert.assertEquals(etat.getJoueur(), 0);
        Assert.assertEquals(etat.getJoueurPreneur(), 0);

    }

}
