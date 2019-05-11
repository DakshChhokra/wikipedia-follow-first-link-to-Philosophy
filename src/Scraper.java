import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Scraper {

    Graph graph;
    Node endNode;
    HashMap<String, Integer> lengthOfPaths = new HashMap<String, Integer>();

    public Scraper(String target) {
        String targetLink;
        if(target.equals("placeholder") || target.isEmpty()) {
            targetLink = "https://en.wikipedia.org/wiki/Philosophy";
        } else {
            targetLink = target;
        }
        this.graph = new Graph();
        endNode = new Node(targetLink);
        graph.addNode(endNode);
    }

    public Graph getGraph() {
        return graph;
    }

    /**
     * Creates an arraylist of nodes which give the path from the input node to Philosophy
     * @param startLink
     * @return An arraylist of nodes
     * @throws IOException when any link is unreachable.
     */
    public ArrayList<Node> getPath(String startLink) throws IOException{
        ArrayList<Node> path = new ArrayList<Node>();

        Node inputNode = new Node(startLink);

        if(inputNode.equals(endNode)) {
            lengthOfPaths.put(inputNode.getUrl(), 0);
            return path;
        }


        System.out.println("Starting traversal of all the first links in wikipedia starting at "
                + inputNode.getPageName() + " and ending until we get to the page with the title "
                + endNode.getPageName());
        getPathMain(inputNode, path);
        lengthOfPaths.put(inputNode.getUrl(), path.size());
        return path;
    }

    /**
     * Primary helper method to traverse through the first link. Called inside getPath
     * @param inputNode
     * @param path
     * @throws IOException  when any link is unreachable.
     */
     void getPathMain(Node inputNode, ArrayList<Node> path) throws IOException {
         System.out.println("************************************************************************************");
         System.out.print("Current Node is: ");
         System.out.println();

        if(graph.isNeighbourListEmpty(inputNode) && !path.contains(inputNode)) {
            System.out.println("Calling helper");
            traverseToTarget(inputNode, path);
        } else {
            Document doc = Jsoup.connect(inputNode.getUrl()).get();
            Elements elem = doc.select("body").select("p").select("a[href]");

            for(Element el: elem) {
                String current = el.attr("abs:href");
                Node currentNode = new Node(current);

                if (currentNode.equals(endNode)) {
                    System.out.println("The first valid link on this page is: ");
                    System.out.print(currentNode);
                    System.out.println();

                    path.add(inputNode);
                    path.add(currentNode);

                    graph.addNode(inputNode);
                    graph.addNode(currentNode);
                    graph.addEdge(inputNode, currentNode);
                    System.out.println("************************************************************************************");
                    return;
                } else {
                    if (isIgnored(current) && !path.contains(currentNode)) {

                        Connection.Response response = Jsoup.connect(current).followRedirects(true).execute();
                        current = response.url().toString();

                        System.out.print("The first valid link on this page is: ");
                        System.out.print(currentNode);
                        System.out.println();

                        graph.addNode(inputNode);
                        graph.addNode(currentNode);
                        graph.addEdge(inputNode, currentNode);


                        path.add(inputNode);

                        getPathMain(currentNode, path);
                        return;
                    }
                }
            }

        }




    }

    /**
     * If the traverser hits a node which already exists in the graph, there is no need to scrape anymore wikipedia
     * pages. This method simply appends the existing path to the current path, and returns it to the main method
     * @param current The current page in the path
     * @param path The path which has been created until now
     */
    void traverseToTarget(Node current, ArrayList<Node> path) {
        path.add(current);
        System.out.println("Helper begins");
        if (current.equals(endNode)) {
            return;
        } else {
            ArrayList<Node> neighbours = graph.getNeighbours(current);
            System.out.println("Current Node in helper is " + current.getUrl());
            traverseToTarget(neighbours.get(0), path);
        }
    }

    /**
     * Simply returns the underlying graph
     * @return Return the adjacency list representation of the underlying graph
     */
    public HashMap<Node,ArrayList<Node>> getUnderlyingGraph() {
        return graph.getAdjList();
    }

    /**
     * Returns the underlying graph in String form
     * @return Return the adjacency list representation of the underlying graph where every Node is a String.
     * @throws IOException when any link is unreachable.
     */
    public HashMap<String, ArrayList<String>> getUnderlyingGraphInStringForm() throws IOException {
        HashMap<String,ArrayList<String>> stringMap = new HashMap<>();

        Set<Node> allVertices = graph.getAdjList().keySet();
        for(Node x: allVertices) {
            ArrayList<Node> neighbourList = graph.getNeighbours(x);
            ArrayList<String> stringList = new ArrayList<>();
            for(Node y: neighbourList) {
                stringList.add(y.getPageName());
            }
            stringMap.put(x.getPageName(), stringList);
        }

        return stringMap;
    }

    /**
     * This method validates all the links in every wikipedia page. We filter out particular strings to ensure we get
     * the right sort of links.
     * @param current
     * @return If true, then the link is validated. Else, false.
     */
    boolean isIgnored(String current) {
        return (!current.contains("Help")
                && !current.contains("#cite_note")
                && !current.contains(".ogg")
                && current.contains("https://en.wikipedia.org/")
                && !current.contains(".jpg")
                && !current.contains(".jpeg")
                && !current.contains(".tiff")
                && !current.contains(".png")
                && !current.contains("Portal:")
                && !current.contains("File:")
                && !current.contains("Order_of")
                && !current.contains("#")
                && !current.contains("Wikipedia:Citation_needed")
                && !current.contains("redlink=1")
                && !current.equals("https://en.wikipedia.org/wiki/Ancient_Greek")
                && !current.equals("https://en.wikipedia.org/wiki/Ancient_Greek_language")
                && !current.equals("https://en.wikipedia.org/wiki/Greek_language")
                && !current.equals("https://en.wikipedia.org/wiki/French_language")
                && !current.equals("https://en.wikipedia.org/wiki/Latin")
                && !current.equals("https://en.wikipedia.org/wiki/Romanization_of_Ancient_Greek")
                && !current.equals("https://en.wikipedia.org/wiki/Romanization_of_Greek")
                && !current.equals("https://en.wikipedia.org/wiki/Literal_translation")
                && !current.equals("https://en.wikipedia.org/wiki/Geographic_coordinate_system"))
                && !current.equals("https://en.wikipedia.org/wiki/Entity");
    }


    /**
     * Writes the graph to a csv which is useful for analysis.
     * @throws IOException when any link is unreachable.
     */
    public void writeTOText() throws IOException {
            PrintWriter out = new PrintWriter("text_graph.txt");
            out.println("Graph:");
            out.print("\n");
            HashMap<String, ArrayList<String>> graph = this.getUnderlyingGraphInStringForm();

            for(Map.Entry<String, ArrayList<String>> i: graph.entrySet()){
                ArrayList<String> list = i.getValue();
                out.print(i.getKey()+" ->");

                for(String j: list){  out.print(" "+j+","); }
                out.print("\n");
            }

            out.print("\n");
            out.println("Funnels:");
            out.print("\n");

            HashMap<String, Integer> funnels = this.graph.getFunnels();

            for(Map.Entry<String, Integer> k: funnels.entrySet()){
                out.print(k.getKey()+": Edges in = "+k.getValue()+"\n");
            }

            out.close();
    }


    /**
     * Get the lengths of all the paths
     * @return a hash map with keys as pages and values as the length of the path
     */
    public HashMap<String, Integer> getSizeOfLength() {
        return this.lengthOfPaths;
    }

    /**
     * Get the length of the path from a given link to Philosophy.
     * @param inputLink
     * @return length of the path
     */
    public int getLengthOfOneParticularNode(String inputLink) {
        return this.lengthOfPaths.get(inputLink);
    }

    /**
     * Get the length of the path from a given link to Philosophy.
     * @param inputNode
     * @return length of the path
     */
    public int getLengthOfOneParticularNode(Node inputNode) {
        return this.lengthOfPaths.get(inputNode.getUrl());
    }







}
