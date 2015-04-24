package cy.ac.ucy.linc.cloudDeployer.json;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSON {

	private static final String FILE_PATH = "Resources" + File.separator;
	private String file_name;
	FileWriter file;
	JSONObject obj = new JSONObject();

	public JSON() {
		file_name = new SimpleDateFormat("ddMMyyyy_HHmmss").format(Calendar
				.getInstance().getTime());
		try {
			file = new FileWriter(FILE_PATH + this.file_name + ".json");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void write_data(HashMap[] xmlinfo, String appName, String depID,
			List<String> modules, List<String> instance, String start_time,
			String finish_time) {

		JSONObject json = new JSONObject();
		int instance_count = 0;

		System.out.println("Starting of JSON creation...");

		JSONArray deplist = new JSONArray();
		JSONObject deployment = new JSONObject();
		JSONArray mod = new JSONArray();

		deployment.put("appName", appName);
		deployment.put("deploymentID", depID);
		deployment.put("startDeployTime", start_time);
		deployment.put("finishDeployTime", finish_time);
		deployment.put("status", "deployed");
		deployment.put("provider", "OpenStack");
		deplist.add(deployment);

		for (int i = 1; i <= modules.size(); i++) {
			try {
				JSONObject module = new JSONObject();
				JSONObject instances = null;
				JSONArray inst = new JSONArray();
				module.put("ModuleID", modules.get(i - 1));
				module.put("ModuleName", xmlinfo[i].get("name"));
				module.put("initInstances", xmlinfo[i].get("initInstances"));

				for (int j = 0; j < Integer.parseInt((String) xmlinfo[i].get(
						"initInstances"))
						&& instance.get(instance_count) != null; j++) {
					try {
						instances = new JSONObject();
						instances.put("instanceID",
								instance.get(instance_count));
						instances.put("ImageID", xmlinfo[i].get("VMI"));
						instances.put("FlavorID", xmlinfo[i].get("flavor"));
						instances.put("KeyPair", xmlinfo[i].get("KeyPair"));
						instance_count++;
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					inst.add(instances);
					module.put("Instances", inst);
				}
				mod.add(module);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		deployment.put("Modules", mod);
		json.put("Deployments", deplist);
		try {
			file.write(json.toJSONString());
			file.flush();
			file.close();
			System.out.println("Finished JSON creation...");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}