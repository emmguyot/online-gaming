/*
 * Created on 25 juin 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CompileLog {

    /**
     * 
     */
    public CompileLog() {
        super();
    }

    /**
     * 
     * @param argv .
     */
    public static void main(String[] argv) {
        
        if (argv.length != 1) {
            System.err.println("Usage : CompileLog fichierlog");
            return;
        }
        
        try {
            FileInputStream fis = new FileInputStream(argv[0]);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            long currentTime = 0;
            String ligne;
            long debutClic = -1;
            long debutRefreshGraphique = -1;
            long debutRefresh = -1;
            long debutReseau = -1;
            long finRefresh = -1;
            boolean vraiClic = false;
            int posDebutTime = 0;
        
            while ((ligne = br.readLine()) != null) {
                
                if ((ligne.length() > 0) && (ligne.indexOf("com.increg") == -1)) {
                    posDebutTime = ligne.indexOf(": ") + 2;
                    if (!Character.isDigit(ligne.charAt(posDebutTime))) {
                        vraiClic = true;
                    }
                    else {
                        currentTime = Long.parseLong(ligne.substring(posDebutTime, ligne.indexOf(" ", posDebutTime)));
                        if (ligne.indexOf("Clic souris") >= 0) {
                            debutClic = currentTime;
                            vraiClic = false;
                        }
                        else if (ligne.indexOf("Début refresh graphique") >= 0) {
                            debutRefreshGraphique = currentTime;
                        }
                        else if (ligne.indexOf("Ajout id Chat") >= 0) {
                            if (finRefresh > 0) {
                                System.out.println(currentTime + ";IR;" + (currentTime - finRefresh));
                            }
                            finRefresh = -1;
                            debutRefresh = currentTime;
                        }
                        else if (ligne.indexOf("Fin Refresh graphique") >= 0) {
                            if (debutRefreshGraphique > 0) {
                                System.out.println(currentTime + ";RG;" + (currentTime - debutRefreshGraphique));
                            }
                            debutRefreshGraphique = -1;
                        }
                        else if (ligne.indexOf("Après notify") >= 0) {
                            if ((debutClic > 0) && vraiClic) {
                                System.out.println(currentTime + ";JC;" + (currentTime - debutClic));
                            }
                            debutClic = -1;
                            if (debutRefresh > 0) {
                                System.out.println(currentTime + ";FR;" + (currentTime - debutRefresh));
                            }
                            debutRefresh = -1;
                            finRefresh = currentTime;
                        }
                        else if (ligne.indexOf("Avant ouverture") >= 0) {
                            debutReseau = currentTime;
                        }
                        else if (ligne.indexOf("Après lecture") >= 0) {
                            if (debutReseau > 0) {
                                System.out.println(currentTime + ";LR;" + (currentTime - debutReseau));
                            }
                            debutReseau = -1;
                        }
                    }
                }
            }
            
            br.close();
            fis.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
