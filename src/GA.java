import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;

public class GA {

    final double elitismPercentage;
    final double crossOverRate;
    final double mutationRate;
    final int popSize;    // Size of Population
    final int trackMod = 10;
    final double[] fitnessTracker = new double[10];

    int maxLength;  // Max length of the chromosome
    String encryptedText;

    chromosome[] population;    // The population
    chromosome bestCandidate;

    /**
     * GA constructor
     * @param crossOverRate rate of crossover
     * @param mutationRate rate of mutation
     * @param popSize population size
     * @param maxGenSpan maximum number of generation
     * @param k tournament selection sample size
     * @param elitismPercentage percentage of elite individuals
     * @param targetFitness target fitness we are trying to achieve
     * @param file the file containing the max key length and encrypted text
     */
    GA(double crossOverRate, double mutationRate, int popSize, int maxGenSpan, int k, double elitismPercentage, double targetFitness, File file) {

        if (k < 1 || k > 5) throw new Error("k has to be between 1 to 5 for tournament selection");

        readFile(file);

        this.crossOverRate = crossOverRate;
        this.mutationRate = mutationRate;
        this.popSize = popSize;
        this.bestCandidate = new chromosome(0);
        this.elitismPercentage = elitismPercentage;

        int counter = 0;    // To track how many times we have written to fitnessTracker

        population = generateInitialPopulation();
        for (int i = 0; i < maxGenSpan && bestCandidate.fitness > targetFitness; i++) {
            evaluateFitness();

            if(i % trackMod == 0) {
                fitnessTracker[counter++] = bestCandidate.getFitness();
                System.out.println("For Generation " + i + ": \n" + bestCandidate.toString() + " " + bestCandidate.getFitness());
            }

            population = generateNewPopulation(k);
        }

        evaluateFitness();

        System.out.println("For Generation " + maxGenSpan + ": \n" + bestCandidate.toString() + " " + bestCandidate.getFitness());
    }

    String getAns(){
        return bestCandidate.toString() + " " + bestCandidate.getFitness();
    }

    chromosome asciiMutate(chromosome c){

        Random rand = new Random();
        int index;
        int change;
        char newVal;
        chromosome copy = c.copy();

        if (rand.nextDouble() > mutationRate) return copy;

        do{
            index = rand.nextInt(maxLength);
        } while(copy.get(index) == '-');

        change = rand.nextInt(5) - 2;
        newVal = (char) ('a' + (copy.get(index) - 'a' + change + 26) % 26);

        copy.set(index, newVal);

        return copy;
    }

    /**
     * Performs inverse mutation on a chromosome
     *
     * @param c the chromosome being mutated
     * @return the mutated chromosome
     */
    chromosome inversionMutate(chromosome c) {

        Random rand = new Random();
        int start;
        int end;
        chromosome copy = c.copy();

        if (rand.nextDouble() > mutationRate) return copy;

        start = rand.nextInt(maxLength);
        end = rand.nextInt(maxLength - start) + start;

        copy.invert(start, end);

        return copy;
    }

    /**
     * Performs uniform crossover
     *
     * @param parents the parents selected for uniform crossover
     * @return a pair of children who underwent uniform crossover
     */
    chromosome[] uniformCrossover(chromosome[] parents) {

        Random rand = new Random();
        char temp;
        chromosome[] children = new chromosome[2];
        children[0] = parents[0].copy();
        children[1] = parents[1].copy();

        if (rand.nextDouble() > crossOverRate) return children;

        for (int x = 0; x < this.maxLength; x++) {

            // If the mask if 1, swap the two
            if (rand.nextDouble() < 0.5) {
                temp = children[0].get(x);
                children[0].set(x, children[1].get(x));
                children[1].set(x, temp);
            }
        }

        return children;
    }

    /**
     * Performs uniform crossover
     *
     * @param parents the parents selected for uniform crossover
     * @return a pair of children who underwent uniform crossover
     */
    chromosome[] twoPointCrossover(chromosome[] parents) {

        Random rand = new Random();
        char temp;
        chromosome[] children = new chromosome[2];
        children[0] = parents[0].copy();
        children[1] = parents[1].copy();

        if (rand.nextDouble() > crossOverRate) return children;

        int start = rand.nextInt(maxLength);
        int end = rand.nextInt(maxLength - start);

        // Swap the mask where it is one
        for (int x = start; x < end; x++) {

            temp = children[0].get(x);
            children[0].set(x, children[1].get(x));
            children[1].set(x, temp);

        }

        return children;
    }

    /**
     * Generate a new population using tournament selection
     * @return a new population of size popSize
     */
    chromosome[] generateNewPopulation(int k){

        chromosome[] newPopulation = new chromosome[this.popSize + 1];  // +1 is to make sure we do not get an out of bounds error in the for loop
        int elitePopulationNumber = (int)(elitismPercentage * popSize);
        chromosome[] parents = new chromosome[2];
        chromosome[] children;

        addElitism(newPopulation);

        // -1 is used to leave one space for elitism
        for(int i = elitePopulationNumber; i < this.popSize; i+=2){
            parents[0] = tournamentSelection(k);
            parents[1] = tournamentSelection(k);

            children = twoPointCrossover(parents);

            newPopulation[i] = asciiMutate(children[0]);
            newPopulation[i+1] = asciiMutate(children[1]);

        }

        return Arrays.copyOf(newPopulation, popSize);
    }

    /**
     * Adds elite individuals to new population
     * @param newPopulation population we are adding to
     */
    void addElitism(chromosome[] newPopulation){

        int elitePopulationNumber = (int)(elitismPercentage * popSize);

        chromosome[] sortedPopulation = Arrays.copyOf(population, popSize);

        Arrays.sort(sortedPopulation, new FitnessComparator());

        for(int i = 0; i < elitePopulationNumber; i++){
            newPopulation[i] = sortedPopulation[i];
        }
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

        return best.copy();
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

//      double crossOverRate;
//      double mutationRate;
        int popSize = 300;
        int maxGenSpan = 100;
        int k = 2;
        double elitePercentage = 0.1;
        double targetFitness = 0.01;
        File file = new File("/Users/jayshah/Developer/Brock University/COSC 3P71/GA/Assign2_Attachments/Data2.txt");

        double[] mutationRates = {0, 0.1, 0.2, 0.3, 0.4, 0.5};
        double[] crossoverRates = {0.9, 1};

        GA test;
        for(double mutationRate : mutationRates){
            for(double crossOverRate : crossoverRates){
                // Repeating each run 5 times
                for(int i = 0; i < 5; i++){
                    System.out.println("Parameters: \n Crossover Rate: " + crossOverRate + "    Mutation Rate: " + mutationRate);
                    test = new GA(crossOverRate, mutationRate, popSize, maxGenSpan, k, elitePercentage, targetFitness, file);
                }
            }
        }
    }
}