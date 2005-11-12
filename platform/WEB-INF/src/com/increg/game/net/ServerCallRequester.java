/*
 * Created on 29 avr. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.net;

import java.net.URL;
import java.util.logging.Logger;

/**
 * @author Manu
 *
 * Interface pour le retour de ServerCall
 */
public interface ServerCallRequester {

    /**
     * M�thode indiquant la fin du chargement
     * @param currentUrl URL Charg�e
     * @param content Contenu obtenu
     * @return retourne l'identifiant de la derni�re info lue
     */
    int notifyLoadEnd(URL currentUrl, byte[] content);
    
    /**
     * M�thode indiquant l'annulation des chargements
     * @param currentUrl URL Charg�e
     * @return retourne l'identifiant de la derni�re info lue
     */
    int notifyLoadAbort(URL currentUrl);
    
    /**
     * @return  Dernier identifiant re�u du serveur
     */
    int getLastReceivedId();

    /**
     * @return logger � utiliser
     */
    Logger getLogger();
}
