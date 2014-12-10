package cy.ac.ucy.linc.cloudDeployer.deployment;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import cy.ac.ucy.linc.cloudDeployer.beans.FlavorObj;
import cy.ac.ucy.linc.cloudDeployer.beans.ImageObj;
import cy.ac.ucy.linc.cloudDeployer.beans.Instance;
import cy.ac.ucy.linc.cloudDeployer.beans.KeyPairsObj;
import cy.ac.ucy.linc.cloudDeployer.beans.NetworkObj;
import cy.ac.ucy.linc.cloudDeployer.beans.SecurityGroupsObj;
import cy.ac.ucy.linc.cloudDeployer.connectors.ICloudConnector;
import cy.ac.ucy.linc.cloudDeployer.connectors.openstack.OpenstackConnector;

public class Deployer {

	private static final String CONFIG_PATH = "Resources" + File.separator + "config.properties";
	private Properties config;
	ICloudConnector conn;
	private HashMap<String,Deployment> deployments;

	public Deployer() throws Exception {
		//parse config file
		this.parseConfig();
		
		//initialize connector and establish connection with cloud platform
		this.conn = this.cloudConnect();

		//initialize deployment list to hold active deployments
		this.deployments = new HashMap<String,Deployment>();
		
		//create a new deployment
		//appName and other deployment params should be gathered by c-Eclipse description
		HashMap<String,String> params = new HashMap<String,String>();
		params.put("appName", "myApplication");
		String depID = this.createDeployment(params);
		System.out.println("Created new Deployment with depID: " + depID);
				
		//create modules
		//module name and other params should be gathered by c-Eclipse description
		params = new HashMap<String,String>();
		params.put("name", "AppServerTier");
		String m1ID = this.createModule(depID, params);
		System.out.println("Created new module with modID: " + m1ID + ", name: " + params.get("name"));
		
		params = new HashMap<String,String>();
		params.put("name", "DatabaseTier");
		String m2ID = this.createModule(depID, params);
		System.out.println("Created new module with modID: " + m2ID + ", name: " + params.get("name"));
		
		//add instances to modules by first creating them
		params = new HashMap<String,String>();
		params.put("name", "testserver");
		params.put("imageID","regionOne/83287548-1666-49de-bf35-c915be44e1cd");
		params.put("flavor", "2");
		params.put("network","8ebb464c-ad94-464b-ab87-28cfc46d9ecb");
		params.put("keypair", "akasta");
		params.put("securityGroup", "default-camf");

//		String vID = this.addInstanceToModule(depID, m1ID, params);
//		System.out.println("Added new Instance to module with id: " + vID);

		if (this.terminateModule(depID, m1ID))
			System.out.println("Successfully terminate module with modID: " + m1ID + ", for deployment with depID: " + depID);
		else
			System.out.println("Failed to terminate module with modID: " + m1ID + ", for deployment with depID: " + depID);
		
		if (this.terminateDeployment(depID))
			System.out.println("Successfully terminated deployment with depID: " + depID);
		else
			System.out.println("Failed to terminated deployment with depID: " + depID);
		
		this.conn.getNetworks();
	}
	
	public ICloudConnector cloudConnect(){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("username", this.config.getProperty("openstack.username"));
		params.put("password", this.config.getProperty("openstack.password"));
		params.put("tenant", this.config.getProperty("openstack.tenant"));
		params.put("apiEndpointURL",
				this.config.getProperty("openstack.apiEndpointURL"));
		params.put("apiEndpointPort",
				this.config.getProperty("openstack.apiEndpointPort"));
		
		//TODO make this generic by loading dynamically the right class
		return new OpenstackConnector(params);
	}
	
	public String createDeployment(HashMap<String,String> params) {
		Deployment d =  new Deployment(params);
		this.deployments.put(d.getDepID(),d);
		return d.getDepID();
	}

	public boolean terminateDeployment(String depID) {
		boolean response = (this.deployments.remove(depID) != null) ? true : false; 
		
		return response;
	}

	public String createModule(String depID, HashMap<String, String> params) {
		Deployment d = this.deployments.get(depID);
		Module module = null;
		String modID = null;
		
		if (d != null){
			module = new Module(params);
			modID = module.getModID();
			d.addModule(module);
		}

		return modID;
	}
	
	public boolean terminateModule(String depID, String modID) {
		Deployment d = this.deployments.get(depID);
		if (d != null){
			Module module = null;
			for (Module m : d.getModulelist()){
				if (m.getModID().equals(modID)){
					module = m;
					ArrayList<Instance> ilist = module.getInstancelist();
					
					for(int i = 0; i < ilist.size(); i++)
						this.removeInstanceFromModule(depID, modID, ilist.get(i).getInstanceID());
					
					d.removeModule(modID);
					
					return true;
				}
			}			
		}
		return false;
	}
	
	public String addInstanceToModule(String depID, String modID, Map<String, String> params) {
		String instID = null;
		
		Deployment d = this.deployments.get(depID);
		if (d != null){
			Module module = null;
			for (Module m : d.getModulelist()){
				if (m.getModID().equals(modID))
					module = m;
			}
			
			if (module != null){
				instID = this.conn.createInstance(params);
				module.addInstance(instID);
			}
			else
				System.out.println("module ID does not exist, please create a module in a valid deployment first");
		}
		else
			System.out.println("deployment ID does not exist, please create a deployment first");
		
		return instID;
	}
	
	
	public boolean removeInstanceFromModule(String depID, String modID, String vID) {
		Deployment d = this.deployments.get(depID);
		if (d != null){
			Module module = null;
			for (Module m : d.getModulelist()){
				if (m.getModID().equals(modID))
					module = m;
			}
			
			if (module != null){
				if (this.conn.terminateInstance(vID)) {
					module.removeInstance(vID);
					System.out.println("Successfully removed instance: "+vID);
					return true;
				}
				else
					System.out.println("Instance does not exist: "+vID);
			}
			else
				System.out.println("module ID does not exist, please create a module in a valid deployment first");
		}
		System.out.println("deployment ID does not exist, please create a deployment first");
		
		return false;
	}

	public String removeInstaceFromModule(String modID) {
		// TODO Auto-generated method stub
		return null;
	}
	
	// parse the configuration file
	private void parseConfig() throws Exception {
		this.config = new Properties();
		// load config properties file
		FileInputStream fis = new FileInputStream(CONFIG_PATH);
		config.load(fis);
		if (fis != null)
			fis.close();
	}

	public static void main(String[] args) throws Exception {
		new Deployer();
	}

}
