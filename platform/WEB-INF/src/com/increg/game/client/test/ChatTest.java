/*
 * Created on 24 avr. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.client.test;

import com.increg.game.client.Chat;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ChatTest extends TestCase {


    /**
     * Teste l'inversion de balise
     *
     */
    public void testReload() {
        Chat aChat = new Chat();
        
        aChat.setStyle("");
        Assert.assertEquals(aChat.getStyle(false), "");
        aChat.setStyle("<b>");
        Assert.assertEquals(aChat.getStyle(false), "</b>");
        aChat.setStyle("<font color=\"blue\">");
        Assert.assertEquals(aChat.getStyle(false), "</font>");
        aChat.setStyle("<font color=\"blue\"><b>");
        Assert.assertEquals(aChat.getStyle(false), "</b></font>");
        aChat.setStyle("<b><font color=\"blue\">");
        Assert.assertEquals(aChat.getStyle(false), "</font></b>");
    }

}
