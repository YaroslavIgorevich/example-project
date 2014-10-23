package project.logic;

import java.io.Serializable;

/**
 * This class contains information about data transmission between two 
 * computer system nodes.
 * @author Yaroslav
 *
 */
public class DataTransmission implements Serializable {
	
	/**
	 * Start time of transmission(in tacts)
	 */
	private int startTime;
	
	/**
	 * End time of transmission(in tacts)
	 */
	private int endTime;
	
	/**
	 * Transmission total time(in tacts)
	 */
	private int transmissionTime;
	
	/**
	 * Source task graph node.
	 */
	private GNode srcGNode;
	
	/**
	 * Destination task graph node.
	 */
	private GNode destGNode;
	
	/**
	 * Source system node.
	 */
	private SystemNode srcSystemNode;
	
	/**
	 * Destination system node.
	 */
	private SystemNode destSystemNode;
	
	/**
	 * Number of physical links of the source system node
	 */
	private int srcSystemNodeLinkNum;
	
	/**
	 * Number of physical links of the destination system node
	 */
	private int destSystemNodeLinkNum;	
	
	/**
	 * Additional constructor.
	 * @param startTime start time of transmission(in tacts)
	 * @param endTime end time of transmission(in tacts)
	 * @param transmissionTime transmission total time(in tacts)
	 */
	public DataTransmission(int startTime, int endTime, int transmissionTime) {		
		this.startTime = startTime;
		this.endTime = endTime;
		this.transmissionTime = transmissionTime;
	}	
	
	/**
	 * Main constructor.
	 * @param startTime start time of transmission(in tacts)
	 * @param endTime end time of transmission(in tacts)
	 * @param srcGNode source graph node
	 * @param destGNode destination graph node
	 * @param srcSystemNode source system node
	 * @param destSystemNode destination system node
	 * @param srcSystemNodeLinkNum number of the physical links of source system node
	 * @param destSystemNodeLinkNum number of the physical links of destination system node
	 */
	public DataTransmission(int startTime, int endTime, GNode srcGNode, GNode destGNode, 
			SystemNode srcSystemNode, SystemNode destSystemNode, int srcSystemNodeLinkNum,
			int destSystemNodeLinkNum) {		
		this.startTime = startTime;
		this.endTime = endTime;
		transmissionTime = endTime - startTime;
		this.srcGNode = srcGNode;
		this.destGNode = destGNode;
		this.srcSystemNode = srcSystemNode;
		this.destSystemNode = destSystemNode;
		this.srcSystemNodeLinkNum = srcSystemNodeLinkNum;
		this.destSystemNodeLinkNum = destSystemNodeLinkNum;
	}
	
	/**
	 * Accessor to the start time field.
	 * @return start time of the transmission
	 */
	public int getStartTime() {
		return startTime;
	}
	
	/**
	 * Sets a value of a start time field.
	 * @param startTime start time value
	 */
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	
	/**
	 * Accessor to the end time field.
	 * @return end time of the transmission
	 */
	public int getEndTime() {
		return endTime;
	}
	
	/**
	 * Sets a value of a end time field.
	 * @param endTime end time value
	 */
	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}
	
	/**
	 * Accessor to the total transmission time field.
	 * @return total time of the transmission
	 */
	public int getTransmissionTime() {
		return transmissionTime;
	}
	
	/**
	 * Sets a value of a total transmission time field.
	 * @param transmissionTime total transmission time value
	 */
	public void setTransmissionTime(int transmissionTime) {
		this.transmissionTime = transmissionTime;
	}
	
	/**
	 * Accessor to the source graph node field.
	 * @return source pointer to a source graph node 
	 */
	public GNode getSrcGNode() {
		return srcGNode;
	}
	
	/**
	 * Sets a value of a source graph node field.
	 * @param srcGNode a pointer to a source graph node field
	 */
	public void setSrcGNode(GNode srcGNode) {
		this.srcGNode = srcGNode;
	}
	
	/**
	 * Accessor to the destination graph node field.
	 * @return source pointer to a source graph node 
	 */
	public GNode getDestGNode() {
		return destGNode;
	}
	
	/**
	 * Sets a value of a destination graph node field.
	 * @param destGNode a pointer to a destination graph node field
	 */
	public void setDestGNode(GNode destGNode) {
		this.destGNode = destGNode;
	}
	
	/**
	 * Accessor to the source system node field.
	 * @return source pointer to a source system node 
	 */
	public SystemNode getSrcSystemNode() {
		return srcSystemNode;
	}
	
	/**
	 * Sets a value of a source system node field.
	 * @param srcSystemNode a pointer to a source system node field
	 */
	public void setSrcSystemNode(SystemNode srcSystemNode) {
		this.srcSystemNode = srcSystemNode;
	}
	
	/**
	 * Accessor to the destination system node field.
	 * @return source pointer to a destination system node 
	 */
	public SystemNode getDestSystemNode() {
		return destSystemNode;
	}
	
	/**
	 * Sets a value of a destination system node field.
	 * @param destSystemNode a pointer to a destination system node field
	 */
	public void setDestSystemNode(SystemNode destSystemNode) {
		this.destSystemNode = destSystemNode;
	}
	
	/**
	 * Accessor to the source system node physical links number field.
	 * @return number of the source system node physical links 
	 */
	public int getSrcSystemNodeLinkNum() {
		return srcSystemNodeLinkNum;
	}
	
	/**
	 * Sets a value of a source system node physical links number field.
	 * @param srcSystemNodeLinkNum a number of physical links of source system node
	 */
	public void setSrcSystemNodeLinkNum(int srcSystemNodeLinkNum) {
		this.srcSystemNodeLinkNum = srcSystemNodeLinkNum;
	}
	
	/**
	 * Accessor to the destination system node physical links number field.
	 * @return number of the destination system node physical links 
	 */
	public int getDestSystemNodeLinkNum() {
		return destSystemNodeLinkNum;
	}
	
	/**
	 * Sets a value of a destination system node physical links number field.
	 * @param destSystemNodeLinkNum a number of physical links of destination system node
	 */
	public void setDestSystemNodeLinkNum(int destSystemNodeLinkNum) {
		this.destSystemNodeLinkNum = destSystemNodeLinkNum;
	}
	
	/**
	 * Generates transmission information message.
	 * @return information message
	 */
	public String getTransmissionText() {
		return srcGNode.getIndexStr() + "->" + destGNode.getIndexStr() + 
				"[" + destSystemNode.getIndexStr() + "(" + destSystemNodeLinkNum + ")]";
	}
}
