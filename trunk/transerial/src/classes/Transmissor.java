package classes;

import util.CRC16;
import util.Utils;

public class Transmissor {
	
	private Utils utils = new Utils();
	private CRC16 crc;
	
	public PacoteEnvio[] transmitirMensagem(String remetente, String destinatario, int tamanhoDados, String mensagem) {
		PacoteEnvio pacote;
		int qtdePacotes = definirQtdePacotes(tamanhoDados, mensagem);
		PacoteEnvio pacoteEnvio[] = new PacoteEnvio[qtdePacotes];
		int inicio = 0;
		for (int i = 0; i < qtdePacotes; i++) {
			pacote = new PacoteEnvio(tamanhoDados);
			
			setarRemetente(pacote, remetente);
			setarDestinatario(pacote, destinatario);
			
			setarDados(pacote, mensagem, inicio);
			
			crc = utils.calcularCRC16(pacote);
			setarCRC(pacote, crc);
			pacoteEnvio[i] = pacote;
		}
		return pacoteEnvio;
	}
	
	private void setarRemetente(PacoteEnvio pacote, String remetente) {
		Byte auxiliar;
		char a; int ascii;
		boolean d = false, l = false, dle = false;
		
		for (int i = 0; i < remetente.length(); i++) { // setar o remetente nos bytes respectivos
			a = remetente.charAt(i);
			if (a == 'd' || a == 'D') {
				d = true;
			} else {
				if ((a == 'l' || a == 'L') && d == true) {
					l = true;
				} else {
					if ((a == 'e' || a == 'E') && d == true && l == true) { // se chegar aqui, eh pq os 2 caracteres anteriores s‹o D e L, e ent‹o o DLE falso Ž formado
						dle = true;
					}
					d = false;
					l = false;
				}
			}
			ascii = a;
			auxiliar = new Byte((byte) ascii);
			pacote.getRemetente().add(auxiliar);
			if (dle) { // precisa colocar um DLE falso
				auxiliar = new Byte((byte) 'd');
				pacote.getRemetente().add(auxiliar);
				auxiliar = new Byte((byte) 'l');
				pacote.getRemetente().add(auxiliar);
				auxiliar = new Byte((byte) 'e');
				pacote.getRemetente().add(auxiliar);
				
				dle = false;
			}
		}
		if (remetente.length() < 10) { // se o remetente nao tiver 10 caracteres, completa com espa�os vazios
			for (int i = remetente.length(); i < 10; i++) {
				auxiliar = new Byte((byte) 32);
				pacote.getRemetente().add(auxiliar);
			}
		}
	}
	
	private void setarDestinatario(PacoteEnvio pacote, String destinatario) {
		Byte auxiliar;
		char a; int ascii;
		boolean d = false, l = false, dle = false;
		
		for (int i = 0; i < destinatario.length(); i++) { // setar o destinatario nos bytes respectivos
			a = destinatario.charAt(i);
			if (a == 'd' || a == 'D') {
				d = true;
			} else {
				if ((a == 'l' || a == 'L') && d == true) {
					l = true;
				} else {
					if ((a == 'e' || a == 'E') && d == true && l == true) { // se chegar aqui, eh pq os 2 caracteres anteriores s‹o D e L, e ent‹o o DLE falso Ž formado
						dle = true;
					}
					d = false;
					l = false;
				}
			}
			ascii = a;
			auxiliar = new Byte((byte) ascii);
			pacote.getDestinatario().add(auxiliar);
			if (dle) { // precisa colocar um DLE falso
				auxiliar = new Byte((byte) 'd');
				pacote.getDestinatario().add(auxiliar);
				auxiliar = new Byte((byte) 'l');
				pacote.getDestinatario().add(auxiliar);
				auxiliar = new Byte((byte) 'e');
				pacote.getDestinatario().add(auxiliar);
				
				dle = false;
			}
		}
		if (destinatario.length() < 10) { // se o destinatario nao tiver 10 caracteres, completa com espa�os vazios
			for (int i = destinatario.length(); i < 10; i++) {
				auxiliar = new Byte((byte) 32);
				pacote.getDestinatario().add(auxiliar);
			}
		}
	}
	
	private int definirQtdePacotes(int tamanhoDados, String mensagem) {
		int bytesMsg = mensagem.getBytes().length;
		int qtdePacotes = ((int) (bytesMsg / tamanhoDados)) + 1;
		return qtdePacotes;
	}
	
	private void setarDados(PacoteEnvio pacote, String mensagem, int inicio) {
		byte[] bytes = mensagem.getBytes();
		boolean d = false, l = false, dle = false;
		
		for (int i = 0; i < bytes.length; i++) {
			if (inicio == bytes.length) { // verifica se o pacote eh o ultimo a ser enviado. (necessario para setar o atributo DLE-ETX)
				pacote.setPossuiETX(true);
				pacote.getDados().setSize(i);
				break;
			}
			
			if (bytes[inicio] == 68 || bytes[inicio] == 100) {
				d = true;
			} else {
				if ((bytes[inicio] == 76 || bytes[inicio] == 108) && d == true) {
					l = true;
				} else {
					if ((bytes[inicio] == 69 || bytes[inicio] == 101) && d == true && l == true) { // se chegar aqui, eh pq os 2 caracteres anteriores s‹o D e L, e ent‹o o DLE falso Ž formado
						dle = true;
					}
					d = false;
					l = false;
				}
			}
			pacote.getDados().add(new Byte(bytes[inicio++]));
			
			if (dle) { // precisa colocar um DLE falso ----------- duvida: como colocar um DLE falso se nao cabe mais nenhum byte na area de dados?
				pacote.getDados().add(new Byte((byte) 'd'));
				pacote.getDados().add(new Byte((byte) 'l'));
				pacote.getDados().add(new Byte((byte) 'e'));
				
				dle = false;
			}
		}
		if (!pacote.getPossuiETX()) {
			pacote.setPossuiETB(true);
		}
	}
	
	private void setarCRC(PacoteEnvio pacote, CRC16 crc) {
		int valor1 = crc.value / 256;
		int valor2 = crc.value % 256;
		
		Byte byte1 = new Byte((byte) valor1); 
		Byte byte2 = new Byte((byte) valor2);
		
		pacote.getCRC16().add(byte1);
		pacote.getCRC16().add(byte2);
	}
	
}
