package acao;
import gui.Gui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;

import com.sun.org.apache.xerces.internal.impl.io.ASCIIReader;

import classes.PacoteEnvio;

public class Play implements Runnable,SerialPortEventListener{

	public SerialPort porta = null;
	public Thread threadLeitura = null;
	public InputStream entrada;
	public CommPortIdentifier cp = null;
	public Gui gui;
	public StringBuffer leitura = new StringBuffer();
	public boolean aguardandoResposta;
	
	public Play(Gui gui)
	{
		this.gui = gui;
	}
	
	
	public void conecta(String portaEscolhida)
	{
		try {
			cp = CommPortIdentifier.getPortIdentifier(portaEscolhida);
			porta = (SerialPort)cp.open(portaEscolhida,100);
			porta.setSerialPortParams(9600, porta.DATABITS_8, porta.STOPBITS_2, porta.PARITY_NONE);
			porta.addEventListener(this);
			lerDados();
		} catch (TooManyListenersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void desconecta ()
	{
		porta.close();
	}
	
	public static void main(String[] args) {
//		Play play = new Play();
	}
	
	public void enviaPacotes(PacoteEnvio [] pacoteEnvio)
	{
		try{
			int i= 0;
			OutputStream saida = porta.getOutputStream();
			aguardandoResposta = true;
//			while (aguardandoResposta)
//			{
//				for (int j=0;j<pacoteEnvio.length;j++)
//				{
//					saida.write(pacoteEnvio[j].getBytes());
//				}
//				if (i==5)
//				{
//					throw new Exception("TIMEOUT");
//				}
//				Thread.sleep(100);
//				i++;
//			}
			for (int j=0;j<pacoteEnvio.length;j++)
			{
				saida.write(pacoteEnvio[j].getBytes());
			}
			Thread.sleep(100);
			saida.flush();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		lerDados();

	}
	
	public void enviaResposta()
	{
		try{
			OutputStream saida = porta.getOutputStream();
			saida.write("1".getBytes());
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
					for (int j = 0;j<bytes.length;j++)
					{
						String s =Byte.toString(bytes[j]);
						System.out.println(s);
					}
					String bytesLidos = new String(bytes);
					if (aguardandoResposta)
					{
						if (bytesLidos.equals("1"))
						{
							aguardandoResposta = false;
						}
					}
					else
					{
						leitura.append(bytesLidos);
						gui.getRecepcao().setText(leitura.toString());
						System.out.print(leitura);
						enviaResposta();
					}
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
