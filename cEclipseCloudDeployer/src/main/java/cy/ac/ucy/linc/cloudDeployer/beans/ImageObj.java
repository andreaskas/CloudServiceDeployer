package cy.ac.ucy.linc.cloudDeployer.beans;

public class ImageObj {
	
	private String imageID;
	private String imageName;
	private String imageDescription;
	
	public ImageObj(String id, String name, String desc){
		this.imageID = id;
		this.imageName = name;
		this.imageDescription = desc;
	}
	
	public String getImageID() {
		return imageID;
	}
	
	public void setImageID(String imageID) {
		this.imageID = imageID;
	}
	
	public String getImageName() {
		return imageName;
	}
	
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	
	public String getImageDescription() {
		return imageDescription;
	}
	
	public void setImageDescription(String imageDescription) {
		this.imageDescription = imageDescription;
	}
	
	public String toString(){
		return "Image >> id: " + this.imageID + ", name: " + this.imageName + ", desc: " + this.imageDescription;
	}
	
	public String toJSON(){
		return null;
	}
}
