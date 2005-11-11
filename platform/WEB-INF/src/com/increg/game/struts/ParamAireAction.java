/*
 * Paramétrage de l'aire
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
package com.increg.game.struts;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.increg.commun.DBSession;
import com.increg.game.bean.GameEnvironment;
import com.increg.game.bean.GameSession;
import com.increg.game.bean.ParamBean;

/**
 * @author Manu
 *
 */
public class ParamAireAction extends AdminAction {

	/* (non-Javadoc)
	 * @see com.increg.game.struts.AdminAction#exclure(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward paramAire(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ParamAireForm paramForm = (ParamAireForm) form;

		ActionMessages errors = new ActionMessages();
        GameEnvironment env = (GameEnvironment) getServlet().getServletContext().getAttribute("Env");
        DBSession dbConnect = ((GameSession) request.getSession().getAttribute("mySession")).getMyDBSession();

        errors.add(precheck(dbConnect));
        
        if (!StringUtils.isEmpty(paramForm.getValParam())) {
        	// Mise à jour du paramètre
        	ParamBean aParam = ParamBean.getParamBean(dbConnect, paramForm.getCdParam());
        	aParam.setLibParam(paramForm.getLibParam());
        	aParam.setValParam(paramForm.getValParam());
        	aParam.maj(dbConnect);
        	
        	// Maj en direct du serveur
            env.loadParamAire(dbConnect, true);
        }
        
		String reqSQL = "select * from param order by libParam";
		
		try {
			ResultSet rs = dbConnect.doRequest(reqSQL);
			
			while (rs.next()) {
				ParamBean aParam = new ParamBean(rs);
				paramForm.getLstParam().add(aParam);
			}
			
			rs.close();
		}
		catch (SQLException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.general", e));
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
        return mapping.findForward("page");
	}
	
	/* (non-Javadoc)
	 * @see com.increg.game.struts.AdminAction#menu(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward menu(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward("menu");
	}

	protected ActionMessages precheck(DBSession dbConnect) {
		
		ActionMessages errors = new ActionMessages();

		// Vérification présence table param
		String reqSQL = "select * from param where 0=1";
		
		try {
			ResultSet rs = dbConnect.doRequest(reqSQL);
			rs.close();
		}
		catch (SQLException e) {
			// Table non présente : Création
			String reqsSQL[] = {"create table PARAM ("
				+ "cdParam     numeric(2)      not null,"
				+ "libParam    varchar(100)     not null,"
				+ "valParam    varchar(200)    not null,"
				+ "constraint PK_PARAM primary key (cdParam))",
				"create sequence SEQ_PARAM",
				"alter table PARAM alter cdParam set default nextval('SEQ_PARAM')",
				"insert into PARAM (libParam, valParam) values ('Nombre de points pour les parties de Belote classique sans annonce hors tournoi', '1000')", 
				"insert into PARAM (libParam, valParam) values ('Nombre de points pour les parties de Belote classique sans annonce dans un tournoi', '1000')", 
				"insert into PARAM (libParam, valParam) values ('Nombre de points pour les parties de Belote classique avec annonces hors tournoi', '1000')", 
				"insert into PARAM (libParam, valParam) values ('Nombre de points pour les parties de Belote classique avec annonces dans un tournoi', '1000')", 
				"insert into PARAM (libParam, valParam) values ('Nombre de points pour les parties de Belote moderne sans annonce hors tournoi', '3000')", 
				"insert into PARAM (libParam, valParam) values ('Nombre de points pour les parties de Belote moderne sans annonce dans un tournoi', '3000')", 
				"insert into PARAM (libParam, valParam) values ('Nombre de points pour les parties de Belote moderne avec annonces hors tournoi', '3000')", 
				"insert into PARAM (libParam, valParam) values ('Nombre de points pour les parties de Belote moderne avec annonces dans un tournoi', '3000')", 
				"insert into PARAM (libParam, valParam) values ('Sauvegarde des chats', 'N')", 
				"insert into PARAM (libParam, valParam) values ('Mots interdits dans le chats (regulars expressions séparées par des ,)', 'merde,conard')",
				"create table CHAT ("
				+ "pseudo varchar(80) not null,"
				+ "pseudoDest varchar(80),"
				+ "idPartie integer,"
				+ "msg varchar(255) not null,"
				+ "dtCreat timestamp with time zone default now() not null,"
				+ "constraint pk_chat primary key (dtCreat, pseudo))"
			};
            try {
				dbConnect.doExecuteSQL(reqsSQL);
			} catch (SQLException e1) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("paramAire.erreurMaj", e1));
			}
		}
		
		return errors;
	}
}
