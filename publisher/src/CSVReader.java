
import calculateEntropy.*;
import java.io.*;

public class CSVReader{


    public static final String delimiter = ",";

    public static double[] read(String csvFile) {
        double[] entropy = new double[15];
        try {
            File file = new File(csvFile);
            FileReader fr = new FileReader(file);
            FileReader fr2 = new FileReader(file);


            BufferedReader reader = new BufferedReader(fr2);        //count rows
            String headerLine2 = reader.readLine();
            int count = 0;
            while (reader.readLine() != null) {
                count++;
            }
            System.out.println(count);
            reader.close();


            BufferedReader br = new BufferedReader(fr);


            String line = "";
            String input = "";

            String[] tempArr;

            String headerLine = br.readLine();

            double[][] data = new double[count][14];


            int i = 0;

            while ((line = br.readLine()) != null) {
                int j = 0;
                tempArr = line.split(delimiter);


                for (String tempStr : tempArr) {
                    if (j < 14) {

                        double d = Double.parseDouble(tempStr);

                        data[i][j] = d;

                        //System.out.println(tempStr + " ");


                    }
                    j++;
                }

                // System.out.println();
                i++;
            }

            double[] helper = new double[count];


            for (int j = 0; j < 14; j++) {

                for (i = 0; i < count; i++) {
                    helper[i] = data[i][j];
                }
                entropy[j] = calculateEntropy.calculateEntropy(helper);
                // System.out.println(entropy[j] + "     ");
                // FinalEntropy[c][j]=entropy[j];
            }
            /*for (int j = 0; j < entropy.length; j++) {

                    writer.append(String.valueOf(entropy[j]));
                    writer.append(",");


            }
            writer.append("\n");*/

            /*for( i=0; i<count; i++ ){
               for(int j=0; j<14; j++) {
                   System.out.println(data[i][j] + "     ");
               }
               }*/


            br.close();


        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return entropy;

    }

    public static double[][] readTraining(String csvFile2) throws IOException {

        File file = new File(csvFile2);
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String headerLine2 = reader.readLine();
        String line = "";
        double [][] TrainingSetArray=new double[36][14];
        String[] tempArr;

        int i=0;
        while ((line = reader.readLine()) != null) {
            int j=0;
            tempArr = line.split(delimiter);


            for (String tempStr : tempArr) {
                if (j>=1 &&  j<15) {

                    double d = Double.parseDouble(tempStr);

                    TrainingSetArray[i][j-1] = d;

                    //System.out.println(tempStr + " ");


                }
                j++;
            }

            // System.out.println();
            i++;
        }


   /* for(i=0; i<36; i++){
        for (int j=0; j<14; j++){
            System.out.print(TrainingSetArray[i][j] + " ");
        }
        System.out.println("\n");
    }*/

        return TrainingSetArray;
    }






}
