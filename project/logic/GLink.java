package project.logic;

import java.awt.*;
import java.util.ArrayList;
import java.awt.font.FontRenderContext;
import java.awt.geom.*;
import java.io.Serializable;

/**
 * This class describes task graph transition link.
 * @author Yaroslav
 *
 */
public class GLink implements Serializable {
	
	/**
	 * Source graph node.
	 */
	private GNode startGNode;
	
	/**
	 * Destination graph node.
	 */
	private GNode endGNode;
	
	/**
	 * Center X coordinate of the transition link on the workspace.
	 */
	private double cx;
	
	/**
	 * Center Y coordinate of the transition link on the workspace.
	 */
	private double cy;
	
	/**
	 * Arrow is a graphical component of the transition, it specifies the direction
	 * of the transition. This field contains the length value of the arrow side.
	 */
	private double arrowSize;
	
	/**
	 * Angle of the arrow relatively to the transition line.
	 */
	private double arrowAngle;
	
	/**
	 * Auxiliary angle.
	 */
	private double beta;
	
	/**
	 * Transition line.
	 */
	private Line2D line;
	
	/**
	 * Transition communication time. It's an abstract value of the amount of transmitted data.
	 */
	private int tCom;
	
	/**
	 * Communication time string. It is displayed in the center of the transition line.
	 */
	private String message;
	
	/**
	 * Additional color field is used to create transition label.
	 */
	public static Color condition_color;

	private ArrayList<Byte> time;
	
	/**
	 * Main construnctor.
	 * @param tCom communication time
	 * @param startGN source graph node
	 * @param endGN destination gaph node
	 */
	public GLink(int tCom, GNode startGN, GNode endGN) {		
		this.tCom = tCom;
		message = String.valueOf(tCom);		
		startGNode = startGN;
		endGNode = endGN;
		startGN.getNext().add(endGN);
		endGN.getPrev().add(startGN);
		arrowAngle = Math.PI / 7;		
	}
	
	/**
	 * Converts value of communication time to string.
	 */
	public void makeMessage() {		
		message = String.valueOf(tCom);
	}
	
	/**
	 * Accessor to source graph node field.
	 * @return a pointer to a source graph node
	 */
	public GNode getStart() {		
		return startGNode;
	}
	
	/**
	 * Accessor to destination graph node field.
	 * @return a pointer to a destination graph node
	 */
	public GNode getEnd() {		
		return endGNode;
	}
	
	/**
	 * Accessor to communication time field.
	 * @return communication time value
	 */
	public int getTCom() {		
		return tCom;
	}		
	
	/**
	 * Sets a value of a communication time field.
	 * @param tCom communication time value
	 */
	public void setTCom(int tCom) {		
		this.tCom = tCom;
		message = String.valueOf(tCom);
	}		
	
	/**
	 * This method checks whether a small area arount the transition line contains
	 * specified point or not.
	 * @param p point to check
	 * @return true if contains
	 */
	public boolean contains(Point2D p) {		
		if (line.intersects(new Rectangle2D.Double(p.getX() - 5, p.getY() - 5, 10, 10))) return true;		
		return false;
	}
	
	/**
	 * This method is used to paint transition on the workspace between two graph nodes.
	 * It contains line, arrow and transition label.
	 * @param g paints graphics
	 */
	public void paint(Graphics g) {		
		Graphics2D g2 = (Graphics2D)g;		
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		double msg_x;
		double msg_y;
		
		arrowSize = 0.25 * GNode.r;
		
		Font f = new Font("SansSerif", Font.PLAIN, 14);
		f = f.deriveFont(0.3F * (float)GNode.d);
		g2.setFont(f);		
		FontRenderContext context = g2.getFontRenderContext();
		Rectangle2D bounds = f.getStringBounds(message, context);
		double height = bounds.getHeight();
		double width = bounds.getWidth();
		double ascent = -bounds.getY();
					
		double y1 = startGNode.cy;
		double y2 = endGNode.cy;
		double x1 = startGNode.cx;
		double x2 = endGNode.cx;			
		
		Point2D p1 = new Point2D.Double(startGNode.cx, startGNode.cy);
		Point2D p2 = new Point2D.Double(endGNode.cx, endGNode.cy);	
					
		line = new Line2D.Double(p1, p2);
		Rectangle2D line_rect = line.getBounds2D();
		cx = line_rect.getCenterX();
		cy = line_rect.getCenterY();
		msg_x = cx - width / 2;
		msg_y = cy - height / 2 + ascent;		
		Rectangle2D msg_rect = new Rectangle2D.Double(msg_x, cy - height / 2, width, height);		
					
		beta = Math.atan2(y1 - y2, x2 - x1);			
		double x_ = cx + GNode.r * Math.cos(beta);
		double y_ = cy - GNode.r * Math.sin(beta);
		Point2D p3 = new Point2D.Double(x_, y_);
		Point2D p4 = new Point2D.Double(x_ - arrowSize * Math.cos(arrowAngle + beta), y_ + arrowSize * Math.sin(arrowAngle + beta));
		Point2D p5 = new Point2D.Double(x_ - arrowSize * Math.cos(beta - arrowAngle), y_ + arrowSize * Math.sin(beta - arrowAngle));
		Line2D l1 = new Line2D.Double(p3, p4);
		Line2D l2 = new Line2D.Double(p3, p5);
		g2.draw(line);
		g2.setPaint(condition_color);
		g2.fill(msg_rect);
		g2.setColor(Color.BLACK);
		g2.draw(l1);
		g2.draw(l2);
		
		g2.drawString(message, (int)msg_x, (int)(msg_y));
	}
}
