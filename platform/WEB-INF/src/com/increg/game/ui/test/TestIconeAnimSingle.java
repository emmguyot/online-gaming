/*
 * Created on 15 mai 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.ui.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageObserver;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TestIconeAnimSingle {

    /**
     * Images animées
     */
    protected String singleImages = "http://www.increg.com/belote/images/252big.gif";
    /**
     * .
     *
     */
    public TestIconeAnimSingle () {

        int tailleY = 5;
        int tailleX = 5;
        
        JPanel aContent = new JPanel(new GridLayout(tailleY, tailleX));
            
        ImageIcon ic = null;
        try {
            ic = new ImageIcon(new URL(singleImages));
        }
        catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
                        
        for (int i = 0; i < tailleX * tailleY; i++) {
    
            JLabel aLabel = new JLabel();
            CropImageFilter cif = new CropImageFilter(59* (i % tailleX), 53* (i / tailleX), 59, 53);
            FilteredImageSource fis = new FilteredImageSource(ic.getImage().getSource(), cif);
            aLabel.setIcon(new ImageIcon(aLabel.createImage(fis)));
            aLabel.setMaximumSize(new Dimension(59, 53));
            aLabel.setMinimumSize(aLabel.getMaximumSize());
            aLabel.setPreferredSize(aLabel.getMaximumSize());
            aLabel.setSize(aLabel.getMaximumSize());
            aContent.add(aLabel);
            ic.setImageObserver(aLabel);
        }
            
        aContent.setBackground(Color.WHITE);

        // Affiche la popup
        JFrame aPopup = new JFrame("TestIconeAnimSingle");
        aPopup.setContentPane(aContent);
        aPopup.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        aPopup.pack(); 
        aPopup.setResizable(false);

        aPopup.setVisible(true);
    }
    /**
     * @see java.lang.Runnable#run()
     */
    public static void main(String[] q) {
        TestIconeAnimSingle tst = new TestIconeAnimSingle(); 
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
        
        // Avec Découpage en 25 labels
        // W98 : 51410 avec 45% de la CPU dont 3 thread : 10%, 17%, 15%
    }

    class MyLabel extends JComponent implements ImageObserver {
        
        boolean firstPaint = true;
        
        int labelWidth;
        
        int labelHeight;

        ImageIcon internalImage;
        int offsetX;
        int offsetY;        
        /**
         * 
         */
        public MyLabel(int width, int height) {
            super();
            labelHeight = height;
            labelWidth = width;
            setMinimumSize(new Dimension(width, height));
            setSize(new Dimension(width, height));
            setMaximumSize(new Dimension(width, height));
            setPreferredSize(new Dimension(width, height));
        }

        /**
         * @param ic
         */
        public void setIcon(ImageIcon ic, int offX, int offY) {
            internalImage = ic;
            offsetX = offX * labelHeight;
            offsetY = offY * labelHeight;
        }

        
        /**
         * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
         */
        protected void paintComponent(Graphics g) {

            g.setClip(offsetX, offsetY, labelWidth, labelHeight);
            g.drawImage(internalImage.getImage(), 0, 0, null);
        }

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
