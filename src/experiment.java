import java.io.File;

public class experiment {

    /**
     * Constructor for the experiment class. Runs the entire experiment for a given crossover and mutation type
     */
    public static void run(){
        int popSize = 500;
        int maxGenSpan = 100;
        int k = 2;
        double elitePercentage = 0.01;
        double targetFitness = 0.01;

        File file;
        GA test;
        double[][] fitnessScores;

        double[] mutationRates = {0, 0.1, 0.5};
        double[] crossoverRates = {0.9, 1};

        file = new File("Assign2_Attachments/Data1.txt");
        csvWriter.extention = "ASCIIMutationTwoPointCrossover1";
        fitnessScores = new double[5][];
        for(double mutationRate : mutationRates){
            for(double crossOverRate : crossoverRates){

                // Repeating each run 5 times
                for(int i = 0; i < 5; i++){
                    System.out.println("Parameters: \n Crossover Rate: " + crossOverRate + "    Mutation Rate: " + mutationRate);
                    test = new GA(crossOverRate, mutationRate, popSize, maxGenSpan, k, elitePercentage, targetFitness, file);
                    csvWriter.writeResult(crossOverRate, mutationRate, test.bestCandidate.fitness);
                    fitnessScores[i] = test.fitnessTracker;
                }
                noteAverages(crossOverRate, mutationRate, fitnessScores);
                noteBestAverages(crossOverRate, mutationRate, fitnessScores);
            }
        }

        file = new File("Assign2_Attachments/Data2.txt");
        csvWriter.extention = "ASCIIMutationTwoPointCrossover2";
        fitnessScores = new double[5][];
        for(double mutationRate : mutationRates){
            for(double crossOverRate : crossoverRates){

                // Repeating each run 5 times
                for(int i = 0; i < 5; i++){
                    System.out.println("Parameters: \n Crossover Rate: " + crossOverRate + "    Mutation Rate: " + mutationRate);
                    test = new GA(crossOverRate, mutationRate, popSize, maxGenSpan, k, elitePercentage, targetFitness, file);
                    fitnessScores[i] = test.fitnessTracker;
                }

                noteAverages(crossOverRate, mutationRate, fitnessScores);
                noteBestAverages(crossOverRate, mutationRate, fitnessScores);
            }
        }
    }

    /**
     * Note the best averages amongst the 5 runs
     * @param crossoverRate the crossover rate that will be noted along with the observation
     * @param mutationRate the mutation rate that will be noted along with the observation
     * @param fitnessScores the fitnessScores that will be averaged out
     */
    public static void noteBestAverages(double crossoverRate, double mutationRate, double[][] fitnessScores){
        double bestAvgFitness = 0;
        int j;

        for(j = 0; j < fitnessScores.length; j++){
            bestAvgFitness += fitnessScores[j][9];
        }
        bestAvgFitness = bestAvgFitness/fitnessScores.length;
        csvWriter.writeBestAverages(crossoverRate, mutationRate, bestAvgFitness);
    }

    /**
     * Note the averages every 10 generations amongst the 5 runs
     * @param crossoverRate the crossover rate that will be noted along with the observation
     * @param mutationRate the mutation rate that will be noted along with the observation
     * @param fitnessScores the fitnessScores that will be averaged out
     */
    public static void noteAverages(double crossoverRate, double mutationRate, double[][] fitnessScores){
        double avgFitness;
        int i = 0;
        int j = 0;

        for(i = 0; i < fitnessScores[0].length; i++){
            avgFitness = 0;
            for(j = 0; j < fitnessScores.length; j++){
                avgFitness += fitnessScores[j][i];
            }
            avgFitness = avgFitness/fitnessScores.length;
            csvWriter.writeAverages(crossoverRate, mutationRate, avgFitness);
        }
    }

    public static void main(String[] args) {
        experiment.run();

    }
}
