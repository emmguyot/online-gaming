<%@ page import="com.increg.game.bean.JoueurBean,
                com.increg.game.bean.PartieBean,
                java.util.Vector, java.util.Iterator
		" %>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<html>
<head>
<title>Administration de l'Aire de Jeu InCrEG</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="Expires" content="">
<link rel="stylesheet" href="style/game.css" type="text/css">
</head>
<body>
<%
    String pseudo = request.getParameter("Pseudo");

    Vector lstJoueur = (Vector) request.getAttribute("lstJoueur");
    Vector lstJoueurPartie = (Vector) request.getAttribute("lstJoueurPartie");
    Vector lstPartie = (Vector) request.getAttribute("lstPartie");
    Vector lstPartieComp = (Vector) request.getAttribute("lstPartieComp");
    JoueurBean aJoueur = (JoueurBean) request.getAttribute("joueur");
    JoueurBean aJoueurPartie = (JoueurBean) request.getAttribute("joueurPartie");
    PartieBean aPartie = (PartieBean) request.getAttribute("partie");
    PartieBean aPartieComp = (PartieBean) request.getAttribute("partieComp");
%>
    <h1>Administration de l'Aire de Jeu InCrEG</h1>
    <salon:valeur valeurNulle="null" valeur='<%= (String) request.getAttribute("Message") %>'>%%</salon:valeur>
    <form name="fiche" action="admin.srv" method="POST">
        <input type="hidden" name="Pseudo" value="<%= pseudo %>">
        <table border="2"><tr><td colspan=2>Remplir une des deux parties</td></tr><tr><td>
            <p>Joueurs connectés :
                <select name="Joueur" onChange="refresh()">
                    <option value=""></option>
                <%
                for (Iterator i=lstJoueur.iterator(); i.hasNext(); ) {
                    JoueurBean joueurListe = (JoueurBean) i.next();
                %>
                    <salon:valeur valeurNulle="null" valeur="<%= joueurListe.getPseudo() %>" >
                        <option value="%%"
                        <%
                        if ((aJoueur != null) && (joueurListe.equals(aJoueur))) {
                        %>
                            selected
                        <%
                        }
                        %>
                        >%%</option>
                    </salon:valeur>
                <%
                }
                %>
                </select>
            </p>
            <p>Parties visibles par le joueur :
                <select name="Partie">
                    <option value=""></option>
                <%
                for (Iterator i=lstPartie.iterator(); i.hasNext(); ) {
                    PartieBean partieListe = (PartieBean) i.next();
                %>
                    <salon:valeur valeurNulle="null" valeur="<%= partieListe.getMyPartie().getTitre() %>" >
                        <option value="%%"
                        <%
                        if ((aPartie != null) && (partieListe.getMyPartie().equals(aPartie.getMyPartie()))) {
                        %>
                            selected
                        <%
                        }
                        %>
                        >%%</option>
                    </salon:valeur>
                <%
                }
                %>
                </select>
            </p>
        </td><td>
            <p>Parties en cours :
                <select name="PartieComp" onChange="refresh()">
                    <option value=""></option>
                <%
                for (Iterator i=lstPartieComp.iterator(); i.hasNext(); ) {
                    PartieBean partieListe = (PartieBean) i.next();
                %>
                    <salon:valeur valeurNulle="null" valeur="<%= partieListe.getMyPartie().getTitre() %>" >
                        <option value="%%"
                        <%
                        if ((aPartieComp != null) && (partieListe.getMyPartie().equals(aPartieComp.getMyPartie()))) {
                        %>
                            selected
                        <%
                        }
                        %>
                        >%%</option>
                    </salon:valeur>
                <%
                }
                %>
                </select>
            </p>
            <p>Joueurs de la partie :
                <select name="JoueurPartie">
                    <option value=""></option>
                <%
                for (Iterator i=lstJoueurPartie.iterator(); i.hasNext(); ) {
                    JoueurBean joueurListe = (JoueurBean) i.next();
                %>
                    <salon:valeur valeurNulle="null" valeur="<%= joueurListe.getPseudo() %>" >
                        <option value="%%"
                        <%
                        if ((aJoueurPartie != null) && (joueurListe.equals(aJoueurPartie))) {
                        %>
                            selected
                        <%
                        }
                        %>
                        >%%</option>
                    </salon:valeur>
                <%
                }
                %>
                </select>
            </p>
        </td></tr></table>
        <p>Quelle action souhaitez-vous faire ?</p>
        <blockquote>
            <input type="radio" name="Action" value="Refresh" checked> Rafraîchir les listes<br>
            <input type="radio" name="Action" value="ExAire"> Exclure le joueur de l'aire<br>
            <input type="radio" name="Action" value="ExPartie"> Exclure le joueur de la partie sélectionnée
        </blockquote>
        <input type="submit">
    </form>
    <p><a href="affPartie.jsp">Affichage des parties effectuées</a></p>
<script language="Javascript">

function refresh() {
    for (i=0; i <document.fiche.Action.length; i++) {
        document.fiche.Action[i].checked = false;
    }
    document.fiche.Action[0].checked = true;
    document.fiche.submit();
}

</script>
</body>
</html>
