package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.UsuarioEntity;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;

@Named(value = "loginController")
@SessionScoped
public class LoginController implements Serializable {
    
    @EJB
    private br.upf.projetojfprimefaces.facade.UsuarioFacade ejbFacade;


    public LoginController() {
    }

    //objeto que representa um usuario
    private UsuarioEntity usuario;

    public void prepareAutenticarUsuario() {
        usuario = new UsuarioEntity();
    }

    /**
     * Método utilizado para inicializar métodos ao instanciar a classe...
     */
    @PostConstruct
    public void init() {
        prepareAutenticarUsuario();
    }

    /**
     * Método utilizado para validar login e senha.   
     * @return
     */
    public String validarLogin() {
        UsuarioEntity usuarioDB = ejbFacade.buscarPorEmail(usuario.getEmail(), usuario.getSenha());
        if ((usuarioDB != null && usuarioDB.getId() != null)) {
            //caso as credenciais foram válidas, então direciona para página index
            return "/user.xhtml?faces-redirect=true";
        } else {
            //senão, exibe uma mensagem de falha...
            FacesMessage fm = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Falha no Login!",
                    "Email ou senha incorreto!");
            FacesContext.getCurrentInstance().addMessage(null, fm);
            return null;
        }
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

}
