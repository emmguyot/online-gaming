/*
 * 
 * Copyright (C) 2003-2005 Emmanuel Guyot <See emmguyot on SourceForge> 
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

import java.sql.SQLException;
import java.util.Calendar;

import com.increg.commun.DBSession;
import com.increg.commun.InCrEGBean;
import com.increg.commun.exception.FctlException;
import com.increg.game.client.Chat;
import com.increg.util.SimpleDateFormatEG;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ChatBean extends Chat implements InCrEGBean {
    
    /**
     * Identifiant du Chat
     */
    protected long id;

    /**
     * Date du chat
     */
    protected Calendar date;
    
    /**
     * Constructeur
     */
    public ChatBean() {
    	super();
    	date = Calendar.getInstance();
    }

    /**
     * @see com.increg.commun.InCrEGBean#create(com.increg.commun.DBSession)
     */
    public void create(DBSession dbConnect) throws SQLException, FctlException {
        SimpleDateFormatEG formatDate  = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        StringBuffer req = new StringBuffer("insert into CHAT ");
        StringBuffer colonne = new StringBuffer("(");
        StringBuffer valeur = new StringBuffer(" values ( ");
    
        if (joueurOrig != null) {
            colonne.append("pseudo,");
            valeur.append(DBSession.quoteWith(joueurOrig.getPseudo(), '\''));
            valeur.append(",");
        }

        if (joueurDest != null) {
            colonne.append("pseudoDest,");
            valeur.append(DBSession.quoteWith(joueurDest.getPseudo(), '\''));
            valeur.append(",");
        }

        if (partie != null) {
            colonne.append("idPartie,");
            valeur.append(partie.getIdentifiant());
            valeur.append(",");
        }

        if ((text != null) && (text.length() != 0)) {
            colonne.append("msg,");
            valeur.append(DBSession.quoteWith(text, '\''));
            valeur.append(",");
        }

        if (date != null) {
            colonne.append("dtCreat,");
            valeur.append(DBSession.quoteWith(formatDate.formatEG(date.getTime()), '\''));
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
        throw new FctlException("Fonction non implémentée");
    }

    /**
     * @see com.increg.commun.InCrEGBean#maj(com.increg.commun.DBSession)
     */
    public void maj(DBSession dbConnect) throws SQLException, FctlException {
        throw new FctlException("Fonction non implémentée");
    }

    /**
     * @return Identifiant du Chat
     */
    public long getId() {
        return id;
    }

    /**
     * @param l Identifiant du Chat
     */
    public void setId(long l) {
        id = l;
    }

	/**
	 * @return Returns the date (Date du chat).
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * @param date The date to set (Date du chat).
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}
}
