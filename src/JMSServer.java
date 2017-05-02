import com.sun.org.apache.regexp.internal.RE;

import javax.jms.*;
import javax.naming.NamingException;

/**
 * Created by zhihan on 5/3/17.
 */
public class JMSServer extends JMSManager implements Runnable {
    private MessageConsumer queueReader;
    private MessageProducer topicSender;
    private GameManager manager;
    private boolean running = true;
    JMSServer(int port, GameManager manager) throws JMSException, NamingException{
        super("localhost", port);
        try {
            topicSender = session.createProducer(topic);
            queueReader = session.createConsumer(queue);
        } catch (JMSException e) {
            System.err.println("Failed setting up JMSServer " + e);
            throw e;
        }
        this.manager = manager;
    }

    public void run() {
        while (running) {
            try {
                Message m = queueReader.receive();
                ClientMessage clientMessage = (ClientMessage) ((ObjectMessage)m).getObject();
                if (clientMessage instanceof FinishedMessage) {
                    clientMessage = (FinishedMessage)clientMessage;
                } else {
                    clientMessage = (RequestMessage)clientMessage;
                }
                manager.onMessage(clientMessage);
            } catch (JMSException e) {
                System.err.println("[ERROR] Failed to receive message: " + e);
                ;
            }
        }
    }

    void send(ServerMessage m) {
        try {
            topicSender.send(createMessage(m));
        } catch (JMSException e) {
            System.err.println("[ERROR] Failed to broadcast message: " + e);
        }
    }

    void shutdown() {
        running = false;
        close();
    }
}
