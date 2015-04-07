package pt.ipg.mcm.services;


import pt.ipg.mcm.controller.PadeiroDao;
import pt.ipg.mcm.controller.imp.UtilizadorDao;
import pt.ipg.mcm.entities.PadeiroEntity;
import pt.ipg.mcm.entities.UtilizadorPadeiroEntity;
import pt.ipg.mcm.entities.VUtilizadorClienteEntity;
import pt.ipg.mcm.errors.MestradoException;
import pt.ipg.mcm.services.authorization.Role;
import pt.ipg.mcm.services.authorization.SecureService;
import pt.ipg.mcm.validacao.Validacao;
import pt.ipg.mcm.xmodel.ReqAddUtilizador;
import pt.ipg.mcm.xmodel.ResAddUtilizador;
import pt.ipg.mcm.xmodel.ResCreationUserClient;
import pt.ipg.mcm.xmodel.ResGetUtilizador;
import pt.ipg.mcm.xmodel.Retorno;
import pt.ipg.mcm.xmodel.UserClienteCreationRequest;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.security.auth.login.LoginException;
import javax.xml.bind.annotation.XmlElement;
import java.util.HashMap;
import java.util.Map;

@WebService(serviceName = "Utilizador", portName = "UtilizadorPort")
public class UtilizadorService extends SecureService {

  @Inject
  private UtilizadorDao utilizadorDao;

  @Inject
  private PadeiroDao padeiroDao;


  @WebMethod
  public ResAddUtilizador addUtilizador(@WebParam(name = "req-add-utilizador") @XmlElement(required = true) ReqAddUtilizador reqAddUtilizador) throws
      LoginException {
    try {
      Map<String, String> aliasMap = new HashMap<String, String>();
      aliasMap.put("idUtilizador", "Utilizador");
      Validacao.getInstance().valida(reqAddUtilizador, aliasMap);
      checkAuthorization(Role.ADMINISTRADOR);
      UtilizadorPadeiroEntity utilizadorPadeiroEntity = new UtilizadorPadeiroEntity();

      utilizadorPadeiroEntity.setLogin(reqAddUtilizador.getLogin());
      utilizadorPadeiroEntity.setPassword(reqAddUtilizador.getPassword());
      utilizadorPadeiroEntity.setNome(reqAddUtilizador.getName());

      utilizadorDao.addUtilizador(utilizadorPadeiroEntity);

      ResAddUtilizador resAddUtilizador = new ResAddUtilizador();
      resAddUtilizador.setRetorno(new Retorno(1, "Padeiro Adicionado Com Sucesso. "));

      return resAddUtilizador;

    } catch (MestradoException e) {
      ResAddUtilizador resAddUtilizador = new ResAddUtilizador();
      resAddUtilizador.setRetorno(new Retorno(e));
      return resAddUtilizador;
    }

  }

  @WebMethod
  public ResGetUtilizador getUtilizadorPadeiro(@WebParam(name = "versao") Long versao) {
    ResGetUtilizador resGetUtilizador = new ResGetUtilizador();
    try {
      PadeiroEntity padeiro = utilizadorDao.getPadeiro(versao);

    } catch (MestradoException e) {
      resGetUtilizador.setRetorno(new Retorno(e));
    }
    return resGetUtilizador;
  }

  @WebMethod
  public ResCreationUserClient createUserCliente(@WebParam(name = "cliente") UserClienteCreationRequest userClienteCreationRequest) {
    try {
      VUtilizadorClienteEntity user = new VUtilizadorClienteEntity();
      user.setContribuinte(userClienteCreationRequest.getContribuinte());
      user.setLogin(userClienteCreationRequest.getLogin());
      user.setPassword(userClienteCreationRequest.getPassword());
      user.setContribuinte(userClienteCreationRequest.getContribuinte());
      user.setNome(userClienteCreationRequest.getNome());
      user.setDatanascimento(userClienteCreationRequest.getDataNascimento());
      user.setContacto(userClienteCreationRequest.getContacto());
      user.setNporta(userClienteCreationRequest.getPorta());
      user.setEmail(userClienteCreationRequest.getEmail());
      user.setLocalidade(userClienteCreationRequest.getLocalidade());
      user.setMorada(userClienteCreationRequest.getMorada());
      utilizadorDao.createUserCliente(user);
      return new ResCreationUserClient();
    } catch (MestradoException e) {
      return new ResCreationUserClient(e);
    }
  }

}
