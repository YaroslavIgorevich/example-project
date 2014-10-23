package project.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import javax.swing.*;

import project.logic.GLink;

/**
 * Main component, on which other components are located.
 * @author Yaroslav
 *
 */
public class MainFrame extends JFrame {	 	
	
	/**
	 * A pointer to the graph panel object.
	 */
	private GraphPanel graphPanel;
	
	/**
	 * A pointer to the computer system object.
	 */
	private ComputerSystemPanel computerSystemPanel;
	
	/**
	 * A pointer to the console panel object.
	 */
	private ConsolePanel consolePanel;
	
	/**
	 * A pointer to the schedule panel object.
	 */
	private SchedulePanel schedulePanel;
	
	/**
	 * Main constructor.
	 */
	public MainFrame() {				
		setExtendedState(Frame.MAXIMIZED_BOTH);		
		setTitle("Scheduler");		
		
		consolePanel = new ConsolePanel();		
		computerSystemPanel = new ComputerSystemPanel();
		computerSystemPanel.setBackground(Color.WHITE);
		
		schedulePanel = new SchedulePanel();
		schedulePanel.setBackground(Color.WHITE);
		
		graphPanel = new GraphPanel(computerSystemPanel, consolePanel, schedulePanel);
		graphPanel.setBackground(Color.WHITE);
		GLink.condition_color = graphPanel.getBackground();						
		
		Box horizontalBox = Box.createHorizontalBox();
		horizontalBox.add(Box.createHorizontalStrut(10));
		horizontalBox.add(graphPanel);
		horizontalBox.add(Box.createHorizontalStrut(10));
		horizontalBox.add(computerSystemPanel);
		horizontalBox.add(Box.createHorizontalStrut(10));		
		
		Box verticalBox = Box.createVerticalBox();
		verticalBox.add(horizontalBox);
		verticalBox.add(Box.createVerticalStrut(10));
		
		JPanel editorPanel = new JPanel();
		editorPanel.setLayout(new BorderLayout());
		editorPanel.add(verticalBox, BorderLayout.CENTER);
		editorPanel.add(consolePanel, BorderLayout.SOUTH);
		
		GraphGeneratorPanel ggp = new GraphGeneratorPanel(computerSystemPanel, graphPanel);		
				
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Editor", editorPanel);		
		tabbedPane.addTab("Generator", ggp);
		tabbedPane.addTab("Schedule", schedulePanel);
		
		add(tabbedPane);						
	}
}
