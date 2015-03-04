package cy.ac.ucy.linc.cloudDeployer.beans;

public class NetworkObj {

	private String NetworkName;
	private String NetworkID;

	public NetworkObj(String networkName, String networkID) {
		this.NetworkName = networkName;
		this.NetworkID = networkID;
	}

	public String getNetworkName() {
		return NetworkName;
	}

	public void setNetworkName(String networkName) {
		this.NetworkName = networkName;
	}

	public String getNeworkID() {
		return NetworkID;
	}

	public void setNeworkID(String networkID) {
		this.NetworkID = networkID;
	}

	public String toString() {
		return "Network >> Name: " + this.NetworkName + ", ID: "
				+ this.NetworkID;
	}

	public String toJSON() {
		return null;
	}

}
