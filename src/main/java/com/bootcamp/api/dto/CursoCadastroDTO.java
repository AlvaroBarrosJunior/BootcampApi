package com.bootcamp.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoCadastroDTO {
	
	private String tituloConteudo;
	private String descricaoConteudo;
	private Integer chCurso;

}
