/*
 * Created on 9 mai 2003
 *
 */
package com.increg.game.client.belote;

import java.util.Vector;

import com.increg.game.client.Annonce;
import com.increg.game.client.Couleur;
import com.increg.game.client.Joueur;
import com.increg.game.client.Partie;

/**
 * @author Manu
 *
 * Partie pour la Belote
 */
public abstract class PartieBelote extends Partie {

    /**
     * Nombre de joueurs dans une partie
     */
    public static final int NB_JOUEUR = 4;

    /**
     * Les annonces sont gérées ?
     */
    protected boolean annonce;

    /**
     * Preneur du tour en cours : Si non significatif = -1
     */
    protected int preneur;
    
    /**
     * Joueur qui a démarré le tour (mis à jour dès qu'une carte est sur le tapis)
     */
    protected int ouvreur;
    
    /**
     * Couleur de l'atout
     */
    protected int atout;

    /**
     * Dernier joueur ayant ramassé
     */    
    protected int dernierRamasse;

    /**
     * Il y a capot ? -1 = non, sinon celui qui est capot
     */
    protected int capot;

    /**
     * Score en litige
     */
    protected int[] litige;
    
    /**
     * Score des annonces prennables
     */
    protected int[] scoreAnnPrennables;
    /**
     * Score des annonces non prennables
     */
    protected int[] scoreAnnNonPrennables;
    
    /**
     * Constructeur classique
     */
    public PartieBelote() {
        super();
        
        participant = new Joueur[NB_JOUEUR];
        dernierParticipant = new Joueur[NB_JOUEUR];
        scores.add(new int[NB_JOUEUR / 2]);
        annonce = false;
        etat = new EtatPartieBelote();
        preneur = -1;
        ouvreur = -1;
        atout = -1;
        dernierRamasse = -1;
        jeu = new JeuBelote(NB_JOUEUR);
        
        jeu.melangeTas();
        scoreDonne = new int[PartieBelote.NB_JOUEUR / 2];
        litige = new int[PartieBelote.NB_JOUEUR / 2];
        scoreAnnNonPrennables = new int[PartieBelote.NB_JOUEUR / 2];
        scoreAnnPrennables = new int[PartieBelote.NB_JOUEUR / 2];
    }

    /**
     * @return Les annonces sont gérées ?
     */
    public boolean isAnnonce() {
        return annonce;
    }

    /**
     * @param b Les annonces sont gérées ?
     */
    public void setAnnonce(boolean b) {
        annonce = b;
    }

    /**
     * @return Joueur preneur
     */
    public int getPreneur() {
        return preneur;
    }

    /**
     * @param joueur Joueur preneur
     */
    public void setPreneur(int joueur) {
        preneur = joueur;
    }

    /**
     * @return Couleur de l'atout
     */
    public int getAtout() {
        return atout;
    }

    /**
     * @param i Couleur de l'atout
     */
    public void setAtout(int i) {
        atout = i;
    }

    /**
     * @return Joueur qui a ouvert le tour
     */
    public int getOuvreur() {
        return ouvreur;
    }

    /**
     * @param joueur Joueur qui a ouvert le tour
     */
    public void setOuvreur(int joueur) {
        ouvreur = joueur;
    }

    /**
     * @return Dernier joueur ayant ramassé
     */
    public int getDernierRamasse() {
        return dernierRamasse;
    }

    /**
     * @param i Dernier joueur ayant ramassé
     */
    public void setDernierRamasse(int i) {
        dernierRamasse = i;
    }

    /**
     * @return Capot ou pas ?
     */
    public boolean isCapot() {
        return (capot != -1);
    }

    /**
     * @param joueur Joueur capot
     */
    public void setCapot(int joueur) {
        capot = joueur;
    }

    /* ***************************************
     * Méthodes fonctionnelles
     * *************************************** */
    /**
     * @see com.increg.game.client.Partie#joueurRejoint(com.increg.game.client.Joueur, int)
     */
    public boolean joueurRejoint(Joueur aJoueur, int position) {
        
        boolean res = super.joueurRejoint(aJoueur, position);
        
        if (res) {
            if ((etat.getEtat() == EtatPartieBelote.ETAT_NON_DEMARRE) 
                && (getNbParticipant() == PartieBelote.NB_JOUEUR)) {
                // Partie complète : Change d'état
                etat.etatSuivant(EtatPartieBelote.ACTION_INITIALISE, etat.getJoueur());
                currentEvent = "La partie commence";
                addNextEvent();
                // Reset la partie au cas ou c'était une partie finie
                restart();
                setStep(getStep() + 1);            
            }
        }
        
        return res;
    }

    /**
     * Un joueur vient de couper
     * @param pos Position de la coupe
     */    
    public void impacteCoupe(int pos) {
        switch (etat.getEtat()) {
            case EtatPartieBelote.ETAT_COUPE_JEU : {

                currentEvent = getParticipant(etat.getJoueur()).getPseudo() + " coupe le jeu";
                
                // Reset de l'atout
                atout = -1;  
                
                etat.etatSuivant(EtatPartieBelote.ACTION_COUPE, etat.getJoueur());
                ((JeuBelote) jeu).coupe(pos);
                
                // Il faut savoir combien en premier : Attente du retour
                setStep(getStep() + 1);            
                break; 
            }
            case EtatPartieBelote.ETAT_NON_DEMARRE :
            case EtatPartieBelote.ETAT_DISTRIBUTION : 
            case EtatPartieBelote.ETAT_FIN_PARTIE : 
            case EtatPartieBelote.ETAT_FIN_TOUR : 
            case EtatPartieBelote.ETAT_JOUE_CARTE : 
            case EtatPartieBelote.ETAT_TOUR_ENCHERE1 : 
            case EtatPartieBelote.ETAT_TOUR_ENCHERE1_PRIS : 
            case EtatPartieBelote.ETAT_TOUR_ENCHERE2_PRIS : 
            case EtatPartieBelote.ETAT_TOUR_ENCHERE2 :
            case EtatPartieBelote.ETAT_FIN_DONNE :
            default : 
        }
    }

    /**
     * Un joueur vient de "distribuer" en choisissant le nb de cartes en premier
     * @param nb Nombre de cartes en premier
     */    
    public void impacteDistribue(int nb) {
        switch (etat.getEtat()) {
            case EtatPartieBelote.ETAT_DISTRIBUTION : {

                currentEvent = getParticipant(etat.getJoueur()).getPseudo() + " distribue les cartes";  

                etat.etatSuivant(EtatPartieBelote.ACTION_DISTRIBUE, etat.getJoueur());
                // Distribue les premieres cartes
                ((JeuBelote) jeu).distribue(0, nb, etat.getJoueur(), 0);
                setStep(getStep() + 1);            
                break; 
            }
            case EtatPartieBelote.ETAT_NON_DEMARRE :
            case EtatPartieBelote.ETAT_COUPE_JEU : 
            case EtatPartieBelote.ETAT_FIN_PARTIE : 
            case EtatPartieBelote.ETAT_FIN_TOUR : 
            case EtatPartieBelote.ETAT_JOUE_CARTE : 
            case EtatPartieBelote.ETAT_TOUR_ENCHERE1 : 
            case EtatPartieBelote.ETAT_TOUR_ENCHERE1_PRIS : 
            case EtatPartieBelote.ETAT_TOUR_ENCHERE2_PRIS : 
            case EtatPartieBelote.ETAT_TOUR_ENCHERE2 :
            case EtatPartieBelote.ETAT_FIN_DONNE :
            default : 
        }
    }

    /**
     * Un joueur vient de décider de prendre ou de ne pas prendre
     * @param couleur Couleur choisue (-1 si aucune)
     */    
    public abstract void impactePrise(int couleur);
    
    /**
     * Indique si le joueur peut prendre à une certaine couleur
     * @param couleur couleur questionnée
     * @return true s'il peut
     */
    public abstract boolean peutPrendreA(int couleur);

    /**
     * Un joueur vient de jouer une carte
     * @param aCarte Carte jouée
     */
    public void impacteJoueCarte(Couleur aCarte) {
        
        switch (etat.getEtat()) {
            case EtatPartieBelote.ETAT_JOUE_CARTE : {
                // Mise à jour du jeu du joueur & Mise à jour du tapis
                if (jeu.joueCarte(etat.getJoueur(), aCarte)) {
                    // La carte est bel et bien jouée
                    // Mise à jour de l'ouvreur
                    if (jeu.getTapis().size() == 1) {
                        ouvreur = etat.getJoueur();
                    }
                
                    // Annonce la Belote / Rebelote si besoin
                    for (int i = 0; i < ((JeuBelote) jeu).getBelote()[etat.getJoueur()].size(); i++) {
                        AnnonceBelote aBelote = (AnnonceBelote) ((JeuBelote) jeu).getBelote()[etat.getJoueur()].get(i);
                    
                        for (int j = 0; j < aBelote.getCartes().length; j++) {
                            if (aBelote.getCartes()[j].equals(aCarte)) {
                                // Il faut annoncer quelque chose ...
                                if (aBelote.isAnnonceFaite()) {
                                    currentEvent = getParticipant(etat.getJoueur()).getPseudo() + " annonce rebelote";  
                                }
                                else {
                                    currentEvent = getParticipant(etat.getJoueur()).getPseudo() + " annonce belote";  
                                }
                                aBelote.setAnnonceFaite(true);
                            }
                        }
                    }
                
                    // Mise à jour de l'état
                    etat.etatSuivant(EtatPartieBelote.ACTION_JOUE, etat.getJoueur());
                
                    // Fin du tour
                    if ((etat.getEtat() == EtatPartieBelote.ETAT_FIN_TOUR) 
                        || (etat.getEtat() == EtatPartieBelote.ETAT_FIN_PARTIE)) {
                    
                        // Fin du tour : Détermine qui est maitre
                        Couleur aCarteMaitresse = getMaitresse();
                        for (int i = 0; i < getJeu().getTapis().size(); i++) {
                            Couleur aCarteTapis = (Couleur) getJeu().getTapis().get(i);
                        
                            if (aCarteTapis.equals(aCarteMaitresse)) {
                                etat.setJoueur(i + ouvreur);
                            }
                        }
                    }
                    setStep(getStep() + 1);            
                }
                break; 
            }
            case EtatPartieBelote.ETAT_TOUR_ENCHERE1 :
            case EtatPartieBelote.ETAT_TOUR_ENCHERE2 :
            case EtatPartieBelote.ETAT_DISTRIBUTION :
            case EtatPartieBelote.ETAT_NON_DEMARRE :
            case EtatPartieBelote.ETAT_COUPE_JEU : 
            case EtatPartieBelote.ETAT_FIN_PARTIE : 
            case EtatPartieBelote.ETAT_FIN_TOUR : 
            case EtatPartieBelote.ETAT_TOUR_ENCHERE1_PRIS : 
            case EtatPartieBelote.ETAT_TOUR_ENCHERE2_PRIS : 
            case EtatPartieBelote.ETAT_FIN_DONNE :
            default : 
        }
    }

    /**
     * Ramasse les cartes formant le plis
     */
    public void impacteRamasse() {
        switch (etat.getEtat()) {
            case EtatPartieBelote.ETAT_FIN_PARTIE : 
            case EtatPartieBelote.ETAT_FIN_DONNE :
            case EtatPartieBelote.ETAT_FIN_TOUR : {

                // Ramasse les cartes
                jeu.ramasseTapis(etat.getJoueur());
                
                dernierRamasse = etat.getJoueur();
                
                etat.etatSuivant(EtatPartieBelote.ACTION_RAMASSE, etat.getJoueur());
                
                if (etat.getEtat() == EtatPartieBelote.ETAT_FIN_DONNE) {
                    // Calcule les points
                    calculePoints();
                    
                    // Ajoute un événement indiquant le score
                    currentEvent = "";
                    int scorePreneur = getScoreDonne()[getPreneur() % 2];
                    int scoreAutre = getScoreDonne()[(getPreneur() + 1) % 2]; 
                    if (scorePreneur > scoreAutre) {
                        currentEvent = "La prise est gagnée : " + scorePreneur + " contre " + scoreAutre; 
                    }
                    else if (scorePreneur < scoreAutre) {
                        currentEvent = "La prise est perdue : " + scorePreneur + " contre " + scoreAutre; 
                    }
                    else {
                        currentEvent = "La prise est en litige : " + scorePreneur + " contre " + scoreAutre; 
                    }
                }
                else {
                    currentEvent = jeu.getMains(0).size() + " carte(s) en main";
                }
                setStep(getStep() + 1);            
                break;
            }
            case EtatPartieBelote.ETAT_TOUR_ENCHERE1 :
            case EtatPartieBelote.ETAT_TOUR_ENCHERE2 : 
            case EtatPartieBelote.ETAT_DISTRIBUTION :
            case EtatPartieBelote.ETAT_NON_DEMARRE :
            case EtatPartieBelote.ETAT_COUPE_JEU : 
            case EtatPartieBelote.ETAT_JOUE_CARTE : 
            case EtatPartieBelote.ETAT_TOUR_ENCHERE1_PRIS : 
            case EtatPartieBelote.ETAT_TOUR_ENCHERE2_PRIS : 
            default : 
        }
    }

    /**
     * Ramasse les cartes formant le plis
     */
    public void impacteScoreVu() {
        switch (etat.getEtat()) {
            case EtatPartieBelote.ETAT_FIN_PARTIE : 
            case EtatPartieBelote.ETAT_FIN_DONNE : {
                // Le score a été vu : On passe à la donne suivante

                // Partie finie ?
                boolean partieFinie = false;                
                for (int j = 0; j < (PartieBelote.NB_JOUEUR / 2); j++) {
                    int total = 0;
                    for (int numTour = 0; numTour < getScores().size(); numTour++) {
                        total += ((int[]) getScores().get(numTour))[j];
                    }
                    if (total >= getScoreMaxPartie()) {
                        partieFinie = true;
                    }
                }

                if (partieFinie) {
                    etat.etatSuivant(EtatPartieBelote.ACTION_QUITTE, -1);
                    currentEvent = "La partie est finie";
                }
                else {
                    etat.etatSuivant(EtatPartieBelote.ACTION_GAGNE, -1);
                    // currentEvent = "La donne est terminée";
                }

                // Reforme le jeu
                getJeu().regroupePlis();

                addNextEvent();
                setStep(getStep() + 1);            

                break;
            }
            case EtatPartieBelote.ETAT_FIN_TOUR :
            case EtatPartieBelote.ETAT_TOUR_ENCHERE1 :
            case EtatPartieBelote.ETAT_TOUR_ENCHERE2 : 
            case EtatPartieBelote.ETAT_DISTRIBUTION :
            case EtatPartieBelote.ETAT_NON_DEMARRE :
            case EtatPartieBelote.ETAT_COUPE_JEU : 
            case EtatPartieBelote.ETAT_JOUE_CARTE : 
            case EtatPartieBelote.ETAT_TOUR_ENCHERE1_PRIS : 
            case EtatPartieBelote.ETAT_TOUR_ENCHERE2_PRIS : 
            default : 
        }
    }

    /**
     * Indique si le joueur peut jouer la carte
     * @param aCarte carte qui va être jouée
     * @return true s'il peut
     */
    public boolean peutJouer(Couleur aCarte) {
        switch (etat.getEtat()) {
            case EtatPartieBelote.ETAT_JOUE_CARTE : {
                /**
                 * La carte est bonne si :
                 * - C'est la première
                 * - Elle est de la même couleur que la première ET :
                 *   - ce n'est pas de l'atout
                 *   - c'est de l'atout ET elle est au dessus de la plus forte
                 *   - c'est de l'atout ET aucune carte du jeu n'est supérieure à la plus forte
                 * - Elle n'est pas de la même couleur ET il n'a pas de la couleur demandée et c'est de l'atout ET :
                 *   - elle est au dessus de la plus forte
                 *   - son partenaire est maitre
                 *   - aucune carte du jeu n'est supérieure à la plus forte
                 * - Elle n'est pas de la même couleur ET il n'a pas de la couleur demandée et c'est pas de l'atout ET :
                 *   - Son partenaire est maitre
                 *   - Aucune carte d'atout est dans le jeu
                 */
                boolean carteOk = false;
                
                if (getJeu().getTapis().size() == 0) {
                    // 1ère carte du tour
                    carteOk = true;
                }
                else if (aCarte.getCouleur() == ((Couleur) getJeu().getTapis().get(0)).getCouleur()) {
                    // Le joueur a fourni
                    if (!(aCarte instanceof AtoutBelote)) {
                        // Ce n'est pas de l'atout
                        carteOk = true;
                    }
                    else if ((getMaitresse().compareTo(aCarte) < 0) 
                            && (Math.abs(getMaitresse().compareTo(aCarte)) != CouleurBelote.COULEUR_DIFF)) {
                        // Atout
                        // Il a monté
                        carteOk = true;
                    }
                    else {
                        // C'est encore de l'atout
                        // Vérification qu'aucune carte ne peut être supérieure
                        boolean foundSup = false;
                        for (int i = 0; !foundSup && (i < getJeu().getMains(etat.getJoueur()).size()); i++) {
                            Couleur anotherCarte = (Couleur) getJeu().getMains(etat.getJoueur()).get(i);
                            
                            if ((getMaitresse().compareTo(anotherCarte) < 0) 
                                && (Math.abs(getMaitresse().compareTo(anotherCarte)) != CouleurBelote.COULEUR_DIFF)) {
                                    
                                // Il aurait pu monter !
                                foundSup = true;
                            }
                        }
                        carteOk = !foundSup;
                    }
                }
                else if (aCarte instanceof AtoutBelote) {
                    // Le joueur à coupé
                    
                    // Vérification qu'il n'a pas de la couleur demandée
                    boolean foundOne = false;
                    for (int i = 0; !foundOne && (i < getJeu().getMains(etat.getJoueur()).size()); i++) {
                        Couleur anotherCarte = (Couleur) getJeu().getMains(etat.getJoueur()).get(i);
                            
                        if (anotherCarte.getCouleur() == ((Couleur) getJeu().getTapis().get(0)).getCouleur()) {
                            // Il aurait pu fournir
                            foundOne = true;
                        }
                    }
                    
                    if (!foundOne) {
                    
                        if ((getMaitresse().compareTo(aCarte) < 0) 
                                && (Math.abs(getMaitresse().compareTo(aCarte)) != CouleurBelote.COULEUR_DIFF)) {
                            // Il a monté
                            carteOk = true;
                        }
                        else if ((getJeu().getTapis().size() >= 2) 
                                && (getMaitresse().equals(getJeu().getTapis().get(getJeu().getTapis().size() - 2)))) {
                            // Vérification si le partenaire est maitre
                                
                            // Le partenaire est maitre
                            carteOk = true;
                        }
                        else {
                            // Vérification qu'aucune carte ne peut être supérieure
                            boolean foundSup = false;
                            for (int i = 0; !foundSup && (i < getJeu().getMains(etat.getJoueur()).size()); i++) {
                                Couleur anotherCarte = (Couleur) getJeu().getMains(etat.getJoueur()).get(i);
                                
                                if ((getMaitresse().compareTo(anotherCarte) < 0) 
                                    && (Math.abs(getMaitresse().compareTo(anotherCarte)) != CouleurBelote.COULEUR_DIFF)) {
                                        
                                    // Il aurait pu monter !
                                    foundSup = true;
                                }
                            }
                            carteOk = !foundSup;
                        }
                    }
                        
                }
                else {
                    // Le joueur s'est défaussé

                    // Vérification qu'il n'a pas de la couleur demandée
                    boolean foundOne = false;
                    for (int i = 0; !foundOne && (i < getJeu().getMains(etat.getJoueur()).size()); i++) {
                        Couleur anotherCarte = (Couleur) getJeu().getMains(etat.getJoueur()).get(i);
                            
                        if (anotherCarte.getCouleur() == ((Couleur) getJeu().getTapis().get(0)).getCouleur()) {
                            // Il aurait pu fournir
                            foundOne = true;
                        }
                    }
                    
                    if (!foundOne) {
                        if ((getJeu().getTapis().size() >= 2) 
                                && (getMaitresse().equals(getJeu().getTapis().get(getJeu().getTapis().size() - 2)))) {
                            // Vérification si le partenaire est maitre
                                
                            // Le partenaire est maitre
                            carteOk = true;
                        }
                        else {
                            // Vérification qu'il ne pouvait pas couper
                            boolean foundAtout = false;
                            for (int i = 0; !foundAtout && (i < getJeu().getMains(etat.getJoueur()).size()); i++) {
                                Couleur anotherCarte = (Couleur) getJeu().getMains(etat.getJoueur()).get(i);
                                
                                if (anotherCarte instanceof AtoutBelote) {
                                    // Il aurait pu couper !
                                    foundAtout = true;
                                }
                            }
                            carteOk = !foundAtout;
                        }
                    }
                }
                
                return carteOk;
            }
            case EtatPartieBelote.ETAT_FIN_TOUR : 
                // En fin de tour = début du suivant
                // On peut tout jouer
                return true;
            case EtatPartieBelote.ETAT_FIN_DONNE :
            case EtatPartieBelote.ETAT_TOUR_ENCHERE1 :
            case EtatPartieBelote.ETAT_TOUR_ENCHERE2 :
            case EtatPartieBelote.ETAT_DISTRIBUTION :
            case EtatPartieBelote.ETAT_NON_DEMARRE :
            case EtatPartieBelote.ETAT_COUPE_JEU : 
            case EtatPartieBelote.ETAT_FIN_PARTIE : 
            case EtatPartieBelote.ETAT_TOUR_ENCHERE1_PRIS : 
            case EtatPartieBelote.ETAT_TOUR_ENCHERE2_PRIS : 
            default : 
                return false;
        }
        
    }
    
    /**
     * Calcule la carte maîtresse du tapis 
     * @return la carte trouvée (éventuellement null)
     */
    public Couleur getMaitresse() {
        Couleur maitresse = null;
        
        for (int i = 0; i < getJeu().getTapis().size(); i++) {
            Couleur aCarte = (Couleur) getJeu().getTapis().get(i);
            
            if (maitresse == null) {
                maitresse = aCarte;
            }
            else if ((maitresse.compareTo(aCarte) < 0) 
                    && (Math.abs(maitresse.compareTo(aCarte)) != CouleurBelote.COULEUR_DIFF)) {
                maitresse = aCarte;
            }
        }
        return maitresse;
    }

    /**
     * Calcule les points de la partie 
     */
    public void calculePoints() {
        
        calculeScoreDonne();
        
        int coef = getCoef();
        
        int[] scoreDistribue = new int[NB_JOUEUR];
        
        // Qui gagne ?
        if (scoreDonne[preneur % 2] < scoreDonne[(preneur + 1) % 2]) {
            // Perte
            scoreDistribue[(preneur + 1) % 2] = coef * (scoreDonne[(preneur + 1) % 2] + scoreDonne[preneur % 2] - scoreAnnNonPrennables[preneur % 2]) 
                                                + litige[preneur % 2] + litige[(preneur + 1) % 2];
            scoreDistribue[preneur % 2] = coef * scoreAnnNonPrennables[preneur % 2];  
            litige[(preneur + 1) % 2] = 0;
            litige[preneur % 2] = 0;
        }
        else if (scoreDonne[preneur % 2] == scoreDonne[(preneur + 1) % 2]) {
            // Litige : Accumulation si plusieurs litiges de suite
            litige[preneur % 2] += coef * scoreDonne[preneur % 2];
            scoreDistribue[(preneur + 1) % 2] = coef * scoreDonne[(preneur + 1) % 2];
            scoreDistribue[preneur % 2] = 0;  
        }
        else {
            scoreDistribue[(preneur + 1) % 2] = coef * scoreDonne[(preneur + 1) % 2];
            scoreDistribue[preneur % 2] = coef * scoreDonne[preneur % 2]  + litige[preneur % 2] + litige[(preneur + 1) % 2];  
            litige[(preneur + 1) % 2] = 0;
            litige[preneur % 2] = 0;
        }
        scores.add(scoreDistribue);
    }

    /**
     * @return Coefficient des scores
     */
    protected abstract int getCoef();
    
    /**
     * Un joueur indique s'il veut rejouer
     * @param aJoueur Joueur qui dit
     * @param b Décision
     * @return Indicateur si la partie redémarre
     */
    public boolean impacteVeutRejouer(Joueur aJoueur, boolean b) {

        boolean restart = false;
        
        super.impacteVeutRejouer(aJoueur, b);
        
        if (nbJoueurRejoue >= participant.length) {
            // Partie complète : Change d'état
            etat.etatSuivant(EtatPartieBelote.ACTION_INITIALISE, etat.getJoueur());
            currentEvent = "La partie recommence";
            
            // Réinit le jeu
            restart();
            restart = true;  
        }
        setStep(getStep() + 1);            
        
        return restart;
    }

    /**
     * Recommence une partie : Initialise tout ce qui est nécessaire
     */
    protected void restart() {
        super.restart();

        scores.add(new int[NB_JOUEUR / 2]);
        // Indicateur des annonces reste identique
        preneur = -1;
        ouvreur = -1;
        atout = -1;
        dernierRamasse = -1;
        // Le jeu est conservé
        scoreDonne = new int[PartieBelote.NB_JOUEUR / 2];
        litige = new int[PartieBelote.NB_JOUEUR / 2];
        scoreAnnNonPrennables = new int[PartieBelote.NB_JOUEUR / 2];
        scoreAnnPrennables = new int[PartieBelote.NB_JOUEUR / 2];
        
    }

    /**
     * Calcul le score de la donnee
     */
    protected void calculeScoreDonne() {
        capot = -1;
        
        for (int i = 0; i < scoreDonne.length; i++) {
            
            scoreDonne[i] = 0;
            
            for (int iPlis = 0; iPlis < getJeu().getPlis(i).size(); iPlis++) {
                Couleur aCarte = (Couleur) getJeu().getPlis(i).get(iPlis);
                scoreDonne[i] += aCarte.getValeur();
            }
            
            if (getJeu().getPlis(i).size() == 0) {
                capot = i;
            }
        }
        
        // 10 de der, capot
        if (isCapot()) {
            scoreDonne[(capot + 1) % 2] += 100;
            // Le capot fait basculer les annonces prennables
            scoreAnnPrennables[(capot + 1) % 2] += scoreAnnPrennables[capot % 2]; 
            scoreAnnPrennables[capot % 2] = 0; 
        }
        else {
            scoreDonne[dernierRamasse % 2] += 10;
        }

        // Ajoutes les annonces prennables
        scoreDonne[0] += scoreAnnPrennables[0];
        scoreDonne[1] += scoreAnnPrennables[1];

        // Ajoutes les annonces non prennables
        scoreDonne[0] += scoreAnnNonPrennables[0];
        scoreDonne[1] += scoreAnnNonPrennables[1];
    }

    /**
     * Retourne la chaîne de caractère correspondante à la couleur de l'atout
     * @return chaine correspondante
     */
    public String atoutToString() {
        switch (atout) {
            case Couleur.PIQUE :
                return "Pique";
            case Couleur.TREFLE :
                return "Trèfle";
            case Couleur.COEUR :
                return "Coeur";
            case Couleur.CARREAU :
                return "Carreau";
            default :
                return "?";
        }
    }

    /**
     * Construit une carte en tenant compte si c'est de l'atout ou pas
     * @param hauteur Hauteur de la carte
     * @param couleur Couleur de la carte
     * @return Carte créée
     */
    public Couleur buildCarte(int hauteur, int couleur) {
        if (isAtoutFinal() && (couleur == atout)) {
            return new AtoutBelote(hauteur, couleur);
        }
        else {
            return new CouleurBelote(hauteur, couleur);
        }
    }
    
    /**
     * Indique si l'atout est définitif ou pas par rapport à l'avancement de la partie
     * @return true si c'est le cas
     */
    protected boolean isAtoutFinal() {
        return (etat.getEtat() != EtatPartieBelote.ETAT_TOUR_ENCHERE1)
            && (etat.getEtat() != EtatPartieBelote.ETAT_TOUR_ENCHERE2)
            && (etat.getEtat() != EtatPartieBelote.ETAT_TOUR_ENCHERE1_PRIS)
            && (etat.getEtat() != EtatPartieBelote.ETAT_TOUR_ENCHERE2_PRIS);
    }


    /**
     * Initialise les score des annonces
     */
    protected void initScoreAnnonce() {
        // Reset des scores
        scoreAnnNonPrennables = new int[PartieBelote.NB_JOUEUR / 2];
        scoreAnnPrennables = new int[PartieBelote.NB_JOUEUR / 2];
        
        Vector[] annonces = ((JeuBelote) jeu).getAnnonces();
        Vector[] belotes = ((JeuBelote) jeu).getBelote();
        
        for (int iJoueur = 0; iJoueur < annonces.length; iJoueur++) {
            for (int i = 0; i < annonces[iJoueur].size(); i++) {
                AnnonceBelote anAnnonce = (AnnonceBelote) annonces[iJoueur].get(i);
                
                scoreAnnPrennables[iJoueur % 2] += anAnnonce.getValeur();
            }
        }

        for (int iJoueur = 0; iJoueur < belotes.length; iJoueur++) {
            for (int i = 0; i < belotes[iJoueur].size(); i++) {
                AnnonceBelote aBelote = (AnnonceBelote) belotes[iJoueur].get(i);
                
                scoreAnnNonPrennables[iJoueur % 2] += aBelote.getValeur();
            }
        }
    }

    /**
     * Filtre les annonces en fonction de l'ordre d'annonce et des précédences
     * @param annonces Tableau d'annonces par joueur
     * @return événements associés
     */
    protected String filtreAnnonce(Vector[] annonces) {
        
        Annonce annonceMax = null;
        int joueurMax = -1;
        String event = "";
        Vector[] annoncesRefus = new Vector[annonces.length];
        for (int i = 0; i < annonces.length; i++) {
            annoncesRefus[i] = new Vector();
        }

        for (int iJoueur = 0; iJoueur < PartieBelote.NB_JOUEUR; iJoueur++) {
            int joueur = (iJoueur + (etat.getJoueurCoupe() + 2)) % PartieBelote.NB_JOUEUR;

            // !TODO : Le joueur ne doit annoncer qu'une tierce qui est le max tant que le camp n'est pas déterminé            
            for (int i = 0; i < annonces[joueur].size(); i++) {
                Annonce anAnnonce = (Annonce) annonces[joueur].get(i);
                
                if ((annonceMax != null) && (anAnnonce.compareTo(annonceMax) <= 0)) {
                    // Annonce annulée car une précédente prime
                    annoncesRefus[joueur].add(annonces[joueur].remove(i));
                    // Tiens compte que l'élément a été retiré
                    i--;
                }
                else {
                    // Annonce valide
                    annonceMax = anAnnonce;
                    joueurMax = joueur;
                    if (event.length() > 0) {
                        event = event + "\n";
                    }
                    event = event + participant[joueur].getPseudo() + " annonce : " + anAnnonce.getDescription();
                }
            }
        }
        
        // Ne conserve que les annonces correspondante à l'équipe du Max
        for (int iJoueur = 0; iJoueur < PartieBelote.NB_JOUEUR; iJoueur++) {
            int joueur = (iJoueur + (etat.getJoueurCoupe() + 2)) % PartieBelote.NB_JOUEUR;

            if ((joueur % (PartieBelote.NB_JOUEUR / 2)) == (joueurMax % (PartieBelote.NB_JOUEUR / 2))) {            
                // Ajoute les annonces refusées
                for (int i = 0; i < annoncesRefus[joueur].size(); i++) {
                    Annonce anAnnonce = (Annonce) annoncesRefus[joueur].get(i);
                
                    annonces[joueur].add(anAnnonce);

                    if (event.length() > 0) {
                        event = event + "\n";
                    }
                    event = event + participant[joueur].getPseudo() + " annonce : " + anAnnonce.getDescription();
                }
            }
            else {
                // Supprime les annonces acceptées
                for (int i = 0; i < annonces[joueur].size(); i++) {
                    annonces[joueur].remove(i);
                    i--;
                }
            }
        }
        
        return event;
    }


    /**
     * @see com.increg.game.client.Partie#addNextEvent()
     */
    protected void addNextEvent() {

        switch (etat.getEtat()) {
            case EtatPartieBelote.ETAT_COUPE_JEU :
                if (currentEvent.length() > 0) {
                    currentEvent = currentEvent + "\n";  
                }
                currentEvent = currentEvent + getParticipant(etat.getJoueur()).getPseudo() + " doit couper";
                break;

            default :
                break;
        }
    }

    /**
     * @see com.increg.game.client.Partie#joueurSeLeve(com.increg.game.client.Joueur)
     */
    public boolean joueurSeLeve(Joueur aJoueur) {
        boolean res = super.joueurSeLeve(aJoueur);
        
        if (etat.getEtat() == EtatPartieBelote.ETAT_FIN_PARTIE) {
            etat.setEtat(EtatPartieBelote.ETAT_NON_DEMARRE);
        }

        return res;
    }

}
