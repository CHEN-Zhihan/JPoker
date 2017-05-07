

import javax.jms.*;
import javax.naming.NamingException;
import java.util.ArrayList;

/**
 * Created by zhihan on 5/3/17.
 */
class JMSServer extends JMSManager implements Runnable {
    private MessageConsumer queueReader;
    private MessageProducer topicSender;
    private GameManager manager;
    private volatile boolean running = true;
    JMSServer(int port, GameManager manager) throws JMSException, NamingException{
        super("localhost", port);
        try {
            topicSender = session.createProducer(topic);
            queueReader = session.createConsumer(queue);
        } catch (JMSException e) {
            System.err.println("Failed setting up JMSServer " + e);
            e.printStackTrace();
            throw e;
        }
        this.manager = manager;
    }

    /**
     * waiting for incoming messages.
     */
    public void run() {
        while (running) {
            try {
                Message m = queueReader.receive();
                ClientMessage clientMessage = (ClientMessage) ((ObjectMessage)m).getObject();
                manager.onMessage(clientMessage);
            } catch (JMSException e) {
                if (running) {
                    System.err.println("[ERROR] Failed to receive message: " + e);
                    e.printStackTrace();
                }
                break;
            } catch (Exception e) {
                if (running) {
                    System.err.println("[ERROR] " + e);
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * create object message, setup property for SELECTOR. send through topicSender.
     * @param m
     */
    void send(ServerMessage m) {
        try {
            Message message = createMessage(m);
            ArrayList<User> users = m.getUsers();
            for (int i = 0; i != users.size(); ++i) {
                message.setIntProperty("Receiver" + i, users.get(i).getID());
            }
            for (int i = 3; i != users.size() - 1; --i) {
                message.setIntProperty("Receiver" + i, -1);
            }
            topicSender.send(message);
        } catch (JMSException e) {
            System.err.println("[ERROR] Failed to broadcast message: " + e);
        }
    }

    /**
     *
     */
    void shutdown() {
        running = false;
        close();
    }
}
