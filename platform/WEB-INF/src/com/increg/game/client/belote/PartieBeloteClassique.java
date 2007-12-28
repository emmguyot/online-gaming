/*
 * Created on 10 mai 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.client.belote;

import java.util.Map;

import com.increg.game.client.Couleur;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class PartieBeloteClassique extends PartieBelote {

	public static final int CD_PARAM_NB_POINT_BELOTE_CLASSIQUE_SS_ANNONCE = 1;
	public static final int CD_PARAM_NB_POINT_BELOTE_CLASSIQUE_SS_ANNONCE_TOURNOI = 2;
	public static final int CD_PARAM_NB_POINT_BELOTE_CLASSIQUE_AVEC_ANNONCES = 3;
	public static final int CD_PARAM_NB_POINT_BELOTE_CLASSIQUE_AVEC_ANNONCES_TOURNOI = 4;

	/**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String res = titre + " - Belote Classique ";
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
        final String[] chaineCouleur = {"Pique", "Coeur", "Trèfle", "Carreau"};

        switch (etat.getEtat()) {
            case EtatPartieBelote.ETAT_TOUR_ENCHERE1 :
            case EtatPartieBelote.ETAT_TOUR_ENCHERE2 : { 
                int joueur = etat.getJoueur();
                if (couleur != -1) {
                    currentEvent = getParticipant(joueur).getPseudo() + " prend à " + chaineCouleur[couleur];  

                    etat.etatSuivant(EtatPartieBelote.ACTION_PREND, joueur);
                    preneur = joueur;
                    atout = couleur;
                    // Distribue les dernières cartes
                    ((JeuBelote) jeu).distribue(1, 2, (etat.getJoueurCoupe() + 1) % NB_JOUEUR, joueur);
                    
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
                setStep(getStep() + 1);            
                break; 
            }
            case EtatPartieBelote.ETAT_DISTRIBUTION :
            case EtatPartieBelote.ETAT_NON_DEMARRE :
            case EtatPartieBelote.ETAT_COUPE_JEU : 
            case EtatPartieBelote.ETAT_FIN_PARTIE : 
            case EtatPartieBelote.ETAT_FIN_TOUR : 
            case EtatPartieBelote.ETAT_JOUE_CARTE : 
            case EtatPartieBelote.ETAT_TOUR_ENCHERE1_PRIS : 
            case EtatPartieBelote.ETAT_TOUR_ENCHERE2_PRIS : 
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
                return couleur == ((Couleur) getJeu().getTas().get(0)).getCouleur();
            }
            case EtatPartieBelote.ETAT_TOUR_ENCHERE2 : { 
                return couleur != ((Couleur) getJeu().getTas().get(0)).getCouleur();
            }
            case EtatPartieBelote.ETAT_DISTRIBUTION :
            case EtatPartieBelote.ETAT_NON_DEMARRE :
            case EtatPartieBelote.ETAT_COUPE_JEU : 
            case EtatPartieBelote.ETAT_FIN_PARTIE : 
            case EtatPartieBelote.ETAT_FIN_TOUR : 
            case EtatPartieBelote.ETAT_JOUE_CARTE : 
            case EtatPartieBelote.ETAT_TOUR_ENCHERE1_PRIS : 
            case EtatPartieBelote.ETAT_TOUR_ENCHERE2_PRIS : 
            case EtatPartieBelote.ETAT_FIN_DONNE :
            default : 
                return false;
        }
        
    }

    /**
     * @return Coefficient des scores
     */
    protected int getCoef() {
        return 1;
    }

    /**
     * @see com.increg.game.client.Partie#setScoreMaxPartie(java.util.Map)
     */
	public void setScoreMaxPartie(Map<Integer, String> lstParam) {
    	String valeur;
        if ((myTournoi != null) && (myTournoi.getIdentifiant() > 0)) {
        	if (annonce) {
            	valeur = lstParam.get(new Integer(PartieBeloteClassique.CD_PARAM_NB_POINT_BELOTE_CLASSIQUE_AVEC_ANNONCES_TOURNOI));
        	}
        	else {
            	valeur = lstParam.get(new Integer(PartieBeloteClassique.CD_PARAM_NB_POINT_BELOTE_CLASSIQUE_SS_ANNONCE_TOURNOI));
        	}
        }
        else {
        	if (annonce) {
            	valeur = lstParam.get(new Integer(PartieBeloteClassique.CD_PARAM_NB_POINT_BELOTE_CLASSIQUE_AVEC_ANNONCES));
        	}
        	else {
            	valeur = lstParam.get(new Integer(PartieBeloteClassique.CD_PARAM_NB_POINT_BELOTE_CLASSIQUE_SS_ANNONCE));
        	}
        }
        if (valeur != null) {
        	setScoreMaxPartie(Integer.parseInt(valeur));
        }
        else {
        	// Valeur par défaut
        	setScoreMaxPartie(1001);
        }
	}

}
