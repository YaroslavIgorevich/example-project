package project.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JTextArea;

/**
 * This class describes acyclic directed graph, which represents the task to solve
 * on the computer system.
 * @author Yaroslav
 *
 */
public class Graph {
	
	/**
	 * A list of the graph nodes.
	 */
	private ArrayList<GNode> gnodeList;
	
	/**
	 * A list of the graph transitions.
	 */
	private ArrayList<GLink> glinkList;
	
	/**
	 * A list to store cycles.
	 */
	private ArrayList<ArrayList<GNode>> cycles;
	
	/**
	 * A list to form queue.
	 */
	private ArrayList<GNode> queue;	
	
	/**
	 * A pointer to the console text area.
	 */
	private JTextArea console;
	
	/**
	 * Critical path time of the graph.
	 */
	private int cpTime;
	
	/**
	 * Number of the nodes on the critical path.
	 */
	private int cpGNodeNumber;	
	
	/**
	 * Additional constructor.
	 */
	public Graph() {
		gnodeList = new ArrayList<>();
		glinkList = new ArrayList<>();
	}
	
	/**
	 * Main constructor.
	 * @param console a pointer to the conosole object
	 */
	public Graph(JTextArea console) {
		gnodeList = new ArrayList<>();
		glinkList = new ArrayList<>();
		cycles = new ArrayList<>();
		this.console = console;
	}
	
	/**
	 * Checks whether task graph contains any nodes or not.
	 * @return true if it hasn't
	 */
	public boolean isEmpty() {
		if (gnodeList.isEmpty()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks the overall system on the 3 criteria:
	 * - task graph emptiness
	 * - computer system graph emptiness
	 * - computer system connectivity
	 * @param comSys
	 * @return
	 */
	public int checkSystemAndGraph(ComputerSystem comSys) {
		if (isEmpty()) {
			return 3;
		}
		if ((boolean)checkCycles()[0] == true) {
			return 4;
		}
		return comSys.checkSystem();
	}
	
	/**
	 * Accessor to the queue field.
	 * @return a pointer to the queue field.
	 */
	public ArrayList<GNode> getQueue() {
		return queue;
	}
	
	/**
	 * This method is used to set a value of the nextIndex field.
	 * It's useful when we delete any graph nodes from the workspace.
	 */
	public void setNextIndex() {
		int maxIndex = 0;		
		for (GNode gnode : gnodeList) {
			if (gnode.getIndex() > maxIndex) {
				maxIndex = gnode.getIndex();
			}
		}
		maxIndex++;
		GNode.nextIndex = maxIndex;
	}	
	
	/**
	 * Searches for transition using source and destination nodes
	 * @param srcNode source node
	 * @param destNode destination node
	 * @return found transition
	 */
	public GLink findGLink(GNode srcNode, GNode destNode) {
		for (GLink glink : glinkList) {
			if (glink.getStart().equals(srcNode) && glink.getEnd().equals(destNode)) {
				return glink;
			}
		}
		return null;
	}
	
	/**
	 * Searches graph node by specified unique index.
	 * @param index index of the node
	 * @return found node
	 */
	private GNode getGNodeByIndex(int index) {
		for (GNode gnode : gnodeList) {
			if (gnode.getIndex() == index) {
				return gnode;
			}
		}
		return null;
	}
	
	/**
	 * This method checks whether specified source and destination nodes are connected or not. 
	 * @param start source node
	 * @param end destination node
	 * @return true if they are connected
	 */
	private boolean areConnected(GNode start, GNode end) {
		for (GLink glink : glinkList) {			 
			if (glink.getStart().equals(start) && glink.getEnd().equals(end)) return true;
			if (glink.getEnd().equals(start) && glink.getStart().equals(end)) return true;
		}
		return false;
	}
	
	/**
	 * Calculates execution time for 1 processor computer system.
	 * @return execution time
 	 */
	public int calculateOneProcessorExeTime() {
		int exeTime = 0;
		for (GNode gnode : gnodeList) {
			exeTime += gnode.getTExe();
		}
		return exeTime;
	}
	
	/**
	 * This method generates information message about found cycles
	 * @return information message string
	 */
	private String generateCyclesInfo() {
		String info = "";
		int currentCycleNumber = 1;
		
		for (ArrayList<GNode> cycle : cycles) {
			info += ("Cycle #" + currentCycleNumber + ": ");
			for (GNode gnode : cycle) {
				info += (gnode.getIndex() + " ");
			}
			info += "\n";			
			currentCycleNumber++;
		}		
		return info;
	}
	
	/**
	 * Sorts graph nodes list by priority.
	 * @param gnodeList a list of the graph nodes
	 */
	private void sortGnodeListByPriority(ArrayList<GNode> gnodeList) {
		boolean sorted = false;
		
		while (sorted == false) {
			sorted = true;
			for (int i = 0; i < gnodeList.size() - 1; i++) {
				GNode currentGNode = gnodeList.get(i);
				GNode nextGNode = gnodeList.get(i + 1);
				if (currentGNode.getPriority() < nextGNode.getPriority()) {
					Collections.swap(gnodeList, i, i + 1);
					sorted = false;
				}
			}
		}
	}
	
	/**
	 * In this method we chose the highest critical path time among the graph nodes.
	 * @return critical path time value
	 */
	public int calculateCriticalTime() {
		int[] criticalTimes = new int[gnodeList.size()];
		for (int i = 0; i < gnodeList.size(); i++) {
			GNode gnode = gnodeList.get(i);
			gnode.calculateCriticalPaths(0, 1);
			criticalTimes[i] = gnode.getCPTime();			
		}
		return ComputerSystem.findArrayMax(criticalTimes);
	}
	
	/**
	 * Calculates critical paths by time and nodes number.
	 */
	private void calculateCriticalPaths() {		
		for (GNode gnode : gnodeList) {
			gnode.calculateCriticalPaths(0, 1);			
		}
		
		cpTime = gnodeList.get(0).getCPTime();
		cpGNodeNumber = gnodeList.get(0).getCPGnodeNumber();
		
		for (int i = 1; i < gnodeList.size(); i++) {
			int gnodeCPTime = gnodeList.get(i).getCPTime();			
			int gnodeCPGnodesNumber = gnodeList.get(i).getCPGnodeNumber();
			if (gnodeCPTime > cpTime) {
				cpTime = gnodeCPTime;
			}
			if (gnodeCPGnodesNumber > cpGNodeNumber) {
				cpGNodeNumber = gnodeCPGnodesNumber;
			}
		}
	}
	
	/**
	 * Calculates priorities of each graph node(queue type code 1)
	 */
	private void calculatePriorities() {
		for (GNode gnode : gnodeList) {
			gnode.calculatePriority(cpTime, cpGNodeNumber);			
		}
	}
	
	/**
	 * Creates the links with the specified link weights and places them to the graph.
	 * @param linkWeightList list of the link weights
 	 */
	public void placeRandomLinks(ArrayList<Integer> linkWeightList) {		
		for (Integer weight : linkWeightList) {
			boolean placed = false;
			
			while (placed == false) {
				int random = GraphGenerator.generateRandomValue(0, gnodeList.size());				
				GNode sourceNode = getGNodeByIndex(random);
				GNode destNode = getGNodeByIndex(GraphGenerator.generateRandomValue(0, gnodeList.size()));				
				if ((areConnected(sourceNode, destNode) == false) && (!sourceNode.equals(destNode))) {					
					GLink link = new GLink(weight, sourceNode, destNode);
					destNode.checkCycles(new ArrayList<GNode>());
					if(destNode.getCyclesCheckCode() == 0) {
						glinkList.add(link);						
						placed = true;
					} else {
						sourceNode.getNext().remove(destNode);
						destNode.getPrev().remove(sourceNode);
					}
				}
			}			
		}
	}
	
	/**
	 * Check graph for cycles. It uses passage of the graph in depth in two directions.
	 * @return object array contains 2 elements:
	 * - flag whether graph contains cycles or not
	 * - cycles information if contains
	 */
	public Object[] checkCycles() {
		cycles.clear();
		Object[] cyclesInfo = new Object[2];
		boolean containsBorderGNodes = false;		
		
		for (GNode gnode : gnodeList) {
			if ((gnode.getNext().isEmpty()) || (gnode.getPrev().isEmpty())) {
				containsBorderGNodes = true;
				if (gnode.isEndNode()) {
					gnode.findCycles(new ArrayList<GNode>(), cycles, 0);
				} else if (gnode.isStartNode()) {
					gnode.findCycles(new ArrayList<GNode>(), cycles, 1);
				}								
			} 
		}
		
		if (containsBorderGNodes == true) {
			if (cycles.isEmpty()) {
				cyclesInfo[0] = false;
				return cyclesInfo;
			} else {
				cyclesInfo[0] = true;
				cyclesInfo[1] = generateCyclesInfo();
				return cyclesInfo;
			}
		} else {
			gnodeList.get(0).findCycles(new ArrayList<GNode>(), cycles, 0);
			cyclesInfo[0] = true;
			cyclesInfo[1] = generateCyclesInfo();
			return cyclesInfo;
		}		
	}
	
	/**
	 * This method is used to generate queues with the specified queue type.
	 * @param queueType a value of the queue type code (1, 12, 16)
	 * @param printInfo is used to not print info during large random graph generations
	 */
	public void generateQueue(int queueType, boolean printInfo) {
		queue = (ArrayList<GNode>)gnodeList.clone(); 
		
		if (queueType == 1) {
			calculateCriticalPaths();
			calculatePriorities();
			sortGnodeListByPriority(queue);
		} else if (queueType == 12) {			
			Collections.sort(queue, new Graph.SortByOutgoingGLinks());					
		} else if (queueType == 16) {
			for (GNode gnode : gnodeList) {
				gnode.calculateCriticalPaths(1, 16);				
			}					
			Collections.sort(queue, new Graph.SortByCriticalTime());				
		}	
		if (printInfo == true) {
			console.setText(generateQueueString(queue, queueType));
		}		
	}
	
	/**
	 * This method generates information message about generated queue
	 * @param queue generated queue
	 * @param queueType code of queue type
	 * @return information string
	 */
	private String generateQueueString(ArrayList<GNode> queue, int queueType) {
		String blockSeparator = "--------------------------------\n";
		String queueSeparator = " | ";
		
		String queueString = "Queue (type = " + queueType + ")\n";
		queueString += blockSeparator;
		
		if (queueType == 1) {
			queueString += ("T = " + cpTime + "\n");
			queueString += ("N = " + cpGNodeNumber + "\n");
			queueString += blockSeparator;
			for (GNode gnode : queue) {
				queueString += ("T" + gnode.getIndex() + " = " + gnode.getCPTime() + "\n");
				queueString += ("N" + gnode.getIndex() + " = " + gnode.getCPGnodeNumber() + "\n");
				queueString += blockSeparator;
			}
			for (GNode gnode : queue) {
				if (queue.indexOf(gnode) == gnodeList.size() - 1) {
					queueSeparator = "";
				}
				queueString += ("G" + gnode.getIndex() + "(" + gnode.getPriority() + ")" + queueSeparator);
			}
		} else if (queueType == 12) {
			for (GNode gnode : queue) {
				if (queue.indexOf(gnode) == gnodeList.size() - 1) {
					queueSeparator = "";
				}
				queueString += ("G" + gnode.getIndex() + "(" + gnode.getNext().size() + ")" + queueSeparator);
			}
		} else if (queueType == 16) {
			for (GNode gnode : queue) {
				if (queue.indexOf(gnode) == gnodeList.size() - 1) {
					queueSeparator = "";
				}
				queueString += ("G" + gnode.getIndex() + "(" + gnode.getCPTime() + ")" + queueSeparator);
			}
		}
		return queueString;		
	}
	
	/**
	 * Accessor to the graph nodes list field.
	 * @return graph nodes list
	 */
	public ArrayList<GNode> getGnodeList() {
		return gnodeList;
	}
	
	/**
	 * Sets a value of the graph nodes list field.
	 * @param gnodeList graph nodes list
	 */
	public void setGnodeList(ArrayList<GNode> gnodeList) {
		this.gnodeList = gnodeList;
	}
	
	/**
	 * Accessor to the transitions list field.
	 * @return transitions list
	 */
	public ArrayList<GLink> getGlinkList() {
		return glinkList;
	}
	
	/**
	 * Sets a value of the transitions list field.
	 * @param glinkList transitions list 
	 */
	public void setGlinkList(ArrayList<GLink> glinkList) {
		this.glinkList = glinkList;
	}
	
	/**
	 * This class comparator sorts list of graph nodes by the number of outgoing
	 * transitions.
	 * @author Yaroslav
	 *
	 */
	public static class SortByOutgoingGLinks implements Comparator<GNode> {
		
		public int compare(GNode node1, GNode node2) {
			return node2.getNext().size() - node1.getNext().size();
		}
	}
	
	/**
	 * This class comparator sorts list of graph nodes by critical path time.
	 * @author Yaroslav
	 *
	 */
	public static class SortByCriticalTime implements Comparator<GNode> {

		@Override
		public int compare(GNode node1, GNode node2) {
			return node1.getCPTime() - node2.getCPTime();
		}		
	}
}
