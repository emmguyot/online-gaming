/*
 * Classe de l'environnement de l'aire
 * Copyright (C) 2003-2011 Emmanuel Guyot <See emmguyot on SourceForge> 
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation; either 
 * version 2 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; 
 * if not, write to the 
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 */
package com.increg.game.bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;

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
     * Nombre de chats conserv�s
     */
    public static final int NB_MAX_CHAT = 100;

    /**
     * Duree de vie des chats (en heures)
     */
    public static final int DUREE_VIE_CHAT = 2;

    /**
     * Liste des joueurs connect�s
     */
    protected Vector<JoueurBean> lstJoueur;

    /**
     * Liste des joueurs connect�s en double
     */
    protected Vector<JoueurBean> lstJoueurDouble;

    /**
     * Liste des parties en cours ou activ�es pour les tournois
     */
    protected Vector<PartieBeloteBean> lstPartie;

    /**
     * Liste des chats
     */
    protected Vector<ChatBean> lstChat;

    /**
     * Offset pour la liste des chats
     */
    protected long chatOffset;

    /**
     * Num�ro de s�quence des parties
     */
    protected int seqPartie;

    /**
     * URL par d�faut sur laquelle il y aura redirection en cas de pb
     */
    protected URL defaultRedirect;

    /**
     * URL au cas ou la JVM n'est pas bonne
     */
    protected URL redirectJVMko;

    /**
     * Indicateur si l'aire est s�curis�e
     */
    protected boolean secured;
    
    /**
     * Param�tres de l'aire
     */
    protected Map<Integer, String> paramAire;
    
    /**
     * Cache pour les mots interdits 
     */
    protected Map<String, Pattern> cachePatternMotInterdit;
    
    /**
     * Constructeur
     */
    public GameEnvironment() {
        // Init les attributs
        lstJoueur = new Vector<JoueurBean>();
        lstJoueurDouble = new Vector<JoueurBean>();
        lstPartie = new Vector<PartieBeloteBean>();
        lstChat = new Vector<ChatBean>();
        chatOffset = 0;
        cachePatternMotInterdit = new HashMap<String, Pattern>();

        ResourceBundle res = ResourceBundle.getBundle(GameSession.DEFAULT_CONFIG);
        try {
            defaultRedirect = new URL(res.getString("defaultRedirect"));
        } catch (MalformedURLException e) {
            // Utilise la valeur par d�faut !!!
            System.err.println("defaultRedirect invalide");
        }
        try {
            redirectJVMko = new URL(res.getString("redirectJVMko"));
        } catch (MalformedURLException e) {
            // Utilise la valeur par d�faut !!!
            System.err.println("redirectJVMko invalide");
        }

        try {
            String securityType = res.getString("security"); 
            secured = ((securityType == null) || (!securityType.equalsIgnoreCase("none")));
        } catch (MissingResourceException ignored) {
            // Par d�faut
            secured = true;
        }
        
        // TODO Recherche les parties initialis�es pour le tournoi
    }

    /**
	 * @return Returns the paramAire.
	 */
	public Map<Integer, String> getParamAire() {
		return paramAire;
	}

    /**
	 * @param code code du param�tre � charger
	 * @return Returns la valeur du param�tre.
	 */
	public String getParamAire(int code) {
		if (paramAire != null) {
			return paramAire.get(new Integer(code));
		}
		else {
			return null;
		}
	}

	/**
	 * @param paramAire The paramAire to set.
	 */
	public void setParamAire(Map<Integer, String> paramAire) {
		this.paramAire = paramAire;
	}

	/**
	 * @param dbConnect Connection base � utiliser
	 */
	public void loadParamAire(DBSession dbConnect) {
		loadParamAire(dbConnect, false);
	}

	/**
	 * @param dbConnect Connection base � utiliser
	 * @param reload force le rechargement
	 */
	public void loadParamAire(DBSession dbConnect, boolean reload) {
		if (paramAire == null) {
			paramAire = new TreeMap<Integer, String>();
		}
		
		if (paramAire.isEmpty() || reload) {
			String reqSQL = "select * from param";
			try {
				ResultSet rs = dbConnect.doRequest(reqSQL);
				
				while (rs.next()) {
					ParamBean aParam = new ParamBean(rs);
					paramAire.put(new Integer(aParam.getCdParam()), aParam.getValParam());
				}
				rs.close();
			}
			catch (SQLException e) {
				// Probl�me de lecture : Il faudra recommencer
	            System.err.println("Probl�me � la lecture des param�tres :");
	            e.printStackTrace();
	            if (!reload) {
	            	// Reset pour rechargement
	            	paramAire = null;
	            }
			}
		}
		
		if (reload) {
			// Reset du cache
			cachePatternMotInterdit.clear();
		}
	}
	
	/**
     * Recherche un joueur par son indice
     * 
     * @param i
     *            indice du joueur
     * @return Retourne un des joueurs connect�s
     */
    public JoueurBean getJoueur(int i) {
        if ((i >= 0) && (i < lstJoueur.size())) {
            return lstJoueur.get(i);
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
            return lstPartie.get(i).getMyPartie();
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
            aPartie = lstPartie.get(i).getMyPartie();
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
     * Met � jour la liste des joueurs suite � la deconnection d'un joueur
     * 
     * @param i
     *            indice du joueur
     */
    public void removeJoueur(int i) {
        lstJoueur.remove(i);
    }

    /**
     * Met � jour la liste des joueurs suite � la deconnection d'un joueur
     * 
     * @param aJoueur
     *            Joueur � supprimer de la liste
     */
    public void removeJoueur(JoueurBean aJoueur) {
        boolean done = false;
        for (int i = 0; (i < lstJoueur.size()) && !done; i++) {
            if (lstJoueur.get(i).getCdJoueur() == aJoueur
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
     * Met � jour la liste des joueurs en double suite � la deconnection d'un
     * joueur par timeout
     * 
     * @param aJoueur
     *            Joueur � supprimer de la liste
     */
    public void removeJoueurDouble(JoueurBean aJoueur) {
        boolean done = false;
        for (int i = 0; (i < lstJoueurDouble.size()) && !done; i++) {
            if (lstJoueurDouble.get(i).getCdJoueur() == aJoueur
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
     * Met � jour la liste des parties suite � la fin d'une partie Sauvegarde la
     * partie dans les historiques des joueurs
     * 
     * @param i
     *            indice du joueur
     */
    public void removePartie(int i) {
        lstPartie.remove(i);
    }

    /**
     * Met � jour la liste des parties suite � la fin d'une partie Sauvegarde la
     * partie dans les historiques des joueurs
     * 
     * @param aPartie
     *            Partie � supprimer
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
     *            Joueur concern�
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
                // La salle a �t� supprim�e...
                // Retour au d�but de la boucle
                i = -1;

            }
        }
        // Supprime le joueur
        removeJoueur(myJoueur);
        
        // M�nage dans le chat
        if (getLstJoueur().size() == 0) {
        	// Plus personne : Reset
        	setChatOffset(0);
        }
    }

    /**
     * @return Liste de tout l'historique de chat
     */
    public Vector<ChatBean> getLstChat() {
        return lstChat;
    }

    /**
     * @param i
     *            Indice de d�part
     * @return Liste de l'historique de chat depuis l'indice fourni
     */
    public List<ChatBean> getLstChat(long i) {
        if ((i - chatOffset) < 0) {
            i = chatOffset;
        }
        if ((i - chatOffset) > lstChat.size()) {
            // Positionne � la fin : Un vector vide sera retourn�
            i = lstChat.size() + chatOffset;
        }
        return lstChat.subList((int) (i - chatOffset), lstChat.size());
    }

    /**
     * @return Liste des joueurs
     */
    public Vector<JoueurBean> getLstJoueur() {
        return lstJoueur;
    }

    /**
     * @return Liste des parties (Bean)
     */
    public Vector<PartieBeloteBean> getLstPartie() {
        return lstPartie;
    }

    /**
     * Purge le chat afin de revenir � une taille r�sonnable
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
     * Met � jour la liste des joueurs suite � la connection d'un joueur
     * 
     * @param aJoueur
     *            Joueur qui vient de se connecter
     * @return indicateur si l'ajout a �t� fait : Il ne l'est pas si le joueur y
     *         est d�j�
     */
    public boolean addJoueur(JoueurBean aJoueur) {
    	
    	// Suppression des vieux chats
    	if ((lstJoueur.size() == 0) && (lstChat.size() > 0)) {
    		Calendar dateLimite = Calendar.getInstance();
    		dateLimite.add(Calendar.HOUR, -DUREE_VIE_CHAT);
	        synchronized (lstChat) {
	    		ChatBean vieuxChat;
				// Purge autant que n�cessaire
				int nbPurge = -1;
	    		int i = 0;
				do {
	        		vieuxChat = lstChat.get(i);
	    			nbPurge++;
	    			i++;
				}
				while ((i < lstChat.size()) && (vieuxChat.getDate().before(dateLimite)));
				
				// Purge r�ellement
				if (nbPurge > 0) {
	                chatOffset += nbPurge;
	                lstChat.subList(0, nbPurge - 1).clear();
				}
	        }
    	}
        /**
         * Si le joueur est d�j� dans la liste : On le vire
         */
        boolean found = false;
        for (int i = 0; !found && (i < lstJoueur.size()); i++) {
            if (lstJoueur.get(i).equals(aJoueur)) {
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
     * Met � jour la liste des parties suite � la cr�ation d'une partie
     * 
     * @param aPartie
     *            Partie qui vient d'�tre cr��e
     */
    public void addPartie(Partie aPartie) {
    	synchronized (lstPartie) {
    		aPartie.setIdentifiant(++seqPartie);
    		lstPartie.add(PartieBeloteBean
                .getPartieBeloteBean((PartieBelote) aPartie));
    	}
    	// Positionne le score max en fonction du param�trage
    	aPartie.setScoreMaxPartie(getParamAire());
    }

    /**
     * Met � jour la liste des chats suite � un nouveau message
     * 
     * @param aChat Chat qui vient d'�tre cr��e
     * @param dbConnect Connexion base � utiliser
     */
    public void addChat(ChatBean aChat, DBSession dbConnect) {
    	// V�rification par rapport aux mots autoris�s
    	String motsInterdits = getParamAire(ParamBean.CD_PARAM_MOT_INTERDIT_CHAT);
    	boolean chatOk = true;
        if (!StringUtils.isEmpty(motsInterdits)) {
        	StringTokenizer token = new StringTokenizer(motsInterdits, ",");
        	while (chatOk && token.hasMoreElements()) {
				String chainePattern = (String) token.nextElement();
				Pattern pattern = cachePatternMotInterdit.get(chainePattern);
				try {
					if (pattern == null) {
						pattern = Pattern.compile(chainePattern, Pattern.CASE_INSENSITIVE);
						cachePatternMotInterdit.put(chainePattern, pattern);
					}
					chatOk = !(pattern.matcher(aChat.getText()).find());
				} catch (PatternSyntaxException e) {
					// Ignore l'expression
		            System.err.println("addChat : Erreur de syntaxe sur le mot interdit " + chainePattern);
				}
			}
        }

        if (chatOk) {
	        /**
	         * Bloc de maj des chats
	         */
	        synchronized (lstChat) {
	            // Num�rote le chat en absolu
	            aChat.setId(lstChat.size() + chatOffset);
	            lstChat.add(aChat);
	        }
	
	        // Sauvegarde en base si besoin
	        if ((dbConnect != null) && (getParamAire(ParamBean.CD_PARAM_SAVE_CHAT) != null) && (getParamAire(ParamBean.CD_PARAM_SAVE_CHAT).equals("O"))) {
	        	try {
					aChat.create(dbConnect);
				} catch (SQLException e) {
		            System.err.println("addChat : Sauvegarde du chat en erreur : " + e);
				} catch (FctlException e) {
		            System.err.println("addChat : Sauvegarde du chat en erreur : " + e);
				}
	        }
	        // Limite le nombre
	        purgeChat();
        }
    }

    /**
     * Un joueur se l�ve d'une partie
     * 
     * @param aPartie
     *            Partie d'ou il sort
     * @param aJoueur
     *            Joueur qui sort
     * @return indicateur si la suppression de la salle a �t� faite
     */
    public boolean joueurSeLeve(Partie aPartie, JoueurBean aJoueur) {

        boolean removed = false;
        if (aPartie.joueurSeLeve(aJoueur)) {

            // V�rification si la partie est vide
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
     *            Position � la table
     * @return indicateur si l'ajout dans la salle a �t� faite
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
     * @return indicateur si l'ajout dans la salle a �t� faite
     */
    public boolean joueurRejoint(Partie aPartie, JoueurBean aJoueur) {

        return aPartie.joueurRejoint(aJoueur);
    }

    /**
     * @return pseudo joueur pour les messages syst�mes par exemple
     */
    public Joueur getSystem() {
        return Joueur.getSystem();
    }

    /**
     * @return Liste des joueurs en double
     */
    public Vector<JoueurBean> getLstJoueurDouble() {
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
     * @return URL de redirection au cas de gros p�pin
     */
    public URL getDefaultRedirect() {
        return defaultRedirect;
    }

    /**
     * @param url URL de redirection au cas de gros p�pin
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
     *            Num�ro de la partie
     */
    public void finPartie(GameSession aSession, DBSession dbConnect, int i) {

        PartieBean thePartie = lstPartie.get(i);

        // D�j� finie ?
        if (thePartie.getDtFin() == null) {
            // C'est vraiment la fin
            thePartie.setDtFin(Calendar.getInstance());

            try {
                lstPartie.get(i).create(dbConnect);
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
     * Une partie red�marre : Mets � jour les �l�ments
     * 
     * @param i
     *            Num�ro de la partie
     */
    public void restartPartie(int i) {
        PartieBean thePartie = lstPartie.get(i);

        thePartie.setDtDebut(Calendar.getInstance());
        thePartie.setDtFin(null);
    }

    /**
     * Informe le site parent de la fin de la partie
     * 
     * @param mySession
     *            Session � utiliser
     * @param aPartie
     *            Partie concern�e
     */
    public void informeSite(GameSession mySession, Partie aPartie) {
        boolean erreur = false;
        String url = "";
        URL curURL = null;
        try {
            url = mySession.getFinURL()
                    + "?jaea="
                    + URLEncoder.encode(aPartie.getDernierParticipant(0).getPseudo(),
                            "UTF8")
                    + "&jaeb="
                    + URLEncoder.encode(aPartie.getDernierParticipant(1).getPseudo(),
                            "UTF8")
                    + "&jbea="
                    + URLEncoder.encode(aPartie.getDernierParticipant(2).getPseudo(),
                            "UTF8")
                    + "&jbeb="
                    + URLEncoder.encode(aPartie.getDernierParticipant(3).getPseudo(),
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
                // Pas de cache : Sinon ca sert � rien
                aCon.setUseCaches(false);
                aCon.connect();
                if (aCon.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream restauStream = aCon.getInputStream();
                    // Rien � lire : C'est juste pour �tre sur d'avoir tout
                    // charg�
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
     *            Partie associ� au bienvenue
     */
    public void sayHello(JoueurBean aJoueur, Partie aPartie) {
        // Un petit chat d'avertissement
        ChatBean hiChat = new ChatBean();
        hiChat.setJoueurOrig(getSystem());
        hiChat.setStyle("<font color=\"green\"><b>");
        String msg = "Bonjour " + aJoueur.getPseudo() + " ";
        if (aPartie == null) {
	        if (lstJoueur.size() >= 4) {
	        	msg += (lstJoueur.size()-1) + " joueurs vous attendent pour jouer.";
	        }
	        else {
	        	msg += " Merci de patienter pour que d'autres joueurs puissent vous rejoindre et jouer avec vous.";
	        }
        }
        hiChat.setText(msg);
        hiChat.setPartie(aPartie);
        addChat(hiChat, null);
    }

    /**
     * Envoi un chat d'aure voir
     * 
     * @param aJoueur
     *            Joueur qui parle
     * @param aPartie
     *            Partie associ� � l'au revoir
     */
    public void sayBye(JoueurBean aJoueur, Partie aPartie) {
        // Un petit chat d'avertissement
        ChatBean byeChat = new ChatBean();
        byeChat.setJoueurOrig(getSystem());
        byeChat.setStyle("<font color=\"red\"><b>");
        byeChat.setText("Au revoir " + aJoueur.getPseudo());
        byeChat.setPartie(aPartie);
        addChat(byeChat, null);
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