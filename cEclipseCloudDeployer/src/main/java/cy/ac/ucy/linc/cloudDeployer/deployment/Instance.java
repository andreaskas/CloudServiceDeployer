package cy.ac.ucy.linc.cloudDeployer.deployment;

public class Instance {

	private String InstanceID;
	private String InstanceName;

	public Instance(String instanceID, String instanceName) {
		this.InstanceID = instanceID;
		this.InstanceName = instanceName;
	}

	public String getInstanceID() {
		return InstanceID;
	}

	public void setInstanceID(String instanceID) {
		this.InstanceID = instanceID;
	}

	public String getInstanceName() {
		return InstanceName;
	}

	public void setInstanceName(String instanceName) {
		this.InstanceName = instanceName;
	}

	public String toString() {
		return "Instance >> id: " + this.InstanceID + ", Name: "
				+ this.InstanceName;
	}

	public String toJSON() {
		return null;
	}
}
