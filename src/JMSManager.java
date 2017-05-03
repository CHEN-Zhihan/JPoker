import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.Serializable;

/**
 * Created by zhihan on 5/3/17.
*/


class JMSManager {
    Queue queue;
    Topic topic;
    private Connection connection;
    Session session;

    JMSManager(String host, int port) throws NamingException, JMSException {
        System.setProperty("org.omg.CORBA.ORBInitialHost", host);
        System.setProperty("org.omg.CORBA.ORBInitialPort", Integer.toString(port));
        ConnectionFactory factory;
        try {
            Context context = new InitialContext();
            factory = (ConnectionFactory)context.lookup("jms/JPoker24GameConnectionFactory");
            queue = (Queue)context.lookup("jms/JPoker24GameQueue");
            topic = (Topic)context.lookup("jms/JPoker24GameTopic");
        } catch (NamingException e) {
            System.err.println(e);
            throw e;
        }
        try {
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e) {
            System.err.println(e);
            throw e;
        }
    }

    void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException e) {

            }
        }
    }

    ObjectMessage createMessage(Serializable obj) throws JMSException {
        try {
            return session.createObjectMessage(obj);
        } catch (JMSException e) {
            System.err.println("Error preparing message: " + e);
            throw e;
        }
    }
}
