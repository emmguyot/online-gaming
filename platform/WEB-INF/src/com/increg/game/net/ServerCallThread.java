/*
 * Created on 28 avr. 2003
 *
 */
package com.increg.game.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Level;

import com.increg.game.client.AireMainModel;
import com.increg.game.client.PerfoMeter;

/**
 * @author Manu
 *
 * Thread de chargement de données
 */
public class ServerCallThread extends Thread {

    /**
     * Au minimum : Une requête toutes les secondes
     */
    public static final int POLL_INTERVAL = 1000;
    
    /**
     * Taille des bouts lus
     */ 
    private static final int CHUNK_SIZE = 4096;
    
    /**
     * Requêtes en cours et en attentes
     */
    private LinkedList requests;

    /**
     * Classe en attente de retour des requêtes en cours et en attentes
     */
    private LinkedList callers;

    /**
     * Thread doit s'arréter
     */
    private boolean shouldStop;

    /**
     * Document Base pour les appels directs
     */
    private String documentBase;
    
    /**
     * Classe à notifier pour les refreshs automatiques
     */
    private ServerCallRequester defaultCaller;
    
    /**
     * Identifiant de session Tomcat pour le passer partout
     */
    private String sessionId;

    /**
     * Indicateur d'attente de requete
     */
    private boolean waiting;

    /**
     * Ensemble des connexions ouvertes ou non
     */
    private HashMap connect;
        
    /**
     * Constructeur
     * @param docBase Base de l'application
     * @param session Session associée à l'utilisateur
     * @param caller caller par défaut pour refresh
     */
    public ServerCallThread(String docBase, String session, ServerCallRequester caller) {
        super();
        requests = new LinkedList();
        callers = new LinkedList();
        shouldStop = false;
        defaultCaller = caller;
        documentBase = docBase;
        sessionId = session;
        connect = new HashMap(2);
    }
    
    /**
     * @see java.lang.Runnable#run()
     */
    public void run() {
        
        // Nomme ce Thread
        Thread.currentThread().setName("InCrEG Loader");
        
        // Connexion globale pour optimisation
        URLConnection aCon = null;
        // Autre variable "globale" à la boucle pour éviter des désallocations si possible
        URL theRequest = null;
        ServerCallRequester caller = defaultCaller;
        InputStream restauStream = null;
        byte dataBytes[] = new byte[CHUNK_SIZE];
        int byteRead = 0;
        ByteArrayOutputStream resultat = new ByteArrayOutputStream(CHUNK_SIZE);
        int nbErreur = 0; 
        /**
         * Workaround trouvé sur 
         * http://www.experts-exchange.com/Programming/Programming_Languages/Java/Q_20470311.html
         * Pour pouvoir réutiliser les connexions
         * Attention si proxy, cela ne marche pas...
         * Attention du coup les cookies ne transitent plus naturellement, le cache navigateur est bypassé, ...
         */
        URL.setURLStreamHandlerFactory(null);
        System.getProperties().setProperty("java.protocol.handler.pkgs", "sun.net.www.protocol");
        String defaultRequest = null; 
        defaultRequest = documentBase + "refreshAll.srv;jsessionid=" + sessionId;
        
        while (!shouldStop) {
            if (requests.size() == 0) {
                // On attends
                try {
                    synchronized (this) {
                        waiting = true;
                        wait(POLL_INTERVAL);
                        waiting = false;
                    }
                }
                catch (InterruptedException e) {
                    // Interruption normale
                }
            }
            
            // Log Optimisation Vitesse
            if (caller.getLogger().isLoggable(Level.FINEST)) {
                caller.getLogger().finest(System.currentTimeMillis() + " : Ajout id Chat");
            }
            theRequest = null;
            caller = defaultCaller;
            synchronized (requests) {
                if (requests.size() == 0) {
                    // Une requête simple de demande de maj
                    try {
                        StringBuffer aRequest = new StringBuffer(defaultRequest);
                        aRequest.append("?").append(AireMainModel.URL_LAST_ID).append("=").append(caller.getLastReceivedId() + 1);
                        aRequest.append("&").append(AireMainModel.URL_HAUTE_PERF).append("=").append(PerfoMeter.isHautePerf() ? 't' : 'f');
                        theRequest = new URL(aRequest.toString());
                    }
                    catch (MalformedURLException e) {
                        // Mauvaise URL
                        caller.getLogger().severe("Erreur : URL de refresh erronée");
                    }
                }
                else {
                    StringBuffer aRequest = (StringBuffer) requests.removeFirst();

                    if (isDynamicURL(aRequest)) {
                        // Constitue l'URL avec le dernier identifiant                    
                        if (aRequest.indexOf("?") == -1) {
                            aRequest.append("?"); 
                        }
                        else {
                            aRequest.append("&"); 
                        }
                        aRequest.append(AireMainModel.URL_LAST_ID).append("=").append(caller.getLastReceivedId() + 1);
                        
                        // Ajoute le critère de perf
                        aRequest.append("&").append(AireMainModel.URL_HAUTE_PERF).append("=").append(PerfoMeter.isHautePerf() ? 't' : 'f');
                    }
                    
                    try {
                        theRequest = new URL(aRequest.toString());
                    }
                    catch (MalformedURLException e) {
                        // Mauvaise URL
                        caller.getLogger().severe("Erreur : URL erronée");
                    }
                    caller = (ServerCallRequester) callers.removeFirst();
                    // TODO Suppression trace
                    caller.getLogger().fine(theRequest.toString());
                }
            }
            
            // Lance le chargement
            aCon = (HttpURLConnection) connect.get(theRequest.getHost());
            if (caller.getLogger().isLoggable(Level.FINEST)) {
                caller.getLogger().finest(System.currentTimeMillis() + " : host=" + theRequest.getHost());
            }

            // 3 tentatives de connexion
            int nbEssai = 0;
            do {
                nbEssai++;
                try {
                    // Log Optimisation Vitesse
                    if (caller.getLogger().isLoggable(Level.FINEST)) {
                        caller.getLogger().finest(System.currentTimeMillis() + " : Avant ouverture");
                    }
                    aCon = theRequest.openConnection();
                    if (aCon instanceof HttpURLConnection) {
                        
                        caller.getLogger().finest(System.currentTimeMillis() + " : HTTP");
                        aCon.setRequestProperty("Connection", "Keep-Alive");
                        aCon.setRequestProperty("Accept", "*/*");
                        aCon.setUseCaches(false);
                        // Sauvegarde la connexion pour réutiliser
                        connect.put(theRequest.getHost(), aCon);
                    }
                    // Log Optimisation Vitesse
                    if (caller.getLogger().isLoggable(Level.FINEST)) {
                        caller.getLogger().finest(System.currentTimeMillis() + " : Après ouverture");
                    }
                } catch (Exception e) {
                    // Problème...
                    aCon = null;
                    // Petite trace de débug
                    caller.getLogger().severe("Erreur à la connexion à " + theRequest);
                    e.printStackTrace();
                    nbErreur++;
                }
            }
            while ((nbEssai < 3) && (aCon == null));

            // Quoi qu'il arrive, il n'y a rien de lu !            
            resultat.reset(); 

            if (aCon == null) {
                // Connexion impossible
                caller.getLogger().severe("Impossible de se connecter au serveur : " + theRequest);
            }
            else {
                try {
                    restauStream = aCon.getInputStream();
                    int nbIter = 0;
                    do {
                        byteRead = restauStream.read(dataBytes, 0, CHUNK_SIZE);
    
                        // Recopie dans la liste
                        if (byteRead != -1) {
                            resultat.write(dataBytes, 0, byteRead);
                        }
                        else {
                            // Log Optimisation Vitesse
                            if (caller.getLogger().isLoggable(Level.FINEST)) {
                                caller.getLogger().finest(System.currentTimeMillis() + " : Fin de lecture Iter=" + nbIter + " taille=" + resultat.size());
                            }
                        }
                        nbIter++;
                    }
                    while (byteRead != -1);
                    restauStream.close();
                    nbErreur = 0;
                    // Log Optimisation Vitesse
                    if (caller.getLogger().isLoggable(Level.FINEST)) {
                        caller.getLogger().finest(System.currentTimeMillis() + " : Après lecture");
                    }
                }
                catch (IOException e) {
                    // Pb !!
                    caller.getLogger().severe("Erreur à la lecture de " + theRequest);
                    e.printStackTrace();
                    resultat.reset();
                    nbErreur++;
                }
            }

            if (resultat.size() > 0) {
                // Notifie l'appelant
                caller.notifyLoadEnd(theRequest, resultat.toByteArray());
            }
            // Log Optimisation Vitesse
            if (caller.getLogger().isLoggable(Level.FINEST)) {
                caller.getLogger().finest(System.currentTimeMillis() + " : Après notify");
            }
            
            if (nbErreur > 10) {
                // 10 erreurs consécutives : On arrete
                shouldStop = true;
                caller.getLogger().severe("Trop d'erreurs : Arrêt du thread");
            }
        }
        
        // Fermeture des connexions réseau
        Set entry = connect.keySet();
        for (Iterator keyIter = entry.iterator(); keyIter.hasNext();) {
            String host = (String) keyIter.next();
            aCon = (HttpURLConnection) connect.get(host);
            if (aCon != null) {
                ((HttpURLConnection) aCon).disconnect();    
            }
        }
        connect.clear();
        caller.getLogger().finest(System.currentTimeMillis() + " : Fin Thread lecture");
    }

    /**
     * Ajoute une requete
     * @param nextRequests Requête à ajouter
     * @param caller Classe appelante pour retour
     */
    public void addRequest(URL nextRequests, ServerCallRequester caller) {
        // Log Optimisation Vitesse
        caller.getLogger().finest(System.currentTimeMillis() + " : Remaniement Request");
        StringBuffer sUrl = new StringBuffer(nextRequests.toString());
        if (isDynamicURL(sUrl)) {
            // C'est un appel à une servlet : Passe la session Id
            String p2;
            int pos = sUrl.indexOf("?");
            if (pos >= 0) {
                p2 = sUrl.substring(pos);
                sUrl.delete(pos, sUrl.length());
            }
            else {
                // Pas de paramètres
                p2 = "";
            }
            sUrl.append(";jsessionid=").append(sessionId).append(p2);
        }
        synchronized (requests) {
            // Ajoute à la suite simplement
            // Log Optimisation Vitesse
            if (caller.getLogger().isLoggable(Level.FINEST)) {
                caller.getLogger().finest(System.currentTimeMillis() + " : Ajout Request");
            }
            requests.addLast(sUrl);
            callers.addLast(caller);
            // Log Optimisation Vitesse
            if (caller.getLogger().isLoggable(Level.FINEST)) {
                caller.getLogger().finest(System.currentTimeMillis() + " : Request ajoutée");
            }
        }
        synchronized (this) {
            if (waiting) {
                notify();
            }
        }
    }

    /**
     * Teste si l'URL est considérée comme dynamique (appel de servlet)
     * @param sUrl URL à tester
     * @return true si c'est dynamique
     */
    protected boolean isDynamicURL(StringBuffer sUrl) {
        return ((sUrl.indexOf("?") >= 0) && (sUrl.indexOf(".srv") < sUrl.indexOf("?")))
            || ((sUrl.indexOf("?") == -1) && (sUrl.indexOf(".srv") >= 0));
    }
    /**
     * @param b Indicateur si le thread doit s'arrêter à la prochaine itération
     */
    public void setShouldStop(boolean b) {
        shouldStop = b;
    }

    /**
     * @return Classe à notifier pour les refreshs automatiques
     */
    public ServerCallRequester getDefaultCaller() {
        return defaultCaller;
    }

    /**
     * @param requester Classe à notifier pour les refreshs automatiques
     */
    public void setDefaultCaller(ServerCallRequester requester) {
        defaultCaller = requester;
    }

}
