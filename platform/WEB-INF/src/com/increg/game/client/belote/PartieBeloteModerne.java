/*
 * Created on 10 mai 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.client.belote;

import com.increg.game.client.Couleur;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class PartieBeloteModerne extends PartieBelote {

    /**
     * Pseudo-Couleur tout atout
     */
    public static final int TOUT_ATOUT = 5;

    /**
     * Pseudo-Couleur sans atout
     */
    public static final int SANS_ATOUT = 4;


    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String res = titre + " - Belote Moderne ";
        if (annonce) {
            res = res + "avec Annonces";  
        }
        else {
            res = res + "sans Annonce";  
        }
        String superRes = super.toString(); 
        if (superRes.length() > 0) {
            res = res + " (" + superRes + ")";
        }
        return res;
    }

    /**
     * Un joueur vient de décider de prendre ou de ne pas prendre
     * @param couleur Couleur choisue (-1 si aucune)
     */    
    public void impactePrise(int couleur) {
        final String[] chaineCouleur = {"Pique", "Coeur", "Trèfle", "Carreau", "Sans atout", "Tout atout"};

        switch (etat.getEtat()) {
            case EtatPartieBelote.ETAT_TOUR_ENCHERE1_PRIS : 
            case EtatPartieBelote.ETAT_TOUR_ENCHERE2_PRIS : 
            case EtatPartieBelote.ETAT_TOUR_ENCHERE1 :
            case EtatPartieBelote.ETAT_TOUR_ENCHERE2 : { 
                int joueur = etat.getJoueur();
                if (couleur != -1) {
                    if (couleur != TOUT_ATOUT) {
                        etat.etatSuivant(EtatPartieBelote.ACTION_PREND_SURCHERE_POSSIBLE, joueur);
                        currentEvent = getParticipant(joueur).getPseudo() + " veut prendre à " + chaineCouleur[couleur];  
                    }
                    else {
                        // Pas de surenchere sur TOUT_ATOUT
                        etat.etatSuivant(EtatPartieBelote.ACTION_PREND, joueur);
                        currentEvent = getParticipant(joueur).getPseudo() + " prend à " + chaineCouleur[couleur];  
                    }
                    preneur = joueur;
                    atout = couleur;
                }
                else {
                    currentEvent = getParticipant(joueur).getPseudo() + " passe";  
                    etat.etatSuivant(EtatPartieBelote.ACTION_PASSE, joueur);
                    
                    // fin du 2ème tour ?
                    if (etat.getEtat() == EtatPartieBelote.ETAT_COUPE_JEU) {
                        // Rassemble les cartes
                        jeu.regroupeMains();
                        addNextEvent();
                    }
                }

                // fin du 2ème tour avec démarrage
                if (etat.getEtat() == EtatPartieBelote.ETAT_JOUE_CARTE) {
                                
                    // Distribue les dernières cartes
                    ((JeuBelote) jeu).distribue(1, 2, (etat.getJoueurCoupe() + 1) % NB_JOUEUR, preneur);
                        
                    // Transforme les atouts dans les jeux
                    ((JeuBelote) jeu).appliqueAtout(atout);
    
                    // Tri les jeux
                    ((JeuBelote) jeu).triMains();
                        
                    if (annonce) {
                        // Recherche les annonces
                        ((JeuBelote) jeu).chercheAnnonce();

                        // Filtre les annonces en tenant compte de l'ordre d'annonce et des hauteurs
                        String eventAnnonce = filtreAnnonce(jeu.getAnnonces());
                        if (eventAnnonce.length() > 0) {
                            currentEvent = currentEvent + "\n" + eventAnnonce;
                        }
                    }
                    
                    // Recherche les Belote / Rebelote
                    ((JeuBelote) jeu).chercheBelote();

                    // Initialise le score des annonces et belotes
                    initScoreAnnonce();
                    
                }
                setStep(getStep() + 1);            

                break; 
            }
            case EtatPartieBelote.ETAT_DISTRIBUTION :
            case EtatPartieBelote.ETAT_NON_DEMARRE :
            case EtatPartieBelote.ETAT_COUPE_JEU : 
            case EtatPartieBelote.ETAT_FIN_PARTIE : 
            case EtatPartieBelote.ETAT_FIN_TOUR : 
            case EtatPartieBelote.ETAT_JOUE_CARTE : 
            case EtatPartieBelote.ETAT_FIN_DONNE :
            default : 
        }
    }
    
    /**
     * Indique si le joueur peut prendre à une certaine couleur
     * @param couleur couleur questionnée
     * @return true s'il peut
     */
    public boolean peutPrendreA(int couleur) {
        switch (etat.getEtat()) {
            case EtatPartieBelote.ETAT_TOUR_ENCHERE1 : {
                if ((couleur >= SANS_ATOUT) && (couleur > atout)) {
                    return true;
                }
                if (atout == -1) {
                    return couleur == ((Couleur) getJeu().getTas().get(0)).getCouleur();
                }
                else {
                    return false;
                }
            }
            case EtatPartieBelote.ETAT_TOUR_ENCHERE1_PRIS : 
            case EtatPartieBelote.ETAT_TOUR_ENCHERE2_PRIS : 
            case EtatPartieBelote.ETAT_TOUR_ENCHERE2 : { 
                if ((couleur >= SANS_ATOUT) && (couleur > atout)) {
                    return true;
                }
                if (atout == -1) {
                    return couleur != ((Couleur) getJeu().getTas().get(0)).getCouleur();
                }
                else {
                    return false;
                }
            }
            case EtatPartieBelote.ETAT_DISTRIBUTION :
            case EtatPartieBelote.ETAT_NON_DEMARRE :
            case EtatPartieBelote.ETAT_COUPE_JEU : 
            case EtatPartieBelote.ETAT_FIN_PARTIE : 
            case EtatPartieBelote.ETAT_FIN_TOUR : 
            case EtatPartieBelote.ETAT_JOUE_CARTE : 
            case EtatPartieBelote.ETAT_FIN_DONNE :
            default : 
                return false;
        }
        
    }

    /**
     * @return Coefficient des scores
     */
    protected int getCoef() {
        int coef = 1;
        
        if (atout == SANS_ATOUT) {
            coef = 2;
        }
        else if (atout == TOUT_ATOUT) {
            coef = 3;
        }
        
        return coef;
    }
        
    /**
     * @see com.increg.game.client.belote.PartieBelote#atoutToString()
     */
    public String atoutToString() {
        switch (atout) {
            case TOUT_ATOUT :
                return "Tout Atout";
            case SANS_ATOUT :
                return "Sans Atout";
            default :
                return super.atoutToString();
        }
    }

    /**
     * @see com.increg.game.client.belote.PartieBelote#getScoreMaxPartie()
     */
    protected int getScoreMaxPartie() {
        return 3001;
    }

    /**
     * @see com.increg.game.client.belote.PartieBelote#buildCarte(int, int)
     */
    public Couleur buildCarte(int hauteur, int couleur) {
        if (isAtoutFinal() && (atout == TOUT_ATOUT)) {
            return new AtoutBelote(hauteur, couleur);
        }
        if (isAtoutFinal() && (atout == SANS_ATOUT)) {
            return new CouleurBelote(hauteur, couleur);
        }
        else {
            return super.buildCarte(hauteur, couleur);
        }
    }

}
