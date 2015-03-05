package cy.ac.ucy.linc.cloudDeployer.beans;

public class FlavorObj {

	private String FlavorID;
	private int FlavorRAM;
	private String FlavorType;
	private int FlavorVCPU;
	private int FlavorHDD;

	public FlavorObj(String flavorID, String flavorType, int flavorVCPU,
			int flavorRAM, int flavorHDD) {
		this.FlavorID = flavorID;
		this.FlavorRAM = flavorRAM;
		this.FlavorType = flavorType;
		this.FlavorVCPU = flavorVCPU;
		this.FlavorHDD = flavorHDD;
	}

	public String getFlavorID() {
		return FlavorID;
	}

	public void setFlavorID(String flavorID) {
		this.FlavorID = flavorID;
	}

	public int getRAM() {
		return FlavorRAM;
	}

	public void setRAM(int rAM) {
		this.FlavorRAM = rAM;
	}

	public String getFlavorType() {
		return FlavorType;
	}

	public void setFlavorType(String flavorType) {
		this.FlavorType = flavorType;
	}

	public int getFlavorVCPU() {
		return FlavorVCPU;
	}

	public void setFlavorVCPU(int flavorVCPU) {
		this.FlavorVCPU = flavorVCPU;
	}

	public int getFlavorRAM() {
		return FlavorRAM;
	}

	public void setFlavorRAM(int flavorRAM) {
		FlavorRAM = flavorRAM;
	}

	public int getFlavorHDD() {
		return FlavorHDD;
	}

	public void setFlavorHDD(int flavorHDD) {
		FlavorHDD = flavorHDD;
	}

	public String toString() {
		return "Flavor >> id: " + this.FlavorID + ", Flavor Type: "
				+ this.FlavorType + ", CPU: " + this.FlavorVCPU + ", RAM: "
				+ this.FlavorRAM + ", HDD: " + this.FlavorHDD;
	}

	public String toJSON() {
		return null;
	}
}
