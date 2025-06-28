import java.io.FileWriter;
import java.io.IOException;

public class csvWriter {

    static String extention = "Undefined";

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
