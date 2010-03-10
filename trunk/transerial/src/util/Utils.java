package util;

import classes.PacoteEnvio;

public class Utils {

	public CRC16 calcularCRC16(PacoteEnvio pacote) {
		CRC16 crc = new CRC16();
		int i = 0;
		
		crc.update(pacote.getSYN());
		crc.update(pacote.getIDPacote());
		for (i = 0; i < pacote.getRemetente().size(); i++) {
			crc.update(pacote.getRemetente().get(i));
		}
		for (i = 0; i < pacote.getDestinatario().size(); i++) {
			crc.update(pacote.getDestinatario().get(i));
		}
		for (i = 0; i < pacote.getDLESTX().size(); i++) {
			crc.update(pacote.getDLESTX().get(i));
		}
		for (i = 0; i < pacote.getDados().size(); i++) {
			crc.update(pacote.getDados().get(i));
		}
		for (i = 0; i < pacote.getDados().size(); i++) {
			crc.update(pacote.getDados().get(i));
		}
		if (pacote.getPossuiETB()) {
			for (i = 0; i < pacote.getDLEETB().size(); i++) {
				crc.update(pacote.getDLEETB().get(i));
			}
		} else {
			for (i = 0; i < pacote.getDLEETX().size(); i++) {
				crc.update(pacote.getDLEETX().get(i));
			}
		}
		crc.update(pacote.getPAD());
		
		return crc;
	}
	
}
