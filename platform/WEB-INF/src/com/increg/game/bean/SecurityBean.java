/*
 * Created on 14 avr. 2003
 *
 * Bean de contrôle de sécurité de l'accès à l'aire
 */
package com.increg.game.bean;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.increg.commun.exception.UnauthorisedUserException;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SecurityBean {

    /**
     * Formatteur de la date
     */
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");

    /**
     * Phrase clé pour le calcul du code CRC
     */
    private String passPhrase = "";
    
    /**
     * Constructeur qui fait la vérification
     * @param id Identifiant de l'utilisateur
     * @param crc Code proposé à vérifier
     * @param aPassPhrase Phrase clé pour le calcul
     * @throws UnauthorisedUserException Si le code est invalide
     */
    public SecurityBean(String id, String crc, String aPassPhrase) throws UnauthorisedUserException {

        passPhrase = aPassPhrase;

        if (!crc.equals(getCRC(id))) {
            // Pas bon
            throw new UnauthorisedUserException();
        }
    }

    /**
     * Calcule le CRC pour l'id fourni
     * @param id pseudo du user
     * @return code crc
     */
    public String getCRC(String id) {    
        String crcCalc = null;
        /**
         * Algo de calcul du CRC
         * 1) Création de la chaine de caractères 
         *      id + passPhrase + date (format jjmmaaaa)
         * 2) Création du digest MD5 correspondant à cette chaîne
         * 3) Conversion en hexadécimal du digest
         */        
        String msg = id 
                    + passPhrase
                    + dateFormat.format(Calendar.getInstance().getTime()); 

        crcCalc = calcCRC(msg);            
        
        return crcCalc;
    }

    /**
     * Calcule le CRC MD5 et conversion héxa
     * @param phrase Phrase à signée
     * @return Chaine CRC
     */    
    public static String calcCRC(String phrase) {

        StringBuffer crcCalc = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5"); //$NON-NLS-1$
            md.update(phrase.getBytes());
                
            byte[] tabDigest = md.digest();
            for (int i = 0; i < tabDigest.length; i++) {
                String octet = "0" + Integer.toHexString(tabDigest[i]);
                 
                crcCalc.append(octet.substring(octet.length() - 2));
            }
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        
        return crcCalc.toString();
    }
    /**
     * @return Phrase clé pour le calcul du code CRC
     */
    public String getPassPhrase() {
        return passPhrase;
    }

    /**
     * @param string Phrase clé pour le calcul du code CRC
     */
    public void setPassPhrase(String string) {
        passPhrase = string;
    }

}
