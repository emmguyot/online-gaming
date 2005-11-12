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
     * Méthode indiquant la fin du chargement
     * @param currentUrl URL Chargée
     * @param content Contenu obtenu
     * @return retourne l'identifiant de la dernière info lue
     */
    int notifyLoadEnd(URL currentUrl, byte[] content);
    
    /**
     * Méthode indiquant l'annulation des chargements
     * @param currentUrl URL Chargée
     * @return retourne l'identifiant de la dernière info lue
     */
    int notifyLoadAbort(URL currentUrl);
    
    /**
     * @return  Dernier identifiant reçu du serveur
     */
    int getLastReceivedId();

    /**
     * @return logger à utiliser
     */
    Logger getLogger();
}
