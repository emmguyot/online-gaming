/*
 * Created on 15 mai 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.ui.test;

import java.awt.Color;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TestIcone extends JDialog {

    /**
     * .
     *
     */
    public TestIcone () {
        ImageIcon ic = null;
        try  {
            ic = new ImageIcon(new URL("http://www.increg.com/belote/images/cadenas.gif"));
        }
        catch (MalformedURLException e) {
            System.err.println("Err");;
        }
        
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);
        JLabel labelCadenas = new JLabel();
        if (ic.getImageLoadStatus() == MediaTracker.COMPLETE) { 
            labelCadenas.setIcon(ic);
        }
        labelCadenas.setBounds(new Rectangle(10, 10, 5, 5));
        getContentPane().add(labelCadenas);
        
        setSize(50, 50);
        setVisible(true);
    }
    /**
     * @see java.lang.Runnable#run()
     */
    public static void main(String[] q) {
        TestIcone tst = new TestIcone(); 
        tst.getClass();
    }


    
}
