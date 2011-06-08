package de.flower.rmt.dao.jpa;

import de.flower.rmt.dao.IMyTeamDao;
import de.flower.rmt.model.MyTeamBE;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author oblume
 */
@Transactional
@Repository
public class MyTeamDao implements IMyTeamDao {

    @PersistenceContext
    EntityManager em;

    @Override
    public void save(MyTeamBE entity) {
        em.persist(entity);
    }


    @Override
    public MyTeamBE loadById(Long id) {
        return em.find(MyTeamBE.class, id);
    }

    @Override
    public List<MyTeamBE> loadAll() {
        return em.createQuery("select e from MyTeamBE e").getResultList();
    }
}