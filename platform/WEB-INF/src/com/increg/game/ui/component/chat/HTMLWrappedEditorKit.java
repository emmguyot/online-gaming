/*
 * Created on 13 mai 2003
 *
 */
package com.increg.game.ui.component.chat;

import javax.swing.SizeRequirements;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

/**
 * @author Manu
 *
 * Classe permettant de forcer les retours en fin de ligne
 */
public class HTMLWrappedEditorKit extends HTMLEditorKit {

    /**
     * @see javax.swing.text.EditorKit#getViewFactory()
     */
    public ViewFactory getViewFactory() {
        return new HTMLWrappedFactory();
    }

    /**
     * 
     * @author Manu
     *
     * Classe interne 
     */
    public static class HTMLWrappedFactory extends HTMLFactory {

        /**
         * Creates a view from an element.
         *
         * @param elem the element
         * @return the view
         */
        public View create(Element elem) {
            Object o = elem.getAttributes().getAttribute(StyleConstants.NameAttribute);
            if (o instanceof HTML.Tag) {
                HTML.Tag kind = (HTML.Tag) o;
                if ((kind == HTML.Tag.P)
                    || (kind == HTML.Tag.H1)
                    || (kind == HTML.Tag.H2)
                    || (kind == HTML.Tag.H3)
                    || (kind == HTML.Tag.H4)
                    || (kind == HTML.Tag.H5)
                    || (kind == HTML.Tag.H6)
                    || (kind == HTML.Tag.DT)
                    || (kind == HTML.Tag.IMPLIED)) {
                    // paragraph
                    return new javax.swing.text.html.ParagraphView(elem) {
                        protected SizeRequirements calculateMinorAxisRequirements(int axis, SizeRequirements r) {

                            r = super.calculateMinorAxisRequirements(axis, r);
                            r.minimum = 0;
                            r.maximum = (int) Float.MAX_VALUE;
                            return r;
                        }
                    };
                }
                else if (kind == HTML.Tag.IMG) {
                    return new ImageView(elem);
                }
            }

            // default to text display
            return super.create(elem);
        }
    }

}
