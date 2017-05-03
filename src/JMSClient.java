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
    void setTopicReader(int gameID){
        if (topicReader != null) {
            try {
                topicReader.close();
            } catch (JMSException e) {
                ;
            }
        }
        try {
            String s = "GameID  = " + gameID +" OR " + selector;
            System.out.println(s);
            topicReader = session.createConsumer(topic, s);
            topicReader.setMessageListener(this);
        } catch (JMSException e) {
            System.err.println("Failed reading from topic: " + e);
        }
    }
    private void setTopicReader() {
        setTopicReader(-1);
    }

    public void onMessage(Message m) {
        try {
            ServerMessage serverMessage = (ServerMessage)((ObjectMessage)m).getObject();
            if (serverMessage instanceof EndMessage) {
                serverMessage = (EndMessage)serverMessage;
            } else {
                serverMessage = (StartMessage)serverMessage;
            }
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
