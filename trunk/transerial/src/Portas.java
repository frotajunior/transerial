import java.util.Enumeration;

import javax.comm.CommPortIdentifier;


public class Portas {
	Enumeration listaDePortas;
	int  i = 0; 
	String[] portas = new String[10];
	
	public Portas()
	{
		listaDePortas  = CommPortIdentifier.getPortIdentifiers();
	}
	
	public String[] getPortas()
	{
		while (listaDePortas.hasMoreElements()) { 
			CommPortIdentifier ips = (CommPortIdentifier)listaDePortas.nextElement(); 
			portas[i] = ips.getName(); 
			i++;
		}
		
		
		return portas;
	}
}
