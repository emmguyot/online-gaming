/*
 * Created on 20 mai 2003
 *
 */
package com.increg.game.test;

import com.increg.game.bean.test.SecurityBeanTest;
import com.increg.game.client.belote.test.AnnonceSequenceTest;
import com.increg.game.client.belote.test.AtoutBeloteTest;
import com.increg.game.client.belote.test.CouleurBeloteTest;
import com.increg.game.client.belote.test.EtatPartieBeloteTest;
import com.increg.game.client.belote.test.JeuBeloteTest;
import com.increg.game.client.belote.test.PartieBeloteClassiqueTest;
import com.increg.game.client.belote.test.PartieBeloteModerneTest;
import com.increg.game.client.test.ChatTest;
import com.increg.game.client.test.JoueurTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Manu
 *
 * Enchaine tous les tests de com.increg.game
 */
public class GameSuite {

    /**
     * 
     * @return .
     */
    public static Test suite() {

        TestSuite suite = new TestSuite("Test for com.increg.game.**");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(SecurityBeanTest.class));
        suite.addTest(new TestSuite(ChatTest.class));
        suite.addTest(new TestSuite(JoueurTest.class));
        suite.addTest(new TestSuite(EtatPartieBeloteTest.class));
        suite.addTest(new TestSuite(JeuBeloteTest.class));
        suite.addTest(new TestSuite(CouleurBeloteTest.class));
        suite.addTest(new TestSuite(AtoutBeloteTest.class));
        suite.addTest(new TestSuite(PartieBeloteClassiqueTest.class));
        suite.addTest(new TestSuite(PartieBeloteModerneTest.class));
        suite.addTest(new TestSuite(AnnonceSequenceTest.class));
        //$JUnit-END$
        return suite;
    }

}
