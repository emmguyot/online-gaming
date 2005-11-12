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
<body style='{margin: 0; padding:0}'><%
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

function MM_findObj(n, d) { //v4.01
    var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
        d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
    if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
    for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
    if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_showHideLayers() { //v3.0
    var i,p,v,obj,args=MM_showHideLayers.arguments;
    for (i=0; i<(args.length-2); i+=3) if ((obj=MM_findObj(args[i]))!=null) { v=args[i+2];
        if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v='hide')?'hidden':v; }
        obj.visibility=v; }
}

function doClose() {
    // Masque pour FireFox qui refuse la fermeture
    MM_showHideLayers('appletDiv','','hide');
    opener = self;
    self.close();
}

// --></script> 
<div id="appletDiv">
<table cellpadding="0" cellspacing="0" border="0" width="100%" height="100%"><tr><td valign="middle" align="center"><applet code="com.increg.game.ui.AireMain" width="<%= width %>" height="<%= height %>" MAYSCRIPT>
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
</applet></td></tr></table></div></body></html>
