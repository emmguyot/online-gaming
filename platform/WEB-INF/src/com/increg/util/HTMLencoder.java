/*
 * Created on 13 mai 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.util;

/**
 * @author Manu
 *
 * Encode les caract�res pour le HTML et le XML
 */
public class HTMLencoder {
    /**
     * Encode pour le HTML / XML
     * @param val Cha�ne � encoder
     * @return Cha�ne encod�
     */
    public static String htmlEncode(String val) {
        StringBuffer buf = new StringBuffer(val.length() + 8);
        char c;

        for (int i = 0; i < val.length(); i++) {
            c = val.charAt(i);

            switch (c) {
                case '<' :
                    buf.append("&lt;");
                    break;
                case '>' :
                    buf.append("&gt;");
                    break;
                case '&' :
                    buf.append("&amp;");
                    break;
                case '\"' :
                    buf.append("&quot;");
                    break;
                case '\n' :
                    buf.append(" ");
                    break;
                case '\t' :
                    buf.append(" ");
                    break;
                default :
                    if (c >= ' ') { 
                        buf.append(c);
                    }
                    // Sinon le caract�re est supprim�
                    break;
            }
        }

        return buf.toString();
    }
}
