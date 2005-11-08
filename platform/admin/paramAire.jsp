<%
/*
 * Page affichant la liste des paramètres de l'aire et permettant de les modifier
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
<title>Paramétrage de l'aire</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="Expires" content="">
<link rel="stylesheet" href="../style/game.css" type="text/css">
</head>
<body>
    <html:form action="/admin/paramAire.do" method="POST" onsubmit="return validateParamAireForm(this);">
    <h1>Paramètres de l'aire</h1>
        <table width="100%" border=1>
        <tr>
            <th>Paramètre</th>
            <th>Valeur</th>
            <th></th>
        </tr>
        <c:forEach var="aParam" items="${paramAireForm.lstParam}" >
            <tr>
                <td><c:out value="${aParam.libParam}" /></td>
                <td><c:out value="${aParam.valParam}" /></td>
                <td><a href="javascript:modifier('<c:out value="${aParam.libParam}"/>', '<c:out value="${aParam.cdParam}"/>', '<c:out value="${aParam.valParam}"/>')">Modifier</a></td>
            </tr>
        </c:forEach>
        </table>
        <p>Modification du paramètre :</p>
        <p><html:textarea readonly="true" property="libParam" cols="40" rows="3"/><br/>
        = 
        <html:textarea property="valParam" cols="40" rows="2"/>
        <html:hidden property="cdParam" /> <html:submit /></p>
        <p><html:errors /></p>
        <html:javascript formName="paramAireForm" staticJavascript="false" />
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
        function modifier(libelle, code, valeur) {
            document.forms[0].libParam.value = libelle;
            document.forms[0].cdParam.value = code;
            document.forms[0].valParam.value = valeur;
            document.forms[0].valParam.focus();
        }
    </script>
</body>
</html>
