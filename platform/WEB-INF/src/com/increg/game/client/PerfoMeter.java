/*
 * Created on 29 nov. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.client;

import java.util.prefs.Preferences;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class PerfoMeter {

    /**
     * Indicateur de performance
     * -1 = non initialisé
     */
    protected static int indicPerf = -1;
    
    /**
     * 
     */
    public PerfoMeter() {
        synchronized (getClass()) {
            if (indicPerf == -1) {
                indicPerf = getIndicPerf();
                if (indicPerf == -1) {
                    // Calcule l'indicateur
                    calculPerf();
                }
            }
        }
    }

    /**
     * Calcule les performances de la machine
     */
    public static synchronized void calculPerf() {
        long debut = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            Thread.yield();
            double calcul = 1.0 * i * Math.sqrt(i) / Math.log(i + 1);
            calcul++; 
        }
        indicPerf = (int) (System.currentTimeMillis() - debut);
        sauveIndicPerf();
        System.out.println("" + indicPerf);
    }
    
    /**
     * Force l'utilisation des fonctionnalités pour les machines performantes
     */
    public static void forceHautePerf() {
        indicPerf = 1;
        sauveIndicPerf();
    }

    /**
     * Force l'utilisation des fonctionnalités pour les machines faibles
     */
    public static void forceFaiblePerf() {
        indicPerf = Integer.MAX_VALUE;
        sauveIndicPerf();
    }

    /**
     * Indique si la machine a une bonne performance
     * @return true si la machine est bonne
     */
    public static boolean isHautePerf() {
        return (indicPerf < 200);    
    }
    
    /**
     * Sauvegarde l'indice de performance pour la prochaine fois
     */
    protected static void sauveIndicPerf() {
		try {
	        Preferences pref = Preferences.systemRoot().node("/com/increg/game");
	        pref.putInt("indicPerf", indicPerf);
		} catch (SecurityException e) {
        	System.err.println("Exception ignorée :");
			e.printStackTrace();
		}
    }
        
    /**
     * Récupère l'indice de performance de la dernière fois
     * @return retourne l'indice préalablement sauvegardé, -1 sinon
     */
    protected static int getIndicPerf() {
        Preferences pref;
		try {
			pref = Preferences.systemRoot().node("/com/increg/game");
	        return pref.getInt("indicPerf", -1);
		} catch (SecurityException e) {
        	System.err.println("Exception ignorée :");
			e.printStackTrace();
			return -1;
		}
    }
        
}
