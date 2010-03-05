import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;

public class Play implements Runnable,SerialPortEventListener{

	SerialPort porta = null;
	Thread threadLeitura = null;
	InputStream entrada;
	CommPortIdentifier cp = null;
	
	public Play() {
		 try {
			cp = CommPortIdentifier.getPortIdentifier("COM1");
			porta = (SerialPort)cp.open("COM1",100);
			porta.setSerialPortParams(9600, porta.DATABITS_8, porta.STOPBITS_2, porta.PARITY_NONE);
			porta.addEventListener(this);
		} catch (TooManyListenersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Play play = new Play();
		play.executa();
	}
	
	public void executa()
	{
		Enumeration listaDePortas; 
		listaDePortas  = CommPortIdentifier.getPortIdentifiers();
		
		//Identificando as postas e obtendo um array delas
		int  i = 0; 
		String[] portas = new String[10]; 
		while (listaDePortas.hasMoreElements()) { 
			CommPortIdentifier ips = (CommPortIdentifier)listaDePortas.nextElement(); 
			portas[i] = ips.getName(); 
			i++; 
		}
		System.out.println(new Portas().getPortas()[4]);
		
		//Abrindo uma porta específica
		
		try{
			OutputStream saida = porta.getOutputStream();
			
			String msg = "Olá Mundo!"; 
			saida.write(msg.getBytes()); 
			Thread.sleep(100); 
			saida.flush();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		lerDados();

	}
	
	public void lerDados()
	{
		try {
			entrada = porta.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		porta.notifyOnDataAvailable(true);
		threadLeitura = new Thread(this);
		threadLeitura.start();
	}
	
	@Override
	public void serialEvent(SerialPortEvent spe) {
		switch (spe.getEventType())
		{
			case SerialPortEvent.BI:
			case SerialPortEvent.OE:
			case SerialPortEvent.FE:
			case SerialPortEvent.PE:
			case SerialPortEvent.CD:
			case SerialPortEvent.CTS:
			case SerialPortEvent.DSR:
			case SerialPortEvent.RI:
			case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
				break;
			case SerialPortEvent.DATA_AVAILABLE:
				try {
					int b = 0;
					int i = 0;
					byte[] bytes = new byte[entrada.available()];
					while (entrada.available() > 0) {
						b = entrada.read(bytes);
						i++;
					}
					String leitura = new String(bytes);
					System.out.print(leitura);
				} catch (Exception e) {
					 e.printStackTrace();
				}
				break;
		}
	}

	@Override
	public void run() {
		
		try
		{
			Thread.sleep(5000);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
}
