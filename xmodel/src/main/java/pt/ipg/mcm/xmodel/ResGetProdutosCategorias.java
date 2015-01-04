package pt.ipg.mcm.xmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Res-get-produtos-categorias")
public class ResGetProdutosCategorias {

  @XmlElement(name = "produto-categoria", required = true)
  private List<ProdutoCategoria> produtoCategoriaList;

  @XmlElement(required = false)
  private Retorno retorno;

  public List<ProdutoCategoria> getProdutoCategoriaList() {
    if (produtoCategoriaList == null) {
      produtoCategoriaList = new ArrayList<ProdutoCategoria>();
    }
    return this.produtoCategoriaList;
  }

  public Retorno getRetorno() {
    return retorno;
  }

  public void setRetorno(Retorno retorno) {
    this.retorno = retorno;
  }
}
