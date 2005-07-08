/*
 * Created on 3 mai 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.bean;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.increg.commun.DBSession;
import com.increg.commun.exception.FctlException;
import com.increg.game.bean.belote.PartieBeloteBean;
import com.increg.game.client.Joueur;
import com.increg.game.client.Partie;
import com.increg.game.client.belote.PartieBelote;
import com.increg.game.client.belote.PartieBeloteClassique;

/**
 * @author Manu
 * 
 * Classe de l'environnement de l'aire
 */
public class GameEnvironment {

    /**
     * Nombre de chats conservés
     */
    public static final int NB_MAX_CHAT = 100;

    /**
     * Liste des joueurs connectés
     */
    protected Vector lstJoueur;

    /**
     * Liste des joueurs connectés en double
     */
    protected Vector lstJoueurDouble;

    /**
     * Liste des parties en cours ou activées pour les tournois
     */
    protected Vector lstPartie;

    /**
     * Liste des chats
     */
    protected Vector lstChat;

    /**
     * Offset pour la liste des chats
     */
    protected long chatOffset;

    /**
     * Numéro de séquence des parties
     */
    protected int seqPartie;

    /**
     * URL par défaut sur laquelle il y aura redirection en cas de pb
     */
    protected URL defaultRedirect;

    /**
     * URL au cas ou la JVM n'est pas bonne
     */
    protected URL redirectJVMko;

    /**
     * Indicateur si l'aire est sécurisée
     */
    protected boolean secured;
    
    /**
     * Constructeur
     */
    public GameEnvironment() {
        // Init les attributs
        lstJoueur = new Vector();
        lstJoueurDouble = new Vector();
        lstPartie = new Vector();
        lstChat = new Vector();
        chatOffset = 0;

        ResourceBundle res = ResourceBundle.getBundle(GameSession.DEFAULT_CONFIG);
        try {
            defaultRedirect = new URL(res.getString("defaultRedirect"));
        } catch (MalformedURLException e) {
            // Utilise la valeur par défaut !!!
            System.err.println("defaultRedirect invalide");
        }
        try {
            redirectJVMko = new URL(res.getString("redirectJVMko"));
        } catch (MalformedURLException e) {
            // Utilise la valeur par défaut !!!
            System.err.println("redirectJVMko invalide");
        }

        try {
            String securityType = res.getString("security"); 
            secured = ((securityType == null) || (!securityType.equalsIgnoreCase("none")));
        } catch (MissingResourceException ignored) {
            // Par défaut
            secured = true;
        }
        
        // TODO Recherche les parties initialisées pour le tournoi
    }

    /**
     * Recherche un joueur par son indice
     * 
     * @param i
     *            indice du joueur
     * @return Retourne un des joueurs connectés
     */
    public JoueurBean getJoueur(int i) {
        if ((i >= 0) && (i < lstJoueur.size())) {
            return (JoueurBean) lstJoueur.get(i);
        } else {
            return null;
        }
    }

    /**
     * Recherche une partie par son indice
     * 
     * @param i
     *            indice de la partie
     * @return Retourne une des parties
     */
    public Partie getPartie(int i) {
        if ((i >= 0) && (i < lstPartie.size())) {
            return ((PartieBean) lstPartie.get(i)).getMyPartie();
        } else {
            return null;
        }
    }

    /**
     * Recherche une partie par son identifiant
     * 
     * @param id
     *            Identifiant de la partie
     * @return Retourne une des parties
     */
    public Partie getPartieById(int id) {

        boolean found = false;
        Partie aPartie = null;
        for (int i = 0; !found && (i < lstPartie.size()); i++) {
            aPartie = ((PartieBean) lstPartie.get(i)).getMyPartie();
            if (aPartie.getIdentifiant() == id) {
                found = true;
            }
        }

        if (found) {
            return aPartie;
        } else {
            return null;
        }
    }

    /**
     * Met à jour la liste des joueurs suite à la deconnection d'un joueur
     * 
     * @param i
     *            indice du joueur
     */
    public void removeJoueur(int i) {
        lstJoueur.remove(i);
    }

    /**
     * Met à jour la liste des joueurs suite à la deconnection d'un joueur
     * 
     * @param aJoueur
     *            Joueur à supprimer de la liste
     */
    public void removeJoueur(JoueurBean aJoueur) {
        boolean done = false;
        for (int i = 0; (i < lstJoueur.size()) && !done; i++) {
            if (((JoueurBean) lstJoueur.get(i)).getCdJoueur() == aJoueur
                    .getCdJoueur()) {
                lstJoueur.remove(i);
                done = true;
            }
        }
        if (!done) {
            System.err.println("Joueur >" + aJoueur.getCdJoueur()
                    + "< introuvable dans remove");
        }
    }

    /**
     * Met à jour la liste des joueurs en double suite à la deconnection d'un
     * joueur par timeout
     * 
     * @param aJoueur
     *            Joueur à supprimer de la liste
     */
    public void removeJoueurDouble(JoueurBean aJoueur) {
        boolean done = false;
        for (int i = 0; (i < lstJoueurDouble.size()) && !done; i++) {
            if (((JoueurBean) lstJoueurDouble.get(i)).getCdJoueur() == aJoueur
                    .getCdJoueur()) {
                lstJoueurDouble.remove(i);
                done = true;
            }
        }
        if (!done) {
            System.err.println("Joueur >" + aJoueur.getCdJoueur()
                    + "< introuvable dans removeDouble");
        }
    }

    /**
     * Met à jour la liste des parties suite à la fin d'une partie Sauvegarde la
     * partie dans les historiques des joueurs
     * 
     * @param i
     *            indice du joueur
     */
    public void removePartie(int i) {
        lstPartie.remove(i);
    }

    /**
     * Met à jour la liste des parties suite à la fin d'une partie Sauvegarde la
     * partie dans les historiques des joueurs
     * 
     * @param aPartie
     *            Partie à supprimer
     */
    public void removePartie(Partie aPartie) {

        boolean done = false;
        for (int i = 0; !done && (i < lstPartie.size()); i++) {
            if (getPartie(i) == aPartie) {
                removePartie(i);
                done = true;
            }
        }
    }

    /**
     * Un joueur sort de l'aire : Nettoyage complet
     * 
     * @param myJoueur
     *            Joueur concerné
     */
    public void sortieJoueur(JoueurBean myJoueur) {
        sayBye(myJoueur);
        // Supprime le joueur des tables
        for (int i = 0; i < getLstPartie().size(); i++) {
            if (getPartie(i).joueurVoitPartie(myJoueur)) {
                // Dit au revoir dans la salle
                sayBye(myJoueur, getPartie(i));
            }
            if (joueurSeLeve(getPartie(i), myJoueur)) {
                // La salle a été supprimée...
                // Retour au début de la boucle
                i = -1;

            }
        }
        // Supprime le joueur
        removeJoueur(myJoueur);
    }

    /**
     * @return Liste de tout l'historique de chat
     */
    public Vector getLstChat() {
        return lstChat;
    }

    /**
     * @param i
     *            Indice de départ
     * @return Liste de l'historique de chat depuis l'indice fourni
     */
    public List getLstChat(long i) {
        if ((i - chatOffset) < 0) {
            i = chatOffset;
        }
        if ((i - chatOffset) > lstChat.size()) {
            // Positionne à la fin : Un vector vide sera retourné
            i = lstChat.size() + chatOffset;
        }
        return lstChat.subList((int) (i - chatOffset), lstChat.size());
    }

    /**
     * @return Liste des joueurs
     */
    public Vector getLstJoueur() {
        return lstJoueur;
    }

    /**
     * @return Liste des parties (Bean)
     */
    public Vector getLstPartie() {
        return lstPartie;
    }

    /**
     * Purge le chat afin de revenir à une taille résonnable
     */
    public void purgeChat() {
        synchronized (lstChat) {
            if (lstChat.size() > NB_MAX_CHAT) {
                chatOffset += lstChat.size() - NB_MAX_CHAT;
                lstChat.subList(0, lstChat.size() - NB_MAX_CHAT).clear();
            }
        }
    }

    /**
     * Met à jour la liste des joueurs suite à la connection d'un joueur
     * 
     * @param aJoueur
     *            Joueur qui vient de se connecter
     * @return indicateur si l'ajout a été fait : Il ne l'est pas si le joueur y
     *         est déjà
     */
    public boolean addJoueur(JoueurBean aJoueur) {
        /**
         * Si le joueur est déjà dans la liste : On le vire
         */
        boolean found = false;
        for (int i = 0; !found && (i < lstJoueur.size()); i++) {
            if (((JoueurBean) lstJoueur.get(i)).equals(aJoueur)) {
                found = true;
                // Mets l'ancien dans les double
                lstJoueurDouble.add(lstJoueur.get(i));
                // Mets le nouveau dans la liste des joueurs
                lstJoueur.set(i, aJoueur);
            }
        }
        if (!found) {
            lstJoueur.add(aJoueur);
        }
        return !found;
    }

    /**
     * Met à jour la liste des parties suite à la création d'une partie
     * 
     * @param aPartie
     *            Partie qui vient d'être créée
     */
    public synchronized void addPartie(Partie aPartie) {
        aPartie.setIdentifiant(++seqPartie);
        lstPartie.add(PartieBeloteBean
                .getPartieBeloteBean((PartieBelote) aPartie));
    }

    /**
     * Met à jour la liste des chats suite à un nouveau message
     * 
     * @param aChat
     *            Chat qui vient d'être créée
     */
    public void addChat(ChatBean aChat) {
        synchronized (lstChat) {
            // Numérote le chat en absolu
            aChat.setId(lstChat.size() + chatOffset);
            lstChat.add(aChat);
        }
        // Limite le nombre
        purgeChat();
    }

    /**
     * Un joueur se lève d'une partie
     * 
     * @param aPartie
     *            Partie d'ou il sort
     * @param aJoueur
     *            Joueur qui sort
     * @return indicateur si la suppression de la salle a été faite
     */
    public boolean joueurSeLeve(Partie aPartie, JoueurBean aJoueur) {

        boolean removed = false;
        if (aPartie.joueurSeLeve(aJoueur)) {

            // Vérification si la partie est vide
            if ((aPartie.getNbParticipant() == 0)
                    && (aPartie.getSpectateurs().size() == 0)) {
                removePartie(aPartie);
                removed = true;
            }

        }
        return removed;
    }

    /**
     * Un joueur rejoint une partie
     * 
     * @param aPartie
     *            Partie ou il va
     * @param aJoueur
     *            Joueur qui rejoint
     * @param position
     *            Position à la table
     * @return indicateur si l'ajout dans la salle a été faite
     */
    public boolean joueurRejoint(Partie aPartie, JoueurBean aJoueur,
            int position) {

        return aPartie.joueurRejoint(aJoueur, position);
    }

    /**
     * Un joueur rejoint une partie en tant spectateur
     * 
     * @param aPartie
     *            Partie ou il va
     * @param aJoueur
     *            Joueur qui rejoint
     * @return indicateur si l'ajout dans la salle a été faite
     */
    public boolean joueurRejoint(Partie aPartie, JoueurBean aJoueur) {

        return aPartie.joueurRejoint(aJoueur);
    }

    /**
     * @return pseudo joueur pour les messages systèmes par exemple
     */
    public Joueur getSystem() {
        return Joueur.getSystem();
    }

    /**
     * @return Liste des joueurs en double
     */
    public Vector getLstJoueurDouble() {
        return lstJoueurDouble;
    }

    /**
     * @return Offset pour la liste des chats
     */
    public long getChatOffset() {
        return chatOffset;
    }

    /**
     * @param i
     *            Offset pour la liste des chats
     */
    public void setChatOffset(long i) {
        chatOffset = i;
    }

    /**
     * @return URL de redirection au cas de gros pépin
     */
    public URL getDefaultRedirect() {
        return defaultRedirect;
    }

    /**
     * @param url URL de redirection au cas de gros pépin
     */
    public void setDefaultRedirect(URL url) {
        defaultRedirect = url;
    }

    /**
     * @return URL si JVM ko
     */
    public URL getRedirectJVMko() {
        return redirectJVMko;
    }

    /**
     * @param url URL si JVM ko
     */
    public void setRedirectJVMko(URL url) {
        redirectJVMko = url;
    }

    /**
     * Indique la fin de la partie : Enregistre en base
     * 
     * @param aSession
     *            Session en cours
     * @param i
     *            Numéro de la partie
     */
    public void finPartie(GameSession aSession, int i) {

        DBSession dbConnect = aSession.getMyDBSession();
        PartieBean thePartie = (PartieBean) lstPartie.get(i);

        // Déjà finie ?
        if (thePartie.getDtFin() == null) {
            // C'est vraiment la fin
            thePartie.setDtFin(Calendar.getInstance());

            try {
                ((PartieBean) lstPartie.get(i)).create(dbConnect);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (FctlException e) {
                e.printStackTrace();
            }

            // Informe le site
            informeSite(aSession, thePartie.getMyPartie());

        }
    }

    /**
     * Une partie redémarre : Mets à jour les éléments
     * 
     * @param i
     *            Numéro de la partie
     */
    public void restartPartie(int i) {
        PartieBean thePartie = (PartieBean) lstPartie.get(i);

        thePartie.setDtDebut(Calendar.getInstance());
        thePartie.setDtFin(null);
    }

    /**
     * Informe le site parent de la fin de la partie
     * 
     * @param mySession
     *            Session à utiliser
     * @param aPartie
     *            Partie concernée
     */
    public void informeSite(GameSession mySession, Partie aPartie) {
        boolean erreur = false;
        String url = "";
        URL curURL = null;
        try {
            url = mySession.getFinURL()
                    + "?jaea="
                    + URLEncoder.encode(aPartie.getParticipant(0).getPseudo(),
                            "UTF8")
                    + "&jaeb="
                    + URLEncoder.encode(aPartie.getParticipant(1).getPseudo(),
                            "UTF8")
                    + "&jbea="
                    + URLEncoder.encode(aPartie.getParticipant(2).getPseudo(),
                            "UTF8")
                    + "&jbeb="
                    + URLEncoder.encode(aPartie.getParticipant(3).getPseudo(),
                            "UTF8") + "&sca=" + aPartie.getScoreTotal(0)
                    + "&scb=" + aPartie.getScoreTotal(1) + "&type=";
            if (aPartie instanceof PartieBeloteClassique) {
                url += "C";
            } else {
                url += "M";
            }
            if ((aPartie instanceof PartieBelote)
                    && (((PartieBelote) aPartie).isAnnonce())) {
                url += "A";
            }
            if ((aPartie.getMyTournoi() != null)
                    && (aPartie.getMyTournoi().getIdentifiant() > 0)) {
                url += "&tournoi=1";
            } else {
                url += "&tournoi=0";
            }

            curURL = new URL(url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            erreur = true;
            curURL = null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            erreur = true;
            curURL = null;
        }

        /**
         * 2 Tentatives
         */
        int nbEssai = 0;
        boolean firstOne = true;
        while ((curURL != null) && (erreur || firstOne) && (nbEssai < 2)) {
            try {
                HttpURLConnection aCon = (HttpURLConnection) curURL
                        .openConnection();
                // Pas de cache : Sinon ca sert à rien
                aCon.setUseCaches(false);
                aCon.connect();
                if (aCon.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream restauStream = aCon.getInputStream();
                    // Rien à lire : C'est juste pour être sur d'avoir tout
                    // chargé
                    restauStream.close();
                    aCon.disconnect();
                    aCon = null;
                } else {
                    erreur = true;
                    // Pb : Envoi par email
                    aCon.disconnect();
                    aCon = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                erreur = true;
            }

            nbEssai++;
            firstOne = false;
        }

        if (erreur) {
            ResourceBundle res = ResourceBundle.getBundle(GameSession.DEFAULT_CONFIG);
            Properties props = System.getProperties();
            props.put("mail.smtp.host", res.getString("serveurSMTP"));
            Session mailSession = Session.getDefaultInstance(props, null);
            Message msg = new MimeMessage(mailSession);

            try {
                msg.setFrom(new InternetAddress(res.getString("fromEMail")));
                msg.setRecipients(Message.RecipientType.TO, InternetAddress
                        .parse(res.getString("webmaster"), false));
                msg.setRecipients(Message.RecipientType.BCC, InternetAddress
                        .parse(res.getString("fromEMail"), false));
                msg.setSubject("Aire de Jeu : Fin de partie non transmise");
                msg.setText(url);
                msg.setHeader("X-Mailer", "InCrEG automation");
                msg.setSentDate(new Date());

                Transport.send(msg);
            } catch (AddressException e1) {
                e1.printStackTrace();
                System.err.println("URL = " + url);
            } catch (MessagingException e1) {
                e1.printStackTrace();
                System.err.println("URL = " + url);
            }

        }
    }

    /**
     * Envoi un chat de bienvenue
     * 
     * @param aJoueur
     *            Joueur qui parle
     */
    public void sayHello(JoueurBean aJoueur) {
        sayHello(aJoueur, null);
    }

    /**
     * Envoi un chat d'aure voir
     * 
     * @param aJoueur
     *            Joueur qui parle
     */
    public void sayBye(JoueurBean aJoueur) {
        sayBye(aJoueur, null);
    }

    /**
     * Envoi un chat de bienvenue
     * 
     * @param aJoueur
     *            Joueur qui parle
     * @param aPartie
     *            Partie associé au bienvenue
     */
    public void sayHello(JoueurBean aJoueur, Partie aPartie) {
        // Un petit chat d'avertissement
        ChatBean hiChat = new ChatBean();
        hiChat.setJoueurOrig(getSystem());
        hiChat.setStyle("<font color=\"green\"><b>");
        hiChat.setText("Bonjour " + aJoueur.getPseudo() + " [salut]");
        hiChat.setPartie(aPartie);
        addChat(hiChat);
    }

    /**
     * Envoi un chat d'aure voir
     * 
     * @param aJoueur
     *            Joueur qui parle
     * @param aPartie
     *            Partie associé à l'au revoir
     */
    public void sayBye(JoueurBean aJoueur, Partie aPartie) {
        // Un petit chat d'avertissement
        ChatBean byeChat = new ChatBean();
        byeChat.setJoueurOrig(getSystem());
        byeChat.setStyle("<font color=\"red\"><b>");
        byeChat.setText("Au revoir " + aJoueur.getPseudo());
        byeChat.setPartie(aPartie);
        addChat(byeChat);
    }

    /**
     * @return Returns the secured.
     */
    public boolean isSecured() {
        return secured;
    }
    /**
     * @param newSecured The secured to set.
     */
    public void setSecured(boolean newSecured) {
        this.secured = newSecured;
    }
}