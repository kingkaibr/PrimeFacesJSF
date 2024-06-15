package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.LancamentosEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("lancamentosController")
@SessionScoped
public class LancamentosController implements Serializable {

    @EJB
    private br.upf.projetojfprimefaces.facade.LancamentosFacade ejbFacade;

    //objeto que representa uma lancamentos
    private LancamentosEntity lancamentos = new LancamentosEntity();
    //objeto que representa uma lista de lancamentoss
    private List<LancamentosEntity> lancamentosList = new ArrayList<>();

    private LancamentosEntity selected;

    //atributo que será utilizado no momento da seleção da linha na datatable
    public LancamentosEntity getSelected() {
        return selected;
    }

    public void setSelected(LancamentosEntity selected) {
        this.selected = selected;
    }

    public LancamentosEntity getLancamentos() {
        return lancamentos;
    }

    public void setLancamentos(LancamentosEntity pessoa) {
        this.lancamentos = pessoa;
    }

    public List<LancamentosEntity> getLancamentosList() {
        if (lancamentosList == null || lancamentosList.isEmpty()) {
            lancamentosList = ejbFacade.buscarTodos();
        }
        return lancamentosList;
    }

    public void setLancamentosList(List<LancamentosEntity> lancamentosList) {
        this.lancamentosList = lancamentosList;
    }

    /**
     * Método utilizado para executar algumas ações antes de abrir o formulário
     * de criação de uma lancamentos
     *
     * @return
     */
    public LancamentosEntity prepareAdicionar() {
        lancamentos = new LancamentosEntity();
        return lancamentos;
    }

    public void adicionarLancamentos() {
        
        persist(LancamentosController.PersistAction.CREATE, 
                "Registro incluído com sucesso!");
    }

    public void editarLancamentos() {
        persist(LancamentosController.PersistAction.UPDATE, 
                "Registro alterado com sucesso!");
    }

    public void deletarLancamentos() {
        persist(LancamentosController.PersistAction.DELETE, 
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
                        if(lancamentos.getSaida() == 0){
                            lancamentos.setSaida(-1);
                        } else if(lancamentos.getEntrada() == 0){
                            lancamentos.setEntrada(-1);
                        }
                        ejbFacade.createReturn(lancamentos);
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
            lancamentosList = ejbFacade.buscarTodos();
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
