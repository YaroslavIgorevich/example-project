package project.logic;

import java.util.ArrayList;

import javax.swing.JTextArea;

import project.graphics.ConsolePrinter;
import project.graphics.RandomGraphPanel;

/**
 * This class is created to generate random task graphs to load them to the 
 * computer systems with different topologies.
 * @author Yaroslav
 *
 */
public class GraphGenerator {
	
	/**
	 * Minimum graph node weight.
	 */
	private int minW;
	
	/**
	 * Maximum graph node weight.
	 */
	private int maxW;
	
	/**
	 * Number of nodes in the graph.
	 */
	private int nodesNumber;
	
	/**
	 * Specified correlation of the graph.
	 */
	private double correlation;	
	
	/**
	 * Minimum transition weight
	 */
	private int minL;
	
	/**
	 * Maximum transition weight
	 */
	private int maxL;
	
	/**
	 * Number of graphs for modeling.
	 */
	private int graphsNumber;
	
	/**
	 * Maximum possible number of links.
	 */
	private int maxLinksNumber;		
	
	/**
	 * A pointer to the object responsible for printing data into the console.
	 */
	private ConsolePrinter consolePrinter;
	
	/**
	 * A pointer to the random graph panel.
	 */
	private RandomGraphPanel randomGraphPanel;
	
	/**
	 * Main constructor.
	 * @param minW minimum graph node weight
	 * @param maxW maximum graph node weight
	 * @param nodesNumber number of nodes in the graph
	 * @param correlation specified correlation of the graph
	 * @param minL minimum transition weight
	 * @param maxL maximum transition weight
	 * @param graphsNumber number of graphs for modeling
	 * @param randomGraphPanel a pointer to the random graph panel
	 * @param console a pointer to the console object
	 */
	public GraphGenerator(int minW, int maxW, int nodesNumber, double correlation, int minL, int maxL, 
			int graphsNumber, RandomGraphPanel randomGraphPanel, JTextArea console) {		
		this.minW = minW;
		this.maxW = maxW;
		this.nodesNumber = nodesNumber;
		this.correlation = correlation;
		this.minL = minL;
		this.maxL = maxL;
		this.graphsNumber = graphsNumber;
		this.randomGraphPanel = randomGraphPanel;
		consolePrinter = new ConsolePrinter(console);
	}
	
	/**
	 * Generates parameters string to display on the console.
	 * @return parameters string
	 */
	private String generateParamMessage() {
		return "minW = " + minW + "\nmaxW = " + maxW + "\nNumber of nodes = " + 
				nodesNumber + "\ncorrelation = " + correlation + "\nminL = " + minL + 
			   "\nmaxL = " + maxL;
	}
	
	/**
	 * Checks input parameters.
	 * @return true if all parameters are right
	 */
	private boolean checkParameters() {
		boolean checkResult = true;
		if (minW >= maxW) {
			consolePrinter.printBlock("Error! minW >= maxW");
			checkResult = false;
		}
		if (nodesNumber == 0) {
			consolePrinter.printBlock("Error! Number of nodes should be more or equal than 1");
			checkResult = false;
		}
		if ((correlation <= 0) || (correlation >= 1)) {
			consolePrinter.printBlock("Error! Correlation is out of range (0 ... 1)");
			checkResult = false;
		}
		if (minL >= maxL) {
			consolePrinter.printBlock("Error! minL >= maxL");
			checkResult = false;
		}
		return checkResult;
	}
	
	/**
	 * Simple random value generator.
	 * @param min minimum value
	 * @param max maximum value
	 * @return random value
	 */
	public static int generateRandomValue(int min, int max) {
		int value = min + (int)(Math.random() * max);
		return value;
	}
	
	/**
	 * Calculates nodes weight sum by adding execution time of each node.
	 * @param gnodeList graph nodes list
	 * @return weight sum
	 */
	private int calculateNodesWeightSum(ArrayList<GNode> gnodeList) {
		int sum = 0;
		
		for (GNode gnode : gnodeList) {
			sum += gnode.getTExe();
		}
		return sum;
	}
	
	/**
	 * Randomly generates links weights based on the links weight sum and input parameters. 
	 * @param linkWeightSum links weights sum
	 * @return a list of links weights
	 */
	private ArrayList<Integer> generateLinkWeights(int linkWeightSum) {
		ArrayList<Integer> linkWeights = new ArrayList<>();
		maxLinksNumber = (nodesNumber - 1) * nodesNumber / 2;		
		if (linkWeightSum > maxLinksNumber * maxL) {
			int halfInterval = (maxL - minL) / 2 + 1;			
			maxL = linkWeightSum / maxLinksNumber + halfInterval;
			minL = linkWeightSum / maxLinksNumber - halfInterval;						
		} 
		if (linkWeightSum < minL) {
			linkWeights.add(new Integer(linkWeightSum));
			
			return linkWeights;
		}		
		int sumBuf = 0;		
		while (linkWeightSum != sumBuf) {
			int linkWeight = GraphGenerator.generateRandomValue(minL, maxL);
			linkWeights.add(linkWeight);
			sumBuf += linkWeight;
			if (sumBuf > linkWeightSum) {
				sumBuf -= linkWeight;
				linkWeights.remove(linkWeights.size() - 1);
				if ((linkWeightSum - sumBuf) < minL) {
					sumBuf -= linkWeights.get(linkWeights.size() - 1);
					linkWeights.remove(linkWeights.size() - 1);
				}
			}
			if (linkWeights.size() > maxLinksNumber) {
				linkWeights.clear();
				sumBuf = 0;
				
			}
		}		
		return linkWeights;
	}
	
	/**
	 * Generates single graph using input parameters.
	 */
	public void generate() {
		consolePrinter.clearConsole();
		if (checkParameters() == true) {
			Graph randomGraph = new Graph();
			ArrayList<GNode> bufNodeList = new ArrayList<>();
			consolePrinter.printBlock(generateParamMessage());			
			for (int i = 0; i < nodesNumber; i++) {
				int gnodeExeTime = generateRandomValue(minW, maxW);
				GNode gnode = new GNode(gnodeExeTime, i);
				bufNodeList.add(gnode);
			}
			randomGraph.setGnodeList(bufNodeList);
			int nodeWeightSum = calculateNodesWeightSum(bufNodeList);
			consolePrinter.printBlock("Node weight sum = " + nodeWeightSum);			
			int linkWeightSum = (int)(Math.round(nodeWeightSum * (1 / correlation - 1)));
			consolePrinter.printBlock("Link weight sum = " + linkWeightSum);			
			ArrayList<Integer> linkWeights = generateLinkWeights(linkWeightSum);
			randomGraph.placeRandomLinks(linkWeights);						
			randomGraphPanel.setGNodeList(bufNodeList);
			randomGraphPanel.setLinkList(randomGraph.getGlinkList());
			double calculatedCorrelation = (double)nodeWeightSum / (nodeWeightSum + linkWeightSum);
			consolePrinter.printBlock("Calculated correlation = " + calculatedCorrelation);
		}		
	}
	
	/**
	 * Prints matrix to the console.
	 * @param matrix with the calculated modeling parameters
	 * @param name label of the matrix
	 */
	public void printMatrix(double[][] matrix, String name) {		
		consolePrinter.println(name);		
		String matrixStr = "";
		
		for (int i = 0; i < matrix.length; i++) {
			matrixStr = "";
			for (int j = 0; j < matrix[0].length; j++) {				
				matrixStr += (String.format("%6.3f", matrix[i][j]) + "  ");				
			}
			consolePrinter.println(matrixStr);			
		}
	}
	
	/**
	 * Calculates average value of the array.
	 * @param array input array
	 * @return output average value
	 */
	public static double getAverage(double[] array) {
		double sum = 0;
		
		for (int i = 0; i < array.length; i++) {
			sum += array[i];
		}
		return sum / array.length;
	}
	
	/**
	 * Initiates list with all possible algorithms.
	 * @return a list of the algorithms
	 */
	private ArrayList<Algorythm> initiateAlgorithmsArray() {
		ArrayList<Algorythm> algorythms = new ArrayList<>();
		
		Algorythm alg1 = new Algorythm(1, 1);
		algorythms.add(alg1);
		Algorythm alg2 = new Algorythm(1, 12);
		algorythms.add(alg2);
		Algorythm alg3 = new Algorythm(1, 16);
		algorythms.add(alg3);
		Algorythm alg4 = new Algorythm(5, 1);
		algorythms.add(alg4);
		Algorythm alg5 = new Algorythm(5, 12);
		algorythms.add(alg5);
		Algorythm alg6 = new Algorythm(5, 16);
		algorythms.add(alg6);
		return algorythms;
	}
	
	/**
	 * This method is used to discover the efficiency of the different computer system
	 * topologies scaling on the large amount of random task graphs. 
	 * Output parameters:
	 * 1. Kp  - acceleration factor.
	 * 2. Ke  - effectiveness ratio.
	 * 3. Kea - effectiveness ratio of algorithm.
	 * @param comSys computer system to model 
	 */
	public void generateMultipleGraphs(ComputerSystem comSys) {		
		ArrayList<Algorythm> algorythms = initiateAlgorithmsArray();
		
		double[][] matKp = new double[17][algorythms.size() * 3];
		double[][] matKe = new double[17][algorythms.size() * 3];
		double[][] matKea = new double[17][algorythms.size() * 3];
		
		for (int n = 16; n <= 48; n += 16) {			
			nodesNumber = n;			
			
			for (int k = 10; k <= 90; k+=5) {
				
				correlation = k / 100.0;								
				
				for (int a = 0; a < algorythms.size(); a++) {
					
					Algorythm alg = algorythms.get(a);
					
					int queueType = alg.getQueueType();
					int assignType = alg.getAssignType();
					
					double[] bufKp = new double[5];
					double[] bufKe = new double[5];
					double[] bufKea = new double[5];					
					
					for (int i = 0; i < 5; i++) {	
						
						Graph randomGraph = new Graph();
						ArrayList<GNode> bufNodeList = new ArrayList<>();						
						for (int j = 0; j < nodesNumber; j++) {
							int gnodeExeTime = generateRandomValue(minW, maxW);
							GNode gnode = new GNode(gnodeExeTime, j);
							bufNodeList.add(gnode);
						}
						randomGraph.setGnodeList(bufNodeList);
						int nodeWeightSum = calculateNodesWeightSum(bufNodeList);					
						int linkWeightSum = (int)(Math.round(nodeWeightSum * (1 / correlation - 1)));			
						ArrayList<Integer> linkWeights = generateLinkWeights(linkWeightSum);
						randomGraph.placeRandomLinks(linkWeights);						
						
						randomGraph.generateQueue(queueType, false);					
						comSys.loadTaskGraph(randomGraph, assignType, 1, false);		
						
						int Tn = comSys.findTotalScheduleTime();
						bufKp[i] = (double)randomGraph.calculateOneProcessorExeTime() / Tn;
						bufKe[i] = bufKp[i] / comSys.getNodeList().size();
						bufKea[i] = (double)randomGraph.calculateCriticalTime() / Tn;																			
									
					}	
					
					matKp[k / 5 - 2][a + (n / 16 - 1) * algorythms.size()] = getAverage(bufKp);
					matKe[k / 5 - 2][a + (n / 16 - 1) * algorythms.size()] = getAverage(bufKe);
					matKea[k / 5 - 2][a + (n / 16 - 1) * algorythms.size()] = getAverage(bufKea);
				}				
			}
		}
		printMatrix(matKp, "Kp");
		printMatrix(matKe, "Ke");
		printMatrix(matKea, "Kea");
	}	
}
