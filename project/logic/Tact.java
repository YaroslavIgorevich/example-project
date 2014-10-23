package project.logic;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class Tact contains information about single tact in system node.
 * @author Yaroslav
 *
 */
public class Tact implements Serializable {
	
	/**
	 * Tact number.
	 */
	private int tactNum;
	
	/**
	 * Computer system node.
	 */
	private SystemNode sysNode;
	
	/**
	 * Scheduled graph node.
	 */
	private GNode scheduledGNode;
	
	/**
	 * Scheduled transmissions for each physical link of processor.
	 */
	private DataTransmission[] scheduledTransmissions;
	
	/**
	 * Array of links state flags.
	 */
	private boolean[] procLinkIsBusy;
	
	/**
	 * This value specifies the direction of data transmission. It is used for duplex links only.
	 */
	private int[] inOut; // 1 - in; 0 - out;
	
	/**
	 * Main constructor.
	 * @param sysNode system node
	 */
	public Tact(SystemNode sysNode) {
		this.sysNode = sysNode;
		int linksNumber = sysNode.getProcessorLinks().size();
		scheduledTransmissions = new DataTransmission[linksNumber];
		procLinkIsBusy = new boolean[linksNumber];
		inOut = new int[linksNumber];
		for (int i = 0; i < linksNumber; i++) {
			inOut[i] = -1;
		}
	}	
	
	/**
	 * Additional constructor.
	 * @param sysNode system node
	 * @param scheduledGNode scheduled graph node
	 * @param scheduledTransmissions scheduled transmissions
	 */
	public Tact(SystemNode sysNode, GNode scheduledGNode,
			DataTransmission[] scheduledTransmissions) {		
		this.sysNode = sysNode;
		this.scheduledGNode = scheduledGNode;
		this.scheduledTransmissions = scheduledTransmissions;
	}
	
	/**
	 * Resets this tact, drops all the flags to default values.
	 */
	public void reset() {
		scheduledGNode = null;
		for (int i = 0; i < ComputerSystem.processorLinkNumber; i++) {
			scheduledTransmissions[i] = null;
			procLinkIsBusy[i] = false;
			inOut[i] = -1;
		}
	}	
	
	/**
	 * Checks whether system node is busy in this tact or not.
	 * @return true if busy
	 */
	public boolean processorIsBusy() {
		if (scheduledGNode != null) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks the state of specified physical link.
	 * @param linkNum
	 * @return
	 */
	public boolean processorLinkIsBusy(int linkNum) {		
		if (procLinkIsBusy[linkNum] == true) {
			return true;
		} 
		return false;
	}
	
	/**
	 * Accessor to the tact number field.
	 * @return tact number
	 */
	public int getTactNum() {
		return tactNum;
	}
	
	/**
	 * Sets a value of the tact number field.
	 * @param tactNum tact number
	 */
	public void setTactNum(int tactNum) {
		this.tactNum = tactNum;
	}
	
	/**
	 * Accessor to the system node field.
	 * @return system node
	 */
	public SystemNode getSysNode() {
		return sysNode;
	}
	
	/**
	 * Sets a value of the system node field.
	 * @param sysNode system node
	 */
	public void setSysNode(SystemNode sysNode) {
		this.sysNode = sysNode;
	}
	
	/**
	 * Accessor to the scheduled graph node field.
	 * @return scheduled gnode
	 */
	public GNode getScheduledGNode() {
		return scheduledGNode;
	}
	
	/**
	 * Sets a value of the scheduled graph node field.
	 * @param scheduledGNode scheduled graph node
	 */
	public void setScheduledGNode(GNode scheduledGNode) {
		this.scheduledGNode = scheduledGNode;
	}
	
	/**
	 * Accessor to the scheduled transmissions field.
	 * @return scheduled transmissions
	 */
	public DataTransmission[] getScheduledTransmissions() {
		return scheduledTransmissions;
	}
	
	/**
	 * Sets a value of the scheduled transmissions field.
	 * @param scheduledTransmissions scheduled transmissions array
	 */
	public void setScheduledTransmissions(DataTransmission[] scheduledTransmissions) {
		this.scheduledTransmissions = scheduledTransmissions;
	}

	/**
	 * Sets schedule transmission to the specified physical link
	 * @param scheduledTransmission scheduled transmission
	 * @param linkNum number of the link for schedule
	 */
	public void setScheduledTransmission(DataTransmission scheduledTransmission, int linkNum) {
		scheduledTransmissions[linkNum] = scheduledTransmission;
		procLinkIsBusy[linkNum] = true;
		inOut[linkNum] = 0;
	}

	/**
	 * Sets busy flag and inOut flag to the specified link.
	 * @param procLinkIsBusy busy flag
	 * @param linkNum link number
	 * @param inOutState inOut flag
	 */
	public void setProcLinkBusyFlag(boolean procLinkIsBusy, int linkNum, int inOutState) {
		this.procLinkIsBusy[linkNum] = procLinkIsBusy;
		inOut[linkNum] = inOutState;
	}
	
	/**
	 * Accessor to the inOut state of the specified physical link.
	 * @param linkNum number of link
	 * @return in out state (0 or 1)
	 */
	public int getInOutState(int linkNum) {
		return inOut[linkNum];
	}
}
