<!-- Example provided by Loneliness. Thanks to him for this contribution -->
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
	should be placed in your web site where players register, log in, ..<p>
	<p>The source of this page show the program to implement to compute the URL that open the game platform.</p>

<?php
global $_SESSION;

// Récupération du login en Session
$login=$_SESSION['login'];

// Pass dans config.properties
$conf_pass = "Your pass phrase";

// URL d'accès a welcomeGame.srv
$url = "http://you.url/welcomeGame.srv";

// Contruction de la clef avec md5()
$key = md5($login.$conf_pass.date("jmY"));

// TODO : encode login

// Affichage du lien pour l'accès à l'aire
echo ("<a href=\"$url?Pseudo=$login&id=$key\">Accès à l'aire / Access to game platform</a>");

?>

</body>
</html>
