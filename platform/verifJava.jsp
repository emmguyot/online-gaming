<jsp:useBean id="Env" scope="application" class="com.increg.game.bean.GameEnvironment" />
<html>
<head>
<title>V�rification de la version de Java</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/game.css" type="text/css">
<meta http-equiv="Expires" content="">
</head>
<body>
<h1>Page de v�rification de la version de Java</h1>

<applet code="VerifJava" width="440" height="60" alt="Votre navigateur n'accepte pas les Applets Java, veuillez changer sa configuration pour acc�der � l'aire de jeu.">
    <param name="sessionId" value="<%= session.getId() %>">
    <param name="urlJVMko" value="<%= Env.getRedirectJVMko().toString() %>">
    Votre navigateur n'accepte pas les Applets Java, veuillez changer sa configuration pour acc�der � l'aire de jeu.
</applet>

<p>Si cette page n'est pas automatiquement remplac�e par l'aire de jeu, cela signifie que votre navigateur ne sais pas afficher les applets Java ou que votre version n'est pas la bonne.</p>
<p>Vous trouverez les �l�ments n�cessaires � la bonne ex�cution de Java <a href="errorJava.html">ici</a>.</p>
<div style="border-color: red; border-width:2px; border-style: solid; padding: 5px">
<p style="margin-top: 20px">
	<b>Attention</b>, vous entrez dans un espace collaboratif. 
	Aussi afin de ne pas p�naliser les autres joueurs, merci d'arr�ter tous les t�l�chargements (e-mule, e-donkey, kazaa, shareaza...) 
	et autres utilisations gourmandes en bande passante.
	Ainsi vous conserverez une bande passante suffisante et garantirez le bon d�roulement des parties.
</p>
<p>
	Les autres joueurs vous en remercient � l'avance.
</p>
</div>
</body>
</html>
