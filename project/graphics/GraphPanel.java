package project.graphics;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import project.logic.GLink;
import project.logic.GNode;
import project.logic.Graph;

import java.awt.geom.*;
import java.io.*;

@SuppressWarnings("serial")
/**
 * A component that contains workspace for creating and editing task graph. 
 * It is possible to save and open created graph.
 * @author Yaroslav
 *
 */
public class GraphPanel extends JPanel {
	
	/**
	 * A pointer to the graph object, which contains all the logic.
	 */
	private Graph graph;
	
	/**
	 * A pointer to the computer system panel.
	 */
	private ComputerSystemPanel csPanel;
	
	/**
	 * A pointer to the schedule panel.
	 */
	private SchedulePanel schedulePanel;
	
	/**
	 * A list of the nodes of the graph.
	 */
	private ArrayList<GNode> gnodesList;
	
	/**
	 * A list of the transitions between graph nodes.
	 */
	private ArrayList<GLink> trsList;
	
	/**
	 * A pointer to the current selected node.
	 */
	private GNode current;
	
	/**
	 * This pointer is used when new graph transition is created. It points to 
	 * the first node of the transition.
	 */
	private GNode startNode;
	
	/**
	 * This pointer is used when new transition is created. It points to 
	 * the second node of the transition.
	 */
	private GNode endNode;
	
	/**
	 * A pointer to the current selected transition.
	 */
	private GLink currentTr;	
	
	/**
	 * This toggle button is used to create new graph node.
	 */
	private JToggleButton newGNode;
	
	/**
	 * This toggle button is used to create new transition.
	 */
	private JToggleButton newTransition;
	
	/**
	 * This toggle button is used to delete graph elements. 
	 */
	private JToggleButton delete;
	
	/**
	 * A button to clear all the elements on the workspace.
	 */
	private JButton clear;
	
	/**
	 * A button to check cycles in the graph.
	 */
	private JButton checkCycles;
	
	/**
	 * This button is used to generate queues.
	 */
	private JButton generateQueues;
	
	/**
	 * A component to specify queue type to generate.
	 */
	private JComboBox queueType;
	
	/**
	 * A button to load task graph on the computer system.
	 */
	private JButton loadTaskGraph;
	
	/**
	 * This combo box is used to specify assign algorithm.
	 */
	private JComboBox assignAlgorithm;
	
	/**
	 * This button is used to save task graph to file.
	 */
	private JButton save;
	
	/**
	 * This button is used to open existing task graph.
	 */
	private JButton open;
	
	/**
	 * This popup menu contains options to change node's index or execution time.
	 * parameters
	 */
	private JPopupMenu nodePopup;
	
	/**
	 * This popup menu contains an option to change transition's amount of data.
	 */
	private JPopupMenu transPopup;
	
	/**
	 * Main constructor.
	 * @param csPanel a pointer to the current computer system panel
	 * @param consolePanel a pointer to the console panel
	 * @param schedulePanel a pointer to the schedule panel
	 */
	public GraphPanel(ComputerSystemPanel csPanel, ConsolePanel consolePanel, SchedulePanel schedulePanel) {
		
		graph = new Graph(consolePanel.getConsole());
		this.csPanel = csPanel;
		this.schedulePanel = schedulePanel;
		
		gnodesList = graph.getGnodeList();
		trsList = graph.getGlinkList();
		current = null;
		
		setLayout(new BorderLayout());
					
		JToolBar bar = new JToolBar();		
				
		newGNode = new JToggleButton(new ImageIcon("icons/node.png"));
		newGNode.setToolTipText("Add new graph node");
		newTransition = new JToggleButton(new ImageIcon("icons/transition.png"));
		newTransition.setToolTipText("Connect two graph nodes");
		delete = new JToggleButton(new ImageIcon("icons/delete.png")); 
		delete.setToolTipText("Delete graph element");
		
		clear = new JButton(new ImageIcon("icons/clear.png"));
		clear.setToolTipText("Clear workspace");
		ClearAction c_a = new ClearAction();
		clear.addActionListener(c_a);
		
		checkCycles = new JButton(new ImageIcon("icons/graph_cycle.png"));
		checkCycles.setToolTipText("Check the graph on cycles");
		CheckCyclesAction cca = new CheckCyclesAction();
		checkCycles.addActionListener(cca);
		
		generateQueues = new JButton(new ImageIcon("icons/graph_queue.png"));
		generateQueues.setToolTipText("Generate queue");
		GenerateQueuesAction gqa  = new GenerateQueuesAction();
		generateQueues.addActionListener(gqa);
		
		queueType = new JComboBox<>();
		queueType.setToolTipText("Select queue type here");
		setComboBoxSize(queueType, 40, 25);		
		queueType.addItem("1");
		queueType.addItem("12");
		queueType.addItem("16");
		
		loadTaskGraph = new JButton(new ImageIcon("icons/load_task_graph.png"));
		loadTaskGraph.setToolTipText("Load task graph on a computer system");
		LoadTaskGraphAction ltga = new LoadTaskGraphAction();
		loadTaskGraph.addActionListener(ltga);
		
		assignAlgorithm = new JComboBox<>();
		assignAlgorithm.setToolTipText("Select schedule algorithm here");
		setComboBoxSize(assignAlgorithm, 40, 25);
		/*assignAlgorithm.setMaximumSize(new Dimension(40, 24));
		assignAlgorithm.setMinimumSize(new Dimension(40, 24));*/
		assignAlgorithm.addItem("1");
		assignAlgorithm.addItem("5");
		assignAlgorithm.setSelectedIndex(1);
		
		save = new JButton(new ImageIcon("icons/save.png"));
		save.setToolTipText("Save graph to file");
		SaveAction s_a = new SaveAction();
		save.addActionListener(s_a);
				
		open = new JButton(new ImageIcon("icons/open.png"));
		open.setToolTipText("Open existing graph from file");
		OpenAction o_a = new OpenAction();
		open.addActionListener(o_a);
		
		ButtonGroup group = new ButtonGroup();
		
		group.add(newGNode);
		group.add(newTransition);
		group.add(delete);
		
		bar.add(newGNode);		
		bar.add(newTransition);
		bar.add(delete);
		bar.add(clear);
		bar.addSeparator(new Dimension(20, 20));
		bar.add(checkCycles);
		bar.add(generateQueues);
		bar.add(queueType);
		bar.addSeparator(new Dimension(20, 20));
		bar.add(loadTaskGraph);
		bar.add(assignAlgorithm);
		bar.addSeparator(new Dimension(20, 20));
		bar.add(save);
		bar.add(open);
		
		add(bar, BorderLayout.NORTH);
		
		nodePopup = new JPopupMenu();
		transPopup = new JPopupMenu();
		
		JMenuItem set_index = new JMenuItem("Set index");
		SetIndexAction s_i_a = new SetIndexAction();
		set_index.addActionListener(s_i_a);		
		
		JMenuItem set_tExe = new JMenuItem("Set T-exe");
		SetTExeAction s_tx_a = new SetTExeAction();
		set_tExe.addActionListener(s_tx_a);
		
		JMenuItem set_tCom = new JMenuItem("Set T-com");
		SetTComAction s_tc_a = new SetTComAction();
		set_tCom.addActionListener(s_tc_a);		
		
		nodePopup.add(set_index);
		nodePopup.add(set_tExe);
		
		transPopup.add(set_tCom);
		
		addMouseListener(new MouseHandler());
		addMouseMotionListener(new MouseMotionHandler());
	}
	
	/**
	 * A method to paint component.
	 */
	public void paintComponent(Graphics g) {		
		super.paintComponent(g);		
		scaling();
		
		for (GLink trans : trsList) {		
			trans.paint(g);
		}
		
		for (GNode gnode : gnodesList){
			
			gnode.paint(g);
		}	
	}
	
	/**
	 * This method returns parsed value of the combo box.
	 * @return code of the queue type
	 */
	public int getQueueType() {
		return Integer.parseInt((String)(queueType.getSelectedItem()));
	}
	
	/**
	 * This method returns parsed value of the combo box.
	 * @return code of the scheduling algorithm 
	 */
	public int getAlgorythmType() {
		return Integer.parseInt((String)(assignAlgorithm.getSelectedItem()));
	}
	
	/**
	 * Changes combo box size.
	 * @param box combo box to resize
	 * @param width specified maximum width
	 * @param height specified maximum height
	 */
	public static void setComboBoxSize(JComboBox box, int width, int height) {
		box.setMaximumSize(new Dimension(width, height));
		box.setMinimumSize(new Dimension(width, height));
	}
	
	/**
	 * Accessor to the graph field value.
	 * @return pointer to the graph object
	 */
	public Graph getGraph() {
		return graph;
	}
	
	/**
	 * Scales the size of the graph nodes relatively to the width of the screen.
	 */
	private void scaling() {		
		GNode.r = 0.03 * getWidth();
		GNode.d = 2 * GNode.r;
	}	
	
	/**
	 * Resets start node value to null.
	 */
	private void resetStartNode() {		
		if (startNode != null) {			
			startNode.setColor(Color.LIGHT_GRAY);
			startNode = null;
		}
	}
	
	/**
	 * Checks whether two graph nodes are connected or not.
	 * @param start first node
	 * @param end second node
	 * @return true if two nodes are connected
	 */
	private boolean areConnected(GNode start, GNode end) {		
		for (GLink t : trsList) {			 
			if (t.getStart().equals(start) && t.getEnd().equals(end)) return true;
			if (t.getEnd().equals(start) && t.getStart().equals(end)) return true;
		}
		return false;
	}
	
	/**
	 * Checks whether there is a node with specified index or not.
	 * @param index index of the node
	 * @return true if such node exists
	 */
	public boolean checkCoincidence(int index) {		
		for (GNode g : gnodesList) {			
			if (g.getIndex() == index) return true;
		}
		return false;
	}
	
	/**
	 * Checks whether specified point located in any graph node on the workspace or not.
	 * If yes it returns this system node.
	 * @param p point to check
	 * @return found graph node
	 */
	private GNode findGNode(Point2D p) {		
		for (GNode gnode : gnodesList) {			
			if (gnode.contains(p)) return gnode;
		}		
		return null;
	}
	
	/**
	 * Checks whether specified point located in any graph transition on the workspace or not.
	 * If yes it returns this transition.
	 * @param p point to check
	 * @return found transition
	 */
	private GLink findTransition(Point2D p) {		
		for (GLink t : trsList) {			
			if (t.contains(p)) return t;			
		}
		return null;
	}
	
	/**
	 * Searches all the transitions of the specified node
	 * @param g graph node
	 * @return array list of the found transitions
	 */
	private ArrayList<GLink> findGNodeConnections(GNode g) {		
		ArrayList<GLink> connections = new ArrayList<GLink>();
		
		for (GLink t : trsList) {			
			if (t.getStart().equals(g) || t.getEnd().equals(g)) connections.add(t);
		}
		return connections;
	}	
	
	/**
	 * Action class of the clear button. Contains the handler method clicking on the button.
	 * @author Yaroslav
	 *
	 */
	private class ClearAction implements ActionListener {
		
		public void actionPerformed(ActionEvent event) {			
			gnodesList.clear();
			trsList.clear();
			GNode.nextIndex = 0;
			repaint();
		}
	} 
	
	/**
	 * Action class of the checking cycles button. Contains the handler method clicking on the button.
	 * @author Yaroslav
	 *
	 */
	private class CheckCyclesAction implements ActionListener {
		
		public void actionPerformed(ActionEvent event) {			
			if (gnodesList.size() < 3) {
				JOptionPane.showMessageDialog(null, "Graph must contain at least 3 nodes to check it for cycles!", 
											  "Warning", JOptionPane.WARNING_MESSAGE);
			} else {
				Object[] result = graph.checkCycles();				
				
				if ((boolean)result[0] == true) {				
					JOptionPane.showMessageDialog(null, "Graph contains cycles!\n" + (String)result[1], 
							  "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Ok!", 
							  "Success", JOptionPane.INFORMATION_MESSAGE);
				}
			}			
		}
	}
	
	/**
	 * Action class of the button to generate queues. Contains the handler method clicking on the button.
	 * @author Yaroslav
	 *
	 */
	private class GenerateQueuesAction implements ActionListener {
		
		public void actionPerformed(ActionEvent event) {			
			if (graph.isEmpty()) {				
				JOptionPane.showMessageDialog(null, "Add at least one node in the graph!", "Error", JOptionPane.WARNING_MESSAGE);
			} else {
				if ((boolean)graph.checkCycles()[0] == false) {
					int typeOfQueue = Integer.parseInt((String)(queueType.getSelectedItem()));
					graph.generateQueue(typeOfQueue, true);
				} else {
					JOptionPane.showMessageDialog(null, "Check graph for cycles!", "Error", JOptionPane.WARNING_MESSAGE);
				}						
			}			
		}
	}
	
	/**
	 * Action class of the button to load task graph on the computer system. 
	 * Contains the handler method clicking on the button.
	 * @author Yaroslav
	 *
	 */
	private class LoadTaskGraphAction implements ActionListener {
		
		public void actionPerformed(ActionEvent event) {
			int returnCode = graph.checkSystemAndGraph(csPanel.getComputerSystem());
			
			switch(returnCode) {
				case 0:
					int typeOfQueue = Integer.parseInt((String)(queueType.getSelectedItem()));
					int algorythm = Integer.parseInt((String)(assignAlgorithm.getSelectedItem()));
					int linksNumber = Integer.parseInt((String)(csPanel.getLinksNumber().getSelectedItem()));
					boolean isDuplex = csPanel.getDuplexBox().isSelected();
					graph.generateQueue(typeOfQueue, true);
					csPanel.getComputerSystem().loadTaskGraph(graph, algorythm, linksNumber, isDuplex);
					schedulePanel.setSystemNodesList(csPanel.getComputerSystem().getNodeList());
					break;
				case 1:
					JOptionPane.showMessageDialog(null, "Add at least one node in the system!", "Error", JOptionPane.WARNING_MESSAGE);
					break;
				case 2:
					JOptionPane.showMessageDialog(null, "The computer system must be connected!", "Error", JOptionPane.WARNING_MESSAGE);
					break;
				case 3:
					JOptionPane.showMessageDialog(null, "Add at least one node in the graph!", "Error", JOptionPane.WARNING_MESSAGE);
					break;
				case 4:
					JOptionPane.showMessageDialog(null, "Check graph for cycles!", "Error", JOptionPane.WARNING_MESSAGE);
					break;
			}										
		}
	}
	
	/**
	 * Action class of the save button. Contains the handler method clicking on the button.
	 * @author Yaroslav
	 *
	 */
	private class SaveAction implements ActionListener {
		
		public void actionPerformed(ActionEvent event) {			
			JFileChooser csr = new JFileChooser();
			csr.setCurrentDirectory(new File("."));
			int result = csr.showSaveDialog(GraphPanel.this);
			
			String filename = "";
			
			if (csr.getSelectedFile() != null) {				
				filename = csr.getSelectedFile().getPath();
			}			
			
			if (result == JFileChooser.APPROVE_OPTION) {				
				try {					
					ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));					
					out.writeObject(gnodesList);
					out.writeObject(trsList);
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}						
		}	
	}
	
	/**
	 * Action class of the open button. Contains the handler method clicking on the button.
	 * @author Yaroslav
	 *
	 */
	private class OpenAction implements ActionListener {
		
		@SuppressWarnings("unchecked")
		public void actionPerformed(ActionEvent event) {			
			gnodesList.clear();
			trsList.clear();
			
			JFileChooser csr = new JFileChooser();
			csr.setCurrentDirectory(new File("."));
			int result = csr.showOpenDialog(GraphPanel.this);			
			String filename = "";
			
			if (csr.getSelectedFile() != null) {				
				filename = csr.getSelectedFile().getPath();
			}			
		
			if (result == JFileChooser.APPROVE_OPTION) {				
				try {
					ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));					
					gnodesList = (ArrayList<GNode>)in.readObject();
					trsList = (ArrayList<GLink>)in.readObject();
					graph.setGnodeList(gnodesList);
					graph.setGlinkList(trsList);
					graph.setNextIndex();
					in.close();
					repaint();
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}			
		}		
	}	
	
	/**
	 * This class contains handler of the set index button clicking.
	 * @author Yaroslav
	 *
	 */
	private class SetIndexAction implements ActionListener {
		
		public void actionPerformed(ActionEvent event) {			
			String indexStr = JOptionPane.showInputDialog(null, "Eneter the index of this node:", "Node index", JOptionPane.QUESTION_MESSAGE);
			
			try {				
				int index = Integer.parseInt(indexStr);
				
				if (checkCoincidence(index) == false) {				
					current.setIndex(index);	
					repaint();
				} else {
					JOptionPane.showMessageDialog(null, "Try another index!", "Error", JOptionPane.WARNING_MESSAGE);
				}
			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(null, "Error!", "Error", JOptionPane.WARNING_MESSAGE);						
			}
		}
	}
	
	/**
	 * Action class of the set execution time submenu. Contains the handler method clicking on the button.
	 * @author Yaroslav
	 *
	 */
	private class SetTExeAction implements ActionListener {
		
		public void actionPerformed(ActionEvent event) {			
			String tExeStr = JOptionPane.showInputDialog(null, "Eneter the node exe time:", "Node exe time", JOptionPane.QUESTION_MESSAGE);
			if (tExeStr != null) {
				try {			
					current.setTExe(Integer.parseInt(tExeStr));	
					repaint();
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(null, "Error!", "Error", JOptionPane.WARNING_MESSAGE);						
				}
			}
		}
	}
	
	/**
	 * Action class of the communication time submenu. Contains the handler method clicking on the button.
	 * @author Yaroslav
	 *
	 */
	private class SetTComAction implements ActionListener {
		
		public void actionPerformed(ActionEvent event) {			
			String tComStr = (String)JOptionPane.showInputDialog(null, "Eneter the transition com time:", "Transition com time", 
					JOptionPane.QUESTION_MESSAGE, null, null, "1");
			if (tComStr != null) {
				try {				
					currentTr.setTCom(Integer.parseInt(tComStr));
					repaint();
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(null, "Error!", "Error", JOptionPane.WARNING_MESSAGE);						
				}
			}
		}
	}	
	
	/**
	 * This mouse handler class contains processing of the mouse clicking events on the task graph workspace.
	 * @author Yaroslav
	 *
	 */
	private class MouseHandler extends MouseAdapter {		
		
		public void mousePressed(MouseEvent event) {			
			current = findGNode(event.getPoint());
		}
		
		@SuppressWarnings("static-access")
		public void mouseClicked(MouseEvent event) {			
			if (event.getButton() == event.BUTTON1) {			
				if (newGNode.getModel().isSelected()) {					
					current = findGNode(event.getPoint());
					resetStartNode();
					
					if (current == null) {	
						String tExeStr = (String)JOptionPane.showInputDialog(null, "Eneter the node exe time:", "Node exe time", 
								JOptionPane.QUESTION_MESSAGE, null, null, "1");
						if (tExeStr != null) {
							try {						
								int tExe = Integer.parseInt(tExeStr);							
								current = new GNode(tExe, event.getX(), event.getY());
								gnodesList.add(current);
								current = null;
								repaint();							
							} catch (NumberFormatException nfe) {
								JOptionPane.showMessageDialog(null, "Error!", "Error", JOptionPane.WARNING_MESSAGE);						
							}
						}					
					}
				} else if (newTransition.getModel().isSelected()) {					
					if (startNode == null) {						
						startNode = findGNode(event.getPoint());
						
						if (startNode != null) {							
							startNode.setColor(new Color(255, 100, 100));
						}
						repaint();
					} else {																
						endNode = findGNode(event.getPoint());
						
						if (endNode != null) {							
							if (!startNode.equals(endNode)) {								
								if (!areConnected(startNode, endNode)) {									
									String tComStr = (String)JOptionPane.showInputDialog(null, "Eneter the transition com time:", "Transition com time", 
											JOptionPane.QUESTION_MESSAGE, null, null, "1");
									if (tComStr != null) {
										try {																	
											int tCom = Integer.parseInt(tComStr);										
											GLink t = new GLink(tCom, startNode, endNode);
											resetStartNode();									
											trsList.add(t);
											repaint();										
										} catch (NumberFormatException nfe) {
											JOptionPane.showMessageDialog(null, "Error!", "Error", JOptionPane.WARNING_MESSAGE);						
										}
									}
								} else {									
									JOptionPane.showMessageDialog(null, "Those nodes are already connected!", "Error", JOptionPane.WARNING_MESSAGE);
									resetStartNode();
									repaint();
								}
							}
						} else {							
							resetStartNode();
							repaint();
						}
					}				
				} else if (delete.getModel().isSelected()) {					
					current = findGNode(event.getPoint());
					
					if (current != null) {						
						ArrayList<GLink> linksToDelete = findGNodeConnections(current);
						
						for (GLink t : linksToDelete) {							
							trsList.remove(t);
						}
						
						if (!current.getPrev().isEmpty()) {							
							for (GNode prevNode : current.getPrev()) {
								prevNode.getNext().remove(current);
							}
						}
						
						if (!current.getNext().isEmpty()) {							
							for (GNode nextNode : current.getNext()) {								
								nextNode.getPrev().remove(current);
							}
						}						
						
						gnodesList.remove(current);
					}
					
					currentTr = findTransition(event.getPoint());
					
					if (currentTr != null) {						
						currentTr.getStart().getNext().remove(currentTr.getEnd());
						currentTr.getEnd().getPrev().remove(currentTr.getStart());
						trsList.remove(currentTr);
					}					
					repaint();
				}				
			} else if (event.getButton() == event.BUTTON3) {				
				current = findGNode(event.getPoint());
				
				if (current != null) nodePopup.show(GraphPanel.this, event.getX(), event.getY());
				
				currentTr = findTransition(event.getPoint());
				
				if (currentTr != null) transPopup.show(GraphPanel.this, event.getX(), event.getY());
			}
		}	
	}
	
	/**
	 * This mouse motion handler class contains processing of the 
	 * mouse dragging events on the task graph workspace.
	 * @author Yaroslav
	 *
	 */
	private class MouseMotionHandler extends MouseMotionAdapter {
		
		public void mouseDragged(MouseEvent event) {			
			if (current != null) {				
				int x = event.getX();
				int y = event.getY();
				
				current.setParameters(x, y);
				repaint();
			} 
		}
	}
}
