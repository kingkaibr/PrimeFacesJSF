package br.upf.projetojfprimefaces.facade;

import br.upf.projetojfprimefaces.entity.UsuarioEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;

//respondem apenas a uma chamada e logo depois podem ser 
@Stateless //utilizados para outras chamadas de qualquer cliente.            
public class UsuarioFacade extends AbstractFacade<UsuarioEntity> {

    /**
     * Definindo a unidade de persistencia
     */
    @PersistenceContext(unitName = "ProjetojfprimefacesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Construtor que passa para superclasse a instância de PessoaEntity
     */
    public UsuarioFacade() {
        super(UsuarioEntity.class);
    }

    private List<UsuarioEntity> entityList;

    public List<UsuarioEntity> buscarTodos() {
        entityList = new ArrayList<>();
        try {
            //utilizando JPQL para construir a query 
            Query query = getEntityManager().createQuery("SELECT p FROM UsuarioEntity p order by p.nome");
            //verifica se existe algum resultado para não gerar excessão
            if (!query.getResultList().isEmpty()) {
                entityList = (List<UsuarioEntity>) query.getResultList();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }

    /**
     * Buscar uma pessoa por email
     * @param email
     * @param senha
     * @return 
     */
    public UsuarioEntity buscarPorEmail(String email, String senha) {
        UsuarioEntity usuario = new UsuarioEntity();
        try {
            //utilizando JPQL para construir a query 
            Query query = getEntityManager()
                    .createQuery("SELECT p FROM UsuarioEntity p WHERE p.email = :email AND p.senha = :senha");
            query.setParameter("email", email);
            query.setParameter("senha", senha);

            //verifica se existe algum resultado para não gerar excessão
            if (!query.getResultList().isEmpty()) {
                usuario = (UsuarioEntity) query.getSingleResult();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return usuario;
    }
}
