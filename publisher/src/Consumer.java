import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Consumer implements Runnable {
    private final BlockingQueue sharedqueue;
    private final MqttClient client;
    private String str;

    public Consumer(BlockingQueue sharedqueue,MqttClient client) {
        this.sharedqueue = sharedqueue;
       this.client=client;

    }


    @Override
    public void run() {
        int i = 0;

        while(true){


            try {
                    str= (String) sharedqueue.take();
                } catch (InterruptedException ex) {
                   Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
               }

               i++;


                MqttMessage message = new MqttMessage(str.getBytes());
               System.out.println("\n Consumed no: " + i + "  " + message);
                message.setQos(2);
                try {
                    client.publish("NJ app", message.getPayload(), 2, false);

                } catch (MqttException e) {
                    e.printStackTrace();
                }


        }
}}