<%@ page import="com.increg.game.bean.JoueurBean,
                com.increg.game.bean.PartieBean,
                com.increg.game.client.belote.PartieBelote,
                java.util.Iterator
		" %>
<jsp:useBean id="mySession" scope="session" class="com.increg.game.bean.GameSession" />
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<html>
<head>
<title>Fiche d'un joueur</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="Expires" content="">
<link rel="stylesheet" href="style/game.css" type="text/css">
</head>
<body>
<script type="text/javascript" language="JavaScript"><!--
    self.resizeTo(700, 500); 
// --></script>
<%
    JoueurBean aJoueur = (JoueurBean) request.getAttribute("Joueur");
    Integer nbParties = (Integer) request.getAttribute("nbParties");
    Integer nbGain = (Integer) request.getAttribute("nbGain");

    if (aJoueur != null) {
        // Affiche le joueur
%>
        <h1><%= aJoueur.getPseudo() %> <img src="<%= aJoueur.getAvatar() %>" alt="Avatar"></h1>
        <p>Historique du joueur : <%= nbParties %> parties effectuées dont <%= nbGain %> gagnées</p>
        <table class="tabScore" border=1>
        <tr>
            <th>Type de partie</th>
            <th>Débutée le</th>
            <th>Joueurs</th>
            <th>Scores</th>
        </tr>
<%
        Iterator partieIter = aJoueur.getHistorique().iterator();
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
<%
    }
    else { 
        // Pas de joueur
%>
        <h1>Joueur inconnu</h1>
<% } %>
</body>
</html>
