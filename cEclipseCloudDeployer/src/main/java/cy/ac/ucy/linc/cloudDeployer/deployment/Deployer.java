package cy.ac.ucy.linc.cloudDeployer.deployment;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.HashMap;
import java.util.Properties;

import cy.ac.ucy.linc.cloudDeployer.beans.ImageObj;
import cy.ac.ucy.linc.cloudDeployer.connectors.openstack.OpenstackConnector;

public class Deployer {
	
	private Properties config;
	private static final String CONFIG_PATH = "Resources" + File.separator + "config.properties";

	public Deployer() throws Exception{
		this.parseConfig();
		HashMap<String,String> params = new HashMap<String, String>();
		params.put("username", this.config.getProperty("openstack.username"));
		params.put("password", this.config.getProperty("openstack.password"));
		params.put("tenant", this.config.getProperty("openstack.tenant"));
		params.put("apiEndpointURL", this.config.getProperty("openstack.apiEndpointURL"));
		params.put("apiEndpointPort", this.config.getProperty("openstack.apiEndpointPort"));

		OpenstackConnector conn = new OpenstackConnector(params);
		
		List<ImageObj> imagelist = conn.getImageList(null, null);
		for(ImageObj l : imagelist)
			System.out.println(l.toString());
	}
	
	//parse the configuration file
	private void parseConfig() throws Exception{
		this.config = new Properties();
		//load config properties file			
		FileInputStream fis = new FileInputStream(CONFIG_PATH);
		config.load(fis);
		if (fis != null)
    		fis.close();
	}

	
	public static void main(String[] args) throws Exception {
		new Deployer();
	}

}
