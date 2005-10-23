<%
/*
 * Page gérant l'exclusion de joueur de l'aire ou d'une partie
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
<title>Exclusion de joueurs</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="Expires" content="">
<link rel="stylesheet" href="../style/game.css" type="text/css">
</head>
<body>
    <h1>Exclusion de joueurs de l'aire de jeu</h1>
    <html:form action="/admin/exclure.do" method="POST" onsubmit="return validateExclureForm(this);">
        <p><html:errors /></p>
        <table border="2"><tr><td colspan=2>Remplir une des deux parties</td></tr><tr><td>
            <p>Joueurs connectés :
                <html:select property="joueur" onchange="refresh()">
                    <html:option value="" />
                    <html:optionsCollection property="lstJoueur" value="pseudo" label="pseudo" />
                </html:select>
            </p>
            <p>Parties visibles par le joueur :
                <html:select property="partie">
                    <html:option value="" />
                    <html:optionsCollection property="lstPartie" value="myPartie.titre" label="myPartie.titre" />
                </html:select>
            </p>
        </td><td>
            <p>Parties en cours :
                <html:select property="partieComp" onchange="refresh()">
                    <html:option value="" />
                    <html:optionsCollection property="lstPartieComp" value="myPartie.titre" label="myPartie.titre" />
                </html:select>
            </p>
            <p>Joueurs de la partie :
                <html:select property="joueurPartie">
                    <html:option value="" />
                    <html:optionsCollection property="lstJoueurPartie" value="pseudo" label="pseudo" />
                </html:select>
            </p>
        </td></tr></table>
        <p>Quelle action souhaitez-vous faire ?</p>
        <blockquote>
            <a href="javascript:refresh()">Rafraîchir les listes</a><br>
            <a href="javascript:exclureAire()">Exclure le joueur de l'aire</a><br>
            <a href="javascript:exclurePartie()">Exclure le joueur de la partie sélectionnée</a>
        </blockquote>
        <html:javascript formName="intervalForm" staticJavascript="false" />
        <html:hidden property="pseudo" />
        <html:hidden property="action" />
    </html:form>
    <p><a href="javascript:goMenu()">Menu d'administration</a></p>
    <script language="Javascript" src="../js/validation.js" ></script>
    <script language="Javascript">
        function goMenu() {
            document.forms[0].action.value = "menu";
            document.forms[0].submit();
        }

        function refresh() {
            document.forms[0].action.value = "exclure";
            document.forms[0].submit();
        }

        function exclureAire() {
            document.forms[0].action.value = "exAire";
            document.forms[0].submit();
        }

        function exclurePartie() {
            document.forms[0].action.value = "exPartie";
            document.forms[0].submit();
        }

    </script>
</body>
</html>
