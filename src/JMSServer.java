

import javax.jms.*;
import javax.naming.NamingException;
import java.util.ArrayList;

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
                manager.onMessage(clientMessage);
            } catch (JMSException e) {
                System.err.println("[ERROR] Failed to receive message: " + e);
            }
        }
    }

    void send(ServerMessage m) {
        try {
            Message message = createMessage(m);
            ArrayList<User> users = m.getUsers();
            for (int i = 0; i != users.size(); ++i) {
                message.setIntProperty("Receiver" + i, users.get(i).getID());
                System.out.println("Receiver" + i + " " + message.getIntProperty("Receiver" + i));
            }
            for (int i = 3; i != users.size() - 1; --i) {
                message.setIntProperty("Receiver" + i, -1);
                System.out.println("Receiver" + i + " " + message.getIntProperty("Receiver" + i));
            }
            topicSender.send(message);
        } catch (JMSException e) {
            System.err.println("[ERROR] Failed to broadcast message: " + e);
        }
    }


    void shutdown() {
        running = false;
        close();
    }
}
