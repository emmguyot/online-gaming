/*
 * Created on 3 mai 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.bean;

import java.sql.SQLException;

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

}
