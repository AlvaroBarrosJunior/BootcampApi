package com.bootcamp.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bootcamp.api.dto.BootcampCadastroDTO;
import com.bootcamp.api.exception.BootcampException;
import com.bootcamp.api.model.Bootcamp;
import com.bootcamp.api.model.Conteudo;
import com.bootcamp.api.model.Dev;
import com.bootcamp.api.repository.BootcampRepository;
import com.bootcamp.api.repository.ConteudoRepository;
import com.bootcamp.api.repository.DevRepository;

import javassist.NotFoundException;

@Service
public class BootcampService {

	@Autowired 
	private BootcampRepository bootcampRepository;
	
	@Autowired
	private ConteudoRepository conteudoRepository;
	
	@Autowired
	private DevService devService;
	
	@Autowired
	private DevRepository devRepository;

	public Bootcamp cadastrarBootcamp(BootcampCadastroDTO requisicao) throws BootcampException, NotFoundException {
		Optional<Bootcamp> bootcamp = bootcampRepository.findByNomeBootcamp(requisicao.getNomeBootcamp());
		
		if (bootcamp.isEmpty()) {
			if (requisicao.getDataInicio().after(requisicao.getDataFim())) {
				List<Conteudo> conteudos = new ArrayList<>();
				
				for(Long id:requisicao.getConteudosInscritos()) {
					Optional<Conteudo> conteudo = conteudoRepository.findById(id);
					
					if (conteudo.isPresent()) {
						conteudos.add(conteudo.get());
					} else {
						throw new NotFoundException("O conteudo ["+ id +"] não foi encontrado");
					}
				}
				
				Bootcamp novoBootcamp = new Bootcamp();
				novoBootcamp.setNomeBootcamp(requisicao.getNomeBootcamp());
				novoBootcamp.setDescricaoBootcamp(requisicao.getDescricaoBootcamp());
				novoBootcamp.setDataInicio(requisicao.getDataInicio());
				novoBootcamp.setDataFim(requisicao.getDataFim());
				novoBootcamp.setConteudosInscritos(conteudos);
				
				return bootcampRepository.saveAndFlush(novoBootcamp);
			} else {
				throw new BootcampException("Data fim não pode ser anterior a data início!");
			}
		} else {
			throw new BootcampException("Nome já cadastrado!");
		}
	}

	public Bootcamp adicionarConteudo(long idBootcamp, long idConteudo) throws NotFoundException, BootcampException {
		Optional<Bootcamp> bootcamp = bootcampRepository.findById(idBootcamp);
		
		if (bootcamp.isPresent()) {
			List<Dev> listaBootcamps = devRepository.findByIdBootcampsConcluidos(idBootcamp);
			
			if (!listaBootcamps.isEmpty()) {
				
				Optional<Conteudo> conteudo = conteudoRepository.findById(idConteudo);
				
				if (conteudo.isPresent()) {
					Bootcamp b = bootcamp.get();
					
					if (!b.getConteudosInscritos().contains(conteudo.get())) {
						List<Conteudo> lista = b.getConteudosInscritos();
						lista.add(conteudo.get());
						
						b.setConteudosInscritos(lista);
						
						bootcampRepository.save(b);
						
						devService.atualizarConteudosInscritos(idBootcamp);
						
						return b;
					} else {
						throw new BootcampException("Conteúdo já cadastrado no bootcamp!");
					}
				} else {
					throw new NotFoundException("Conteudo '["+ idConteudo + "] não encontrado!");
				}
			} else {
				throw new BootcampException("Não é possível adicionar conteúdos a um bootcamp concluído por algum dev");
			}
		} else {
			throw new NotFoundException("Bootcamp '["+ idBootcamp + "] não encontrado!");
		}
	}
}
