/*
 * Created on Apr 28, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.util;

/**
 * Classe sémaphore
 * @author EGuyot
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Semaphore {
    /**
     * Compteur de resource
     */
    private int count;

    /**
     * Constructeur
     * @param n resources disponible
     */
    public Semaphore(int n) {
        this.count = n;
    }

    /**
     * Prend une resource
     *
     */
    public synchronized void acquire() {
        while (count == 0) {
            try {
                wait();
            }
            catch (InterruptedException e) {
                //keep trying
            }
        }
        count--;
    }

    /**
     * Libère une resource
     *
     */
    public synchronized void release() {
        count++;
        notify(); //alert a thread that's blocking on this semaphore
    }
}