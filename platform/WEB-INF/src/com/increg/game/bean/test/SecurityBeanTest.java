/*
 * Created on 18 avr. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.bean.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.increg.game.bean.SecurityBean;

import junit.framework.TestCase;
import junit.framework.Assert;

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
    }

    /**
     * Vérification que le calcul du CRC est correct
     *
     */
    public void testAfficheCRC() {
        
        SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
        
        String chaine = "Le Club des Beloteux vous accueille" + format.format(Calendar.getInstance().getTime());
        
        System.out.println("InCrEG = " + SecurityBean.calcCRC("InCrEG" + chaine));
        System.out.println("InCrEG2 = " + SecurityBean.calcCRC("InCrEG2" + chaine));
        System.out.println("InCrEG3 = " + SecurityBean.calcCRC("InCrEG3" + chaine));
        System.out.println("InCrEG4 = " + SecurityBean.calcCRC("InCrEG4" + chaine));
    }

}
