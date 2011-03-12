/*
 * Connexion base avec pool
 * Copyright (C) 2011-2011 Emmanuel Guyot <See emmguyot on SourceForge> 
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

import java.sql.SQLException;
import java.util.Calendar;
import java.util.MissingResourceException;

import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.postgresql.jdbc3.Jdbc3PoolingDataSource;

import com.increg.commun.exception.NoDatabaseException;

/**
 * Bean Session avec gestion du pool
 * Creation date: 
 * @author: Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */

/**
 * Classe gérant la connexion à la base et les accès
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class PooledDBSession extends DBSession {

	/**
     * Commons Logging instance.
     */
    protected static Log log = LogFactory.getLog(PooledDBSession.class);
    
    protected static String lastConfigName = null;
    
    /**
     * SalonSession constructor comment.
     * @throws NoDatabaseException 
     * 
     */
    public PooledDBSession() throws NoDatabaseException {
        super(lastConfigName);
    }

    /**
     * SalonSession constructor comment.
     * @throws NoDatabaseException 
     * 
     */
    public PooledDBSession(String configName) throws NoDatabaseException {
        super(configName);
        lastConfigName = configName;
    }

    /**
     * Ouverture de la base
     * @throws SQLException .
     */
    public void open() throws SQLException {

        String pwd = getRessource("password");
        String maxCnx; 
        try {
        	maxCnx = getRessource("maxCnxBase");
        }
        catch (MissingResourceException e) {
			maxCnx = "10";
		}

        InitialContext iCtxt = null;
        DataSource source = null;
        try {
			iCtxt = new InitialContext();
		    source = (DataSource)iCtxt.lookup("DBSession");
        }
        catch (NameNotFoundException ignored) {
	    	/* Création de la data Source avec Pool de connexion */
    	    Jdbc3PoolingDataSource newSource = new Jdbc3PoolingDataSource();
    	    newSource.setDataSourceName("DBSession");
    	    newSource.setServerName("localhost");
    	    // pour la compatibilité, extraction du nom de la base de l'URL
    	    newSource.setDatabaseName(getBaseName());
        	newSource.setUser(user);
        	newSource.setPassword(pwd);
        	newSource.setMaxConnections(Integer.parseInt(maxCnx));
        	try {
				iCtxt.rebind("DBSession", newSource);
			} catch (NamingException e) {
				log.error("Erreur au bind de la connexion base", e);
			}
			source = newSource;
        }
        catch (NamingException e) {
			log.error("Erreur à la création de la connexion base", e);
		}
        dbConnect = source.getConnection();
    	
        String reqSQL[] = {""}; 
        try {
            // Initialise le TimeZone pour qu'il soit cohérent entre la base et Java
            Calendar now = Calendar.getInstance();
            int tzOffset = -(now.get(Calendar.DST_OFFSET) + now.get(Calendar.ZONE_OFFSET)) / (60 * 60 * 1000);
            reqSQL[0] = "set timezone to \"GMT";
            if (tzOffset > 0) {
                reqSQL[0] += "+" + tzOffset + "\"";
            }
            else {
                reqSQL[0] += tzOffset + "\"";
            }
            doExecuteSQL(reqSQL);
        }
        catch (SQLException e) {
        	log.error("Erreur à l'initialistion de la Timezone : ", e);
            log.debug("Requete : " + reqSQL[0]);
        }
    }
}
