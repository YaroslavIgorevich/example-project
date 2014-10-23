package project.graphics;

import javax.swing.JTextArea;

/**
 * This class encapsulates methods to print console information messages.
 * @author Yaroslav
 *
 */
public class ConsolePrinter {
	
	/**
	 * A pointer to the console object.
	 */
	private JTextArea console;
	
	/**
	 * Separator of the information blocks.
	 */
	private final String SEPARATOR = "\n----------------------------------------\n";
	
	/**
	 * Main constructor
	 * @param console console to use
	 */
	public ConsolePrinter(JTextArea console) {
		setConsole(console);
	}
	
	/**
	 * Prints separator.
	 */
	public void printSeparator() {
		console.append(SEPARATOR);
	}
	
	/**
	 * Appends the information message with separator after it.
	 * @param message information message
	 */
	public void printBlock(String message) {
		console.append(message + SEPARATOR);
	}
	
	/**
	 * Appends the information message with "\n"(transport carriage) after it.
	 * @param message information message
	 */
	public void println(String message) {
		console.append(message + "\n");
	}
	
	/**
	 * Simply appends information message.
	 * @param message information message
	 */
	public void print(String message) {
		console.append(message);
	}
	
	/**
	 * Clears console.
	 */
	public void clearConsole() {
		console.setText("");
	}
	
	/**
	 * Accessor to the console field.
	 * @return pointer to the console object
	 */
	public JTextArea getConsole() {
		return console;
	}
	
	/**
	 * Sets a value of a console field.
	 * @param console pointer to the console object
	 */
	public void setConsole(JTextArea console) {
		this.console = console;
	}	
}
