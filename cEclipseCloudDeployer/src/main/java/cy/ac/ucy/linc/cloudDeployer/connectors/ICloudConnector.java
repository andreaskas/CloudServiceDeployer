package cy.ac.ucy.linc.cloudDeployer.connectors;
import java.util.List;
import java.util.Map;

import cy.ac.ucy.linc.cloudDeployer.beans.FlavorObj;
import cy.ac.ucy.linc.cloudDeployer.beans.ImageObj;
import cy.ac.ucy.linc.cloudDeployer.beans.Instance;
import cy.ac.ucy.linc.cloudDeployer.beans.KeyPairsObj;
import cy.ac.ucy.linc.cloudDeployer.beans.NetworkObj;
import cy.ac.ucy.linc.cloudDeployer.beans.SecurityGroupsObj;


public interface ICloudConnector {
		
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
	public List<Instance> getInstances();
	
	//Note: We may need specific methods for authentication but will look into that. 
	//      The may be added in the implementation and not in the interface. Have to look into this. 	
	
	public String createImageFromInstance(String imageName, String instanceID);
	
	public String createInstance(Map<String, String> params);
	
	public boolean terminateInstance(String vID);
}
