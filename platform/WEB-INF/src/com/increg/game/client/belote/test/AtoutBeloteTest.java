/*
 * Created on 24 avr. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.client.belote.test;

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
public class AtoutBeloteTest extends TestCase {


    /**
     * Teste la comparaison de carte
     *
     */
    public void testComparaison() {
        
        CouleurBelote dixCarreau = new CouleurBelote(CouleurBelote.DIX, CouleurBelote.CARREAU);
        AtoutBelote dixCarreauAtout = new AtoutBelote(AtoutBelote.DIX, AtoutBelote.CARREAU);
        AtoutBelote valetCarreauAtout = new AtoutBelote(AtoutBelote.VALET, AtoutBelote.CARREAU);
        
        Assert.assertTrue(dixCarreauAtout.compareTo(valetCarreauAtout) < 0);
        Assert.assertTrue(dixCarreauAtout.compareTo(dixCarreauAtout) == 0);
        Assert.assertTrue(valetCarreauAtout.compareTo(dixCarreauAtout) > 0);
        Assert.assertTrue(valetCarreauAtout.compareTo(dixCarreau) > 0);
        Assert.assertTrue(dixCarreauAtout.compareTo(dixCarreau) == 0);
        
    }


    /**
     * Teste la promotion de carte en atout
     *
     */
    public void testPromotion() {
        
        CouleurBelote dixCarreau = new CouleurBelote(CouleurBelote.DIX, CouleurBelote.CARREAU);
        AtoutBelote dixCarreauAtout = new AtoutBelote(dixCarreau);
        AtoutBelote neufCarreauAtout = new AtoutBelote(AtoutBelote.NEUF, AtoutBelote.CARREAU);
        
        Assert.assertTrue(dixCarreauAtout.compareTo(neufCarreauAtout) < 0);
        Assert.assertTrue(dixCarreauAtout.compareTo(dixCarreauAtout) == 0);
        Assert.assertTrue(neufCarreauAtout.compareTo(dixCarreauAtout) > 0);
        Assert.assertTrue(neufCarreauAtout.compareTo(dixCarreau) > 0);
        Assert.assertTrue(dixCarreauAtout.compareTo(dixCarreau) == 0);
        
    }

}
