package com.bootcamp.api.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "idConteudo")
@Table(name="tb004_curso", schema="bootcamp")
public class Curso extends Conteudo {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "ch_curso")
	private Integer chCurso;

}
