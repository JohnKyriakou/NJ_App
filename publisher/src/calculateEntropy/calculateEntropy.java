package calculateEntropy;

public class calculateEntropy
{
    public static double LOG_BASE = 2.0;

    public calculateEntropy() {}

    public static double calculateEntropy(double[] dataVector)
    {
        ProbabilityState state = new ProbabilityState(dataVector);

        double entropy = 0.0;
        for (Double prob : state.probMap.values())
        {
            if (prob > 0)
            {
                entropy -= prob * Math.log(prob);
            }
        }

        entropy /= Math.log(LOG_BASE);

        return entropy;
    }


}
