package com.bootcamp.api.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BootcampCadastroDTO {

	private String nomeBootcamp;
	private String descricaoBootcamp;
	private Date dataInicio;
	private Date dataFim;
	private List<Long> conteudosInscritos; 
}
