/*
 * Created on 24 avr. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.client.belote.test;

import com.increg.game.client.Couleur;
import com.increg.game.client.belote.AnnonceCarre;
import com.increg.game.client.belote.AnnonceCent;
import com.increg.game.client.belote.AnnonceQuatrieme;
import com.increg.game.client.belote.AnnonceSequence;
import com.increg.game.client.belote.AnnonceTierce;
import com.increg.game.client.belote.AtoutBelote;
import com.increg.game.client.belote.CouleurBelote;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class AnnonceSequenceTest extends TestCase {


    /**
     * Teste la valeur pondérée des séquences
     *
     */
    public void testValeurPonderee() {

        CouleurBelote septCarreau = new CouleurBelote(CouleurBelote.SEPT, CouleurBelote.CARREAU);
        CouleurBelote huitCarreau = new CouleurBelote(CouleurBelote.HUIT, CouleurBelote.CARREAU);
        CouleurBelote neufCarreau = new CouleurBelote(CouleurBelote.NEUF, CouleurBelote.CARREAU);
//        CouleurBelote dixCarreau = new CouleurBelote(CouleurBelote.DIX, CouleurBelote.CARREAU);
//        CouleurBelote valetCarreau = new CouleurBelote(CouleurBelote.VALET, CouleurBelote.CARREAU);
        CouleurBelote dameCarreau = new CouleurBelote(CouleurBelote.DAME, CouleurBelote.CARREAU);
//        CouleurBelote roiCarreau = new CouleurBelote(CouleurBelote.ROI, CouleurBelote.CARREAU);
//        CouleurBelote asCarreau = new CouleurBelote(CouleurBelote.AS, CouleurBelote.CARREAU);
//        CouleurBelote septTrefle = new CouleurBelote(CouleurBelote.SEPT, CouleurBelote.TREFLE);
//        CouleurBelote huitTrefle = new CouleurBelote(CouleurBelote.HUIT, CouleurBelote.TREFLE);
//        CouleurBelote neufTrefle = new CouleurBelote(CouleurBelote.NEUF, CouleurBelote.TREFLE);
//        CouleurBelote dixTrefle = new CouleurBelote(CouleurBelote.DIX, CouleurBelote.TREFLE);
//        CouleurBelote valetTrefle = new CouleurBelote(CouleurBelote.VALET, CouleurBelote.TREFLE);
        CouleurBelote dameTrefle = new CouleurBelote(CouleurBelote.DAME, CouleurBelote.TREFLE);
//        CouleurBelote roiTrefle = new CouleurBelote(CouleurBelote.ROI, CouleurBelote.TREFLE);
//        CouleurBelote asTrefle = new CouleurBelote(CouleurBelote.AS, CouleurBelote.TREFLE);
        CouleurBelote septCoeur = new CouleurBelote(CouleurBelote.SEPT, CouleurBelote.COEUR);
        CouleurBelote huitCoeur = new CouleurBelote(CouleurBelote.HUIT, CouleurBelote.COEUR);
        CouleurBelote neufCoeur = new CouleurBelote(CouleurBelote.NEUF, CouleurBelote.COEUR);
        CouleurBelote dixCoeur = new CouleurBelote(CouleurBelote.DIX, CouleurBelote.COEUR);
        CouleurBelote valetCoeur = new CouleurBelote(CouleurBelote.VALET, CouleurBelote.COEUR);
        CouleurBelote dameCoeur = new CouleurBelote(CouleurBelote.DAME, CouleurBelote.COEUR);
//        CouleurBelote roiCoeur = new CouleurBelote(CouleurBelote.ROI, CouleurBelote.COEUR);
//        CouleurBelote asCoeur = new CouleurBelote(CouleurBelote.AS, CouleurBelote.COEUR);
        AtoutBelote septPique = new AtoutBelote(AtoutBelote.SEPT, CouleurBelote.PIQUE);
        AtoutBelote huitPique = new AtoutBelote(AtoutBelote.HUIT, CouleurBelote.PIQUE);
        AtoutBelote neufPique = new AtoutBelote(AtoutBelote.NEUF, CouleurBelote.PIQUE);
        AtoutBelote dixPique = new AtoutBelote(AtoutBelote.DIX, CouleurBelote.PIQUE);
        AtoutBelote valetPique = new AtoutBelote(AtoutBelote.VALET, CouleurBelote.PIQUE);
        AtoutBelote damePique = new AtoutBelote(AtoutBelote.DAME, CouleurBelote.PIQUE);
        AtoutBelote roiPique = new AtoutBelote(AtoutBelote.ROI, CouleurBelote.PIQUE);
        AtoutBelote asPique = new AtoutBelote(AtoutBelote.AS, CouleurBelote.PIQUE);

        Couleur[] cartes1 = new Couleur[5];        
        cartes1[0] = septCarreau;
        cartes1[1] = huitCarreau;
        cartes1[2] = neufCarreau;
        AnnonceTierce tierce1 = new AnnonceTierce(cartes1);        
        Couleur[] cartes2 = new Couleur[5];        
        cartes2[0] = septCoeur;
        cartes2[1] = huitCoeur;
        cartes2[2] = neufCoeur;
        AnnonceTierce tierce2 = new AnnonceTierce(cartes2);        
        Couleur[] cartes3 = new Couleur[5];        
        cartes3[0] = huitCoeur;
        cartes3[1] = neufCoeur;
        cartes3[2] = dixCoeur;
        AnnonceTierce tierce3 = new AnnonceTierce(cartes3);        
        Couleur[] cartes4 = new Couleur[5];        
        cartes4[0] = septPique;
        cartes4[1] = huitPique;
        cartes4[2] = neufPique;
        AnnonceTierce tierce4 = new AnnonceTierce(cartes4);        
        Couleur[] cartes5 = new Couleur[5];        
        cartes5[0] = damePique;
        cartes5[1] = roiPique;
        cartes5[2] = asPique;
        AnnonceTierce tierce5 = new AnnonceTierce(cartes5);        
        Couleur[] cartes6 = new Couleur[5];        
        cartes6[0] = septPique;
        cartes6[1] = huitPique;
        cartes6[2] = neufPique;
        cartes6[3] = dixPique;
        AnnonceSequence cinquante6 = new AnnonceQuatrieme(cartes6);        
        Couleur[] cartes7 = new Couleur[5];        
        cartes7[0] = valetPique;
        cartes7[1] = damePique;
        cartes7[2] = roiPique;
        cartes7[3] = asPique;
        AnnonceSequence cinquante7 = new AnnonceQuatrieme(cartes7);        
        Couleur[] cartes8 = new Couleur[5];        
        cartes8[0] = septCoeur;
        cartes8[1] = huitCoeur;
        cartes8[2] = neufCoeur;
        cartes8[3] = dixCoeur;
        cartes8[4] = valetCoeur;
        AnnonceSequence cent8 = new AnnonceCent(cartes8);        
        Couleur[] cartes9 = new Couleur[5];        
        cartes9[0] = dixPique;
        cartes9[1] = valetPique;
        cartes9[2] = damePique;
        cartes9[3] = roiPique;
        cartes9[4] = asPique;
        AnnonceSequence cent9 = new AnnonceCent(cartes9);        
        Couleur[] cartes10 = new Couleur[5];        
        cartes10[0] = damePique;
        cartes10[1] = dameCoeur;
        cartes10[2] = dameCarreau;
        cartes10[3] = dameTrefle;
        AnnonceCarre carre10 = new AnnonceCarre(cartes10);        
        
        Assert.assertEquals(tierce1.getValeurPonderee(), tierce2.getValeurPonderee());
        Assert.assertTrue(tierce1.getValeurPonderee() < tierce3.getValeurPonderee());
        Assert.assertTrue(tierce4.getValeurPonderee() > tierce1.getValeurPonderee());
        Assert.assertTrue(tierce4.getValeurPonderee() < tierce3.getValeurPonderee());
        Assert.assertTrue(tierce5.getValeurPonderee() > tierce4.getValeurPonderee());
        Assert.assertTrue(cinquante6.getValeurPonderee() > tierce5.getValeurPonderee());
        Assert.assertTrue(cinquante7.getValeurPonderee() > cinquante6.getValeurPonderee());
        Assert.assertTrue(cent8.getValeurPonderee() > cinquante7.getValeurPonderee());
        Assert.assertTrue(cent9.getValeurPonderee() > cent8.getValeurPonderee());
        Assert.assertTrue(carre10.getValeurPonderee() > cent9.getValeurPonderee());
        Assert.assertTrue(tierce5.getValeurPonderee() > tierce3.getValeurPonderee());
        
    }

}
