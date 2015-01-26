package pt.ipg.mcm.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class EncomendaEntity {
  private long idEncomenda;
  private Timestamp dataEntrega;
  private EncomendaEntity encomendaAssociada;
  private long sync;
  private long fatura;
  private int estado;
  private ClienteEntity clienteEntity;
  private List<EncomendaProdutoEntity> encomendaProdutoEntityList;
  private List<EncomendaEntity> encomendasAssociadas;
  private CalendarioEntity calendarioEntity;

  public List<EncomendaProdutoEntity> getEncomendaProdutoEntityList() {
    if (encomendaProdutoEntityList == null) {
      encomendaProdutoEntityList = new ArrayList<EncomendaProdutoEntity>();
    }
    return encomendaProdutoEntityList;
  }

  public long getIdEncomenda() {
    return idEncomenda;
  }

  public void setIdEncomenda(long idEncomenda) {
    this.idEncomenda = idEncomenda;
  }

  public Timestamp getDataEntrega() {
    return dataEntrega;
  }

  public void setDataEntrega(Timestamp dataEntrega) {
    this.dataEntrega = dataEntrega;
  }

  public EncomendaEntity getEncomendaAssociada() {
    return encomendaAssociada;
  }

  public void setEncomendaAssociada(EncomendaEntity encomendaAssociada) {
    this.encomendaAssociada = encomendaAssociada;
  }

  public long getSync() {
    return sync;
  }

  public void setSync(long sync) {
    this.sync = sync;
  }

  public long getFatura() {
    return fatura;
  }

  public void setFatura(long fatura) {
    this.fatura = fatura;
  }

  public List<EncomendaEntity> getEncomendasAssociadas() {
    if (encomendasAssociadas == null) {
      return new ArrayList<EncomendaEntity>();
    }
    return encomendasAssociadas;
  }

  public CalendarioEntity getCalendarioEntity() {
    return calendarioEntity;
  }

  public void setCalendarioEntity(CalendarioEntity calendarioEntity) {
    this.calendarioEntity = calendarioEntity;
  }

  public ClienteEntity getClienteEntity() {
    return clienteEntity;
  }

  public void setClienteEntity(ClienteEntity clienteEntity) {
    this.clienteEntity = clienteEntity;
  }

  static enum Estado{
    A_ESPERA_PADEIRO(1),
    TEMPO_ESGOTADO_UTILIZADOR(2),
    A_ESPERA_UTILIZADOR(3)
    ;

    int nEstado;

    Estado(int nEstado) {
      this.nEstado = nEstado;
    }
  }
}
