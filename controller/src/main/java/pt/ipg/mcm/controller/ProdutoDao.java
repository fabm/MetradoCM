package pt.ipg.mcm.controller;

import pt.ipg.mcm.entities.VProdutoCategoriaEntity;
import pt.ipg.mcm.errors.Erro;
import pt.ipg.mcm.errors.MestradoException;
import pt.ipg.mcm.xmodel.ReqAddProduto;
import pt.ipg.mcm.xmodel.ReqGetProduto;
import pt.ipg.mcm.xmodel.ResAddProduto;
import pt.ipg.mcm.xmodel.ResGetProduto;
import pt.ipg.mcm.xmodel.Retorno;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ProdutoDao {
  @Resource(lookup = "jdbc/mestrado")
  private DataSource mestradoDataSource;

  public ResAddProduto addProduto(ReqAddProduto reqAddProduto) throws MestradoException {
    ResAddProduto resAddProduto = new ResAddProduto();

    try {
      Connection connection = mestradoDataSource.getConnection();
      ResultSet rs = connection.createStatement().executeQuery("SELECT SEQ_PRODUTO.NEXTVAL FROM DUAL");

      long idProduto = rs.getLong(1);

      CallableStatement call = connection.prepareCall("INSERT INTO PRODUTO (ID_PRODUTO,NOME,PRECO_ATUAL,ID_CATEGORIA,FOTO)VALUES(?,?,?,?,?)");

      call.setLong(1, idProduto);
      call.setString(2, reqAddProduto.getNome());
      call.setBigDecimal(3, reqAddProduto.getPrecoUnitario());
      call.setLong(4, reqAddProduto.getCategoria());
      InputStream in = new ByteArrayInputStream(reqAddProduto.getFoto());

      call.setBinaryStream(4, in);


      call.executeUpdate();

      resAddProduto.setId(idProduto);
    } catch (SQLException e) {
      throw new MestradoException(Erro.TECNICO);
    }

    resAddProduto.setRetorno(new Retorno(1, "Produto inserido com sucesso"));

    return resAddProduto;
  }

  public ResGetProduto getProduto(ReqGetProduto reqGetProduto) throws MestradoException {
    ResGetProduto resGetProduto = new ResGetProduto();

    try {
      String sqlString = "SELECT PRODUTO.NOME,\n" +
          "  PRODUTO.PRECO_ATUAL,\n" +
          "  PRODUTO.ID_CATEGORIA,\n" +
          "FROM PRODUTO\n" +
          "WHERE PRODUTO.ID_PRODUTO = ?";

      Connection connection = mestradoDataSource.getConnection();

      PreparedStatement call = connection.prepareStatement(sqlString);
      call.setLong(1, reqGetProduto.getId());
      ResultSet rs = call.executeQuery();

      if (!rs.next()) {
        throw new MestradoException(Erro.PRODUTO_NAO_ENCONTRADO, reqGetProduto.getId());
      }

      resGetProduto.setNome(rs.getString(1));
      resGetProduto.setPrecoUnitario(rs.getBigDecimal(2));
      resGetProduto.setCategoria(rs.getLong(3));
      resGetProduto.setFoto(rs.getBytes(4));

    } catch (SQLException e) {
      throw new MestradoException(Erro.TECNICO);
    }

    return resGetProduto;
  }

  public void saveFoto(long id, InputStream inputStream) throws MestradoException {
    try {
      String sqlString = "UPDATE PRODUTO\n" +
          "SET FOTO = ?\n" +
          "WHERE ID_PRODUTO = ?";
      Connection connection = mestradoDataSource.getConnection();
      CallableStatement call = connection.prepareCall(sqlString);

      call.setLong(2, id);

      int qt = call.executeUpdate();

      if (qt == 0) {
        throw new MestradoException(Erro.TECNICO);
      }
    } catch (SQLException e) {
      throw new MestradoException(Erro.TECNICO);
    }
  }

  public InputStream getFoto(Long id) throws MestradoException {
    String sqlString = "SELECT PRODUTO.FOTO\n" +
        "FROM PRODUTO\n" +
        "WHERE PRODUTO.ID_PRODUTO = ?";

    try {
      Connection connection = mestradoDataSource.getConnection();

      CallableStatement call = connection.prepareCall(sqlString);

      call.setLong(1, id);
      ResultSet rs = call.executeQuery();

      if (!rs.next()) {
        throw new MestradoException(Erro.PRODUTO_NAO_ENCONTRADO, id);
      }

      return rs.getBinaryStream(1);
    } catch (SQLException e) {
      throw new MestradoException(Erro.TECNICO);
    }

  }

  private ResultSet getResultProdutos(String strSql, long idCategoria) throws SQLException {
    Connection connection = mestradoDataSource.getConnection();
    PreparedStatement call = connection.prepareStatement(strSql.concat("\nWHERE ID_CATEGORIA = ?"));
    call.setLong(1, idCategoria);
    return call.executeQuery();
  }

  private ResultSet getResultProdutos(String strSql) throws SQLException {
    Connection connection = mestradoDataSource.getConnection();
    Statement call = connection.createStatement();
    return call.executeQuery(strSql);
  }

  public List<VProdutoCategoriaEntity> getProdutos(boolean withFoto, Long idCategoria) throws MestradoException {

    try {
      Connection connection = mestradoDataSource.getConnection();
      Statement call = connection.createStatement();
      String sqlStr;
      if (withFoto) {
        sqlStr = ",FOTO";
      } else {
        sqlStr = "";
      }

      sqlStr = String.format("SELECT NOME_CATEGORIA, DESCRICAO,PRECO_ATUAL,NOME_PRODUTO%s from V_PRODUTO_CATEGORIA", sqlStr);

      ResultSet rs;
      if (idCategoria != null) {
        rs = getResultProdutos(sqlStr, idCategoria);
      } else {
        rs = getResultProdutos(sqlStr);
      }

      List<VProdutoCategoriaEntity> vProdutoCategoriaEntities = new ArrayList<VProdutoCategoriaEntity>();

      while (rs.next()) {
        VProdutoCategoriaEntity vProdutoCategoriaEntity = new VProdutoCategoriaEntity();
        vProdutoCategoriaEntity.setNomeCategoria(rs.getString(1));
        vProdutoCategoriaEntity.setDescricao(rs.getString(2));
        vProdutoCategoriaEntity.setPrecoAtual(rs.getBigDecimal(3));
        vProdutoCategoriaEntity.setNomeProduto(rs.getString(4));
        if (withFoto) {
          vProdutoCategoriaEntity.setFoto(rs.getBytes(5));
        }
        vProdutoCategoriaEntities.add(vProdutoCategoriaEntity);
      }
      return vProdutoCategoriaEntities;
    } catch (SQLException e) {
      throw new MestradoException(Erro.TECNICO);
    }
  }


}
