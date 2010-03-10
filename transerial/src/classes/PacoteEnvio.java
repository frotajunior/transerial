package classes;

import java.util.Vector;

public class PacoteEnvio {
	
	private Byte SYN;
	private Byte IDPacote;
	private Vector<Byte> remetente;
	private Vector<Byte> destinatario;
	private Vector<Byte> DLESTX;
	private Vector<Byte> dados;
	private Vector<Byte> DLEETB;
	private Vector<Byte> DLEETX;
	private Vector<Byte> CRC16;
	private Byte PAD;
	private static int ID = 1;
	private boolean possuiETB;
	private boolean possuiETX;
	
	public PacoteEnvio(int tamanhoDados) {
		SYN = new Byte((byte) 241);
		IDPacote = new Byte((byte) ID++);
		remetente = new Vector<Byte>();
		destinatario = new Vector<Byte>();
		DLESTX = new Vector<Byte>(2);
		DLESTX.add(new Byte((byte) 250));
		DLESTX.add(new Byte((byte) 243));
		dados = new Vector<Byte>(tamanhoDados);
		CRC16 = new Vector<Byte>(2);
		PAD = new Byte((byte) 246);
		possuiETB = false;
		possuiETX = false;
	}

	public Byte getSYN() {
		return SYN;
	}

	public void setSYN(Byte sYN) {
		SYN = sYN;
	}

	public Byte getIDPacote() {
		return IDPacote;
	}

	public void setIDPacote(Byte iDPacote) {
		IDPacote = iDPacote;
	}

	public Vector<Byte> getRemetente() {
		return remetente;
	}

	public void setRemetente(Vector<Byte> remetente) {
		this.remetente = remetente;
	}

	public Vector<Byte> getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(Vector<Byte> destinatario) {
		this.destinatario = destinatario;
	}

	public Vector<Byte> getDLESTX() {
		return DLESTX;
	}

	public void setDLESTX(Vector<Byte> dLESTX) {
		DLESTX = dLESTX;
	}

	public Vector<Byte> getDados() {
		return dados;
	}

	public void setDados(Vector<Byte> dados) {
		this.dados = dados;
	}

	public Vector<Byte> getDLEETB() {
		return DLEETB;
	}

	public void setDLEETB(Vector<Byte> dLEETB) {
		DLEETB = dLEETB;
	}

	public Vector<Byte> getDLEETX() {
		return DLEETX;
	}

	public void setDLEETX(Vector<Byte> dLEETX) {
		DLEETX = dLEETX;
	}

	public Vector<Byte> getCRC16() {
		return CRC16;
	}

	public void setCRC16(Vector<Byte> cRC16) {
		CRC16 = cRC16;
	}

	public Byte getPAD() {
		return PAD;
	}

	public void setPAD(Byte pAD) {
		PAD = pAD;
	}

	public static int getID() {
		return ID;
	}

	public static void setID(int iD) {
		ID = iD;
	}

	public boolean getPossuiETB() {
		return possuiETB;
	}

	public void setPossuiETB(boolean possuiETB) {
		this.possuiETB = possuiETB;
		if (possuiETB == true) {
			DLEETB = new Vector<Byte>(2);
			DLEETB.add(new Byte((byte) 250));
			DLEETB.add(new Byte((byte) 244));
		}
	}

	public boolean getPossuiETX() {
		return possuiETX;
	}

	public void setPossuiETX(boolean possuiETX) {
		this.possuiETX = possuiETX;
		if (possuiETX == true) {
			DLEETX = new Vector<Byte>(2);
			DLEETX.add(new Byte((byte) 250));
			DLEETX.add(new Byte((byte) 245));
		}
	}
	
	public byte[] getBytes()
	{
		int size = 3; 
		size += remetente.size()+1;
		size += destinatario.size()+1;
		size += DLESTX.size()+1;
		size += dados.size()+1;
		size += DLEETB.size()+1;
		if (DLEETX != null)
		{
			size += DLEETX.size()+1;
		}
		size += CRC16.size()+1;
		
		byte [] pacote = new byte[size];
		pacote[0] = SYN;
		pacote[1] = IDPacote;
		int i = 2;
		for (int j=0;j<remetente.size();j++)
		{
			pacote[i] = remetente.get(j);
			i++;
		}
		
		int k = i+1;
		for (int l =0;l<destinatario.size();l++)
		{
			pacote[k] = destinatario.get(l);
			k++;
		}
		
		int a = k+1;
		for (int b=0;b<DLESTX.size();b++)
		{
			pacote[a] = DLESTX.get(b);
			a++;
		}
		
		int c = a+1;
		for (int d = 0;d<dados.size();d++)
		{
			pacote[c] = dados.get(d);
			c++;
		}
		
		if (possuiETB)
		{
			int e = c+1;
			for (int f = 0;f<DLEETB.size();f++)
			{
				pacote[e] = DLEETB.get(f);
				e++;
			}
			int x = e +1;
			for (int crc =0;crc<CRC16.size();crc++)
			{
				pacote[x] = CRC16.get(crc);
				x++;
			}
			pacote[++x] = PAD;
		}
		else
		{
			int g = c+1;
			for (int h=0;h<DLEETX.size();h++)
			{
				pacote[g] = DLEETX.get(h);
				g++;
			}
			int x = g +1;
			for (int crc =0;crc<CRC16.size();crc++)
			{
				pacote[x] = CRC16.get(crc);
				x++;
			}
			
			pacote[++x] = PAD;
		}
		
		return pacote;
	}
	
}
