/*
 * Created on 26 avr. 2003
 *
 * Classe effectuant l'intéraction avec le serveur (à partir du client donc)
 */
package com.increg.game.net;

import java.applet.AudioClip;
import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.WeakHashMap;

import javax.swing.ImageIcon;

import com.increg.game.ui.AireMain;

/**
 * @author Manu
 *
 * Classe effectuant l'intéraction avec le serveur (à partir du client donc)
 */
public class ServerCall {

    /**
     * Applet à l'origine de tout
     */
    private AireMain masterParent;
    /**
     * Thread de téléchargement en background
     */
    private ServerCallThread myThread;

    /**
     * DocumentBase de l'applet (reformaté pour le débug)
     */
    private String docBase;
    
    /**
     * Cache des images
     */
    private WeakHashMap<URL, ImageIcon> cacheImageIcon;
    
    /**
     * Cache des images
     */
    private WeakHashMap<URL, Image> cacheImage;

    /**
     * Cache des sons
     */
    private WeakHashMap<URL, AudioClip> cacheSon;
    
    /**
     * Constructeur 
     * @param parent Applet parente
     * @param defaultCaller Appelant par défaut pour les refresh
     */    
    public ServerCall(AireMain parent, ServerCallRequester defaultCaller) {

        // Stocke les attributs
        masterParent = parent;

        cacheImageIcon = new WeakHashMap<URL, ImageIcon>();
        cacheImage = new WeakHashMap<URL, Image>();
        cacheSon = new WeakHashMap<URL, AudioClip>();
        
        // Il faut démarrer le thread
        try {
        	docBase = masterParent.getDocBase();
        }
        catch (NullPointerException e) {
            docBase = "";
        }
        if (docBase.startsWith("file")) {
            // Débuggage local
            docBase = "http://192.168.0.1:8181/belote/";
        }
        else if (!docBase.endsWith("/")) {
            docBase = docBase + "/";
        }

        String session = parent.getParameter("sessionId");
        try {
	        if (System.getProperty("Debug") != null) {
	            // Débuggage : Ajoute la session aux URL
	            session = System.getProperty("Debug");
	        }
        }
        catch (SecurityException e) {
        	System.err.println("Exception ignorée :");
			e.printStackTrace();
		}
        myThread = new ServerCallThread(docBase, session, defaultCaller);
        // Priorité en relatif car la JVM donne une certaine priorité au groupe 4?
        myThread.setPriority(Thread.currentThread().getPriority() - 1);
    }
    
    /**
     * Destructeur
     * @see java.lang.Object#finalize()
     */
    public void finalize() {
        if (myThread != null) {
            myThread.setShouldStop(true);
            myThread = null;
        }
    }
    
    /**
     * Chargement d'une image à partir de son URL (avec gestion d'un cache)
     * @param imageUrl Image à charger
     * @return L'objet image créée (L'image n'est pas forcément loadée ==> Chargement par un autre thread)
     */
    public Image getImage(URL imageUrl) {
        
        return getImageIcon(imageUrl).getImage();
    }
    
    /**
     * Chargement d'une image à partir de son URL (avec gestion d'un cache)
     * @param imageUrl Image à charger
     * @return L'objet image créée (L'image n'est pas forcément loadée ==> Chargement par un autre thread)
     * @throws MalformedURLException URL invalide
     */
    public Image getImage(String imageUrl) throws MalformedURLException {
        return getImageIcon(imageUrl).getImage();
    }
    
    /**
     * Chargement d'une image à partir de son URL
     * @param imageUrl Image à charger
     * @return L'objet image créée (L'image n'est pas forcément loadée ==> Chargement par un autre thread)
     * @throws MalformedURLException .
     */
    public Image getRelativeImage(String imageUrl) throws MalformedURLException {

        URL theURL = null;
        
        if (imageUrl.charAt(0) == '/') {
            theURL = this.getClass().getResource(imageUrl);
        } 

        if (theURL != null) {
            // Tape dans le Jar
            return getImage(theURL);
        }
        else {
            return getImage(new URL(docBase + "/" + imageUrl));
        }
    }

    /**
     * Chargement d'une image Icone à partir de son URL
     * @param imageUrl Image à charger
     * @return L'objet image créée (L'image n'est pas forcément loadée ==> Chargement par un autre thread)
     * @throws MalformedURLException .
     */
    public ImageIcon getRelativeImageIcon(String imageUrl) throws MalformedURLException {

        URL theURL = null; 

        if (imageUrl.charAt(0) == '/') {
            theURL = this.getClass().getResource(imageUrl);
        }

        if (theURL != null) {
            // Tape dans le Jar
            return getImageIcon(theURL);
        }
        else {
            return getImageIcon(new URL(docBase + "/" + imageUrl));
        }
    }
        
    /**
     * Chargement d'une image à partir de son URL (avec gestion d'un cache)
     * @param imageUrl Image à charger
     * @return L'objet image créée (L'image n'est pas forcément loadée ==> Chargement par un autre thread)
     * @throws MalformedURLException URL invalide
     */
    public ImageIcon getImageIcon(String imageUrl) throws MalformedURLException {
        
        URL theURL = null; 

        if (imageUrl.charAt(0) == '/') {
            theURL = this.getClass().getResource(imageUrl);
        }

        if (theURL != null) {
            // Tape dans le Jar
            return getImageIcon(theURL);
        }
        else {
            return getImageIcon(new URL(imageUrl));
        }
    }
    
    /**
     * Chargement d'une image Icone à partir de son URL
     * @param imageUrl Image à charger
     * @return L'objet image créée (L'image n'est pas forcément loadée ==> Chargement par un autre thread)
     */
    public ImageIcon getImageIcon(URL imageUrl) {
        
        ImageIcon resultat = null;

        // Image dans le cache ?        
        resultat = cacheImageIcon.get(imageUrl);
        if (resultat == null) {
            // Non, on la charge
            resultat = new ImageIcon(masterParent.getAppletContext().getImage(imageUrl));
            if (!imageUrl.getProtocol().equals("jar")) {
                // Stocke dans le cache pour la prochaine fois
                cacheImageIcon.put(imageUrl, resultat);
                cacheImage.put(imageUrl, resultat.getImage());
            }
            masterParent.getLogger().finer(System.currentTimeMillis() + " : Cache fail > " + imageUrl);
        }
        else {
            masterParent.getLogger().finer(System.currentTimeMillis() + " : Cache succeed > " + imageUrl);
        }
        return resultat;
    }
    
    /**
     * Chargement d'un son à partir de son URL (avec gestion d'un cache)
     * @param sonUrl Son à charger
     * @return L'objet son créé
     * @throws MalformedURLException URL invalide
     */
    public AudioClip getAudioClip(String sonUrl) throws MalformedURLException {
        return getAudioClip(sonUrl, true);
    }
    
    /**
     * Chargement d'un son à partir de son URL (avec gestion d'un cache)
     * @param sonUrl Son à charger
     * @param doLoad Indique si le chargement doit être lancé si besoin (sinon utilisation du cache)
     * @return Son chargé ou null si pas le son en cache et le chargement n'est pas demandé
     * @throws MalformedURLException URL invalide
     */
    public AudioClip getAudioClip(String sonUrl, boolean doLoad) throws MalformedURLException {
        URL theURL = null; 

        if (sonUrl.charAt(0) == '/') {
            theURL = this.getClass().getResource(sonUrl);
        }

        if (theURL != null) {
            // Tape dans le Jar
            return getAudioClip(theURL, doLoad);
        }
        else {
            return getAudioClip(new URL(sonUrl), doLoad);
        }
    }
    
    /**
     * Chargement d'un son à partir de son URL
     * @param sonUrl Image à charger
     * @param doLoad Indique si le chargement doit être lancé si besoin (sinon utilisation du cache)
     * @return Son chargé ou null si pas le son en cache et le chargement n'est pas demandé
     */
    public AudioClip getAudioClip(URL sonUrl, boolean doLoad) {
        
        AudioClip resultat = null;

        // Image dans le cache ?        
        resultat = cacheSon.get(sonUrl);
        if (resultat == null) {
            // non pas dans le cache
            if (doLoad) {
                // Non, on la charge
                resultat = masterParent.getAppletContext().getAudioClip(sonUrl);
                if (!sonUrl.getProtocol().equals("jar")) {
                    // Stocke dans le cache pour la prochaine fois
                    cacheSon.put(sonUrl, resultat);
                }
                masterParent.getLogger().finer(System.currentTimeMillis() + " : Cache fail > " + sonUrl);
            }
        }
        else {
            masterParent.getLogger().finer(System.currentTimeMillis() + " : Cache succeed > " + sonUrl);
        }
        return resultat;
    }
    
    /**
     * Demande de chargement d'un fichier texte à partir de son URL
     * Le thread de chargement s'en charge
     * @param fileUrl URL du fichier à charger
     * @param caller Classe appelante pour retour
     */
    public void load(URL fileUrl, ServerCallRequester caller) {
        if (!myThread.isAlive()) {
            myThread.start();
        }
        myThread.addRequest(fileUrl, caller);
    }

    /**
     * Demande de chargement d'un fichier texte à partir de son URL
     * Le thread de chargement s'en charge
     * @param partialFileUrl URL relative du fichier à charger
     * @param caller Classe appelante pour retour
     */
    public void load(String partialFileUrl, ServerCallRequester caller) {
        
        URL fileUrl = null;
        try {
            if (partialFileUrl.charAt(0) == '/') {
                fileUrl = this.getClass().getResource(partialFileUrl);
            }

            masterParent.getLogger().finest(System.currentTimeMillis() + " : load : " + partialFileUrl);
            if (fileUrl == null) {
                masterParent.getLogger().finest(System.currentTimeMillis() + " : load : fileUrl == null");
                fileUrl = new URL(docBase + partialFileUrl);
            }
            load (fileUrl, caller);
            // Log Optimisation Vitesse
            masterParent.getLogger().finest(System.currentTimeMillis() + " : Action queued");
        }
        catch (MalformedURLException e) {
            masterParent.getLogger().severe("URL invalide pour " + partialFileUrl);
        }
        
    }

    /**
     * @return Dictionaire du cache
     */
    public Dictionary<URL, Image> getCacheHandler() {
        return new CacheDictionary(); 
    }

    /**
     * @author Manu
     *
     * Class dictionaire pour le cache
     * Charge si besoin les demandes 
     */
    class CacheDictionary extends Dictionary<URL, Image> {

        /**
         * Par défaut
         */
        public CacheDictionary() {
            super();
        }

        /**
         * @see java.util.Dictionary#get(java.lang.Object)
         */
        public synchronized Image get(Object key) {
            Image res = cacheImage.get(key); 
            if ((res == null) && (key instanceof URL)) {
                // Chargement
                res = getImage((URL) key);
            }
            
            return res;
        }

        /**
         * @see java.util.Dictionary#size()
         */
        public int size() {
            return cacheImage.size();
        }

        /**
         * @see java.util.Dictionary#isEmpty()
         */
        public boolean isEmpty() {
            return cacheImage.isEmpty();
        }

        /**
         * @see java.util.Dictionary#keys()
         */
        public Enumeration<URL> keys() {
            return new Enumeration<URL>() {
                Iterator<URL> myIter;

                public boolean hasMoreElements() {
                    if (myIter == null) {
                        myIter = cacheImage.keySet().iterator();
                    }
                    return myIter.hasNext();
                }

                public URL nextElement() {
                    if (myIter == null) {
                        myIter = cacheImage.keySet().iterator();
                    }
                    return myIter.next();
                }
                
            };
        }

        /**
         * @see java.util.Dictionary#elements()
         */
        public Enumeration<Image> elements() {
            return new Enumeration<Image>() {
                Iterator<Image> myIter;

                public boolean hasMoreElements() {
                    if (myIter == null) {
                        myIter = cacheImage.values().iterator();
                    }
                    return myIter.hasNext();
                }

                public Image nextElement() {
                    if (myIter == null) {
                        myIter = cacheImage.values().iterator();
                    }
                    return myIter.next();
                }
                
            };
        }

        /**
         * @see java.util.Dictionary#put(java.lang.Object, java.lang.Object)
         */
        public Image put(URL key, Image value) {
        	cacheImageIcon.put(key, new ImageIcon(value));
        	return cacheImage.put(key, value);
        }

        /**
         * @see java.util.Dictionary#remove(java.lang.Object)
         */
        public Image remove(Object key) {
            // Ne fait rien
            return null;
        }

    }
}
