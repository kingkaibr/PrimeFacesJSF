package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.EntradaEntity;
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

@Named(value = "entradaController")
@SessionScoped
public class EntradaController implements Serializable {

    @EJB
    private br.upf.projetojfprimefaces.facade.EntradaFacade ejbFacade;

    //objeto que representa uma entrada
    private EntradaEntity entrada = new EntradaEntity();
    //objeto que representa uma lista de entradas
    private List<EntradaEntity> entradaList = new ArrayList<>();

    private EntradaEntity selected;

    //atributo que será utilizado no momento da seleção da linha na datatable
    public EntradaEntity getSelected() {
        return selected;
    }

    public void setSelected(EntradaEntity selected) {
        this.selected = selected;
    }

    public EntradaEntity getEntrada() {
        return entrada;
    }

    public void setEntrada(EntradaEntity pessoa) {
        this.entrada = pessoa;
    }

    public List<EntradaEntity> getEntradaList() {
        if (entradaList == null || entradaList.isEmpty()) {
            entradaList = ejbFacade.buscarTodos();
        }
        return entradaList;
    }

    public void setEntradaList(List<EntradaEntity> entradaList) {
        this.entradaList = entradaList;
    }

    /**
     * Método utilizado para executar algumas ações antes de abrir o formulário
     * de criação de uma entrada
     *
     * @return
     */
    public EntradaEntity prepareAdicionar() {
        entrada = new EntradaEntity();
        return entrada;
    }

    public void adicionarEntrada() {
        //buscando a datahoraatual do sistema.
        Date datahoraAtual = new Timestamp(System.currentTimeMillis());
        entrada.setDatahorareg(datahoraAtual);
        persist(EntradaController.PersistAction.CREATE, 
                "Registro incluído com sucesso!");
    }

    public void editarEntrada() {
        persist(EntradaController.PersistAction.UPDATE, 
                "Registro alterado com sucesso!");
    }

    public void deletarEntrada() {
        persist(EntradaController.PersistAction.DELETE, 
                "Registro excluído com sucesso!");
    }

    public EntradaEntity getEntradaByID(int id) {
        try {
            return ejbFacade.find(id);
        } catch (EJBException ex) {
            addErrorMessage("Error retrieving SaidaEntity: " + ex.getMessage());
            return null;
        } catch (Exception ex) {
            addErrorMessage("Error retrieving SaidaEntity: " + ex.getMessage());
            return null;
        }
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
                        ejbFacade.createReturn(entrada);
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
            // Refresh the list after any action
            entradaList = ejbFacade.buscarTodos();
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
