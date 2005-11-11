/*
 * One line to give the program's name and a brief idea of what it does.
 * Copyright (C) 2005 Emmanuel Guyot <See emmguyot on SourceForge>
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation; either 
 * version 2 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; 
 * if not, write to the 
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 */
package com.increg.game.bean;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.increg.commun.DBSession;
import com.increg.commun.InCrEGBean;
import com.increg.commun.exception.FctlException;

public class ParamBean implements InCrEGBean {
	
	public static final int CD_PARAM_NB_POINT_BELOTE_CLASSIQUE_SS_ANNONCE = 1;
	public static final int CD_PARAM_NB_POINT_BELOTE_CLASSIQUE_SS_ANNONCE_TOURNOI = 2;
	public static final int CD_PARAM_NB_POINT_BELOTE_CLASSIQUE_AVEC_ANNONCES = 3;
	public static final int CD_PARAM_NB_POINT_BELOTE_CLASSIQUE_AVEC_ANNONCES_TOURNOI = 4;
	public static final int CD_PARAM_NB_POINT_BELOTE_MODERNE_SS_ANNONCE = 5;
	public static final int CD_PARAM_NB_POINT_BELOTE_MODERNE_SS_ANNONCE_TOURNOI = 6;
	public static final int CD_PARAM_NB_POINT_BELOTE_MODERNE_AVEC_ANNONCES = 7;
	public static final int CD_PARAM_NB_POINT_BELOTE_MODERNE_AVEC_ANNONCES_TOURNOI = 8;
	public static final int CD_PARAM_SAVE_CHAT = 9;
	public static final int CD_PARAM_MOT_INTERDIT_CHAT = 10;
	/**
	 * Code du paramètre
	 */
	protected int cdParam;
	/**
	 * Libellé du paramètre
	 */
	protected String libParam;
	/**
	 * Valeur du paramètre
	 */
	protected String valParam;
	
	
	/**
	 * @return Returns the code.
	 */
	public int getCdParam() {
		return cdParam;
	}
	/**
	 * @param code The code to set.
	 */
	public void setCdParam(int code) {
		this.cdParam = code;
	}
	/**
	 * @return Returns the libelle.
	 */
	public String getLibParam() {
		return libParam;
	}
	/**
	 * @param libelle The libelle to set.
	 */
	public void setLibParam(String libelle) {
		this.libParam = libelle;
	}
	/**
	 * @return Returns the valeur.
	 */
	public String getValParam() {
		return valParam;
	}
	/**
	 * @param valeur The valeur to set.
	 */
	public void setValParam(String valeur) {
		this.valParam = valeur;
	}

	
    /**
     * Constructeur par défaut
     */
    public ParamBean () {
        super();
    }

    /**
     * Paramétre à partir d'un RecordSet.
     * @param rs ResultSet contenant les données à charger
     */
    public ParamBean(ResultSet rs) {
        super();
        try {
            cdParam = rs.getInt("cdParam");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.err.println("Erreur dans ParamBean (RS) : " + e.toString());
            }
        }
        
        try {
            libParam = rs.getString("libParam");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.err.println("Erreur dans ParamBean (RS) : " + e.toString());
            }
        }
        try {
            valParam = rs.getString("valParam");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.err.println("Erreur dans ParamBean (RS) : " + e.toString());
            }
        }
    }

    /**
     * Chargement d'un paramètre à partir de la base
     * @param dbConnect Connexion base à utiliser
     * @param cdParam Code du paramètre à charger
     * @return Paramètre chargé
     */
    public static ParamBean getParamBean(DBSession dbConnect, String cdParam) {
        String reqSQL = "select * from Param where cdParam=" + cdParam;
        ParamBean res = null;

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            if (aRS.next()) {
                res = new ParamBean(aRS);
            }
            aRS.close();
        }
        catch (Exception e) {
            System.err.println("ParamBean::Erreur dans constructeur sur clé : " + e.toString());
        }
        return res;
    }

    public void create(DBSession dbConnect) throws SQLException, FctlException {
        StringBuffer req = new StringBuffer("insert into PARAM ");
        StringBuffer colonne = new StringBuffer("(");
        StringBuffer valeur = new StringBuffer(" values ( ");
    
        if (cdParam == 0) {
            /**
             * Numérotation automatique des codes
             */
            String reqMax = "select nextval('SEQ_PARAM')";
            try {
                ResultSet aRS = dbConnect.doRequest(reqMax);
                cdParam = 1; // Par défaut

                while (aRS.next()) {
                    cdParam = aRS.getInt(1);
                }
                aRS.close();
            }
            catch (Exception e) {
                System.out.println("ParamBean::Erreur dans reqSeq : " + e.toString());
            }
        }
        colonne.append("cdParam,");
        valeur.append(cdParam);
        valeur.append(",");
    
        if ((libParam != null) && (libParam.length() != 0)) {
            colonne.append("libParam,");
            valeur.append(DBSession.quoteWith(libParam, '\''));
            valeur.append(",");
        }

        if (valParam != null) {
            colonne.append("valParam,");
            valeur.append(DBSession.quoteWith(valParam, '\''));
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
	public void delete(DBSession dbConnect) throws SQLException, FctlException {
        StringBuffer req = new StringBuffer("delete from Param ");
        StringBuffer where = new StringBuffer(" where cdParam=" + cdParam);
    
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
	public void maj(DBSession dbConnect) throws SQLException, FctlException {
        StringBuffer req = new StringBuffer("update Param set ");
        StringBuffer colonne = new StringBuffer("");
        StringBuffer where = new StringBuffer(" where cdParam =" + cdParam);

        colonne.append("libParam=");
        if ((libParam != null) && (libParam.length() != 0)) {
            colonne.append(DBSession.quoteWith(libParam, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("valParam=");
        if (valParam != null) {
            colonne.append(DBSession.quoteWith(valParam, '\''));
        }
        else {
            colonne.append("NULL");
        }

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
}