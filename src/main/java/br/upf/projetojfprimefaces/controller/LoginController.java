package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.UsuarioEntity;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpSession;
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
        
        FacesContext context = FacesContext.getCurrentInstance();
        
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
                
        
        UsuarioEntity usuarioDB = ejbFacade.buscarPorEmail(usuario.getEmail(), usuario.getSenha());
        
        if ((usuarioDB != null && usuarioDB.getId() != null)) {
            //caso as credenciais foram válidas, então direciona para página index
            
            session.setAttribute("usuarioLogado", usuarioDB);
            return "/admin/lancamentos.xhtml?faces-redirect=true";
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
    
    public String logout(){
        FacesContext context = FacesContext.getCurrentInstance();
        
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        
        session.invalidate();
        
        return "/login.xhtml?faces-redirect=true";
        
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

}
