package com.bootcamp.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DevLogadoDTO {

	private Long idDev;
	private String nomeDev;
	private String token;
}
