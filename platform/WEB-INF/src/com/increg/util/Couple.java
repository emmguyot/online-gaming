/*
 * Created on 11 juin 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.util;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Couple {

    /**
     * Cl� du couple
     */
    protected Object key;
    /**
     * Valeur du couple
     */
    protected Object value;

    /**
     * Constructeur par d�faut 
     */
    public Couple() {
    }

    /**
     * Constructeur par d�faut 
     * @param aKey .
     * @param aValue .
     */
    public Couple(Object aKey, Object aValue) {
        key = aKey;
        value = aValue;
    }

    /**
     * @return Cl� du couple
     */
    public Object getKey() {
        return key;
    }

    /**
     * @return Valeur du couple
     */
    public Object getValue() {
        return value;
    }

    /**
     * @param object Cl� du couple
     */
    public void setKey(Object object) {
        key = object;
    }

    /**
     * @param object Valeur du couple
     */
    public void setValue(Object object) {
        value = object;
    }

}
