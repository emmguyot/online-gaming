<html>
<head>
	<title>Accès à l'aire / Game platform access</title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body>
	<h1>Accès à l'aire / Game platform access</h1>
	<h2>[fr]</h2>
	<p>Cette page présente un exemple de lancement de l'aire. Cette page ou son équivalent en PHP, ASP, Perl, ... 
	doit être placée sur votre site où les joueurs s'enregistrent, s'identifient, ...<p>
	<p>La source de cette page présente le programme à implémenter pour calculer l'adresse à activer pour ouvrir l'aire.</p>
	
	<h2>[en]</h2>
	<p>This page shows an example to launch the game platform. This page or an quite same one in PHP, ASP, Perl, ...
	should be placed in your web site where players register, log in, ...<p>
	<p>The source of this page show the program to implement to compute the URL that open the game platform.</p>
	
	<%
		// Code Java
		String url = "http://www.game-platform.com/game/"; // Customize with your domain and context
		
		// servlet name : No customization
		url = url + "welcomeGame.srv?";
		
		// Player pseudo / nickname
		url = url + "Pseudo=" + URLEncoder.encode(playerPseudo, "UTF8"); // Customize playerPseudo
		
		// Compute the checksum that prevents unknown or illegal users
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
        
		// Add the string to the URL
		url = url + "&id=" + crcCalc;

		// You're done!!
	%>

	<p><a href="<%= url %>">Accès à l'aire / Access to game platform</a></p>
	<p>Exemple d'URL / URL Example :
			http://www.game-platform.com/game/welcomeGame.srv?Pseudo=joe&id=0a0b0101010101010101010101010c01</p>
	
</body>
</html>
