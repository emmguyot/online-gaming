/*
 * Created on 9 juin 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.ui.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import com.increg.game.client.AireMainModel;

/**
 * @author Manu
 *
 * Gestion des popups présentant les smileys et les sons
 */
public class ImageComboBox extends JButton implements ActionListener, MouseListener, WindowListener {

    /**
     * Titre de la popup
     */
    protected String titre;

    /**
     * Images à représenter
     */
    protected String[] images;
    
    /**
     * Valeur pour chaque image
     */
    protected String[] valeur;
    
    /**
     * Taille en nb d'item du carré représentant les smileys
     */
    protected int tailleY;
    
    /**
     * Taille en nb d'item du carré représentant les smileys
     */
    protected int tailleX;
    
    /**
     * Listener d'événements
     */
    protected static HashMap myListener = new HashMap(2);

    /**
     * Popups présentant les items
     */
    protected static HashMap popups = new HashMap(2);

    /**
     * Contenus des popups
     */
    protected static HashMap contents = new HashMap(2);
    
    /**
     * Aire contenant le tout
     */
    protected AireMainModel aire;

    /**
     * Legende des icônes
     */
    protected static HashMap legende = new HashMap(2);

    /**
     * Timer permettant de fermer automatiquement la fenêtre des smileys
     */
    protected Timer timerClose;
    
    /**
     * Constructeur 
     * @param items Valeur des items
     * @param chImages URL des images correspondantes aux items
     * @param anAire aire mère
     * @param aTitre Titre de la popup
     */
    public ImageComboBox(String[] items, String[] chImages, AireMainModel anAire, String aTitre) {
        super();
        
        // Sauvegarde les attributs
        aire = anAire;
        valeur = items;
        titre = aTitre;
        images = chImages;

        tailleY = (int) Math.ceil(Math.sqrt(items.length));
        tailleX = (int) Math.ceil((0.0 + items.length) / tailleY);
        
        if (myListener.get(titre) == null) {
            myListener.put(titre, new Vector());
        }

        super.addActionListener(this);
    }

    /**
     * Reset de la fenêtre
     * @param items Valeur des items
     * @param chImages URL des images correspondantes aux items
     */
    public void reset(String[] items, String[] chImages) {
        valeur = items;
        images = chImages;
        if (contents.get(titre) != null) {

            JPanel aContent = (JPanel) contents.get(titre);
            aContent.removeAll(); 

            for (int i = 0; i < images.length; i++) {
    
                JLabel aLabel = new JLabel();
                aLabel.setVerticalAlignment(SwingConstants.CENTER);
                aLabel.setHorizontalAlignment(SwingConstants.CENTER);
                aLabel.setToolTipText(valeur[i]);
                try {
                    aLabel.setIcon(aire.getImageIcon(images[i]));
                }
                catch (MalformedURLException e1) {
                    e1.printStackTrace();
                }                
                aContent.add(aLabel);
                aLabel.addMouseListener(this);
            }
            
            aContent.invalidate();
            JFrame popup = (JFrame) popups.get(titre);

            popup.pack();           
        }
    }
    
    /**
     * Fermeture du tout
     */
    public void dispose() {
        if (popups.get(titre) != null) {
            JFrame popup = (JFrame) popups.get(titre);
            popup.removeWindowListener(this);
            
            if (popup.getWindowListeners().length == 0) {
                // Plus personne n'écoute : Destruction
                popup.removeAll();
                popup.dispose();
                popups.remove(titre);
                contents.remove(titre);
            }
            if ((timerClose != null) && (timerClose.isRunning())) {
            	timerClose.stop();
            }
        }
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        // Clic sur le bouton
        if (contents.get(titre) == null) {
            // Initialise la popup
            JPanel aContent = new JPanel(new GridLayout(tailleY, tailleX));
            
            for (int i = 0; i < images.length; i++) {
    
                JLabel aLabel = new JLabel();
                aLabel.setVerticalAlignment(SwingConstants.CENTER);
                aLabel.setHorizontalAlignment(SwingConstants.CENTER);
                aLabel.setToolTipText(valeur[i]);
                try {
                    aLabel.setIcon(aire.getImageIcon(images[i]));
                }
                catch (MalformedURLException e1) {
                    e1.printStackTrace();
                }                
                aContent.add(aLabel);
                aLabel.addMouseListener(this);
            }
            
            aContent.setBackground(Color.WHITE);
            contents.put(titre, aContent);
        }
        if (popups.get(titre) == null) {
            // Affiche la popup
            JPanel panelComplet = new JPanel(new BorderLayout());
            panelComplet.add((JPanel) contents.get(titre), BorderLayout.CENTER);
            JLabel aLegende = new JLabel(" ");
            aLegende.setHorizontalAlignment(SwingConstants.CENTER);
            panelComplet.add(aLegende, BorderLayout.SOUTH);
            panelComplet.setBackground(Color.WHITE);
            legende.put(titre, aLegende);

            JFrame aPopup = new JFrame(titre);
            aPopup.setContentPane(panelComplet);
            aPopup.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            aPopup.addWindowListener(this);
            aPopup.pack(); 
            aPopup.setResizable(false);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            aPopup.setLocation(screenSize.width - aPopup.getWidth(), 0);

            popups.put(titre, aPopup);
        }
        
        // Initialise le timer qui ferme automatiquement la fenêtre
        if (timerClose == null) {
            // Chargement pour la première fois de la tempo de ramassage
        	// TODO : Changer le mode de configuration
            ResourceBundle resConfig = ResourceBundle.getBundle("configAire");
            int tempoClose = Integer.parseInt(resConfig.getString("tempoPopup"));
	        timerClose = new Timer(tempoClose, this);
	        timerClose.setRepeats(false);
	        timerClose.setCoalesce(true);
        }
        if (timerClose.isRunning()) {
        	timerClose.stop();
        }

        if (((JFrame) popups.get(titre)).isVisible()) {
            ((JFrame) popups.get(titre)).setVisible(false);
        }
        else {
            ((JFrame) popups.get(titre)).setVisible(true);
            // Au cas où la fenêtre a été mise en icône
            ((JFrame) popups.get(titre)).setExtendedState(JFrame.NORMAL);
            timerClose.start();
        }
    }

    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent e) {
        
        if (contents.get(titre) != null) {
            for (int i = 0; i < ((Vector) myListener.get(titre)).size(); i++) {
                ActionListener aListener = (ActionListener) ((Vector) myListener.get(titre)).get(i);

                aListener.actionPerformed(new ActionEvent(this, e.getID(), ((JLabel) e.getSource()).getToolTipText()));
            }
            timerClose.restart();
        }
    }

    /**
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e) {
    }

    /**
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent e) {
        if (legende.get(titre) != null) {
            ((JLabel) legende.get(titre)).setText(((JLabel) e.getSource()).getToolTipText());
        }
    }

    /**
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent e) {
    }

    /**
     * @see javax.swing.AbstractButton#addActionListener(java.awt.event.ActionListener)
     */
    public void addActionListener(ActionListener l) {
        ((Vector) myListener.get(titre)).add(l);
    }

    /**
     * @see java.awt.Container#removeAll()
     */
    public void removeAll() {
        super.removeActionListener(this);
        if (popups.get(titre) != null) {
            JFrame popup = (JFrame) popups.get(titre);
            popup.removeWindowListener(this);
            if (contents.get(titre) != null) {
                JPanel aContent = (JPanel) contents.get(titre);
            
                Component[] tabLabel = aContent.getComponents();
                for (int iLabel = 0; iLabel < tabLabel.length; iLabel++) {
                    JLabel aLabel = (JLabel) tabLabel[iLabel];
                    for (int iListener = 0; iListener < aLabel.getMouseListeners().length; iListener++) {
                        MouseListener aListener = aLabel.getMouseListeners()[iListener];
                        if (aListener instanceof ImageComboBox) {
                            aLabel.removeMouseListener(aListener);
                            iListener--;
                        }
                    }
                    aLabel.addMouseListener(this);
                }
            }
            int nbListener = 0;
            for (int i = 0; i < popup.getWindowListeners().length; i++) {
                if (popup.getWindowListeners()[i] instanceof ImageComboBox) {
                    nbListener++;
                }
            }
            if (nbListener == 0) {
                popup.removeAll();
                popup.dispose();
                popups.remove(titre);
                contents.remove(titre);
            }
        }
        super.removeAll();
    }

    /**
     * @see javax.swing.AbstractButton#removeActionListener(java.awt.event.ActionListener)
     */
    public void removeActionListener(ActionListener l) {
        ((Vector) myListener.get(titre)).remove(l);
    }

    /**
     * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
     */
    public void windowOpened(WindowEvent e) {
    }

    /**
     * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
     */
    public void windowClosing(WindowEvent e) {
    	synchronized (popups) {
	        if (e.getSource() == popups.get(titre)) {
	            ((JFrame) popups.get(titre)).removeWindowListener(this);
	            popups.remove(titre);
	        }
    	}
    }

    /**
     * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
     */
    public void windowClosed(WindowEvent e) {
    }

    /**
     * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
     */
    public void windowIconified(WindowEvent e) {
    }

    /**
     * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
     */
    public void windowDeiconified(WindowEvent e) {
    }

    /**
     * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
     */
    public void windowActivated(WindowEvent e) {
    }

    /**
     * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
     */
    public void windowDeactivated(WindowEvent e) {
    }

    /**
     * Positionne celui qui écoute les événements à cet instant
     * @param l Listener actif
     */
    public void setActiveListener(ActionListener l) {
        if (contents.get(titre) != null) {
            JPanel aContent = (JPanel) contents.get(titre);
            
            Component[] tabLabel = aContent.getComponents();
            for (int iLabel = 0; iLabel < tabLabel.length; iLabel++) {
                JLabel aLabel = (JLabel) tabLabel[iLabel];
                for (int iListener = 0; iListener < aLabel.getMouseListeners().length; iListener++) {
                    MouseListener aListener = aLabel.getMouseListeners()[iListener];
                    if (aListener instanceof ImageComboBox) {
                        aLabel.removeMouseListener(aListener);
                        iListener--;
                    }
                }
                aLabel.addMouseListener(this);
            }
            
        }
    }

}
