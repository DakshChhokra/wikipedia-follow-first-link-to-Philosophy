import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ScraperTests {



    @Test
    public void baseCase() throws IOException {

        Scraper scraper = new Scraper("placeholder");
        ArrayList<Node> checker = scraper.getPath("https://en.wikipedia.org/wiki/Philosophy");;


        System.out.println("Test 1: Input is philosophy. Same as output");
        System.out.println("The size of the path is: " + checker.size());
        System.out.println("Path is: ");
        for(Node x: checker) {
            System.out.println(x);
        }
        System.out.println("***************************************************************************");
    }

    @Test
    public void semanticsIsInput() throws IOException {

        Scraper scraper = new Scraper("placeholder");
        ArrayList<Node> checker = scraper.getPath("https://en.wikipedia.org/wiki/Semantics");

        System.out.println("Test 2: Input is Semantics. No Overlap");
        System.out.println("The size of the path is: " + checker.size());
        System.out.println("The Path is: ");
        for(Node x: checker) {
            System.out.println(x);
        }
        assertTrue(true);
        System.out.println("***************************************************************************");

    }

    @Test
    public void overlapTest() throws IOException {
        Scraper scraper = new Scraper("placeholder");
        scraper.getPath("https://en.wikipedia.org/wiki/Semantics");


        ArrayList<Node> checker = scraper.getPath("https://en.wikipedia.org/wiki/Brisingr");



        System.out.println("Test 3: Input is Brisingr. Overlaps with Science");
        System.out.println("The size of the path is: " + checker.size());
        System.out.println("The Path is: ");
        for(Node x: checker) {
            System.out.println(x);
        }
        assertTrue(true);
        System.out.println("The size of the path is: " + checker.size());
        System.out.println("***************************************************************************");

    }

    @Test
    public void weirdPage() throws IOException {
        Scraper scraper = new Scraper("placeholder");


        ArrayList<Node> checker = scraper.getPath("https://en.wikipedia.org/wiki/Brisingr");



        System.out.println("Test 4: Input is Brisingr.");
        System.out.println("The size of the path is: " + checker.size());
        System.out.println("The Path is: ");
        for(Node x: checker) {
            System.out.println(x);
        }
        assertTrue(true);
        System.out.println("***************************************************************************");

    }



    @Test
    public void smallConnection() throws IOException {
        Scraper scraper = new Scraper("placeholder");


        ArrayList<Node> checker = scraper.getPath("https://en.wikipedia.org/wiki/Data");



        System.out.println("Test 5: Input is Data.");
        System.out.println("The size of the path is: " + checker.size());
        System.out.println("The Path is: ");
        for(Node x: checker) {
            System.out.println(x);
        }
        assertTrue(true);
        System.out.println("***************************************************************************");

    }

    @Test
    public void testingLengths() throws IOException {
        Scraper scraper = new Scraper("placeholder");


        ArrayList<Node> checker = scraper.getPath("https://en.wikipedia.org/wiki/Data");



        System.out.println("Test 6: Testing the length arrayList");
        System.out.println("The size of the path is: " + checker.size());
        System.out.println("The Path is: ");
        for(Node x: checker) {
            System.out.println(x);
        }

        assertEquals(checker.size(),
                scraper.getLengthOfOneParticularNode("https://en.wikipedia.org/wiki/Data"));
        assertEquals(checker.size(), (int) scraper.getSizeOfLength().get("https://en.wikipedia.org/wiki/Data"));
        assertEquals(checker.size(), scraper.getLengthOfOneParticularNode(new Node("https://en.wikipedia.org/wiki/Data")));
        System.out.println("***************************************************************************");

    }

    @Test
    public void testFunnels() throws IOException {
        Scraper scraper = new Scraper("placeholder");


        System.out.println("Test 7: Testing the funnels method");

        ArrayList<Node> checker;

        checker =  scraper.getPath("https://en.wikipedia.org/wiki/Semantics");
        System.out.println("The size of the path is: " + checker.size());
        System.out.println("The Path is: ");
        for(Node x: checker) {
            System.out.println(x);
        }

        checker =  scraper.getPath("https://en.wikipedia.org/wiki/CGP_Grey");
        System.out.println("The size of the path is: " + checker.size());
        System.out.println("The Path is: ");
        for(Node x: checker) {
            System.out.println(x);
        }

        checker =  scraper.getPath("https://en.wikipedia.org/wiki/Avengers:_Endgame");
        System.out.println("The size of the path is: " + checker.size());
        System.out.println("The Path is: ");
        for(Node x: checker) {
            System.out.println(x);
        }



        HashMap<String, Integer> funnels = scraper.graph.getFunnels();
        Set<String> allFun = funnels.keySet();
        for(String x: allFun) {
            System.out.println("Funnel is: " + x + " and its frequency is " + funnels.get(x));
        }

        System.out.println("***************************************************************************");

    }



}
