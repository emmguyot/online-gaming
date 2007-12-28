/*
 * Created on 6 avr. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.bean;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Vector;

import com.increg.commun.DBSession;
import com.increg.commun.InCrEGBean;
import com.increg.commun.exception.FctlException;
import com.increg.game.bean.belote.PartieBeloteBean;
import com.increg.game.client.Joueur;
import com.increg.game.client.Partie;
import com.increg.util.SimpleDateFormatEG;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class JoueurBean extends Joueur implements InCrEGBean {

    /**
     * Code du joueur (clé base de données)
     */
    protected long cdJoueur;

    /**
     * Date de modification du joueur
     */
    protected Calendar dtModif;
    
    /* **************************************************************************/
    
    /**
     * Constructeur par défaut
     */
    public JoueurBean () {
        super();
        dtModif = Calendar.getInstance();
    }

    /**
     * Joueur à partir d'un RecordSet.
     * @param rs ResultSet contenant les données à charger
     */
    public JoueurBean(ResultSet rs) {
        super();
        try {
            cdJoueur = rs.getLong("cdJoueur");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.err.println("Erreur dans JoueurBean (RS) : " + e.toString());
            }
        }
        
        /**
         * Chargement des données du Joueur
         */
        try {
            pseudo = rs.getString("pseudo");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.err.println("Erreur dans JoueurBean (RS) : " + e.toString());
            }
        }
        try {
            privilege = rs.getInt("privilege");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.err.println("Erreur dans JoueurBean (RS) : " + e.toString());
            }
        }
        try {
            couleur = rs.getString("couleur");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.err.println("Erreur dans JoueurBean (RS) : " + e.toString());
            }
        }
        try {
            String anAvatar = rs.getString("avatar");
            avatarHautePerf = null;
            if (anAvatar != null) {
                try {
                    avatarHautePerf = new URL(anAvatar);
                }
                catch (MalformedURLException e) {
                    System.err.println("Avatar mal formé : " + anAvatar);
                }
            }
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.err.println("Erreur dans JoueurBean (RS) : " + e.toString());
            }
        }
        try {
            String anAvatar = rs.getString("avatarLow");
            avatarFaiblePerf = null;
            if (anAvatar != null) {
                try {
                    avatarFaiblePerf = new URL(anAvatar);
                }
                catch (MalformedURLException e) {
                    System.err.println("Avatar mal formé : " + anAvatar);
                }
            }
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.err.println("Erreur dans JoueurBean (RS) : " + e.toString());
            }
        }
        dtModif = Calendar.getInstance();  
        try {
            dtModif.setTime(rs.getTimestamp("dtModif"));
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans JoueurBean (RS) : " + e.toString());
            }
        }
        
    }

    /**
     * Chargement d'un joueur à partir de la base
     * @param dbConnect Connexion base à utiliser
     * @param cdJoueur Code du joueur à charger
     * @return Joueur chargé
     */
    public static JoueurBean getJoueurBean(DBSession dbConnect, String cdJoueur) {
        String reqSQL = "select * from Joueur where cdJoueur=" + cdJoueur;
        JoueurBean res = null;

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            if (aRS.next()) {
                res = new JoueurBean(aRS);
            }
            aRS.close();
        }
        catch (Exception e) {
            System.err.println("JoueurBean::Erreur dans constructeur sur clé : " + e.toString());
        }
        return res;
    }
    
    /**
     * Chargement d'un joueur à partir de la base
     * @param dbConnect Connexion base à utiliser
     * @param pseudo Code du joueur à charger
     * @return Joueur chargé
     */
    public static JoueurBean getJoueurBeanFromPseudo(DBSession dbConnect, String pseudo) {
        String reqSQL = "select * from Joueur where pseudo=" + DBSession.quoteWith(pseudo, '\'');
        JoueurBean res = null;

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            if (aRS.next()) {
                res = new JoueurBean(aRS);
            }
            aRS.close();
        }
        catch (Exception e) {
        	System.err.println("JoueurBean::Erreur dans constructeur sur pseudo : " + e.toString());
        	e.printStackTrace();
        }
        return res;
    }
    
    /**
     * @see com.increg.commun.InCrEGBean#create(com.increg.commun.DBSession)
     */
    public void create(DBSession dbConnect) throws SQLException, FctlException {
        SimpleDateFormatEG formatDate  = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        StringBuffer req = new StringBuffer("insert into JOUEUR ");
        StringBuffer colonne = new StringBuffer("(");
        StringBuffer valeur = new StringBuffer(" values ( ");
    
        if (cdJoueur == 0) {
            /**
             * Numérotation automatique des codes clients
             */
            String reqMax = "select nextval('SEQ_JOUEUR')";
            try {
                ResultSet aRS = dbConnect.doRequest(reqMax);
                cdJoueur = 1; // Par défaut

                while (aRS.next()) {
                    cdJoueur = aRS.getInt(1);
                }
                aRS.close();
            }
            catch (Exception e) {
                System.out.println("JoueurBean::Erreur dans reqSeq : " + e.toString());
            }
        }
        colonne.append("cdJoueur,");
        valeur.append(cdJoueur);
        valeur.append(",");
    
        if ((pseudo != null) && (pseudo.length() != 0)) {
            colonne.append("pseudo,");
            valeur.append(DBSession.quoteWith(pseudo, '\''));
            valeur.append(",");
        }

        if (avatarHautePerf != null) {
            colonne.append("avatar,");
            valeur.append(DBSession.quoteWith(avatarHautePerf.toString(), '\''));
            valeur.append(",");
        }

        if (avatarFaiblePerf != null) {
            colonne.append("avatarLow,");
            valeur.append(DBSession.quoteWith(avatarFaiblePerf.toString(), '\''));
            valeur.append(",");
        }

        colonne.append("privilege,");
        valeur.append(privilege);
        valeur.append(",");
 
        if ((couleur != null) && (couleur.length() != 0)) {
            colonne.append("couleur,");
            valeur.append(DBSession.quoteWith(couleur, '\''));
            valeur.append(",");
        }

        if (dtModif != null) {
            colonne.append("dtModif,");
            valeur.append(DBSession.quoteWith(formatDate.formatEG(dtModif.getTime()), '\''));
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

        StringBuffer req = new StringBuffer("delete from Joueur ");
        StringBuffer where = new StringBuffer(" where cdJoueur=" + cdJoueur);
    
        // Constitue la requete finale
        req.append(where);

        // Execute la création
        String[] reqs = new String[1];
        reqs[0] = req.toString();
        int[] nb = new int[1];
        nb = dbConnect.doExecuteSQL(reqs);

        if (nb[0] != 1) {
            throw (new SQLException("Suppression non effectuée"));
        }   

    }

    /**
     * @see com.increg.commun.InCrEGBean#maj(com.increg.commun.DBSession)
     */
    public void maj(DBSession dbConnect) throws SQLException, FctlException {

        SimpleDateFormatEG formatDate  = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        StringBuffer req = new StringBuffer("update Joueur set ");
        StringBuffer colonne = new StringBuffer("");
        StringBuffer where = new StringBuffer(" where cdJoueur =" + cdJoueur);

        colonne.append("pseudo=");
        if ((pseudo != null) && (pseudo.length() != 0)) {
            colonne.append(DBSession.quoteWith(pseudo, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("avatar=");
        if (avatarHautePerf != null) {
            colonne.append(DBSession.quoteWith(avatarHautePerf.toString(), '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("avatarLow=");
        if (avatarFaiblePerf != null) {
            colonne.append(DBSession.quoteWith(avatarFaiblePerf.toString(), '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("privilege=");
        colonne.append(privilege);
        colonne.append(",");

        if ((couleur != null) && (couleur.length() != 0)) {
            colonne.append("couleur=");
            colonne.append(DBSession.quoteWith(couleur, '\''));
            colonne.append(",");
        }

        colonne.append("dtModif=");
        dtModif = Calendar.getInstance();
        colonne.append(DBSession.quoteWith(formatDate.formatEG(dtModif.getTime()), '\''));

        // Constitue la requete finale
        req.append(colonne);
        req.append(where);

        // Execute la création
        String[] reqs = new String[1];
        reqs[0] = req.toString();
        int[] nb = new int[1];
        nb = dbConnect.doExecuteSQL(reqs);

        if (nb[0] != 1) {
            throw (new SQLException("Mise à jour non effectuée"));
        }

    }

    /**
     * Récupération de l'historique des parties du joueur à partir de la base
     * @param dbConnect Connexion à utiliser
     * @return Liste des historiques
     */
    public Vector<Partie> getHistorique (DBSession dbConnect) {
        // Chargement de l'historique
    	Vector<PartieBeloteBean> lstParties = PartieBeloteBean.getPartieFromJoueur(dbConnect, Long.toString(cdJoueur));
    	historique = new Vector<Partie>(lstParties.size());
    	
    	for (PartieBeloteBean partieBeloteBean : lstParties) {
			historique.add(partieBeloteBean.getMyPartie());
		}
    		
        return historique;
    }
    
    /**
     * @return code du joueur
     */
    public long getCdJoueur() {
        return cdJoueur;
    }

    /**
     * @param l code du joueur
     */
    public void setCdJoueur(long l) {
        cdJoueur = l;
    }

}
