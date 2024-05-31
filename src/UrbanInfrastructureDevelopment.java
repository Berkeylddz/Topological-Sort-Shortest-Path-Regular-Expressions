import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UrbanInfrastructureDevelopment implements Serializable {
    static final long serialVersionUID = 88L;

    /**
     * Given a list of Project objects, prints the schedule of each of them.
     * Uses getEarliestSchedule() and printSchedule() methods of the current project to print its schedule.
     * @param projectList a list of Project objects
     */
    public void printSchedule(List<Project> projectList) {
        // TODO: YOUR CODE HERE
        for (Project project : projectList) {
            int[] schedule = project.getEarliestSchedule();
            printProjectSchedule(project, schedule);
        }
    }

    private void printProjectSchedule(Project project, int[] schedule) {
        System.out.println("-----------------------------------------------------------------");
        System.out.println("Project name: " + project.getName());
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-10s%-45s%-7s%-5s%n", "Task ID", "Description", "Start", "End");
        System.out.println("-----------------------------------------------------------------");
        for (int i = 0; i < schedule.length; i++) {
            Task task = project.getTasks().get(i);
            System.out.printf("%-10d%-45s%-7d%-5d%n", i, task.getDescription(), schedule[i], schedule[i] + task.getDuration());
        }
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("Project will be completed in %d days.%n", project.getProjectDuration());
        System.out.println("-----------------------------------------------------------------");
    }






    /**
     * TODO: Parse the input XML file and return a list of Project objects
     *
     * @param filename the input XML file
     * @return a list of Project objects
     */
    public List<Project> readXML(String filename) {
        List<Project> projectList = new ArrayList<>();

        try {
            File file = new File(filename);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList projectNodes = doc.getElementsByTagName("Project");
            for (int i = 0; i < projectNodes.getLength(); i++) {
                Node projectNode = projectNodes.item(i);
                if (projectNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element projectElement = (Element) projectNode;
                    String projectName = projectElement.getElementsByTagName("Name").item(0).getTextContent();
                    NodeList taskNodes = projectElement.getElementsByTagName("Task");
                    List<Task> taskList = new ArrayList<>();
                    for (int j = 0; j < taskNodes.getLength(); j++) {
                        Node taskNode = taskNodes.item(j);
                        if (taskNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element taskElement = (Element) taskNode;
                            int taskId = Integer.parseInt(taskElement.getElementsByTagName("TaskID").item(0).getTextContent());
                            String description = taskElement.getElementsByTagName("Description").item(0).getTextContent();
                            int duration = Integer.parseInt(taskElement.getElementsByTagName("Duration").item(0).getTextContent());
                            List<Integer> dependencies = new ArrayList<>();
                            NodeList dependencyNodes = taskElement.getElementsByTagName("DependsOnTaskID");
                            for (int k = 0; k < dependencyNodes.getLength(); k++) {
                                dependencies.add(Integer.parseInt(dependencyNodes.item(k).getTextContent()));
                            }
                            taskList.add(new Task(taskId, description, duration, dependencies));
                        }
                    }
                    projectList.add(new Project(projectName, taskList));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return projectList;
    }
}
