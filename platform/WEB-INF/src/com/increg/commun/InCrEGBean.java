/*
 * Created on 6 avr. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.commun;

import java.sql.SQLException;
import com.increg.commun.exception.FctlException;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface InCrEGBean {
    /**
     * Création du client
     * Creation date: (23/07/2001 15:32:03)
     * @param dbConnect com.increg.salon.bean.DBSession
     * @throws SQLException The exception description.
     * @throws FctlException The exception description.
     */
    void create(DBSession dbConnect) throws SQLException, FctlException;
    /**
     * Suppression du client
     * Creation date: (23/07/2001 15:32:03)
     * @param dbConnect com.increg.salon.bean.DBSession
     * @exception SQLException The exception description.
     * @throws FctlException .
     */
    void delete(DBSession dbConnect) throws SQLException, FctlException;
    /**
     * Sauvegarde du client
     * Creation date: (23/07/2001 15:32:03)
     * @param dbConnect com.increg.salon.bean.DBSession
     * @exception SQLException The exception description.
     * @throws FctlException .
     */
    void maj(DBSession dbConnect) throws SQLException, FctlException;
    /**
     * Affichage sous forme texte du bean
     * Creation date: (18/08/2001 17:20:48)
     * @return Chaine représentant l'objet
     */
    String toString();
}