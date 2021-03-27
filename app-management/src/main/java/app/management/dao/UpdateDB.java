package app.management.dao;

import org.hibernate.exception.ConstraintViolationException;
import app.management.exception.DBException;
import app.management.model.entity.ApplicationDataEntity;

import java.util.Random;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;

/**
 * This class updates/retrieve information from the database.
 *
 * @since 1.0.0
 */
public class UpdateDB {

    private EntityManagerFactory entityManagerFactory;
    private int maxRetries;
    private int retryInterval;

    /**
     * Constructor to create a {@link UpdateDB} object.
     *
     * @param entityManagerFactory Entity user.management.manager factory object
     * @param maxRetries           Number of maximum retries for database connection failures
     * @param retryInterval        Retrying interval
     */
    public UpdateDB(EntityManagerFactory entityManagerFactory, int maxRetries, int retryInterval) {

        this.entityManagerFactory = entityManagerFactory;
        this.maxRetries = maxRetries;
        this.retryInterval = retryInterval;
    }

    /**
     * Helper method to get a new {@link EntityManager} object.
     *
     * @return entity user.management.manager object
     */
    private EntityManager getEntityManager() {

        return entityManagerFactory.createEntityManager();
    }

    /**
     * Helper method to close the {@link EntityManager}.
     *
     * @param entityManager entity user.management.manager object
     */
    private void closeEntityManager(EntityManager entityManager) {

        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
    }

    /**
     * This method inserts a list of entities into a table. If entity is already exists then it ignores the entity and
     * continues adding the other entries.
     *
     * @param entity Entity to be persisted
     * @throws DBException If the database connection causes an error
     */

    public void insertEntity(Object entity) throws DBException {

        int numAttempts = 0;
        EntityManager entityManager = getEntityManager();
        do {
            numAttempts++;
            try {
                entityManager.getTransaction().begin();
                entityManager.persist(entity);
                entityManager.getTransaction().commit();
                entityManager.close();
                return;
            } catch (PersistenceException e) {
                Throwable cause = e.getCause();
                if (cause != null) {
//                    if (cause instanceof CommunicationsException || cause instanceof JDBCConnectionException) {
//                        continue;
//                    }
                    Throwable nestedCause = cause.getCause();
                    if (nestedCause instanceof ConstraintViolationException) {
                        return;
                    }
                    throw new DBException("Exception occurred while connecting to the database: ", e);
                }
            } finally {
                closeEntityManager(entityManager);
            }
        } while (numAttempts <= maxRetries);
        throw new DBException("Connection failed. QueryTimeoutException occurred.");
    }

    public void removeEntity(String name, String id) throws DBException {

        int numAttempts = 0;
        EntityManager entityManager = getEntityManager();
        do {
            numAttempts++;
            try {
                entityManager.getTransaction().begin();
                ApplicationDataEntity applicationDataEntity = new ApplicationDataEntity();
                applicationDataEntity.setId(id);
                applicationDataEntity.setAppName(name);
                ApplicationDataEntity data =  entityManager.find(ApplicationDataEntity.class, applicationDataEntity);
                entityManager.remove(data);
                entityManager.getTransaction().commit();
                entityManager.close();
                return;
            } catch (PersistenceException e) {
                Throwable cause = e.getCause();
                if (cause != null) {
//                    if (cause instanceof CommunicationsException || cause instanceof JDBCConnectionException) {
//                        continue;
//                    }
                    Throwable nestedCause = cause.getCause();
                    if (nestedCause instanceof ConstraintViolationException) {
                        return;
                    }
                    throw new DBException("Exception occurred while connecting to the database: ", e);
                }
            } finally {
                closeEntityManager(entityManager);
            }
        } while (numAttempts <= maxRetries);
        throw new DBException("Connection failed. QueryTimeoutException occurred.");
    }

    private void validatePersistenceException(PersistenceException e) throws DBException {

        Throwable cause = e.getCause();
//        if (!(cause instanceof com.mysql.jdbc.CommunicationsException) && !(cause instanceof JDBCConnectionException)) {
//            throw new DBException("Exception occurred while connecting to the database", e);
//        }
        try {
            Thread.sleep(retryInterval + (new Random()).nextInt(1000));
        } catch (InterruptedException interruptedException) {
            throw new DBException(interruptedException);
        }
    }
}
