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
        throw new FctlException("Fonction non implémentée");
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
