/*
 * Created on 30 avr. 2003
 *
 */
package com.increg.game.servlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.increg.game.bean.ChatBean;
import com.increg.game.bean.GameEnvironment;
import com.increg.game.bean.GameSession;
import com.increg.game.bean.JoueurBean;
import com.increg.game.client.AireMainModel;
import com.increg.game.client.Couleur;
import com.increg.game.client.Joueur;
import com.increg.game.client.belote.AnnonceBelote;
import com.increg.game.client.belote.EtatPartieBelote;
import com.increg.game.client.belote.PartieBelote;
import com.increg.game.client.belote.PartieBeloteClassique;
import com.increg.util.HTMLencoder;

/**
 * @author Manu
 *
 * Retourne les informations depuis la dernière fois 
 * Ceci couvre tous les aspects :
 *      - Liste des joueurs présents
 *      - Liste des parties
 *      - Partie en cours pour le joeur (Participant ou spectateur)
 */
public class RefreshAll extends ConnectedServlet {

    /**
     * @see com.increg.game.servlet.ConnectedServlet#performTask(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void performTask(HttpServletRequest request, HttpServletResponse response) {

        GameEnvironment env = (GameEnvironment) getServletContext().getAttribute("Env");
        GameSession mySession = (GameSession) request.getSession().getAttribute("mySession");

        // Vérification
        if (!env.getLstJoueur().contains(mySession.getMyJoueur())) {
            // Joueur inconnu !
            // Déconnexion immédiate
            request.getSession().invalidate();
            try {
                System.err.println("Utilisation pseudo inconnu (ou exclu) dans la liste >" + mySession.getMyJoueur().getPseudo() + "<");
                response.sendError(501);
            }
            catch (IOException e) {
                // Ignore
            }
            return;
        }
        
        if (env.getLstJoueurDouble().contains(mySession.getMyJoueur())) {
            // Utilisation en double : Vires-en un...
            Iterator<JoueurBean> joueurIter = env.getLstJoueurDouble().iterator();
            while (joueurIter.hasNext()) {
                Joueur aJoueur = joueurIter.next();
                // Test égalité de pointeur
                if (aJoueur == mySession.getMyJoueur()) {
                    // Doublon : Supprimes le ==> C'est fait au détachement de la session
                    // Bye bye
                    request.getSession().invalidate();
                    try {
                        System.err.println("Utilisation pseudo en double >" + mySession.getMyJoueur().getPseudo() + "<");
                        response.sendError(501);
                    }
                    catch (IOException e) {
                        // Ignore
                    }
                    return;
                }
            }
            
        }
                
        // Pas de cache
        response.addHeader("Pragma", "No-cache");
        response.addHeader("Cache-Control", "no-cache");
        response.addDateHeader("Expires", 1);
        // Pas d'entete inutile
        // response.setHeader("Content-Encoding", "gzip");
                
        int lastId = Integer.parseInt(request.getParameter(AireMainModel.URL_LAST_ID));
        if (lastId > 0) {
            // lastId = 0 ==> Ne le connait (pour l'init)
            mySession.setLastChatSeen(lastId);
        }
        else {
            // Positionne l'offset de Chat
            mySession.setLastChatSeen(env.getChatOffset());
        }
        boolean hautePerf = ((request.getParameter(AireMainModel.URL_HAUTE_PERF) != null) 
                            && (request.getParameter(AireMainModel.URL_HAUTE_PERF).equals("t")));


        // Doit-on faire un fullRefresh ?
        boolean fullRefresh = (request.getAttribute("fullRefresh") != null);
                
        StringBuffer fullXml = new StringBuffer();
        StringBuffer subXml = new StringBuffer();
        
        // Format XML
        try {
            // Compression pour gagner en temps de transit
            GZIPOutputStream outOS = new GZIPOutputStream(response.getOutputStream());
            OutputStreamWriter out = new OutputStreamWriter(outOS, "UTF8");
            
            fullXml.append("<").append(AireMainModel.XML_TAG_STATUS);
            if ((mySession.getMessage("Erreur") != null) && (mySession.getMessage("Erreur").length() > 0)) {
                fullXml.append(" ").append(AireMainModel.XML_ATT_ERROR).append("=\"").append(mySession.getMessage("Erreur")).append("\"");
            }
            fullXml.append(">");

            // Les joueurs
            Vector<JoueurBean> lstJoueur = env.getLstJoueur();
            for (int i = 0; i < lstJoueur.size(); i++) {
                Joueur aJoueur = lstJoueur.get(i);
                subXml.append("<").append(AireMainModel.XML_TAG_JOUEUR)
                        .append(" ").append(AireMainModel.XML_ATT_PSEUDO).append("=\"").append(HTMLencoder.htmlEncode(aJoueur.getPseudo())).append("\" ");
                subXml.append(AireMainModel.XML_ATT_AVATAR).append("=\"");
                if (hautePerf) {
                    if (aJoueur.getAvatarHautePerf() != null) {
                    	subXml.append(HTMLencoder.htmlEncode(aJoueur.getAvatarHautePerf().toString()));
                    }
                }
                else {
                	if (aJoueur.getAvatarFaiblePerf() != null) {
                		subXml.append(HTMLencoder.htmlEncode(aJoueur.getAvatarFaiblePerf().toString()));
                	}
                }
                subXml.append("\" ");
                if (aJoueur.getPrivilege() >= Joueur.MODERATEUR_PRIVILEGE) {
                    subXml.append(AireMainModel.XML_ATT_MODERATEUR).append("=\"O\" ");
                }
                subXml.append("/>");
            }
            
            // Les parties
            for (int i = 0; i < env.getLstPartie().size(); i++) {
                PartieBelote aPartie = (PartieBelote) env.getPartie(i);
                
                StringBuffer ligne = new StringBuffer();
                
                /**
                 * Infos que tout le monde voit
                 */
                ligne.append("<").append(AireMainModel.XML_TAG_PARTIE);
                ligne.append(" ").append(AireMainModel.XML_ATT_ID).append("=\"").append(aPartie.getIdentifiant()).append("\" ");
                ligne.append(" ").append(AireMainModel.XML_ATT_STEP).append("=\"").append(aPartie.getStep()).append("\" ");
                if (aPartie.getOwner() != null) { 
                    ligne.append(AireMainModel.XML_ATT_PSEUDO).append("=\"").append(HTMLencoder.htmlEncode(aPartie.getOwner().getPseudo())).append("\" ");
                }
                else {
                    ligne.append(AireMainModel.XML_ATT_PSEUDO).append("=\"\" ");
                }
                ligne.append(AireMainModel.XML_ATT_TITRE).append("=\"").append(HTMLencoder.htmlEncode(aPartie.getTitre())).append("\" ");
                if (aPartie instanceof PartieBeloteClassique) { 
                    ligne.append(AireMainModel.XML_ATT_TYPE).append("=\"C\" ");
                }
                else {
                    ligne.append(AireMainModel.XML_ATT_TYPE).append("=\"M\" ");
                }
                if (aPartie.isAnnonce()) {
                    ligne.append(AireMainModel.XML_ATT_ANNONCE).append("=\"O\" ");
                }
                else {
                    ligne.append(AireMainModel.XML_ATT_ANNONCE).append("=\"N\" ");
                }
                if (aPartie.isPrivate()) {
                    // N'affiche pas le mot de passe : Pas besoin sur le client. Juste X
                    ligne.append(AireMainModel.XML_ATT_MDP).append("=\"X\" ");
                }
                if ((aPartie.getMyTournoi() != null) && (aPartie.getMyTournoi().getIdentifiant() > 0)) {
                    ligne.append(AireMainModel.XML_ATT_TOURNOI).append("=\"").append(aPartie.getMyTournoi().getIdentifiant()).append("\" ");
                }

                
                /**
                 * Infos visibles/nécessaire pour les joueurs et les participants
                 */
                if (aPartie.joueurVoitPartie(mySession.getMyJoueur())) {
                    // Le joueur peut voir, on passe les infos

                    if (aPartie.getPreneur() != -1) {
                        ligne.append(AireMainModel.XML_ATT_PRENEUR).append("=\"").append(aPartie.getPreneur()).append("\" ");
                        ligne.append(AireMainModel.XML_ATT_ATOUT).append("=\"").append(aPartie.getAtout()).append("\" ");
                    }

                    // Etat
                    ligne.append(AireMainModel.XML_ATT_ETAT).append("=\"").append(aPartie.getEtat().getEtat()).append("\" ");
                    // Joueur qui doit faire
                    ligne.append(AireMainModel.XML_ATT_JOUEUR).append("=\"").append(aPartie.getEtat().getJoueur()).append("\" ");
                    // Joueur qui a ouvert
                    ligne.append(AireMainModel.XML_ATT_OUVREUR).append("=\"").append(aPartie.getOuvreur()).append("\" ");
                    // Joueur qui a ramassé en dernier
                    ligne.append(AireMainModel.XML_ATT_RAMASSE).append("=\"").append(aPartie.getDernierRamasse()).append("\" ");
                    // Joueur qui a coupé
                    ligne.append(AireMainModel.XML_ATT_COUPEUR).append("=\"").append(aPartie.getEtat().getJoueurCoupe()).append("\" ");

                    // Fin de l'entete
                    ligne.append(">");
                    
                    /**
                     * Fin 1ère partie Infos spécifiques
                     */
                }
                else {
                    // Fin de l'entete
                    ligne.append(">");
                }

                for (int j = 0; j < PartieBelote.NB_JOUEUR; j++) {
                    Joueur aJoueur = aPartie.getParticipant(j);
                    if (aJoueur == null) {
                        ligne.append("<").append(AireMainModel.XML_TAG_JOUEUR).append(" ").append(AireMainModel.XML_ATT_POS).append("=\"").append(j).append("\" />");
                    }
                    else {
                        ligne.append("<").append(AireMainModel.XML_TAG_JOUEUR).append(" ").append(AireMainModel.XML_ATT_POS).append("=\"")
                                .append(j).append("\" ").append(AireMainModel.XML_ATT_PSEUDO).append("=\"").append(HTMLencoder.htmlEncode(aJoueur.getPseudo())).append("\" />");
                    }
                }

                /**
                 * 2nde partie spécifiques aux joueurs et participants
                 * Car certains éléments nécessitent que les joueurs soient connus avant le bon décodage
                 */
                if (aPartie.joueurVoitPartie(mySession.getMyJoueur())) {
                    // Score
                    for (int j = 0; j < (PartieBelote.NB_JOUEUR / 2); j++) {
                        ligne.append("<").append(AireMainModel.XML_TAG_SCORE).append(" ").append(AireMainModel.XML_ATT_POS).append("=\"").append(j).append("\" ")
                                .append(AireMainModel.XML_ATT_VALEUR).append("=\"").append(aPartie.getScoreTotal(j)).append("\" />");
                    }

                    for (int j = 0; j < aPartie.getSpectateurs().size(); j++) {
                        Joueur aJoueur = aPartie.getSpectateurs(j);
                        ligne.append("<").append(AireMainModel.XML_TAG_SPECTATEUR).append(" ").append(AireMainModel.XML_ATT_PSEUDO).append("=\"").append(HTMLencoder.htmlEncode(aJoueur.getPseudo())).append("\" />");
                    }
                    
                    /**
                     * Infos sur le jeu des joueurs : Ne passe que les cartes du joueur en question
                     */
                    for (int j = 0; j < PartieBelote.NB_JOUEUR; j++) {
                        Joueur aJoueur = aPartie.getParticipant(j);
                        if ((aJoueur != null) && (aJoueur.equals(mySession.getMyJoueur()))) {
                            ligne.append("<").append(AireMainModel.XML_TAG_JEU).append(">");
                            for (int iJeu = 0; iJeu < aPartie.getJeu().getMains(j).size(); iJeu++) {
                                Couleur aCarte = (Couleur) aPartie.getJeu().getMains(j).get(iJeu);
                                ligne.append("<").append(AireMainModel.XML_TAG_CARTE).append(" ").append(AireMainModel.XML_ATT_HAUTEUR).append("=\"").append(aCarte.getHauteur())
                                            .append("\" ").append(AireMainModel.XML_ATT_COULEUR).append("=\"").append(aCarte.getCouleur())
                                            .append("\" />");
                            }
                            ligne.append("</").append(AireMainModel.XML_TAG_JEU).append(">");
                        }
                    }
                    
                    /**
                     * Premiere carte du tas
                     */
                    if (aPartie.getJeu().getTas().size() > 0) {
                        Couleur aCarte = (Couleur) aPartie.getJeu().getTas().get(0);
                        ligne.append("<").append(AireMainModel.XML_TAG_TAS).append("><")
                                    .append(AireMainModel.XML_TAG_CARTE).append(" ").append(AireMainModel.XML_ATT_HAUTEUR).append("=\"").append(aCarte.getHauteur())
                                    .append("\" ").append(AireMainModel.XML_ATT_COULEUR).append("=\"").append(aCarte.getCouleur())
                                    .append("\" /></").append(AireMainModel.XML_TAG_TAS).append(">");
                    }
                    
                    /**
                     * Tapis
                     */
                    ligne.append("<").append(AireMainModel.XML_TAG_TAPIS).append(">");
                    for (int iJeu = 0; iJeu < aPartie.getJeu().getTapis().size(); iJeu++) {
                        Couleur aCarte = (Couleur) aPartie.getJeu().getTapis().get(iJeu);
                        ligne.append("<").append(AireMainModel.XML_TAG_CARTE).append(" ").append(AireMainModel.XML_ATT_HAUTEUR).append("=\"").append(aCarte.getHauteur())
                                    .append("\" ").append(AireMainModel.XML_ATT_COULEUR).append("=\"").append(aCarte.getCouleur())
                                    .append("\" />");
                    }
                    ligne.append("</").append(AireMainModel.XML_TAG_TAPIS).append(">");
                    
                    /**
                     * Plis : Indicateur de présence pour chaque équipe et cartes du dernier plis
                     */
                    ligne.append("<").append(AireMainModel.XML_TAG_PLIS).append(">");
                    int nbPlis = 0;
                    for (int j = 0; j < (PartieBelote.NB_JOUEUR / 2); j++) {
                        if (aPartie.getJeu().getPlis(j).size() > 0) {
                            if ((aPartie.getDernierRamasse() % 2) == j) {
                                for (int iPlis = (aPartie.getJeu().getPlis(j).size() - PartieBelote.NB_JOUEUR);
                                         iPlis < aPartie.getJeu().getPlis(j).size(); 
                                         iPlis++) {
                                    Couleur aCarte = (Couleur) aPartie.getJeu().getPlis(j).get(iPlis);
                                    ligne.append("<").append(AireMainModel.XML_TAG_CARTE).append(" ").append(AireMainModel.XML_ATT_HAUTEUR).append("=\"").append(aCarte.getHauteur())
                                                .append("\" ").append(AireMainModel.XML_ATT_COULEUR).append("=\"").append(aCarte.getCouleur())
                                                .append("\" />");
                                }
                            }

                            ligne.append("<").append(AireMainModel.XML_TAG_PRESENCE).append(" ").append(AireMainModel.XML_ATT_POS).append("=\"").append(j).append("\" />");
                        }
                        nbPlis += (aPartie.getJeu().getPlis(j).size()) / PartieBelote.NB_JOUEUR;
                    }
                    ligne.append("</").append(AireMainModel.XML_TAG_PLIS).append(">");
                    
                    /**
                     * Infos sur les annonces : Au premier tour seulement
                     */
                    if (nbPlis == 1) {
                        for (int j = 0; j < PartieBelote.NB_JOUEUR; j++) {
                            for (int iAnnonce = 0; iAnnonce < aPartie.getJeu().getAnnonces()[j].size(); iAnnonce++) {
                                AnnonceBelote anAnnonce = (AnnonceBelote) aPartie.getJeu().getAnnonces()[j].get(iAnnonce);
                                ligne.append("<").append(AireMainModel.XML_TAG_ANNONCE).append(" ").append(AireMainModel.XML_ATT_JOUEUR).append("=\"").append(j);
                                ligne.append("\" ").append(AireMainModel.XML_ATT_INDICE).append("=\"").append(iAnnonce)
                                            .append("\" ").append(AireMainModel.XML_ATT_TYPE).append("=\"").append(anAnnonce.getType())
                                            .append("\">");
                                for (int iCarte = 0; iCarte < anAnnonce.getCartes().length; iCarte++) {
                                    Couleur aCarte = (Couleur) anAnnonce.getCartes()[iCarte];
                                    if (aCarte != null) {
                                        ligne.append("<").append(AireMainModel.XML_TAG_CARTE).append(" ").append(AireMainModel.XML_ATT_HAUTEUR).append("=\"").append(aCarte.getHauteur())
                                                    .append("\" ").append(AireMainModel.XML_ATT_COULEUR).append("=\"").append(aCarte.getCouleur())
                                                    .append("\" />");
                                    }
                                }
                                ligne.append("</").append(AireMainModel.XML_TAG_ANNONCE).append(">");
                            }
                        }
                    }
                    
                    /**
                     * Score de la donne
                     */
                    if (aPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_FIN_DONNE) {
                        for (int j = 0; j < (PartieBelote.NB_JOUEUR / 2); j++) {
                            ligne.append("<").append(AireMainModel.XML_TAG_SCORE_DONNE).append(" ").append(AireMainModel.XML_ATT_POS).append("=\"").append(j).append("\" ")
                                    .append(AireMainModel.XML_ATT_VALEUR).append("=\"").append(aPartie.getScoreDonne(j)).append("\" />");
                        }
                    }
                }
                
                subXml.append(ligne.append("</").append(AireMainModel.XML_TAG_PARTIE).append(">"));
                
            }
            
            // Fin de la partie stable
            // Optimisation : Si la même chôse que la fois précédent ==> N'envoie rien
            if (subXml.length() == mySession.getLastSubRefresh().length()) {
                // Taille égal : Il faut comparer l'intérieur

                String subXmlString = subXml.toString();
                
                if (!subXmlString.equals(mySession.getLastSubRefresh().toString())) {
                    fullXml.append(subXml);
                    mySession.setLastSubRefresh(subXml);
                }
                else if (mySession.isLastSubRefreshRepeat()) {
                    fullXml.append(subXml);
                    // Ne répette qu'une fois : C'est pour être sur que le message est bien reçu
                    mySession.setLastSubRefreshRepeat(false);
                }
                else {
                    // Sinon ne renvoies qu'une partie indiquant qu'il n'y a pas de changement
                    fullXml.append("<").append(AireMainModel.XML_TAG_CACHE).append("/>");
                }
            }
            else {
                // Taille différente : Ecriture
                fullXml.append(subXml);
                mySession.setLastSubRefresh(subXml);
            }
            
            
            // Le chat
            long lastSentId = 0;
            synchronized (env.getLstChat()) {
                List<ChatBean> lstChat = env.getLstChat(mySession.getLastChatSeen());
                for (int i = 0; i < lstChat.size(); i++) {
                    ChatBean aChat = lstChat.get(i);
                    lastSentId = aChat.getId();
                    
                    // Le chat n'est envoyé que si le joueur n'est pas celui qui est connecté
                    // Ou cas particulier du premier Refresh
                    if ((fullRefresh) 
                            || (aChat.getJoueurOrig() == null) 
                            || (!aChat.getJoueurOrig().equals(mySession.getMyJoueur()))) {

                        StringBuffer ligne = new StringBuffer();

                        ligne.append("<").append(AireMainModel.XML_TAG_CHAT).append(" ")
                                    .append(AireMainModel.XML_ATT_ID).append("=\"").append(aChat.getId()).append("\" ")
                                    .append(AireMainModel.XML_ATT_TEXT).append("=\"").append(HTMLencoder.htmlEncode(aChat.getText())).append("\" ")
                                    .append(AireMainModel.XML_ATT_STYLE).append("=\"").append(HTMLencoder.htmlEncode(aChat.getStyle())).append("\" ");
                            
                        if (aChat.getJoueurOrig() != null) {
                            ligne.append(AireMainModel.XML_ATT_FROM).append("=\"").append(HTMLencoder.htmlEncode(aChat.getJoueurOrig().getPseudo())).append("\" "); 
                        }
                
                        // Filtre les messages secrets
                        boolean sendIt = true;
                        // Controle le destinataire que pour les joueurs sans privilège particulier
                        if (aChat.getJoueurDest() != null) {
                            if ((!mySession.getMyJoueur().equals(aChat.getJoueurDest()))
                                && (!mySession.getMyJoueur().equals(aChat.getJoueurOrig()))
                                && (mySession.getMyJoueur().getPrivilege() < Joueur.MODERATEUR_PRIVILEGE)) {
                                sendIt = false;
                            }
                            else {
                                ligne.append(AireMainModel.XML_ATT_TO).append("=\"").append(HTMLencoder.htmlEncode(aChat.getJoueurDest().getPseudo())).append("\" ");
                            }
                        }
                        if (sendIt && (aChat.getPartie() != null)) {
                            // Par défaut non
                            sendIt = aChat.getPartie().joueurVoitPartie(mySession.getMyJoueur());
                            if (sendIt) {
                                ligne.append(AireMainModel.XML_ATT_PARTIE).append("=\"").append(aChat.getPartie().getIdentifiant()).append("\" ");
                            }
                        }

                        if (sendIt) {                    
                            fullXml.append(ligne.append("/>"));
                            //System.out.println("Envoi à " + mySession.getMyJoueur().getPseudo() + " : " + ligne);
                        }
                        else {
                            //System.out.println("Non Envoi à " + mySession.getMyJoueur().getPseudo() + " : " + ligne);
                        }
                    }
                }
            }
            mySession.setLastChatSeen(lastSentId);
                    
            fullXml.append("</").append(AireMainModel.XML_TAG_STATUS).append(">");

            // Optimisation : Si la même chôse que la fois précédent ==> N'envoie rien
            if (fullXml.length() == mySession.getLastRefresh().length()) {
                // Taille égal : Il faut comparer l'intérieur

                String fullXmlString = fullXml.toString();
                
                if (!fullXmlString.equals(mySession.getLastRefresh().toString())) {
                    out.write(fullXml.toString());
                    mySession.setLastRefresh(fullXml);
                }
                else if (mySession.isLastRefreshRepeat()) {
                    out.write(fullXml.toString());
                    // Ne répette qu'une fois : C'est pour être sur que le message est bien reçu
                    mySession.setLastRefreshRepeat(false);
                }
                // Sinon ne renvoies rien
            }
            else {
                // Taille différente : Ecriture
                out.write(fullXml.toString());
                mySession.setLastRefresh(fullXml);
            }
            
            out.close();
        }
        catch (IOException e) {
            // Pb à l'écriture
            System.err.println("RefreshAll : Erreur d'écriture " + e.toString());
        }

    }

}
