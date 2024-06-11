package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.UsuarioEntity;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpSession;

@Named(value = "sessionController")
@SessionScoped
public class SessionController implements Serializable {

    public UsuarioEntity getUsuarioLogado() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        return (UsuarioEntity) session.getAttribute("usuarioLogado");
    }

    public String getNomeUsuarioLogado() {
        UsuarioEntity usuario = getUsuarioLogado();
        return usuario != null ? usuario.getNome(): "Guest";
    }
}
