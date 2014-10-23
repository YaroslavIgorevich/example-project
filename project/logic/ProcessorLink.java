package project.logic;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class describes physical link of the computer system node.
 * @author Yaroslav
 *
 */
public class ProcessorLink implements Serializable {
	
	/**
	 * Number of physical link.
	 */
	private int linkNumber;
	
	/**
	 * A list of scheduled to this link transmissions. 
	 */
	private ArrayList<DataTransmission> scheduledTransmissions;
	
	/**
	 * Main constructor.
	 * @param linkNumber physical link number
	 */
	public ProcessorLink (int linkNumber) {
		setLinkNumber(linkNumber);
		scheduledTransmissions = new ArrayList<>();
	}	
	
	/**
	 * Accessor to the link number field.
	 * @return link number
	 */
	public int getLinkNumber() {
		return linkNumber;
	}
	
	/**
	 * Sets a value of the link number field
	 * @param linkNumber link number
	 */
	public void setLinkNumber(int linkNumber) {
		this.linkNumber = linkNumber;
	}
	
	/**
	 * Accessor to the scheduled transmissions list field.
	 * @return scheduled transmissions
	 */
	public ArrayList<DataTransmission> getScheduledTransmissions() {
		return scheduledTransmissions;
	}
	
	/**
	 * Sets a value to the scheduled transmissions field.
	 * @param scheduledTransmissions scheduled transmissions 
	 */
	public void setScheduledTransmissions(
			ArrayList<DataTransmission> scheduledTransmissions) {
		this.scheduledTransmissions = scheduledTransmissions;
	}
	
	/**
	 * This method adds another transmission to the physical link of system node.
	 * @param transmission transmission to add
	 */
	public void addScheduledTransmission(DataTransmission transmission) {
		scheduledTransmissions.add(transmission);
	}
}
