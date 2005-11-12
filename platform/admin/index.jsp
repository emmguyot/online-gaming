<%
/*
 * Page assurant la gestion de l'aire de jeu
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
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
<title>Menu d'administration de l'Aire de Jeu InCrEG</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="Expires" content="">
<link rel="stylesheet" href="../style/game.css" type="text/css">
</head>
<body>
    <h1>Administration de <a href="http://online-gaming.sourceforge.net">l'Aire de Jeu InCrEG</a></h1>
    <html:errors />
    <html:form action="/admin/admin.do" method="POST">
        <html:hidden property="pseudo" />
        <html:hidden property="action" />

        <p>Les différentes actions d'administration de l'aire sont les suivantes :</p>
        <ul type="square">
        <li><a href="javascript:submit('exclure')">Exclure des joueurs</a></li>
        <li><a href="javascript:submit('affPartie')">Voir les parties jouées</a></li>
        <li><a href="javascript:submit('affChat')">Voir les messages chats</a></li>
        <li><a href="javascript:submit('paramAire')">Paramétrer l'aire</a></li>
        <li><a href="javascript:submit('purge')">Purger la base et l'optimiser</a></li>
        </ul>
    </html:form>

    <hr/>
    <p>Tests de l'installation : 
        <ol><li><a href="../test/Test01.jsp">Test Tomcat / Java</a>,</li>
        <li><a href="../test/Test02.jsp">Test base de données</a>,</li>
        <li><a href="../test/Test03.jsp">Test accès externe</a>,</li>
        <li><a href="../test/Test04.jsp">Test envoi mail</a></li>
        </ol>
    </p>

    <script language="javascript">
    function submit(action) {
        document.forms[0].action.value = action;
        document.forms[0].submit();
    }
    </script>
</body>
</html>
