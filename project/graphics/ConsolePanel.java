package project.graphics;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * A component of the console. It is used to print information
 * messages.
 * @author Yaroslav
 *
 */
public class ConsolePanel extends JPanel {	
	
	/**
	 * Text area object.
	 */
	private JTextArea console;
	
	/**
	 * Main constructor.
	 */
	public ConsolePanel() {
		setLayout(new BorderLayout());
		console = new JTextArea(8,40);
		console.setLineWrap(true);
		console.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(console);
		add(scrollPane);
	}
	
	/**
	 * Accessor to the console field. 
	 * @return pointer to the console object
	 */
	public JTextArea getConsole() {
		return console;
	}
}
