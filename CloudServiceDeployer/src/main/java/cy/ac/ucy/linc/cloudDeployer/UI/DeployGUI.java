package cy.ac.ucy.linc.cloudDeployer.UI;

import java.awt.Button;
import java.awt.Color;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import cy.ac.ucy.linc.cloudDeployer.beans.FlavorObj;
import cy.ac.ucy.linc.cloudDeployer.beans.ImageObj;
import cy.ac.ucy.linc.cloudDeployer.beans.Instance;
import cy.ac.ucy.linc.cloudDeployer.beans.KeyPairsObj;
import cy.ac.ucy.linc.cloudDeployer.beans.NetworkObj;
import cy.ac.ucy.linc.cloudDeployer.beans.SecurityGroupsObj;
import cy.ac.ucy.linc.cloudDeployer.connectors.ICloudConnector;
import cy.ac.ucy.linc.cloudDeployer.connectors.openstack.OpenstackConnector;
import cy.ac.ucy.linc.cloudDeployer.deployment.Deployer;

public class DeployGUI extends JFrame {
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	public final Button button = new Button("Create VM");
	public final Button button_1 = new Button("Get Image List");
	public final Button button_2 = new Button("Get Flavor List");
	public final Button button_3 = new Button("Get Network List");
	public final Button button_4 = new Button("Get Keypairs");
	public final Button button_5 = new Button("Get SecurityGroups");
	public final Button button_6 = new Button("Get Instances");
	public final Button button_7 = new Button("Auto Create");
	public final Button button_8 = new Button("Remove VM");
	public final TextArea textArea = new TextArea();

	private Deployer deploy;

	private ICloudConnector conn;
	private Properties config;
	private static final String CONFIG_PATH = "Resources" + File.separator
			+ "config.properties";

	public DeployGUI(ICloudConnector conn) {
		super("Deploy GUI");
		try {
			// parse config file
			this.parseConfig();
			// initialize connector and establish connection with cloud platform
			this.conn = this.cloudConnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// this.conn = conn;
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

	public void init() {

		setBackground(new Color(153, 204, 255));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 915, 618);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(153, 204, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		textArea.setBounds(146, 247, 600, 400);
		contentPane.add(textArea);

		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// image list listener
				textArea.setText(" ");

				List<ImageObj> imagelist = conn.getImageList(null, null);
				for (final ImageObj l : imagelist) {
					// System.out.println(l.toString());
					textArea.append(l.toString() + "\n");
				}
			}
		});

		button_1.setActionCommand("Print");
		button_1.setBounds(20, 32, 97, 22);
		contentPane.add(button_1);

		Button button_2 = new Button("Get Flavor List");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// flavor listener
				textArea.setText(" ");

				List<FlavorObj> flavorlist = conn.getFlavorList(null);
				for (FlavorObj fl : flavorlist) {
					// System.out.println(fl.toString());
					textArea.append(fl.toString() + "\n");

				}
			}
		});

		button_2.setActionCommand("Print");
		button_2.setBounds(123, 32, 97, 22);
		contentPane.add(button_2);

		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// network listener
				textArea.setText(" ");

				List<NetworkObj> networklist = conn.getNetworks();
				for (NetworkObj nw : networklist) {
					// System.out.println(nw.toString());
					textArea.append(nw.toString() + "\n");
				}
			}
		});
		button_3.setActionCommand("Print");
		button_3.setBounds(236, 32, 97, 22);
		contentPane.add(button_3);

		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// keypairs
				textArea.setText(" ");

				List<KeyPairsObj> keypairs = conn.getKeyPairs();
				for (KeyPairsObj keys : keypairs) {
					// System.out.println(keys.toString());
					textArea.append(keys.toString() + "\n");
				}
			}
		});
		button_4.setActionCommand("Print");
		button_4.setBounds(339, 32, 97, 22);
		contentPane.add(button_4);

		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// security
				textArea.setText(" ");

				List<SecurityGroupsObj> securitygroups = conn
						.getSecurityGroups();
				for (SecurityGroupsObj secgroups : securitygroups) {
					// System.out.println(secgroups.toString());
					textArea.append(secgroups.toString() + "\n");
				}

			}
		});
		button_5.setActionCommand("Print");
		button_5.setBounds(20, 73, 97, 22);
		contentPane.add(button_5);

		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// get Instances
				textArea.setText(" ");

				List<Instance> instances = conn.getInstances();
				for (Instance insta : instances) {
					// System.out.println(insta.toString());
					textArea.append(insta.toString() + "\n");
				}
			}
		});
		button_6.setActionCommand("Print");
		button_6.setBounds(123, 73, 97, 22);
		contentPane.add(button_6);

		button_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// Auto create
					textArea.setText(" ");
					textArea.append("Waiting for tosca file...\n");
					File filename = new File(JOptionPane.showInputDialog(contentPane,
							"Enter File Name?", "myApp.tosca").toString());

					// System.out.println(f1.getAbsolutePath());
					textArea.append("Starting now...\n");
					deploy = new Deployer(filename);
					textArea.append("Finished...\n");
					/*
					 * JFileChooser fileChooser = new JFileChooser();
					 * 
					 * FileNameExtensionFilter filter = new
					 * FileNameExtensionFilter( "TOSCA Files", "tosca");
					 * fileChooser.setFileFilter(filter);
					 * fileChooser.setCurrentDirectory(new File("Resources" +
					 * File.separator)); int returnValue =
					 * fileChooser.showOpenDialog(null); if (returnValue ==
					 * JFileChooser.APPROVE_OPTION) { File selectedFile =
					 * fileChooser.getSelectedFile(); if (selectedFile != null)
					 * { textArea.append("Read file on: " +
					 * selectedFile.getName().toString() + "\n"); //
					 * System.out.println(selectedFile.getName()); } } else
					 * textArea.append("File not found...\n");
					 */

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_7.setActionCommand("Print");
		button_7.setBounds(442, 73, 97, 22);
		contentPane.add(button_7);

		button.setActionCommand("Print");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Create VM
				textArea.setText(" ");
			}
		});
		button.setBounds(236, 73, 97, 22);
		contentPane.add(button);

		button_8.setActionCommand("Print");
		button_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// remove VM
				textArea.setText(" ");
			}
		});
		button_8.setBounds(339, 73, 97, 22);
		contentPane.add(button_8);

		JButton button_10 = new JButton("Exit");
		button_10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				System.exit(0);
			}
		});
		button_10.setBounds(20, 374, 98, 23);
		contentPane.add(button_10);

		this.setVisible(true);
		this.setSize(800, 800);
	}

	public static void main(String[] args) throws Exception {
		new DeployGUI(null).init();
	}
}