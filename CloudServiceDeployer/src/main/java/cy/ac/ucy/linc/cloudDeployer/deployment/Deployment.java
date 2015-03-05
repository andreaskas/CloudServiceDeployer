package cy.ac.ucy.linc.cloudDeployer.deployment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Deployment {

	private String depID;
	private String appName;
	private ArrayList<Module> modulelist;

	public Deployment(HashMap<String, String> params) {
		this.appName = params.get("appName");
		this.depID = UUID.randomUUID().toString();
		this.modulelist = new ArrayList<Module>();
	}

	public void addModule(Module module) {
		modulelist.add(module);
	}

	public void removeModule(String modID) {
		for (int i = 0; i < this.modulelist.size(); i++) {
			Module m = this.modulelist.get(i);
			if (m.getModID().equals(modID))
				modulelist.remove(i);
		}
	}

	public String getDepID() {
		return this.depID;
	}

	public void setDepID(String deploymentID) {
		this.depID = deploymentID;
	}

	public String getAppName() {
		return this.appName;
	}

	public void setAppName(String name) {
		this.appName = name;
	}

	public ArrayList<Module> getModulelist() {
		return this.modulelist;
	}

	public void setModulelist(ArrayList<Module> modulelist) {
		this.modulelist = modulelist;
	}
}
