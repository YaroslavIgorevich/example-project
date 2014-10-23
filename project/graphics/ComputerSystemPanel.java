package project.graphics;

import java.awt.BorderLayout;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.*;

import project.logic.ComputerSystem;
import project.logic.SystemLink;
import project.logic.SystemNode;

/**
 * A component that contains workspace for creating and editing graph
 * of the computer system.  
 * @author Yaroslav
 *
 */
public class ComputerSystemPanel extends JPanel {		
	
	/**
	 * Maximum X coordinate.
	 */
	private int xMax;
	
	/**
	 * Maximum Y coordinate.
	 */
	private int yMax;	
	
	/**
	 * A pointer of the current selected node.
	 */
	private SystemNode current;
	
	/**
	 * This pointer is used when new system link is created. It points to 
	 * the first node of the system link.
	 */
	private SystemNode startNode;
	
	/**
	 * This pointer is used when new system link is created. It points to 
	 * the second node of the system link.
	 */
	private SystemNode endNode;
	
	/**
	 * A pointer of the current selected system link.
	 */
	private SystemLink currentTr;	
	
	/**
	 * This toggle button is used to create new system nodes. When this button is toggled 
	 * it is possible to create new node on the workspace by simply clicking on it once.
	 */
	private JToggleButton newSystemNode;
	
	/**
	 * This toggle button is used to create new system links. When this button is toggled
	 * it is possible to create new system link in two steps: first, click to the first node,
	 * then just click to the node you want to connect with the first one.
	 */
	private JToggleButton newSystemLink;
	
	/**
	 * This toggle button is used to delete graph elements.
	 */
	private JToggleButton delete;
	
	/**
	 * This toggle button is used to clear all the elements on the workspace.
	 */
	private JButton clear;
	
	/**
	 * This button is used to check the connectivity of the computer system graph. 
	 */
	private JButton checkConnectivity;
	
	/**
	 * A component to specify physical links of the computer system nodes.
	 */
	private JComboBox<String> linksNumber;
	
	/**
	 * A component to set on/off duplex property of the physical links.
	 */
	private JCheckBox duplexBox;
	
	/**
	 * This button is used to save computer system graph to the file.
	 */
	private JButton save;
	
	/**
	 * This button is used to open existing computer system graph file.
	 */
	private JButton open;
	
	/**
	 * A popup menu that contains an option to change index of the system node.
	 */
	private JPopupMenu nodePopup;
	
	/**
	 * This is the object of the computer system.
	 */
	private ComputerSystem computerSystem;	
	
	/**
	 * A list which contains nodes of the computer system.
	 */
	private ArrayList<SystemNode> nodeList;
	
	/**
	 * A list which contains system links of the system.
	 */
	private ArrayList<SystemLink> linkList;
	
	/**
	 * Main constructor.
	 */
	public ComputerSystemPanel() {
		computerSystem = new ComputerSystem();		
		nodeList = computerSystem.getNodeList();
		linkList = computerSystem.getLinkList();
		current = null;
		
		setLayout(new BorderLayout());
		
		JToolBar bar = new JToolBar();		
				
		newSystemNode = new JToggleButton(new ImageIcon("icons/system_node.png"));
		newSystemNode.setToolTipText("Add new system node");
		newSystemLink = new JToggleButton(new ImageIcon("icons/node_connection.png"));
		newSystemLink.setToolTipText("Connect two system nodes");
		delete = new JToggleButton(new ImageIcon("icons/delete.png"));
		delete.setToolTipText("Delete system node or connection");
		
		clear = new JButton(new ImageIcon("icons/clear.png"));
		clear.setToolTipText("Clear all system nodes and connections");
		ClearAction c_a = new ClearAction();
		clear.addActionListener(c_a);
		
		checkConnectivity = new JButton(new ImageIcon("icons/cs_connectivity.png"));
		checkConnectivity.setToolTipText("Check connectivity of the system");
		CheckConnectivityAction cca = new CheckConnectivityAction();
		checkConnectivity.addActionListener(cca);		
		
		JLabel linksNumberLabel = new JLabel("Links number");
		linksNumber = new JComboBox<String>();
		linksNumber.setToolTipText("Select number of physical links here");
		GraphPanel.setComboBoxSize(linksNumber, 40, 25);
		linksNumber.addItem("1");
		linksNumber.addItem("2");
		linksNumber.addItem("3");
		linksNumber.addItem("4");
		
		JLabel duplexLabel = new JLabel("Duplex");
		duplexBox = new JCheckBox();
		duplexBox.setToolTipText("Duplex can transmit data in both directions simultaneously");
		duplexBox.setSelected(false);
		
		save = new JButton(new ImageIcon("icons/save.png"));
		save.setToolTipText("Save computer system to the file");
		SaveAction s_a = new SaveAction();
		save.addActionListener(s_a);
				
		open = new JButton(new ImageIcon("icons/open.png"));
		open.setToolTipText("Open existing computer system from the file");
		OpenAction o_a = new OpenAction();
		open.addActionListener(o_a);
		
		ButtonGroup group = new ButtonGroup();
		
		group.add(newSystemNode);
		group.add(newSystemLink);
		group.add(delete);
		
		bar.add(newSystemNode);		
		bar.add(newSystemLink);
		bar.add(delete);
		bar.add(clear);
		bar.addSeparator(new Dimension(20, 20));
		bar.add(checkConnectivity);		
		bar.addSeparator(new Dimension(20, 20));
		bar.add(linksNumberLabel);
		bar.addSeparator(new Dimension(5, 5));
		bar.add(linksNumber);
		bar.addSeparator(new Dimension(10, 10));
		bar.add(duplexLabel);
		bar.add(duplexBox);
		bar.addSeparator(new Dimension(20, 20));
		bar.add(save);
		bar.add(open);
		
		add(bar, BorderLayout.NORTH);
		
		nodePopup = new JPopupMenu();		
		
		JMenuItem setIndex = new JMenuItem("Set index");
		SetIndexAction s_i_a = new SetIndexAction();
		setIndex.addActionListener(s_i_a);	
		
		nodePopup.add(setIndex);		
		
		addMouseListener(new MouseHandler());
		addMouseMotionListener(new MouseMotionHandler());
	}
	
	/**
	 * A method to paint component. 
	 */
	public void paintComponent(Graphics g) {		
		super.paintComponent(g);			
			
		xMax = getWidth();
		yMax = getHeight();		
							
		scaling();
		
		for (SystemLink link : linkList) {
			link.paint(g);
		}
		
		for (SystemNode node : nodeList) {
			node.paint(g);		
		}
	}
	
	/**
	 * Accessor to the computer system field.
	 * @return pointer to the computer system object
	 */
	public ComputerSystem getComputerSystem() {
		return computerSystem;
	}	
	
	/**
	 * Accessor to the combobox field
	 * @return pointer to the combobox object
	 */
	public JComboBox<String> getLinksNumber() {
		return linksNumber;
	}
	
	/**
	 * Accesor to the checkbox field
	 * @return pointer to the checkbox object
	 */
	public JCheckBox getDuplexBox() {
		return duplexBox;
	}
	
	/**
	 * Sets a value of a nodelist field.
	 * @param nodeList nodelist to set
	 */
	public void setNodeList(ArrayList<SystemNode> nodeList) {		
		this.nodeList = nodeList;
	}
	
	/**
	 * Sets a value of a linklist field.
	 * @param linkList llinklist to set
	 */
	public void setLinkList(ArrayList<SystemLink> linkList) {		
		this.linkList = linkList;
	}	
	
	/**
	 * This method is used to scale nodes size, which depends on the maximum X and Y 
	 * coordinates.
	 */
	private void scaling() {		
		SystemNode.size = 0.03 * (xMax + yMax);				
	}
	
	/**
	 * Resets start node pointer.
	 */
	private void resetStartNode() {		
		if (startNode != null) {			
			startNode.setColor(Color.LIGHT_GRAY);
			startNode = null;
		}
	}
	
	/**
	 * Checks whether are there any nodes in the graph with the specified index in the graph or not.
	 * @param index index to check
	 * @return true if there is a node with the same index
	 */
	public boolean checkCoincidence(int index) {		
		for (SystemNode node : nodeList) {			
			if (node.getIndex() == index) return true;
		}
		return false;
	}
	
	/**
	 * Checks the connection of the two specified system nodes.
	 * @param start first node
	 * @param end second node
	 * @return true if the nodes are connected
	 */
	private boolean areConnected(SystemNode start, SystemNode end) {		
		for (SystemLink link : linkList) {			
			if (link.getFirstNode().equals(start) && link.getSecondNode().equals(end)) return true;
			if (link.getSecondNode().equals(start) && link.getFirstNode().equals(end)) return true;
		}
		return false;
	}
	
	/**
	 * Checks whether specified point located in any system node on the workspace or not.
	 * If yes it returns this system node.
	 * @param p point to check
	 * @return found system node
	 */
	private SystemNode findSystemNode(Point2D p) {		
		for (SystemNode node : nodeList) {			
			if (node.contains(p)) return node;
		}		
		return null;
	}
	
	/**
	 * Checks whether specified point located in any system link on the workspace or not.
	 * If yes it returns this system link.
	 * @param p point to check
	 * @return found system link
	 */
	private SystemLink findSystemLink(Point2D p) {		
		for (SystemLink link : linkList) {			
			if (link.contains(p)) return link;			
		}
		return null;
	}
	
	/**
	 * Finds all the system links connected to the specified system node.
	 * @param node node to find its connections
	 * @return array list of the system links
	 */
	private ArrayList<SystemLink> findSystemNodeConnections(SystemNode node) {		
		ArrayList<SystemLink> connections = new ArrayList<SystemLink>();
		
		for (SystemLink link : linkList) {			
			if (link.getFirstNode().equals(node) || link.getSecondNode().equals(node)) { 
				connections.add(link);
			}
		}
		return connections;
	}	
	
	/**
	 * Action class of the clear button.
	 * @author Yaroslav
	 *
	 */
	private class ClearAction implements ActionListener {
		
		public void actionPerformed(ActionEvent event) {			
			nodeList.clear();
			linkList.clear();
			SystemNode.nextIndex = 0;
			repaint();
		}
	}
	
	/**
	 * Action class of the connectivity button.
	 * @author Yaroslav
	 *
	 */
	private class CheckConnectivityAction implements ActionListener {
		
		public void actionPerformed(ActionEvent event) {
			if (nodeList.size() < 2) {
				JOptionPane.showMessageDialog(null, "System must contain at least 2 nodes to check it for connectivity!", 
						  "Warning", JOptionPane.WARNING_MESSAGE);
			} else {
				boolean result = computerSystem.checkConnectivity();
				
				if (result == true) {
					JOptionPane.showMessageDialog(null, "Ok!", 
							  "Success", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "System is not connected!", 
							  "Error", JOptionPane.ERROR_MESSAGE);
				}
			}		
		}
	}	
	
	/**
	 * Action class of the save button.
	 * @author Yaroslav
	 *
	 */
	private class SaveAction implements ActionListener {
		
		public void actionPerformed(ActionEvent event) {			
			JFileChooser csr = new JFileChooser();
			csr.setCurrentDirectory(new File("."));
			int result = csr.showSaveDialog(ComputerSystemPanel.this);
			
			String filename = "";
			
			if (csr.getSelectedFile() != null) {			
				filename = csr.getSelectedFile().getPath();
			}			
			
			if (result == JFileChooser.APPROVE_OPTION) {				
				try {					
					ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));					
					out.writeObject(nodeList);
					out.writeObject(linkList);
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}						
		}	
	}
	
	/**
	 * Action class of the open button
	 * @author Yaroslav
	 *
	 */
	private class OpenAction implements ActionListener {
		
		@SuppressWarnings("unchecked")
		public void actionPerformed(ActionEvent event) {			
			nodeList.clear();
			linkList.clear();
			
			JFileChooser csr = new JFileChooser();
			csr.setCurrentDirectory(new File("."));
			int result = csr.showOpenDialog(ComputerSystemPanel.this);			
			String filename = "";
			
			if (csr.getSelectedFile() != null) {			
				filename = csr.getSelectedFile().getPath();
			}			
		
			if (result == JFileChooser.APPROVE_OPTION) {				
				try {
					ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));					
					nodeList = (ArrayList<SystemNode>)in.readObject();
					linkList = (ArrayList<SystemLink>)in.readObject();
					computerSystem.setNodeList(nodeList);
					computerSystem.setLinkList(linkList);
					computerSystem.setNextIndex();
					in.close();
					repaint();
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}			
		}		
	}
	
	/**
	 * Action class of the set index submenu.
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
	 * This mouse handler class contains processing of the mouse clicking events on the computer systems graph workspace.
	 * @author Yaroslav
	 *
	 */
	private class MouseHandler extends MouseAdapter {		
		
		public void mousePressed(MouseEvent event) {			
			current = findSystemNode(event.getPoint());
		}
		
		public void mouseClicked(MouseEvent event) {			
			if (event.getButton() == MouseEvent.BUTTON1) {			
				if (newSystemNode.getModel().isSelected()) {				
					
					current = findSystemNode(event.getPoint());
					resetStartNode();
					
					if (current == null) {										
						try {						
							current = new SystemNode(event.getX(), event.getY());
							nodeList.add(current);
							current = null;
							repaint();							
						} catch (NumberFormatException nfe) {
							JOptionPane.showMessageDialog(null, "Error!", "Error", JOptionPane.WARNING_MESSAGE);						
						}
					}
				} else if (newSystemLink.getModel().isSelected()){
					
					if (startNode == null) {						
						startNode = findSystemNode(event.getPoint());
						
						if (startNode != null) {							
							startNode.setColor(new Color(255, 100, 100));
						}
						repaint();
					} else {
																
						endNode = findSystemNode(event.getPoint());
						
						if (endNode != null) {							
							if (!startNode.equals(endNode)) {								
								if (!areConnected(startNode, endNode)) {																	
									try {																			
										SystemLink link = new SystemLink(startNode, endNode);
										resetStartNode();									
										linkList.add(link);
										repaint();										
									} catch (NumberFormatException nfe) {
										JOptionPane.showMessageDialog(null, "Error!", "Error", JOptionPane.WARNING_MESSAGE);						
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
					current = findSystemNode(event.getPoint());
					
					if (current != null) {						
						ArrayList<SystemLink> linksToDelete = findSystemNodeConnections(current);
						
						for (SystemLink link : linksToDelete) {							
							linkList.remove(link);
						}
						
						if (!current.getNeighbours().isEmpty()) {							
							for (SystemNode neighbour : current.getNeighbours()) {								
								neighbour.getNeighbours().remove(current);
							}
						}					
						nodeList.remove(current);
					}
					
					currentTr = findSystemLink(event.getPoint());
					
					if (currentTr != null) {						
						currentTr.getFirstNode().getNeighbours().remove(currentTr.getSecondNode());
						currentTr.getSecondNode().getNeighbours().remove(currentTr.getFirstNode());
						linkList.remove(currentTr);
					}					
					repaint();
				}				
			} else if (event.getButton() == MouseEvent.BUTTON3) {				
				current = findSystemNode(event.getPoint());				
				if (current != null) nodePopup.show(ComputerSystemPanel.this, event.getX(), event.getY());				
			}
		}	
	}
	
	/**
	 * This mouse motion handler class contains processing of the 
	 * mouse dragging event on the computer systems graph workspace.
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
