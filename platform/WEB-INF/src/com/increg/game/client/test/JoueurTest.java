/*
 * Created on 24 avr. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.client.test;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.DataFormatException;

import com.increg.game.client.Joueur;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class JoueurTest extends TestCase {


    /**
     * Test le rechargement XML
     *
     */
    public void testReload() {
        Joueur aJoueur = new Joueur();
        
        String tstString = "<joueur pseudo=\"Test EG avec accent יטא\" avatar=\"http://www.anothersite.com/images/forum/avatar/1.gif\" profil=\"simple\" avatarLow=\"http://www.anothersite.com/images/forum/avatar/1.gif\" />";
        
        try {
            ByteArrayInputStream tstIs = new ByteArrayInputStream(tstString.getBytes("UTF8")); 
            aJoueur.reloadJoueur(tstIs);
        }
        catch (UnsupportedEncodingException e) {
            Assert.fail("Exception sur encoding UTF8");
        }
        catch (DataFormatException e) {
            Assert.fail("Exception sur format");
        }
        Assert.assertEquals(aJoueur.getPseudo(), "Test EG avec accent יטא");
        Assert.assertEquals(aJoueur.getAvatar().toString(), "http://www.anothersite.com/images/forum/avatar/1.gif");
        Assert.assertEquals(aJoueur.getPrivilege(), 0);
        
        // Passe 2
        tstString = "<joueur pseudo=\"Test EG sans accent avec &amp; &quot;\" avatar=\"http://www.anothersite.com/images/forum/avatar/1.gif\" profil=\"complet\" avatarLow=\"http://www.anothersite.com/images/forum/avatar/1.gif\" />";
        
        try {
            ByteArrayInputStream tstIs = new ByteArrayInputStream(tstString.getBytes("UTF8")); 
            aJoueur.reloadJoueur(tstIs);
        }
        catch (UnsupportedEncodingException e) {
            Assert.fail("Exception sur encoding UTF8");
        }
        catch (DataFormatException e) {
            Assert.fail("Exception sur format");
        }
        Assert.assertEquals(aJoueur.getPseudo(), "Test EG sans accent avec & \"");
        Assert.assertEquals(aJoueur.getAvatar().toString(), "http://www.anothersite.com/images/forum/avatar/1.gif");
        Assert.assertEquals(aJoueur.getPrivilege(), Joueur.MAX_PRIVILEGE);
    }

}
