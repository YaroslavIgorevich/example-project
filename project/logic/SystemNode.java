package project.logic;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class holds the information and methods about computer system node.
 * @author Yaroslav
 *
 */
public class SystemNode implements Serializable {	 
	
	/**
	 * This static field is used to define IDs of the created system nodes.
	 */
	public static int nextIndex = 0;
	
	/**
	 * Unique system node index.
	 */
	private int index;
	
	/**
	 * Index string.
	 */
	private String indexStr;
	
	/**
	 * System node priority. The greater nodes coherence, the more priority it has.
	 */
	private int priority;
	
	/**
	 * A list of scheduled graph nodes.
	 */
	private ArrayList<GNode> scheduledNodes;
	
	/**
	 * A list of system node physical links.
	 */
	private ArrayList<ProcessorLink> processorLinks;
	
	/**
	 * A list of tacts for this node.
	 */
	private ArrayList<Tact> tacts;
	
	/**
	 * Total scheduling time(i.e. last scheduled task/transmission).
	 */
	private int totalTime;
	
	/**
	 * Color to display on the workspace
	 */
	private Color color;
	
	/**
	 * Basic rectangle for graphical representation.
	 */
	private Rectangle2D systemNode;
	
	/**
	 * A pointer to prev system node. It is used when we generate reverse path.
	 */
	private SystemNode prev;
	
	/**
	 * A list of system node "neighbours" (nodes that are connected to this node).
	 */
	private ArrayList<SystemNode> neighbours;
	
	/**
	 * This flag is used for breadth-first search in the system nodes graph.
	 */
	private boolean used;
	
	/**
	 * Center X coordinate of the system node rectangle.
	 */
	public double cx;
	
	/**
	 * Center Y coordinate of the system node rectangle.
	 */
	public double cy;
	
	/**
	 * This is the size of system node rectangle side.
	 */
	public static double size;
	 
	/**
	 * Main constructor.
	 * @param cx center X coordinate
	 * @param cy center Y coordinate
	 */
	public SystemNode(double cx, double cy) {		
		index = nextIndex;
		nextIndex++;
		indexStr = "N" + String.valueOf(index);
		scheduledNodes = new ArrayList<>();
		processorLinks = new ArrayList<>();		
		for (int i = 0; i < ComputerSystem.processorLinkNumber; i++) {
			ProcessorLink pLink = new ProcessorLink(i);
			processorLinks.add(pLink);
		}
		tacts = new ArrayList<>();
		for (int i = 0; i < 5000; i++) {
			Tact tact = new Tact(this);
			tact.setTactNum(i);
			tacts.add(tact);
		}		
		color = Color.LIGHT_GRAY;
		neighbours = new ArrayList<>();
		this.cx = cx;
		this.cy = cy;
	}	
	
	/**
	 * Accessor to the priority field.
	 * @return priority
	 */
	public int getPriority() {
		return priority;
	}
	
	/**
	 * Accessor to the scheduled nodes field.
	 * @return shecduled nodes
	 */
	public ArrayList<GNode> getScheduledNodes() {		
		return scheduledNodes;
	}	
	
	/**
	 * Accessor to the total time field.
	 * @return total scheduled time
	 */
	public int getTotalTime() {
		return totalTime;
	}		
	
	/**
	 * Adds specified graph node to the scheduled nodes list.
	 * @param g graph node
	 */
	public void setTaskForExecute(GNode g) {		
		scheduledNodes.add(g);
	}
	 
	/**
	 * Sets a value of the system node color.
	 * @param color node color
	 */
	public void setColor(Color color) {		
		this.color = color;
	}	
	
	/**
	 * Accessor to the neighbors field.
	 * @return a list of neighbors
	 */
	public ArrayList<SystemNode> getNeighbours() {
		return neighbours;
	}
	 
	/**
	 * Sets a value of the neighbors list field.
	 * @param neighbours neighbors list
	 */
	public void setNeighbours(ArrayList<SystemNode> neighbours) {
		this.neighbours = neighbours;
	}
	
	/**
	 * Sets a pair of center coordinates for system node.
	 * @param cx center X coordinate
	 * @param cy center Y coordinate
	 */
	public void setParameters(double cx, double cy) {		
		this.cx = cx;
		this.cy = cy;
	}
	
	/**
	 * Accessor to the index field.
	 * @return node index
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Accessor to the index string field.
	 * @return index string
	 */
	public String getIndexStr() {
		return indexStr;
	}
	
	/**
	 * Sets a value to the index filed.
	 * @param index node index
	 */
	public void setIndex(int index) {
		this.index = index;
		indexStr = "N" + Integer.toString(index);
	}
	
	/**
	 * Returns the value of the used falg.
	 * @return used flag
	 */
	public boolean isUsed() {
		return used;
	}
	
	/**
	 * Sets a value to the used flag.
	 * @param used flag
	 */
	public void setUsed(boolean used) {
		this.used = used;
	}	
	
	/**
	 * Accessor to the prev field.
	 * @return previous node in the path
	 */
	public SystemNode getPrev() {
		return prev;
	}
	
	/**
	 * Sets a value to the prev field.
	 * @param prev previous node in the path
	 */
	public void setPrev(SystemNode prev) {
		this.prev = prev;
	}	
	
	/**
	 * Accessor to the physical links field.
	 * @return a list of the physical links
	 */
	public ArrayList<ProcessorLink> getProcessorLinks() {
		return processorLinks;
	}
	
	/**
	 * Accessor to the tacts field.	
	 * @return a list of tacts
	 */
	public ArrayList<Tact> getTacts() {
		return tacts;
	}
	
	/**
	 * Checks whether system node rectangle contains specified point or not.
	 * @param p point to check
	 * @return true if contains
	 */
	public boolean contains(Point2D p) {		 
		if (systemNode.contains(p)) return true;
		return false;
	}	
	
	/**
	 * Increases total time with the specified value.
	 * @param value
	 */
	public void increaseTotalTime(int value) {
		totalTime += value;
	}
	
	/**
	 * Assigns graph node to this system node.
	 * @param gnode graph node
	 */
	public void assignGNode(GNode gnode) {
		scheduledNodes.add(gnode);
	}
	
	/**
	 * Calculates priority of the system node
	 */
	public void calculatePriority() {
		priority = neighbours.size();
	}
	
	/**
	 * Returns last tact of the last scheduled task.
	 * @return last tact
	 */
	public int getLastTaskEndTime() {
		if (scheduledNodes.isEmpty()) {
			return 0;
		} else {
			return scheduledNodes.get(scheduledNodes.size() - 1).getEndTime();
		}				
	}
	
	/**
	 * Assigns graph node to this system node.
	 * @param gnode grpah node
	 */
	public void addScheduledGNode(GNode gnode) {
		scheduledNodes.add(gnode);
	}
	
	/**
	 * Resets a list of tacts.
	 */
	public void resetTacts() {
		tacts.clear();
		for (int i = 0; i < 5000; i++) {
			Tact tact = new Tact(this);
			tact.setTactNum(i);
			tacts.add(tact);
		}		
	}
	
	/**
	 * Checks the state of the link on the specified period of time.
	 * @param startTime first tact
	 * @param endTime last tact
	 * @param linkNum link number
	 * @return true if at least one tact on the specified period is busy
	 */
	public boolean linkTimeIntervalIsBusy(int startTime, int endTime, int linkNum) {
		boolean busy = false;
		for (int i = startTime; i < endTime; i++) {
			Tact tact = tacts.get(i);
			if (tact.processorLinkIsBusy(linkNum) == true) {
				busy = true;
			}
		}
		return busy;
	}
	
	/**
	 * Almost the same as previous method, but this one works for duplex links and has
	 * additional parameter.
	 * @param startTime first tact
	 * @param endTime last tact
	 * @param linkNum link number
	 * @param inOutState 1 for IN state, 0 for OUT state
	 * @return true if at least one tact on the specified period is busy
	 */
	public boolean linkTimeIntervalIsBusy(int startTime, int endTime, int linkNum, int inOutState) {
		boolean busy = false;
		for (int i = startTime; i < endTime; i++) {
			Tact tact = tacts.get(i);
			if ((tact.processorLinkIsBusy(linkNum) == true) && (tact.getInOutState(linkNum) == inOutState)) {
				busy = true;
			}
		}
		return busy;
	}
	
	/**
	 * Checks the state of the time interval on the specified period of time. 
	 * @param startTime first tact
	 * @param endTime last tact
	 * @return true if at least one tact on the specified period is busy
	 */
	public boolean procTimeIntervalIsBusy(int startTime, int endTime) {
		boolean busy = false;
		for (int i = startTime; i < endTime; i++) {
			if (tacts.get(i).processorIsBusy()) {
				busy = true;
			}
		}
		return busy;
	}	
	
	/**
	 * Adds physical link to the system node.
	 * @param pLink physical link
	 */
	public void addProcessorLink(ProcessorLink pLink) {
		processorLinks.add(pLink);
	}
	
	/**
	 * Paints the graphical representation of the system node.
	 * @param g paints graphics
	 */
	public void paint(Graphics g) {		
		Graphics2D g2 = (Graphics2D)g;
				
		BasicStroke pen = new BasicStroke(2); 
	    g2.setStroke(pen);
		
	    g2.setColor(color);
		systemNode = new Rectangle2D.Double(cx - size / 2, cy - size / 2, size, size);
		g2.fill(systemNode);
		g2.setColor(Color.BLACK);
		
		Rectangle2D contour = new Rectangle2D.Double(cx - size / 2, cy - size / 2, size, size);
		g2.draw(contour);
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
		Font f = new Font("SansSerif", Font.PLAIN, 14);
		f = f.deriveFont(0.3F * (float)size);
		g2.setFont(f);
		FontRenderContext context = g2.getFontRenderContext();
		Rectangle2D bounds = f.getStringBounds(indexStr, context);
		
		double height = bounds.getHeight();
		double width = bounds.getWidth();
		
		double ascent = -bounds.getY();
		double x = cx - size / 2 + (size - width) / 2;		
		double y = cy - size / 2 + (size - height) / 2 + ascent;
		
		g2.drawString(indexStr, (int)x, (int)y);		
	}
}
