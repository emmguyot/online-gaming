<%@ page import="java.io.File, java.util.Date, java.text.SimpleDateFormat" %>
<jsp:useBean id="mySession" scope="session" class="com.increg.game.bean.GameSession" />
<jsp:useBean id="Env" scope="application" class="com.increg.game.bean.GameEnvironment" />
<html>
<head>
<title>Parties de Belote en cours</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="Expires" content="">
<link rel="stylesheet" href="style/game.css" type="text/css">
</head>
<body style='{ margin: 0; padding:0; background-color: #FFFFFF; color: #000000}'><%
    /**
     * Initialisation de la taille de l applet
     */
    int width = 750;
    int height = 562;

    if ((mySession == null) || (mySession.getMyJoueur() == null)) {
        // Déconnecté
        response.sendRedirect(Env.getDefaultRedirect().toExternalForm());
        return;
    }

	// Timeout de 30 minutes pour permettre les longs chargements
	session.setMaxInactiveInterval(60 * 30);
	
    /**
     * Constitue la version
     */
    String jar[] = {"applets/aire.jar", "applets/aire_conf.jar", "applets/aire_res.jar"};
    String version[] = new String[jar.length];
    SimpleDateFormat format = new SimpleDateFormat("yy.MM.dd.HHmm");
    for (int i = 0; i < jar.length; i++) {
        String path = getServletContext().getRealPath("/" + jar[i]);
        File aFile = new File(path);
        Date aDate = new Date(aFile.lastModified());
        version[i] = format.format(aDate);
    }
%>
<script type="text/javascript" language="JavaScript"><!--
var ns4up = (document.layers) ? 1 : 0;  // browser sniffer
var ns6up = (document.getElementById) ? 1 : 0;
var ie4up = (document.all) ? 1 : 0;
if (ie4up) {
    iWidth = document.body.clientWidth;
    iHeight = document.body.clientHeight; 
    iWidth = <%= width %> - iWidth;
    iHeight = <%= height %> - iHeight;
    window.resizeBy(iWidth, iHeight); 
}
else if (ns4up || ns6up) {
    self.innerHeight = <%= height %>;
    self.innerWidth = <%= width %>;
}
else {
    self.resizeTo(<%= width + 10 %>, <%= height + 28 %>); 
}
// --></script> 
<table cellpadding="0" cellspacing="0" border="0" width="100%" height="100%"><tr><td valign="middle" align="center"><applet code="com.increg.game.ui.AireMain" width="<%= width %>" height="<%= height %>" >
    <param name="cache_archive" value="<% for (int i = 0 ; i < jar.length; i++) { if (i>0) { out.write(','); } out.write(jar[i]); } %>">
    <param name="cache_version" value="<% for (int i = 0 ; i < version.length; i++) { if (i>0) { out.write(','); } out.write(version[i]); } %>">
    <param name="pseudo" value="<%= mySession.getMyJoueur().getPseudo() %>" >
    <param name="avatar" value="<%= mySession.getMyJoueur().getAvatarHautePerf() %>" >
    <param name="avatarLow" value="<%= mySession.getMyJoueur().getAvatarFaiblePerf() %>" >
    <param name="couleur" value="<%= mySession.getMyJoueur().getCouleur() %>" >
    <param name="config" value="/conf/belote.xml" >
    <param name="sessionId" value="<%= session.getId() %>">
    <param name="progressbar" value="true">
    <param name="boxbgcolor" value="white">
    <param name="boxfgcolor" value="black">
    <param name="progresscolor" value="orange">
    Votre navigateur n'accepte pas les Applets Java, veuillez changer sa configuration pour accéder à l'aire de jeu.
</applet></td></tr></table></body></html>
