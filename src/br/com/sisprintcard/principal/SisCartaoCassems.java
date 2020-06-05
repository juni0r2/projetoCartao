package br.com.sisprintcard.principal;

import br.com.sisprintcard.common.XPS_Java_SDK;
import br.com.sisprintcard.exception.UsuarioNaoEncontradoException;
import br.com.sisprintcard.service.ImprimeCardService;

public class SisCartaoCassems {

	public static void main(String[] args) {

		new XPS_Java_SDK();
		try {
			Long[] atributo = recebeAtributos(args);
			
			new ImprimeCardService().imprimir(atributo[0], atributo[1]);
//			EntityManagerFactory createEntityManagerFactory = Persistence.createEntityManagerFactory("conta");
//			EntityManager em = createEntityManagerFactory.createEntityManager();
//			EnSamBeneficiarioCartaoIdentif usuario = new BeneficiarioDAO(em).buscaPorId(atributo[0]);
//			EnImpressora impressora = new ImpressoraDAO(em).buscaPorId(atributo[1]);
//			createEntityManagerFactory.close();
//
//			if (impressora != null && usuario != null) {
//
//				System.out.println("\nDados da Impressora :: ");
//				System.out.println(impressora);
//				System.out.println("Imprimir Dados do Beneficiario :: ");
//
//				DadosParaImpressaoDto build = DadosParaImpressaoDto.builder()
//						.beneficiarioCartaoIdentif(usuario.getKTrilhaCarenciaCartao()).impressora(impressora.getNome())
//						.build();
//			}

		} catch (UsuarioNaoEncontradoException w) {
			w.printStackTrace();
		}

		System.exit(0);
	}

	private static Long[] recebeAtributos(String[] args) {
		String idUsuario = "288258";
		String idImpressora = "241";
//	    String idImpressora = args[1];
//		String idUsuario = args[0];

		if (idUsuario == null || idUsuario.trim().isEmpty())
			throw new UsuarioNaoEncontradoException("Usuário não encontrado");
		else
		
		if (idImpressora == null || idImpressora.trim().isEmpty())
			throw new UsuarioNaoEncontradoException("Impressora não encontrada");

		Long[] variaveis = { Long.parseLong(idUsuario), Long.parseLong(idImpressora) };
		return variaveis;
	}

}
