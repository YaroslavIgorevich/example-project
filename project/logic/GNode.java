package project.logic;

import java.util.ArrayList;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.*;
import java.io.Serializable;

/**
 * Contains information about task graph node and additional methods 
 * to interact with it.
 * @author Yaroslav
 *
 */
public class GNode implements Serializable {
	
	/**
	 * This static field is used to define IDs of the created graph nodes.
	 */
	public static int nextIndex = 0;
	
	/**
	 * Unique graph node index
	 */
	private int index;
	
	/**
	 * Graph node index string. It is used to display index in the top side of the 
	 * graph node circle.
	 */
	private String indexStr;
	
	/**
	 * Execution time of the node.
	 */
	private int tExe;
	
	/**
	 * Execution time string. It is used to display time in the bottom side of the 
	 * graph node circle. 
	 */
	private String tExeStr;
	
	/**
	 * Assigning flag.
	 */
	private boolean assigned;
	
	/**
	 * Cycles check code.
	 */
	private int cyclesCheckCode;
	
	/**
	 * Critical path time of the node. It's the time of the longest path
	 * from this node to the end of the task graph.
	 */
	private int cpTime;
	
	/**
	 * Critical path graph node number.
	 */
	private int cpGNodeNumber;
	
	/**
	 * Priority of the node. 
	 */
	private double priority;
	
	/**
	 * Grpah node start time(in tacts).
	 */
	private int startTime;
	
	/**
	 * Grpah node end time(in tacts).
	 */
	private int endTime;
	
	/**
	 * Center X coordinate of the node.
	 */
	public double cx;
	
	/**
	 * Center Y coordinate of the node.
	 */
	public double cy;
	
	/**
	 * Radius of the node.
	 */
	public static double r;
	
	/**
	 * Diameter of the node.
	 */
	public static double d;
	
	/**
	 * Default color of the node.
	 */
	private Color color;
	
	/**
	 * Basic circle of the graph node.
	 */
	private Ellipse2D gnode;
	
	/**
	 * This list contains successors of this graph node.
	 */
	private ArrayList<GNode> next;
	
	/**
	 * This list contains ancestry of this graph node.
	 */
	private ArrayList<GNode> prev;
	
	/**
	 * This list stores different routes(depending on queue type) of this graph node.
	 */
	private ArrayList<ArrayList<GNode>> routes;
	
	/**
	 * Specifies computer system node to which this graph node is assigned.
	 */
	private SystemNode assignedSystemNode;
	
	/**
	 * Additional constructor. This constructor is used when we generate multiple
	 * random task graphs.
	 * @param tExe execution time
	 * @param index unique index
	 */
	public GNode(int tExe, int index) {		
		this.index = index;		
		color = Color.LIGHT_GRAY;		
		this.tExe = tExe;			
		indexStr = "G" + Integer.toString(index);
		tExeStr = Integer.toString(tExe);		
		next = new ArrayList<GNode>();
		prev = new ArrayList<GNode>();
		routes = new ArrayList<ArrayList<GNode>>();
	}
	
	/**
	 * Main constructor. This constructor is used when we create new graph nodes on the
	 * workspace.
	 * @param tExe execution time
	 * @param cx center X coordinate of the node
	 * @param cy center Y coordinate of the node
	 */
	public GNode(int tExe, double cx, double cy) {
		index = nextIndex;
		nextIndex++;
		color = Color.LIGHT_GRAY;		
		this.tExe = tExe;
		this.cx = cx;
		this.cy = cy;				
		indexStr = "G" + Integer.toString(index);
		tExeStr = Integer.toString(tExe);		
		next = new ArrayList<GNode>();
		prev = new ArrayList<GNode>();
		routes = new ArrayList<ArrayList<GNode>>();
	}	
	
	/**
	 * Sets a value of a color field.
	 * @param color color of the node
	 */
	public void setColor(Color color) {		
		this.color = color;
	}
	 
	/**
	 * Sets a value of index field.
	 * @param index unique index of the node
	 */
	public void setIndex(int index) {		
		this.index = index; 
		indexStr = "G" + Integer.toString(index);		
	}
	
	/**
	 * Sets a value of the execution time field.
	 * @param tExe execution time
	 */
	public void setTExe(int tExe) {		
		this.tExe = tExe;
		tExeStr = Integer.toString(tExe);		
	}
	
	/**
	 * Sets a value of the critical path time field.
	 * @param priority critical path time value
	 */
	public void setCPTime(int priority) {		
		this.cpTime = priority;		
	}
	
	/**
	 * Sets a value of the start time field.
	 * @param startTime start time of the node
	 */
	public void setStartTime(int startTime) {		
		this.startTime = startTime;
	}
	
	/**
	 * Sets a value of the end time field.
	 * @param endTime end time of the node
	 */
	public void setEndTime(int endTime) {		
		this.endTime = endTime;
	}
	
	/**
	 * Accessor to the index field.
	 * @return index value
	 */
	public int getIndex() {
		return index;
	}	
	
	/**
	 * This method sets a center pair of coordinates. 
	 * @param cx center X coordinate
	 * @param cy center Y coordinate
	 */
	public void setParameters(double cx, double cy) {		
		this.cx = cx;
		this.cy = cy;
	}
	
	/**
	 * Accessor to the execution time field.
	 * @return execution time of the node
	 */
	public int getTExe() {		
		return tExe;
	}
	
	/**
	 * Accessor to the cycles check code field.
	 * @return cycles check code of the node
	 */
	public int getCyclesCheckCode() {
		return cyclesCheckCode;
	}
	
	/**
	 * Accessor to the critical path time field.
	 * @return critical path time
	 */
	public int getCPTime() {		
		return cpTime;
	}
	
	/**
	 * Accessor to the critical path graph node number field.
	 * @return critical path graph node number
	 */
	public int getCPGnodeNumber() {
		return cpGNodeNumber;
	}
	
	/**
	 * Accessor to the priority field.
	 * @return priority value
	 */
	public double getPriority() {
		return priority;
	}
	
	/**
	 * Accessor to the index string field.
	 * @return index string
	 */
	public String getIndexStr() {		
		return indexStr;
	}
	
	/**
	 * Accessor to the start time field.
	 * @return start time value
	 */
	public int getStartTime() {		
		return startTime;
	}
	
	/**
	 * Accessor to the end time field.
	 * @return end time value
	 */
	public int getEndTime() {		
		return endTime;
	}
	
	/**
	 * This method returns a center pair of coordinates as array of two elements.
	 * @return array with coordinates
	 */
	public double[] getParameters() {		
		double[] p = {cx, cy};
		return p;
	}
	
	/**
	 * Accessor to the next nodes list field.
	 * @return a list of the successors nodes
	 */
	public ArrayList<GNode> getNext() {	
		return next;
	}
	
	/**
	 * Accessor to the previous nodes list field.
	 * @return a list of the ancestry nodes
	 */
	public ArrayList<GNode> getPrev() {		
		return prev;
	}
	
	/**
	 * Builds routes of the graph node.
	 * @return a list that contains lists of graph nodes, i.e. paths
	 */
	public ArrayList<ArrayList<GNode>> getRoutes() {		
		routes.clear();
		buildRoutes(new ArrayList<GNode>(), routes, 0);
		return routes;
	}
	 
	/**
	 * Checks whether this node has any ancestry nodes or not
	 * @return true if it has not
	 */
	public boolean isEndNode() {
		if (next.isEmpty()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks whether this node has any successor nodes or not
	 * @return true if it has not
	 */
	public boolean isStartNode() {
		if (prev.isEmpty()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks assigning of the node
	 * @return assigning flag
	 */
	public boolean isAssigned() {
		return assigned;
	}
	
	/**
	 * Sets the value of the assign flag.
	 * @param assigned true if the node is assigned
	 */
	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}
	
	/**
	 * Accessor to the assigned system node field.
	 * @return a pointer to the assigned system node
	 */
	public SystemNode getAssignedSystemNode() {
		return assignedSystemNode;
	}
	
	/**
	 * Sets a value of the assigned system node field.
	 * @param assignedSystemNode a pointer to the assgned system node
	 */
	public void setAssignedSystemNode(SystemNode assignedSystemNode) {
		this.assignedSystemNode = assignedSystemNode;
	}
	
	/**
	 * Check whether ancestry of this node are assigned or not.
	 * @return true if ancestry are assigned
	 */
	public boolean ancestryAreAssigned() {
		boolean areAssigned = true;
		
		for (GNode ancestor : prev) {
			if (ancestor.isAssigned() == false) {
				areAssigned = false;
			}
		}
		return areAssigned;
	}	
	
	/**
	 * Builds routes of the node in both forward or reverse direction(depends on parameter).
	 * @param route a list to store current path
	 * @param routes a list of several paths
	 * @param direction 0 for forward routes, 1 for backward
	 */
	private void buildRoutes(ArrayList<GNode> route, ArrayList<ArrayList<GNode>> routes, int direction) {		
		route.add(this);
		
		ArrayList<GNode> lookupList = null;
		
		if (direction == 0) {
			lookupList = next;
		} else if (direction == 1) {
			lookupList = prev;
		}		
		
		if (lookupList.isEmpty()) {			
			routes.add(route);
			return;
		}
		
		for (int i = 0 ; i < lookupList.size(); i++) {			
			ArrayList<GNode> tmp = (ArrayList<GNode>)route.clone();
			lookupList.get(i).buildRoutes(tmp, routes, direction);
		}
	}
	
	/**
	 * Calculates priority of the node according to queue type with code 1.
	 * @param graphCPTime critical path time of the graph
	 * @param graphCPGnodeNumber number of the graph nodes on the path
	 */
	public void calculatePriority(int graphCPTime, int graphCPGnodeNumber) {
		priority = (double)cpTime / graphCPTime + (double)cpGNodeNumber / graphCPGnodeNumber;
	}
	
	/**
	 * Calculates critical path for time and nodes number.
	 * @param direction direction of the path
	 * @param queueType queue type code value
	 */
	public void calculateCriticalPaths(int direction, int queueType) {
		routes.clear();
		buildRoutes(new ArrayList<GNode>(), routes, direction);
		int[] routesTime = new int[routes.size()];
		int[] routesNodesNumber = new int[routes.size()];
		int startIndex = 0;
		
		if (queueType == 16) {
			startIndex = 1;
		}
		
		for (int i = 0; i < routes.size(); i++) {				
			ArrayList<GNode> route = routes.get(i);
			
			if ((queueType == 16) && (route.size() == 1)) {
				routesTime[i] = 0;
			} else {
				for (int j = startIndex; j < route.size(); j++) {					
					routesTime[i] += route.get(j).getTExe();
					routesNodesNumber[i]++;
				}
			}								
		}
		
		cpTime = routesTime[0];
		cpGNodeNumber = routesNodesNumber[0];
		
		for (int i = 1; i < routesTime.length; i++) {			
			if (routesTime[i] > cpTime) cpTime = routesTime[i];
			if (routesNodesNumber[i] > cpGNodeNumber) cpGNodeNumber = routesNodesNumber[i];
		}
	}		
	
	/**
	 * Checks task graph for cycles. Counts the quantity of the cycles.
	 * @param list
	 */
	public void checkCycles(ArrayList<GNode> list) {
		if (list.contains(this)) {
			cyclesCheckCode++;
			return;			
		}
		list.add(this);
		if (prev.isEmpty()) {
			return;
		}
		for (int i = 0; i < prev.size(); i++) {	
			ArrayList<GNode> tmp = (ArrayList<GNode>)list.clone();			
			prev.get(i).checkCycles(tmp); 
		}
	}
	
	/**
	 * This method searches for cycles in the task graph and store them into list.
	 * @param list current path to check cycles
	 * @param cycles list of cycles
	 * @param direction 0 or 1 depending on the direction of the checking
	 */
	public void findCycles(ArrayList<GNode> list, ArrayList<ArrayList<GNode>> cycles, int direction) {		
		if (list.contains(this)) {			
			ArrayList<GNode> cyclesList = (ArrayList<GNode>)list.clone();
			
			while (cyclesList.get(0) != this) cyclesList.remove(0);
			
			if (!cycles.contains(cyclesList)) cycles.add(cyclesList);
			return;
		}
		
		list.add(this);
		
		ArrayList<GNode> listForCheck;
		
		if (direction == 0) {
			listForCheck = prev;
		} else {
			listForCheck = next;
		}
		
		if (prev.isEmpty()) {
			routes.add(list);			
			return;
		}	
		
		for (int i = 0; i < listForCheck.size(); i++)
		{	
			ArrayList<GNode> tmp = (ArrayList<GNode>)list.clone();			
			listForCheck.get(i).findCycles(tmp, cycles, direction); 
		}
	}
	
	/**
	 * This method is used to check whether graph node contains specified point or not.
	 * @param p point to check
	 * @return true if contains
	 */
	public boolean contains(Point2D p) {		
		if (gnode.contains(p)) return true;
		return false;
	}
	
	/**
	 * This method is used to paint task graph node. It contains two parts: index on the top, and
	 * execution time value on the bottom.
	 * @param g paints graphics
	 */
	public void paint(Graphics g) {  		
		Graphics2D g2 = (Graphics2D)g;
				
		BasicStroke pen = new BasicStroke(2); 
	    g2.setStroke(pen);
		
	    g2.setColor(color);
		gnode = new Ellipse2D.Double(cx - r, cy - r, d, d);
		g2.fill(gnode);
		
		g2.setColor(Color.BLACK);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		Line2D l = new Line2D.Double(cx - r, cy, cx + r, cy);
		Ellipse2D contour = new Ellipse2D.Double(cx - r, cy - r, d, d);
		
		g2.draw(l);
		g2.draw(contour);
		
		Font f = new Font("SansSerif", Font.PLAIN, 14);
		f = f.deriveFont(0.3F * (float)d);
		g2.setFont(f);
		FontRenderContext context = g2.getFontRenderContext();
		Rectangle2D bounds = f.getStringBounds(indexStr, context);
		Rectangle2D bounds2 = f.getStringBounds(tExeStr, context);
		
		double height = bounds.getHeight();
		double width = bounds.getWidth();
		double width2 = bounds2.getWidth();		
		double ascent = -bounds.getY();
		double x = cx - r + (d - width) / 2;
		double x2 = cx - r + (d - width2) / 2;		
		double y1 = cy - r + (d - height) / 2 + ascent - r / 2;
		double y2 = cy - r + (d - height) / 2 + ascent + r / 2;		
		g2.drawString(indexStr, (int)x, (int)y1);
		g2.drawString(tExeStr, (int)x2, (int)y2);
			
	}
}
