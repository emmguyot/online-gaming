<html>
<body>
<%

    out.println("<p>Test N°3 : Accès externe");

	try {
        java.util.ResourceBundle resConfig = java.util.ResourceBundle.getBundle("config");
        java.net.URL curURL = new java.net.URL(resConfig.getString("baseURL"));
        java.net.HttpURLConnection aCon = (java.net.HttpURLConnection) curURL.openConnection();
		out.println("<p>Ouverture connexion OK");
        aCon.setUseCaches(false);
		out.println("<p>use cache OK");
        aCon.connect();
		out.println("<p>connect OK");

		out.println("<p>responseCode=" + aCon.getResponseCode());
	}
	catch (Exception e) {
		out.println("<p>Exception : " + e.toString());
	}
%>
</body>
</html>