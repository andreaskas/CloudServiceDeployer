package cy.ac.ucy.linc.cloudDeployer.beans;

public class SecurityGroupsObj {

	private String GroupName;
	private String GroupDescription;

	public SecurityGroupsObj(String name, String description) {
		this.GroupName = name;
		this.GroupDescription = description;
	}

	public String getName() {
		return GroupName;
	}

	public void setName(String name) {
		this.GroupName = name;
	}

	public String getDescription() {
		return GroupDescription;
	}

	public void setDescription(String description) {
		this.GroupDescription = description;
	}

	public String toString() {
		return "SecurityGroups >> Name: " + this.GroupName + ", Description: "
				+ this.GroupDescription;
	}

	public String toJSON() {
		return null;
	}

}
