/*
 * Created on 5 déc. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.client;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SmileySon {

    /**
     * Code du son
     */
    protected String code;
    /**
     * Image icone représentant le son
     */
    protected String image;
    /**
     * Son correspondant
     */
    protected String son;

    /* ***************************************
     * Méthodes 
     * *************************************** */    

    /**
     * @param aCode Code du son
     * @param aImage Icone du son
     * @param aSon Son proprement dit
     */
    public SmileySon(String aCode, String aImage, String aSon) {
        code = aCode;
        image = aImage;
        son = aSon; 
    }

    /**
     * @return Code du son
     */
    public String getCode() {
        return code;
    }

    /**
     * @return Image icone représentant le son
     */
    public String getImage() {
        return image;
    }

    /**
     * @return Son correspondant
     */
    public String getSon() {
        return son;
    }

    /**
     * @param string Code du son
     */
    public void setCode(String string) {
        code = string;
    }

    /**
     * @param string Image icone représentant le son
     */
    public void setImage(String string) {
        image = string;
    }

    /**
     * @param string Son correspondant
     */
    public void setSon(String string) {
        son = string;
    }

}
