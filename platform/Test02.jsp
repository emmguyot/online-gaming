<html>
<body>
<%

    out.println("<p>Test N°2");

	try {
	    com.increg.commun.DBSession myDBSession = new com.increg.commun.DBSession("config");
	
		if (myDBSession != null) {
			out.println("<p>Accès classe base OK");
			out.println("<p>Base = " + myDBSession.getBaseName());
			try {
				myDBSession.open();
				out.println("<p>Open base OK");
				myDBSession.close();
			}
			catch (Exception e) {
				out.println("<p>Exception open base : " + e.toString());

				if ((request.getParameter("base") != null) && (request.getParameter("base").length() > 0)) {
					out.println("<p>Essai 2");
					try {
				        String className = "org.postgresql.Driver";
				        
				        Class.forName(className);
				        java.sql.Connection dbConnect = java.sql.DriverManager.getConnection(
				        	request.getParameter("base"),
				        	request.getParameter("user"),
				        	request.getParameter("pwd"));
						out.println("<p>Open base (Essai 2) OK");
						dbConnect.close();
				    }
				    catch (Exception e2) {
						out.println("<p>Exception open base (Essai 2): " + e2.toString());
	                }
                }
            }
		}
		else {
			out.println("<p>Accès classe base KO");
		}
	}
	catch (Exception e) {
		out.println("<p>Exception classe base : " + e.toString());
	}
%>
<form>
	<p>Base : <input type="text" name="base">
	<p>User : <input type="text" name="user">
	<p>Passwd : <input type="text" name="pwd">
	<p><input type="submit">
</form>
</body>
</html>