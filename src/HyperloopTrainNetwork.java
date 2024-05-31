import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HyperloopTrainNetwork implements Serializable {
    static final long serialVersionUID = 11L;
    public double averageTrainSpeed;
    public final double averageWalkingSpeed = 1000 / 6.0;;
    public int numTrainLines;
    public Station startPoint;
    public Station destinationPoint;
    public List<TrainLine> lines;

    /**
     * Method with a Regular Expression to extract integer numbers from the fileContent
     * @return the result as int
     */
    public int getIntVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*([0-9]+)");
        Matcher m = p.matcher(fileContent);
        m.find();
        return Integer.parseInt(m.group(1));
    }

    /**
     * Write the necessary Regular Expression to extract string constants from the fileContent
     * @return the result as String
     */
    public String getStringVar(String varName, String fileContent) {
        Pattern pattern = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(fileContent);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * Write the necessary Regular Expression to extract floating point numbers from the fileContent
     * Your regular expression should support floating point numbers with an arbitrary number of
     * decimals or without any (e.g. 5, 5.2, 5.02, 5.0002, etc.).
     * @return the result as Double
     */
    public Double getDoubleVar(String varName, String fileContent) {
        Pattern pattern = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*([0-9]+(?:\\.[0-9]*)?)");
        Matcher matcher = pattern.matcher(fileContent);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        }
        return null;
    }

    /**
     * Write the necessary Regular Expression to extract a Point object from the fileContent
     * points are given as an x and y coordinate pair surrounded by parentheses and separated by a comma
     * @return the result as a Point object
     */
    public Point getPointVar(String varName, String fileContent) {
        Pattern pattern = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)");
        Matcher matcher = pattern.matcher(fileContent);
        if (matcher.find()) {
            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2));
            return new Point(x, y);
        }
        return null;
    }


    /**
     * Function to extract the train lines from the fileContent by reading train line names and their 
     * respective stations.
     * @return List of TrainLine instances
     */
    public List<TrainLine> getTrainLines(String fileContent) {
        List<TrainLine> trainLines = new ArrayList<>();
                                                        //train_line_stations=(0 , 200) (5000, 200) (7000 , 200)

        Pattern pattern = Pattern.compile("train_line_name\\s*=\\s*\"(.*?)\".*?train_line_stations\\s*=\\s*\\((.*?)\\)\\s*\\((.*?)\\)\\s*\\((.*?)\\)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(fileContent);
        while (matcher.find()) {
            String lineName = matcher.group(1);
            String[] stationPairs = matcher.group(2).split("\\s*\\)\\s*\\("); // Parantezler arasında boşluklar olabilir
            List<Station> stations = new ArrayList<>();
            for (String pair : stationPairs) {
                pair = pair.replaceAll("\\s+", ""); // Boşlukları kaldır
                String[] coordinates = pair.split(",");
                int x = Integer.parseInt(coordinates[0].trim());
                int y = Integer.parseInt(coordinates[1].trim());
                stations.add(new Station(new Point(x, y), "Description"));
            }
            trainLines.add(new TrainLine(lineName, stations));
        }

        return trainLines;
    }


    /**
     * Function to populate the given instance variables of this class by calling the functions above.
     */
    public void readInput(String filename)  {
        // Dosyayı oku ve içeriği al
        StringBuilder fileContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Değişkenleri ayıkla
        numTrainLines = getIntVar("num_train_lines", fileContent.toString());
        startPoint = new Station(getPointVar("starting_point", fileContent.toString()),"A");
        destinationPoint = new Station(getPointVar("destination_point", fileContent.toString()),"B");
        averageTrainSpeed = getDoubleVar("average_train_speed", fileContent.toString());

        // Tren hatlarını al
        lines = getTrainLines(fileContent.toString());
    }
}