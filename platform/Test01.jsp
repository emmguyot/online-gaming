<html>
<body>
<%

    out.println("<p>Hello World");

//    out.println("<p>java.home=" + System.getProperty("java.home"));
//    out.println("<p>java.class.path=" + System.getProperty("java.class.path"));
    out.println("<p>java.specification.version=" + System.getProperty("java.specification.version"));
    out.println("<p>java.specification.vendor=" + System.getProperty("java.specification.vendor"));
    out.println("<p>java.specification.name=" + System.getProperty("java.specification.name"));
    out.println("<p>java.version=" + System.getProperty("java.version"));
    out.println("<p>java.vendor=" + System.getProperty("java.vendor"));

%>
</body>
</html>