/*
 * Created on 23 may 2003
 *
 */
package com.increg.game.ui.component;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.HashMap;

import javax.swing.JComponent;

import com.increg.game.client.Carte;

/**
 * @author Manu
 *
 * Classe représentant graphiquement une carte
 */
public class CarteComponent extends JComponent {

    /**
     * Hauteur des cartes
     */
    private static int hauteurCarte;
    /**
     * Largeur des cartes
     */
    private static int largeurCarte;
     
    /**
     * Carte à afficher
     */
    private Carte myCarte;
    
    /**
     * Orientation en radian (0 = Debout)
     */
    private double angle;
    
    /**
     * Faut-il mettre une marque à droite de la carte ?
     */
    private boolean marque;
    
    /**
     * Parent de ce composant
     */
    private Container parent;
    
    /**
     * Face visible ?
     */
    private boolean face;

    /**
     * Image à afficher pour la carte
     */
    private Image imageCarte;
    /**
     * Image à afficher pour le dos
     */
    private Image imageDos;

    /**
     * Les calculs ont été faits ?
     */
    private boolean computeDone;
    
    /**
     * Offset X de positionnement du coin en haut à gauche
     */
    private int offX;

    /**
     * Offset Y de positionnement du coin en haut à gauche
     */
    private int offY;

    /**
     * Coordonnées absolues des 4 coins de la carte
     */    
    private Point pointA;
    /**
     * Coordonnées absolues des 4 coins de la carte
     */    
    private Point pointB;
    /**
     * Coordonnées absolues des 4 coins de la carte
     */    
    private Point pointC;
    /**
     * Coordonnées absolues des 4 coins de la carte
     */    
    private Point pointD;

    /**
     * Polygone avec les 4 points
     */
    protected Polygon cartePoly;

    /**
     * Affichage de la carte en inverse vidéo ?
     */
    private boolean inverse;
        
    /* **************************************************************************** */
    
    /**
     * Constructeur par défaut
     * @param aParent Parent graphique
     * @param aCarte carte à afficher
     * @param anAngle Angle par rappor à l'axe vertical (radians)
     */
    public CarteComponent(JComponent aParent, Carte aCarte, double anAngle) {
        // Récupération des attributs
        parent = aParent;
        myCarte = aCarte;
        angle = anAngle;
        // Init des autres attributs
        face = true;
        inverse = false;
        
        // Init des éléments graphiques
        setOpaque(false);
        setDoubleBuffered(false);
    }

    /**
     * Constructeur avec coo polaire
     * @param aParent Parent graphique
     * @param aCarte carte à afficher
     * @param x0 Abcisse de l'origine du repère polaire
     * @param y0 Ordonnée de l'origine du repère polaire
     * @param anAngle Angle par rappor à l'axe vertical (radians)
     * @param rayon rayon du cercle où se posent les cartes
     */
    public CarteComponent(Container aParent, Carte aCarte, int x0, int y0, double anAngle, double rayon) {
        
        // Récupération des attributs
        parent = aParent;
        myCarte = aCarte;
        angle = anAngle;
        // Init des autres attributs
        face = true;
        
        // Init des éléments graphiques
        setOpaque(false);
        
        // Calcul la vraie position des cartes
        int h = (int) (0.5 + Math.abs(hauteurCarte * Math.cos(angle)) + Math.abs(largeurCarte * Math.sin(angle)));
        int w = (int) (0.5 + Math.abs(hauteurCarte * Math.sin(angle)) + Math.abs(largeurCarte * Math.cos(angle)));
        int y = (int) (0.5 + y0 - rayon * Math.cos(angle) - h + Math.abs((largeurCarte / 2) * Math.sin(angle)));
        int x = 0;
        if (angle >= 0) {
            // A droite 
            x = (int) (0.5 + x0 + rayon * Math.sin(angle) - (largeurCarte / 2) * Math.cos(angle));
        }
        else {
            // A gauche
            x = (int) (0.5 + x0 + rayon * Math.sin(angle) + hauteurCarte * Math.sin(angle) - (largeurCarte / 2) * Math.cos(angle));
        }
        
        setBounds(x, y, w, h);
        
        offX = 0;
        offY = 0;
        computeDone = false;

    }

    /**
     * @return Orientation en radian (0 = Debout)
     */
    public double getAngle() {
        return angle;
    }

    /**
     * @return Faut-il mettre une marque à droite de la carte ?
     */
    public boolean isMarque() {
        return marque;
    }

    /**
     * @return Carte à afficher
     */
    public Carte getMyCarte() {
        return myCarte;
    }

    /**
     * @param d Orientation en radian (0 = Debout)
     */
    public void setAngle(double d) {
        angle = d;
        computeDone = false;
    }

    /**
     * @param b Faut-il mettre une marque à droite de la carte ?
     */
    public void setMarque(boolean b) {
        marque = b;
    }

    /**
     * @param carte Carte à afficher
     */
    public void setMyCarte(Carte carte) {
        myCarte = carte;
    }

    /**
     * @return Face visible ?
     */
    public boolean isFace() {
        return face;
    }

    /**
     * @param b Face visible ?
     */
    public void setFace(boolean b) {
        face = b;
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        
        // Optimisation : ne calcule qu'une fois
        if (!computeDone) {
            offX = 0;
            offY = 0;
            if (angle > 0) {
                offX = (int) (hauteurCarte * Math.sin(angle));
            }
            else {
                offY = (int) (-largeurCarte * Math.sin(angle));
            }
            // Coordonnées des angles
            pointA = new Point(getBounds().x + offX, getBounds().y + offY);
            pointB = new Point(pointA.x + (int) (largeurCarte * Math.cos(angle)), pointA.y + (int) (largeurCarte * Math.sin(angle)));
            pointC = new Point(pointB.x - (int) (hauteurCarte * Math.sin(angle)), pointB.y + (int) (hauteurCarte * Math.cos(angle)));
            pointD = new Point(pointC.x - (int) (largeurCarte * Math.cos(angle)), pointC.y - (int) (largeurCarte * Math.sin(angle)));

            cartePoly = new Polygon();
            cartePoly.addPoint(pointA.x, pointA.y);
            cartePoly.addPoint(pointB.x, pointB.y);
            cartePoly.addPoint(pointC.x, pointC.y);
            cartePoly.addPoint(pointD.x, pointD.y);
                
            computeDone = true;
        }
        
        // Affichage
        //g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        
        HashMap renderOpts = new HashMap();
//        renderOpts.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
//       renderOpts.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        renderOpts.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        renderOpts.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
//        renderOpts.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2.setRenderingHints(renderOpts);
        
        g2.translate(offX, offY);
        g2.rotate(angle, 0, 0);
        // Affichage de la carte
        if (isFace()) {
            if (imageCarte != null) {
                try {
                    g2.drawImage(imageCarte, 0, 0, parent);
                    if (inverse) {
                        g2.setColor(new Color(0, 0, 0, 50));
                        g2.fillRect(0, 0, largeurCarte, hauteurCarte);
                    }
                }
                catch (Exception e) {
                    // Pb d'affichage
                    imageCarte = null;
                    e.printStackTrace();
                }
            } 
        }
        else {
            if (imageDos != null) {
                try {
                    g2.drawImage(imageDos, 0, 0, parent);
                }
                catch (Exception e) {
                    // Pb d'affichage
                    imageDos = null;
                    e.printStackTrace();
                }
            }
        }
        // Retour à la normale
        g2.rotate(-angle, 0, 0);
        g2.translate(-offX, -offY);
    }

    /**
     * @return Image à afficher pour la carte
     */
    public Image getImageCarte() {
        return imageCarte;
    }

    /**
     * @return Image à afficher pour le dos
     */
    public Image getImageDos() {
        return imageDos;
    }

    /**
     * @param image Image à afficher pour la carte
     */
    public void setImageCarte(Image image) {
        imageCarte = image;
        invalidate();
    }

    /**
     * @param image Image à afficher pour le dos
     */
    public void setImageDos(Image image) {
        imageDos = image;
        invalidate();
    }

    /**
     * @return Hauteur des cartes
     */
    public static int getHauteurCarte() {
        return hauteurCarte;
    }

    /**
     * @return Largeur des cartes
     */
    public static int getLargeurCarte() {
        return largeurCarte;
    }

    /**
     * @param i Hauteur des cartes
     */
    public static void setHauteurCarte(int i) {
        hauteurCarte = i;
    }

    /**
     * @param i Largeur des cartes
     */
    public static void setLargeurCarte(int i) {
        largeurCarte = i;
    }

    /**
     * Le point est sur la carte ?
     * @param point Point à tester
     * @return true si le point y est
     */
    public boolean isOnCarte(Point point) {
        
        // Que si la carte est visible
        if (isVisible()) {
            // Réponse possible que si les calcul ont été faits.
            if (computeDone) {
                return cartePoly.contains(point); 
            }
        }
        return false;
    }

    /**
     * @see java.awt.Component#setVisible(boolean)
     */
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
    }

    /**
     * @return Affichage de la carte en inverse vidéo ?
     */
    public boolean isInverse() {
        return inverse;
    }

    /**
     * @param b Affichage de la carte en inverse vidéo ?
     */
    public void setInverse(boolean b) {
        inverse = b;
    }

}
