package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.SaidaEntity;
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

@Named("saidaController")
@SessionScoped
public class SaidaController implements Serializable {

    @EJB
    private br.upf.projetojfprimefaces.facade.SaidaFacade ejbFacade;

    //objeto que representa uma saida
    private SaidaEntity saida = new SaidaEntity();
    //objeto que representa uma lista de saidas
    private List<SaidaEntity> saidaList = new ArrayList<>();

    private SaidaEntity selected;

    //atributo que será utilizado no momento da seleção da linha na datatable
    public SaidaEntity getSelected() {
        return selected;
    }

    public void setSelected(SaidaEntity selected) {
        this.selected = selected;
    }

    public SaidaEntity getSaida() {
        return saida;
    }

    public void setSaida(SaidaEntity pessoa) {
        this.saida = pessoa;
    }

    public List<SaidaEntity> getSaidaList() {
        if (saidaList == null || saidaList.isEmpty()) {
            saidaList = ejbFacade.buscarTodos();
        }
        return saidaList;
    }

    public void setSaidaList(List<SaidaEntity> saidaList) {
        this.saidaList = saidaList;
    }

    /**
     * Método utilizado para executar algumas ações antes de abrir o formulário
     * de criação de uma saida
     *
     * @return
     */
    public SaidaEntity prepareAdicionar() {
        saida = new SaidaEntity();
        return saida;
    }

    public void adicionarSaida() {
        //buscando a datahoraatual do sistema.
        Date datahoraAtual = new Timestamp(System.currentTimeMillis());
        saida.setDatahorareg(datahoraAtual);
        persist(SaidaController.PersistAction.CREATE, 
                "Registro incluído com sucesso!");
    }

    public void editarSaida() {
        persist(SaidaController.PersistAction.UPDATE, 
                "Registro alterado com sucesso!");
    }

    public void deletarSaida() {
        persist(SaidaController.PersistAction.DELETE, 
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
                        ejbFacade.createReturn(saida);
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
