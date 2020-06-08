package br.com.sisprintcard.principal;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import br.com.sisprintcard.common.XPS_Java_SDK;
import br.com.sisprintcard.exception.UsuarioNaoEncontradoException;
import br.com.sisprintcard.service.ImprimeCardService;

public class SisCartaoCassems {
	
	static final Logger logger = LogManager.getLogger(SisCartaoCassems.class.getName());

	public static void main(String[] args) {

		new XPS_Java_SDK();
		try {
			Long[] atributo = recebeAtributos(args);
			
			new ImprimeCardService().imprimir(atributo[0], atributo[1]);

		} catch (UsuarioNaoEncontradoException w) {
			w.printStackTrace();
		}

		System.exit(0);
	}

	private static Long[] recebeAtributos(String[] args) {
//		String idUsuario = "288258";
//		String idImpressora = "241";
	    String idImpressora = args[1];
		String idUsuario = args[0];

		if (idUsuario == null || idUsuario.trim().isEmpty())
			throw new UsuarioNaoEncontradoException("Usuário nao encontrado");
		else
		
		if (idImpressora == null || idImpressora.trim().isEmpty())
			throw new UsuarioNaoEncontradoException("Impressora não encontrada");

		Long[] variaveis = { Long.parseLong(idUsuario), Long.parseLong(idImpressora) };
		return variaveis;
	}

}
