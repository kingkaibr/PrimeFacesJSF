package br.upf.projetojfprimefaces.facade;

import br.upf.projetojfprimefaces.entity.EntradaEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;

//respondem apenas a uma chamada e logo depois podem ser 
@Stateless //utilizados para outras chamadas de qualquer cliente.            
public class EntradaFacade extends AbstractFacade<EntradaEntity> {

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
    public EntradaFacade() {
        super(EntradaEntity.class);
    }

    private List<EntradaEntity> entityList;

    public List<EntradaEntity> buscarTodos() {
        entityList = new ArrayList<>();
        try {
            //utilizando JPQL para construir a query 
            Query query = getEntityManager().createQuery("SELECT p FROM EntradaEntity p order by p.id");
            //verifica se existe algum resultado para não gerar excessão
            if (!query.getResultList().isEmpty()) {
                entityList = (List<EntradaEntity>) query.getResultList();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }
    
    public EntradaEntity getEntradaByID(int id) {
        EntradaEntity entity = null;
        try {
            // Using JPQL to construct the query
            Query query = getEntityManager().createQuery("SELECT p FROM EntradaEntity p WHERE p.id = :id");
            query.setParameter("id", id);
            entity = (EntradaEntity) query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No result found for ID: " + id);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return entity;
    }
}
