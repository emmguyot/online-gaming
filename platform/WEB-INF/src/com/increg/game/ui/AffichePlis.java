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
 * Affiche le dernier plis ramassé
 */
public class AffichePlis extends JDialog implements ActionListener {

    /**
     * Plis à afficher
     */
    protected List plis;
    
    /**
     * Tapis de jeu
     */
    protected CarteComponent[] cartes;

    /**
     * Salle concernée pour cet affichage
     */
    protected SalleModel salle;
    
    /**
     * Timer de fermeture automatique
     */
    protected Timer autoClose;
    
    /**
     * Constructeur
     * @param parent Fenêtre parente
     * @param titre Titre de la fenêtre
     * @param aPlis plis à afficher
     * @param aSalle Salle concernée
     */
    public AffichePlis(Frame parent, String titre, List aPlis, SalleModel aSalle) {
        // non Modale
        super(parent, false);
        
        // Sauvegarde les attributs
        plis = aPlis;
        salle = aSalle;

        setTitle(titre);
        getContentPane().setLayout(null);
        
        // Ajoute les éléments à la fenêtre
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
        
        // Boucle tant que les cartes n'ont pas pu être mises
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
     * Ajoute les cartes affichées
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
            // Accès concurent : Ignore l'erreur en recommançant un peu plus tard
            
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
