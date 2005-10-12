<%
/*
 * Page affichant la liste des parties disputées
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
<title>Parties jouées</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="Expires" content="">
<link rel="stylesheet" href="../style/game.css" type="text/css">
</head>
<body>
    <html:form action="/admin/lstPartie.do" method="POST" onsubmit="return validateIntervalForm(this);">
    <h1>Parties jouées du <c:out value="${intervalForm.debut}" /> au <c:out value="${intervalForm.fin}" /></h1>
        <p><c:out value="${fn:length(intervalForm.lignes)}" /> parties effectuées.</p>
        <table width="100%" border=1>
        <tr>
            <th>Débutée le</th>
            <th>Type de partie</th>
            <th>1<sup>ère</sup> Equipe</th>
            <th>2<sup>ème</sup> Equipe</th>
            <th>Scores</th>
            <th>Fin à</th>
        </tr>
        <c:forEach var="aPartieBean" items="${intervalForm.lignes}" >
            <tr>
                <td><fmt:formatDate value="${aPartieBean.dtDebut.time}" pattern="dd/MM/yyyy HH:mm" /></td>
                <td><c:out value="${aPartieBean.myPartie}" /></td>
                <td><c:out value="${aPartieBean.myPartie.participant[0].pseudo}" /> et <c:out value="${aPartieBean.myPartie.participant[2].pseudo}" /></td>
                <td><c:out value="${aPartieBean.myPartie.participant[1].pseudo}" /> et <c:out value="${aPartieBean.myPartie.participant[3].pseudo}" /></td>
                <td><c:out value="${aPartieBean.myPartie.currentScore[0]}" /> / <c:out value="${aPartieBean.myPartie.currentScore[1]}" /></td>
                <td><fmt:formatDate value="${aPartieBean.dtFin.time}" pattern="dd/MM/yyyy HH:mm" /></td>
            </tr>
        </c:forEach>
        </table>
        <p>Autre requête :</p>
        <p>Du <html:text property="debut" size="15" /> au <html:text property="fin" size="15"/> <html:submit /></p>
        <p><html:errors /></p>
        <html:javascript formName="intervalForm" staticJavascript="false" />
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
    </script>
</body>
</html>
