<%
/*
 * Bean assurant la gestion du catalogue fournisseur (articles fournis par un fournisseur)
 * Copyright (C) 2002-2005 Emmanuel Guyot <See emmguyot on SourceForge> 
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
%>
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

    <p><form name="purge" action="purge.srv" method="POST">
        <input type="hidden" name="Pseudo" value="<%= pseudo %>">
        <a href="javascript:purge.submit()">Purger la base et l'optimiser</a>
    </form></p>

    <hr/>
    <p>Tester l'installation : 
        <ol><li><a href="../test/Test01.jsp">Test Tomcat / Java</a>,</li>
        <li><a href="../test/Test02.jsp">Test base de données</a>,</li>
        <li><a href="../test/Test03.jsp">Test accès externe</a>,</li>
        <li><a href="../test/Test04.jsp">Test envoi mail</a></li>
        </ol>
    </p>
</body>
</html>
