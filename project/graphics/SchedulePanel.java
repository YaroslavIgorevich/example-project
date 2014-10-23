package project.graphics;

import java.awt.*;

import java.awt.font.FontRenderContext;
import java.awt.geom.*;
import java.util.ArrayList;
import javax.swing.JPanel;
import project.logic.ComputerSystem;
import project.logic.DataTransmission;
import project.logic.GNode;
import project.logic.ProcessorLink;
import project.logic.SystemNode;

/**
 * This component graphically displays schedule diagram, which is based
 * on the task graph, computer system topology and specified parameters(algorithm
 * type, links number etc.). Schedule diagram is a result of assigning task graph on the
 * computer system.
 * @author Yaroslav
 *
 */
public class SchedulePanel extends JPanel {
	
	/**
	 * Maximum X coordinate.
	 */
	private int xMax;
	
	/**
	 * Maximum Y coordinate.
	 */
	private int yMax;
	
	/**
	 * Number of the nodes of the computer system.
	 */
	private int N;
	
	/**
	 * Maximum tact number. It can be calculated by adding execution time value of each 
	 * node of the task graph.
	 */
	private int maxTactNumber;
	
	/**
	 * This value specifies the height of each computer system node section.
	 */
	private double nodeSectionHeight;
	
	/**
	 * This value specifies the height of the schedule diagram bars.
	 */
	private double tactHeight;
	
	/**
	 * The width of the single tact.
	 */
	private double tactWidth;
	
	/**
	 * Left intend.
	 */
	private static final double LEFT_INTEND = 80.0;
	
	/**
	 * Top intend.
	 */
	private static final double TOP_INTEND = 40.0;
	
	/**
	 * Bottom intend.
	 */
	private static final double BOTTOM_INTEND = 40.0;
	
	/**
	 * Right intend.
	 */
	private static final double RIGHT_INTEND = 20.0;
	
	/**
	 * Bottom intend for graduation.
	 */
	private static final int BOTTOM_GRAD_INTEND = 10;
	
	/**
	 * Default number of tacts.
	 */
	private static final int DEFAULT_TACT_NUM = 30;
	
	/**
	 * Color, specified for task bars.
	 */
	private static final Color taskColor = new Color(46, 184, 0);
	
	/**
	 * Color, specified for transmission bars.
	 */
	private static final Color transmissionColor = new Color(255, 36, 36);
	
	/**
	 * Start of the coordinates.
	 */
	private Line2D startLine;	
	
	/**
	 * List of the computer system nodes.
	 */
	private ArrayList<SystemNode> systemNodesList;
	
	/**
	 * Main constructor.
	 */
	public SchedulePanel() {			
		systemNodesList = new ArrayList<>();
	}
	
	/**
	 *  A method to paint component.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		xMax = getWidth();
		yMax = getHeight();	
		
		if (!systemNodesList.isEmpty()) {			
			paintDiagram(g2);
			paintStartLine(g2);
		} else {
			paintScale(g2, DEFAULT_TACT_NUM);
		}
	}
	
	/**
	 * Sets a value of a system nodes list field.
	 * @param systemNodesList computer system nodes list
	 */
	public void setSystemNodesList(ArrayList<SystemNode> systemNodesList) {
		this.systemNodesList = systemNodesList;
		N = systemNodesList.size();		
		repaint();
	}
	
	/**
	 * This method paints a scale for diagram
	 * @param g2 object, which paints graphics
	 * @param tactNumber number of tacts for scale
	 */
	private void paintScale(Graphics2D g2, int tactNumber) {		
		tactWidth = (xMax - LEFT_INTEND - RIGHT_INTEND) / tactNumber;
		
		float[] dashPattern = {5, 10};		
		BasicStroke gridStroke = new BasicStroke(1.0F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0F, dashPattern, 0);
		
		g2.setStroke(gridStroke);				
		
		Font f = new Font("SansSerif", Font.PLAIN, 7);
		f = f.deriveFont(0.02F * (float)yMax);
		g2.setFont(f);
		FontRenderContext context = g2.getFontRenderContext();
		
		for (int i = 1; i < tactNumber + 1; i++) {
			double xCoord = LEFT_INTEND + i * tactWidth;
			Line2D gridLine = new Line2D.Double(xCoord, 0, xCoord, yMax);
			
			String graduation = String.valueOf(i);
			Rectangle2D bounds = f.getStringBounds(graduation, context);			
			double width = bounds.getWidth();
			double height = bounds.getHeight();
			double ascent = -bounds.getY();
			double x = gridLine.getX1() - width / 2;
			
			Rectangle2D messageRect = new Rectangle2D.Double(x, yMax - BOTTOM_GRAD_INTEND - ascent, width, height);
					
			g2.setColor(Color.LIGHT_GRAY);
			g2.draw(gridLine);						
			
			g2.setColor(Color.WHITE);
			g2.fill(messageRect);
			g2.setColor(Color.BLACK);		
			
			g2.drawString(graduation, (int)x, yMax - BOTTOM_GRAD_INTEND);						
		}		
	}	
	
	/**
	 * Main method to paint schedule diagram. It uses the data from the computer system nodes 
	 * objects.
	 * @param g2 object, which paints graphics
	 */
	private void paintDiagram(Graphics2D g2) {
		maxTactNumber = findMaxTactNumber();
		tactWidth = (xMax - LEFT_INTEND - RIGHT_INTEND) / maxTactNumber;
		nodeSectionHeight = (yMax - TOP_INTEND - BOTTOM_INTEND) / N;
		int subsectionsNumber = ComputerSystem.processorLinkNumber + 1;		
		tactHeight = nodeSectionHeight / subsectionsNumber;
		
		paintScale(g2, maxTactNumber);				
		
		Font f = new Font("SansSerif", Font.PLAIN, 7);
		f = f.deriveFont(0.03F * (float)yMax);
		g2.setFont(f);
		FontRenderContext context = g2.getFontRenderContext();
		
		for (int i = 0; i < N; i++) {
			SystemNode sysNode = systemNodesList.get(i);
			String systemNodeIndexStr = sysNode.getIndexStr();
			
			Rectangle2D bounds = f.getStringBounds(systemNodeIndexStr, context);			
			double width = bounds.getWidth();
			double height = bounds.getHeight();
			double ascent = -bounds.getY();
			
			double x = (LEFT_INTEND - width) / 2;
			double y = TOP_INTEND + nodeSectionHeight * i + (nodeSectionHeight - height) / 2 + ascent;
			
			g2.drawString(systemNodeIndexStr, (int)x, (int)y);			
			
			g2.setStroke(new BasicStroke(1.0F));
			
			for (GNode gnode : sysNode.getScheduledNodes()) {
				double xTask = LEFT_INTEND + gnode.getStartTime() * tactWidth;
				double yTask = TOP_INTEND + nodeSectionHeight * i;				
				double taskWidth = gnode.getTExe() * tactWidth;
				String taskText = gnode.getIndexStr();				
				paintTimelineBar(0, xTask, yTask, taskWidth, taskText, g2, context, f);					
			}
			
			ArrayList<DataTransmission> scheduledTransmissions = new ArrayList<>();
			
			for (ProcessorLink pLink : sysNode.getProcessorLinks()) {
				ArrayList<DataTransmission> transmissions = pLink.getScheduledTransmissions();
				for (DataTransmission transmission : transmissions) {
					scheduledTransmissions.add(transmission);
				}
			}
			
			for (DataTransmission dTrans : scheduledTransmissions) {
				double xTrans = LEFT_INTEND + dTrans.getStartTime() * tactWidth;
				double yTrans = TOP_INTEND + nodeSectionHeight * i + (dTrans.getSrcSystemNodeLinkNum() + 1) * tactHeight;
				double transWidth = dTrans.getTransmissionTime() * tactWidth;
				String transText = dTrans.getTransmissionText();
				paintTimelineBar(1, xTrans, yTrans, transWidth, transText, g2, context, f);
			}			
		}
				
		for (int i = 0; i < N + 1; i++) {	
			g2.setStroke(new BasicStroke(3.0F));
			double yCoord = TOP_INTEND + i * nodeSectionHeight;
			Line2D sectionLine = new Line2D.Double(0, yCoord, xMax, yCoord);
			g2.draw(sectionLine);			
			
			for (int j = 1; j < subsectionsNumber && i < N; j++) {
				g2.setStroke(new BasicStroke(2.0F));
				double ySubCoord = yCoord + j * tactHeight;
				Line2D subsectionLine = new Line2D.Double(LEFT_INTEND, ySubCoord, xMax, ySubCoord);
				g2.draw(subsectionLine);
			}
		}
		paintStartLine(g2);
	}
	
	/**
	 * This method paints start of the scale.
	 * @param g2 object, which paints graphics
	 */
	private void paintStartLine(Graphics2D g2) {
		startLine = new Line2D.Double(LEFT_INTEND, 0, LEFT_INTEND, yMax);
		g2.draw(startLine);
	}
	
	/**
	 * This method is used to paint schedule diagram bars.
	 * @param typeOfBar task bars or transmission bars
	 * @param xCoord x coordinate of the top left corner of the bar
	 * @param yCoord y coordinate of the top left corner of the bar
	 * @param width width of the bar
	 * @param text information text of the bar
	 * @param g2 object, which paints graphics
	 * @param context an object to correctly measure text
	 * @param f specifies font type of the text
	 */
	private void paintTimelineBar(int typeOfBar, double xCoord, double yCoord, 
			double width, String text, Graphics2D g2, FontRenderContext context, Font f) {
		Rectangle2D timelineBar = new Rectangle2D.Double(xCoord, yCoord, width, tactHeight);
		Rectangle2D barBounds = (Rectangle2D)timelineBar.clone();
		
		if (typeOfBar == 0) {
			g2.setColor(taskColor);
		} else if (typeOfBar == 1) {
			g2.setColor(transmissionColor);
		}		
		g2.fill(timelineBar);
		g2.setColor(Color.BLACK);
		g2.draw(barBounds);		

		Rectangle2D textBounds = f.getStringBounds(text, context);			
		double boundsWidth = textBounds.getWidth();
		double boundsHeight = textBounds.getHeight();
		double ascent = -textBounds.getY();
		double timelineStrX = timelineBar.getX() + (timelineBar.getWidth() - boundsWidth) / 2;
		double timelineStrY = yCoord + (tactHeight - boundsHeight) / 2 + ascent;
		g2.drawString(text, (int)timelineStrX, (int)timelineStrY);
	}	
	
	/**
	 * This method is used to find maximum tacts number to buid diagram.
	 * @return maximum tacts number value
	 */
	private int findMaxTactNumber() {
		int[] totalTimeArray = new int[systemNodesList.size()];
		
		for (int i = 0; i < totalTimeArray.length; i++) {
			totalTimeArray[i] = systemNodesList.get(i).getLastTaskEndTime();			
		}
		
		int maxTactNumber = totalTimeArray[0];
		
		for (int i = 1; i < totalTimeArray.length; i++) {
			if (totalTimeArray[i] > maxTactNumber) {
				maxTactNumber = totalTimeArray[i];
			}
		}		
		return maxTactNumber;
	}
}
