  package com.konoha.votacao.controllers;

import java.net.URI;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.konoha.votacao.dto.AssembleiaDTO;
import com.konoha.votacao.forms.AtualizarAssembleiaForm;
import com.konoha.votacao.mappers.AssembleiaMapper;
import com.konoha.votacao.modelo.Assembleia;
import com.konoha.votacao.services.AssembleiaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("assembleias")
@RequiredArgsConstructor
public class AssembleiaController {

	private final AssembleiaService assembleiaService;

	private final AssembleiaMapper assembleiaMapper;

	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody AssembleiaDTO assembleiaDto) {
		log.info("Criando nova assembleia.");
		
		Assembleia assembleia = assembleiaService.save(assembleiaMapper.assembleiaDtoToAssembleia(assembleiaDto));
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{assembleiaId}")
				.buildAndExpand(assembleia.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}

	/**
	 * Busca uma uma lista de assembleia por ID
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<AssembleiaDTO> detalhar(@PathVariable Long id) {
		log.info("Buscando assembleia com ID = {}", id);
		Assembleia assembleia = assembleiaService.findById(id);
		AssembleiaDTO assembleiaDTO = assembleiaMapper.assembleiaToAssembleiaDto(assembleia);
		return ResponseEntity.ok(assembleiaDTO);
	}
	/**
	 * Busca uma uma lista de assembleia.
	 * 
	 * @param pageable
	 * @return
	 */
	@GetMapping
	public ResponseEntity<?> listarAssembleias(Pageable pageable) {
		log.info("Listando assembleias.");
		Page<Assembleia> assembleiaPage = assembleiaService.findAll(pageable);
		Page<AssembleiaDTO> assembleiaDtoPage = assembleiaPage.map(i -> assembleiaMapper.assembleiaToAssembleiaDto(i));
		return ResponseEntity.ok(assembleiaDtoPage);
	}

	/**
	 * Remove uma assembleia pelo ID.
	 * 
	 * @param id
	 * @return
	 */
	@Transactional
	@DeleteMapping("/{id}")
	public ResponseEntity<?> remover(@PathVariable Long id) {
		log.info("Removendo assembleia ID = {}", id);
		assembleiaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Atualiza uma assembleia pelo ID.
	 * 
	 * @Transactional serve para o spring comitar a transacao no final do metado.
	 * @param id
	 * @return
	 */
	@Transactional
	@PatchMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizarAssembleiaForm form) {
		log.info("Atualizando assembleia ID = {}", id);
		Assembleia assembleia = assembleiaMapper.atualizarAssembleiaFormToAssembleia(form);
		assembleia.setId(id);
		assembleia = assembleiaService.atualizar(assembleia);

		return ResponseEntity.ok(assembleiaMapper.assembleiaToAssembleiaDto(assembleia));

	}
	
	
}
