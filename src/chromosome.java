import java.util.Arrays;

/**
 * Chromosome class which acts as a possible key to a given encryption
 */
public class chromosome {

    char[] key;
    double fitness;

    /**
     * constructor for the chromosome class
     * @param length the max-length of the chromosome
     */
    chromosome(int length){
        key = new char[length];
        fitness = Double.POSITIVE_INFINITY;
    }

    void invert(int start, int end){

        char temp;
        int left = start;
        int right = end;

        while (left < right) {
            temp = key[left];
            key[left] = key[right];
            key[right] = temp;
            left++;
            right--;
        }

    }

    /**
     * @return a new copy of the chromosome
     */
    chromosome copy(){
        chromosome c = new chromosome(this.key.length);
        c.key = Arrays.copyOf(this.key, this.key.length);
        c.fitness = this.fitness;

        return  c;
    }

    /**
     * set one of the values in the chromosomes
     * @param index the index that is being set
     * @param val the val that the index is set to
     */
    void set(int index, char val){

        if ((val >= 'a' && val <= 'z') || val == '-') {
            key[index] = val;
        } else {
            throw new Error("Invalid Character Entry in the Key.");
        }
    }

    /**
     * get one of the values in the chromosomes
     * @param index the index we want the value of
     * @return the value from index index
     */
    char get(int index) {
        return key[index];
    }

    /**
     * @return a string version of the chromosome
     */
    public String toString(){
        return Arrays.toString(key);
    }

    /**
     * get fitness of chromosome
     * @return fitness
     */
    public double getFitness() {
        return fitness;
    }

    /**
     * set fitness of chromosome
     * @param fitness the new fitness
     */
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

}
