/*
 * Created on 24 avr. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.client.belote.test;

import com.increg.game.client.belote.CouleurBelote;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CouleurBeloteTest extends TestCase {


    /**
     * Teste la comparaison de carte
     *
     */
    public void testComparaison() {
        
        CouleurBelote dixCarreau = new CouleurBelote(CouleurBelote.DIX, CouleurBelote.CARREAU);
        CouleurBelote valetCarreau = new CouleurBelote(CouleurBelote.VALET, CouleurBelote.CARREAU);
        
        Assert.assertTrue(dixCarreau.compareTo(valetCarreau) > 0);
        Assert.assertTrue(dixCarreau.compareTo(dixCarreau) == 0);
        Assert.assertTrue(valetCarreau.compareTo(dixCarreau) < 0);
        
    }

}
