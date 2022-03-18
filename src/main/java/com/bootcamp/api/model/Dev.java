package com.bootcamp.api.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
@Table(name="tb001_dev", schema="bootcamp")
@SequenceGenerator(name="sq_id_dev", sequenceName = "seq_id_dev", schema="bootcamp", allocationSize=1, initialValue=1)
public class Dev {
 
	@Id
	@GeneratedValue(generator = "sq_id_dev", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_dev")
	private Long idDev;
	
	@Column(name = "nome_dev")
	private String nomeDev;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "senha_dev")
	private String senhaDev;
	
	@ManyToMany
	@JoinTable(name = "tb005_conteudos_inscritos", schema = "bootcamp", joinColumns = {@JoinColumn(name = "id_dev")},
			inverseJoinColumns = {@JoinColumn(name = "id_conteudo")})
	private List<Conteudo> conteudosInscritos; 
	
	@ManyToMany
	@JoinTable(name = "tb006_conteudos_concluidos", schema = "bootcamp", joinColumns = {@JoinColumn(name = "id_dev")},
			inverseJoinColumns = {@JoinColumn(name = "id_conteudo")})
	private List<Conteudo> conteudosConcluidos; 
	
	@ManyToMany
	@JoinTable(name = "tb009_bootcamps_inscritos", schema = "bootcamp", joinColumns = {@JoinColumn(name = "id_dev")},
			inverseJoinColumns = {@JoinColumn(name = "id_bootcamp")})
	private List<Bootcamp> bootcampsInscritos; 
	
	@ManyToMany
	@JoinTable(name = "tb010_bootcamps_concluidos", schema = "bootcamp", joinColumns = {@JoinColumn(name = "id_dev")},
			inverseJoinColumns = {@JoinColumn(name = "id_bootcamp")})
	private List<Bootcamp> bootcampsConcluidos; 
}
