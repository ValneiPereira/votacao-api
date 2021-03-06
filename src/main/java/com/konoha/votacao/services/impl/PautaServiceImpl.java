package com.konoha.votacao.services.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.konoha.votacao.exceptions.NotFoundException;
import com.konoha.votacao.modelo.Pauta;
import com.konoha.votacao.repository.PautaRepository;
import com.konoha.votacao.services.PautaService;
import com.konoha.votacao.services.ResultadoVotacaoSchedulerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PautaServiceImpl implements PautaService {

	private final PautaRepository pautaRepository;
	private final ResultadoVotacaoSchedulerService resultadoVotacaoScheduler;
	
	/**
	 * Salva uma pauta no sistema.
	 * 
	 */
	@Override
	public Pauta save(Pauta pauta) {
		pauta.setDataCriacao(LocalDateTime.now());
		pauta = pautaRepository.save(pauta);
		if(pauta.getSessao().getInicioSessao() != null) {
			// faz agendamento do envio de resultado
			resultadoVotacaoScheduler.scheduleTask(pauta); 
		} 
		return pauta;
	}

	/**
	 * Busca uma pauta específica pelo seu id.
	 * 
	 */
	@Override
	public Pauta findById(Long pautaId) {
		Optional<Pauta> optPauta = pautaRepository.findById(pautaId);
		if(optPauta.isPresent()){
			return optPauta.get();
		}
		throw new NotFoundException("Pauta não encontrada!");
	}

	/**
	 * Lista pautas de uma assembleia.
	 * 
	 */
	@Override
	public Page<Pauta> findByAssembleiaId(Long assembleiaId, Pageable pageable) {
		return pautaRepository.findByAssembleiaId(assembleiaId, pageable);
	}

	@Override
	public void deleteById(Long id) {
		findById(id);
		pautaRepository.deleteById(id);
	}
	
	
}
