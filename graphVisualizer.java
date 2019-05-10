
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


public class graphVisualizer {


    /**
     * Main visualizer. Takes a graph as the input. Called after a graph is created from the various
     * paths.
     *
     * @param inputGraph
     * @throws IOException
     */
    public static void visualize(Graph inputGraph) throws IOException {
        SingleGraph graph = new SingleGraph("Main Graph");

        HashMap<String, ArrayList<String>> input = inputGraph.getStringForm();

        for(String x: input.keySet()) {
            graph.addNode(x);
        }

        int i = 0;
        for(String x: input.keySet()) {
            System.out.println("The x is " + x);
            ArrayList<String> neighbours = input.get(x);
            for(String y: neighbours) {
                System.out.println("The y is " + y);
                graph.addEdge(Integer.toString(i), x, y);
                i++;
            }
        }

        HashMap<String, Integer> funnels = inputGraph.getFunnels();
        Set<String> allFun = funnels.keySet();
        System.out.println("Funnels are: ");
        for(String c: allFun) {
            System.out.println(c);
        }


        for (Node node : graph) {
            node.addAttribute("ui.label", node.getId());
            if (node.getId().equals("Philosophy")) {
                node.setAttribute( "ui.style", "fill-color: rgb(255,0,0);");
                node.addAttribute("ui.size", 50);
            } else if (allFun.contains(node.getId())) {
                node.setAttribute( "ui.style", "fill-color: rgb(0,0,255);");
            } else {
                node.setAttribute( "ui.style", "fill-color: rgb(0,255,0);");
            }
        }

        graph.display();
    }


}
