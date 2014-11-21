package cy.ac.ucy.linc.cloudDeployer.connectors;
import java.util.List;
import java.util.Map;

import cy.ac.ucy.linc.cloudDeployer.beans.FlavorObj;
import cy.ac.ucy.linc.cloudDeployer.beans.ImageObj;
import cy.ac.ucy.linc.cloudDeployer.beans.InstancesObj;
import cy.ac.ucy.linc.cloudDeployer.beans.KeyPairsObj;
import cy.ac.ucy.linc.cloudDeployer.beans.NetworkObj;
import cy.ac.ucy.linc.cloudDeployer.beans.SecurityGroupsObj;


public interface ICloudConnector {

	/**
	 * create an initial deployment and parameterizes it (if needed).
	 * returns the deployment ID.
	 * @param params
	 * @return
	 */
	public String createDeployment(Map<String, String> params);
	
	/**
	 * terminate the deployment with given deployment ID.
	 * returns the status of this action (success, fail).
	 * for now lets keep the return value as a string.
	 * @param depID
	 * @return
	 */
	public String terminateDeployment(String depID);
	
	/**
	 * create a high level logical abstraction for a tier of
	 * a cloud application. Cloud providers usually dont categorize
	 * virtual running instances but orchestrators do. Returns module ID.
	 * @param depID
	 * @param params
	 * @return
	 */
	public String createModule(String depID, Map<String, String> params);
	
	/**
	 * terminate the module with the given module ID
	 * @param modID
	 * @return
	 */
	public String terminateModule(String modID);
	
	/**
	 * add an instance of a component described in c-Eclipse. To the module
	 * with the given modID. Return instance id.
	 * @param modID
	 * @param params
	 * @return
	 */
	public String addInstanceToModule(String modID, Map<String, String> params);
	
	/**
	 * remove/terminate instance with vID from the given module with modID.
	 * return a status message.
	 * @return
	 */
	public String removeInstanceFromModule(String vID, String modID);
	
	/**
	 * remove/terminate ANY instance from the given module with modID
	 * @param modID
	 * @return
	 */
	public String removeInstaceFromModule(String modID);
	
	//thanasis stuff
	/**
	 * Get the list of the available flavors (vm configurations),
	 * that the user has access to. Params input exposes any additional filtering or quering 
	 * parameters the IaaS has.
	 * Note that additional parameters such as user region is consider
	 * that are statically defined. If is not they should be given as parameters.
	 * @param params
	 * @return
	 */
	public List<FlavorObj> getFlavorList(Map<String, String> params);
	
	/**
	 * Get the list of the available images, that the user has access to.
	 * Params input exposes any additional filtering or quering parameters the IaaS has.
	 * Parameter scope defines the visibily of the return images (private, public, all)
	 * Note that additional parameters such as user region is consider
	 * that are statically defined. If is not they should be given as parameters.
	 * @param scope
	 * @param params
	 * @return
	 */
	public List<ImageObj> getImageList(String scope, Map<String, String> params);
	/**
	 * Get any additional service / resource that is offered and
	 * can be attached to the virtual machine during the start up or later
	 * such services are network interfaces, disk space, ssh key, groups, firewall templates
	 * serviceType parameters is used to further filter the return list of services
	 * @param serviceType
	 */
	public List<String> getAdditionalServices(String serviceType);
	
	/**
	 * Get the user quota for a specific resourse type
	 * (Memory, Storage, CPUs, Public IPs, VM number)
	 * @param resourceType
	 */
	public List<String> getQuotas(String resourceType);
	
	//nicholas stuff
	/* The list of virtual network interfaces */	
	public List<NetworkObj> getNetworks();
	
	/* The list of SSH keypairs available at the IaaS */
	public List<KeyPairsObj> getKeyPairs();
	
	/* The list of Security Groups */
	public List<SecurityGroupsObj> getSecurityGroups();
	
	/* The list of running instances - We might want to have this to clone a running instance */
	public List<InstancesObj> getInstances();
	
	//Note: We may need specific methods for authentication but will look into that. 
	//      The may be added in the implementation and not in the interface. Have to look into this. 	
	
	public String createImageFromInstance(String imageName, String instanceID);
}
