/*
 * Created on 2 juin 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.ui.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

/**
 * @author Manu
 *
 * Sphère indiquant le donneur, le maitre et le joueur
 */
public class SphereComponent extends JComponent {

    /**
     * Parent de ce composant
     */
    private JComponent parent;

    /**
     * Nombre de joueurs
     */
    private int nbJoueur;

    /**
     * Fond représentant la partie graphiquement riche
     */
    protected Image fond;

    /**
     * Centre de la sphere
     */
    protected Point2D centre;

    /**
     * Offset pour centrer les lettres
     */
    protected double offY;
    /**
     * Offset pour centrer les lettres
     */
    protected double offDX;
    /**
     * Offset pour centrer les lettres
     */
    protected double offMX;
    /**
     * Angle entre 2 joueurs
     */
    protected double delta;

    /**
     * Aiguille indiquant le joueur
     */
    protected Image aiguille;
    
    /**
     * Fonte à utiliser pour le texte 
     */
    protected Font textFont;
    /**
     * Couleur à utiliser pour le texte du donneur quand sélectionné
     */
    protected Color textColorDSel;
    /**
     * Couleur à utiliser pour le texte du maitre quand sélectionné
     */
    protected Color textColorMSel;
    /**
     * Couleur à utiliser pour le texte normal
     */
    protected Color textColor;
    
    /**
     * Donneur à afficher
     */
    protected int donneur;
    /**
     * Maitre à afficher
     */
    protected int maitre;
    /**
     * Jouer à afficher
     */
    protected int joueur;
    
    /**
     * @param aParent parent graphique
     * @param aNbJoueur nombre de joueurs
     */
    public SphereComponent(JComponent aParent, int aNbJoueur) {
        // Sauvegarde les attributs
        parent = aParent;
        nbJoueur = aNbJoueur;
        
        // Initialise les autres
        delta = 2 * Math.PI / nbJoueur; 

        // Init des éléments graphiques
        setOpaque(false);
    }

    /**
     * Positionne l'état à afficher
     * @param aDonneur Donneur
     * @param aMaitre Maitre
     * @param aJoueur Joueur
     */
    public void setState(int aDonneur, int aMaitre, int aJoueur) {
        
        donneur = aDonneur;
        maitre = aMaitre;
        joueur = aJoueur;
        
    }
    
    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Point2D p;
        
        /**
         * Calcule le centre qu'une fois
         */        
        if (centre == null) {
            centre = new Point2D.Double((double) getSize().width / 2, (double) getSize().height / 2);
            
            // Calcule les infos pour centrer les lettres
            FontRenderContext frc = g2.getFontRenderContext();
            Rectangle2D dBounds = textFont.getStringBounds("d", frc);
            Rectangle2D mBounds = textFont.getStringBounds("m", frc);
            LineMetrics metrics = textFont.getLineMetrics("d", frc);
            
            offY = metrics.getAscent() / 2.0 - 1.0;
            offDX = dBounds.getWidth() / 2.0;
            offMX = mBounds.getWidth() / 2.0;
        }

        g2.drawImage(fond, 0, 0, this);
        
        g2.setFont(textFont);
        g2.setColor(textColor);
        for (int i = 0; i < nbJoueur; i++) {
            if (i != donneur) {
                p = getPosition(i, 31);
                g2.drawString("d", (float) (p.getX() - offDX), (float) (p.getY() + offY));
            }
        }
        if (donneur >= 0) {
            p = getPosition(donneur, 31);
            g2.setColor(textColorDSel);
            g2.drawString("d", (float) (p.getX() - offDX), (float) (p.getY() + offY));
        }
        
        g2.setColor(textColor);
        for (int i = 0; i < nbJoueur; i++) {
            if (i != maitre) {
                p = getPosition(i, 15);
                g2.drawString("m", (float) (p.getX() - offMX), (float) (p.getY() + offY));
            }
        }
        if (maitre >= 0) {
            p = getPosition(maitre, 15);
            g2.setColor(textColorMSel);
            g2.drawString("m", (float) (p.getX() - offMX), (float) (p.getY() + offY));
        }
        
        g2.rotate(joueur * delta, centre.getX(), centre.getY());
        g2.drawImage(aiguille, 0, 0, this);
        g2.rotate(-joueur * delta, centre.getX(), centre.getY());
    }    
    
    /**
     * Calcule la position des lettres 
     * @param aJoueur Joueur concerné
     * @param rayon rayon pour positionner la lettre
     * @return Point de position
     */
    protected Point2D getPosition(int aJoueur, int rayon) {
        
        double x, y;
        
        x = centre.getX() - rayon * Math.sin(aJoueur * delta);
        y = centre.getY() + rayon * Math.cos(aJoueur * delta);
        
        return new Point2D.Double(x, y);
    }
    
    /**
     * @return Aiguille indiquant le joueur
     */
    public Image getAiguille() {
        return aiguille;
    }

    /**
     * @return Fond représentant la partie graphiquement riche
     */
    public Image getFond() {
        return fond;
    }

    /**
     * @param image Aiguille indiquant le joueur
     */
    public void setAiguille(Image image) {
        aiguille = image;
    }

    /**
     * @param image Fond représentant la partie graphiquement riche
     */
    public void setFond(Image image) {
        fond = image;
    }

    /**
     * @return Fonte à utiliser pour le texte 
     */
    public Font getTextFont() {
        return textFont;
    }

    /**
     * @param font Fonte à utiliser pour le texte 
     */
    public void setTextFont(Font font) {
        textFont = font;
    }

    /**
     * @return Couleur à utiliser pour le texte normal
     */
    public Color getTextColor() {
        return textColor;
    }

    /**
     * @return Couleur à utiliser pour le texte du donneur quand sélectionné
     */
    public Color getTextColorDSel() {
        return textColorDSel;
    }

    /**
     * @return Couleur à utiliser pour le texte du maitre quand sélectionné
     */
    public Color getTextColorMSel() {
        return textColorMSel;
    }

    /**
     * @param color Couleur à utiliser pour le texte normal
     */
    public void setTextColor(Color color) {
        textColor = color;
    }

    /**
     * @param color Couleur à utiliser pour le texte du donneur quand sélectionné
     */
    public void setTextColorDSel(Color color) {
        textColorDSel = color;
    }

    /**
     * @param color Couleur à utiliser pour le texte du maitre quand sélectionné
     */
    public void setTextColorMSel(Color color) {
        textColorMSel = color;
    }

}
