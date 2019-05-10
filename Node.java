import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Node {
    String url;

    public Node(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPageName() throws IOException {
        if(this.url.equals("Overlap")) {
            return "Overlap starts here";
        }
        Document doc = Jsoup.connect(this.url).get();
        String current = doc.title();
        String[] currentArr = current.split("-");
        current = currentArr[0];
        current = current.trim();
        return current;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(url, node.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }

    @Override
    public String toString() {
        String x = null;
        try {
            x = this.getPageName();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Node{" +
                "Name of page='" + x + '\'' +
                '}';
    }
}
