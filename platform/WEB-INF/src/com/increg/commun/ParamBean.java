/*
 * Bean de gestion des param�tres de l'application
 * Copyright (C) 2001-2005 Emmanuel Guyot <See emmguyot on SourceForge> 
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
package com.increg.commun;

import java.sql.*;

/**
 * Bean de gestion des param�tres de l'application
 * Creation date: (18/11/2001 21:58:58)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class ParamBean implements InCrEGBean {
    /**
     * Code du param�tre
     */
    protected int CD_PARAM;
    /**
     * Libell� du param�tre
     */
    protected java.lang.String LIB_PARAM;
    /**
     * Valeur du param�tre
     */
    protected java.lang.String VAL_PARAM;

    /**
     * ParamBean constructor comment.
     */
    public ParamBean() {

    }
    /**
     * ParamBean constructor comment.
     * @param rs java.sql.ResultSet
     */
    public ParamBean(ResultSet rs) {
        try {
            CD_PARAM = rs.getInt("CD_PARAM");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ParamBean (RS) : " + e.toString());
            }
        }
        try {
            LIB_PARAM = rs.getString("LIB_PARAM");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ParamBean (RS) : " + e.toString());
            }
        }
        try {
            VAL_PARAM = rs.getString("VAL_PARAM");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ParamBean (RS) : " + e.toString());
            }
        }
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void create(DBSession dbConnect) throws SQLException {

        StringBuffer req = new StringBuffer("insert into PARAM");
        StringBuffer colonne = new StringBuffer(" (");
        StringBuffer valeur = new StringBuffer(" values ( ");

        if (CD_PARAM == 0) {
            /**
             * Num�rotation automatique des prestations
             */
            String reqMax = "select nextval('SEQ_PARAM')";
            try {
                ResultSet aRS = dbConnect.doRequest(reqMax);
                CD_PARAM = 1; // Par d�faut

                while (aRS.next()) {
                    CD_PARAM = aRS.getInt(1);
                }
                aRS.close();
            } catch (Exception e) {
                System.out.println("Erreur dans reqSeq : " + e.toString());
            }
        }
        colonne.append("CD_PARAM,");
        valeur.append(CD_PARAM);
        valeur.append(",");

        if ((LIB_PARAM != null) && (LIB_PARAM.length() != 0)) {
            colonne.append("LIB_PARAM,");
            valeur.append(DBSession.quoteWith(LIB_PARAM, '\''));
            valeur.append(",");
        }

        if ((VAL_PARAM != null) && (VAL_PARAM.length() != 0)) {
            colonne.append("VAL_PARAM,");
            valeur.append(DBSession.quoteWith(VAL_PARAM, '\''));
            valeur.append(",");
        }

        // retire les derni�res virgules
        colonne.setLength(colonne.length() - 1);
        valeur.setLength(valeur.length() - 1);

        // Constitue la requete finale
        req.append(colonne);
        req.append(")");
        req.append(valeur);
        req.append(")");

        // Execute la cr�ation
        String[] reqs = new String[1];
        reqs[0] = req.toString();
        int[] nb = new int[1];
        nb = dbConnect.doExecuteSQL(reqs);

        if (nb[0] != 1) {
            throw (new SQLException(BasicSession.TAG_I18N + "message.creationKo" + BasicSession.TAG_I18N));
        }

    }
    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void delete(DBSession dbConnect) throws SQLException {

        StringBuffer req = new StringBuffer("delete from PARAM");
        StringBuffer where = new StringBuffer(" where CD_PARAM =" + CD_PARAM);

        // Constitue la requete finale
        req.append(where);

        // Execute la cr�ation
        String[] reqs = new String[1];
        reqs[0] = req.toString();
        int[] nb = new int[1];
        nb = dbConnect.doExecuteSQL(reqs);

        if (nb[0] != 1) {
            throw (new SQLException(BasicSession.TAG_I18N + "message.suppressionKo" + BasicSession.TAG_I18N));
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:55:16)
     * @return int
     */
    public int getCD_PARAM() {
        return CD_PARAM;
    }
    /**
     * Cr�ation d'un Bean Type de Vente � partir de sa cl�
     * Creation date: (19/08/2001 21:14:20)
     * @param dbConnect Connexion � la base � utiliser
     * @param CD_PARAM Code � rechercher
     * @return Bean correspondant au code
     */
    public static ParamBean getParamBean(DBSession dbConnect, String CD_PARAM) {
        String reqSQL = "select * from PARAM where CD_PARAM=" + CD_PARAM;
        ParamBean res = null;

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                res = new ParamBean(aRS);
            }
            aRS.close();
        } catch (Exception e) {
            System.out.println("Erreur dans constructeur sur cl� : " + e.toString());
        }
        return res;
    }
    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:52:52)
     * @return java.lang.String
     */
    public java.lang.String getLIB_PARAM() {
        return LIB_PARAM;
    }
    /**
     * Insert the method's description here.
     * Creation date: (02/09/2001 00:13:51)
     * @return java.lang.String
     */
    public java.lang.String getVAL_PARAM() {
        return VAL_PARAM;
    }
    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void maj(DBSession dbConnect) throws SQLException {

        StringBuffer req = new StringBuffer("update PARAM set ");
        StringBuffer colonne = new StringBuffer("");
        StringBuffer where = new StringBuffer(" where CD_PARAM=" + CD_PARAM);

        colonne.append("LIB_PARAM=");
        if ((LIB_PARAM != null) && (LIB_PARAM.length() != 0)) {
            colonne.append(DBSession.quoteWith(LIB_PARAM, '\''));
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("VAL_PARAM=");
        if ((VAL_PARAM != null) && (VAL_PARAM.length() != 0)) {
            colonne.append(DBSession.quoteWith(VAL_PARAM, '\''));
        } else {
            colonne.append("NULL");
        }

        // Constitue la requete finale
        req.append(colonne);
        req.append(where);

        // Execute la cr�ation
        String[] reqs = new String[1];
        reqs[0] = req.toString();
        int[] nb = new int[1];
        nb = dbConnect.doExecuteSQL(reqs);

        if (nb[0] != 1) {
            throw (new SQLException(BasicSession.TAG_I18N + "message.enregistrementKo" + BasicSession.TAG_I18N));
        }

    }
    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:55:16)
     * @param newCD_PARAM int
     */
    public void setCD_PARAM(int newCD_PARAM) {
        CD_PARAM = newCD_PARAM;
    }
    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:55:16)
     * @param newCD_PARAM String
     */
    public void setCD_PARAM(String newCD_PARAM) {
        if ((newCD_PARAM != null) && (newCD_PARAM.length() != 0)) {
            CD_PARAM = Integer.parseInt(newCD_PARAM);
        } else {
            CD_PARAM = 0;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:52:52)
     * @param newLIB_PARAM java.lang.String
     */
    public void setLIB_PARAM(java.lang.String newLIB_PARAM) {
        LIB_PARAM = newLIB_PARAM;
    }
    /**
     * Insert the method's description here.
     * Creation date: (02/09/2001 00:13:51)
     * @param newVAL_PARAM java.lang.String
     */
    public void setVAL_PARAM(java.lang.String newVAL_PARAM) {
        VAL_PARAM = newVAL_PARAM;
    }
    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public java.lang.String toString() {
        return getLIB_PARAM();
    }
}
