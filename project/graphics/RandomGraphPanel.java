package project.graphics;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.JPanel;
import project.logic.GLink;
import project.logic.GNode;

/**
 * This component is used to graphically display randomly generated
 * task graphs. 
 * @author Yaroslav
 *
 */
public class RandomGraphPanel extends JPanel {		
	
	/**
	 * Maximum X coordinate.
	 */
	private int xMax;
	
	/**
	 * Maximum Y coordinate.
	 */
	private int yMax;
	
	/**
	 * X coordinate of the center of the component.
	 */
	private double centerX;
	
	/**
	 * Y coordinate of the center of the component.
	 */
	private double centerY;
	
	/**
	 * On this panel graph nodes locate on the circle. This field
	 * contains the value of the circle radius.
	 */
	private double R;
	
	/**
	 * An auxiliary angle to locate graph nodes on the circle. 
	 */
	private double alpha;
	
	/**
	 * List of the graph nodes.
	 */
	private ArrayList<GNode> gnodeList;
	
	/**
	 * List of the graph transitions.
	 */
	private ArrayList<GLink> linkList;		
	
	/**
	 * Main constructor.
	 */
	public RandomGraphPanel() {		
		gnodeList = new ArrayList<GNode>();
		linkList = new ArrayList<GLink>();					
	}
	
	/**
	 *  A method to paint component.
	 */
	public void paintComponent(Graphics g) {		
		super.paintComponent(g);				
		
		xMax = getWidth();
		yMax = getHeight();		
		
		centerX = xMax / 2;
		centerY = yMax / 2;
		
		scaling();
		
		for (GLink link : linkList) {
			link.paint(g);
		}
		
		for (GNode gnode : gnodeList) {
			gnode.paint(g);
		}
	}
	
	/**
	 * Sets a value of a nodes list field.
	 * @param gList list of the graph nodes
	 */
	public void setGNodeList(ArrayList<GNode> gList) {		
		gnodeList.clear();
		
		for (int i = 0; i < gList.size(); i++) {			
			gnodeList.add(gList.get(i));
		}	
		repaint();
	}
	
	/**
	 * Sets a value of a transition list field.
	 * @param gList list of the transitions
	 */
	public void setLinkList(ArrayList<GLink> lList) {		
		linkList.clear();
		
		for (int i = 0; i < lList.size(); i++) {			
			linkList.add(lList.get(i));
		}	
		repaint();
	}	
	
	/**
	 * This method generates the coordinates of the graph nodes based on
	 * the auxiliary angle and circle radius.
	 */
	public void setCoordinates() {		
		alpha = 2 * Math.PI / gnodeList.size();
		
		for (int i = 0; i < gnodeList.size(); i++) {			
			GNode g_n = gnodeList.get(i);
			
			g_n.cx = centerX + R * Math.cos(alpha * i);
			g_n.cy = centerY - R * Math.sin(alpha * i);
		}			
	}
	
	/**
	 * Scales the size of the graph nodes relatively to the height of the screen.
	 */
	public void scaling() {	
		GNode.r = 0.05 * yMax;
		GNode.d = 2 * GNode.r;		
		R = 0.3 * yMax;			
		setCoordinates();
	}	
}
