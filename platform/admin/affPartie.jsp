<%@ page import="com.increg.game.bean.GameSession,
                com.increg.game.bean.JoueurBean,
                com.increg.game.bean.PartieBean,
                com.increg.game.client.belote.PartieBelote,
                java.util.Vector, java.util.Iterator
		" %>
<jsp:useBean id="mySession" scope="session" class="com.increg.game.bean.GameSession" />
<%@ taglib uri="../WEB-INF/salon-taglib.tld" prefix="salon" %>
<html>
<head>
<title>Parties jouées</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="Expires" content="">
<link rel="stylesheet" href="../style/game.css" type="text/css">
</head>
<body>
<script type="text/javascript" language="JavaScript"><!--
    self.resizeTo(700, 500); 
// --></script>
<%
    String debut = request.getParameter("Debut");
    String fin = request.getParameter("Fin");

    Vector lstPartie = (Vector) request.getAttribute("lstPartie");
    if (lstPartie == null) {
        lstPartie = new Vector();
    }
%>
    <h1>Parties jouées du <salon:valeur valeur="<%= debut %>" valeurNulle="null">%% au</salon:valeur> 
        <salon:valeur valeur="<%= fin %>" valeurNulle="null">%%</salon:valeur></h1>
        <p><%= lstPartie.size() %> parties effectuées.</p>
        <table width="100%" border=1>
        <tr>
            <th>Type de partie</th>
            <th>Débutée le</th>
            <th>Joueurs</th>
            <th>Scores</th>
        </tr>
<%
        Iterator partieIter = lstPartie.iterator();
        while (partieIter.hasNext()) {
            PartieBean aPartieBean = (PartieBean) partieIter.next();
            PartieBelote aPartie = (PartieBelote) aPartieBean.getMyPartie();
%>
            <tr>
                <td><%= aPartie.toString() %></td>
                <td><salon:valeur valeur="<%= (aPartieBean.getDtDebut() != null) ? aPartieBean.getDtDebut() : null %>" valeurNulle="null" format="dd/MM/yyyy HH:mm">%%</salon:valeur></td>
                <td><%= aPartie.getParticipant(0).getPseudo() %> et <%= aPartie.getParticipant(2).getPseudo() %> contre<br><%= aPartie.getParticipant(1).getPseudo() %> et <%= aPartie.getParticipant(3).getPseudo() %></td>
                <td><%= aPartie.getScoreTotal(0) %><br><%= aPartie.getScoreTotal(1) %></td>
            </tr>
<%
        }
%>
        </table>
        <form action="affPartie.srv" method="POST">
        <p>Autre requête :</p>
        <p>Du <input type="text" name="Debut" size="15"> au <input type="text" name="Fin" size="15"> <input type="submit"></p>
        </form>
</body>
</html>
