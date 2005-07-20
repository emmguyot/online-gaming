<%@ page import="com.increg.game.bean.JoueurBean,
                com.increg.game.bean.PartieBean,
                java.util.Vector, java.util.Iterator
		" %>
<%@ taglib uri="../WEB-INF/salon-taglib.tld" prefix="salon" %>
<html>
<head>
<title>Menu d'administration de l'Aire de Jeu InCrEG</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="Expires" content="">
<link rel="stylesheet" href="../style/game.css" type="text/css">
</head>
<body>
<%
    String pseudo = request.getParameter("Pseudo");
%>
    <h1>Administration de <a href="http://online-gaming.sourceforge.net">l'Aire de Jeu InCrEG</a></h1>
    <salon:valeur valeurNulle="null" valeur='<%= (String) request.getAttribute("Message") %>'>%%</salon:valeur>

    <p><form name="admin" action="admin.srv" method="POST">
        <input type="hidden" name="Pseudo" value="<%= pseudo %>">
        <a href="javascript:admin.submit()">Exclure des joueurs</a>
    </form></p>

    <p><form name="affPartie" action="affPartie.srv" method="POST">
        <input type="hidden" name="Pseudo" value="<%= pseudo %>">
        <a href="javascript:affPartie.submit()">Voir les parties jouées</a>
    </form></p>

    <p><form name="paramAire" action="paramAire.srv" method="POST">
        <input type="hidden" name="Pseudo" value="<%= pseudo %>">
        <a href="javascript:paramAire.submit()">Paramétrer l'aire</a>
    </form></p>

    <p>Tester l'installation : 
        <ol><li><a href="../test/Test01.jsp">Test Tomcat / Java</a>,</li>
        <li><a href="../test/Test02.jsp">Test base de données</a>,</li>
        <li><a href="../test/Test03.jsp">Test accès externe</a>,</li>
        <li><a href="../test/Test04.jsp">Test envoi mail</a></li>
    </p>
</body>
</html>
