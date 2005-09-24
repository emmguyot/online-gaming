/*
 * Created on 18 d�c. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.ui.component;

import java.applet.AudioClip;
import java.net.MalformedURLException;
import java.util.logging.Level;

import com.increg.game.client.AireMainModel;
import com.increg.util.Semaphore;

/**
 * @author Manu
 *
 * Pour jouer un son en t�che de fond
 */
public class SoundPlayer implements Runnable {

    /**
     * Objet pour locker l'acc�s en double au t�l�chargement
     */
    protected static Semaphore semaphoreLecture = new Semaphore(1);
    
    /**
     * Aire principale pour acc�s aux m�thodes de lecture 
     */
    protected AireMainModel aire;
    /**
     * Son � jouer
     */
    protected String son;
    
    /**
     * Constructeur
     * @param anAire Aire
     * @param aSon Son � jouer
     */
    public SoundPlayer(AireMainModel anAire, String aSon) {
        aire = anAire;
        son = aSon;
    }
    
    /**
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            Thread.yield();
            if (aire.getLogger().isLoggable(Level.FINEST)) {
            	aire.getLogger().finest(System.currentTimeMillis() + " : Debut son (" + Thread.activeCount() + ")");
            }
            AudioClip sonAJouer = aire.getAudioClip(son, false);
            if (sonAJouer != null) {
                // Joue le maintenant
                sonAJouer.play();
            }
            else {
                // Il faut charger le son : 1 � la fois
                semaphoreLecture.acquire();
                sonAJouer = aire.getAudioClip(son, true);
                semaphoreLecture.release();
                sonAJouer.play();
            }
            if (aire.getLogger().isLoggable(Level.FINEST)) {
            	aire.getLogger().finest(System.currentTimeMillis() + " : Fin son(" + Thread.activeCount() + ")");
            }
        }
        catch (MalformedURLException ignore) {
            // Ignore l'erreur
            ignore.printStackTrace();
        }
    }
    
}
