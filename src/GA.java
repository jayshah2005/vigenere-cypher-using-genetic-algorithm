import java.io.File;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;

public class GA {

    double crossOverRate;
    double mutationRate;
    int popSize;    // Size of Population
    chromosome[] population;    // The population
    int maxLength;  // Max length of the chromosome
    String encryptedText;
    chromosome bestCandidate;
    PriorityQueue<chromosome> eliteCandidates;   // The elite population
    int generalPopulation;  // Size of population excluding the elite individuals

    GA(double crossOverRate, double mutationRate, int popSize, int maxGenSpan, int k, File file){

        if(k < 1 || k > 5) throw new Error("k has to be between 1 to 5 for tournament selection");

        readFile(file);

        this.crossOverRate = crossOverRate;
        this.mutationRate = mutationRate;
        this.popSize = popSize;
        this.bestCandidate = new chromosome(0);
        this.generalPopulation = (int) (popSize * 0.9);
        this.eliteCandidates = new PriorityQueue<>(new FitnessComparator());

        population = generateInitialPopulation();
        for(int i = 0; i < maxGenSpan; i++){
            evaluateFitness();
            System.out.println(bestCandidate.toString() + " " + bestCandidate.getFitness());
            population = generateNewPopulation(k);
            uniformCrossover();
            inversionMutate();

            // Include elitism when generating each new population
            population[this.popSize - 1] = bestCandidate.copy();
        }

        System.out.println(Evaluation.decrypt(bestCandidate.toString(), encryptedText));
        System.out.println(Evaluation.decrypt("this is a super secret answer", encryptedText));

    }


    /**
     * Perform inversion mutation on the entire population
     */
    void inversionMutate(){

        Random rand = new Random();
        int start;
        int end;

        if(rand.nextDouble() > mutationRate) return;

        // -1 is used to leave one space for elitism
        for(int i = 0; i < popSize - 1; i++){

            start = rand.nextInt(maxLength);
            end = rand.nextInt(maxLength - start) + start;

            population[i].invert(start, end);
        }
    }

    /**
     * Perform uniform crossover on the population
     */
    void uniformCrossover(){

        Random rand = new Random();
        char temp;
        int i = rand.nextInt(this.maxLength);
        int j = rand.nextInt(this.maxLength);

        if(rand.nextDouble() > crossOverRate) return;

        // -1 is used to leave one space for elitism
        temp = population[i].get(j);
        population[i].set(j, population[i+1].get(j));
        population[i+1].set(j, temp);
    }

    /**
     * Generate a new population using tournament selection
     * @return a new population of size popSize
     */
    chromosome[] generateNewPopulation(int k){
        chromosome[] newPopulation = new chromosome[this.popSize];

        // -1 is used to leave one space for elitism
        for(int i = 0; i < this.popSize - 1; i++){
            newPopulation[i] = tournamentSelection(k);
        }

        return newPopulation;
    }

    /**
     * run a round of tournament selection
     * @param k the sample size
     * @return the best chromosome among k sameples
     */
    chromosome tournamentSelection(int k){
        chromosome best = new chromosome(0);
        Random rand = new Random();
        int index;

        // Get a the best out of a sample population of size k
        for(int i = 0; i < k; i++) {
            index = rand.nextInt(popSize);

            if(population[index].getFitness() < best.getFitness()) best = population[index];
        }

        return best;
    }

    /**
     * Evaluate a given population using the default evaluate function
     */
    void evaluateFitness(){

        double fitness = 0.0;

        for(chromosome c : population){
            fitness = Evaluation.fitness(c.toString(), encryptedText);

            c.setFitness(fitness);

            // Keep track of the best chromosome
            if(fitness < bestCandidate.getFitness()) bestCandidate = c.copy();
        }
    }

    /**
     * Read a file to load teh maxLength of key/chromosome and fetch the encrypted text
     * @param f the file that is read
     */
    public void readFile(File f){
        try {
            Scanner myReader = new Scanner(f);

            maxLength = myReader.nextInt();

            this.encryptedText = "";
            while (myReader.hasNextLine()) {
                encryptedText += myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            throw new Error(e);
        }
    }

    /**
     * Generate Initial Population for Genetic Algorithm
     * @return a population of size popSize
     */
    chromosome[] generateInitialPopulation(){
        chromosome[] population = new chromosome[this.popSize];

        for(int i = 0; i < this.popSize; i++){
            population[i] = generateChromosome();
        }

        return population;
    }

    /**
     * Generates a random chromosome
     * @return a chromosome
     */
    chromosome generateChromosome(){
        chromosome c = new chromosome(maxLength);

        for(int i = 0; i< maxLength; i++){
            c.set(i, getRandomChar());
        }

        return c;
    }

    /**
     * Generates a random char between a to z and -
     * @return a char
     */
    char getRandomChar() {
        Random rand = new Random();
        int n = rand.nextInt(27);  // 0 to 26
        return (n < 26) ? (char) ('a' + n) : '-';
    }

    chromosome[] generateSpecializedInitialPopulation(){
        chromosome[] population = new chromosome[this.popSize];

        for(int i = 0; i < this.popSize; i++){
            population[i] = generateChromosome(maxLength - i%(maxLength-1));
        }

        return population;
    }

    /**
     * Generates a chromosome with actual length k
     * @return a chromosome
     */
    chromosome generateChromosome(int length){
        chromosome c = new chromosome(maxLength);

        for(int i = 0; i< length; i++){
            c.set(i, getRandomLetter());
        }

        for(int i = length; i < maxLength; i++){
            c.set(i, '-');
        }

        return c;
    }

    /**
     * generates a random letter from a to z
     * @return a char from a to z
     */
    char getRandomLetter() {
        Random rand = new Random();
        int n = rand.nextInt(26);  // 0 to 26
        return (char) ('a' + n);
    }

    public static void main(String[] args) {

        float crossOverRate;
        float mutationRate;
        int popSize;
        File file = new File("/Users/jayshah/Developer/Brock University/COSC 3P71/GA/Assign2_Attachments/Data1.txt");

        new GA(0.9, 0.1, 500, 500, 2, file);
    }
}