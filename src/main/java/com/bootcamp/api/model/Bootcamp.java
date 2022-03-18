package com.bootcamp.api.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="tb007_bootcamp", schema="bootcamp")
@SequenceGenerator(name="sq_id_bootcamp", sequenceName = "seq_id_bootcamp", schema="bootcamp", allocationSize=1, initialValue=1)
public class Bootcamp implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "sq_id_bootcamp", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_bootcamp")
	private Long idBootcamp;
	
	@Column(name = "nome_bootcamp")
	private String nomeBootcamp;
	
	@Column(name = "descricao_bootcamp")
	private String descricaoBootcamp;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_inicio")
	private Date dataInicio;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_fim")
	private Date dataFim;
	
	@ManyToMany
	@JoinTable(name = "tb008_conteudos_bootcamp", schema = "bootcamp", joinColumns = {@JoinColumn(name = "id_bootcamp")},
			inverseJoinColumns = {@JoinColumn(name = "id_conteudo")})
	private List<Conteudo> conteudosInscritos; 

}
