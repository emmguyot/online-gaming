/*
 * Created on 30 juin 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.ui.component.chat.test;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.increg.game.client.Chat;
import com.increg.game.client.Joueur;
import com.increg.game.ui.component.chat.HTMLWrappedEditorKit;
import com.increg.game.ui.component.chat.JTextPane;

/**
 * @author guyot_e
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ChatPanelTest2 extends JFrame {

    /**
     * Panel en test
     */
    protected JTextPane chatView;

    /**
     * .
     */
    protected String baliseFont;
    /**
     * .
     */
    protected String pendingChat;
    /**
     * @param title .
     * @throws HeadlessException .
     */
    public ChatPanelTest2(String title) throws HeadlessException {
        super(title);

        JScrollPane scrollChat = new javax.swing.JScrollPane();
        scrollChat.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollChat.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        chatView = new JTextPane();
        chatView.setEditable(false);
        chatView.setContentType("text/html");
        
        chatView.setEditorKit(new HTMLWrappedEditorKit());
//        chatView.setEditorKit(new StyledEditorKit());
        
        scrollChat.setSize(234, 490);

        baliseFont = "<font face=\"Trebuchet MS\" size=\"4\">";
        chatView.setText("<html><body>" + baliseFont + "<br></font></body></html>");

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
        for (int i = 0 ; i < 100000; i++) {
            System.gc();
            String msgTot = "<i><b>InCrEG</b></i> : Test full Texte (" + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() - memInit) + ")";
        
            // Gère les remplacements de fonte
            int pos = msgTot.indexOf("<font");
            while (pos >= 0) {
            
                // Intercale une fin de fonte (de la précédente)
                msgTot = msgTot.substring(0, pos) + "</font>" + msgTot.substring(pos);
            
                /**
                 * Force la police pour contourner le Bug Java
                 */
                int posFin = msgTot.indexOf(">", pos + 7);
                int posFace = msgTot.indexOf("face=", pos + 7);
            
                if ((posFace == -1) || (posFace > posFin)) {
                    // Ajoute la face et la taille
                    msgTot = msgTot.substring(0, posFin) + baliseFont.substring(5) + msgTot.substring(posFin + 1);
                }
            
                int pos2 = msgTot.indexOf("</font>", pos + 1);
            
                // Intercale le début de fonte 
                msgTot = msgTot.substring(0, pos2 + 7) + baliseFont + msgTot.substring(pos2 + 7);
            
                pos = msgTot.indexOf("<font", pos2 + 8);
            }

            // Un espace à la fin pour que la dernière balise fonte ne soit pas supprimée
            pendingChat = "<br>" + msgTot + "&nbsp;"; 
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        String contenu = chatView.getText();
                        int pos = contenu.lastIndexOf("</font>");
                        if (pos != -1) {
                            /**
                             * Optimise la chaîne et coupe par rapport au nombre de lignes
                             */
                            StringBuffer newChat = new StringBuffer(contenu.substring(0, pos) + pendingChat + "</font></body></html>");
                            int nbBlanc = 0;
                            for (int i = 0 ; i < newChat.length(); i++) {
                                if (newChat.charAt(i) == ' ') {
                                    nbBlanc++;
                                }
                                else {
                                    if (nbBlanc > 1) {
                                        // Supprime les n blancs par un seul
                                        newChat = newChat.delete(i - (nbBlanc - 1), i);
                                        i -= (nbBlanc - 1);
                                    }
                                    nbBlanc = 0;
                                }
                            }
                            // Compte les lignes
                            pos = newChat.lastIndexOf("<br>");
                            int nbLigne = 0;
                            while ((pos != -1) && (nbLigne <= 30)) {
                                nbLigne++;
    
                                if (nbLigne > 30) {
                                    // Il faut couper
                                    int firstPos = newChat.indexOf("<br>");
                                    newChat = newChat.delete(firstPos, pos);
                                }
                        
                                pos = newChat.lastIndexOf("<br>", pos - 1);
                            }
                            chatView.setText(newChat.toString());
                        }                        
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

        ChatPanelTest2 tester = new ChatPanelTest2("Testeur Mémoire ChatPanel");
        tester.doTest();
    }
}
