/*
 * Created on 30 juin 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.ui.component.chat.test;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.StringContent;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import javax.swing.text.Element;

import com.increg.game.client.Chat;
import com.increg.game.client.Joueur;

/**
 * @author guyot_e
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ChatPanelTest3 extends JFrame {

    /**
     * Panel en test
     */
    protected JTextPane chatView;
    /**
     * .
     */
    protected HTMLDocument doc;

    /**
     * .
     */
    protected String baliseFont;
    /**
     * .
     */
    protected String pendingChat;
    /**
     * .
     */
    protected int num;
    /**
     * .
     */
    protected int lastRemoved;
    /**
     * .
     */
    protected StyleSheet style;
    /**
     * @param title .
     * @throws HeadlessException .
     */
    public ChatPanelTest3(String title) throws HeadlessException {
        super(title);

        JScrollPane scrollChat = new javax.swing.JScrollPane();
        scrollChat.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollChat.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        chatView = new JTextPane();
        //chatView.setEditable(false);
        //chatView.setContentType("text/html");
        
//        chatView.setEditorKit(new StyledEditorKit());
        
        scrollChat.setSize(234, 490);

        style = new StyleSheet();
        style.addRule(".allbody { font-family: Trebuchet MS; text-align: left; color: #990000; font-weight:bold }");
        doc = new HTMLDocument(new StringContent(), style);
        HTMLEditorKit kit = new HTMLEditorKit();
        try {
                Class c = Class.forName("javax.swing.text.html.parser.ParserDelegator");
            doc.setParser((HTMLEditorKit.Parser) c.newInstance());
        } catch (Throwable e) {
            e.printStackTrace();
        }

        chatView.setEditorKit(kit);
        chatView.setDocument(doc);
        
        baliseFont = "<font face=\"Trebuchet MS\" size=\"4\">";
        //chatView.setText("<html><body><p id=\"DebutBody\">&nbsp;</p><div id=\"FinBody\">&nbsp;</div></body></html>");

        Element elt = doc.getRootElements()[0].getElement(0);
        try {
            //hdoc.insertString(elt.getStartOffset(), pendingChat, hdoc.getStyle(".body"));
            doc.insertAfterStart(elt, "<p id=\"DebutBody\">&nbsp;</p><div id=\"FinBody\">&nbsp;</div>");
        }
        catch (BadLocationException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        scrollChat.setViewportView(chatView);
        
        getContentPane().add(scrollChat, BorderLayout.CENTER);

        setVisible(true);
        pack();
    }

    /**
     * Teste
     *
     */
    public void doTest() {
        
        System.out.println("Memoire prise : " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
        Chat addChat = new Chat();
        Joueur aJoueur = new Joueur();
        aJoueur.setPseudo("InCrEG");
        addChat.setJoueurOrig(aJoueur);
        addChat.setText("Test full Texte");
        System.gc();
        System.out.println("Memoire prise : " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
        long memInit = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        lastRemoved = 0;
        for (int i = 0 ; i < 100000; i++) {
            System.gc();
            String msgTot = "<i><b>InCrEG</b></i> : Test full Texte (" + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() - memInit) + ")";

            num = i;
            // Un espace à la fin pour que la dernière balise fonte ne soit pas supprimée
            pendingChat = "<p class=\"allbody\" id=\"" + num + "\">" + num + "-" + msgTot + "&nbsp;</p>";
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        
                        Element elt = doc.getElement("DebutBody");
                        try {
                            //hdoc.insertString(elt.getStartOffset(), pendingChat, hdoc.getStyle(".body"));
                            doc.insertBeforeStart(elt, pendingChat);
                        }
                        catch (BadLocationException e) {
                            e.printStackTrace();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        
                        
                        if (num > 30) {
                            try {
                                elt = doc.getElement(Integer.toString(lastRemoved));
                                doc.remove(elt.getStartOffset(), elt.getEndOffset());
                                //lastRemoved++;
                            }
                            catch (BadLocationException e1) {
                                e1.printStackTrace();
                            }
                            catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }
                        
                        
//                        String contenu = chatView.getText();
//                        int pos = contenu.lastIndexOf("</font>");
//                        if (pos != -1) {
//                            /**
//                             * Optimise la chaîne et coupe par rapport au nombre de lignes
//                             */
//                            StringBuffer newChat = new StringBuffer(contenu.substring(0, pos) + pendingChat + "</font></body></html>");
//                            int nbBlanc = 0;
//                            for (int i = 0 ; i < newChat.length(); i++) {
//                                if (newChat.charAt(i) == ' ') {
//                                    nbBlanc++;
//                                }
//                                else {
//                                    if (nbBlanc > 1) {
//                                        // Supprime les n blancs par un seul
//                                        newChat = newChat.delete(i - (nbBlanc - 1), i);
//                                        i -= (nbBlanc - 1);
//                                    }
//                                    nbBlanc = 0;
//                                }
//                            }
//                            // Compte les lignes
//                            pos = newChat.lastIndexOf("<br>");
//                            int nbLigne = 0;
//                            while ((pos != -1) && (nbLigne <= 30)) {
//                                nbLigne++;
//    
//                                if (nbLigne > 30) {
//                                    // Il faut couper
//                                    int firstPos = newChat.indexOf("<br>");
//                                    newChat = newChat.delete(firstPos, pos);
//                                }
//                        
//                                pos = newChat.lastIndexOf("<br>", pos - 1);
//                            }
//                            chatView.setText(newChat.toString());
//                        }                        
                        Thread.yield();
                    }
                });
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Memoire prise : " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));

    }
    
    /**
     * @param args .
     */
    public static void main(String[] args) {

        ChatPanelTest3 tester = new ChatPanelTest3("Testeur Mémoire ChatPanel");
        tester.doTest();
    }
}
