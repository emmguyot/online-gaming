/*
 * Created on 3 juin 2003
 *
 */
package com.increg.game.ui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.Timer;

import com.increg.game.client.Couleur;
import com.increg.game.client.SalleModel;
import com.increg.game.ui.component.CarteComponent;

/**
 * @author Manu
 *
 * Affiche le dernier plis ramass�
 */
public class AffichePlis extends JDialog implements ActionListener {

    /**
     * Plis � afficher
     */
    protected List plis;
    
    /**
     * Tapis de jeu
     */
    protected CarteComponent[] cartes;

    /**
     * Salle concern�e pour cet affichage
     */
    protected SalleModel salle;
    
    /**
     * Timer de fermeture automatique
     */
    protected Timer autoClose;
    
    /**
     * Constructeur
     * @param parent Fen�tre parente
     * @param titre Titre de la fen�tre
     * @param aPlis plis � afficher
     * @param aSalle Salle concern�e
     */
    public AffichePlis(Frame parent, String titre, List aPlis, SalleModel aSalle) {
        // non Modale
        super(parent, false);
        
        // Sauvegarde les attributs
        plis = aPlis;
        salle = aSalle;

        setTitle(titre);
        getContentPane().setLayout(null);
        
        // Ajoute les �l�ments � la fen�tre
        addEveryComponents();
        setSize(200, 150);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        setResizable(false);

        autoClose = new Timer(4000, this);
        autoClose.setRepeats(false);
        autoClose.start();
    }

    /**
     * Ajoute tous les composants d'affichage
     *
     */
    public void addEveryComponents() {
        
        // Boucle tant que les cartes n'ont pas pu �tre mises
        // Avec un max en cas de pepin
        int nbEssai = 0;
        while ((nbEssai < 5) && (!addCartes())) {
            nbEssai++;
            try {
                Thread.sleep(200);
            }
            catch (InterruptedException e) {
                // Ignore
                e.printStackTrace();
            }
        }
        getContentPane().setBackground(salle.getSkinConfig().getRBoutonGlobalColor());
    }

    /**
     * Ajoute les cartes affich�es
     * @return Indicateur si le traitement a pu aller jusqu'au bout
     */
    private boolean addCartes() {
        
        boolean res = false;
        
        CarteComponent.setHauteurCarte(salle.getSkinConfig().getSCarte().height);
        CarteComponent.setLargeurCarte(salle.getSkinConfig().getSCarte().width);

        try {
            int taille = plis.size();
            cartes = new CarteComponent[taille];
            
            for (int i = 0; i < taille; i++) {
                cartes[i] = new CarteComponent(getContentPane(), (Couleur) plis.get(i), 
                                            98,
                                            210,
                                            salle.getSkinConfig().getSAlphaJeu() * (1.5 - i),
                                            salle.getSkinConfig().getSRayonJeu());
                try {
                    if (plis.get(i) != null) {
                        cartes[i].setImageCarte(salle.getRelativeImage("/images/" + ((Couleur) plis.get(i)) + ".gif"));
                    }
                }
                catch (MalformedURLException e) {
                    salle.getLogger().severe("Chemin de la carte invalide : " + "/images/" + ((Couleur) plis.get(i)) + ".gif");
                }
                cartes[i].setFace(true);
                cartes[i].setVisible(true);
                getContentPane().add(cartes[i]);
            }
            res = true;
        }
        catch (ConcurrentModificationException e) {
            // Acc�s concurent : Ignore l'erreur en recomman�ant un peu plus tard
            
        }
        
        return res;
        
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == autoClose) {
            dispose();
        }
    }
}
