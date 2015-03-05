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

		obj.put("appName ", appName);
		obj.put("deploymentID ", depID);
		obj.put("startDeployTime ", start_time);
		obj.put("finishDeployTime ", finish_time);
		obj.put("status ", "running");

		JSONArray list3 = new JSONArray();
		for (int i = 1; i < xmlinfo.length; i++) {
			JSONArray list = new JSONArray();
			list.add("moduleID : " + modules.get(i-1));
			list.add("moduleName : " + xmlinfo[i].get("name"));
			JSONArray list1 = new JSONArray();
			list.add("instances");
			list1.add("instanceID : " + instance.get(i-1));
			list1.add("image : " + xmlinfo[i].get("VMI"));
			list1.add("flavor : " + xmlinfo[i].get("flavor"));
			list1.add("key : " + xmlinfo[i].get("KeyPair"));
			list.add(list1);
			list3.add(list);
		}
		obj.put("modules", list3);
		try {

			file.write(obj.toJSONString());
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// System.out.print(obj);

	}
}