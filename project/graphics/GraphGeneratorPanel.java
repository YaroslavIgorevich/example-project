package project.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import project.logic.ComputerSystem;
import project.logic.GraphGenerator;

/**
 * A component, which contains random graphs generation tools.
 * @author Yaroslav
 *
 */
public class GraphGeneratorPanel extends JPanel {	
	
	/**
	 * A panel, which contains input fields of the parameters.
	 */
	private JPanel inputPanel;
	
	/**
	 * A pointer to the panel, which graphically displays generated graph. 
	 */
	private RandomGraphPanel randomGraphPanel;
	
	/**
	 * A pointer to the console panel.
	 */
	private ConsolePanel consolePanel;
	
	/**
	 * This check box specifies whether we generate single graph or multiple
	 */
	private JCheckBox single;
	
	/**
	 * Generation button.
	 */
	private JButton generate;
	
	/**
	 * A pointer to the object of graph generator, which contains all the logic of
	 * the graphs generation.
	 */
	private GraphGenerator graphGenerator;
	
	/**
	 * A list to store all the input fields of the parameters.
	 */
	private ArrayList<JTextField> fields;
	
	/**
	 * A list to store all the labels of the input fields.
	 */
	private ArrayList<JLabel> labels;
	
	/**
	 * A pointer to the computer system panel.
	 */
	private ComputerSystemPanel computerSystemPanel;
	
	/**
	 * Main constructor.
	 * @param computerSystemPanel this field is required to access to the computer system properties
	 * @param graphPanel a pointer to the graph panel
	 */
	public GraphGeneratorPanel(ComputerSystemPanel computerSystemPanel, GraphPanel graphPanel) {	
		this.computerSystemPanel = computerSystemPanel;		
		
		setLayout(new BorderLayout());
					
		inputPanel = new JPanel();		
		randomGraphPanel = new RandomGraphPanel();
		randomGraphPanel.setBackground(Color.WHITE);
		
		consolePanel = new ConsolePanel();
		
		fields = new ArrayList<>(); 
		labels = new ArrayList<>();
		
		labels.add(new JLabel("minW"));
		labels.add(new JLabel("maxW"));
		labels.add(new JLabel("Number of nodes"));
		labels.add(new JLabel("Correlation"));
		labels.add(new JLabel("minL"));
		labels.add(new JLabel("maxL"));
		labels.add(new JLabel("Number of graphs"));
		
		for (int i = 0; i < 7; i++) {
			JTextField field = new JTextField(5);
			fields.add(field);
			inputPanel.add(labels.get(i));
			inputPanel.add(field);
		}
		
		JLabel singleLabel = new JLabel("Single");
		inputPanel.add(singleLabel);
		
		single = new JCheckBox();
		single.setToolTipText("Generate and display single graph");
		single.setSelected(true);
		inputPanel.add(single);
		
		fields.get(0).setText("1");
		fields.get(1).setText("5");
		fields.get(2).setText("4");
		fields.get(3).setText("0.5");
		fields.get(4).setText("1");
		fields.get(5).setText("50");
		fields.get(6).setText("10");				
		
		generate = new JButton("Generate");
		generate.setToolTipText("Generate single or multiple graphs");
		GenerateGraphAction gga = new GenerateGraphAction();
		generate.addActionListener(gga);	
		
		inputPanel.add(generate);		
		
		add(inputPanel, BorderLayout.NORTH);
		add(randomGraphPanel, BorderLayout.CENTER);
		add(consolePanel, BorderLayout.SOUTH);		
	}
	
	/**
	 * Action class of the generation button.
	 * @author Yaroslav
	 *
	 */
	private class GenerateGraphAction implements ActionListener {
		
		public void actionPerformed(ActionEvent event) {
			int minW = Integer.parseInt(fields.get(0).getText());
			int maxW = Integer.parseInt(fields.get(1).getText());
			int nodesNumber = Integer.parseInt(fields.get(2).getText());
			double correlation = Double.parseDouble(fields.get(3).getText());
			int minL = Integer.parseInt(fields.get(4).getText());
			int maxL = Integer.parseInt(fields.get(5).getText());
			int graphsNumber = Integer.parseInt(fields.get(6).getText());
			
			graphGenerator = new GraphGenerator(minW, maxW, nodesNumber, correlation, minL, maxL, graphsNumber, 
					randomGraphPanel, consolePanel.getConsole());
			
			if (single.isSelected() == true) {
				graphGenerator.generate();
			} else {				
				ComputerSystem comSys = computerSystemPanel.getComputerSystem();
				int returnCode = comSys.checkSystem();
				
				switch(returnCode) {
					case 0:
						graphGenerator.generateMultipleGraphs(computerSystemPanel.getComputerSystem());
						break;
					case 1:
						JOptionPane.showMessageDialog(null, "Add at least one node in the system!", "Error", JOptionPane.WARNING_MESSAGE);
						break;
					case 2:
						JOptionPane.showMessageDialog(null, "The computer system must be connected!", "Error", JOptionPane.WARNING_MESSAGE);
						break;
				}							
			}			
		}
	}	
}
