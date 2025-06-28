import java.util.Comparator;

class FitnessComparator implements Comparator<chromosome> {
    @Override
    public int compare(chromosome a, chromosome b) {
        return Double.compare(a.getFitness(), b.getFitness());
    }
}
