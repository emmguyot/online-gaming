/*
 * Created on 27 avr. 2003
 *
 * Affiche une table contenant en ligne une image sur laquelle 
 * des �l�ments peuvent �tre plac�s (Text, Images, boutons, ...) 
 */
package com.increg.game.ui.component;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;

/**
 * @author Manu
 *
 * Affiche une table contenant en ligne une image sur laquelle 
 * des �l�ments peuvent �tre plac�s (Text, Images, boutons, ...) 
 */
public class ImageBasedTable extends JPanel {

    /**
     * Panel d'appartenance
     */
    protected JPanel parentPanel;

    /**
     * Nombre de lignes pr�sente dans la liste
     */    
    private int nbItem = 0;
    
    /**
     * Image de fond d'�cran non bufferis�e (car transparente)
     */
    private Image imageFd;
 
    /**
     * Constructeur
     * @param parent parent de ce contr�le
     * @param anImage Image de fond � utiliser pour une ligne
     */
    public ImageBasedTable(JPanel parent, Image anImage) {
        
        // Positionne les attributs
        parentPanel = parent;
        imageFd = anImage;
    }

    /**
     * Calcule l'offset Y pour l'afficahge 
     * @param numPartie partie concern�e
     * @return nombre de pixel de d�calage
     */    
    protected int getOffsetY(int numPartie) {
        Image singleImage = getImageFd();
        if (getHeight() > (1.5 * singleImage.getHeight(this))) {
            return numPartie * singleImage.getHeight(this);
        }
        else {
            return 0;
        }
    }

    /**
     * Calcule l'offset X pour l'afficahge 
     * @param numPartie partie concern�e
     * @return nombre de pixel de d�calage
     */    
    protected int getOffsetX(int numPartie) {
        Image singleImage = getImageFd();
        if (getWidth() > (1.5 * singleImage.getWidth(this))) {
            return numPartie * singleImage.getWidth(this);
        }
        else {
            return 0;
        }
            
    }

    /**
     * Affiche le composant comme il faut
     * @see java.awt.Component#paintComponent(java.awt.Graphics)
     */
    public void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;

        // Dessine les images lignes par lignes
        for (int i = 0; i < nbItem; i++) {
            g.drawImage(imageFd, getOffsetX(i), getOffsetY(i), this);
        }
        super.paintComponent(graphics);
    }

    /**
     * @return Nombre de lignes repr�sent�es
     */
    public int getNbItem() {
        return nbItem;
    }

    /**
     * @param i Nombre de lignes repr�sent�es
     */
    public void setNbItem(int i) {
        nbItem = i;
        Dimension taille = null;
        taille = new Dimension(getOffsetX(nbItem) + imageFd.getWidth(this), getOffsetY(nbItem) + imageFd.getHeight(this));
        setSize(taille);
        setPreferredSize(taille);
        setMaximumSize(taille);
        setMinimumSize(taille);
        // Il faudra redessiner tout �a
        invalidate();
    }

    /**
     * @return Image de fond
     */
    protected Image getImageFd() {
        return imageFd;
    }

    /**
     * @param image Image de fond (peut �tre null)
     */
    protected void setImageFd(Image image) {
        imageFd = image;
    }

}
