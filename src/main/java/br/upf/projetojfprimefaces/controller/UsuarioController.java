package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.UsuarioEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named(value = "usuarioController")
@SessionScoped
public class UsuarioController implements Serializable {

    @EJB
    private br.upf.projetojfprimefaces.facade.UsuarioFacade ejbFacade;

    //objeto que representa um usuario
    private UsuarioEntity usuario = new UsuarioEntity();
    //objeto que representa uma lista de usuarios
    private List<UsuarioEntity> usuarioList = new ArrayList<>();

    private UsuarioEntity selected;

    //atributo que será utilizado no momento da seleção da linha na datatable
    public UsuarioEntity getSelected() {
        return selected;
    }

    public void setSelected(UsuarioEntity selected) {
        this.selected = selected;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public List<UsuarioEntity> getUsuarioList() {
        return ejbFacade.buscarTodos();
    }

    public void setUsuarioList(List<UsuarioEntity> usuarioList) {
        this.usuarioList = usuarioList;
    }

    /**
     * Método utilizado para executar algumas ações antes de abrir o formulário
     * de criação de um usuario
     *
     * @return
     */
    public UsuarioEntity prepareAdicionar() {
        usuario = new UsuarioEntity();
        return usuario;
    }

    public void adicionarUsuario() {
        //buscando a datahoraatual do sistema.
        Date datahoraAtual = new Timestamp(System.currentTimeMillis());
        usuario.setDatahorareg(datahoraAtual);
        persist(UsuarioController.PersistAction.CREATE, 
                "Registro incluído com sucesso!");
    }

    public void editarPessoa() {
        persist(UsuarioController.PersistAction.UPDATE, 
                "Registro alterado com sucesso!");
    }

    public void deletarPessoa() {
        persist(UsuarioController.PersistAction.DELETE, 
                "Registro excluído com sucesso!");
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    /**
     * declaração de uma classe enum disponivel para classe
     */
    public static enum PersistAction {
        CREATE,
        DELETE,
        UPDATE
    }

    /**
     * Método que recebe a requisição para persistencia e executa a mesma.
     * @param persistAction
     * @param successMessage 
     */
    private void persist(PersistAction persistAction, String successMessage) {
        try {
            if (null != persistAction) {
                switch (persistAction) {
                    case CREATE:
                        ejbFacade.createReturn(usuario);
                        break;
                    case UPDATE:
                        ejbFacade.edit(selected);
                        selected = null;
                        break;
                    case DELETE:
                        ejbFacade.remove(selected);
                        selected = null;
                        break;
                    default:
                        break;
                }
            }
            addSuccessMessage(successMessage);
        } catch (EJBException ex) {
            String msg = "";
            Throwable cause = ex.getCause();
            if (cause != null) {
                msg = cause.getLocalizedMessage();
            }
            if (msg.length() > 0) {
                addErrorMessage(msg);
            } else {
                addErrorMessage(ex.getLocalizedMessage());
            }
        } catch (Exception ex) {
            addErrorMessage(ex.getLocalizedMessage());
        }
    }

}
