package com.increg.util;

/**
 * Comparateur classant dans les cha�nes en ignorant tous les caract�res qui ne sont pas des caracteres
 * Creation date: 18 oct. 2003
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class StringCharOnlyComp implements java.util.Comparator {
    /**
     * StringInverseComp constructor comment.
     */
    public StringCharOnlyComp() {
        super();
    }
    /**
     * @see java.util.Comparator
     */
    public int compare(Object o1, Object o2) {
        
        // Cr�ation des cha�nes en retirant les caract�res interdit
        char[] o1Sans = new char[((String) o1).length()];
        char[] o2Sans = new char[((String) o2).length()];
        char[] o1Avec = ((String) o1).toCharArray();
        char[] o2Avec = ((String) o2).toCharArray();
        
        o1Sans = strip(o1Avec);
        o2Sans = strip(o2Avec);
        
        return new String(o1Sans).compareTo(new String(o2Sans));
    }

    /**
     * Suppression des caract�res non alpha et retourne le r�sultat
     * @param o1Avec Chaine � �pur�e
     * @return Chaine �pur�e
     */
    private char[] strip(char[] o1Avec) {
        char[] o1Sans = new char[o1Avec.length];

        int j = 0;
        for (int i = 0; i < o1Sans.length; i++) {
            if (Character.isLetterOrDigit(o1Avec[i])) {
                o1Sans[j++] = Character.toLowerCase(o1Avec[i]);
            }
        }
        
        return o1Sans;
    }
}
