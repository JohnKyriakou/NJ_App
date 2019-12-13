import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.io.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.System.in;


public class MqttPublishSample  {

    private static String[] frequency = new String[1];

    private static String findMajorityClass(String[] array) {

        Set<String> h = new HashSet<String>(Arrays.asList(array));
        String[] uniqueValues = h.toArray(new String[0]);
        int[] counts = new int[uniqueValues.length];
        for (int i = 0; i < uniqueValues.length; i++) {
            for (int j = 0; j < array.length; j++) {
                if (array[j].equals(uniqueValues[i])) {
                    counts[i]++;
                }
            }
        }
        for (int i = 0; i < uniqueValues.length; i++)
            System.out.println(uniqueValues[i]);
        for (int i = 0; i < counts.length; i++)
            System.out.println(counts[i]);
        int max = counts[0];
        for (int counter = 1; counter < counts.length; counter++) {
            if (counts[counter] > max) {
                max = counts[counter];
            }
        }
        System.out.println("max # of occurences: " + max);
        int freq = 0;
        for (int counter = 0; counter < counts.length; counter++) {
            if (counts[counter] == max) {
                freq++;
            }
        }
        int index = -1;
        if(freq==1){
            for (int counter = 0; counter < counts.length; counter++) {
                if (counts[counter] == max) {
                    index = counter;
                    break;
                }
            }

            return uniqueValues[index];
        } else{
            int[] ix = new int[freq];
            System.out.println("multiple majority classes: "+freq+" classes");
            int ixi = 0;
            for (int counter = 0; counter < counts.length; counter++) {
                if (counts[counter] == max) {
                    ix[ixi] = counter;
                    ixi++;
                }
            }

            for (int counter = 0; counter < ix.length; counter++)
                System.out.println("class index: "+ix[counter]);

            //now choose one at random
            Random generator = new Random();
            //get random number 0 <= rIndex < size of ix
            int rIndex = generator.nextInt(ix.length);
            System.out.println("random index: "+rIndex);
            int nIndex = ix[rIndex];
            //return unique value at that index
            return uniqueValues[nIndex];
        }
    }


    /*private static double meanOfArray(double[] m) {
        double sum = 0.0;
        for (int j = 0; j < m.length; j++){
            sum += m[j];
        }
        return sum/m.length;
    }*/



    public static void main(String[] args) throws MqttException, IOException, InterruptedException {


        String topic = "NJ app";
        String topic1="NJ App";
        String content = "null";
        int qos = 2;
        String broker = "tcp://localhost:1883";
        String clientId = "JavaSample";
        MemoryPersistence persistence = new MemoryPersistence();

        frequency[0]="null";

        MqttClient sampleClient = null;
        while (!content.equalsIgnoreCase("exit")) {

            try {
                BufferedReader bufferRead = new BufferedReader(new InputStreamReader(in));
                System.out.println("Give an instruction:");
                content = bufferRead.readLine();

                System.out.println(content);

                sampleClient = new MqttClient(broker, clientId, persistence);
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(true);
                System.out.println("Connecting to broker: " + broker);
                sampleClient.connect(connOpts);
                System.out.println("Connected");
                System.out.println("Publishing message: " + content);

                MqttMessage message = new MqttMessage(content.getBytes());

                message.setQos(qos);
                sampleClient.publish(topic, message.getPayload(), 2, false);
                System.out.println("Message published");



            } catch (MqttException me) {
                System.out.println("reason " + me.getReasonCode());
                System.out.println("msg " + me.getMessage());
                System.out.println("loc " + me.getLocalizedMessage());
                System.out.println("cause " + me.getCause());
                System.out.println("excep " + me);
                me.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        //////////////////////////Xekinaei to deytero paradoteo ///////////////////////////////////////////


        sampleClient = new MqttClient(broker, clientId, persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        sampleClient.connect(connOpts);
        sampleClient.subscribe(topic1, qos);

        int k =0;
        while(k==0){
            System.out.println("Give k neighbours from keyboard");
            Scanner in = new Scanner(System.in);
            k = in.nextInt();
        }



        System.out.println("Give frequency from appe:");

        while(frequency[0].toString().equals("null")) {
            Thread.sleep(1);
            sampleClient.setCallback(new MqttCallback() {

                @Override
                public void connectionLost(Throwable throwable) {

                }

                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    //System.out.println("frequency = " + mqttMessage);
                    frequency[0] = mqttMessage.toString();
                     //System.out.print(frequency[0]);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });
       }

        double[][] Training=new double[36][14];

        double[] entropy=new double[14];

        int sum_class=0;
        int c=0;

        File dir = new File("C:/Users/John/Desktop/Data Final for Software Development");

        File[] fileArray = dir.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(".csv");
            }
        });
        BlockingQueue sharedQueue=new LinkedBlockingQueue();

        String [] Buffer=new String[264];
        for(File f: fileArray){


            String csvFile2="C:/Users/John/Desktop/Training Set.csv";
            Training=CSVReader.readTraining(csvFile2);

            List<TrainingSet> TrainingSetList = new ArrayList<TrainingSet>();

            for (int i=0; i<36; i=i+2){
                TrainingSetList.add(new TrainingSet(Training[i],"EyesClosed"));
                TrainingSetList.add(new TrainingSet(Training[i+1],"EyesOpened"));
            }

            List<result> resultList = new ArrayList<result>();

            System.out.println(f.getPath());

            entropy= CSVReader.read(f.getPath());

          /*  for(int i=0; i<14; i++){
                System.out.print(entropy[i]);
            }*/

            for (TrainingSet train : TrainingSetList) {
                double dist = 0.0;
                for (int j = 0; j < train.Training_Set.length; j++) {
                    dist += Math.pow(train.Training_Set[j] - entropy[j], 2);

                    //System.out.print(city.cityAttributes[j]+" ");
                }
                double distance = Math.sqrt(dist);
                resultList.add(new result(distance, train.s));
                //System.out.println(distance);
            }


            //System.out.println(resultList);
            Collections.sort(resultList, new DistanceComparator());
            String[] ss = new String[k];
            for (int x = 0; x < k; x++) {
                System.out.println(resultList.get(x).s + " .... " + resultList.get(x).distance);

                ss[x] = resultList.get(x).s;
            }
            String majClass = findMajorityClass(ss);
            System.out.println("Class of new instance is: " + majClass);

            Buffer[c]=majClass;

            c++;
            System.out.print("\n");
            if(f.getPath().toLowerCase().contains(majClass.toLowerCase())) {

                sum_class++;
            }

        }
        int fr=Integer.parseInt(frequency[0]);

        Thread ProdThread=new Thread(new Producer(sharedQueue,Buffer,fr));
        Thread ConThread=new Thread((new Consumer(sharedQueue,sampleClient)));
        ProdThread.start();
        ConThread.start();

      /* for(int i=0; i<264; i++){

        System.out.println(Buffer[i]+"      ");
        }*/


        System.out.println(sum_class);
        double rate =sum_class/264.0;

        System.out.print("rate = " +rate*100+"%"+"\n");



       // sampleClient.disconnect();
       // System.out.println("Disconnected");
       // System.exit(0);


    }


}








