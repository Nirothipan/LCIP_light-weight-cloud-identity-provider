package licensekey.generator.dao;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import licensekey.generator.exception.DBException;
import licensekey.generator.model.entity.LicensekeyGeneratorEntity;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.JDBCConnectionException;

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
     * @param entityManagerFactory Entity manager factory object
     * @param maxRetries           Number of maximum retries for database connection failures
     * @param retryInterval        Retrying interval
     */
    public UpdateDB(EntityManagerFactory entityManagerFactory, int maxRetries, int retryInterval) {

        this.entityManagerFactory = entityManagerFactory;
        this.maxRetries = maxRetries;
        this.retryInterval = retryInterval;
    }

    /**
     * This method is to retrieve the JWT from the DB if exists
     *
     * @param username Email of the user who requested the JWT
     * @return JWT if exist or null
     * @throws DBException If the Database connection causes an error
     */
    public LicensekeyGeneratorEntity retrieveJWTKeyIfExists(LicensekeyGeneratorEntity licensekeyGeneratorEntity)
            throws DBException {

        int numAttempts = 0;
        do {
            numAttempts++;
            EntityManager entityManager = getEntityManager();

            try {
                entityManager.getTransaction().begin();
                return entityManager.find(LicensekeyGeneratorEntity.class, licensekeyGeneratorEntity);
            } catch (PersistenceException e) {
                validatePersistenceException(e);
            } finally {
                closeEntityManager(entityManager);
            }
        } while (numAttempts <= maxRetries);
        throw new DBException("Connection failed. QueryTimeoutException occurred.");

    }

    /**
     * Helper method to get a new {@link EntityManager} object.
     *
     * @return entity manager object
     */
    private EntityManager getEntityManager() {

        return entityManagerFactory.createEntityManager();
    }

    /**
     * Helper method to close the {@link EntityManager}.
     *
     * @param entityManager entity manager object
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

    /**
     * Helper method to analyze the {@link PersistenceException}. If the {@link PersistenceException} is not occurred
     * due to a {@link com.mysql.jdbc.CommunicationsException} or a {@link JDBCConnectionException}, it will throw a
     * {@link DBException}. Else, it will wait for the retry interval.
     *
     * @param e Persistence exception
     * @throws DBException if the error is not a JDBC exception nor a Communication exception
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
