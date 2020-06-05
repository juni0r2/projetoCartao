package br.com.sisprintcard.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "SAM_BENEFICIARIO_CARTAOIDENTIF", schema = "BENNERP")
public class EnSamBeneficiarioCartaoIdentif implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8647365849070042851L;

	@Id
    @Column(name = "HANDLE")
    private Long handle;

    @Column(name = "Z_GRUPO")
    private Integer grupo;

    @Temporal(TemporalType.DATE)
    @Column(name = "COMPETENCIAINICIALVALIDADE")
    private Date competenciaInicialValidade;

    @Temporal(TemporalType.DATE)
    @Column(name = "COMPETENCIAFINALVALIDADE")
    private Date competenciaFinalValidade;

    @Column(name = "FATURA")
    private Integer fatura;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATAGERACAO")
    private Date dataGeracao;

    @Column(name = "COMPOSICAOCARTAO")
    private String composicaoCartao;

    @Column(name = "NUMEROCARTAO")
    private String numeroCartao;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATAINICIALVALIDADE")
    private Date dataInicialValidade;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATAFINALVALIDADE")
    private Date dataFinalValidade;

    @Column(name = "CARTAOMOTIVORETORNO")
    private Integer cartaoMotivoRetorno;

    @Column(name = "TIPOCARTAO")
    private Integer tipoCartao;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATARETIRADA")
    private Date dataRetirada;

    @Column(name = "RGCPFRETIRADA")
    private String rgCpfRetirada;

    @Column(name = "NOMERETIRADA")
    private String nomeRetirada;

    @Column(name = "SENHACARTAO")
    private Integer senhaCartao;

    @Column(name = "VALORFATURADO")
    private BigDecimal valorFaturado;

    @Column(name = "VIA")
    private String via;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATACANCELAMENTO")
    private Date dataCancelamento;

    @Column(name = "DV")
    private String dv;

    @Column(name = "BENEFICIARIO")
    private Integer beneficiario;

    @Column(name = "CARTAOMOTIVO")
    private Integer cartaoMotivo;

    @Column(name = "SITUACAO")
    private String situacao;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATAEMISSAO")
    private Date dataEmissao;

    @Column(name = "K_IMPRESSO")
    private String kImpresso;

    @Column(name = "K9_VALIDADEPRAZODATA")
    private String k9ValidadePrazoData;

    @Column(name = "K9_USUARIOREVALIDACAO")
    private String k9UsuarioRevalidacao;

    @Temporal(TemporalType.DATE)
    @Column(name = "K_BENEFDATAAUTORIZ")
    private Date kBenefDataAutoriz;

    @Column(name = "K_TRILHACARENCIACARTAO")
    private String kTrilhaCarenciaCartao;

    @Column(name = "NOMENOCARTAO")
    private String nomeNoCartao;

    @Column(name = "Z_NOMENOCARTAO")
    private String zNomeNoCartao;

    @Column(name = "K_USUARIOGERACAO")
    private Integer kUsuarioGeracao;
    
    @Override
    public String toString() {
    	return "Nome do Beneficiário : " + this.nomeNoCartao;
    }
}
