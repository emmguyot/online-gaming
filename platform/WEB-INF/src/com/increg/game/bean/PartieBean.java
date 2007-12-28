/*
 * Created on 3 mai 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.bean;

import com.increg.game.client.Partie;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import com.increg.commun.DBSession;
import com.increg.commun.InCrEGBean;
import com.increg.commun.exception.FctlException;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class PartieBean implements InCrEGBean {

    /**
     * Partie associ�e � ce bean : C'est elle qui est charg�e / sauvegard�e ...
     */
    protected Partie myPartie;

    /**
     * Constructeur � partir d'une partie
     * @param aPartie Partie associ�e au bean
     */
    public PartieBean(Partie aPartie) {
        myPartie = aPartie;
        
        myPartie.setDtDebut(Calendar.getInstance());
    }
    
    /**
     * Partie � partir d'un RecordSet.
     * @param rs ResultSet contenant les donn�es � charger
     */
    public PartieBean(ResultSet rs) {
        super();

        try {
            Timestamp aTime = rs.getTimestamp("dtDebut");
            if (aTime != null) {
                Calendar dt = Calendar.getInstance();
                dt.setTime(aTime);
                myPartie.setDtDebut(dt);  
            }
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PartieBean (RS) : " + e.toString());
            }
        }

        try {
            Timestamp aTime = rs.getTimestamp("dtFin");
            if (aTime != null) {
                Calendar dt = Calendar.getInstance();
                dt.setTime(aTime);
                myPartie.setDtFin(dt);  
            }
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PartieBean (RS) : " + e.toString());
            }
        }
    }
    
    /**
     * @see com.increg.commun.InCrEGBean#create(com.increg.commun.DBSession)
     * Cr�ation de la partie
     */
    public void create(DBSession dbConnect) throws SQLException, FctlException {
        
    }

    /**
     * @see com.increg.commun.InCrEGBean#delete(com.increg.commun.DBSession)
     */
    public void delete(DBSession dbConnect) throws SQLException, FctlException {

    }

    /**
     * @see com.increg.commun.InCrEGBean#maj(com.increg.commun.DBSession)
     */
    public void maj(DBSession dbConnect) throws SQLException, FctlException {

    }

    /**
     * @see com.increg.commun.InCrEGBean#toString()
     */
    public String toString() {
        return null;
    }

    /**
     * @return Partie associ�e � ce bean : C'est elle qui est charg�e / sauvegard�e ...
     */
    public Partie getMyPartie() {
        return myPartie;
    }

    /**
     * @param partie Partie associ�e � ce bean : C'est elle qui est charg�e / sauvegard�e ...
     */
    public void setMyPartie(Partie partie) {
        myPartie = partie;
    }

    /**
     * @return Type de partie
     */
    protected int getType() {
        return 0;
    }

    /**
     * @return Date de d�but de la partie
     */
    public Calendar getDtDebut() {
        return myPartie.getDtDebut();
    }

    /**
     * @return Date de fin de la partie
     */
    public Calendar getDtFin() {
        return myPartie.getDtFin();
    }

    /**
     * @param calendar Date de d�but de la partie
     */
    public void setDtDebut(Calendar calendar) {
        myPartie.setDtDebut(calendar);
    }

    /**
     * @param calendar Date de fin de la partie
     */
    public void setDtFin(Calendar calendar) {
        myPartie.setDtFin(calendar);
    }
}
