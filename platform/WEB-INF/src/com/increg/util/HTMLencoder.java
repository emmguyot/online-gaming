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
 * Encode les caractères pour le HTML et le XML
 */
public class HTMLencoder {
    /**
     * Encode pour le HTML / XML
     * @param val Chaîne à encoder
     * @return Chaîne encodé
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
                    // Sinon le caractère est supprimé
                    break;
            }
        }

        return buf.toString();
    }
}
