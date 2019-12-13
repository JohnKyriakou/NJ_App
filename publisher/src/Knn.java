import java.util.*;



class TrainingSet{
    double [] Training_Set;
    String s;
    public TrainingSet(double[] Training_Set,String s) {
        this.Training_Set=Training_Set;
        this.s=s;
    }
}
class result{
    double distance;
    String s;
    public result(double distance, String s){

        this.distance = distance;
        this.s=s;
    }

}

class DistanceComparator implements Comparator<result> {
    @Override
    public int compare(result a, result b)
    {
        return a.distance < b.distance ? -1 : a.distance == b.distance ? 0 : 1;
    }
}

public class Knn {

    public  static void List(){

        CSVReader Csv=new CSVReader();



    }



















}
