/*
 * Created on 3 mai 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.bean.belote;

import com.increg.game.bean.PartieBean;
import com.increg.game.client.belote.PartieBelote;
import com.increg.game.client.belote.PartieBeloteClassique;
import com.increg.game.client.belote.PartieBeloteModerne;
import com.increg.util.SimpleDateFormatEG;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import com.increg.commun.DBSession;
import com.increg.commun.exception.FctlException;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class PartieBeloteBean extends PartieBean {

    /**
     * Constructeur à partir d'une partie
     * @param aPartie Partie associée au bean
     */
    public PartieBeloteBean(PartieBelote aPartie) {
        super(aPartie);
    }

    /**
     * @param rs .
     */
    public PartieBeloteBean(ResultSet rs) {
        super(rs);
    }

    /**
     * Partie à partir d'un RecordSet.
     * @param aPartie partie à encapsuler
     * @return Partie de belote sous forme de bean
     */
    public static PartieBeloteBean getPartieBeloteBean(PartieBelote aPartie) {

        if (aPartie instanceof PartieBeloteClassique) {
            return new PartieBeloteClassiqueBean((PartieBeloteClassique) aPartie);
        }
        else if (aPartie instanceof PartieBeloteModerne) {
            return new PartieBeloteModerneBean((PartieBeloteModerne) aPartie);
        }
        else {
            System.err.println("Type de partie inconnu");
            return null;
        }
    }

    /**
     * Partie à partir d'un RecordSet.
     * @param rs ResultSet contenant les données à charger
     * @return Partie de belote créée (Classique ou Moderne)
     */
    public static PartieBeloteBean getPartieBeloteBean(ResultSet rs) {

        // Récupère le type
        int type = 0;
        try {
            type = rs.getInt("type");
        }
        catch (SQLException e) {
            System.err.println("JoueurBean::Erreur dans constructeur sur RS : ");
            e.printStackTrace();
        }

        if (type == PartieBeloteClassiqueBean.TYPE_CLASSIQUE) {
            return new PartieBeloteClassiqueBean(rs);
        }
        else if (type == PartieBeloteModerneBean.TYPE_MODERNE) {
            return new PartieBeloteModerneBean(rs);
        }
        else {
            System.err.println("Type de partie inconnu");
            return null;
        }
    }

    /**
     * Chargement des parties d'un joueur à partir de la base
     * @param dbConnect Connexion base à utiliser
     * @param cdJoueur Code du joueur à charger
     * @return ensemble des parties du joueurs
     */
    public static Vector getPartieFromJoueur(DBSession dbConnect, String cdJoueur) {

        String reqSQL =
            "select * from partie, joueur where (partie.pseudo[1] = joueur.pseudo "
                + "or partie.pseudo[2] = joueur.pseudo "
                + "or partie.pseudo[3] = joueur.pseudo "
                + "or partie.pseudo[4] = joueur.pseudo) and cdJoueur="
                + cdJoueur
                + "order by dtDebut desc";
        Vector res = new Vector();

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                res.add(PartieBeloteBean.getPartieBeloteBean(aRS));
            }
            aRS.close();
        }
        catch (Exception e) {
            System.err.println("PartieBeloteBean::getPartieFromJoueur : " + e.toString());
        }
        return res;
    }

    /**
     * Chargement des parties d'un tournoi à partir de la base
     * @param dbConnect Connexion base à utiliser
     * @param idTournoi Code du tournoi à charger
     * @return ensemble des parties du tournoi
     */
    public static Vector getPartieFromTournoi(DBSession dbConnect, String idTournoi) {

        // !TODO Implémentation tournoi à faire
        String reqSQL = "select * from partie where idTournoi=" + idTournoi;
        Vector res = new Vector();

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                res.add(PartieBeloteBean.getPartieBeloteBean(aRS));
            }
            aRS.close();
        }
        catch (Exception e) {
            System.err.println("PartieBeloteBean::getPartieFromTournoi : " + e.toString());
        }
        return res;
    }

    /**
     * @see com.increg.commun.InCrEGBean#create(com.increg.commun.DBSession)
     */
    public void create(DBSession dbConnect) throws SQLException, FctlException {

        SimpleDateFormatEG formatDate = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        StringBuffer req = new StringBuffer("insert into PARTIE ");
        StringBuffer colonne = new StringBuffer("(");
        StringBuffer valeur = new StringBuffer(" values ( ");

        colonne.append("idPartie,");
        valeur.append(myPartie.getIdentifiant());
        valeur.append(",");

        colonne.append("type,");
        valeur.append(getType());
        valeur.append(",");

        colonne.append("annonce,");
        valeur.append(((PartieBelote) myPartie).isAnnonce());
        valeur.append(",");

        if (dtDebut != null) {
            colonne.append("dtDebut,");
            valeur.append(DBSession.quoteWith(formatDate.formatEG(dtDebut.getTime()), '\''));
            valeur.append(",");
        }

        if (dtFin != null) {
            colonne.append("dtFin,");
            valeur.append(DBSession.quoteWith(formatDate.formatEG(dtFin.getTime()), '\''));
            valeur.append(",");
        }

        colonne.append("pseudo,");
        String lstJoueur = "";
        for (int i = 0; i < myPartie.getDernierParticipant().length; i++) {
            if (lstJoueur.length() > 0) {
                lstJoueur = lstJoueur + ",";
            }
            lstJoueur = lstJoueur + DBSession.quoteWith(myPartie.getDernierParticipant()[i].getPseudo(), '\"');
        }
        valeur.append(DBSession.quoteWith("{" + lstJoueur + "}", '\''));
        valeur.append(",");

        colonne.append("score,");
        String lstScore = "";
        for (int i = 0; i < (myPartie.getDernierParticipant().length / 2); i++) {
            if (lstScore.length() > 0) {
                lstScore = lstScore + ",";
            }
            lstScore = lstScore + myPartie.getScoreTotal(i);
        }
        valeur.append(DBSession.quoteWith("{" + lstScore + "}", '\''));
        valeur.append(",");

        if (myPartie.getMyTournoi() != null) {
            colonne.append("idTournoi,");
            valeur.append(myPartie.getMyTournoi().getIdentifiant());
            valeur.append(",");
        }

        // retire les dernières virgules
        colonne.setLength(colonne.length() - 1);
        valeur.setLength(valeur.length() - 1);

        // Constitue la requete finale
        req.append(colonne);
        req.append(")");
        req.append(valeur);
        req.append(")");

        // Execute la création
        String[] reqs = new String[1];
        reqs[0] = req.toString();
        int[] nb = new int[1];
        nb = dbConnect.doExecuteSQL(reqs);

        if (nb[0] != 1) {
            throw (new SQLException("Création non effectuée"));
        }

    }

    /**
     * @see com.increg.commun.InCrEGBean#delete(com.increg.commun.DBSession)
     */
    public void delete(DBSession dbConnect) throws SQLException, FctlException {
        throw (new FctlException("Suppression non implémentée"));
    }

    /**
     * @see com.increg.commun.InCrEGBean#maj(com.increg.commun.DBSession)
     */
    public void maj(DBSession dbConnect) throws SQLException, FctlException {
        throw (new FctlException("Mise à jour non implémentée"));
    }

    /**
     * @see com.increg.commun.InCrEGBean#toString()
     */
    public String toString() {
        return myPartie.toString();
    }

    /**
     * @param tab Tableau au format texte
     * @return Tableau de chaine résultant de la décomposition
     * @throws FctlException En cas d'erreur de format
     */
    protected String[] decodeArray(String tab) throws FctlException {
        /**
         * Décompose le tableau
         * Syntaxe : {valeur1,valeur2,valeur3,valeur4}
         * Si valeur contient } la valeur est entre guillemets
         * Si valeur contient " c'est remplacé par \"
         * Si valeur contient , la valeur est entre guillemets
         */
        ArrayList res = new ArrayList();
        boolean dansMot = false;
        boolean surrounded = false;
        StringBuffer mot = new StringBuffer();
        
        for (int pos = 0; pos < tab.length(); pos++) {
            
            if ((pos == 0) && (tab.charAt(pos) != '{')) {
                // Pas bon
                throw new FctlException("Format du tableau invalide : Devrait commencer par {");
            }
            else if (pos > 0) {
                if (dansMot) {
                    // Vérifie la fin ou un caractère escapé
                    if (tab.charAt(pos) == '\\') {
                        // Début caractère escapé
                        mot.append(tab.charAt(++pos));
                    }
                    else if ((tab.charAt(pos) == '"') && surrounded) {
                        // Fin de champ
                        dansMot = false;
                        res.add(mot.toString());
                    }
                    else if ((tab.charAt(pos) == '"') && !surrounded) {
                        mot.append(tab.charAt(pos));
                    }
                    else if ((tab.charAt(pos) == ',') && surrounded) {
                        mot.append(tab.charAt(pos));
                    }
                    else if ((tab.charAt(pos) == ',') && !surrounded) {
                        // Fin de champ
                        dansMot = false;
                        res.add(mot.toString());
                    }
                    else if ((tab.charAt(pos) == '}') && surrounded) {
                        mot.append(tab.charAt(pos));
                    }
                    else if ((tab.charAt(pos) == '}') && !surrounded) {
                        // Fin de champ
                        dansMot = false;
                        res.add(mot.toString());
                    }
                    else {
                        mot.append(tab.charAt(pos));
                    }
                }
                else {
                    // Cherche le début
                    if (tab.charAt(pos) == '"') {
                        // Début
                        dansMot = true;
                        surrounded = true;
                        mot.delete(0, mot.length());
                    }
                    else if (tab.charAt(pos) == ',') {
                        // Rien à faire
                    }
                    else if (tab.charAt(pos) == '}') {
                        // Rien à faire
                    }
                    else {
                        dansMot = true;
                        surrounded = false;
                        mot.delete(0, mot.length());
                        mot.append(tab.charAt(pos));
                    }
                }
            }
        }
        
        String[] resCh = new String[res.size()];
        for (int i = 0; i < res.size(); i++) {
            resCh[i] = (String) res.get(i);
        }
        return resCh;
    }


}
