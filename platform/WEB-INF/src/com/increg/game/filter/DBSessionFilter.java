/*
 * Filtre J2EE assurant la récupération/restitution de la base de données à partir d'un pool
 * Copyright (C) 2011 Emmanuel Guyot <See emmguyot on SourceForge> 
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
package com.increg.game.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.DBSession;
import com.increg.commun.PooledDBSession;
import com.increg.commun.exception.NoDatabaseException;
import com.increg.game.bean.GameSession;

/**
 * 
 * @author Manu
 *
 */
public class DBSessionFilter implements Filter {

	/**
     * Commons Logging instance.
     */
    protected static Log log = LogFactory.getLog(DBSessionFilter.class);

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		boolean resetToDo = false;
		DBSession dbConnect = null;
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request; 
			// Initialise la connection
			try {
				dbConnect = new PooledDBSession(GameSession.DEFAULT_CONFIG);
				httpRequest.setAttribute("DBSession", dbConnect);
			} catch (NoDatabaseException e) {
				log.error("Erreur à la création de la connexion base", e);
			}
			resetToDo = true;
		}
		chain.doFilter(request, response);
		
		if (resetToDo) {
			dbConnect.close();
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
