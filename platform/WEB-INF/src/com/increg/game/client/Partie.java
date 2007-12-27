/*
 * Created on 27 avr. 2003
 *
 * Partie en cours ou planifi�e
 */
package com.increg.game.client;

import java.util.Map;
import java.util.Vector;

import com.increg.game.client.belote.EtatPartieBelote;

/**
 * @author Manu
 *
 * Partie en cours ou planifi�e
 */
public abstract class Partie implements Comparable {

    /**
     * Identifiant de la partie (chrono sans signification)
     */
    protected int identifiant;
     
    /**
     * Joueur propri�taire de la partie
     */
    protected Joueur owner;

    /**
     * Joueurs de la partie
     */
    protected Joueur[] participant;
    
    /**
     * Derniers Joueurs de la partie (au cas o� qq'un est parti en route)
     */
    protected Joueur[] dernierParticipant;

    /**
     * Joueurs spectateurs
     */
    protected Vector<Joueur> spectateurs;

    /**
     * Titre de la partie
     */
    protected String titre;

    /**
     * Mot de passe pour les parties priv�es : Chaine vide sinon
     */
    protected String motDePasse;
    
    /**
     * Score Vector des scores des diff�rents "tours" jou�s
     */
    protected Vector<int[]> scores;
    
    /**
     * Etat de la partie en cours
     */ 
    protected EtatPartie etat;
    
    /**
     * Jeu de cartes
     */
    protected JeuCartes jeu;

    /**
     * Score de la donne
     */
    protected int[] scoreDonne;

    /**
     * Message repr�sentant l'�v�nement en cours
     */
    protected String currentEvent;

    /**
     * Nombre de joueur voulant rejouer 
     */
    protected int nbJoueurRejoue;
    
    /**
     * Tournoi de la partie (peut �tre null)
     */
    protected Tournoi myTournoi;
    
    /**
     * Num�ro d'�tape de la partie : Permet de g�rer la s�quence 
     * et de savoir si cela correspond � un vieil �tat ou pas
     */
    protected long step;
    
    /**
     * Score max de la partie
     */
    protected int scoreMaxPartie;
    
    /* ***************************************
     * M�thodes
     * *************************************** */

    /**
     * Constructeur de la partie
     *
     */
    public Partie() {
        spectateurs = new Vector<Joueur>();
        motDePasse = "";
        titre = "";
        scores = new Vector<int[]>();
        etat = new EtatPartieBelote();
        identifiant = 0;
        currentEvent = "";
        nbJoueurRejoue = 0;
        myTournoi = null;
    }
    
    /**
     * @return Joueur propri�taire de la partie
     */
    public Joueur getOwner() {
        return owner;
    }

    /**
     * @param joueur Joueur propri�taire de la partie
     */
    public void setOwner(Joueur joueur) {
        owner = joueur;
    }

    /**
     * @return Identifiant de la partie (chrono sans signification)
     */
    public int getIdentifiant() {
        return identifiant;
    }

    /**
     * @param i Identifiant de la partie (chrono sans signification)
     */
    public void setIdentifiant(int i) {
        identifiant = i;
    }

    /**
     * @return Joueurs de la partie
     */
    public Joueur[] getParticipant() {
        return participant;
    }

    /**
     * @param i indice du joueur
     * @return un joueur de la partie
     */
    public Joueur getParticipant(int i) {
        return participant[i];
    }

    /**
     * @return Derniers Joueurs de la partie
     */
    public Joueur[] getDernierParticipant() {
        return dernierParticipant;
    }

    /**
     * @param i indice du joueur
     * @return un joueur de la partie
     */
    public Joueur getDernierParticipant(int i) {
        return dernierParticipant[i];
    }

    /**
     * @return Scores de la partie
     */
    public Vector<int[]> getScores() {
        return scores;
    }

    /**
     * @return Scores de la partie
     */
    public int[] getCurrentScore() {
        if (scores.size() > 0) {
            return scores.get(scores.size() - 1);
        }
        else {
            return null;
        }
    }

    /**
     * @param i indice du joueur / �quipe
     * @return un score de la partie
     */
    public int getCurrentScore(int i) {
        int[] currentScore = getCurrentScore();
        if (currentScore != null) {
            return currentScore[i];
        }
        else {
            return 0;
        }
    }

    /**
     * @return Joueurs spectateurs
     */
    public Vector<Joueur> getSpectateurs() {
        return spectateurs;
    }

    /**
     * @param i indice du joueur
     * @return un joueur spectateur
     */
    public Joueur getSpectateurs(int i) {
        return spectateurs.get(i);
    }

    /**
     * @param joueurs Joueurs de la partie
     */
    public void setParticipant(Joueur[] joueurs) {
        participant = joueurs;
        dernierParticipant = joueurs;
    }

    /**
     * @param joueur Joueur qui rejoint la partie
     * @param position Position prise par le joueur
     */
    public void addParticipant(Joueur joueur, int position) {
        participant[position] = joueur;
        dernierParticipant[position] = joueur;
    }

    /**
     * @param fullScores Scores en cours
     */
    public void setScore(Vector<int[]> fullScores) {
        scores = fullScores;
    }

    /**
     * @param aScore Scores en cours
     */
    public void addScore(int[] aScore) {
        scores.add(aScore);
    }

    /**
     * @param aScore Score � attribuer
     * @param position Position prise par le joueur
     */
    public void addScore(int aScore, int position) {
        getCurrentScore()[position] = aScore;
    }

    /**
     * @return Nombre de participants
     */
    public int getNbParticipant() {
        int nb = 0;
        for (int i = 0; i < participant.length; i++) {
            if (participant[i] != null) {
                nb++;
            }
        }
        return nb;
    }
    
    /**
     * @param vector Joueurs spectateurs 
     */
    public void setSpectateurs(Vector<Joueur> vector) {
        spectateurs = vector;
    }

    /**
     * @param joueur Joueur qui vient regarder 
     */
    public void addSpectateur(Joueur joueur) {
        spectateurs.add(joueur);
    }

    /**
     * @return Titre de la partie
     */
    public String getTitre() {
        return titre;
    }

    /**
     * @param string Titre de la partie
     */
    public void setTitre(String string) {
        titre = string;
    }

    /**
     * @return Mot de passe pour les parties priv�es : Chaine vide sinon
     */
    public String getMotDePasse() {
        return motDePasse;
    }

    /**
     * @param string Mot de passe pour les parties priv�es : Chaine vide sinon
     */
    public void setMotDePasse(String string) {
        motDePasse = string;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o) {
        if ((identifiant != 0)
                || (((Partie) o).getIdentifiant() != 0)) {
            return (identifiant - ((Partie) o).getIdentifiant());
        }
        else {
            // Tous les deux nul
            return titre.compareTo(((Partie) o).getTitre());
        }
    }

    /**
     * @return Indique si la partie est priv�e
     */
    public boolean isPrivate() {
        return ((motDePasse != null) && (motDePasse.length() > 0));
    }

    /**
     * @return Tournoi de la partie (peut �tre null)
     */
    public Tournoi getMyTournoi() {
        return myTournoi;
    }

    /**
     * @param tournoi Tournoi de la partie (peut �tre null)
     */
    public void setMyTournoi(Tournoi tournoi) {
        myTournoi = tournoi;
    }

    /* ***************************************
     * M�thodes fonctionnelles
     * *************************************** */
     
    /**
     * @param aJoueur Joueur � chercher
     * @return Indique si un joueur participe ou est spectateur d'une partie
     */
    public boolean joueurVoitPartie(Joueur aJoueur) {
        
        boolean res = false;

        if (participant != null) {        
            for (int i = 0; (!res && (i < participant.length)); i++) {
                if (participant[i] != null) {
                    res = participant[i].equals(aJoueur);
                }
            }
        }
        
        for (int i = 0; (!res && (i < spectateurs.size())); i++) {
            res = spectateurs.get(i).equals(aJoueur);
        }
        return res;
    }

    /**
     * Un joueur se l�ve de la partie
     * @param aJoueur Joueur qui sort
     * @return indicateur si le joueur a bien �t� retir� 
     */
    public boolean joueurSeLeve(Joueur aJoueur) {

        boolean found = false;        
        for (int i = 0; i < participant.length; i++) {
            if ((participant[i] != null) && (participant[i].equals(aJoueur))) {
                participant[i] = null;
                found = true;
                // Si c'�tait le propri�taire, il faut r�affecter
                if (owner.equals(aJoueur)) {
                    // Recherche un autre joueur
                    owner = null;
                    for (int j = 0; (owner == null) && (j < participant.length); j++) {
                        if (participant[j] != null) {
                            owner = participant[j];
                        }
                    }
                }
                // Reset des joueurs qui rejoue car �a n'a plus de sens
                nbJoueurRejoue = 0;
                // C'est un participant : La partie change d'�tat global
                setStep(getStep() + 1);            
            }
        }
        // Dans les spectateurs
        for (int i = 0; !found && (i < spectateurs.size()); i++) {
            Joueur anotherJoueur = getSpectateurs(i); 
            if (anotherJoueur.equals(aJoueur)) {
                found = true;
                spectateurs.remove(i);
            }
        }
        
        return found;
    }
    
    /**
     * Un joueur rejoint la partie
     * @param aJoueur Joueur qui sort
     * @param position Position du joueur
     * @return indicateur si le joueur a bien �t� ajout�
     */
    public boolean joueurRejoint(Joueur aJoueur, int position) {

        boolean found = false;
        boolean done = false;
        /**
         * R�gle de gestion :
         * - Le joueur ne peut �tre 2 fois dans la partie
         * - Un spectateur peut devenir joueur, mais il n'est plus spectateur
         */
        for (int i = 0; i < participant.length; i++) {
            if ((participant[i] != null) && (participant[i].equals(aJoueur))) {
                found = true;
            }
        }

        if (!found) {
            // La place est libre ?
            if (participant[position] == null) {
                // Effectue l'ajout
                participant[position] = aJoueur;
                dernierParticipant[position] = aJoueur;
                done = true;
                // Si pas de propri�taire, il le devient
                if (owner == null) {
                    owner = aJoueur;
                }
                
                // V�rification s'il �tait spectateur
                for (int i = 0; !found && (i < spectateurs.size()); i++) {
                    Joueur anotherJoueur = getSpectateurs(i); 
                    if (anotherJoueur.equals(aJoueur)) {
                        // Oui : Alors on le supprime
                        found = true;
                        spectateurs.remove(i);
                    }
                }

                // Reset des joueurs qui rejoue car �a n'a plus de sens
                nbJoueurRejoue = 0;
                // C'est un participant : La partie change d'�tat global
                setStep(getStep() + 1);            
            }
        }
        
        return done;
    }

    /**
     * Un joueur rejoint la partie en tant que spectateur
     * @param aJoueur Joueur qui sort
     * @return indicateur si le joueur a bien �t� ajout�
     */
    public boolean joueurRejoint(Joueur aJoueur) {

        boolean found = false;
        /**
         * R�gles de gestion :
         * - Pas de doublons dans les spectateurs
         * - Un participant ne peut pas �tre spectateur
         */
        for (int i = 0; i < participant.length; i++) {
            if ((participant[i] != null) && (participant[i].equals(aJoueur))) {
                found = true;
            }
        }
        for (int i = 0; !found && (i < spectateurs.size()); i++) {
            Joueur anotherJoueur = getSpectateurs(i); 
            if (anotherJoueur.equals(aJoueur)) {
                found = true;
            }
        }

        if (!found) {
            // Insertion � faire
            addSpectateur(aJoueur);
        }
        
        return !found;
    }

    /**
     * Recherche la position du joueur dans la partie
     * @param aJoueur Joueur concern�
     * @return -1 si le joueur n'est pas dans la partie en tant que joueur, sinon la position 0, 1, ... 
     */
    public int getPositionJoueur(Joueur aJoueur) {

        int pos = -1;
        for (int i = 0; i < participant.length; i++) {
            if ((participant[i] != null) && (participant[i].equals(aJoueur))) {
                pos = i;
            }
        }
        
        return pos;
    }

    /**
     * @return Etat de la partie
     */
    public EtatPartie getEtat() {
        return etat;
    }

    /**
     * @param partie Etat de la partie
     */
    public void setEtat(EtatPartie partie) {
        etat = partie;
    }

    /**
     * Un joueur vient de couper
     * @param pos Position de la coupe
     */    
    public abstract void impacteCoupe(int pos);

    /**
     * @return Jeu de cartes
     */
    public JeuCartes getJeu() {
        return jeu;
    }

    /**
     * @return Score de la donne
     */
    public int[] getScoreDonne() {
        return scoreDonne;
    }

    /**
     * @param i Joueur ou �quipe concern�e
     * @return Score de la donne
     */
    public int getScoreDonne(int i) {
        return scoreDonne[i];
    }

    /**
     * @param is Score de la donne
     */
    public void setScoreDonne(int[] is) {
        scoreDonne = is;
    }

    /**
     * @param i Indice mis � jour
     * @param is Score de la donne
     */
    public void setScoreDonne(int i, int is) {
        scoreDonne[i] = is;
    }

    /**
     * @return Ev�nement en cours 
     */
    public String getCurrentEvent() {
        return currentEvent;
    }

    /**
     * @param string Ev�nement en cours 
     */
    public void setCurrentEvent(String string) {
        currentEvent = string;
    }

    /**
     * Ev�nement en cours 
     */
    public void resetCurrentEvent() {
        currentEvent = "";
    }
    
    /**
     * Un joueur indique s'il veut rejouer
     * @param aJoueur Joueur qui dit
     * @param b D�cision
     * @return Indicateur si la partie red�marre
     */
    public boolean impacteVeutRejouer(Joueur aJoueur, boolean b) {

        if (b) {
            nbJoueurRejoue++;
        
            currentEvent = aJoueur.getPseudo() + " veut rejouer";
        }
        else {
            currentEvent = aJoueur.getPseudo() + " ne veut pas rejouer";
        }
        
        return false;
    }

    /**
     * Recommence une partie : Initialise tout ce qui est n�cessaire
     */
    protected void restart() {

        // Les spectateurs sont identiques
        // Le mot de passe ne change pas        
        // Le titre ne change pas
        // R�initialise les score        
        scores.clear();
        // Etat est d�j� ok
        // identifiant ne bouge pas
        // currentEvent ne doit pas bouger
        nbJoueurRejoue = 0;
    }


    /**
     * Enrichi de l'�v�nement indiquant ce qui doit ce passer apr�s
     */
    protected void addNextEvent() {
    }

    /**
     * @return Indicateur si la partie est d�marr�e ou pas
     */
    public boolean isStarted() {
        return etat.isStarted();
    }

    /**
     * Fourni le score total d'une partie
     * @param i Joueur demand�
     * @return Total
     */    
    public int getScoreTotal(int i) {

        int total = 0;
        for (int numTour = 0; numTour < scores.size(); numTour++) {
            total += scores.get(numTour)[i];
        }

        return total;
    }
    
    
    /**
     * @return Num�ro d'�tape de la partie : Permet de g�rer la s�quence et de savoir si cela correspond � un vieil �tat ou pas
     */
    public long getStep() {
        return step;
    }

    /**
     * @param l Num�ro d'�tape de la partie : Permet de g�rer la s�quence et de savoir si cela correspond � un vieil �tat ou pas
     */
    public void setStep(long l) {
        step = l;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String res = "";
        if ((myTournoi != null) && (myTournoi.getIdentifiant() > 0)) {
            res += "Partie de tournoi";
        }
        return res;
    }

	/**
	 * @return Returns the scoreMaxPartie.
	 */
	public int getScoreMaxPartie() {
		return scoreMaxPartie;
	}

	/**
	 * @param scoreMaxPartie The scoreMaxPartie to set.
	 */
	protected void setScoreMaxPartie(int scoreMaxPartie) {
		this.scoreMaxPartie = scoreMaxPartie;
	}

	/**
	 * @param lstParam Param�tre de l'aire de jeu
	 */
	public abstract void setScoreMaxPartie(Map lstParam);
}
