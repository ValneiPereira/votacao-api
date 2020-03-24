package com.konoha.votacao.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
@Entity
@Table(name = "pauta")
public class Pauta implements Serializable{
  
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cod_pauta")
  @EqualsAndHashCode.Include
  private Long codPauta;
  
  @NotNull
  @Column(name = "titulo")
  private String titulo;
  
  @Column(name = "descricao")
  private String descricao;
  
  @NotNull
  @EqualsAndHashCode.Include
  @Column(name = "data_criacao")
  private LocalDateTime dataCriacao;
  
  @Column(name = "observacao")
  private String observacao;
  
  @ManyToOne
  @JoinColumn(name = "cod_assembleia")
  private Assembleia assembleia;
  
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "pauta")
  private List<ItemPauta> listaItemPautas= new ArrayList<>();
  
  @Embedded
  private Sessao sessao;

}