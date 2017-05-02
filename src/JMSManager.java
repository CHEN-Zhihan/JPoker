import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Created by zhihan on 5/3/17.
 */


public class JMSManager {
    private String host;
    private int port;
    private Context context;
    private ConnectionFactory factory;
    private Queue queue;
    private Connection connection;
    private Session session;
    private MessageProducer queueSender;

    public JMSManager(String host, int port) throws NamingException, JMSException {
        System.setProperty("org.omg.CORBA.ORBInitialHost", host);
        System.setProperty("org.omg.CORBA.ORBInitialPort", Integer.toString(port));
        try {
            context = new InitialContext();
            factory = (ConnectionFactory)context.lookup("jms/TestConnectionFactory");
            queue = (Queue)context.lookup("jms/TestQueue");
        } catch (NamingException e) {
            System.err.println(e);
            throw e;
        }
        try {
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            queueSender = session.createProducer(queue);
        } catch (JMSException e) {
            System.err.println(e);
            throw e;
        }

    }

    private void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException e) {
                ;
            }
        }
    }
}
