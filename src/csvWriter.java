import java.io.FileWriter;
import java.io.IOException;

public class csvWriter {

    // Extension we will set to identify different configurations
    static String extention = "Undefined";

    /**
     * Write to the results file
     * @param crossoverRate the crossover rate that will be noted along with the observation
     * @param mutationRate the mutation rate that will be noted along with the observation
     * @param fitness the fitness that will be noted
     */
    public static void writeResult(double crossoverRate, double mutationRate, double fitness){
        String filePath = "results" + extention + ".csv";

        try (FileWriter writer = new FileWriter(filePath, true)) { // 'true' = append mode
            writer.append(String.valueOf(crossoverRate)).append(",");
            writer.append(String.valueOf(mutationRate)).append(",");
            writer.append(String.valueOf(fitness)).append("\n");

            System.out.println("Result written to " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Write to the best averages file
     * @param crossoverRate the crossover rate that will be noted along with the observation
     * @param mutationRate the mutation rate that will be noted along with the observation
     * @param fitness the fitness that will be noted
     */
    public static void writeBestAverages(double crossoverRate, double mutationRate, double fitness){
        String filePath = "bestAverages" + extention + ".csv";

        try (FileWriter writer = new FileWriter(filePath, true)) { // 'true' = append mode
            writer.append(String.valueOf(crossoverRate)).append(",");
            writer.append(String.valueOf(mutationRate)).append(",");
            writer.append(String.valueOf(fitness)).append("\n");

            System.out.println("Result written to " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Write to the averages file
     * @param crossoverRate the crossover rate that will be noted along with the observation
     * @param mutationRate the mutation rate that will be noted along with the observation
     * @param fitness the fitness that will be noted
     */
    public static void writeAverages(double crossoverRate, double mutationRate, double fitness) {
        String filePath = "averages" + extention + ".csv";

        try (FileWriter writer = new FileWriter(filePath, true)) { // 'true' = append mode
            writer.append(String.valueOf(crossoverRate)).append(",");
            writer.append(String.valueOf(mutationRate)).append(",");
            writer.append(String.valueOf(fitness)).append("\n");

            System.out.println("Result written to " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

}
