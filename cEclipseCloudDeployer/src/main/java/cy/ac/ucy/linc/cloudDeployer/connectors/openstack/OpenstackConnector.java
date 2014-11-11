package cy.ac.ucy.linc.cloudDeployer.connectors.openstack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.Image;
import org.jclouds.domain.Credentials;
import org.jclouds.ContextBuilder;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.openstack.keystone.v2_0.domain.Access;
import org.jclouds.openstack.keystone.v2_0.domain.Endpoint;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.NovaApiMetadata;
import org.jclouds.openstack.nova.v2_0.domain.Flavor;
import org.jclouds.openstack.nova.v2_0.domain.Server;
import org.jclouds.openstack.nova.v2_0.features.ImageApi;
import org.jclouds.openstack.v2_0.domain.Resource;
import org.jclouds.openstack.keystone.v2_0.domain.Service;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;

import cy.ac.ucy.linc.cloudDeployer.beans.ImageObj;
import cy.ac.ucy.linc.cloudDeployer.connectors.ICloudConnector;

public class OpenstackConnector implements ICloudConnector{
	private static final String PROVIDER = "openstack-nova";
	private static final String NOVA_API_VERSION = "v2.0";
	
	private NovaApi novaAPI;
	private ComputeService computeAPI;
	private Set<String> zones;

	private HashMap<String,String> params;
	
	public OpenstackConnector(HashMap<String,String> params){
//		for(Entry<String, String> e : params.entrySet())
//			System.out.println(e.getKey() + " " + e.getValue());
		String tenant =  params.get("tenant");
		String username = params.get("username");
		String password = params.get("password");
		String apiEndpointURL = params.get("apiEndpointURL");
		String apiEndpointPort = params.get("apiEndpointPort");
        String identity = tenant + ":" + username; // tenantName:userName
        
        Iterable<Module> modules = ImmutableSet.<Module>of(new SLF4JLoggingModule());
        ContextBuilder builder = ContextBuilder.newBuilder(new NovaApiMetadata())
                					           .endpoint(apiEndpointURL + ":" + apiEndpointPort + "/" + OpenstackConnector.NOVA_API_VERSION + "/")					   
                					           .credentials(identity, password)
                					           .modules(modules);
        computeAPI = builder.buildView(ComputeServiceContext.class).getComputeService();
        novaAPI = builder.buildApi(NovaApi.class);
        
        this.zones= this.novaAPI.getConfiguredZones();
	}
	
	public String createDeployment(Map<String, String> params) {
		// TODO Auto-generated method stub
		return null;
	}

	public String terminateDeployment(String depID) {
		// TODO Auto-generated method stub
		return null;
	}

	public String createModule(String depID, Map<String, String> params) {
		// TODO Auto-generated method stub
		return null;
	}

	public String terminateModule(String modID) {
		// TODO Auto-generated method stub
		return null;
	}

	public String addInstanceToModule(String modID, Map<String, String> params) {
		// TODO Auto-generated method stub
		return null;
	}

	public String removeInstanceFromModule(String vID, String modID) {
		// TODO Auto-generated method stub
		return null;
	}

	public String removeInstaceFromModule(String modID) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getFlavorList(Map<String, String> params) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ImageObj> getImageList(String scope, Map<String, String> params) {
		List<ImageObj> imagelist = new ArrayList<ImageObj>();
		for (Image i : this.computeAPI.listImages())
			imagelist.add(new ImageObj(i.getId(), i.getName(), i.getDescription()));
		return imagelist;
	}

	public List<String> getAdditionalServices(String serviceType) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getQuotas(String resourceType) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getNetworks() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getKeyPairs() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getSecurityGroups() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getInstances() {
		// TODO Auto-generated method stub
		return null;
	}

}
