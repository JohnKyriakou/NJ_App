import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Producer implements Runnable {
    private final BlockingQueue sharedqueue;
    private final String[] majclass;
    private int frequency;

    public Producer(BlockingQueue sharedqueue, String[] majclass,int frequency) {
        this.sharedqueue = sharedqueue;
        this.majclass = majclass;
        this.frequency=frequency;
    }


    @Override
    public void run() {

        for (int i = 0; i < 264; i++) {
            try {
                Thread.sleep(frequency);
                sharedqueue.put(majclass[i]);
                System.out.println("\n Produced no =  " +( i + 1) +  "  "+ majclass[i]);

            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);

            }


        }

    }


}