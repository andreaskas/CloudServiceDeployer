package cy.ac.ucy.linc.cloudDeployer.beans;

public class KeyPairsObj {
	private String KeyPairName;
	private String PublicKey;
	private String PrivateKey;
	private String Fingerprint;

	public KeyPairsObj(String keyPairName, String publicKey, String privateKey,
			String fingerprint) {
		this.KeyPairName = keyPairName;
		this.PublicKey = publicKey;
		this.PrivateKey = privateKey;
		this.Fingerprint = fingerprint;
	}

	public String getKeyPairName() {
		return KeyPairName;
	}

	public void setKeyPairName(String keyPairName) {
		this.KeyPairName = keyPairName;
	}

	public String getPublicKey() {
		return PublicKey;
	}

	public void setPublicKey(String publicKey) {
		this.PublicKey = publicKey;
	}

	public String getPrivateKey() {
		return PrivateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.PrivateKey = privateKey;
	}

	public String getFingerprint() {
		return Fingerprint;
	}

	public void setFingerprint(String fingerprint) {
		Fingerprint = fingerprint;
	}

	public String toString() {
		return "KeyPairs >> Name: " + this.KeyPairName + ", PublicKey: "
				+ this.PublicKey + ", PrivateKey: " + this.PrivateKey
				+ ", Fingerpring: " + this.Fingerprint;
	}

	public String toJSON() {
		return null;
	}
}
