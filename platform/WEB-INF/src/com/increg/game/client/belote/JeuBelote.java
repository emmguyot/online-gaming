/*
 * Created on 22 mai 2003
 *
 */
package com.increg.game.client.belote;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import com.increg.game.client.Carte;
import com.increg.game.client.Couleur;
import com.increg.game.client.CouleurStd;
import com.increg.game.client.Jeu32Cartes;

/**
 * @author Manu
 *
 * Jeu de 32 Cartes dans le cas de la Belote
 */
public class JeuBelote extends Jeu32Cartes {

    /**
     * 
     * Annonces Belote/Rebelote des joueurs : Tableau indicé par le joueur et Vector d'Annonce
     */
    protected Vector[] belote;

    /**
     * Constructeur
     * @param nbJoueur Nombre de joueurs
     */
    public JeuBelote(int nbJoueur) {
        super(nbJoueur);
        
        belote = new Vector[nbJoueur];
        
        for (int i = 0; i < annonces.length; i++) {
            belote[i] = new Vector(2);
        }

    }
    
    /**
     * Distribue le tas 
     * @param phase Phase correspondante
     * @param nbDebut Nombre de cartes au début 2 ou 3 par exemple
     * @param premierJoueur Premier joueur à servir
     * @param preneur preneur (si influence la distribution)
     */
    public void distribue(int phase, int nbDebut, int premierJoueur, int preneur) {
        
        if (phase == 0) {
            // RAZ des mains et des plis
            for (int i = 0; i < mains.length; i++) {
                mains[i].clear();
                plis[i].clear();
            }
            
            // Distribution des premières cartes
            for (int i = 0; i < mains.length; i++) {
                Carte aCarte = (Carte) tas.remove(0);
                mains[(premierJoueur + i) % mains.length].add(aCarte);
                
                aCarte = (Carte) tas.remove(0);
                mains[(premierJoueur + i) % mains.length].add(aCarte);
                
                // 3 ème carte ?
                if (nbDebut == 3) {
                    aCarte = (Carte) tas.remove(0);
                    mains[(premierJoueur + i) % mains.length].add(aCarte);
                }
            }
            
            // Second tour
            // Distribution des premières cartes
            for (int i = 0; i < mains.length; i++) {
                Carte aCarte = (Carte) tas.remove(0);
                mains[(premierJoueur + i) % mains.length].add(aCarte);
                
                aCarte = (Carte) tas.remove(0);
                mains[(premierJoueur + i) % mains.length].add(aCarte);
                
                // 3 ème carte ?
                if (nbDebut == 2) {
                    aCarte = (Carte) tas.remove(0);
                    mains[(premierJoueur + i) % mains.length].add(aCarte);
                }
            }
        }
        else {
            // Première carte au preneur
            Carte aCarte = (Carte) tas.remove(0);
            mains[preneur].add(aCarte);
            
            // Distribue le reste
            for (int i = 0; i < mains.length; i++) {
                aCarte = (Carte) tas.remove(0);
                mains[(premierJoueur + i) % mains.length].add(aCarte);
                
                aCarte = (Carte) tas.remove(0);
                mains[(premierJoueur + i) % mains.length].add(aCarte);
                
                // 3 ème carte ? Si pas preneur
                if (((premierJoueur + i) % mains.length) != preneur) {
                    aCarte = (Carte) tas.remove(0);
                    mains[(premierJoueur + i) % mains.length].add(aCarte);
                }
            }
            
        }
    }

    /**
     * Transforme les cartes en atout en fonction de la couleur
     * @param atout Couleur de l'atout
     */
    public void appliqueAtout(int atout) {
        
        // Boucle sur chaque main
        for (int joueur = 0; joueur < mains.length; joueur++) {
            for (int j = 0; j < mains[joueur].size(); j++) {
                CouleurBelote aCarte = (CouleurBelote) mains[joueur].get(j); 
                if ((aCarte.getCouleur() == atout) || (atout == PartieBeloteModerne.TOUT_ATOUT)) {
                    // Transforme la carte en Atout
                    mains[joueur].set(j, new AtoutBelote(aCarte));
                }
            }
        }
    }

    /**
     * Tri les mains 
     */
    public void triMains() {
        
        // Boucle sur chaque main
        for (int joueur = 0; joueur < mains.length; joueur++) {
            SortedSet mainTriee = new TreeSet(mains[joueur]);
            
            mains[joueur].clear();
            mains[joueur].addAll(mainTriee);
        }
        
        // Remanie les mains pour alterner les couleurs
        for (int joueur = 0; joueur < mains.length; joueur++) {
            int firstCouleur = -1;
            int lastCouleur = -1;
            int offsetDebut = 0;
            int nbCarteFin = 0;
            
            // Recherche les couleurs en mains
            for (int i = 0; i < (mains[joueur].size() - nbCarteFin); i++) {
                Couleur aCarte = (Couleur) mains[joueur].get(i);
                
                if (firstCouleur != -1) {
                    /**
                     * Couleur différente de la précédente
                     * Couleur (Rouge / Noir) identique => Intervertion nécessaire
                     */
                    if ((aCarte.getCouleur() != lastCouleur)
                        && ((aCarte.getCouleur() % 2) == (lastCouleur % 2))) {

                        // Destination en fonction de la premier carte
                        if ((firstCouleur % 2) == (lastCouleur % 2)) {
                            // Même couleur (N/R) => A la fin
                            mains[joueur].add(mains[joueur].remove(i));
                            
                            // Mets à jour l'indice
                            i = i - 1;
                            nbCarteFin++;
                        }
                        else {
                            // Au début : Gestion d'un Offset pour éviter de déclasser les cartes
                            mains[joueur].add(offsetDebut, mains[joueur].remove(i));
                            offsetDebut++;
                        }
                    }
                    else {
                        // C'est Ok
                        lastCouleur = aCarte.getCouleur();
                        offsetDebut = 0;
                    }
                }
                else {
                    firstCouleur = aCarte.getCouleur();
                    lastCouleur = firstCouleur;
                }
                
            }
        }        
    }
    
    /**
     * Le joueur ramasse le tapis
     * @param joueur joueur qui ramasse
     */
    public void ramasseTapis(int joueur) {
        while (tapis.size() > 0) {
            plis[joueur % 2].add(tapis.remove(0));
        }
    }

    /**
     * @see com.increg.game.client.JeuCartes#regroupePlis()
     */
    public void regroupePlis() {
        super.regroupePlis();
        
        // Boucle sur le tas qui contient toutes les cartes
        for (int j = 0; j < tas.size(); j++) {
            Couleur aCarte = (Couleur) tas.get(j);
            if (aCarte instanceof AtoutBelote) {
                // Transforme la carte en "normale"
                tas.set(j, new CouleurBelote((AtoutBelote) aCarte));
            }
        }
    }

    /**
     * @return Annonces trouvées : Vecteur par joueur
     */
    public Vector[] chercheAnnonce() {
        
        for (int i = 0; i < annonces.length; i++) {
            annonces[i].clear();
        }

        boolean[][] carteLibre = new boolean[mains.length][mains[0].size()];
        // Initialisation
        for (int iJoueur = 0; iJoueur < mains.length; iJoueur++) {
            for (int j = 0; j < carteLibre[iJoueur].length; j++) {
                carteLibre[iJoueur][j] = true;
            }
        }

        // Recherche proprement dite
        for (int iJoueur = 0; iJoueur < mains.length; iJoueur++) {
            
            /**
             * Ignore les différences Atouts / Couleur en basculant tout dans la couleur standard
             * L'ordre est donc naturel vis à vis des séquences
             */
            CouleurStd[] mainSimple = new CouleurStd[mains[iJoueur].size()];
            for (int i = 0; i < mains[iJoueur].size(); i++) {
                Couleur carte = (Couleur) mains[iJoueur].get(i);

                if (carte instanceof AtoutBelote) {
                    mainSimple[i] = ((AtoutBelote) carte).getCouleurStd(); 
                }
                else {
                    mainSimple[i] = ((CouleurBelote) carte).getCouleurStd(); 
                }
            }
            /**
             * Recherche des carrés puis des séquences
             * Pour la recherche des carrés :
             * - Recherche au sein de la première couleur du jeu
             * - Demande si la carte correspondante est possédée
             * Pour la recherche des séquences :
             * - ...
             */
            chercheCarre(iJoueur, mainSimple, carteLibre[iJoueur]);
            
            /**
             * Recherche des séquences
             */
            chercheSequence(iJoueur, mainSimple, carteLibre[iJoueur]);
        }
        
        return annonces;
    }
    
    /**
     * Recherche les carrés dans le jeu d'un joueur 
     * @param iJoueur joueur concerné (E)
     * @param mainSimple main de cartes standards (E)
     * @param carteLibre Indicateur des cartes libres (E/S)
     */
    protected void chercheCarre(
        int iJoueur,
        CouleurStd[] mainSimple,
        boolean[] carteLibre) {
            
        // Dernière couleur étudiée
        int lastCouleur = mainSimple[0].getCouleur();
        // Nombres de cartes trouvées pour former le carré
        int nbCartes = 0;
        Couleur[] cartes = new Couleur[AnnonceBelote.NB_CARTES_CARRE];
        
        for (int i = 0; 
            (lastCouleur == mainSimple[0].getCouleur())
            && (i < mainSimple.length);
            i++) {
                
            if (carteLibre[i]) {                
                CouleurStd carte = mainSimple[i];

                // Compte la carte actuelle                
                cartes[nbCartes] = (Couleur) mains[iJoueur].get(i);
                nbCartes++;
                
                for (int couleur = (carte.getCouleur() + 1) % Couleur.NB_COULEUR;
                    couleur != carte.getCouleur();
                    couleur = (couleur + 1) % Couleur.NB_COULEUR) {
                        
                    int pos = indexOfMain(iJoueur, new CouleurStd(carte.getHauteur(), couleur)); 
                    if ((pos >= 0) && (carteLibre[pos])) {
                        // Une carte de plus pour le carré
                        cartes[nbCartes] = (Couleur) mains[iJoueur].get(pos);
                        nbCartes++;
                    }
                }
                
                // Dépouillement
                if (nbCartes == AnnonceBelote.NB_CARTES_CARRE) {
                    // Ajoute l'annonce
                    AnnonceBelote anAnnonce = new AnnonceCarre(cartes);
                    
                    if (anAnnonce.getValeur() > 0) {
                        // Conserve l'annonce et flag les cartes
                        annonces[iJoueur].add((anAnnonce));
                        
                        for (int nCarte = 0; nCarte < nbCartes; nCarte++) {
                            Couleur carteFlag = cartes[nCarte];
                            boolean found = false; 
                            for (int j = 0; !found && (j < mainSimple.length); j++) {
                                if (mainSimple[j].equals(carteFlag)) {
                                    // Trouvé : Flag la carte comme prise
                                    carteLibre[j] = false;
                                    found = true;
                                }
                            }
                        }
                    }
                }
                // Reset
                lastCouleur = carte.getCouleur();
                nbCartes = 0;
                cartes = new Couleur[AnnonceBelote.NB_CARTES_CARRE];
            } // Carte libre                    
        }
    }

    /**
     * Recherche les carrés dans le jeu d'un joueur 
     * @param iJoueur joueur concerné (E)
     * @param mainSimple main de cartes standards (E)
     * @param carteLibre Indicateur des cartes libres (E/S)
     */
    protected void chercheSequence(
        int iJoueur,
        CouleurStd[] mainSimple,
        boolean[] carteLibre) {
            
        // Nombres de cartes trouvées pour former une séquence
        int nbCartes = 0;
        Couleur[] cartes = new Couleur[mainSimple.length];
        
        for (int i = 0; (i < mainSimple.length); i++) {
                
            if (carteLibre[i]) {                
                CouleurStd carte = mainSimple[i];
                boolean rupture = false;

                // Tente les suivantes
                for (int j = 0; !rupture && (j < mainSimple.length); j++) {

                    int pos = indexOfMain(iJoueur, new CouleurStd(carte.getHauteur() + j, carte.getCouleur())); 
                    if ((pos >= 0) && (carteLibre[pos])) {
                        // Une carte de plus pour la séquence
                        Couleur currentCarte = (Couleur) mains[iJoueur].get(pos);
                        cartes[nbCartes] = currentCarte;
                        nbCartes++;
                    }
                    else {
                        // La suivante n'est pas possédée
                        rupture = true;
                    }
                }
                
                rupture = false;
                // Tente les précédentes
                for (int j = 1; !rupture && (j <= carte.getHauteur()); j++) {

                    int pos = indexOfMain(iJoueur, new CouleurStd(carte.getHauteur() - j, carte.getCouleur())); 
                    if ((pos >= 0) && (carteLibre[pos])) {
                        // Une carte de plus pour la séquence
                        // A placer au début
                        for (int iDecal = nbCartes; iDecal > 0; iDecal--) {
                            cartes[iDecal] = cartes[iDecal - 1]; 
                        }
                        cartes[0] = (Couleur) mains[iJoueur].get(pos);
                        nbCartes++;
                    }
                    else {
                        // La suivante n'est pas possédée
                        rupture = true;
                    }
                }

                AnnonceBelote anAnnonce = null;
                
                // Dépouillement
                if (nbCartes == AnnonceBelote.NB_CARTES_TIERCE) {
                    // Ajoute l'annonce
                    anAnnonce = new AnnonceTierce(cartes);
                }
                else if (nbCartes == AnnonceBelote.NB_CARTES_QUATRIEME) {
                    // Ajoute l'annonce
                    anAnnonce = new AnnonceQuatrieme(cartes);
                }
                else if (nbCartes >= AnnonceBelote.NB_CARTES_CENT) {
                    // Ajoute l'annonce
                    while (nbCartes > AnnonceBelote.NB_CARTES_CENT) {
                        // Retire les plus petites cartes qui sont en trop
                        for (int iDecal = 0; iDecal < nbCartes; iDecal++) {
                            cartes[iDecal] = cartes[iDecal + 1]; 
                        }
                        nbCartes--;
                    }
                    anAnnonce = new AnnonceCent(cartes);
                }
                    
                if (anAnnonce != null) {
                    // Conserve l'annonce et flag les cartes
                    annonces[iJoueur].add((anAnnonce));
                    
                    for (int nCarte = 0; nCarte < nbCartes; nCarte++) {
                        Couleur carteFlag = cartes[nCarte];
                        boolean found = false; 
                        for (int j = 0; !found && (j < mainSimple.length); j++) {
                            if (mainSimple[j].equals(carteFlag)) {
                                // Trouvé : Flag la carte comme prise
                                carteLibre[j] = false;
                                found = true;
                            }
                        }
                    }
                }
                // Reset
                nbCartes = 0;
                cartes = new Couleur[mainSimple.length];
            } // Carte libre                    
        }
    }

    /**
     * @return Belotes trouvées : Vecteur par joueur
     */
    public Vector[] chercheBelote() {

        // RAZ des belotes
        for (int i = 0; i < belote.length; i++) {
            belote[i].clear();
        }
        
        // Recherche proprement dite
        for (int iJoueur = 0; iJoueur < mains.length; iJoueur++) {
            
            // Dernière couleur étudiée
            int lastCouleur = -1;
            // Nombres de cartes trouvées pour former la Belote
            int nbCartes = 0;
            Couleur[] cartes = new Couleur[AnnonceBelote.NB_CARTES_BELOTE];
            
            for (int i = 0; i < mains[iJoueur].size(); i++) {
                Couleur carte = (Couleur) mains[iJoueur].get(i);
                
                if (carte instanceof AtoutBelote) {
                    
                    // Changement de couleur ?
                    if (lastCouleur != carte.getCouleur()) {
                        // Reset
                        lastCouleur = carte.getCouleur();
                        nbCartes = 0;
                        cartes = new Couleur[AnnonceBelote.NB_CARTES_BELOTE];
                    }
                    
                    if ((carte.getHauteur() == AtoutBelote.DAME)
                        || (carte.getHauteur() == AtoutBelote.ROI)) {
                        // Une carte de plus pour la Belote
                        cartes[nbCartes] = carte;
                        nbCartes++;

                        // Dépouillement
                        if (nbCartes == AnnonceBelote.NB_CARTES_BELOTE) {
                            // Ajoute l'annonce
                            belote[iJoueur].add(new AnnonceBeloteRebelote(cartes));
                            nbCartes = 0;
                            cartes = new Couleur[AnnonceBelote.NB_CARTES_BELOTE];
                        }
                    }
                }
            }
        }        
        
        return belote;
    }

    /**
     * @return Annonces Belote/Rebelote des joueurs : Tableau indicé par le joueur et Vector d'Annonce
     */
    public Vector[] getBelote() {
        return belote;
    }

    /**
     * @param vectors Annonces Belote/Rebelote des joueurs : Tableau indicé par le joueur et Vector d'Annonce
     */
    public void setBelote(Vector[] vectors) {
        belote = vectors;
    }

}
