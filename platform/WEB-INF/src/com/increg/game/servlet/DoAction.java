/**
 * Created on 8 mai 2003 
 *
 */
package com.increg.game.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.increg.game.bean.ChatBean;
import com.increg.game.bean.GameEnvironment;
import com.increg.game.bean.GameSession;
import com.increg.game.bean.JoueurBean;
import com.increg.game.client.Couleur;
import com.increg.game.client.Partie;
import com.increg.game.client.Tournoi;
import com.increg.game.client.belote.EtatPartieBelote;
import com.increg.game.client.belote.PartieBelote;
import com.increg.game.client.belote.PartieBeloteClassique;
import com.increg.game.client.belote.PartieBeloteModerne;
import com.increg.game.net.ServerCallThread;

/**
 * @author Manu
 *
 * Gère les diverses actions faites sur les clients 
 */
public class DoAction extends ConnectedServlet {

    /// TODO : Codification des actions pour améliorer le débit
    /**
     * @see com.increg.game.servlet.ConnectedServlet#performTask(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void performTask(HttpServletRequest request, HttpServletResponse response) {

        GameEnvironment env = (GameEnvironment) getServletContext().getAttribute("Env");
        HttpSession session = request.getSession();
        GameSession mySession = (GameSession) session.getAttribute("mySession");

        // Dégelle la session
        mySession.setFrozen(false);

        // Restaure le timeout de 5 minutes
        session.setMaxInactiveInterval(60 * 5);
    
        
        try {
            request.setCharacterEncoding("UTF8");
        }
        catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            // Tant pis
        }
        
        // Traitement proprement dit
        String action = request.getParameter("Action");

        if (action.equals("init")) {
            doInit(env, mySession);
            // Indique au Refresh qu'il faut tout envoyer
            request.setAttribute("fullRefresh", "1");
        }
        else if (action.equals("addPartie")) {
            doAddPartie(request, env, mySession);
        }
        else if (action.equals("addChat")) {
            doAddChat(request, env, mySession);
        }
        else if (action.equals("rejointPartie")) {
            doRejointPartie(request, env, mySession);
        }
        else if (action.equals("regardePartie")) {
            doRegardePartie(request, env, mySession);
        }
        else if (action.equals("coupe")) {
            doCoupe(request, env, mySession);
        }
        else if (action.equals("distrib")) {
            doDistrib(request, env, mySession);
        }
        else if (action.equals("decision")) {
            doDecision(request, env, mySession);
        }
        else if (action.equals("joueCarte")) {
            doJoueCarte(request, env, mySession);
        }
        else if (action.equals("ramasseJeu")) {
            doRamasse(request, env, mySession);
        }
        else if (action.equals("scoreVu")) {
            doScoreVu(request, env, mySession);
        }
        else if (action.equals("replay")) {
            doReplay(request, env, mySession);
        }
        else if (action.equals("quittePartie")) {
            doQuittePartie(request, env, mySession);
        }
        else if (action.equals("quit")) {
            doQuit(request, env, mySession);
        }
        else {
            System.err.println("Action non prévue : " + action);
        }

        // En retour : Toujours toutes les informations                
        try {
            if (!action.equals("quit")) {
                // Flag pour ne pas resetter les msg
                request.setAttribute("noRAZ", "1");
                forward(request, response, "/refreshAll.srv");
            }
        }
        catch (ServletException e) {
            // Ignore
        }
        catch (IOException e) {
            // Ignore
        }

    }
    
    /**
     * Quitte l'aire
     * @param request Request Servlet
     * @param env Environnement
     * @param mySession Session
     */
    protected void doQuit(HttpServletRequest request, GameEnvironment env, GameSession mySession) {
        JoueurBean myJoueur = mySession.getMyJoueur();
        if (myJoueur != null) {
            // Dit au revoir..
            env.sortieJoueur(myJoueur);
        }
        request.getSession().invalidate();
    }

    /**
     * Quitte une partie
     * @param request Request Servlet
     * @param env Environnement
     * @param mySession Session
     */
    protected void doQuittePartie(HttpServletRequest request, GameEnvironment env, GameSession mySession) {
        // Un joueur sort d'une partie
        String idPartie = request.getParameter("idPartie");
        
        Partie aPartie = env.getPartieById(Integer.parseInt(idPartie));
        JoueurBean myJoueur = mySession.getMyJoueur();
        
        if (aPartie != null) {
            if (aPartie.joueurVoitPartie(myJoueur)) {
                // Dit au revoir dans la salle
                env.sayBye(myJoueur, aPartie);
            }
            env.joueurSeLeve(aPartie, mySession.getMyJoueur());
            addEvent(env, aPartie);
        }
        else {
            System.err.println("quittePartie : Impossible de retrouver la partie : " + idPartie);
        }
    }

    /**
     * Un joueur veut regarder une partie
     * @param request Request Servlet
     * @param env Environnement
     * @param mySession Session
     */
    protected void doRegardePartie(HttpServletRequest request, GameEnvironment env, GameSession mySession) {
        // Un nouveau spectateur
        String idPartie = request.getParameter("idPartie");
        String mdp = request.getParameter("mdp");
        
        // Un joueur rejoint une partie
        JoueurBean myJoueur = mySession.getMyJoueur();
        
        // Recherche la partie par rapport à son identifiant
        boolean found = false;
        for (int i = 0; !found && (i < env.getLstPartie().size()); i++) {
            if (Integer.toString(env.getPartie(i).getIdentifiant()).equals(idPartie)) {
                // Partie trouvée
                Partie aPartie = env.getPartie(i);
                // Check si privée
                if (aPartie.isPrivate() 
                        && (!aPartie.getMotDePasse().equals(mdp))
                        && (myJoueur.getPrivilege() < JoueurBean.MODERATEUR_PRIVILEGE)) {
                    // Mauvais mot de passe
                    mySession.setMessage("Erreur", "Le mot de passe saisi est invalide.");
                }
                else {
                    // Ok
                    try {
                        found = env.joueurRejoint(env.getPartie(i), myJoueur);
                        addEvent(env, aPartie);
                        
                        // Ajoute le chat correspondant
                        env.sayHello(myJoueur, env.getPartie(i));
                    }
                    catch (NumberFormatException e) {
                        // Pas bon : Ne fait rien
                        mySession.setMessage("Erreur", "regardePartie : Emplacement invalide");
                    }
                }
            }
        }
        if ((!found) 
                && ((mySession.getMessage("Erreur") == null) 
                    || (mySession.getMessage("Erreur").length() == 0))) {
            mySession.setMessage("Erreur", "Impossible de regarder cette partie.");
        }
    }

    /**
     * Un joueur veut rejoindre une partie
     * @param request Request Servlet
     * @param env Environnement
     * @param mySession Session
     */
    protected void doRejointPartie(HttpServletRequest request, GameEnvironment env, GameSession mySession) {
        // Attributs de cette action
        String idPartie = request.getParameter("idPartie");
        String place = request.getParameter("place");
        String mdp = request.getParameter("mdp");
        
        // Un joueur rejoint une partie
        JoueurBean myJoueur = mySession.getMyJoueur();
        
        // Recherche la partie par rapport à son identifiant
        boolean found = false;
        Partie aPartie = null;
        for (int i = 0; !found && (i < env.getLstPartie().size()); i++) {
            if (Integer.toString(env.getPartie(i).getIdentifiant()).equals(idPartie)) {
                // Partie trouvée
                aPartie = env.getPartie(i);
                // Check si privée
                if (aPartie.isPrivate() && (!aPartie.getMotDePasse().equals(mdp))) {
                    // Mauvais mot de passe
                    mySession.setMessage("Erreur", "Le mot de passe saisi est invalide.");
                }
                else {
                    // Ok
                    try {
                        boolean started = aPartie.isStarted();
                        found = env.joueurRejoint(aPartie, myJoueur, Integer.parseInt(place));
                        if (found) {
                            addEvent(env, aPartie);
                        
                            // Ajoute le chat correspondant
                            env.sayHello(myJoueur, aPartie);
                            
                            // La partie vient de démarrer ?
                            if (!started && aPartie.isStarted()) {
                                // Alors reset
                                env.restartPartie(i);
                            }
                        }
                    }
                    catch (NumberFormatException e) {
                        // Pas bon : Ne fait rien
                        mySession.setMessage("Erreur", "rejointPartie : Emplacement invalide");
                    }
                }
            }
        }
        if ((!found) 
                && ((mySession.getMessage("Erreur") == null) 
                    || (mySession.getMessage("Erreur").length() == 0))) {
            mySession.setMessage("Erreur", "Impossible de rejoindre cette partie.");
        }
    }

    /**
     * Un joueur veut ou pas rejouer
     * @param request Request Servlet
     * @param env Environnement
     * @param mySession Session
     */
    protected void doReplay(HttpServletRequest request, GameEnvironment env, GameSession mySession) {
        // Attributs de cette action
        String idPartie = request.getParameter("idPartie");
        String want = request.getParameter("want");
        
        // Un joueur rejoint une partie
        JoueurBean myJoueur = mySession.getMyJoueur();
        
        // Recherche la partie par rapport à son identifiant
        boolean found = false;
        Partie aPartie = null;
        for (int i = 0; !found && (i < env.getLstPartie().size()); i++) {
            if (Integer.toString(env.getPartie(i).getIdentifiant()).equals(idPartie)) {
                // Partie trouvée
                aPartie = env.getPartie(i);
                // On veut peut-être rejouer
                boolean restart = aPartie.impacteVeutRejouer(myJoueur, Boolean.valueOf(want).booleanValue());
                addEvent(env, aPartie);
                if (restart) {
                    env.restartPartie(i);
                }
                found = true;
            }
        }
        if ((!found) 
                && ((mySession.getMessage("Erreur") == null) 
                    || (mySession.getMessage("Erreur").length() == 0))) {
            mySession.setMessage("Erreur", "Impossible de rejouer cette partie.");
        }
    }

    /**
     * Un joueur vient de couper le jeu
     * @param request Request Servlet
     * @param env Environnement
     * @param mySession Session
     */
    protected void doCoupe(HttpServletRequest request, GameEnvironment env, GameSession mySession) {
        // Un nouveau spectateur
        String idPartie = request.getParameter("idPartie");
        String pos = request.getParameter("pos");
        
        JoueurBean myJoueur = mySession.getMyJoueur();

        // Recherche la partie par rapport à son identifiant
        boolean found = false;
        for (int i = 0; !found && (i < env.getLstPartie().size()); i++) {
            if (Integer.toString(env.getPartie(i).getIdentifiant()).equals(idPartie)) {
                // Partie trouvée
                Partie aPartie = env.getPartie(i);
                // Vérification que le joueur joue bien à son tour
                if (aPartie.getParticipant(aPartie.getEtat().getJoueur()).equals(myJoueur)) {
                    // On vient de couper
                    aPartie.impacteCoupe(Integer.parseInt(pos));
                    
                    addEvent(env, aPartie);
                }
                found = true;
            }
        }
        if ((!found) 
                && ((mySession.getMessage("Erreur") == null) 
                    || (mySession.getMessage("Erreur").length() == 0))) {
            mySession.setMessage("Erreur", "Impossible de couper le jeu.");
        }
    }

    /**
     * Un joueur vient de lancer la distribution
     * @param request Request Servlet
     * @param env Environnement
     * @param mySession Session
     */
    protected void doDistrib(HttpServletRequest request, GameEnvironment env, GameSession mySession) {

        String idPartie = request.getParameter("idPartie");
        String nb = request.getParameter("nb");
        
        JoueurBean myJoueur = mySession.getMyJoueur();

        // Recherche la partie par rapport à son identifiant
        boolean found = false;
        for (int i = 0; !found && (i < env.getLstPartie().size()); i++) {
            if (Integer.toString(env.getPartie(i).getIdentifiant()).equals(idPartie)) {
                // Partie trouvée
                PartieBelote aPartie = (PartieBelote) env.getPartie(i);
                // Vérification que le joueur joue bien à son tour
                if (aPartie.getParticipant(aPartie.getEtat().getJoueur()).equals(myJoueur)) {
                    // On vient de donner le nombre pour distribuer
                    aPartie.impacteDistribue(Integer.parseInt(nb));
                    addEvent(env, aPartie);
                }
                found = true;
            }
        }
        if ((!found) 
                && ((mySession.getMessage("Erreur") == null) 
                    || (mySession.getMessage("Erreur").length() == 0))) {
            mySession.setMessage("Erreur", "Impossible de distribuer le jeu.");
        }
    }

    /**
     * Un joueur vient de décider quelquechose
     * @param request Request Servlet
     * @param env Environnement
     * @param mySession Session
     */
    protected void doDecision(HttpServletRequest request, GameEnvironment env, GameSession mySession) {

        String idPartie = request.getParameter("idPartie");
        String couleur = request.getParameter("couleur");
        
        JoueurBean myJoueur = mySession.getMyJoueur();

        // Recherche la partie par rapport à son identifiant
        boolean found = false;
        for (int i = 0; !found && (i < env.getLstPartie().size()); i++) {
            if (Integer.toString(env.getPartie(i).getIdentifiant()).equals(idPartie)) {
                // Partie trouvée
                PartieBelote aPartie = (PartieBelote) env.getPartie(i);

                // Vérification que le joueur joue bien à son tour
                if (aPartie.getParticipant(aPartie.getEtat().getJoueur()).equals(myJoueur)) {
                    // On vient de décider de prendre ou de ne pas prendre
                    if (couleur == null) {
                        aPartie.impactePrise(-1);
                    }
                    else { 
                        aPartie.impactePrise(Integer.parseInt(couleur));
                    } 
                    addEvent(env, aPartie);
                }
                found = true;
            }
        }
        if ((!found) 
                && ((mySession.getMessage("Erreur") == null) 
                    || (mySession.getMessage("Erreur").length() == 0))) {
            mySession.setMessage("Erreur", "Impossible de prendre en compte la décision.");
        }
    }

    /**
     * Un joueur vient de jouer une carte
     * @param request Request Servlet
     * @param env Environnement
     * @param mySession Session
     */
    protected void doJoueCarte(HttpServletRequest request, GameEnvironment env, GameSession mySession) {

        String idPartie = request.getParameter("idPartie");
        String couleur = request.getParameter("c");
        String hauteur = request.getParameter("h");
        
        JoueurBean myJoueur = mySession.getMyJoueur();

        // Recherche la partie par rapport à son identifiant
        boolean found = false;
        for (int i = 0; !found && (i < env.getLstPartie().size()); i++) {
            if (Integer.toString(env.getPartie(i).getIdentifiant()).equals(idPartie)) {
                // Partie trouvée
                PartieBelote aPartie = (PartieBelote) env.getPartie(i);
                
                // Vérification que le joueur joue bien à son tour
                if (aPartie.getParticipant(aPartie.getEtat().getJoueur()).equals(myJoueur)) {

                    long now = Calendar.getInstance().getTimeInMillis();
                    if ((aPartie.getJeu().getTapis().size() == PartieBelote.NB_JOUEUR) 
                        && ((now - mySession.getLastTimeCarteJouee()) < ServerCallThread.POLL_INTERVAL)) {
                        // Trop rapide : Les joueurs ne verront pas las dernière cartes du plis précédent
                        try {
                            Thread.sleep(ServerCallThread.POLL_INTERVAL - (now - mySession.getLastTimeCarteJouee()));
                        }
                        catch (InterruptedException e) {
                            // Ignore l'interruption
                        }                                
                    }
                        
                    // =========== Similaire à SalleModel.setCarteJouee ===============
                    if (aPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_FIN_TOUR) {
                        // Ramasse d'abord
                        aPartie.impacteRamasse();
                    }
                    addEvent(env, aPartie);
                    if (aPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_JOUE_CARTE) {
                        // Il faut jouer la carte
                        Couleur aCarte = aPartie.buildCarte(Integer.parseInt(hauteur), Integer.parseInt(couleur));
                        aPartie.impacteJoueCarte(aCarte);
                        addEvent(env, aPartie);
                        // Mémorise la dernière fois que la carte a été jouée
                        mySession.setLastTimeCarteJouee(now);
                    }
                    found = true;
                }
            }
        }
        if ((!found) 
                && ((mySession.getMessage("Erreur") == null) 
                    || (mySession.getMessage("Erreur").length() == 0))) {
            mySession.setMessage("Erreur", idPartie + "|Impossible de jouer cette carte.");
        }
    }

    /**
     * Un joueur vient de ramasser les cartes
     * @param request Request Servlet
     * @param env Environnement
     * @param mySession Session
     */
    protected void doRamasse(HttpServletRequest request, GameEnvironment env, GameSession mySession) {

        String idPartie = request.getParameter("idPartie");
        
        JoueurBean myJoueur = mySession.getMyJoueur();

        // Recherche la partie par rapport à son identifiant
        boolean found = false;
        for (int i = 0; !found && (i < env.getLstPartie().size()); i++) {
            if (Integer.toString(env.getPartie(i).getIdentifiant()).equals(idPartie)) {
                // Partie trouvée
                PartieBelote aPartie = (PartieBelote) env.getPartie(i);
                
                // Vérification que le joueur joue bien à son tour + Vérif de l'état en cas de doublon
                if ((aPartie.getParticipant(aPartie.getEtat().getJoueur()).equals(myJoueur))
                    && (aPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_FIN_TOUR)) {

                    aPartie.impacteRamasse();
                    
                    addEvent(env, aPartie);
                    found = true;
                }
            }
        }
        if ((!found) 
                && ((mySession.getMessage("Erreur") == null) 
                    || (mySession.getMessage("Erreur").length() == 0))) {
//            mySession.setMessage("Erreur", idPartie + "|Impossible de ramasser les cartes.");
            System.out.println(GameSession.dateToString(Calendar.getInstance()) + " >> " + myJoueur.getPseudo() + " : Impossible de ramasser les cartes (" + idPartie + ")");
            for (int i = 0; (i < env.getLstPartie().size()); i++) {
                if (Integer.toString(env.getPartie(i).getIdentifiant()).equals(idPartie)) {
                    // Partie trouvée
                    PartieBelote aPartie = (PartieBelote) env.getPartie(i);
                    System.out.println(GameSession.dateToString(Calendar.getInstance()) + " >> Partie Trouvée Etat=" + aPartie.getEtat().getEtat());
                }
            }
        }
    }

    /**
     * Un joueur vient de voir les scores
     * @param request Request Servlet
     * @param env Environnement
     * @param mySession Session
     */
    protected void doScoreVu(HttpServletRequest request, GameEnvironment env, GameSession mySession) {

        String idPartie = request.getParameter("idPartie");
        
        // Recherche la partie par rapport à son identifiant
        boolean found = false;
        for (int i = 0; !found && (i < env.getLstPartie().size()); i++) {
            if (Integer.toString(env.getPartie(i).getIdentifiant()).equals(idPartie)) {
                // Partie trouvée
                PartieBelote aPartie = (PartieBelote) env.getPartie(i);

                // Ne fait l'action que pour un joueur : Le premier
                // Limite les accès concurents => Permet de n'enregistrer qu'une fois
                synchronized (aPartie) {
                    if (aPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_FIN_DONNE) {
                        aPartie.impacteScoreVu();
                        addEvent(env, aPartie);
                    }
                    
                    if (aPartie.getEtat().getEtat() == EtatPartieBelote.ETAT_FIN_PARTIE) {
                        // Création de la partie en base de données
                        // & Averti le site pour le classement
                        env.finPartie(mySession, i);
                    }
                }

                found = true;
            }
        }
        if ((!found) 
                && ((mySession.getMessage("Erreur") == null) 
                    || (mySession.getMessage("Erreur").length() == 0))) {
            mySession.setMessage("Erreur", "Impossible de valider le score.");
        }
    }

    /**
     * Quelqu'un parle
     * @param request Request Servlet
     * @param env Environnement
     * @param mySession Session
     */
    protected void doAddChat(HttpServletRequest request, GameEnvironment env, GameSession mySession) {
        // Message de chat
        
        // Récupère les param spécifiques
        String pseudo = request.getParameter("Pseudo");
        String to = request.getParameter("To");
        String msg = request.getParameter("Msg");
        String idPartie = request.getParameter("Partie");
        String couleur = request.getParameter("Couleur");
        
        ChatBean aChat = new ChatBean();
        aChat.setText(msg);
        
        // TODO Optimisation : Recherche en mémoire plutot qu'en base. Base nécessaire si déconnexion, mais en second temps seulement
        aChat.setJoueurOrig(JoueurBean.getJoueurBeanFromPseudo(mySession.getMyDBSession(), pseudo));
        if (idPartie != null) {
            // Recherche la partie dans la liste des parties
            for (int j = 0; j < env.getLstPartie().size(); j++) {
                Partie aPartie = env.getPartie(j); 
                if (aPartie.getIdentifiant() == Integer.parseInt(idPartie)) {
                    aChat.setPartie(aPartie);
                }
            }
        }
        if (to != null) {
            aChat.setJoueurDest(JoueurBean.getJoueurBeanFromPseudo(mySession.getMyDBSession(), to));
            /**
             * Vérification que le joueur n'est pas dans la même partie que l'origine
             * Sinon : Rend le message visible par tous
             */
            boolean ok = true;
            for (int j = 0; j < env.getLstPartie().size(); j++) {
                Partie aPartie = env.getPartie(j);
                
                if ((aPartie.joueurVoitPartie(aChat.getJoueurDest()))
                    && (aPartie.joueurVoitPartie(aChat.getJoueurOrig()))) {
                    // Ils voient la même partie (Participant ou spectateurs)
                    ok = false; 
                }
            }
            
            if (!ok) {
                aChat.setJoueurDest(null);
            }
        }
        // Ne positionne la couleur que pour qq chose de différent du noir par défaut
        if (!couleur.equals("#000000")) {
            aChat.setStyle("<font color=\"" + couleur + "\">");
        }
        env.addChat(aChat, mySession.getMyDBSession());
        
        // Sauvegarde la couleur du joueur
        String oldCouleur = mySession.getMyJoueur().getCouleur();
        mySession.getMyJoueur().setCouleur(couleur);
        try {
            // Ne sauvegarde qu'en cas de changement
            if ((oldCouleur == null) || !oldCouleur.equals(couleur)) {
                mySession.getMyJoueur().maj(mySession.getMyDBSession());
            }
        }
        catch (Exception e) {
            System.err.println("doAddChat : Mise à jour du joueur en erreur : " + e);
        }
    }

    /**
     * Création d'une partie
     * @param request Request Servlet
     * @param env Environnement
     * @param mySession Session
     */
    protected void doAddPartie(HttpServletRequest request, GameEnvironment env, GameSession mySession) {
        // Création d'une partie
        
        // Récupère les param spécifiques
        String pseudo = request.getParameter("Pseudo");
        String titre = request.getParameter("Titre");
        String type = request.getParameter("Type");
        String annonce = request.getParameter("Annonce");
        String motDePasse = request.getParameter("MDP");
        String idTournoi = request.getParameter("idTournoi");
        
        PartieBelote aPartie;
        if (type.equals("C")) {
            aPartie = new PartieBeloteClassique();
        }
        else {
            aPartie = new PartieBeloteModerne();
        }
        aPartie.setOwner(JoueurBean.getJoueurBeanFromPseudo(mySession.getMyDBSession(), pseudo));
        aPartie.setTitre(titre);
        aPartie.setMotDePasse(motDePasse);
        aPartie.setAnnonce(annonce.equals("O"));
        aPartie.addParticipant(aPartie.getOwner(), 0);
        if ((idTournoi != null) && (Integer.parseInt(idTournoi) > 0)) {
            Tournoi aTournoi = new Tournoi();
            aTournoi.setIdentifiant(Integer.parseInt(idTournoi));
            aPartie.setMyTournoi(aTournoi);
        }
        env.addPartie(aPartie);
    }

    /**
     * Initialise l'environnement
     * @param env Environnement
     * @param mySession Session
     */
    protected void doInit(GameEnvironment env, GameSession mySession) {
        // Enregistre le joueur dans les joueurs présents
        JoueurBean myJoueur = mySession.getMyJoueur();

        // Ne fait l'init que si le joueur n'est pas déjà validé : 
        // Ceci évite d'ajouter à tort des joueurs dans la liste des doublons        
        if (!mySession.isValide()) {
            // Ajoute le joueur
            if (env.addJoueur(myJoueur)) {
                // Dit bonjour... si il n'était pas déjà là
                env.sayHello(myJoueur);
                // Si le joueur existe déjà : 
                // Il sera soit déconnecté au bout du timeout, soit à la prochaine requête (cf RefreshAll)

            }
            // Indique que la session est valide
            mySession.setValide(true);
        }
    }

    /**
     * Ajoute un évenement dans le déroulement de la partie si besoin
     * @param env Environnement
     * @param aPartie Partie concernée
     */
    private void addEvent(GameEnvironment env, Partie aPartie) {
        // Crée l'événement
        if (aPartie.getCurrentEvent().length() > 0) {
            // Découpe si plusieurs messages
            int posD = 0;
            int posF;
            do {
                posF = aPartie.getCurrentEvent().indexOf("\n", posD);
                ChatBean aChat = new ChatBean();
                if (posF == -1) {
                    aChat.setText(aPartie.getCurrentEvent().substring(posD));
                }
                else {
                    aChat.setText(aPartie.getCurrentEvent().substring(posD, posF));
                }
                aChat.setPartie(aPartie);
                env.addChat(aChat, null);

                posD = posF + 1;
            }
            while (posF != -1);
            
            aPartie.resetCurrentEvent();
        }
    }
}
