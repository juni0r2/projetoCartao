package br.com.sisprintcard.service;

import java.util.regex.Pattern;

import javax.persistence.EntityManager;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import br.com.sisprintcard.common.JavaPrint;
import br.com.sisprintcard.common.PrinterStatusXML;
import br.com.sisprintcard.common.XPS_Java_SDK;
import br.com.sisprintcard.common.XPS_Java_SDK.XpsDriverInteropLib;
import br.com.sisprintcard.config.JpaUtil;
import br.com.sisprintcard.dao.BeneficiarioDAO;
import br.com.sisprintcard.dao.ImpressoraDAO;
import br.com.sisprintcard.dto.DadosParaImpressaoDto;
import br.com.sisprintcard.dto.UsuarioImpressaoDTO;
import br.com.sisprintcard.exception.UsuarioNaoEncontradoException;
import br.com.sisprintcard.model.EnImpressora;
import br.com.sisprintcard.model.EnSamBeneficiarioCartaoIdentif;

public class ImprimeCardService {
	
	static final Logger logger = LogManager.getLogger(ImprimeCardService.class.getName());

	public void imprimir(final Long idUsuario, final Long idImpressora) {

		try {
			EntityManager entityManager = JpaUtil.forneceEntityManager();
			EnImpressora impressora = new ImpressoraDAO(entityManager).buscaPorId(idImpressora);
			EnSamBeneficiarioCartaoIdentif usuario = new BeneficiarioDAO(entityManager).buscaPorId(idUsuario);
			entityManager.close();

			if (impressora == null) {
				System.out.println("\nImpressora não localizada ...");
				return;
			}

			if (usuario == null) {
				System.out.println("\nUsuário não encontrado...");
				return;
			}

			DadosParaImpressaoDto build = DadosParaImpressaoDto.builder()
					.beneficiarioCartaoIdentif(usuario.getKTrilhaCarenciaCartao()).impressora(impressora.getNome())
					.build();

			
			UsuarioImpressaoDTO usuarioImpressaoDTO = extraiDadosBeneficiario(build);
			System.out.println("Dados do Beneficiário :: ");
			System.out.println("Nome : "+ usuarioImpressaoDTO.getNome());

			System.out.println("\nDados da Impressora :: ");
			System.out.println(impressora);

			if (verificaStatusImpressora(impressora.getNome()))
				return;

			Imprimir(build.getImpressora(), usuarioImpressaoDTO);

		} catch (UsuarioNaoEncontradoException w) {
			w.printStackTrace();
		}
	}

	private void Imprimir(String mPrinterName, UsuarioImpressaoDTO usuario) {

		final int S_OK = 0;
		byte returnXML[] = new byte[XPS_Java_SDK.BUFFSIZE];
		int sizeOfReturnXML[] = new int[1];
		sizeOfReturnXML[0] = XPS_Java_SDK.BUFFSIZE;
		boolean bDisplayError = true;
		boolean bJobStarted = false;
		String returnValue;

		if (usuario.getVersao().length() > 2) {
			return;
		}

		PrinterStatusXML printerStatusXml = new PrinterStatusXML();

		System.setProperty("jna.library.path", "C:\\TEMP\\");

		if (usuario.getCpf().length() == 0)
			usuario.setCpf("0");

		if (usuario.getCpf().contains("OBRIGAT"))
			usuario.setCpf("0");

		if (usuario.getCpf().length() != 11)
			usuario.setCpf("0");

		if (S_OK == XpsDriverInteropLib.INSTANCE.StartJob(mPrinterName, returnXML, sizeOfReturnXML)) {
			bJobStarted = true;

			// Atualiza o status do registro atual para imprimindo
//			resultado.setFicStatus(S_OK);
//			resultado.setFicDataImpressao(new Date());
//			fila.update(resultado);

			// get PrintJobID
			printerStatusXml.Parse(returnXML, sizeOfReturnXML);

			String matriculaFormatada = usuario.getMatricula().replace("-", "").replace(".", "") + "=109=1=" + usuario.getVersao();
			sizeOfReturnXML[0] = XPS_Java_SDK.BUFFSIZE;

			String nomeTarjaMagnetica = RemoverAcentos.remover(usuario.getNome());
			System.out.println(nomeTarjaMagnetica + " - " + matriculaFormatada + " - " + usuario.getCpf());

			if (S_OK == XpsDriverInteropLib.INSTANCE.MagstripeEncode(mPrinterName, nomeTarjaMagnetica,
					nomeTarjaMagnetica.length(), matriculaFormatada, matriculaFormatada.length(), usuario.getCpf(), usuario.getCpf().length(),
					returnXML, sizeOfReturnXML)) {
				System.out.format("'%s' Magstripe Encode Succeed\n", mPrinterName);
				bDisplayError = false;

				// reset the buffer:
				sizeOfReturnXML[0] = XPS_Java_SDK.BUFFSIZE;
				if (S_OK == XpsDriverInteropLib.INSTANCE.MagstripeRead(mPrinterName, returnXML, sizeOfReturnXML)) {
					returnValue = PrinterStatusXML.cStringToJavaString(returnXML, sizeOfReturnXML[0]);
					System.out.format("'%s' MagStripe Read return length: %d\n\n%s\n\n", mPrinterName,
							sizeOfReturnXML[0], returnValue);
					bDisplayError = false;
				} else {
					// Magstripe Read has error
					bDisplayError = true;
				}
			}

		}

		// Any error needs to display
		if (bDisplayError) {
			printerStatusXml.Parse(returnXML, sizeOfReturnXML);

			// always cancel error condition
			// printerStatusXml.SetCommand(printerStatusXml.PRINTERACTION_CANCEL);

			returnValue = printerStatusXml.GetErrorMessage();
			System.out.format("\nMagstripe operation error. Printer return: %s\n Cancel Operation\n\n", returnValue);
			System.out.println("\nErro ao imprimir cartao");
			// XpsDriverInteropLib.INSTANCE.SendResponseToPrinter(mPrinterName,
			// printerStatusXml.GetCommand(), printerStatusXml.GetPrintJobID(),
			// printerStatusXml.GetErrorCode());
		} else {

			Integer modelo = 0;
			new JavaPrint(mPrinterName, usuario, modelo);
//			JavaPrint javaPrint = new JavaPrint(mPrinterName, matricula, nome, tipoDependencia, dataVencimento, versao,
//					dataNascimento, municipio, orgao, tipoPlano, carenciaL1, carenciaL2, carenciaL3, modelo,
//					matricOrgao);
//					javaPrint.Print();
			System.out.println("\n\nImprimindo Cartão ... ");

			// need to wait for data get spooler before calling EndJob
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}
		}

		// this sample always cancel job when it has error
		// thus only call EndJob on succeed job
		if (bJobStarted && !bDisplayError) {
			System.out.println("EndJob called");
			XpsDriverInteropLib.INSTANCE.EndJob(mPrinterName);
		}

		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}

	private UsuarioImpressaoDTO extraiDadosBeneficiario(DadosParaImpressaoDto dadosParaImpressaoDto) {
		String dados[] = dadosParaImpressaoDto.getBeneficiarioCartaoIdentif().split(Pattern.quote(";"));

//		// VariÃ¡veis AuxiliÃ¡res para o Armazenamento dos Dados
//		String matricula = dados[0].trim().replace("'", "");
//		String nome = dados[3].trim().replace("'", "");
//		String dataVencimento = dados[4].trim().replace("'", "");
//		String dataNascimento = dados[5].trim().replace("'", "");
//		String cpf = dados[7].trim().replace("'", "");
//		String tipoPlano = dados[9].trim().replace("'", "");
//		String municipio = dados[10].trim().replace("'", "");
//		String tipoDependencia = dados[11].trim().replace("'", "");
//		String orgao = dados[12].trim().replace("'", "");
//
		String carenciaL1 = "";
		String carenciaL2 = "";
		String carenciaL3 = "";

		int cont = 14;

		if (dados.length > 14) {
			for (int i = 14; i < dados.length - 1; i++) {
				String txt = dados[i].trim().replace("'", "");
				cont = i;
				if (txt.length() > 1) {
		
					if ((carenciaL1.length() + txt.length()) <= 55)
						carenciaL1 += txt + " ";
					else if ((carenciaL2.length() + txt.length()) <= 55)
						carenciaL2 += txt + " ";
					else
						carenciaL3 += txt + " ";
				}
			}
		}
//		System.out.println(nome);
//		String versao = dados[21].trim().replace("'", "");
		String matricOrgao = "";
		if (22 < dados.length)
			matricOrgao = dados[22].trim().replace("'", "");
		
		return UsuarioImpressaoDTO.builder()
			.matricula(dados[0].trim().replace("'", ""))
			.nome(dados[3].trim().replace("'", ""))
			.dataVencimento(dados[4].trim().replace("'", ""))
			.dataNascimento(dados[5].trim().replace("'", ""))
			.cpf(dados[7].trim().replace("'", ""))
			.tipoPlano(dados[9].trim().replace("'", ""))
			.municipio(dados[10].trim().replace("'", ""))
			.tipoDependencia(dados[11].trim().replace("'", ""))
			.orgao(dados[12].trim().replace("'", ""))
			.versao(dados[21].trim().replace("'", ""))
			.matricOrgao(matricOrgao)
			.carenciaL1(carenciaL1)
			.carenciaL2(carenciaL2)
			.carenciaL3(carenciaL3)
			.build();
	}

	private Boolean verificaStatusImpressora(String nomeImpressora) {
		PrinterStatusXML jp = new PrinterStatusXML();
		jp.GetPrinterMessages(nomeImpressora);

		if (jp.GetErrorCode() > 0) {
			System.out.println("\nErro Impressora :: " + jp.GetErrorMessage());
			return true;
		} else if (jp.toString() == null) {
			System.out.println("\nImpressora Offline.");
			return true;
		} else {
			System.out.println("\nImpressora Online.");
			return false;
		}
	}
}
