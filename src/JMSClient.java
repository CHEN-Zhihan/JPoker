import javax.jms.*;
import javax.naming.NamingException;

/**
 * Created by zhihan on 5/3/17.
 */
public class JMSClient extends JMSManager implements MessageListener{
    int id;
    private MessageProducer queueSender;
    private MessageConsumer topicReader;
    private Client c;
    private String selector;
    JMSClient(String ip, int port, int id, Client c) throws NamingException, JMSException{
        super(ip, port);
        this.id = id;
        try {
            queueSender = session.createProducer(queue);
        } catch (JMSException e) {
            System.err.println("Failed sending to topic: " + e);
            throw e;
        }
        this.c = c;
        selector = "Receiver0 = " + id + " OR Receiver1 = " + id + " OR Receiver2 = " + id + " OR Receiver3 = " + id;
        setTopicReader();
    }
    private void setTopicReader(){
        try {
            System.out.println(selector);
            topicReader = session.createConsumer(topic, selector);
            topicReader.setMessageListener(this);
        } catch (JMSException e) {
            System.err.println("Failed reading from topic: " + e);
        }
    }

    public void onMessage(Message m) {
        try {
            for (int i = 0; i != 4; ++i) {
                System.out.println("Receiver" + i + " " + m.getIntProperty("Receiver" + i));
            }
            ServerMessage serverMessage = (ServerMessage)((ObjectMessage)m).getObject();
            c.onMessage(serverMessage);
        } catch (JMSException e) {
            System.err.println("[ERROR] Error receiving message");
        }
    }

    void sendMessage(ClientMessage m) {
        try {
            queueSender.send(createMessage(m));
        } catch (JMSException e) {
            System.err.println("[ERROR] Error sending message: " + e);
        }
    }

}
