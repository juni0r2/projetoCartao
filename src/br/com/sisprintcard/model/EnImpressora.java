package br.com.sisprintcard.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
@Table(name = "TB_IMPRESSORA", schema = "SYSGS")
public class EnImpressora implements Serializable {

	private static final long serialVersionUID = -8509822801020035741L;

	@Id
    @Column(name = "IMP_ID")
    private Long id;

    @Column(name = "IMP_NOME")
    private String nome;

    @Column(name = "IMP_LOCALIZACAO")
    private String localizacao;

    @Column(name = "IMP_IP")
    private String ip;

    @Column(name = "IMP_SITUACAO")
    private String situacao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "IMP_DATA_ALTERACAO")
    private Date dataAlteracao;

    @Column(name = "IMP_ID_ANTIGO")
    private Long idAntigo;
    
    @Override
    public String toString() {
    	return "Nome : "+ this.nome +"\nIP : " + this.ip + "\nLocalizacao : "+ this.localizacao;
    }

}
