package cy.ac.ucy.linc.cloudDeployer.beans;

public class Instance {

	private String InstanceID;
	private String InstanceName;
	private String Status;
	private String IPs;
	private String URI;

	public Instance(String instanceID, String instanceName, String ips,
			String status, String uri) {
		this.InstanceID = instanceID;
		this.InstanceName = instanceName;
		this.IPs = ips;
		this.Status = status;
		this.URI = uri;
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

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getIPs() {
		return IPs;
	}

	public void setIPs(String iPs) {
		IPs = iPs;
	}

	public String getURI() {
		return URI;
	}

	public void setURI(String uRI) {
		URI = uRI;
	}

	public String toString() {
		return "Instance >> id: " + this.InstanceID + ", Name: "
				+ this.InstanceName + ", Ips: " + this.IPs + ", Status: "
				+ this.Status + ", URI: " + this.URI;
	}

	public String toJSON() {
		return null;
	}
}
