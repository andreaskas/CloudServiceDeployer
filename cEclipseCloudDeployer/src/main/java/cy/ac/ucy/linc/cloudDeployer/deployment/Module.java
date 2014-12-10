package cy.ac.ucy.linc.cloudDeployer.deployment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import cy.ac.ucy.linc.cloudDeployer.beans.Instance;


public class Module {

	private String modID;
	private String modName;
	private ArrayList<Instance> instancelist;

	public Module(HashMap<String,String> params) {
		this.modID = UUID.randomUUID().toString();
		this.modName = params.get("name");
		this.instancelist = new ArrayList<Instance> ();
	}
	
	public String getModName() {
		return modName;
	}

	public void setModName(String modName) {
		this.modName = modName;
	}

	public void addInstance(String instID) {
		instancelist.add(new Instance(instID,instID)); //pass the instance ID as the name for now
	}

	public void removeInstance(String instID) {
		for (int i=0;i<this.instancelist.size();i++){
			Instance obj = this.instancelist.get(i);
			if (obj.getInstanceID().equals(instID))
				this.instancelist.remove(i);
		}
	}

	public String getModID() {
		return this.modID;
	}

	public void setModID(String modID) {
		this.modID = modID;
	}

	public ArrayList<Instance> getInstancelist() {
		return this.instancelist;
	}

	public void setInstancelist(ArrayList<Instance> instancelist) {
		this.instancelist = instancelist;
	}
}
