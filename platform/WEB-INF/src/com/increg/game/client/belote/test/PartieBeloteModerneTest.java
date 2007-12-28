/*
 * Created on 24 avr. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.client.belote.test;

import java.util.Vector;

import com.increg.game.client.Carte;
import com.increg.game.client.belote.CouleurBelote;
import com.increg.game.client.belote.EtatPartieBelote;
import com.increg.game.client.belote.PartieBeloteModerne;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class PartieBeloteModerneTest extends TestCase {


    /**
     * Teste la vérification de la possibilité de jouer une carte
     *
     */
    public void testPeutJouer() {
        
        PartieBeloteModerne aPartie = new PartieBeloteModerne();
        aPartie.setAtout(PartieBeloteModerne.SANS_ATOUT);
        aPartie.getEtat().setEtat(EtatPartieBelote.ETAT_JOUE_CARTE);
        aPartie.getEtat().setJoueur(0);
        
        CouleurBelote valetCoeur = new CouleurBelote(CouleurBelote.VALET, CouleurBelote.COEUR);
        CouleurBelote huitTrefle = new CouleurBelote(CouleurBelote.HUIT, CouleurBelote.TREFLE);

        Vector<Carte> tapis = new Vector<Carte>();
        tapis.add(valetCoeur);
        tapis.add(huitTrefle);
        
        CouleurBelote dixCarreau = new CouleurBelote(CouleurBelote.DIX, CouleurBelote.CARREAU);
        CouleurBelote dameCarreau = new CouleurBelote(CouleurBelote.DAME, CouleurBelote.CARREAU);
        CouleurBelote valetCarreau = new CouleurBelote(CouleurBelote.VALET, CouleurBelote.CARREAU);
        CouleurBelote roiCoeur = new CouleurBelote(CouleurBelote.ROI, CouleurBelote.COEUR);

        Vector<CouleurBelote> main = new Vector<CouleurBelote>(); 
        main.add(dixCarreau);
        main.add(dameCarreau);
        main.add(valetCarreau);
        main.add(roiCoeur);
        
        /**
         * 1èr Tentative erronée de joeur le valet de coeur
         */
        aPartie.getJeu().setTapis(tapis);
        aPartie.getJeu().getMains()[0].addAll(main);
        
        Assert.assertEquals(aPartie.peutJouer(valetCarreau), false);
        Assert.assertEquals(aPartie.peutJouer(dameCarreau), false);
        Assert.assertEquals(aPartie.peutJouer(dixCarreau), false);
        Assert.assertEquals(aPartie.peutJouer(roiCoeur), true);
        
    }        
}
