package cy.ac.ucy.linc.cloudDeployer.deployment;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import cy.ac.ucy.linc.cloudDeployer.XMLParser.XMLParser;
import cy.ac.ucy.linc.cloudDeployer.beans.Instance;
import cy.ac.ucy.linc.cloudDeployer.connectors.ICloudConnector;
import cy.ac.ucy.linc.cloudDeployer.connectors.openstack.OpenstackConnector;
import cy.ac.ucy.linc.cloudDeployer.json.JSON;

@SuppressWarnings("rawtypes")
public class Deployer {

	private static final String CONFIG_PATH = "Resources" + File.separator
			+ "config.properties";

	private static final String TOSCA_PATH = "Resources" + File.separator
			+ "myApp.tosca";

	private Properties config;
	private ICloudConnector conn;
	private HashMap<String, Deployment> deployments;
	public List<String> modules = new ArrayList<String>();
	public List<String> instances = new ArrayList<String>();
	public XMLParser xml;
	public HashMap[] xmlinfo;
	public JSON json;
	public String start_time;
	public String finish_time;
	public String appName;
	public String depID;
	private String initInstances;

	public Deployer(File filename) throws Exception {

		// parse XML File
		this.xml = new XMLParser();
		if (filename.getName().equals(""))
			this.xmlinfo = xml.parseXMLFile(TOSCA_PATH);
		else
			this.xmlinfo = xml.parseXMLFile("Resources" + File.separator
					+ filename.getName());

		// create JSON object
		this.json = new JSON();

		// parse config file
		this.parseConfig();

		// initialize connector and establish connection with cloud platform
		this.conn = this.cloudConnect();

		// initialize deployment list to hold active deployments
		this.deployments = new HashMap<String, Deployment>();

		// set the time the deployment started
		this.start_time = new SimpleDateFormat("dd/M/yyyy hh:mm:ss")
				.format(Calendar.getInstance().getTime());
		// create a new deployment id. appName and other
		// deployment params should be gathered by c-Eclipse description

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("appName", xmlinfo[0].get("appName").toString());
		this.appName = params.get("appName");
		this.depID = this.createDeployment(params);

		System.out.println("Created new Deployment with depID: " + this.depID);

		for (int i = 1; i < xmlinfo.length; i++) {
			// create module modName and other params
			// should be gathered by c-Eclipse description

			// get Initital Instances
			this.initInstances = xmlinfo[i].get("initInstances").toString();

			params = new HashMap<String, String>();
			// get module name
			params.put("name", xmlinfo[i].get("name").toString());
			String m1ID = this.createModule(depID, params);
			this.modules.add(m1ID);
			System.out.println("Created new module with modID: " + m1ID
					+ ", name: " + params.get("name"));

			// add instances to modules by gathering the params
			// through c-Eclipse description
			params = new HashMap<String, String>();
			params.put("name", xmlinfo[i].get("name").toString());
			params.put("imageID", xmlinfo[i].get("VMI").toString());
			params.put("flavor", "2");
			params.put("network", xmlinfo[i].get("Network").toString());
			params.put("keypair", xmlinfo[i].get("KeyPair").toString());
			params.put("securityGroup", "default-camf");

			// built instances according to initialInstances parameter
			for (int j = 0; j < Integer.parseInt(this.initInstances); j++) {
				String vID = this.addInstanceToModule(this.depID, m1ID, params);
				this.instances.add(vID);
				System.out.println("Added new Instance to module "
						+ xmlinfo[i].get("name").toString() + " with id: "
						+ vID);
			}
		}

		// set the time the deployment finished
		this.finish_time = new SimpleDateFormat("dd/M/yyyy hh:mm:ss")
				.format(Calendar.getInstance().getTime());

		// create json information
		json.write_data(this.xmlinfo, this.appName, this.depID, this.modules,
				this.instances, this.start_time, this.finish_time);

		@SuppressWarnings("resource")
		Scanner reader = new Scanner(System.in);
		System.out
				.println("Would you like to terminate all the deployments? 1/0");
		int a = reader.nextInt();

		if (a == 1) {
			for (int i = 0; i < modules.size(); i++) {
				if (this.terminateModule(depID, modules.get(i)))
					System.out
							.println("Successfully terminate module with modID: "
									+ modules.get(i)
									+ ", for deployment with depID: " + depID);
				else
					System.out
							.println("Failed to terminate module with modID: "
									+ modules.get(i)
									+ ", for deployment with depID: " + depID);
			}

			if (this.terminateDeployment(depID))
				System.out
						.println("Successfully terminated deployment with depID: "
								+ depID);
			else
				System.out
						.println("Failed to terminated deployment with depID: "
								+ depID);
		}
	}

	public ICloudConnector cloudConnect() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("username", this.config.getProperty("openstack.username"));
		params.put("password", this.config.getProperty("openstack.password"));
		params.put("tenant", this.config.getProperty("openstack.tenant"));
		params.put("apiEndpointURL",
				this.config.getProperty("openstack.apiEndpointURL"));
		params.put("apiEndpointPort",
				this.config.getProperty("openstack.apiEndpointPort"));

		// TODO make this generic by loading dynamically the right class
		return new OpenstackConnector(params);
	}

	public String createDeployment(HashMap<String, String> params) {
		Deployment d = new Deployment(params);
		this.deployments.put(d.getDepID(), d);
		return d.getDepID();
	}

	public boolean terminateDeployment(String depID) {
		boolean response = (this.deployments.remove(depID) != null) ? true
				: false;

		return response;
	}

	public String createModule(String depID, HashMap<String, String> params) {
		Deployment d = this.deployments.get(depID);

		Module module = null;
		String modID = null;

		if (d != null) {
			module = new Module(params);
			modID = module.getModID();
			d.addModule(module);
		}

		return modID;
	}

	public boolean terminateModule(String depID, String modID) {
		Deployment d = this.deployments.get(depID);
		if (d != null) {
			Module module = null;
			for (Module m : d.getModulelist()) {
				if (m.getModID().equals(modID)) {
					module = m;
					ArrayList<Instance> ilist = module.getInstancelist();
					System.out.println(module.getInstancelist());
					for (int i = ilist.size(); i > 0; i--)
						this.removeInstanceFromModule(depID, modID, ilist
								.get(i-1).getInstanceID());

					d.removeModule(modID);

					return true;
				}
			}
		}
		return false;
	}

	public String addInstanceToModule(String depID, String modID,
			Map<String, String> params) {
		String instID = null;

		Deployment d = this.deployments.get(depID);
		if (d != null) {
			Module module = null;
			for (Module m : d.getModulelist()) {
				if (m.getModID().equals(modID))
					module = m;
			}

			if (module != null) {
				instID = this.conn.createInstance(params);
				module.addInstance(instID);
			} else
				System.out
						.println("module ID does not exist, please create a module in a valid deployment first");
		} else
			System.out
					.println("deployment ID does not exist, please create a deployment first");

		return instID;
	}

	public boolean removeInstanceFromModule(String depID, String modID,
			String vID) {
		Deployment d = this.deployments.get(depID);
		if (d != null) {
			Module module = null;
			for (Module m : d.getModulelist()) {
				if (m.getModID().equals(modID))
					module = m;
			}

			if (module != null) {
				if (this.conn.terminateInstance(vID)) {
					module.removeInstance(vID);
					System.out.println("Successfully removed instance: " + vID);
					return true;
				} else
					System.out.println("Instance does not exist: " + vID);
			} else
				System.out
						.println("module ID does not exist, please create a module in a valid deployment first");
		}
		System.out
				.println("deployment ID does not exist, please create a deployment first");

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
		if (args.length != 1) {
			System.out.println("Usage: deployer file.tosca");
			System.exit(2);
		}
		File filename = new File(args[0]);
		new Deployer(filename);
	}

}
