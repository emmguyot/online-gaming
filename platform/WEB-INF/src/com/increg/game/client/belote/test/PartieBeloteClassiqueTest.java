/*
 * Created on 24 avr. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.client.belote.test;

import java.util.Vector;

import com.increg.game.client.belote.AtoutBelote;
import com.increg.game.client.belote.CouleurBelote;
import com.increg.game.client.belote.EtatPartieBelote;
import com.increg.game.client.belote.PartieBeloteClassique;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class PartieBeloteClassiqueTest extends TestCase {


    /**
     * Teste la vérification de la possibilité de jouer une carte
     *
     */
    public void testPeutJouer() {
        
        PartieBeloteClassique aPartie = new PartieBeloteClassique();
        aPartie.setAtout(CouleurBelote.CARREAU);
        aPartie.getEtat().setEtat(EtatPartieBelote.ETAT_JOUE_CARTE);
        aPartie.getEtat().setJoueur(0);
        
        AtoutBelote dixCarreau = new AtoutBelote(CouleurBelote.DIX, CouleurBelote.CARREAU);
        CouleurBelote asTrefle = new CouleurBelote(CouleurBelote.AS, CouleurBelote.TREFLE);
        CouleurBelote dixTrefle = new CouleurBelote(CouleurBelote.DIX, CouleurBelote.TREFLE);
        AtoutBelote septCarreau = new AtoutBelote(CouleurBelote.SEPT, CouleurBelote.CARREAU);

        Vector main = new Vector(); 
        main.add(dixCarreau);
        main.add(asTrefle);
        main.add(dixTrefle);
        main.add(septCarreau);
        
        /**
         * 1èr cas pas de tapis
         */
        aPartie.getJeu().setTapis(new Vector());
        aPartie.getJeu().getMains()[0].addAll(main);
        
        Assert.assertEquals(aPartie.peutJouer(asTrefle), true);
        Assert.assertEquals(aPartie.peutJouer(dixCarreau), true);
        Assert.assertEquals(aPartie.peutJouer(dixTrefle), true);
        Assert.assertEquals(aPartie.peutJouer(septCarreau), true);
        
        
        /**
         * 2ème cas ouverture faite : Pas à l'atout
         * Partenaire n'a pas joué
         */
        aPartie.getJeu().setTapis(new Vector());
        aPartie.getJeu().getTapis().add(new CouleurBelote(CouleurBelote.SEPT, CouleurBelote.TREFLE));

        Assert.assertEquals(aPartie.peutJouer(asTrefle), true);
        Assert.assertEquals(aPartie.peutJouer(dixCarreau), false);
        Assert.assertEquals(aPartie.peutJouer(dixTrefle), true);
        Assert.assertEquals(aPartie.peutJouer(septCarreau), false);
        
        /**
         * 3ème cas ouverture faite : Pas à l'atout
         * Partenaire n'a pas joué
         */
        aPartie.getJeu().setTapis(new Vector());
        aPartie.getJeu().getTapis().add(new CouleurBelote(CouleurBelote.SEPT, CouleurBelote.COEUR));

        Assert.assertEquals(aPartie.peutJouer(asTrefle), false);
        Assert.assertEquals(aPartie.peutJouer(dixCarreau), true);
        Assert.assertEquals(aPartie.peutJouer(dixTrefle), false);
        Assert.assertEquals(aPartie.peutJouer(septCarreau), true);
        
        /**
         * 4ème cas ouverture faite : Pas à l'atout
         * Partenaire a joué
         */
        aPartie.getJeu().setTapis(new Vector());
        aPartie.getJeu().getTapis().add(new CouleurBelote(CouleurBelote.DIX, CouleurBelote.COEUR));
        aPartie.getJeu().getTapis().add(new CouleurBelote(CouleurBelote.SEPT, CouleurBelote.COEUR));

        Assert.assertEquals(aPartie.peutJouer(asTrefle), true);
        Assert.assertEquals(aPartie.peutJouer(dixCarreau), true);
        Assert.assertEquals(aPartie.peutJouer(dixTrefle), true);
        Assert.assertEquals(aPartie.peutJouer(septCarreau), true);
        
        /**
         * 5ème cas ouverture faite : A l'atout
         * Partenaire n'a pas joué
         */
        aPartie.getJeu().setTapis(new Vector());
        aPartie.getJeu().getTapis().add(new AtoutBelote(AtoutBelote.HUIT, CouleurBelote.CARREAU));

        Assert.assertEquals(aPartie.peutJouer(asTrefle), false);
        Assert.assertEquals(aPartie.peutJouer(dixCarreau), true);
        Assert.assertEquals(aPartie.peutJouer(dixTrefle), false);
        Assert.assertEquals(aPartie.peutJouer(septCarreau), false);
        
        /**
         * 6ème cas ouverture faite : A l'atout
         * Partenaire a joué
         */
        aPartie.getJeu().setTapis(new Vector());
        aPartie.getJeu().getTapis().add(new AtoutBelote(AtoutBelote.DAME, CouleurBelote.CARREAU));
        aPartie.getJeu().getTapis().add(new AtoutBelote(AtoutBelote.HUIT, CouleurBelote.CARREAU));

        Assert.assertEquals(aPartie.peutJouer(asTrefle), false);
        Assert.assertEquals(aPartie.peutJouer(dixCarreau), true);
        Assert.assertEquals(aPartie.peutJouer(dixTrefle), false);
        Assert.assertEquals(aPartie.peutJouer(septCarreau), false);
        
        /**
         * 7ème cas ouverture faite : Pisse
         * Partenaire n'a pas joué
         */
        aPartie.getJeu().setTapis(new Vector());
        aPartie.getJeu().getTapis().add(new AtoutBelote(AtoutBelote.NEUF, CouleurBelote.CARREAU));

        Assert.assertEquals(aPartie.peutJouer(asTrefle), false);
        Assert.assertEquals(aPartie.peutJouer(dixCarreau), true);
        Assert.assertEquals(aPartie.peutJouer(dixTrefle), false);
        Assert.assertEquals(aPartie.peutJouer(septCarreau), true);
    }

    /**
     * Teste la recherche de la carte maitresse
     *
     */
    public void testMaitresse() {
        PartieBeloteClassique aPartie = new PartieBeloteClassique();
        aPartie.setAtout(CouleurBelote.PIQUE);
        Vector tapis = new Vector();
        tapis.add(new CouleurBelote(7, 1));
        tapis.add(new CouleurBelote(4, 1));
        tapis.add(new CouleurBelote(3, 1));
        tapis.add(new CouleurBelote(4, 3));
        aPartie.getJeu().setTapis(tapis);
        
        Assert.assertEquals(aPartie.getMaitresse(), new CouleurBelote(7, 1));
    }

}
