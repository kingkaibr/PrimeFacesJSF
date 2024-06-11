package br.upf.projetojfprimefaces.facade;

import br.upf.projetojfprimefaces.entity.SaidaEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;

//respondem apenas a uma chamada e logo depois podem ser 
@Stateless //utilizados para outras chamadas de qualquer cliente.            
public class SaidaFacade extends AbstractFacade<SaidaEntity> {

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
    public SaidaFacade() {
        super(SaidaEntity.class);
    }

    private List<SaidaEntity> entityList;

    public List<SaidaEntity> buscarTodos() {
        entityList = new ArrayList<>();
        try {
            // JPQL query to fetch all SaidaEntity records sorted by ID
            Query query = getEntityManager().createQuery("SELECT p FROM SaidaEntity p ORDER BY p.id");
            if (!query.getResultList().isEmpty()) {
                entityList = (List<SaidaEntity>) query.getResultList();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }

}
