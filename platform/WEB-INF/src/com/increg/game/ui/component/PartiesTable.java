/*
 * Created on 27 avr. 2003
 *
 */
package com.increg.game.ui.component;

import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.increg.game.client.AireMainModel;
import com.increg.game.client.Joueur;
import com.increg.game.client.belote.PartieBelote;
import com.increg.game.client.belote.PartieBeloteClassique;

/**
 * @author Manu
 *
 * Classe représentant l'intérieur de la liste de défilement :
 * <ul>
 * <li>Image de fond par ligne</li>
 * <li>Label d'identification de la table</li>
 * <li>Bouton pour Joindre</li>
 * <li>Nom et Avatar des joueurs déjà présents</li>
 * </ul> 
 */
public class PartiesTable extends ImageBasedTable implements ActionListener, MouseListener {

    /**
     * Chaine  
     */
    static final String CLASSIQUE = "Belote Classique";
    /**
     * Chaine  
     */
    static final String MODERNE = "Belote Moderne";
    /**
     * Chaine  
     */
    static final String AVEC_ANNONCE = "Avec annonces";
    /**
     * Chaine  
     */
    static final String SANS_ANNONCE = "Sans annonce";

    /* ***************************************
     * Attributs
     * *************************************** */

    /**
     * Model de l'aire qui contient les données
     */
    protected AireMainModel aire;
    
    /**
     * Liste des boutons Jouer de chaque partie : Chaque élément est un tableau de JButton
     */
    protected Vector jouerButton;
    
    /**
     * Texte représentant les pseudo des joueurs : Chaque élément est un tableau de JLabel
     */
    protected Vector pseudoLabel;
    
    /**
     * Icône représentant l'avatar des joueurs : Chaque élément est un tableau de JLabel
     */
    protected Vector avatarLabel;
    
    /**
     * Texte de présentation de la partie : Chaque élément est un tableau de 3 JLabel
     */
    protected Vector textePresent;

    /**
     * Label du cadenas des parties privées : Chaque élément est JLabel (ou null)
     */
    protected Vector cadenasLabel;

    /**
     * Label du tournoi des parties : Chaque élément est JLabel (ou null)
     */
    protected Vector tournoiLabel;

    /**
     * Image pour les parties lockées
     */
    protected ImageIcon cadenasImg;

    /**
     * Image pour les parties tournoi
     */
    protected ImageIcon tournoiImg;

    /**
     * Constructeur 
     * @param parent parent de ce contrôle
     * @param anImage Image de fond à utiliser pour une ligne
     * @param a Modèle de l'aire ==> Source de données pour refresh
     */
    public PartiesTable(JPanel parent, Image anImage, AireMainModel a) {

        super(parent, anImage);

        // alimente les attributs
        aire = a;

        // Initialise les attributs        
        jouerButton = new Vector();
        pseudoLabel = new Vector();
        avatarLabel = new Vector();
        textePresent = new Vector();
        cadenasLabel = new Vector();
        tournoiLabel = new Vector();
        
        // Positionnement en direct
        setLayout(null);
        
        try {
            cadenasImg = aire.getImageIcon(aire.getSkinConfig().getRImgCadenasListePartie());
        }
        catch (MalformedURLException e) {
            aire.getLogger().severe("URL du cadenas invalide");
        }
        
        try {
            tournoiImg = aire.getImageIcon(aire.getSkinConfig().getRImgTournoiListePartie());
        }
        catch (MalformedURLException e) {
            aire.getLogger().severe("URL du cadenas invalide");
        }
        
        addMouseListener(this);
    }
    
    /**
     * Mise à jour globale de la liste (à partir du model)
     */
    public void updateParties() {

        boolean actionDone = false;
                
        for (int i = 0; i < aire.getNbPartie(); i++) {
            JButton[] jouer;
            JLabel[] pseudo;
            JLabel[] avatar;
            JLabel[] texte;
            JLabel cadenas;
            JLabel tournoi;
            PartieBelote partie = (PartieBelote) aire.getPartie(i);
            
            if (i < getNbItem()) {
                // Simple mise à jour : Toutes les infos, car en cas de suppression, il peut y avoir décallage
                // récupère les éléments créés à la liste dans les attributs
                jouer = (JButton[]) jouerButton.get(i);
                avatar = (JLabel[]) avatarLabel.get(i);
                pseudo = (JLabel[]) pseudoLabel.get(i);
                cadenas = (JLabel) cadenasLabel.get(i);
                tournoi = (JLabel) tournoiLabel.get(i);
                
                fillTexte(partie, i);
                fillCadenas(partie, i);
                fillTournoi(partie, i);
               
                for (int j = 0; j < PartieBelote.NB_JOUEUR; j++) {
                    fillBoutonJouer(partie, i, j);
                    fillTextePseudo(partie, i, j);
                    fillTexteAvatar(partie, i, j);
                }
            }
            else {
                // Ajout d'un pavé
                jouer = new JButton[PartieBelote.NB_JOUEUR];
                pseudo = new JLabel[PartieBelote.NB_JOUEUR];
                avatar = new JLabel[PartieBelote.NB_JOUEUR];

                texte = getTexte(partie, i);
                add(texte[0]);
                add(texte[1]);
                add(texte[2]);
                cadenas = getCadenas(partie, i);
                if (cadenas != null) {
                    add(cadenas);
                } 
                tournoi = getTournoi(partie, i);
                if (tournoi != null) {
                    add(tournoi);
                } 
                for (int j = 0; j < PartieBelote.NB_JOUEUR; j++) {
                    jouer[j] = getBoutonJouer(partie, i, j);
                    pseudo[j] = getTextePseudo(partie, i, j);
                    avatar[j] = getTexteAvatar(partie, i, j);
                    add(jouer[j]);
                    add(pseudo[j]);
                    add(avatar[j]);
                }

                // Ajoute les éléments créés à la liste dans les attributs
                textePresent.add(texte);
                jouerButton.add(jouer);
                avatarLabel.add(avatar);
                pseudoLabel.add(pseudo);
                cadenasLabel.add(cadenas);
                tournoiLabel.add(tournoi);
                
                // Mise à jour des données
                fillTexte(partie, i);
                fillCadenas(partie, i);
                fillTournoi(partie, i);
                for (int j = 0; j < PartieBelote.NB_JOUEUR; j++) {
                    fillBoutonJouer(partie, i, j);
                    fillTextePseudo(partie, i, j);
                    fillTexteAvatar(partie, i, j);
                }
                
                actionDone = true;
            }
        }
        // Dispose les anciens éléments
        for (int i = aire.getNbPartie(); i < getNbItem(); i++) {
            
            // Utilise toujours l'indice aire.getNbPartie() car suppression au fur et à mesure
            for (int j = 0; j < PartieBelote.NB_JOUEUR; j++) {
                ((JButton[]) jouerButton.get(aire.getNbPartie()))[j].removeActionListener(this);
            }
            JButton[] jouer = (JButton[]) jouerButton.remove(aire.getNbPartie());
            JLabel[] pseudo = (JLabel[]) pseudoLabel.remove(aire.getNbPartie());
            JLabel[] avatar = (JLabel[]) avatarLabel.remove(aire.getNbPartie());
            JLabel[] texte = (JLabel[]) textePresent.remove(aire.getNbPartie());
            JLabel cadenas = (JLabel) cadenasLabel.remove(aire.getNbPartie());
            JLabel tournoi = (JLabel) tournoiLabel.remove(aire.getNbPartie());
            
            if (texte != null) {
                remove(texte[0]);
                remove(texte[1]);
                remove(texte[2]);
            }
            if (cadenas != null) {
                remove(cadenas);
            }
            if (tournoi != null) {
                remove(tournoi);
            }
            for (int j = 0; j < PartieBelote.NB_JOUEUR; j++) {
                if (jouer[j] != null) {
                    remove(jouer[j]);
                }
                if (pseudo[j] != null) {
                    remove(pseudo[j]);
                }
                if (avatar[j] != null) {
                    remove(avatar[j]);
                }
            }
            actionDone = true;
        }
        if (aire.getNbPartie() != getNbItem()) {
            setNbItem(aire.getNbPartie());
        }
        
        if (actionDone) {
            revalidate();
            repaint();
        }
    }

    /**
     * Création des labels présentant la partie
     * @param partie Partie concernée
     * @param numPartie emplacement de la partie dans la liste
     * @return Champs correspondant (tableau de 3)
     */
    protected JLabel[] getTexte(PartieBelote partie, int numPartie) {
        JLabel[] texte = new JLabel[3];
        
        // Texte de présentation de la partie
        for (int i = 0; i < texte.length; i++) {
            texte[i] = new JLabel();
            texte[i].setForeground(aire.getSkinConfig().getRTexteDsListePartieColor());
            texte[i].setHorizontalAlignment(SwingConstants.CENTER);
            
            texte[i].setFont(aire.getSkinConfig().getRListeFont());
            texte[i].setBounds(getOffsetX(numPartie) + aire.getSkinConfig().getRTexteDsListePartie().x,
                            getOffsetY(numPartie) + aire.getSkinConfig().getRTexteDsListePartie().y
                                + i * aire.getSkinConfig().getRTexteDsListePartie().height,
                            aire.getSkinConfig().getRTexteDsListePartie().width,
                            aire.getSkinConfig().getRTexteDsListePartie().height);
        }
        return texte;
    }

    /**
     * Remplissage des labels présentant la partie
     * @param partie Partie concernée
     * @param numPartie emplacement de la partie dans la liste
     */
    protected void fillTexte(PartieBelote partie, int numPartie) {
        
        JLabel[] texte = (JLabel[]) textePresent.get(numPartie);
        
        /**
         * Mise à jour que si nécessaire : pour éviter clignotement
         */
        if (partie instanceof PartieBeloteClassique) {
            if (!texte[0].getText().equals(CLASSIQUE)) {
                texte[0].setText(CLASSIQUE);
            }
        }
        else {
            if (!texte[0].getText().equals(MODERNE)) {
                texte[0].setText(MODERNE);
            }
        }
        if (!texte[1].getText().equals(partie.getTitre())) {
            texte[1].setText(partie.getTitre());
        }
        if (partie.isAnnonce()) {
            if (!texte[2].getText().equals(AVEC_ANNONCE)) {
                texte[2].setText(AVEC_ANNONCE); 
            }
        }
        else {
            if (!texte[2].getText().equals(SANS_ANNONCE)) {
                texte[2].setText(SANS_ANNONCE); 
            }
        }
    }

    /**
     * Génére l'image du cadenas si besoin
     * @param partie Partie concernée
     * @param numPartie emplacement de la partie dans la liste
     * @return Label du cadenas ou null
     */
    protected JLabel getCadenas(PartieBelote partie, int numPartie) {
        JLabel labelCadenas = new JLabel();
        
        // Construction
        Rectangle position = aire.getSkinConfig().getRCadenasListePartie();
        labelCadenas.setBounds(getOffsetX(numPartie) + position.x,
                        getOffsetY(numPartie) + position.y,
                        position.width,
                        position.height);

        return labelCadenas;
    }

    /**
     * Affiche ou pas l'image du cadenas si besoin
     * @param partie Partie concernée
     * @param numPartie emplacement de la partie dans la liste
     */
    protected void fillCadenas(PartieBelote partie, int numPartie) {
        JLabel labelCadenas = (JLabel) cadenasLabel.get(numPartie);
        
        if ((partie.isPrivate() && labelCadenas.getIcon() == null)) {
            // Partie privée
            // Affiche le cadenas 
            if (cadenasImg.getImageLoadStatus() == MediaTracker.COMPLETE) { 
                labelCadenas.setIcon(cadenasImg);
            }
        }
        else if ((!partie.isPrivate() && labelCadenas.getIcon() != null)) {
            labelCadenas.setIcon(null);
        }
    }

    /**
     * Génére l'image pour le tournoi si besoin
     * @param partie Partie concernée
     * @param numPartie emplacement de la partie dans la liste
     * @return Label du tournoi ou null
     */
    protected JLabel getTournoi(PartieBelote partie, int numPartie) {
        JLabel labelTournoi = new JLabel();
        
        // Construction
        Rectangle position = aire.getSkinConfig().getRTournoiListePartie();
        labelTournoi.setBounds(getOffsetX(numPartie) + position.x,
                        getOffsetY(numPartie) + position.y,
                        position.width,
                        position.height);

        return labelTournoi;
    }

    /**
     * Affiche ou pas l'image du tournoi si besoin
     * @param partie Partie concernée
     * @param numPartie emplacement de la partie dans la liste
     */
    protected void fillTournoi(PartieBelote partie, int numPartie) {
        JLabel labelTournoi = (JLabel) tournoiLabel.get(numPartie);
        
        boolean partieTournoi; 
        partieTournoi = (partie.getMyTournoi() != null) 
                        && (partie.getMyTournoi().getIdentifiant() > 0);
                        
        if (partieTournoi && (labelTournoi.getIcon() == null)) {
            // Partie tournoi
            // Affiche l'image du tournoi
            if (tournoiImg.getImageLoadStatus() == MediaTracker.COMPLETE) { 
                labelTournoi.setIcon(tournoiImg);
            }
        }
        else if ((!partieTournoi && labelTournoi.getIcon() != null)) {
            labelTournoi.setIcon(null);
        }
    }

    /**
     * Création du bouton pour joindre la partie
     * @param partie Partie concernée
     * @param numPartie emplacement de la partie dans la liste
     * @param numJoueur numéro du joueur de la partie concerné 
     * @return Champs correspondant
     */
    protected JButton getBoutonJouer(PartieBelote partie, int numPartie, int numJoueur) {
        JButton jouer;
        
        // Texte de présentation de la partie
        jouer = new JButton();
        jouer.setMargin(new Insets(0, 0, 0, 0));
        Rectangle position;
        String img;
        switch (numJoueur) {
            case Joueur.EST :
                position = aire.getSkinConfig().getRJoueurEBouton();
                img = aire.getSkinConfig().getRJoueurEBoutonImg();
                break;

            case Joueur.NORD :
                position = aire.getSkinConfig().getRJoueurNBouton();
                img = aire.getSkinConfig().getRJoueurNBoutonImg();
                break;

            case Joueur.SUD :
                position = aire.getSkinConfig().getRJoueurSBouton();
                img = aire.getSkinConfig().getRJoueurSBoutonImg();
                break;

            case Joueur.OUEST :
                position = aire.getSkinConfig().getRJoueurOBouton();
                img = aire.getSkinConfig().getRJoueurOBoutonImg();
                break;

            default :
                position = null;
                img = null;
                break;
        }
        if (img == null) {
            jouer.setFont(aire.getSkinConfig().getRFont());
            jouer.setBackground(aire.getSkinConfig().getRBoutonGlobalColor());
            jouer.setText("Jouer");
        }
        else {
            try {
                jouer.setIcon(aire.getImageIcon(img));
            }
            catch (MalformedURLException e) {
                aire.getLogger().severe("Image bouton Jouer erronée");
            }
            jouer.setToolTipText("Jouer");
        }
        if (!aire.getSkinConfig().isRBoutonGlocalBorder()) {
            jouer.setBorder(BorderFactory.createEmptyBorder());
        }
        jouer.setBounds(getOffsetX(numPartie) + position.x,
                        getOffsetY(numPartie) + position.y,
                        position.width,
                        position.height);
        jouer.addActionListener(this);
        return jouer;
    }

    /**
     * Gère l'affichage si besoin du bouton pour joindre la partie
     * @param partie Partie concernée
     * @param numPartie emplacement de la partie dans la liste
     * @param numJoueur numéro du joueur de la partie concerné 
     */
    protected void fillBoutonJouer(PartieBelote partie, int numPartie, int numJoueur) {
        JButton[] jouer = (JButton[]) jouerButton.get(numPartie);
        
        if (partie.getParticipant(numJoueur) == null) {
            jouer[numJoueur].setVisible(true);
        }
        else {
            jouer[numJoueur].setVisible(false);
        }
    }
    
    /**
     * Création du texte affichant le pseudo du joueur
     * @param partie Partie concernée
     * @param numPartie emplacement de la partie dans la liste
     * @param numJoueur numéro du joueur de la partie concerné 
     * @return Champs correspondant
     */
    protected JLabel getTextePseudo(PartieBelote partie, int numPartie, int numJoueur) {
        JLabel pseudo;
        
        // Texte de présentation de la partie
        pseudo = new JLabel();
        pseudo.setFont(aire.getSkinConfig().getRFont().deriveFont(Font.BOLD));
        pseudo.setForeground(aire.getSkinConfig().getRTexteDsListePartieColor());
        pseudo.setHorizontalAlignment(SwingConstants.CENTER);
        Rectangle position;
        switch (numJoueur) {
            case Joueur.EST :
                position = aire.getSkinConfig().getRJoueurENom();
                break;

            case Joueur.NORD :
                position = aire.getSkinConfig().getRJoueurNNom();
                break;

            case Joueur.SUD :
                position = aire.getSkinConfig().getRJoueurSNom();
                break;

            case Joueur.OUEST :
                position = aire.getSkinConfig().getRJoueurONom();
                break;

            default :
                position = null;
                break;
        }
        pseudo.setBounds(getOffsetX(numPartie) + position.x,
                        getOffsetY(numPartie) + position.y,
                        position.width,
                        position.height);
        return pseudo;
    }


    /**
     * Remplissage et affichege du texte affichant le pseudo du joueur si besoin
     * @param partie Partie concernée
     * @param numPartie emplacement de la partie dans la liste
     * @param numJoueur numéro du joueur de la partie concerné 
     */
    protected void fillTextePseudo(PartieBelote partie, int numPartie, int numJoueur) {
        JLabel[] pseudo = (JLabel[]) pseudoLabel.get(numPartie);
        
        if (partie.getParticipant(numJoueur) == null) {
            pseudo[numJoueur].setVisible(false);
        }
        else {
            if (!pseudo[numJoueur].getText().equals(partie.getParticipant(numJoueur).getPseudo())) {
                pseudo[numJoueur].setText(partie.getParticipant(numJoueur).getPseudo());
            }
            pseudo[numJoueur].setVisible(true);
        }
    }
    
    /**
     * Création du texte contenant l'avatar du joueur
     * @param partie Partie concernée
     * @param numPartie emplacement de la partie dans la liste
     * @param numJoueur numéro du joueur de la partie concerné 
     * @return Champs correspondant
     */
    protected JLabel getTexteAvatar(PartieBelote partie, int numPartie, int numJoueur) {
        JLabel avatar;
        
        // Texte de présentation de la partie
        avatar = new JLabel();
        Rectangle position;
        switch (numJoueur) {
            case Joueur.EST :
                position = aire.getSkinConfig().getRJoueurEAvatar();
                break;

            case Joueur.NORD :
                position = aire.getSkinConfig().getRJoueurNAvatar();
                break;

            case Joueur.SUD :
                position = aire.getSkinConfig().getRJoueurSAvatar();
                break;

            case Joueur.OUEST :
                position = aire.getSkinConfig().getRJoueurOAvatar();
                break;

            default :
                position = null;
                break;
        }
        avatar.setBounds(getOffsetX(numPartie) + position.x,
                        getOffsetY(numPartie) + position.y,
                        position.width,
                        position.height);
        avatar.getInsets(new Insets(0, 0, 0, 0));
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setVerticalAlignment(SwingConstants.CENTER);
        return avatar;
    }

    /**
     * Remplissage et affichge du texte contenant l'avatar du joueur si besoin
     * @param partie Partie concernée
     * @param numPartie emplacement de la partie dans la liste
     * @param numJoueur numéro du joueur de la partie concerné 
     */
    protected void fillTexteAvatar(PartieBelote partie, int numPartie, int numJoueur) {
        JLabel[] avatar = (JLabel[]) avatarLabel.get(numPartie);
        
        if (partie.getParticipant(numJoueur) == null) {
            avatar[numJoueur].setVisible(false);
            if (avatar[numJoueur].getIcon() != null) {
                ImageIcon oldIcon = (ImageIcon) avatar[numJoueur].getIcon();
                oldIcon.getImage().flush();
                avatar[numJoueur].setIcon(null);
            }
        }
        else {
            ImageIcon avatarImg;
            try {
                avatarImg = aire.getImageIcon(partie.getParticipant(numJoueur).getAvatar().toString());
                if ((avatar[numJoueur].getWidth() != avatarImg.getIconWidth())
                        || (avatar[numJoueur].getHeight() != avatarImg.getIconHeight())) {
                    // Retaille l'image afin qu'il n'y ait pas à la transformer, ce qui améliore les perfs
                    avatar[numJoueur].setSize(avatarImg.getIconWidth(), avatarImg.getIconHeight());
                }
                avatar[numJoueur].setIcon(avatarImg);
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            avatar[numJoueur].setVisible(true);
        }
    }
    
    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        // Clic sur un bouton... ==> Recherche lequel
        boolean found = false;
        for (int i = 0; !found && (i < getNbItem()); i++) {
            JButton[] jouer;
            jouer = (JButton[]) jouerButton.get(i);
            for (int j = 0; j < PartieBelote.NB_JOUEUR; j++) {
                if ((jouer[j] != null) && (jouer[j] == e.getSource())) {
                    // Trouvé
                    found = true;
                    aire.joueurRejointPartie(i, j);
                }
            }
        }
    }

    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent e) {
        // Clic quelquepart recherche ...
        boolean found = false;
        for (int i = 0; !found && (i < getNbItem()); i++) {
            if (((e.getY() >= getOffsetY(i)) && ((e.getY() < getOffsetY(i + 1))))
                || (((e.getX() >= getOffsetX(i)) && ((e.getX() < getOffsetX(i + 1)))))) {
                // Trouvé
                found = true;
                aire.affichePartie(i);
            }
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
    }

    /**
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent e) {
    }
    /**
     * @see java.awt.Container#removeAll()
     */
    public void removeAll() {
        removeMouseListener(this);
        for (int i = 0; i < getNbItem(); i++) {
            for (int j = 0; j < PartieBelote.NB_JOUEUR; j++) {
                ((JButton[]) jouerButton.get(i))[j].removeActionListener(this);
            }
        }

        super.removeAll();
    }

}
