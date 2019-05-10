import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class hw5_ui implements ActionListener{

	JFrame frame = new JFrame();
	JFrame start = new JFrame();
	JFrame fun_frame = new JFrame();
	JTextField input;
	JButton run;
	JButton exit;
	JButton start_exit;
	JButton visual;
	JButton get_fun;
	JButton a_driver;
	JTextField start_in;
	JLabel start_label;
	JTextArea queries;
	JLabel q_label;
	JTextArea numbers;
	JLabel n_label;
	JTextArea dis_path;
	JLabel p_label;
	JTextArea dis_fun;
	JLabel f_label;

	String exit_node = "";

	Scraper scraper;

	hw5_ui(){



		start_exit = new JButton("Continue");
		start_exit.setBounds(125,200,150,30);
		start_exit.addActionListener(this);

		start_in = new JTextField();
		start_in.setBounds(75, 100, 250, 20);

		start_label = new JLabel("Enter an Exit Node Link:");
		start_label.setBounds(120,75,250, 20);

		input = new JTextField();
		input.setBounds(20, 20, 250, 20);

		run = new JButton("Run");
		run.setBounds(20, 45, 30, 30);
		run.addActionListener(this);

		visual = new JButton("Show Graph");
		visual.setBounds(55, 45, 100, 30);
		visual.addActionListener(this);

		get_fun = new JButton("Get Funnels");
		get_fun.setBounds(55, 80, 100, 30);
		get_fun.addActionListener(this);

		a_driver = new JButton("Generate Random Graph");
		a_driver.setBounds(55, 115, 200, 30);
		a_driver.addActionListener(this);
		
		exit = new JButton("Exit");
		exit.setBounds(20, 80, 30, 30);
		exit.addActionListener(this);

		q_label = new JLabel("Previous Queries:");
		q_label.setBounds(280, 20, 250, 20);

		queries = new JTextArea();
		queries.setBounds(280, 45, 300, 570);
		queries.setEditable(false);

		n_label = new JLabel("Length of path to Exit Node:");
		n_label.setBounds(580, 20, 250, 20);

		numbers = new JTextArea();
		numbers.setBounds(620, 45, 100, 570);
		numbers.setEditable(false);

		p_label = new JLabel("Path from previous Query:");
		p_label.setBounds(820, 20, 300, 20);

		dis_path = new JTextArea();
		dis_path.setBounds(820, 45, 500, 570);
		dis_path.setEditable(false);

		dis_fun = new JTextArea();
		dis_fun.setBounds(20,40 , 360, 560);
		dis_fun.setEditable(false);

		f_label = new JLabel("List of Funnels in Graph:");
		f_label.setBounds(20, 20, 300, 20);

		frame.add(input);
		frame.add(run);
		frame.add(exit);
		frame.add(q_label);
		frame.add(queries);
		frame.add(n_label);
		frame.add(numbers);
		frame.add(p_label);
		frame.add(dis_path);
		frame.add(visual);
		frame.add(get_fun);
		frame.add(a_driver);

		frame.setSize(1400,600);
		frame.setLayout(null);
		frame.setVisible(false);

		start.add(start_exit);
		start.add(start_in);
		start.add(start_label);

		start.setSize(400, 400);
		start.setLayout(null);
		start.setVisible(true);

		fun_frame.add(dis_fun);
		fun_frame.add(f_label);

		fun_frame.setSize(400,600);
		fun_frame.setLayout(null);
		fun_frame.setVisible(false);


	}

	public void actionPerformed(ActionEvent e){

		if(e.getSource() == run){
			Node currentInput = new Node(input.getText());
			String user_in = input.getText();

			frame.remove(dis_path);

			dis_path = new JTextArea();
			dis_path.setBounds(820, 45, 500, 570);
			dis_path.setEditable(false);

			frame.add(dis_path);

			try {
				ArrayList<Node> path = scraper.getPath(user_in);
				numbers.append(Integer.toString(path.size())+"\n");

				for(Node x: path){
					dis_path.append(x.getPageName()+"\n");
				}

			} catch (Exception exception){
				numbers.append("invalid query\n");
				dis_path.append("none\n");
			}

			try {
				queries.append(currentInput.getPageName()+"\n");
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			input.setText("");			

		}else if(e.getSource() == exit){
			System.exit(0);

		}else if(e.getSource() == start_exit){
			exit_node = start_in.getText();
			scraper = new Scraper(exit_node);

			start.setVisible(false);
			frame.setVisible(true);


		}else if(e.getSource() == visual){

			try{
				graphVisualizer.visualize(scraper.getGraph());
			} catch(Exception exception2){
				System.out.println(exception2);
			}

		}else if(e.getSource() == get_fun){
			fun_frame.remove(dis_fun);

			dis_fun = new JTextArea();
			dis_fun.setBounds(20,40 , 360, 560);
			dis_fun.setEditable(false);

			Graph graph = scraper.getGraph();
			HashMap<String, Integer> funnels = null;
			try {
				funnels = graph.getFunnels();
			} catch (IOException ex) {
				ex.printStackTrace();
			}

			for(Map.Entry<String, Integer> i: funnels.entrySet()){
				dis_fun.append(i.getKey()+": Edges in = "+i.getValue()+"\n");
			}

			fun_frame.add(dis_fun);
			fun_frame.setVisible(true);

		}else if(e.getSource() == a_driver){
			try {
				AnalysisDriver.main(null);
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}

	}

	/**
	 * Main class which runs the UI.
	 * @param args
	 */
	public static void main(String[] args){

		new hw5_ui();


	}



}
