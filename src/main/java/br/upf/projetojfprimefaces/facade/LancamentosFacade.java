package br.upf.projetojfprimefaces.facade;

import br.upf.projetojfprimefaces.entity.LancamentosEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class LancamentosFacade extends AbstractFacade<LancamentosEntity> {

    @PersistenceContext(unitName = "ProjetojfprimefacesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LancamentosFacade() {
        super(LancamentosEntity.class);
    }

    private List<LancamentosEntity> entityList;

    public List<LancamentosEntity> buscarTodos() {
        entityList = new ArrayList<>();
        try {
            // JPQL query to fetch all SaidaEntity records sorted by ID
            Query query = getEntityManager().createQuery("SELECT p FROM LancamentosEntity p ORDER BY p.id");
            if (!query.getResultList().isEmpty()) {
                entityList = (List<LancamentosEntity>) query.getResultList();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }
}