package br.com.sisprintcard.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DadosParaImpressaoDto {
	
	private String impressora;
	private String beneficiarioCartaoIdentif;
	
}
