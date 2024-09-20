package core.basesyntax.dao.animal;

import core.basesyntax.dao.AbstractDao;
import core.basesyntax.model.zoo.Animal;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class AnimalDaoImpl extends AbstractDao implements AnimalDao {
    public AnimalDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Animal save(Animal animal) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(animal);
            transaction.commit();
            return animal;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't add animal to DB: " + animal, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Animal> findByNameFirstLetter(Character character) {
        try (Session session = sessionFactory.openSession()) {
            Query<Animal> getAnimalByFirstLetterQuery = session.createQuery("FROM Animal a "
                    + "WHERE a.name LIKE :character1 OR a.name LIKE :character2", Animal.class);
            getAnimalByFirstLetterQuery.setParameter("character1",
                    Character.toUpperCase(character) + "%");
            getAnimalByFirstLetterQuery.setParameter("character2",
                    Character.toLowerCase(character) + "%");
            return getAnimalByFirstLetterQuery.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Can't get animal by first letter: " + character, e);
        }
    }
}
