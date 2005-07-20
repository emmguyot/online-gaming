<%@ page import="java.util.ResourceBundle, java.util.Properties, java.util.Date,
                javax.mail.Session, javax.mail.Message, javax.mail.internet.InternetAddress, javax.mail.Transport,
                javax.mail.internet.AddressException, javax.mail.MessagingException, javax.mail.internet.MimeMessage,
                com.increg.game.bean.GameSession
		" %>
<html>
<body>
<%

    out.println("<p>Test N°4 : Envoi email");

    ResourceBundle res = ResourceBundle.getBundle(GameSession.DEFAULT_CONFIG);
    Properties props = System.getProperties();
    props.put("mail.smtp.host", res.getString("serveurSMTP"));
    Session mailSession = Session.getDefaultInstance(props, null);
    Message msg = new MimeMessage(mailSession);

    try {
        msg.setFrom(new InternetAddress(res.getString("fromEMail")));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress
                .parse(res.getString("webmaster"), false));
        msg.setRecipients(Message.RecipientType.BCC, InternetAddress
                .parse(res.getString("fromEMail"), false));
        msg.setSubject("Aire de Jeu : Test envoi d'email depuis Tomcat");
        msg.setText("Tentative d'envoi d'email.");
        msg.setHeader("X-Mailer", "InCrEG automation");
        msg.setSentDate(new Date());

        out.println("<p>Préparation email OK");
        Transport.send(msg);
        out.println("<p>Envoi email OK");
    } catch (AddressException e1) {
        e1.printStackTrace();
        out.println("<p>Exception : " + e1.toString());
    } catch (MessagingException e1) {
        e1.printStackTrace();
        out.println("<p>Exception : " + e1.toString());
    }
%>
</body>
</html>
