package cy.ac.ucy.linc.cloudDeployer.deployment;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.HashMap;
import java.util.Properties;

import cy.ac.ucy.linc.cloudDeployer.beans.FlavorObj;
import cy.ac.ucy.linc.cloudDeployer.beans.ImageObj;
import cy.ac.ucy.linc.cloudDeployer.beans.InstancesObj;
import cy.ac.ucy.linc.cloudDeployer.beans.KeyPairsObj;
import cy.ac.ucy.linc.cloudDeployer.beans.NetworkObj;
import cy.ac.ucy.linc.cloudDeployer.beans.SecurityGroupsObj;
import cy.ac.ucy.linc.cloudDeployer.connectors.openstack.OpenstackConnector;

public class Deployer {

	private Properties config;
	private static final String CONFIG_PATH = "Resources" + File.separator
			+ "config.properties";

	public Deployer() throws Exception {
		this.parseConfig();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("username", this.config.getProperty("openstack.username"));
		params.put("password", this.config.getProperty("openstack.password"));
		params.put("tenant", this.config.getProperty("openstack.tenant"));
		params.put("apiEndpointURL",
				this.config.getProperty("openstack.apiEndpointURL"));
		params.put("apiEndpointPort",
				this.config.getProperty("openstack.apiEndpointPort"));

		OpenstackConnector conn = new OpenstackConnector(params);

		List<ImageObj> imagelist = conn.getImageList(null, null);
		for (ImageObj l : imagelist)
			System.out.println(l.toString());

		List<FlavorObj> flavorlist = conn.getFlavorList(null);
		for (FlavorObj fl : flavorlist)
			System.out.println(fl.toString());

		List<NetworkObj> networklist = conn.getNetworks();
		for (NetworkObj nw : networklist)
			System.out.println(nw.toString());

		List<KeyPairsObj> keypairs = conn.getKeyPairs();
		for (KeyPairsObj keys : keypairs)
			System.out.println(keys.toString());

		List<SecurityGroupsObj> securitygroups = conn.getSecurityGroups();
		for (SecurityGroupsObj secgroups : securitygroups)
			System.out.println(secgroups.toString());
		
		List<InstancesObj> instances = conn.getInstances();
		for (InstancesObj insta : instances)
			System.out.println(insta.toString());
		
		//conn.addInstanceToModule(null, null);
		
		HashMap<String,String> instanceProperties = new HashMap<String,String>();
		instanceProperties.put("name", "myfavoritevm");
		instanceProperties.put("imageID", "regionOne/8d2433da-8464-4abf-b149-2313c8639949");
		instanceProperties.put("flavor","2");
		instanceProperties.put("network","3ebaa012-be83-4708-afa0-24cf815be072");
		instanceProperties.put("keypair","mykey");
		instanceProperties.put("securityGroup","default");

		String msg =""; 
//		msg = conn.addInstanceToModule(null, instanceProperties);
		System.out.println(msg);
		
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
