<jsp:useBean id="mySession" scope="session" class="com.increg.game.bean.GameSession" />
<html>
<head>
<title>Parties de Belote en cours</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/game.css" type="text/css">
</head>
<body><%
    /**
     * Initialisation de la taille de l'applet
     */
    int width = 734;
    int height = 550;

    session.setMaxInactiveInterval(3600);
%>
Session = <%= session.getId() %>
</body>
</html>
