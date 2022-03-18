package com.bootcamp.api.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="tb002_conteudo", schema="bootcamp")
@SequenceGenerator(name="sq_id_conteudo", sequenceName = "seq_id_conteudo", schema="bootcamp", allocationSize=1, initialValue=1)
public class Conteudo implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "sq_id_conteudo", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_conteudo")
	private Long idConteudo;
	
	@Column(name = "titulo_conteudo")
	private String tituloConteudo;
	
	@Column(name = "descricao_conteudo")
	private String descricaoConteudo;

}
