package de.flower.rmt.service;

import com.google.common.base.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import de.flower.common.util.Check;
import de.flower.common.util.NameFinder;
import de.flower.rmt.model.QUniform;
import de.flower.rmt.model.Team;
import de.flower.rmt.model.Uniform;
import de.flower.rmt.repository.IUniformRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author flowerrrr
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class UniformManager extends AbstractService implements IUniformManager {

    @Autowired
    private IUniformRepo uniformRepo;

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void save(Uniform entity) {
        validate(entity);
        uniformRepo.save(entity);
    }

    @Override
    public Uniform loadById(Long id) {
        return Check.notNull(uniformRepo.findOne(id));
    }

    @Override
    public List<Uniform> findAll() {
        return uniformRepo.findAll();
    }

    @Override
    public List<Uniform> findAllByTeam(Team team) {
        Check.notNull(team);
        BooleanExpression predicate = QUniform.uniform.team.eq(team);
        return uniformRepo.findAll(predicate);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void delete(Long id) {
        Uniform entity = loadById(id);
        entity.setName(NameFinder.delete(entity.getName(), new Predicate<String>() {
            @Override
            public boolean apply(final String name) {
                return uniformRepo.findByName(name) == null;
            }
        }));
        uniformRepo.softDelete(entity);
    }

    @Override
    public Uniform newInstance(Team team) {
        return new Uniform(team);
    }

}
