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
import java.util.zip.DataFormatException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.increg.game.client.AireMainModel;
import com.increg.game.client.Chat;
import com.increg.game.client.Joueur;
import com.increg.game.ui.AireMain;
import com.increg.game.ui.component.chat.ChatPanel;

/**
 * @author guyot_e
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ChatPanelTest extends JFrame {

    /**
     * Panel en test
     */
    protected ChatPanel panel;

    /**
     * Aire simulée
     */
    protected AireMainModel aire;

    /**
     * Applet simulée
     */
    protected AireMain aireApplet;

    /**
     * @param title .
     * @throws HeadlessException .
     */
    public ChatPanelTest(String title) throws HeadlessException {
        super(title);

        // Initialise l'environnement        
        aireApplet = new AireMain();
        aire = new AireMainModel(aireApplet, null);

        try {
            aire.decodeConfig(config);
            Chat.decodeSmiley(configSmiley);
        }
        catch (DataFormatException e) {
            e.printStackTrace();
        }
        getContentPane().setLayout(new BorderLayout());
        JPanel bigPanel = new JPanel(null);
        bigPanel.setSize(750, 564);
        getContentPane().add(bigPanel, BorderLayout.CENTER);

        panel = new ChatPanel(bigPanel, aire);

        setVisible(true);
    }

    /**
     * Teste
     *
     */
    public void doTest() {

        System.out.println("Memoire prise : " + (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()));
        Chat addChat = new Chat();
        Joueur aJoueur = new Joueur();
        aJoueur.setPseudo("InCrEG");
        addChat.setJoueurOrig(aJoueur);
        addChat.setText("Test full Texte");
        System.out.println("Memoire prise : " + (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()));
        for (int i = 0; i < 100000; i++) {
            panel.addPendingChat(addChat);
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        panel.refresh();
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
        System.out.println("Memoire prise : " + (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()));

    }

    /**
     * @param args .
     */
    public static void main(String[] args) {

        ChatPanelTest tester = new ChatPanelTest("Testeur Mémoire ChatPanel");
        tester.doTest();
    }

    /**
     * qdsf
     */
    protected String config =
        "<skin client=\"x\" url=\"http://www.anothersite.com\">"
            + "<aire fond=\"http://www.increg.com/belote/images/fd_aire.jpg\">"
            + "<boutonGlobal bgcolor=\"#0093DD\" />"
            + "<font face=\"Trebuchet MS\" size=\"12\" style=\"0\" />"
            + "<listePartie fond=\"http://192.168.0.1:8181/belote/images/fd_liste.gif\" x=\"16\" y=\"41\" w=\"343\" h=\"490\">"
            + "<text x=\"75\" y=\"60\" w=\"170\" h=\"20\">"
            + "<font face=\"Trebuchet MS\" size=\"14\" style=\"1\" />"
            + "</text><joueur><bouton x=\"210\" y=\"125\" w=\"55\" h=\"20\" />"
            + "<avatar x=\"220\" y=\"114\" w=\"35\" h=\"35\" />"
            + "<nom x=\"161\" y=\"166\" w=\"150\" h=\"15\" />"
            + "</joueur><joueur><bouton x=\"64\" y=\"125\" w=\"55\" h=\"20\" />"
            + "<avatar x=\"74\" y=\"114\" w=\"35\" h=\"35\" />"
            + "<nom x=\"13\" y=\"166\" w=\"150\" h=\"15\" />"
            + "</joueur><joueur><bouton x=\"64\" y=\"12\" w=\"55\" h=\"20\" /><avatar x=\"74\" y=\"1\" w=\"35\" h=\"35\" />"
            + "<nom x=\"13\" y=\"45\" w=\"150\" h=\"15\" /></joueur><joueur>"
            + "<bouton x=\"210\" y=\"12\" w=\"55\" h=\"20\" />"
            + "<avatar x=\"220\" y=\"1\" w=\"35\" h=\"35\" />"
            + "<nom x=\"161\" y=\"45\" w=\"150\" h=\"15\" />"
            + "</joueur>"
            + "<cadenas img=\"http://192.168.0.1:8181/belote/images/cadenas.gif\" x=\"20\" y=\"85\" w=\"5\" h=\"5\" />"
            + "</listePartie>"
            + "<bouton>"
            + "<creation x=\"362\" y=\"420\" w=\"69\" h=\"28\" /> <fermeture x=\"434\" y=\"420\" w=\"69\" h=\"28\" />"
            + " </bouton> <listeJoueur x=\"362\" y=\"41\" w=\"141\" h=\"374\" bgcolor=\"#0093DD\"/> "
            + "<chat> <view x=\"506\" y=\"41\" w=\"234\" h=\"490\" /> "
            + "<saisie x=\"362\" y=\"537\" w=\"355\" h=\"20\" /> "
            + "<valid img=\"http://192.168.0.1:8181/belote/images/entree.gif\" x=\"717\" y=\"537\" w=\"23\" h=\"20\" /> "
            + "<smiley url=\"http://www.increg.com/belote/conf/smiley.xml\" x=\"475\" y=\"511\" w=\"28\" h=\"20\" /> "
            + "<couleur x=\"444\" y=\"511\" w=\"28\" h=\"20\" /> "
            + "<secret img=\"http://192.168.0.1:8181/belote/images/secret.gif\" x=\"413\" y=\"511\" w=\"28\" h=\"20\" /> </chat> "
            + "<lien> <copyright1 x=\"16\" y=\"535\" w=\"120\" h=\"20\" /> "
            + "<copyright2 x=\"309\" y=\"535\" w=\"50\" h=\"20\" /> </lien> "
            + "</aire> <salle fond=\"http://192.168.0.1:8181/belote/images/fd_salle.jpg\"> "
            + "<font face=\"Trebuchet MS\" size=\"14\" style=\"1\" /> <joueur> "
            + "<avatar x=\"341\" y=\"412\" w=\"35\" h=\"35\" /> "
            + "<nom x=\"260\" y=\"370\" w=\"200\" h=\"20\" /> "
            + "<score x=\"678\" y=\"58\" w=\"200\" h=\"20\" /> "
            + "<plis x=\"163\" y=\"319\" w=\"0\" h=\"0\" /> "
            + "<tapis x=\"322\" y=\"221\" w=\"76\" h=\"105\" /> "
            + "</joueur> <joueur> <avatar x=\"18\" y=\"191\" w=\"35\" h=\"35\" /> "
            + "<nom x=\"10\" y=\"245\" w=\"200\" h=\"20\" /> "
            + "<score x=\"597\" y=\"58\" w=\"200\" h=\"20\" /> "
            + "<plis x=\"531\" y=\"195\" w=\"0\" h=\"0\" /> "
            + "<tapis x=\"260\" y=\"195\" w=\"76\" h=\"105\" /> "
            + "</joueur> <joueur> <avatar x=\"343\" y=\"59\" w=\"35\" h=\"35\" /> "
            + "<nom x=\"260\" y=\"120\" w=\"200\" h=\"20\" /> <score x=\"678\" y=\"58\" w=\"200\" h=\"20\" /> "
            + "<plis x=\"163\" y=\"319\" w=\"0\" h=\"0\" /> <tapis x=\"322\" y=\"141\" w=\"76\" h=\"105\" /> "
            + "</joueur> <joueur> <avatar x=\"648\" y=\"191\" w=\"35\" h=\"35\" /> "
            + "<nom x=\"525\" y=\"245\" w=\"200\" h=\"20\" /> <score x=\"597\" y=\"58\" w=\"200\" h=\"20\" /> "
            + "<plis x=\"531\" y=\"195\" w=\"0\" h=\"0\" /> <tapis x=\"384\" y=\"195\" w=\"76\" h=\"105\" /> "
            + "</joueur> <preneur x=\"15\" y=\"99\" w=\"235\" h=\"20\" /> "
            + "<event x=\"15\" y=\"37\" w=\"235\" h=\"54\" bgcolor=\"#0093DD\"/> <chat> "
            + "<view x=\"14\" y=\"469\" w=\"702\" h=\"48\" /> <saisie x=\"98\" y=\"517\" w=\"595\" h=\"20\" /> "
            + "<valid img=\"http://192.168.0.1:8181/belote/images/entree.gif\" x=\"693\" y=\"517\" w=\"23\" h=\"20\" /> "
            + "<smiley url=\"unused\" x=\"70\" y=\"517\" w=\"28\" h=\"20\" /> "
            + "<couleur x=\"42\" y=\"517\" w=\"28\" h=\"20\" /> "
            + "<secret img=\"http://192.168.0.1:8181/belote/images/secret.gif\" x=\"14\" y=\"517\" w=\"28\" h=\"20\" /> </chat> <carte w=\"76\" h=\"105\"> <dos img=\"http://192.168.0.1:8181/belote/images/dos.gif\" /> "
            + "</carte> <distribution1 x=\"274\" y=\"210\" w=\"76\" h=\"105\" /> "
            + "<distribution2 x=\"370\" y=\"210\" w=\"76\" h=\"105\" /> "
            + "<curseur x=\"260\" y=\"320\" w=\"200\" h=\"30\" /> "
            + "<validation x=\"335\" y=\"345\" w=\"50\" h=\"20\" /> "
            + "<bouton2Cartes x=\"270\" y=\"345\" w=\"70\" h=\"20\" /> "
            + "<bouton3Cartes x=\"370\" y=\"345\" w=\"70\" h=\"20\" /> "
            + "<jeu x=\"360\" y=\"530\" r=\"100\" a=\"0.16\" /> <prise> "
            + "<bouton x=\"561\" y=\"440\" w=\"50\" h=\"20\" /> "
            + "<bouton x=\"666\" y=\"410\" w=\"50\" h=\"20\" /> "
            + "<bouton x=\"596\" y=\"410\" w=\"50\" h=\"20\" /> "
            + "<bouton x=\"526\" y=\"410\" w=\"50\" h=\"20\" /> "
            + "<bouton x=\"456\" y=\"410\" w=\"50\" h=\"20\" /> "
            + "<bouton x=\"596\" y=\"380\" w=\"50\" h=\"20\" /> "
            + "<bouton x=\"526\" y=\"380\" w=\"50\" h=\"20\" /> </prise> "
            + "<sphere x=\"635\" y=\"103\" w=\"79\" h=\"79\"> "
            + "<aiguille img=\"cadre.gif\" /> <fond img=\"sphere.gif\" /> "
            + "<font face=\"Trebuchet MS\" size=\"11\" style=\"1\" normal=\"#404040\" donneur=\"#FAD114\" maitre=\"#E24804\" /> </sphere> <boutonPlis x=\"14\" y=\"440\" w=\"120\" h=\"20\" /> "
            + "</salle> </skin>";

    /**
     * qdsf
     */
    protected String configSmiley =
        ""
            + "<conf_smiley><smiley code=\":-(b)\" img=\"http://www.anothersite.com/aire/smileys/r_s_clap.gif\"/>"
            + "<smiley code=\":-(y)\" img=\"http://www.anothersite.com/aire/smileys/s_thumbup.gif\"/>"
            + "<smiley code=\":-(n)\" img=\"http://www.anothersite.com/aire/smileys/t_thumbdown.gif\"/>"
            + "<smiley code=\":-)O\" img=\"http://www.anothersite.com/aire/smileys/m_yes.gif\"/>"
            + "<smiley code=\":-)N\" img=\"http://www.anothersite.com/aire/smileys/n_no.gif\"/>"
            + "<smiley code=\":-)\" img=\"http://www.anothersite.com/aire/smileys/a_plain.gif\"/>"
            + "<smiley code=\";-)\" img=\"http://www.anothersite.com/aire/smileys/b_wink.gif\"/>"
            + "<smiley code=\":-D\" img=\"http://www.anothersite.com/aire/smileys/c_laugh.gif\"/>"
            + "<smiley code=\":-*)\" img=\"http://www.anothersite.com/aire/smileys/d_smily_tooth.gif\"/>"
            + "<smiley code=\"!-)\" img=\"http://www.anothersite.com/aire/smileys/f_eyebrows.gif\"/>"
            + "<smiley code=\":-O\" img=\"http://www.anothersite.com/aire/smileys/g_chewingum.gif\"/>"
            + "<smiley code=\":-{\" img=\"http://www.anothersite.com/aire/smileys/h_angry.gif\"/>"
            + "<smiley code=\":-@\" img=\"http://www.anothersite.com/aire/smileys/i_angry_steaming.gif\"/>"
            + "<smiley code=\":-(\" img=\"http://www.anothersite.com/aire/smileys/j_sad.gif\"/>"
            + "<smiley code=\":'-(\" img=\"http://www.anothersite.com/aire/smileys/k_crying.gif\"/>"
            + "<smiley code=\":-S\" img=\"http://www.anothersite.com/aire/smileys/l_bhuhhh.gif\"/>"
            + "<smiley code=\"8-)\" img=\"http://www.anothersite.com/aire/smileys/o_smokin.gif\"/>"
            + "<smiley code=\":-?\" img=\"http://www.anothersite.com/aire/smileys/p_stupid.gif\"/>"
            + "<smiley code=\":-|(\" img=\"http://www.anothersite.com/aire/smileys/q_plaster.gif\"/>"
            + "<smiley code=\":-/\" img=\"http://www.anothersite.com/aire/smileys/u_thinking02y.gif\"/></conf_smiley>";

}
