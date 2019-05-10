import java.io.IOException;
import java.util.*;

public class Graph {

    HashMap<Node, ArrayList<Node>> adjList;

    public Graph() {
        adjList = new HashMap<Node, ArrayList<Node>>();
    }

    /**
     * Checks if a particular node exists in the graph
     *
     * @param inputNode
     * @return True if successfully added. False otherwise.
     */
    public boolean containsNode(Node inputNode) {
        return adjList.containsKey(inputNode);
    }

    /**
     * Add a node to the graph.
     * @param input
     * @return True if successfully added. False otherwise.
     */
    public boolean addNode(Node input) {
        if(containsNode(input)) {
            return false;
        } else {
            ArrayList<Node> value = new ArrayList<Node>();
            adjList.put(input, value);
            return true;
        }

    }

    /**
     * Check if a particular edge exists
     * @param from Originating node of the edge
     * @param to Destination node of the edge
     * @return True if edge exists. False otherwise.
     */
    public boolean containsEdge(Node from, Node to) {
        if (!containsNode(from) || !containsNode(to)) {
            throw new IllegalArgumentException("The nodes in the edge don't exist");
        } else {
            ArrayList<Node> checklist = adjList.get(from);
            return checklist.contains(to);
        }
    }

    /**
     * Adds an edge to the graph
     * @param from Originating node of the edge
     * @param to Destination node of the edge
     * @return True if successfully added. False otherwise.
     */
    public boolean addEdge(Node from, Node to) {
        if(containsEdge(from, to)) {
            return false;
        } else {
            ArrayList<Node> checklist = adjList.get(from);
            checklist.add(to);
            adjList.put(from, checklist);
            return true;
        }
    }

    /**
     * Get all the neighbours of a particular node in the graph.
     * @param input
     * @return An Arraylist of all neighbouring Nodes
     */
    public ArrayList<Node> getNeighbours(Node input) {
        if(!containsNode(input)) {
            throw new IllegalArgumentException("Node does not exist");
        } else {
            return adjList.get(input);
        }
    }

    /**
     * Checks if a particular node has no neighbours
     * @param input
     * @return True, if no edges originate at the input edge. False otherwise.
     */
    public boolean isNeighbourListEmpty(Node input) {
        if(!containsNode(input)) {
            return false;
        } else {
            ArrayList<Node> neighbour = adjList.get(input);
            return  !neighbour.isEmpty();
        }

    }

    @Override
    public String toString() {
        return "Graph{" +
                "adjList=" + adjList +
                '}';
    }

    /**
     * @return The Adjacency List which represents and stores the graph
     */
    public HashMap<Node, ArrayList<Node>> getAdjList() {
        return adjList;
    }

    /**
     * Returns the distance of the input link to Philosophy
     * @param inputLink
     * @return The integer distance to Philosophy if the input node exists. Null if it doesn't.
     * @throws IOException when any link is unreachable.
     */
    public Integer searchUsingBFS(String  inputLink) throws IOException {
        return searchUsingBFS(new Node(inputLink));
    }

    /**
     * Returns the distance of the input node to Philosophy
     * @param inputNode
     * @return The integer distance to Philosophy if the input node exists. Null if it doesn't.
     * @throws IOException when any link is unreachable.
     */
    public Integer searchUsingBFS(Node inputNode) throws IOException {
        ArrayList<Node> input = new ArrayList<>();
        input.add(inputNode);
        HashMap<Node, Integer> output = searchUsingBFS(input);
        return output.get(inputNode);
    }

    /**
     * Returns the distance of the all the input nodes to Philosophy
     * @param inputList
     * @return A hashmap where the key is the node and the value is the distance to philosophy.
     * @throws IOException when any link is unreachable.
     */
    public HashMap<Node, Integer> searchUsingBFS(ArrayList<Node> inputList) throws IOException{
        return BFSDriver(inputList);
    }

    /**
     * Get the distance of every node in the graph to Philosophy
     * @return A Hash Map where the key is the node and the value is the distance to philosophy.
     * @throws IOException when any link is unreachable.
     */
    public HashMap<Node, Integer> getAllDistances() throws IOException{
        return BFSDriver(this.adjList.keySet());
    }

    /**
     * A very interesting method which basically performs Breadth First Search to figure out the
     * distance of all the nodes to Philosophy.
     * But there is a tiny issue. This is a directed graph to maintain the invariants, Philosophy only has
     * incoming edges. But I want to start BFS at Philosophy and move outwards because it is the most central
     * node. So first we convert the directed graph to an undirected graph, and then we run BFS on it.
     * @param inputList
     * @return A Hash Map where the key is the node and the value is the distance to Philosophy.
     * @throws IOException when any link is unreachable.
     */
    public HashMap<Node, Integer>  BFSDriver(Collection<Node> inputList) throws IOException {

        Graph biDirectional = getBidrectional();


        HashMap<Node, Integer> returnable = new HashMap<>();
        HashMap<Node, Boolean> discovered = new HashMap<>();

        for(Node x: biDirectional.getAdjList().keySet()) {
            discovered.put(x, false);
        }
        Node startNode = new Node("https://en.wikipedia.org/wiki/Philosophy");
        ArrayList<ArrayList<Node>> levelStore = new ArrayList<>();
        ArrayList<Node> level0 = new ArrayList<>();
        level0.add(startNode);
        levelStore.add(level0);

        int i = 0;

        while(!levelStore.get(i).isEmpty()) {
            ArrayList<Node> nextLevel = new ArrayList<>();
            for (Node x: levelStore.get(i)) {
                ArrayList<Node> neighbours = biDirectional.getNeighbours(x);
                for (Node y: neighbours) {
                    System.out.println(y.getPageName());
                    if (!discovered.get(y)) {
                        if (inputList.contains(y)) {
                            returnable.put(y, i+1);
                        }
                        discovered.put(y, true);
                        nextLevel.add(y);
                    }
                }
            }

            levelStore.add(nextLevel);
            i++;
        }
        if(inputList.contains(startNode)) {
            returnable.put(startNode, 0);
        }

        return returnable;


    }

    /**
     * Convert a directed graph to an undirected graph.
     * @return undirected graph
     */
    private Graph getBidrectional() {
        Graph returnable = new Graph();
        for(Node x: adjList.keySet()) {
            returnable.addNode(x);
            ArrayList<Node> neighbours = getNeighbours(x);
            for(Node y: neighbours) {
                returnable.addNode(y);
                returnable.addEdge(x, y);
                returnable.addEdge(y, x);
            }
        }
        return returnable;
    }

    /**
     * This method allows us to get the node(Wikipedia Pages) which act like funnels to Philosophy. This node
     * finds the nodes which have multiple incoming edges, and then returns those.
     * @return A hash map where the keys are the "Funnel" nodes and the value is the "importance" of those nodes
     * quantified by the number of incoming edges.
     * @throws IOException when any link is unreachable.
     */
    public HashMap<String, Integer> getFunnels() throws IOException {
        HashMap<String, Integer> funnel = new HashMap();
        HashMap<String, Integer> returnable = new HashMap();
        Set<Node> allNodes = adjList.keySet();
        for(Node x: allNodes) {

            ArrayList<Node> neighbours = getNeighbours(x);

            for(Node y: neighbours) {
                if (funnel.get(y.getUrl()) == null) {
                    funnel.put(y.getUrl(), 1);


                } else {
                    int currentCount = funnel.get(y.getUrl());
                    funnel.put(y.getUrl(), currentCount+1);
                    returnable.put(y.getPageName(), currentCount+1);
                }
            }
        }

        returnable.remove("Philosophy");
        return returnable;
    }

    /**
     * Get the string form of the adjacency list
     * @return a hash map which represent the adjacency list
     * @throws IOException when any link is unreachable.
     */
    public HashMap<String, ArrayList<String>> getStringForm() throws IOException {
        HashMap<String,ArrayList<String>> stringMap = new HashMap<>();

        Set<Node> allVertices = adjList.keySet();
        for(Node x: allVertices) {
            ArrayList<Node> neighbourList = getNeighbours(x);
            ArrayList<String> stringList = new ArrayList<>();
            for(Node y: neighbourList) {
                stringList.add(y.getPageName());
            }
            stringMap.put(x.getPageName(), stringList);
        }

        return stringMap;
    }
}
