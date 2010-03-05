package util;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.comm.CommPortIdentifier;


public class Portas {
	Enumeration listaDePortas;
	int  i = 0; 
	List<String> portas;
	
	public Portas()
	{
		listaDePortas  = CommPortIdentifier.getPortIdentifiers();
	}
	
	public String[] getPortas()
	{
		portas = new ArrayList<String>();
		while (listaDePortas.hasMoreElements()) { 
			CommPortIdentifier ips = (CommPortIdentifier)listaDePortas.nextElement(); 
			portas.add(ips.getName());
			i++;
		}

		return portas.toArray(new String[i]);
	}
}
