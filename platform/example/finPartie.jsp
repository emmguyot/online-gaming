<%
	/**
	 *	[fr] Gestion des scores
	 *	[en] Score management
	 *
	 *	[fr] Cette page est sollicit�e par l'aire � la fin de chaque partie pour informer le site princpal du score
	 *	Cette page ou son �quivalent en PHP, ASP, Perl, ... doit �tre plac�e sur votre site o� les joueurs s'enregistrent, s'identifient, ...
	 *	La source de cette page pr�sente les param�tres fournis.
	 *  Cette page est r�f�renc�e dans le config.properties sous le nom <b>finURL</b>
	 *	
	 *
	 *	[en] This page is asked by the game platform at each end of a game play
	 *	This page or an quite same one in PHP, ASP, Perl, ... should be placed in your web site where players register, log in, ...
	 *	The source of this page show the parameters provided
	 *  This page is referenced in the config.properties with the name <b>finURL</b>
	 *	
	 *	
	 *	Exemple d'appel / Call example :
	 *		http://www.yourMainSite.com/whateverYouWant/finPartie.jsp?jaea=joe&jaeb=joe2&jbea=joe3&jbeb=joe4&sca=1040&sca=960&type=C&tournoi=0
	 */

	// Parameters are encoding in UTF8
    try {
        request.setCharacterEncoding("UTF8");
    } catch (java.io.UnsupportedEncodingException e1) {
    	// Too bad
        e1.printStackTrace();
    }

	// Score value
	String scoreEquipeA = request.getParameter("sca");
	String scoreEquipeB = request.getParameter("scb");

	// players pseudo of each team
	String[] equipeA = new String[2];
	String[] equipeB = new String[2];
	equipeA[0] = request.getParameter("jaea");
	equipeA[1] = request.getParameter("jbea");
	equipeB[0] = request.getParameter("jaeb");
	equipeB[1] = request.getParameter("jbeb");

	// Kind of game : Values : C (classical), M (modern), CA (classical with announce), MA (modern with announce)
	String type = request.getParameter("type");
	// The game is part of a tournament ? 0 = No, 1 = Yes
	String tournoi = request.getParameter("tournoi");
	
	
	// Store scores, compute hall of champs ...
	// TODO ...
%>