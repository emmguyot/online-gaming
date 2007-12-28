/*
 * Created on 3 mai 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.bean.belote;

import com.increg.commun.exception.FctlException;
import com.increg.game.client.Joueur;
import com.increg.game.client.Tournoi;
import com.increg.game.client.belote.PartieBelote;
import com.increg.game.client.belote.PartieBeloteModerne;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 * @author Manu
 *
 * Bean BD d'une partie de Belote moderne
 */
public class PartieBeloteModerneBean extends PartieBeloteBean {

    /**
     * Type de partie moderne
     */
    protected static final int TYPE_MODERNE = 2;

    /**
     * Constructeur à partir d'une partie
     * @param aPartie Partie associée au bean
     */
    public PartieBeloteModerneBean(PartieBeloteModerne aPartie) {
        super(aPartie);
    }

    /**
     * Partie à partir d'un RecordSet.
     * @param rs ResultSet contenant les données à charger (Le type doit être correct)
     */
    public PartieBeloteModerneBean(ResultSet rs) {
        super(rs);

        myPartie = new PartieBeloteModerne();
        
        try {
            myPartie.setIdentifiant(rs.getInt("idPartie"));
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.err.println("Erreur dans PartieBeloteModerneBean (RS) : " + e.toString());
            }
        }
        
        try {
            int idTournoi = rs.getInt("idTournoi");  
            if (idTournoi != 0) {
                Tournoi aTournoi = new Tournoi();
                aTournoi.setIdentifiant(idTournoi);
                myPartie.setMyTournoi(aTournoi);
            }
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.err.println("Erreur dans PartieBeloteModerneBean (RS) : " + e.toString());
            }
        }
        
        try {
            ((PartieBeloteModerne) myPartie).setAnnonce(rs.getBoolean("annonce"));
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.err.println("Erreur dans PartieBeloteModerneBean (RS) : " + e.toString());
            }
        }
        
        try {
            Joueur[] tabJoueur = new Joueur[PartieBelote.NB_JOUEUR];
            String[] tabPseudo = decodeArray(rs.getString("pseudo"));
            
            for (int i = 0; i < tabJoueur.length; i++) {
                // Recrées un joueur de toutes pieces
                // A charge de l'appelant de compléter si besoin
                tabJoueur[i] = new Joueur();
                tabJoueur[i].setPseudo(tabPseudo[i]);
            }
            ((PartieBeloteModerne) myPartie).setParticipant(tabJoueur);
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.err.println("Erreur dans PartieBeloteModerneBean (RS) : " + e.toString());
            }
        } catch (FctlException e) {
            e.printStackTrace();
        }
        
        try {
            String[] tabScoreCh = decodeArray(rs.getString("score"));
            int[] tabScore = new int[tabScoreCh.length];
            
            for (int i = 0; i < tabScoreCh.length; i++) {
                tabScore[i]  = Integer.parseInt(tabScoreCh[i]);
            }
            Vector<int[]> vScore = new Vector<int[]>(1);
            vScore.add(tabScore);
            
            ((PartieBeloteModerne) myPartie).setScore(vScore);
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.err.println("Erreur dans PartieBeloteModerneBean (RS) : " + e.toString());
            }
        } catch (FctlException e) {
            e.printStackTrace();
        }
        
    }

    /**
     * @return Type de partie
     */
    protected int getType() {
        return TYPE_MODERNE;
    }

}
