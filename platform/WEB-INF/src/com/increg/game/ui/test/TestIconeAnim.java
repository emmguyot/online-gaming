/*
 * Created on 15 mai 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.ui.test;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TestIconeAnim {

    /**
     * Images animées
     */
    protected String[] images = {
        "http://www.increg.com/aire/smileys/r_s_clap.gif",
        "http://www.increg.com/aire/smileys/s_thumbup.gif",
        "http://www.increg.com/aire/smileys/t_thumbdown.gif",
        "http://www.increg.com/aire/smileys/m_yes.gif",
        "http://www.increg.com/aire/smileys/n_no.gif",
        "http://www.increg.com/aire/smileys/a_plain.gif",
        "http://www.increg.com/aire/smileys/b_wink.gif",
        "http://www.increg.com/aire/smileys/c_laugh.gif",
        "http://www.increg.com/aire/smileys/d_smily_tooth.gif",
        "http://www.increg.com/aire/smileys/f_eyebrows.gif",
        "http://www.increg.com/aire/smileys/g_chewingum.gif",
        "http://www.increg.com/aire/smileys/h_angry.gif",
        "http://www.increg.com/aire/smileys/i_angry_steaming.gif",
        "http://www.increg.com/aire/smileys/j_sad.gif",
        "http://www.increg.com/aire/smileys/k_crying.gif",
        "http://www.increg.com/aire/smileys/l_bhuhhh.gif",
        "http://www.increg.com/aire/smileys/o_smokin.gif",
        "http://www.increg.com/aire/smileys/p_stupid.gif",
        "http://www.increg.com/aire/smileys/q_plaster.gif",
        "http://www.increg.com/aire/smileys/u_thinking02y.gif",
        "http://www.increg.com/aire/avatar/252.gif"
        //"http://www.increg.com/belote/images/252big.gif"
        };
    /**
     * .
     *
     */
    public TestIconeAnim () {

        int tailleY = (int) Math.ceil(Math.sqrt(images.length));
        int tailleX = (int) Math.ceil((0.0 + images.length) / tailleY);
        
        JPanel aContent = new JPanel(new GridLayout(tailleY, tailleX));
            
        for (int i = 0; i < images.length; i++) {
    
            MyLabel aLabel = new MyLabel();
            aLabel.setVerticalAlignment(SwingConstants.CENTER);
            aLabel.setHorizontalAlignment(SwingConstants.CENTER);
            try {
                ImageIcon ic = new ImageIcon(new URL(images[i]));
                aLabel.setIcon(ic);
                ic.setImageObserver(aLabel);
            }
            catch (MalformedURLException e1) {
                e1.printStackTrace();
            }                
            aContent.add(aLabel);
        }
            
        aContent.setBackground(Color.WHITE);

        // Affiche la popup
        JFrame aPopup = new JFrame("TestIconeAnim");
        aPopup.setContentPane(aContent);
        aPopup.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        aPopup.pack(); 
        aPopup.setResizable(false);

        aPopup.show();
    }
    /**
     * @see java.lang.Runnable#run()
     */
    public static void main(String[] q) {
        TestIconeAnim tst = new TestIconeAnim(); 
        tst.getClass();
        
        long debut = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            Thread.yield();
            double calcul = 1.0 * i * Math.sqrt(i) / Math.log(i + 1);
            
            calcul++; 
        }
        System.out.println(System.currentTimeMillis() - debut);
        // Exemple (sans débuggage et sous eclipse : 13570, 13790
        // Avec doubleBuffered = false : 14190
        // Avec Repaint en commentaire : 12809
        
        // Sous W98 : 50640 pour l'affichage animé
        // Globalement l'animation prends 25% de la CPU, dont 1 thread à 13% (il retombe à 0 quand la fenêtre est en icône)
        // Les autres threads tournent même si en icone
        // Pas d'effet, si fenêtre cachée
        // Avec Repaint en commentaire : 45700 et 11% de la CPU (NB: Avatars fixes = 0%)

        // Avec un gros gif : Même surface
        // 12297
        // W98 : 43670 et 16% de la CPU dont 1 thread à 13%
    }

    class MyLabel extends JLabel {
        
        boolean firstPaint = true;
        /**
         * @see java.awt.image.ImageObserver#imageUpdate(java.awt.Image, int, int, int, int, int)
         */
        public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {

            // Repaint when done or when new pixels arrive:
            if ((infoflags & FRAMEBITS) != 0) {
                if (firstPaint) {
                    repaint(0);
                    firstPaint = false;
                } 
            }
            if ((infoflags & (ALLBITS)) != 0) {
                repaint(0);
            }
            return ((infoflags & ALLBITS) == 0);
        }

    }
}
