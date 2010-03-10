package classes;

public class PacoteResposta {
	
	private Byte SYN;
	private Byte ACK;
	private Byte PAD;
	
	public PacoteResposta() {
		SYN = new Byte((byte) 241);
		ACK = new Byte((byte) 247);
		PAD = new Byte((byte) 246);
	}

	public Byte getSYN() {
		return SYN;
	}

	public void setSYN(Byte sYN) {
		SYN = sYN;
	}

	public Byte getACK() {
		return ACK;
	}

	public void setACK(Byte aCK) {
		ACK = aCK;
	}

	public Byte getPAD() {
		return PAD;
	}

	public void setPAD(Byte pAD) {
		PAD = pAD;
	}
	
}
