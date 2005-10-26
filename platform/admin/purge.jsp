<%
/*
 * Page permettant de gérer la base de données
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
<title>Purge de la base de données</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="Expires" content="">
<link rel="stylesheet" href="../style/game.css" type="text/css">
</head>
<body>
    <h1>Purge de la base de données</h1>
    <html:form action="/admin/purge.do" method="POST" onsubmit="return validatePurgeForm(this);">
        <p><html:errors /></p>
        <p>Compteurs :</p>
        <ul>
        <li><c:out value="${purgeForm.nbPartieTot}" /> parties effectuées.</li>
        <li><c:out value="${purgeForm.nbJoueurs}" /> joueurs enregistrés.</li>
        <li>Dernier joueur non connecté le <fmt:formatDate value="${purgeForm.dtVieuxJoueur}" pattern="dd/MM/yyyy HH:mm" /></li>
        <li>Première partie de l'historique le <fmt:formatDate value="${purgeForm.dtVieillePartie}" pattern="dd/MM/yyyy HH:mm" /></li>
        </ul>

        <p>Purge jusqu'au <html:text property="dtPurge" size="15" /></p>
        <ul>
            <li><a href="javascript:valideEtPurge('purgePartie')">des parties</a></li>
            <li><a href="javascript:valideEtPurge('purgeJoueur')">des joueurs</a></li>
        </ul>
        <p><a href="javascript:goOptim()">Optimisation de la base</a></p>

        <html:javascript formName="purgeForm" staticJavascript="false" />
        <html:hidden property="pseudo" />
        <html:hidden property="action" />
        <p><a href="javascript:goMenu()">Menu d'administration</a></p>
    </html:form>
    <script language="Javascript" src="../js/validation.js" ></script>
    <script language="Javascript">
        function goMenu() {
            document.forms[0].action.value = "menu";
            document.forms[0].submit();
        }
        function valideEtPurge(action) {
            if (document.forms[0].onsubmit()) {
                document.forms[0].action.value = action;
                document.forms[0].submit();
            }
        }
        function goOptim() {
            document.forms[0].action.value = "optimise";
            document.forms[0].submit();
        }
    </script>
</body>
</html>
