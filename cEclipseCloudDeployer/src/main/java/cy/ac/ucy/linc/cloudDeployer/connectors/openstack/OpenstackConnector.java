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
import org.jclouds.openstack.neutron.v2.NeutronApi;
import org.jclouds.openstack.neutron.v2.NeutronApiMetadata;
import org.jclouds.openstack.neutron.v2.domain.Network;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.NovaApiMetadata;
import org.jclouds.openstack.nova.v2_0.domain.Flavor;
import org.jclouds.openstack.nova.v2_0.domain.FloatingIP;
import org.jclouds.openstack.nova.v2_0.domain.KeyPair;
import org.jclouds.openstack.nova.v2_0.domain.SecurityGroup;
import org.jclouds.openstack.nova.v2_0.domain.Server;
import org.jclouds.openstack.nova.v2_0.domain.ServerCreated;
import org.jclouds.openstack.nova.v2_0.extensions.FloatingIPApi;
import org.jclouds.openstack.nova.v2_0.extensions.KeyPairApi;
import org.jclouds.openstack.nova.v2_0.extensions.SecurityGroupApi;
import org.jclouds.openstack.nova.v2_0.features.FlavorApi;
import org.jclouds.openstack.nova.v2_0.features.ImageApi;
import org.jclouds.openstack.nova.v2_0.features.ServerApi;
import org.jclouds.openstack.nova.v2_0.options.CreateServerOptions;
import org.jclouds.openstack.v2_0.domain.Resource;
import org.jclouds.openstack.keystone.v2_0.domain.Service;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;

import cy.ac.ucy.linc.cloudDeployer.beans.ImageObj;
import cy.ac.ucy.linc.cloudDeployer.beans.FlavorObj;
import cy.ac.ucy.linc.cloudDeployer.beans.KeyPairsObj;
import cy.ac.ucy.linc.cloudDeployer.beans.NetworkObj;
import cy.ac.ucy.linc.cloudDeployer.beans.SecurityGroupsObj;
import cy.ac.ucy.linc.cloudDeployer.connectors.ICloudConnector;
import cy.ac.ucy.linc.cloudDeployer.deployment.Instance;

public class OpenstackConnector implements ICloudConnector {
	private static final String PROVIDER = "openstack-nova";
	private static final String NEUTRON_PROVIDER = "openstack-neutron";
	private static final String NOVA_API_VERSION = "v2.0";
	private static final String DEFAULT_REGION = "regionOne";

	private NovaApi novaAPI;
	private ComputeService computeAPI;
	private NeutronApi networkAPI;
	private Set<String> zones;

	private HashMap<String, String> params;

	public OpenstackConnector(HashMap<String, String> params) {
		String tenant = params.get("tenant");
		String username = params.get("username");
		String password = params.get("password");
		String apiEndpointURL = params.get("apiEndpointURL");
		String apiEndpointPort = params.get("apiEndpointPort");
		String identity = tenant + ":" + username; 

		Iterable<Module> modules = ImmutableSet.<Module> of(new SLF4JLoggingModule());
		ContextBuilder builder = ContextBuilder.newBuilder(new NovaApiMetadata())
											   .endpoint(apiEndpointURL + ":" + apiEndpointPort + "/" 
		                                                 + OpenstackConnector.NOVA_API_VERSION + "/")
		                                       .credentials(identity, password).modules(modules);
		
		this.computeAPI = builder.buildView(ComputeServiceContext.class).getComputeService();
		this.novaAPI = builder.buildApi(NovaApi.class);
		this.zones = this.novaAPI.getConfiguredZones();
		
		this.networkAPI = ContextBuilder.newBuilder(new NeutronApiMetadata())
				   .endpoint(apiEndpointURL + ":" + apiEndpointPort + "/" 
                           + OpenstackConnector.NOVA_API_VERSION + "/")
                 .credentials(identity, password).modules(modules).buildApi(NeutronApi.class);		
	}
	
	public String createInstance(Map<String, String> params) {
		String instName = params.get("name");
		String imageID = params.get("imageID");
		String flavorID = params.get("flavor");
		String net1 = params.get("network");
		String[] networks = {net1};
		String keypair = params.get("keypair");
		String securityGroup = params.get("securityGroup");
		CreateServerOptions serverOpts = CreateServerOptions.Builder.adminPass("linc").networks(networks);
		serverOpts.keyPairName(keypair);
		serverOpts.securityGroupNames(securityGroup);
		
		System.out.println("OpenstackConnector>> received ADD instance request, with instance name: " + instName);
		
		ServerApi serverAPI = this.novaAPI.getServerApiForZone(OpenstackConnector.DEFAULT_REGION);
		ServerCreated s = serverAPI.create(instName, imageID, flavorID, serverOpts);
		
		return s.getId();
	}

	public boolean terminateInstance(String vID) {
		ServerApi serverAPI = this.novaAPI.getServerApiForZone(DEFAULT_REGION);
		if (serverAPI.delete(vID)) {
			System.out.println("Successfully removed instance: "+vID);
			return true;
		}
		else
			System.out.println("Instance does not exist: "+vID);
		return false;
	}

	public List<FlavorObj> getFlavorList(Map<String, String> params) {
		
		List<FlavorObj> flavorlist = new ArrayList<FlavorObj>();
		FlavorApi zonesapi = this.novaAPI.getFlavorApiForZone("regionOne");

		for (Flavor fl : zonesapi.listInDetail().concat()) {
			flavorlist.add(new FlavorObj(fl.getId(), fl.getName(), fl
					.getVcpus(), fl.getRam(), fl.getDisk()));
		}

		return flavorlist;
	}

	public List<ImageObj> getImageList(String scope, Map<String, String> params) {
		
		List<ImageObj> imagelist = new ArrayList<ImageObj>();
		for (Image i : this.computeAPI.listImages())
			imagelist.add(new ImageObj(i.getId(), i.getName(), i
					.getDescription()));
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

	public List<NetworkObj> getNetworks() {
		List<NetworkObj> networklist = new ArrayList<NetworkObj>();

		for (Network n: this.networkAPI.getNetworkApi(OpenstackConnector.DEFAULT_REGION).list().concat()){
			System.out.println(n.getName()+" "+n.getId());
		}

//
//		Optional<? extends FloatingIPApi> zonesapi = this.novaAPI
//				.getFloatingIPExtensionForZone("regionOne");
//		FloatingIPApi floatingapi = zonesapi.get();
//		Set<? extends FloatingIP> response = floatingapi.list().toSet();
//
//		for (FloatingIP ip : response) {
//			networklist.add(new NetworkObj(ip.getFixedIp(), ip.getIp()));
//		}

		return networklist;
	}

	public List<KeyPairsObj> getKeyPairs() {

		List<KeyPairsObj> keypairs = new ArrayList<KeyPairsObj>();
		Optional<? extends KeyPairApi> keyPairApi = this.novaAPI
				.getKeyPairExtensionForZone("regionOne");
		KeyPairApi keypairapi = keyPairApi.get();
		Set<KeyPair> response = keypairapi.list().toSet();

		for (KeyPair key : response) {
			keypairs.add(new KeyPairsObj(key.getName(), key.getPublicKey(), key
					.getPrivateKey(), key.getFingerprint()));
		}

		return keypairs;
	}

	public List<SecurityGroupsObj> getSecurityGroups() {

		List<SecurityGroupsObj> securitygroup = new ArrayList<SecurityGroupsObj>();
		Optional<? extends SecurityGroupApi> securityGroupApi = this.novaAPI
				.getSecurityGroupExtensionForZone("regionOne");
		SecurityGroupApi securitygroupapi = securityGroupApi.get();
		Set<SecurityGroup> response = securitygroupapi.list().toSet();
		
		for (SecurityGroup sg : response) {
			securitygroup.add(new SecurityGroupsObj(sg.getName(), sg
					.getDescription()));
		}
		
		return securitygroup;
	}

	public List<Instance> getInstances() {
		List<Instance> instances = new ArrayList<Instance>();

		for (ComputeMetadata i : this.computeAPI.listNodes())
			instances.add(new Instance(i.getId(), i.getName()));

		return instances;
	}
	
	public String createImageFromInstance(String imageName, String instanceID){
		ServerApi serverAPI = this.novaAPI.getServerApiForZone(OpenstackConnector.DEFAULT_REGION);
		String imageID = serverAPI.createImageFromServer(imageName, instanceID);
		return imageID;
	}
}
