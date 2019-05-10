import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AnalysisDriver {
    public static void main(String[] args) throws IOException {
        Scraper scraper = new Scraper("placeholder");


        PrintWriter out = new PrintWriter("csv_graph.csv");

        out.println("Source, Dist,");
        out.print("\n");

        for(int i = 0; i < 50; i++) {

            String link = "https://en.wikipedia.org/wiki/Special:Random";
            Connection.Response response = Jsoup.connect(link).followRedirects(true).execute();
            link = response.url().toString();

            ArrayList<Node> path = scraper.getPath(link);

            out.println(link+", "+path.size()+",");

            System.out.println("^^^^^^");
            System.out.println(i+1);
            System.out.println("^^^^^^");

        }


        out.println("Funnel, Edges In,");
        out.print("\n");

        HashMap<String, Integer> funnels = scraper.graph.getFunnels();

        for(Map.Entry<String, Integer> k: funnels.entrySet()){
            out.println(k.getKey()+", "+k.getValue()+",");
        }

        out.close();
        graphVisualizer.visualize(scraper.getGraph());
    }
}
