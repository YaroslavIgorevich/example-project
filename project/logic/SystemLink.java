package project.logic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

/**
 * This class describes logical connection between two system nodes. 
 * @author Yaroslav
 *
 */
public class SystemLink implements Serializable {
	
	/**
	 * First system node.
	 */
	private SystemNode firstNode;
	
	/**
	 * Second system node.
	 */
	private SystemNode secondNode;
	
	/**
	 * Line, which represents this connection graphically on the workspace.
	 */
	private Line2D line;
	 
	/**
	 * Main constructor.
	 * @param firstNode first node
	 * @param secondNode second node
	 */
	public SystemLink(SystemNode firstNode, SystemNode secondNode) {		
		this.firstNode = firstNode;
		this.secondNode = secondNode;		
		firstNode.getNeighbours().add(secondNode);
		secondNode.getNeighbours().add(firstNode);
	}	
	
	/**
	 * Accessor to the first system node field.
	 * @return first system node
	 */
	public SystemNode getFirstNode() {
		return firstNode;
	}
	
	/**
	 * Sets a value of the first node field.
	 * @param firstNode first system node
	 */
	public void setFirstNode(SystemNode firstNode) {
		this.firstNode = firstNode;
	}
	
	/**
	 * Accessor to the second system node field.
	 * @return second system node
	 */
	public SystemNode getSecondNode() {
		return secondNode;
	}
	
	/**
	 * Sets a value of the second system node field.
	 * @param secondNode second system node
	 */
	public void setSecondNode(SystemNode secondNode) {
		this.secondNode = secondNode;
	}
	
	/**
	 * Checks whether system link contains specified point or not.
	 * @param p point to check
	 * @return true if contains
	 */
	public boolean contains(Point2D p) {		
		if (line.intersects(new Rectangle2D.Double(p.getX() - 5, p.getY() - 5, 10, 10))) return true;		
		return false;
	}
	
	/**
	 * Paints system link on the workspace.  
	 * @param g paints graphics
	 */
	public void paint(Graphics g) {		
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(Color.BLACK);
		line = new Line2D.Double(firstNode.cx, firstNode.cy, secondNode.cx, secondNode.cy);
		g2.draw(line);
	}
}
