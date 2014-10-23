package project.logic;

/**
 * This is class container of the information about the assigning algorithm type.
 * It is possible to assign task graph to the computer system using 6 different algorithms,
 * which are based on the queue type of the task graph nodes and scheduling algorithm.
 * @author Yaroslav
 *
 */
public class Algorythm {
	
	/**
	 * This value specifies scheduling algorithm type.
	 */
	private int assignType;
	
	/**
	 * This value specifies queue type.
	 */
	private int queueType;
	
	/**
	 * Main constructor
	 * @param assignType assigning algorithm
	 * @param queueType queue type
	 */
	public Algorythm(int assignType, int queueType) {
		super();
		this.assignType = assignType;
		this.queueType = queueType;
	}
	
	/**
	 * Accessor to the scheduling algorithm field.
	 * @return the value of the assigning type field
	 */
	public int getAssignType() {
		return assignType;
	}
	
	/**
	 * Sets a value of assigning type field.
	 * @param assignType code of the algorithm.
	 */
	public void setAssignType(int assignType) {
		this.assignType = assignType;
	}
	
	/**
	 * Accessor to the queue type field.
	 * @return queue type code
	 */
	public int getQueueType() {
		return queueType;
	}
	
	/**
	 * Sets a value of a queue type field.
	 * @param queueType code of the queue type
	 */
	public void setQueueType(int queueType) {
		this.queueType = queueType;
	}
	
	/**
	 * Generates information message about algorithm.
	 * @return information message
	 */
	public String getText() {
		return String.valueOf(assignType) + "-" + String.valueOf(queueType);
	}
}
