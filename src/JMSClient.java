import javax.jms.*;
import javax.naming.NamingException;

/**
 * Created by zhihan on 5/3/17.
 */
class JMSClient extends JMSManager implements MessageListener{
    private MessageProducer queueSender;
    private Client c;
    JMSClient(String ip, int port, int id, Client c) throws NamingException, JMSException{
        super(ip, port);
        try {
            queueSender = session.createProducer(queue);
        } catch (JMSException e) {
            System.err.println("Failed sending to topic: " + e);
            throw e;
        }
        this.c = c;
        String selector = "Receiver0 = " + id + " OR Receiver1 = " + id + " OR Receiver2 = " + id + " OR Receiver3 = " + id;
        try {
            MessageConsumer topicReader = session.createConsumer(topic, selector);
            topicReader.setMessageListener(this);
        } catch (JMSException e) {
            System.err.println("Failed reading from topic: " + e);
        }
    }

    public void onMessage(Message m) {
        try {
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
    
    void shutdown() {
    	try {
    		queueSender.close();
    	} catch(JMSException e) {
    		;
    	}
    	close();
    }

}
