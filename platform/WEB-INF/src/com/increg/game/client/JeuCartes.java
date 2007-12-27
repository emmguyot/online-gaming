/*
 * Created on 22 mai 2003
 *
 */
package com.increg.game.client;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

/**
 * @author Manu
 *
 * Jeu de n cartes
 */
public class JeuCartes {

    /**
     * Jeu initial
     */
    protected Vector<Carte> jeuInit;
    /**
     * Tas regroupé des cartes
     */
    protected Vector<Carte> tas;
    /**
     * Plis effectué
     */
    protected Vector<Carte>[] plis;
    /**
     * Cartes en main
     */
    protected Vector<Carte>[] mains;
    /**
     * Tapis
     */
    protected Vector<Carte> tapis;
    /**
     * Annonces des joueurs : Tableau indicé par le joueur et Vector d'Annonce
     */
    protected Vector<Annonce>[] annonces;

    /**
     * Constructeur
     * @param nbCarte Nombre de cartes dans le jeu
     * @param nbJoueur Nombre de joueurs dans le jeu
     */
    public JeuCartes(int nbCarte, int nbJoueur) {
        
        // Initialise les tableaux
        jeuInit = new Vector<Carte>(nbCarte);
        tas = new Vector<Carte>(nbCarte);
        plis = new Vector[nbJoueur];
        mains = new Vector[nbJoueur];
        tapis = new Vector<Carte>(nbJoueur);
        annonces = new Vector[nbJoueur];
        
        for (int i = 0; i < nbJoueur; i++) {
            plis[i] = new Vector<Carte>(nbCarte);
            mains[i] = new Vector<Carte>(nbCarte / nbJoueur);
            annonces[i] = new Vector<Annonce>();
        }
        
        // Création du jeu initial
        // Par les classes filles car cela peut être très exotique
    }

    /**
     * Mélange le tas à partir du jeu initial
     */    
    public void melangeTas() {
        
        // Recopie le jeu Initial
        tas.clear();
        tas.addAll(jeuInit);

        Random alea = null;
        try {
            alea = SecureRandom.getInstance("SHA1PRNH");
        }
        catch (NoSuchAlgorithmException e) {
            alea = new Random();
        }

        // 1000 intervertions !
        for (int i = 0; i < 1000; i++) {
            
            int indice1 = alea.nextInt(jeuInit.size());
            int indice2 = alea.nextInt(jeuInit.size());
            
            Carte carte1 = tas.get(indice1);
            Carte carte2 = tas.get(indice2);
            
            tas.set(indice2, carte1);
            tas.set(indice1, carte2);
        }
    }
    
    /**
     * Coupe le tas
     * @param pos position de coupe
     */
    public void coupe(int pos) {
        
        if (tas.size() != jeuInit.size()) {
            // Anomalie seulement sur le serveur
            if (System.getProperty("java.awt.headless") != null) {
                // On est sur le serveur
                new Exception("La taille du tas n'est pas correcte : " + tas.size() + " contre " + jeuInit.size()).printStackTrace();
            }
            // Reparre d'un tas neuf !
            melangeTas();
        }
        Vector<Carte> newTas = new Vector<Carte>(tas.size());
        newTas.addAll(tas.subList(pos, tas.size()));
        newTas.addAll(tas.subList(0, pos));

        tas = newTas;        
    }
    
    /**
     * @return Tableau par joueur de cartes en mains
     */
    public Vector<Carte>[] getMains() {
        return mains;
    }

    /**
     * @return Tableau par joueur des plis
     */
    public Vector<Carte>[] getPlis() {
        return plis;
    }

    /**
     * @param i indice du joueur
     * @return Cartes en main d'un joueur
     */
    public Vector<Carte> getMains(int i) {
        return mains[i];
    }

    /**
     * @param i indice du joueur
     * @return Plis du joueurs
     */
    public Vector<Carte> getPlis(int i) {
        return plis[i];
    }
    
    /**
     * @return Ensemble des cartes dans le tas
     */
    public Vector<Carte> getTas() {
        return tas;
    }

    /**
     * Regroupe les plis pour former le tas
     *
     */
    public void regroupePlis() {
        
        int tot = tas.size(); 
        // Vérification qu'on ne va pas perdre de cartes
        for (int i = 0; i < plis.length; i++) {
            tot += plis[i].size();
        }
        // Regroupement
        for (int i = 0; i < plis.length; i++) {
            tas.addAll(plis[i]);
        }


        if (tot != jeuInit.size()) {
            // Problème
            System.err.println("Regroupement des plis => Perte ou génération de cartes. Compte=" + tot + " Normal=" + jeuInit.size());
            Exception e = new Exception();
            e.printStackTrace();
            
            // Rattrapage en recréant le tas
            melangeTas();
        }
    }

    /**
     * Regroupe les mains pour former le tas
     *
     */
    public void regroupeMains() {
        
        int tot = tas.size(); 
        // Vérification qu'on ne va pas perdre de cartes
        for (int i = 0; i < mains.length; i++) {
            tot += mains[i].size();
        }
        if (tot != jeuInit.size()) {
            // Problème
            System.err.println(new Date().toString() + " Regroupement des mains => Perte ou génération de cartes");
            Exception e = new Exception();
            e.printStackTrace();
        }
        
        // Regroupement
        for (int i = 0; i < mains.length; i++) {
            tas.addAll(mains[i]);
        }
    }
    
    /**
     * Une carte est jouée par un joueur
     * @param joueur Joueur ayant joué
     * @param theCarte carte jouée
     * @return true si la carte a vraiment été jouée
     */
    public boolean joueCarte(int joueur, Carte theCarte) {
        
        // Retire la carte du jeu du joueur et place la sur le tapis
        boolean found = false;
        for (int i = 0; !found && (i < mains[joueur].size()); i++) {
            Carte aCarte = (Carte) mains[joueur].get(i);  
            if (aCarte.equals(theCarte)) {
                mains[joueur].remove(i);
                tapis.add(aCarte);
                found = true;
            }
        }
        if (!found) {
            System.err.println("La carte " + theCarte + " n'a pas été trouvée pour le joueur " + joueur);
        }
        
        return found;
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
     * @return tapis des cartes jouées
     */
    public Vector<Carte> getTapis() {
        return tapis;
    }

    /**
     * @param vectors Cartes en main
     */
    public void setMains(Vector<Carte>[] vectors) {
        mains = vectors;
    }

    /**
     * @param vectors Plis effectué
     */
    public void setPlis(Vector<Carte>[] vectors) {
        plis = vectors;
    }

    /**
     * @param vector Tapis
     */
    public void setTapis(Vector<Carte> vector) {
        tapis = vector;
    }

    /**
     * @param vector Tas regroupé des cartes
     */
    public void setTas(Vector<Carte> vector) {
        tas = vector;
    }

    /**
     * @return Annonces trouvées dans le jeu
     */
    public Vector<Annonce>[] chercheAnnonce() {
        for (int i = 0; i < annonces[i].size(); i++) {
            annonces[i].clear();
        }
        return annonces;
    }

    /**
     * @return Annonces des joueurs : Tableau indicé par le joueur et Vector d'Annonce
     */
    public Vector<Annonce>[] getAnnonces() {
        return annonces;
    }

    /**
     * @param vectors Annonces des joueurs : Tableau indicé par le joueur et Vector d'Annonce
     */
    public void setAnnonces(Vector<Annonce>[] vectors) {
        annonces = vectors;
    }

    /**
     * Indique si le joueur possède une certaine carte
     * @param joueur Indice du joueur concerné
     * @param theCarte Carte recherchée
     * @return position dans la main de la carte, -1 si pas truvée
     */
    public int indexOfMain(int joueur, Carte theCarte) {
        int pos = -1;
        for (int i = 0; (pos == -1) && (i < mains[joueur].size()); i++) {
            Carte aCarte = (Carte) mains[joueur].get(i);  
            if (aCarte.equals(theCarte)) {
                pos = i;
            }
        }
        
        return pos;
    }
}
