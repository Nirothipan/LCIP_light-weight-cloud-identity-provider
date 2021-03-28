package user.management.dao;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.JDBCConnectionException;
import user.management.exception.DBException;
import user.management.model.entity.UserDataEntity;
import user.management.utils.Constants;

import java.util.List;
import java.util.Random;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

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
                    if (cause instanceof CommunicationsException || cause instanceof JDBCConnectionException) {
                        continue;
                    }
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
                UserDataEntity userDataEntity = new UserDataEntity();
                userDataEntity.setTenantId(id);
                userDataEntity.setUserName(name);
                UserDataEntity data = entityManager.find(UserDataEntity.class, userDataEntity);
                entityManager.remove(data);
                entityManager.getTransaction().commit();
                entityManager.close();
                return;
            } catch (PersistenceException e) {
                Throwable cause = e.getCause();
                if (cause != null) {
                    if (cause instanceof CommunicationsException || cause instanceof JDBCConnectionException) {
                        continue;
                    }
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

    public UserDataEntity findEntity(String name, String id) throws DBException {

        int numAttempts = 0;
        EntityManager entityManager = getEntityManager();
        do {
            numAttempts++;
            try {
                entityManager.getTransaction().begin();
                UserDataEntity userDataEntity = new UserDataEntity();
                userDataEntity.setTenantId(id);
                userDataEntity.setUserName(name);
                return entityManager.find(UserDataEntity.class, userDataEntity);
            } catch (PersistenceException e) {
                Throwable cause = e.getCause();
                if (cause != null) {
                    if (cause instanceof CommunicationsException || cause instanceof JDBCConnectionException) {
                        continue;
                    }
                    Throwable nestedCause = cause.getCause();
                    if (nestedCause instanceof ConstraintViolationException) {
                        return null;
                    }
                    throw new DBException("Exception occurred while connecting to the database: ", e);
                }
            } finally {
                entityManager.getTransaction().commit();
                entityManager.close();
                closeEntityManager(entityManager);
            }
        } while (numAttempts <= maxRetries);
        throw new DBException("Connection failed. QueryTimeoutException occurred.");
    }

    public List<UserDataEntity> getAllUsers() throws DBException {

        int numAttempts = 0;
        do {
            numAttempts++;
            EntityManager entityManager = getEntityManager();

            try {
                Session session = (Session) entityManager.getDelegate();
                session.setDefaultReadOnly(true);
                TypedQuery<UserDataEntity> query = entityManager.createNamedQuery(
                        Constants.Database.Queries.GET_ALL_USER, UserDataEntity.class);
                if (query.getResultList().size() > 0) {
                    return query.getResultList();
                }
            } catch (PersistenceException e) {
                validatePersistenceException(e);
            } finally {
                closeEntityManager(entityManager);
            }
        } while (numAttempts <= maxRetries);
        throw new DBException("Connection failed. QueryTimeoutException occurred.");

    }

    /**
     * Helper method to analyze the {@link PersistenceException}. If the {@link PersistenceException} is not occurred
     * due to a {@link com.mysql.jdbc.CommunicationsException} or a {@link JDBCConnectionException}, it will throw a
     * {@link DBException}. Else, it will wait for the retry interval.
     *
     * @param e Persistence user.management.exception
     * @throws DBException if the error is not a JDBC user.management.exception nor a Communication user.management.exception
     */
    private void validatePersistenceException(PersistenceException e) throws DBException {

        Throwable cause = e.getCause();
        if (!(cause instanceof com.mysql.jdbc.CommunicationsException) && !(cause instanceof JDBCConnectionException)) {
            throw new DBException("Exception occurred while connecting to the database", e);
        }
        try {
            Thread.sleep(retryInterval + (new Random()).nextInt(1000));
        } catch (InterruptedException interruptedException) {
            throw new DBException(interruptedException);
        }
    }
}
