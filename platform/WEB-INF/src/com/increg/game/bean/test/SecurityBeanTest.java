/*
 * Created on 18 avr. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.bean.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.increg.game.bean.GameSession;
import com.increg.game.bean.SecurityBean;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SecurityBeanTest extends TestCase {

    /**
     * Vérification que le calcul du CRC est correct
     *
     */
    public void testCalculCRC() {
        Assert.assertEquals(SecurityBean.calcCRC("TestEGLe Club des Beloteux vous accueille19042003"), "d4a6b8ec9c45759ac3c5f0173a9c5423");
        Assert.assertEquals(SecurityBean.calcCRC("JoeLe Club des Beloteux vous accueille03052003"), "717cb2d8e07d5544985e8f45c40698af");
        Assert.assertEquals("60d7309a1d854b7090a1cd1a82bf5e78", SecurityBean.calcCRC("TestEGMy Private PassPhrase19042003"));
        Assert.assertEquals("c66faa4ba9ab28cc15639af6c1e4db3c", SecurityBean.calcCRC("JoeKeep your secret secret03052003"));
    }

    /**
     * Vérification que le calcul du CRC est correct
     *
     */
    public void testAfficheCRC() {
        
        SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
        
        ResourceBundle res = ResourceBundle.getBundle(GameSession.DEFAULT_CONFIG);
        String chaine = res.getString("passPhrase") + format.format(Calendar.getInstance().getTime());
        
        System.out.println("InCrEG = " + SecurityBean.calcCRC("InCrEG" + chaine));
        System.out.println("InCrEG2 = " + SecurityBean.calcCRC("InCrEG2" + chaine));
        System.out.println("InCrEG3 = " + SecurityBean.calcCRC("InCrEG3" + chaine));
        System.out.println("InCrEG4 = " + SecurityBean.calcCRC("InCrEG4" + chaine));
    }

}
