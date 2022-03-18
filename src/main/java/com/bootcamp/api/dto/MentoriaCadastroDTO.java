package com.bootcamp.api.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentoriaCadastroDTO {

	private String tituloConteudo;
	private String descricaoConteudo;
	private Date dataMentoria;
	
}
