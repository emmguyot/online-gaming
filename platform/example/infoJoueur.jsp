<%@ page import="java.net.URLEncoder, java.io.UnsupportedEncodingException, java.text.SimpleDateFormat, java.util.Calendar,
				java.security.MessageDigest, java.security.NoSuchAlgorithmException" %>
<%
	/**
	 *	[fr] Contrôle et fourniture des infos du joueur
	 *	[en] Control and provide information about the player
	 *
	 *	[fr] Cette page est sollicitée par l'aire afin d'obtenir le complément d'information sur le joueur 
	 *	et au passage vérifier que le joueur est habilité à rentrer sur l'aire.
	 *	Cette page ou son équivalent en PHP, ASP, Perl, ... doit être placée sur votre site où les joueurs s'enregistrent, s'identifient, ...
	 *	La source de cette page présente le programme à implémenter pour contrôler et fournir les informations.
	 *  Cette page est référencée dans le config.properties sous le nom <b>baseURL</b>
	 *	
	 *
	 *	[en] This page is asked by the game platform to get full information about a player
	 *	and in the meantime to control whether the player is allowed to enter the platform.
	 *	This page or an quite same one in PHP, ASP, Perl, ... should be placed in your web site where players register, log in, ...
	 *	The source of this page show the program to implement to control and provide player information.
	 *  This page is referenced in the config.properties with the name <b>baseURL</b>
	 *	
	 *	
	 *	Exemple d'appel / Call example :
	 *		http://www.yourMainSite.com/whateverYouWant/infoJoueur.jsp?Pseudo=joe&id=0a0b0101010101010101010101010c01
	 */

	/**
	 * Exemple 
	 * Les principes sont : 
	 *   1) Controle de la valeur de id
	 *   2) Recherche des droits du joueur
	 *   3) Formatage des données
	 *
	 * Steps are : 
	 *   1) Control the id value
	 *   2) Search for player rights
	 *   3) Data layout
	 */

	String passPhrase = "PassPhrase extracted from your config.properties"; // Customize here

	// Parameters are encoding in UTF8
    try {
        request.setCharacterEncoding("UTF8");
    } catch (UnsupportedEncodingException e1) {
    	// Too bad
        e1.printStackTrace();
    }

	String playerPseudo = request.getParameter("Pseudo");
    String id = request.getParameter("id");

	// Compute the checksum and compare with the one given : Same as in aire.jsp
	/**
	 * Le principe du calcul est : 
	 *   1) Constituer la chaîne avec pseudo + phraseMotDePasse définie dans le properties + La date du moment au format jjmmaaaa
	 *   2) Extraire la signature MD5 de cette chaîne
	 *   3) Convertir chaque octet de la signature en hexa (toujours sur 2 caractères)
	 *   4) la chaine est la somme des signature hexa 
	 *
	 * The steps to compute are : 
	 *   1) Build a string with pseudo + passPhrase defined in the config.properties + current date with the following format ddmmyyyy
	 *   2) Extract the MD5 checksum
	 *   3) Convert each byte of the checksum to hexo with 2 digits
	 *   4) The string to use is the concatenation of each hexa strings
	 */
	
	// You can check SecurityBean about this
	SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
    String msg = playerPseudo 		// Customize playerPseudo
                + passPhrase 		// Customize here
                + dateFormat.format(Calendar.getInstance().getTime()); 

	// StringBuffer is prefered to String, but String is used to make the code clearer
    String crcCalc = "";
    try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(msg.getBytes());
            
        byte[] tabDigest = md.digest();
        for (int i = 0; i < tabDigest.length; i++) {
            String octet = "0" + Integer.toHexString(tabDigest[i]);
             
            // 2 digits only with leading 0
            crcCalc = crcCalc + octet.substring(octet.length() - 2);
        }
    }
    catch (NoSuchAlgorithmException e) {
    	// KO : pb with MD5
        e.printStackTrace();
    }
    

	if (!crcCalc.equals(id)) {
		// Fake id : get out!!!
		response.sendError(java.net.HttpURLConnection.HTTP_INTERNAL_ERROR);
		return;
	}


	// Search in your database if playerPseudo exists and get data
	
	// TODO Implement here your code
	
	boolean playerOk = true || false; // The player has been found ?
	// Low is static image / The normal is animated. They may be the same
	String avatarURL = "http://whatYouveFoundInDatabase/../.gif"; // Or jpg
	String avatarLow = "http://whatYouveFoundInDatabase/../.gif"; // Or jpg
	String profil = "..."; // possible values : complet, moderateur, autre
	
	
	if (!playerOk) {
		// Fake user : get out!!!
		response.sendError(java.net.HttpURLConnection.HTTP_INTERNAL_ERROR);
		return;
	}


	// Data layout
	out.print("<joueur pseudo=\"" + URLEncoder.encode(playerPseudo, "UTF8") 
		+ "\" avatar=\"" + avatarURL + "\" avatarLow=\"" + avatarLow 
		+ "\" profil=\"" + profil + "\" />");
%>