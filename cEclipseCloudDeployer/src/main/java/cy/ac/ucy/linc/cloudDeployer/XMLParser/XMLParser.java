package cy.ac.ucy.linc.cloudDeployer.XMLParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLParser {

	/*
	 * private static final String FILE_PATH = "Resources" + File.separator +
	 * "myApplicationDescription.tosca";
	 */

	public HashMap[] parseXMLFile(String filename) {
		// HashMap<String, String> params = new HashMap<String, String>();

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		try {
			// parse the document
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File(filename));
			NodeList appName = doc
					.getElementsByTagName("tosca:ServiceTemplate");
			NodeList modNames = doc.getElementsByTagName("tosca:NodeTemplate");

			// get app name
			for (int i = 0; i < appName.getLength(); i++) {
				HashMap<String, String> params = new HashMap<String, String>();
				Element first = (Element) appName.item(i);
				if (first.hasAttributes()) {
					String app_name = first.getAttribute("name");
					// System.out.println(app_name);
					params.put("appName", app_name);
					list.add(params);
				}
			}

			// get module names
			for (int i = 0; i < modNames.getLength(); i++) {
				HashMap<String, String> params = new HashMap<String, String>();
				Element first = (Element) modNames.item(i);
				if (first.hasAttributes()) {
					String mod_name = first.getAttribute("name");
					String initInstances = first.getAttribute("initInstances");
					params.put("name", mod_name);
					params.put("initInstances", initInstances);
					// System.out.println(mod_name);
				}

				// get Flavor
				NodeList flavor = first
						.getElementsByTagName("elasticity:Flavor");
				for (int j = 0; j < flavor.getLength(); j++) {
					Element el = (Element) flavor.item(j);
					params.put("flavor", el.getTextContent());
					// System.out.println(el.getTextContent());
				}

				// get Artifacts
				NodeList artifacts = first
						.getElementsByTagName("tosca:DeploymentArtifact");
				for (int j = 0; j < artifacts.getLength(); j++) {
					Element el = (Element) artifacts.item(j);
					if (el.hasAttributes()) {
						// System.out.println(el.getAttribute("artifactType"));
						// System.out.println(el.getAttribute("name"));
						params.put(el.getAttribute("artifactType"),
								el.getAttribute("name"));
					}
				}
				list.add(params);
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException ed) {
			ed.printStackTrace();
		}

		return list.toArray(new HashMap[list.size()]);
		// return list;
	}

}