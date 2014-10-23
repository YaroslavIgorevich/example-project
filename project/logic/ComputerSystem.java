package project.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * This class describes the computer system. Contains scheduling algorithms
 * implementation based on the specified task graph. 
 * @author Yaroslav
 *
 */
public class ComputerSystem {
	
	/**
	 * Name of the computer system
	 */
	private String name;
	
	/**
	 * List of the computer system nodes.
	 */
	private ArrayList<SystemNode> nodeList;
	
	/**
	 * List of the computer system links.
	 */
	private ArrayList<SystemLink> linkList;
	
	/**
	 * A pointer to the task graph to schedule.
	 */
	private Graph taskGraph;
	
	/**
	 * Code of the algorithm(1 or 5)
	 */
	private int algorythmType;
	
	/**
	 * This flag specifies whether we use duplex physical 
	 * links or not
	 */
	private boolean duplex;
	
	/**
	 * Default processor physical links number.
	 */
	public static int processorLinkNumber = 1;
	
	/**
	 * Main constructor.
	 */
	public ComputerSystem() {
		nodeList = new ArrayList<>();
		linkList = new ArrayList<>();
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Makes a copy of the existing computer system object.
	 */
	public ComputerSystem clone() {
		ComputerSystem comSys = new ComputerSystem();
		comSys.setNodeList((ArrayList<SystemNode>)nodeList.clone());
		comSys.setLinkList((ArrayList<SystemLink>)linkList.clone());
		return comSys;
	}
	
	/**
	 * Auxiliary method to find maximum value of the array.
	 * Uses simple "Bubble" algorithm.
	 * @param array array to sort 
	 * @return sorted array
	 */
	public static int findArrayMax(int[] array) {
		int max = array[0];
		
		for (int i = 1;  i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
			}
		}
		return max;
	}
	
	/**
	 * This method searches for nextIndex value. It is useful
	 * when we delete existing nodes and then add new one.
	 */
	public void setNextIndex() {
		int maxIndex = 0;		
		for (SystemNode sysNode : nodeList) {
			if (sysNode.getIndex() > maxIndex) {
				maxIndex = sysNode.getIndex();
			}
		}
		maxIndex++;
		SystemNode.nextIndex = maxIndex;
	}
	
	/**
	 * Checks whether computer system contains at least 1 node or not.
	 * @return false if system doesn't contain any nodes
	 */
	private boolean isEmpty() {
		if (nodeList.isEmpty()) {
			return true;
		}
		return false;
	}
	
	/**
	 * This method check whether computer system graph is fully connected or not.
	 * @return true if graph is connected
	 */
	public boolean checkConnectivity() {
		for (SystemNode node : nodeList) {
			node.setUsed(false);
		}
		
		ArrayList<SystemNode> queue = new ArrayList<SystemNode>();
		int count = 0;
		queue.add(nodeList.get(0));
		nodeList.get(0).setUsed(true);
				
		while (!queue.isEmpty()) {			
			SystemNode currentNode = queue.get(0);
			count++;
			
			ArrayList<SystemNode> neighbours = currentNode.getNeighbours();			
			
			for (SystemNode neighbour : neighbours) {
				if (neighbour.isUsed() == false) {					
					queue.add(neighbour);
					neighbour.setUsed(true);															
				}
			}			
			queue.remove(0);
		}		
		
		if (count == nodeList.size()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * This method checks computer system using both emptiness and 
	 * connectivity checking
	 * @return true if both checks are true
	 */
	public int checkSystem() {
		if (isEmpty()) {
			return 1;
		}
		if (checkConnectivity() == false) {
			return 2;
		}
		return 0; 
	}	
	
	/**
	 * Searches for total execution type on this system in tacts.
	 * @return total schedule time
	 */
	public int findTotalScheduleTime() {
		int[] lastTacts = new int[nodeList.size()];
		for (int i = 0; i < nodeList.size(); i++) {
			lastTacts[i] = nodeList.get(i).getLastTaskEndTime();
		}
		return findArrayMax(lastTacts);
	}
	
	/**
	 * Resets all the assigned task graph nodes of each computer system node.
	 * @param graph task graph
	 */
	private void resetAssignation(Graph graph) {
		for (SystemNode sysNode : nodeList) {
			sysNode.getScheduledNodes().clear();
			sysNode.getProcessorLinks().clear();
			for (int i = 0; i < processorLinkNumber; i++) {
				ProcessorLink pLink = new ProcessorLink(i);
				sysNode.addProcessorLink(pLink);
			}			
			sysNode.resetTacts();
		}
		
		for (GNode gnode : graph.getGnodeList()) {
			gnode.setAssigned(false);
		}
	}
	
	/**
	 * Selects task graph node from the formed queue.
	 * @param queue task graph queue
	 * @return selected graph node
	 */
	private GNode selectCurrentGNode(ArrayList<GNode> queue) {
		boolean selected = false;
		int i = 0;
		GNode currentGNode = null;
		
		while (selected == false) {
			currentGNode = queue.get(i);
			i++;
			if (currentGNode.ancestryAreAssigned() == true) {
				selected = true;
			}
		}
		return currentGNode;
	}
	
	/**
	 * Main method to load task graph on the computer system.
	 * @param graph task graph
	 * @param algorythm algorithm code
	 * @param linksNumber number of the physical links 
	 * @param duplex duplex/simplex flag
	 */
	public void loadTaskGraph(Graph graph, int algorythm, int linksNumber, boolean duplex) {
		algorythmType = algorythm;
		processorLinkNumber = linksNumber;
		this.duplex = duplex;
		resetAssignation(graph);		
		this.taskGraph = graph;
		ArrayList<GNode> taskQueue = graph.getQueue();
		
		@SuppressWarnings("unchecked")
		ArrayList<GNode> bufQueue = (ArrayList<GNode>)taskQueue.clone();
		calculateSystemNodesPriority();
		sortSystemNodesByPriority();		
				
		while (!bufQueue.isEmpty()) {
			GNode currentGNode = selectCurrentGNode(bufQueue);						
			if (currentGNode.isStartNode()) {
				assignIndependentGNode(currentGNode);			
			} else {
				assignDependentGNode(currentGNode);
			}					
			bufQueue.remove(currentGNode);
		}		
	}	
	
	/**
	 * Searches for system node, which has the smallest end time. 
	 * @return computer system node
	 */
	private SystemNode findEarliestSystemNode() {
		SystemNode earliestNode = nodeList.get(0);
		
		for (int i = 1; i < nodeList.size(); i++) {
			if (nodeList.get(i).getLastTaskEndTime() < earliestNode.getLastTaskEndTime()) {
				earliestNode = nodeList.get(i);
			}
		}
		return earliestNode;
	}
	
	/**
	 * Generates random index to chose system node.
	 * @return random system node
	 */
	private SystemNode chooseRandomProcessor() {
		int processorIndex = GraphGenerator.generateRandomValue(0, nodeList.size());
		return nodeList.get(processorIndex);
	}
	
	/**
	 * Assigns task graph node to a specified computer system node. 
	 * @param gnode task graph node
	 * @param sysNode computer system node
	 * @param startTime start time(first tact) of the graph node
	 * @param endTime end time(last tact) of the graph node
	 */
	private void assignGNodeProcessor(GNode gnode, SystemNode sysNode, int startTime, int endTime) {
		gnode.setStartTime(startTime);
		gnode.setEndTime(endTime);
		gnode.setAssigned(true);
		gnode.setAssignedSystemNode(sysNode);
		sysNode.addScheduledGNode(gnode);
		for (int i = startTime; i < endTime; i++) {
			sysNode.getTacts().get(i).setScheduledGNode(gnode);
		}
	}
	
	/**
	 * This method assigns a node from task graph, which is not depended on the other nodes.
	 * @param independentGNode independent graph node
	 */
	private void assignIndependentGNode(GNode independentGNode) {
		if (algorythmType == 1) {			
			SystemNode chosenProcessor = chooseRandomProcessor();
			int startTime = chosenProcessor.getLastTaskEndTime();
			int endTime = startTime + independentGNode.getTExe();
			assignGNodeProcessor(independentGNode, chosenProcessor, startTime, endTime); 
		} else if (algorythmType == 5) {
			if (emptySystemNodesInSystem() == true) {
				for (int i = nodeList.size() - 1; i >= 0; i--) {
					SystemNode sysNode = nodeList.get(i);
					if (sysNode.getScheduledNodes().isEmpty()) {					
						assignGNodeProcessor(independentGNode, sysNode, 0, independentGNode.getTExe());
						return;
					} 
				}
			} else {
				SystemNode earliestNode = findEarliestSystemNode();
				int lastTact = earliestNode.getLastTaskEndTime();			
				assignGNodeProcessor(independentGNode, earliestNode, lastTact, 
						lastTact + independentGNode.getTExe());			
			}
		}			
	}	
	
	/**
	 * Checks whether all ancestry nodes of the specified node are assigned or not.
	 * @param ancestry a list of the ancestry nodes
	 * @return true if all the ancestry are assigned
	 */
	private boolean areAssigned(ArrayList<GNode> ancestry) {
		boolean areAssigned = true;
		
		for (GNode gnode : ancestry) {
			if (!gnode.isAssigned()) {
				areAssigned = false;
			}
		}
		return areAssigned;
	}
	
	/**
	 * Searches for the biggest ancestry execution end time.
	 * @param ancestry a list of the ancestry nodes
	 * @return the "latest" ancestry end time(last tact)
	 */
	private int getAncestryEndTime(ArrayList<GNode> ancestry) {
		int endTime = ancestry.get(0).getEndTime();
		
		for (int i = 1; i < ancestry.size(); i++) {
			if (ancestry.get(i).getEndTime() > endTime) {
				endTime = ancestry.get(i).getEndTime();
			}
		}
		return endTime;
	}
	
	/**
	 * Searches for free system node based on the specified time.
	 * @param time this time should be less than time we are looking for
	 * @return a list of found system nodes
	 */
	private ArrayList<SystemNode> findFreeSystemNodes(int time) {
		ArrayList<SystemNode> freeSystemNodes = new ArrayList<>();
		
		for (SystemNode sysNode : nodeList) {
			if (sysNode.getLastTaskEndTime() <= time) {
				freeSystemNodes.add(sysNode);
			}
		}
		return freeSystemNodes;
	}
	
	/**
	 * Calculates the path time from one computer system node to another.
	 * @param path a path from one system node to another
	 * @param linkWeight weight of the transition taken from the task graph
	 * @return calculated path time
	 */
	private int calculatePathTime(ArrayList<SystemNode> path, int linkWeight) {
		return linkWeight * (path.size() - 1);
	}
	
	/**
	 * This method searches for system node, which has best "start time". It is based on the 
	 * neighboring destination algorithm with the preemption shipments.
	 * @param dependentGNode a node, which is depended of other nodes
	 * @return found system node
	 */
	private SystemNode selectBestSystemNode(GNode dependentGNode) {		
		ArrayList<GNode> ancestry = dependentGNode.getPrev();
		int startTime[] = new int[nodeList.size()];
		
		if (ancestry.size() == 1) {
			GNode ancestor = ancestry.get(0);
			for (int i = 0; i < nodeList.size(); i++) {
				SystemNode sysNode = nodeList.get(i);								
				startTime[i] = calculatePathTime(findShortestPath(ancestor.getAssignedSystemNode(), sysNode), 
						  taskGraph.findGLink(ancestor, dependentGNode).getTCom());				
			}
		} else {
			if (ComputerSystem.processorLinkNumber == 1) {
				for (int i = 0; i < nodeList.size(); i++) {
					SystemNode sysNode = nodeList.get(i);
					for (GNode ancestor : ancestry) {				
						startTime[i] += calculatePathTime(findShortestPath(ancestor.getAssignedSystemNode(), sysNode), 
								  taskGraph.findGLink(ancestor, dependentGNode).getTCom());
					}					
				}
			} else {
				for (int i = 0; i < nodeList.size(); i++) {
					SystemNode sysNode = nodeList.get(i);
					int[] pathLength = new int[ancestry.size()];
					
					for (int j = 0; j < ancestry.size(); j++) {	
						GNode ancestor = ancestry.get(j);
						pathLength[j] = calculatePathTime(findShortestPath(ancestor.getAssignedSystemNode(), sysNode), 
								  taskGraph.findGLink(ancestor, dependentGNode).getTCom());
					}
					startTime[i] = findArrayMax(pathLength);					
				}
			}
		}		
		
		int bestStartTime = startTime[0];
		int k = 0;
		
		for (int i = 1; i < startTime.length; i++) {
			if (startTime[i] < bestStartTime) {
				bestStartTime = startTime[i];
				k = i;
			}
		}		
		return nodeList.get(k);
	}	
	
	/**
	 * This method assigns transmission to a processor physical link.
	 * @param currentNode current computer system node
	 * @param nextNode next(in the path) computer system node
	 * @param iterationStartTime start time of iteration
	 * @param iterationEndTime end time of iteration
	 * @param linkWeight weight of the link from task graph
	 * @param ancestor task graph ancestor node
	 * @param dependentGNode successor of the ancestor
	 * @return best end time of the transmission
	 */
	private int assignTransmissionProcessorLink(SystemNode currentNode, SystemNode nextNode, 
			int iterationStartTime, int iterationEndTime, int linkWeight, GNode ancestor, 
			GNode dependentGNode) {
		ArrayList<Tact> currentNodeTacts = currentNode.getTacts();
		ArrayList<ProcessorLink> currentNodeLinks = currentNode.getProcessorLinks();
		
		ArrayList<Tact> nextNodeTacts = nextNode.getTacts();
		ArrayList<ProcessorLink> nextNodeLinks = nextNode.getProcessorLinks();
		
		int[] currentNodeBestStartTime = new int[currentNodeLinks.size()];
		int[] nextNodeBestStartTime = new int[nextNodeLinks.size()];
		
		int bestCurrentLinkNum = -1;
		int bestNextLinkNum = -1;		
		
		for (int i = 0; i < currentNodeLinks.size(); i++) {
			ProcessorLink currentLink = currentNodeLinks.get(i);
			int linkNum = currentLink.getLinkNumber();
			boolean intervalFound = false;
			int tempStartTime = iterationStartTime;
			int tempEndTime = iterationEndTime;
			
			if (duplex) {
				if (currentNode.linkTimeIntervalIsBusy(tempStartTime, tempEndTime, linkNum, 0) == true) {					
					while (intervalFound == false) {
						tempStartTime++;
						tempEndTime++;
						if (nextNode.linkTimeIntervalIsBusy(tempStartTime, tempEndTime, linkNum, 0) == false) {
							intervalFound = true;
						}
					}
				}				
			} else {
				if (currentNode.linkTimeIntervalIsBusy(tempStartTime, tempEndTime, linkNum) == true) {				
					while (intervalFound == false) {
						tempStartTime++;
						tempEndTime++;
						if (nextNode.linkTimeIntervalIsBusy(tempStartTime, tempEndTime, linkNum) == false) {
							intervalFound = true;
						}
					}
				}
			}
			currentNodeBestStartTime[i] = tempStartTime;			
		}
		
		int currentBestStartTime = currentNodeBestStartTime[0];
		int currentIndex = 0;
		
		for (int i = 1;  i < currentNodeBestStartTime.length; i++) {
			if (currentNodeBestStartTime[i] < currentBestStartTime) {
				currentBestStartTime = currentNodeBestStartTime[i];
				currentIndex = i;
			}
		}
		
		bestCurrentLinkNum = currentNodeLinks.get(currentIndex).getLinkNumber();		
		int currentBestEndTime = currentBestStartTime + linkWeight;
		
		for (int i = 0; i < nextNodeLinks.size(); i++) {
			ProcessorLink currentLink = nextNodeLinks.get(i);
			int linkNum = currentLink.getLinkNumber();
			boolean intervalFound = false;
			int tempStartTime = currentBestStartTime;
			int tempEndTime = currentBestEndTime;
			
			if (duplex) {
				if (currentNode.linkTimeIntervalIsBusy(tempStartTime, tempEndTime, linkNum, 1) == true) {				
					while (intervalFound == false) {
						tempStartTime++;
						tempEndTime++;
						if (nextNode.linkTimeIntervalIsBusy(tempStartTime, tempEndTime, linkNum, 1) == false) {
							intervalFound = true;
						}
					}
				}				
			} else {
				if (nextNode.linkTimeIntervalIsBusy(tempStartTime, tempEndTime, linkNum) == true) {				
					while (intervalFound == false) {
						tempStartTime++;
						tempEndTime++;
						if (nextNode.linkTimeIntervalIsBusy(tempStartTime, tempEndTime, linkNum) == false) {
							intervalFound = true;
						}
					}
				}
			}
			nextNodeBestStartTime[i] = tempStartTime;			
		}
		
		int nextBestStartTime = nextNodeBestStartTime[0];
		int nextIndex = 0;
		
		for (int i = 1;  i < nextNodeBestStartTime.length; i++) {
			if (nextNodeBestStartTime[i] < nextBestStartTime) {
				nextBestStartTime = nextNodeBestStartTime[i];
				nextIndex = i;
			}
		}
		
		bestNextLinkNum = nextNodeLinks.get(nextIndex).getLinkNumber();		
		int nextBestEndTime = nextBestStartTime + linkWeight;		
		
		DataTransmission transmission = new DataTransmission(nextBestStartTime, nextBestEndTime, 
				ancestor, dependentGNode, currentNode, nextNode, bestCurrentLinkNum, bestNextLinkNum);
		currentNode.getProcessorLinks().get(bestCurrentLinkNum).addScheduledTransmission(transmission);		
		
		for (int i = nextBestStartTime; i < nextBestEndTime; i++) {
			currentNodeTacts.get(i).setScheduledTransmission(transmission, bestCurrentLinkNum);
			nextNodeTacts.get(i).setProcLinkBusyFlag(true, bestNextLinkNum, 1);
		}
		return nextBestEndTime;
	}
	
	/**
	 * This method uses best system node for specified depended node and then
	 * routes(creates zero or more transitions from ancestry system node to found best system node) data.
	 * @param dependentGNode current task graph node
	 * @param linkWeights link weights from ancestry to our dependent node
	 * @param bestSystemNode system node, on which dependent graph node is assigned
	 */
	private void routeData(GNode dependentGNode, int[] linkWeights, SystemNode bestSystemNode) {
		ArrayList<GNode> ancestry = dependentGNode.getPrev();
		ArrayList<SystemNode> ancestrySysNodes = new ArrayList<>();
		ArrayList<ArrayList<SystemNode>> paths = new ArrayList<>();
		int[] finalTacts = new int[ancestry.size()];
		
		for (int i = 0; i < ancestry.size(); i++) {
			GNode ancestor = ancestry.get(i);
			SystemNode ancestorSysNode = ancestor.getAssignedSystemNode();
			ancestrySysNodes.add(ancestorSysNode);
			ArrayList<SystemNode> shortestPath = findShortestPath(ancestorSysNode, bestSystemNode);
			int iterationStartTime = ancestor.getEndTime();
			int iterationEndTime = iterationStartTime + linkWeights[i];
			
			if (shortestPath.size() >= 2) {
				for (int j = 0; j < shortestPath.size() - 1; j++) {
					SystemNode currentNode = shortestPath.get(j);
					SystemNode nextNode = shortestPath.get(j + 1);					
					
					iterationStartTime = assignTransmissionProcessorLink(currentNode, nextNode, iterationStartTime, iterationEndTime, 
							linkWeights[i], ancestor, dependentGNode);
					iterationEndTime = iterationStartTime + linkWeights[i];					
				}				
			}			
			finalTacts[i] = iterationStartTime;
		}
		
		int depGNodeStartTime = findArrayMax(finalTacts);
		int depGNodeEndTIme = depGNodeStartTime + dependentGNode.getTExe();
		
		if (bestSystemNode.procTimeIntervalIsBusy(depGNodeStartTime, depGNodeEndTIme) == true) {
			boolean intervalFound = false;
			
			while (intervalFound == false) {
				depGNodeStartTime++;
				depGNodeEndTIme++;
				if (bestSystemNode.procTimeIntervalIsBusy(depGNodeStartTime, depGNodeEndTIme) == false) {
					intervalFound = true;
				}
			}
		}
		assignGNodeProcessor(dependentGNode, bestSystemNode, depGNodeStartTime, depGNodeEndTIme);		
	}
	
	/**
	 * Main method to assign dependent graph node to the computer system.
	 * @param dependentGNode specified dependent graph node
	 */
	private void assignDependentGNode(GNode dependentGNode) {
		ArrayList<GNode> ancestry = dependentGNode.getPrev();
		int[] linkWeights = new int[ancestry.size()];		
		
		for (int i = 0; i < ancestry.size(); i++) {
			linkWeights[i] = taskGraph.findGLink(ancestry.get(i), dependentGNode).getTCom();			
		}		
		
		SystemNode selectedSystemNode = null;
		if (algorythmType == 1) {
			selectedSystemNode = chooseRandomProcessor();
		} else if (algorythmType == 5) {
			selectedSystemNode = selectBestSystemNode(dependentGNode);
		}		
		routeData(dependentGNode, linkWeights, selectedSystemNode);
	}
	
	/**
	 * Checks whether there are empty system nodes(i.e. don't contain any scheduled
	 * task graph nodes).
	 * @return true if there are empty system nodes
	 */
	private boolean emptySystemNodesInSystem() {
		for (SystemNode sysNode : nodeList) {
			if (sysNode.getScheduledNodes().isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Calculates the priority of the computer system nodes.
	 */
	private void calculateSystemNodesPriority() {
		for (SystemNode sysNode : nodeList) {
			sysNode.calculatePriority();
		}
	}
	
	/**
	 * Sorts system nodes using their priority property.
	 */
	private void sortSystemNodesByPriority() {
		Collections.sort(nodeList, new ComputerSystem.SortByPriority());
	}	
	
	/**
	 * Searches for system node with specified index.
	 * @param index index to search
	 * @return found system node
	 */
	private SystemNode getSystemNodeByIndex(int index) {
		for (SystemNode sysNode : nodeList) {
			if (sysNode.getIndex() == index) {
				return sysNode;
			}
		}
		return null;
	}
	
	/**
	 * Generates forward path, using "prev" fields of the system node. 
	 * @param destNode destination node from which we start to build forward path
	 * @return a list of system nodes
	 */
	private ArrayList<SystemNode> generateForwardPath(SystemNode destNode) {
		ArrayList<SystemNode> reversePath = new ArrayList<>();
		ArrayList<SystemNode> forwardPath = new ArrayList<>();
		SystemNode currentNode = destNode;
		
		while (currentNode.getPrev() != null) {
			reversePath.add(currentNode);
			currentNode = currentNode.getPrev();
		}
		reversePath.add(currentNode);
		for (int i = reversePath.size() - 1; i >= 0; i--) {
			forwardPath.add(reversePath.get(i));
		}
		return forwardPath;
	}
	
	/**
	 * This method searches for shortest path in the graph of computer system using breadth-first search.
	 * @param srcNode source node
	 * @param destNode destination node
	 * @return a list of system nodes which is the shortest path between specified system nodes
	 */
	private ArrayList<SystemNode> findShortestPath(SystemNode srcNode, SystemNode destNode) {						
		for (SystemNode node : nodeList) {			
			node.setUsed(false);
			node.setPrev(null);
		}	
		
		if (srcNode.equals(destNode)) {
			ArrayList<SystemNode> oneNodePath = new ArrayList<>();
			oneNodePath.add(srcNode);
			return oneNodePath;
		} else {
			ArrayList<SystemNode> queue = new ArrayList<>();		
			queue.add(srcNode);
			srcNode.setUsed(true);
			
			while (!queue.isEmpty()) {				
				SystemNode currentNode = queue.get(0);				
				
				ArrayList<SystemNode> neighbours = currentNode.getNeighbours();			
				
				for (SystemNode neighbour : neighbours) {
					if (neighbour.isUsed() == false) {
						neighbour.setPrev(currentNode);
						if (neighbour == destNode) {
							return generateForwardPath(destNode);
						}
						queue.add(neighbour);
						neighbour.setUsed(true);															
					}
				}			
				queue.remove(0);			
			}
		}					
		return null;
	}		
	
	/**
	 * Accessor to the name field value.
	 * @return name field value
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets a value of a name field.
	 * @param name specified name string
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Accessor to the system nodes list field.
	 * @return a list of the system nodes
	 */
	public ArrayList<SystemNode> getNodeList() {
		return nodeList;
	}
	
	/**
	 * Sets a value of a system nodes list field.
	 * @param nodeList a list of the system nodes
	 */
	public void setNodeList(ArrayList<SystemNode> nodeList) {
		this.nodeList = nodeList;
	}
	
	/**
	 * Accessor to the system links list field.
	 * @return system links list
	 */
	public ArrayList<SystemLink> getLinkList() {
		return linkList;
	}
	
	/**
	 * Sets a value of a system links list field.
	 * @param linkList system links list
	 */
	public void setLinkList(ArrayList<SystemLink> linkList) {
		this.linkList = linkList;
	}
	
	/**
	 * Class comparator for sorting system nodes by priority property.
	 * @author Yaroslav
	 *
	 */
	public static class SortByPriority implements Comparator<SystemNode> {
		
		@Override
		public int compare(SystemNode node1, SystemNode node2) {
			return node1.getPriority() - node2.getPriority();
		}		
	}
}
