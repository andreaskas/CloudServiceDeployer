package cy.ac.ucy.linc.cloudDeployer.beans;

public class NetworkObj {

	private String LocalIP;
	private String ExternalIP;

	public NetworkObj(String localiP, String externaliP) {
		this.LocalIP = localiP;
		this.ExternalIP = externaliP;
	}

	public String getLocalIP() {
		return LocalIP;
	}

	public void setLocalIP(String localIP) {
		this.LocalIP = localIP;
	}

	public String getExternalIP() {
		return ExternalIP;
	}

	public void setExternalIP(String externalIP) {
		this.ExternalIP = externalIP;
	}

	public String toString() {
		return "Network >> LocalIP: " + this.LocalIP + ", ExternalIP: "
				+ this.ExternalIP;
	}

	public String toJSON() {
		return null;
	}

}
