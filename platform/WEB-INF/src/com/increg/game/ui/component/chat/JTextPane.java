/*
 * Created on 4 juil. 2003
 *
 */
package com.increg.game.ui.component.chat;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.StyledDocument;

/**
 * @author Manu
 *
 * Class surchargeant JTextPane pour faire face à la non libération des styles dans le document HTML
 * Ce document est recréé complètement à chaque mise à jour
 */
public class JTextPane extends javax.swing.JTextPane {

    /**
     * 
     */
    public JTextPane() {
        super();
    }

    /**
     * @param doc .
     */
    public JTextPane(StyledDocument doc) {
        super(doc);
    }

    /**
     * Sets the text of this <code>TextComponent</code> to the specified
     * content,
     * which is expected to be in the format of the content type of
     * this editor.  For example, if the type is set to <code>text/html</code>
     * the string should be specified in terms of HTML.  
     * <p>
     * This is implemented to remove the contents of the current document,
     * and replace them by parsing the given string using the current
     * <code>EditorKit</code>.  This gives the semantics of the
     * superclass by not changing
     * out the model, while supporting the content type currently set on
     * this component.  The assumption is that the previous content is
     * relatively
     * small, and that the previous content doesn't have side effects.
     * Both of those assumptions can be violated and cause undesirable results.
     * To avoid this, create a new document,
     * <code>getEditorKit().createDefaultDocument()</code>, and replace the
     * existing <code>Document</code> with the new one. You are then assured the
     * previous <code>Document</code> won't have any lingering state.
     * <ol>
     * <li>
     * Leaving the existing model in place means that the old view will be
     * torn down, and a new view created, where replacing the document would
     * avoid the tear down of the old view.
     * <li>
     * Some formats (such as HTML) can install things into the document that
     * can influence future contents.  HTML can have style information embedded
     * that would influence the next content installed unexpectedly.
     * </ol>
     * <p>
     * An alternative way to load this component with a string would be to
     * create a StringReader and call the read method.  In this case the model
     * would be replaced after it was initialized with the contents of the
     * string.
     * <p>
     * This method is thread safe, although most Swing methods
     * are not. Please see 
     * <A HREF="http://java.sun.com/products/jfc/swingdoc-archive/threads.html">Threads
     * and Swing</A> for more information.     
     *
     * @param t the new text to be set
     * @see #getText
     * @beaninfo
     * description: the text of this component
     */
    public void setText(String t) {
        try {
            Document doc;
            EditorKit kit = getEditorKit();
            doc = kit.createDefaultDocument();
            doc.putProperty("imageCache", getDocument().getProperty("imageCache"));
            if (t == null || t.equals("")) {
                return;
            }
            Reader r = new StringReader(t);
            kit.read(r, doc, 0);
            setDocument(doc);
        }
        catch (IOException ioe) {
            UIManager.getLookAndFeel().provideErrorFeedback(this);
        }
        catch (BadLocationException ble) {
            UIManager.getLookAndFeel().provideErrorFeedback(this);
        }
    }

}
