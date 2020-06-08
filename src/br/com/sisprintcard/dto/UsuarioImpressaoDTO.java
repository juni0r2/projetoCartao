package br.com.sisprintcard.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioImpressaoDTO {
	
	private String matricula;
	private String nome;
	private String dataVencimento;
	private String dataNascimento;
	private String cpf;
	private String tipoPlano;
	private String municipio;
	private String tipoDependencia;
	private String orgao;
	private String versao;
	private String matricOrgao;
	private String carenciaL1;
	private String carenciaL2;
	private String carenciaL3;
}
