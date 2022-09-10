package dao;

import entity.BaseEntity;
import exception.DatabaseException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

public class BaseDAO<ID extends Serializable, Entity extends BaseEntity<ID>> {
    private final EntityManager entityManager;
    private final Class<Entity> entityClass;

    public BaseDAO(EntityManager entityManager, Class<Entity> entityClass) {
        this.entityManager = entityManager;
        this.entityClass = entityClass;
    }

    private EntityTransaction getEntityTransaction() throws DatabaseException {
        try {
            return this.entityManager.getTransaction();
        } catch (IllegalStateException illegalStateException) {
            throw new DatabaseException("getTransaction() can't be invoked on a JTA entity manager.");
        }
    }

    private void beginEntityTransaction(EntityTransaction entityTransaction) throws DatabaseException {
        try {
            entityTransaction.begin();
        } catch (IllegalStateException illegalStateException) {
            throw new DatabaseException("Couldn't begin resource transaction because an active one already exists.");
        }
    }

    private void commitEntityTransaction(EntityTransaction entityTransaction) throws DatabaseException {
        try {
            entityTransaction.commit();
        } catch (IllegalStateException illegalStateException) {
            throw new DatabaseException("Couldn't rollback resource transaction because there is none active.");
        } catch (RollbackException rollbackException) {
            throw new DatabaseException("Couldn't commit resource transaction.");
        }
    }

    private void cleanUpEntityTransaction(EntityTransaction entityTransaction) throws DatabaseException {
        try {
           if (entityTransaction.isActive()) {
               entityTransaction.rollback();
           }
        } catch (IllegalStateException illegalStateException) {
            throw new DatabaseException("Couldn't rollback resource transaction because there is none active.");
        } catch (PersistenceException persistenceException) {
            throw new DatabaseException("Couldn't persist data because an unexpected error condition was encountered.");
        }
    }

    public void save(Entity entity) throws DatabaseException {
        EntityTransaction entityTransaction = null;

        try {
            entityTransaction = this.getEntityTransaction();
            this.beginEntityTransaction(entityTransaction);
            this.entityManager.persist(entity);
            this.commitEntityTransaction(entityTransaction);
        } catch (EntityExistsException entityExistsException) {
            throw new DatabaseException("Couldn't persist entity because it already exists.");
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new DatabaseException("Couldn't persist data because it is not an entity.");
        } catch (TransactionRequiredException transactionRequiredException) {
            throw new DatabaseException("Couldn't persist entity because there is no active resource transaction.");
        } finally {
            if (entityTransaction != null) {
                this.cleanUpEntityTransaction(entityTransaction);
            }
        }
    }

    public void update(Entity entity) throws DatabaseException {
        EntityTransaction entityTransaction = null;

        try {
            entityTransaction = this.getEntityTransaction();
            this.beginEntityTransaction(entityTransaction);
            this.entityManager.merge(entity);
            this.commitEntityTransaction(entityTransaction);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new DatabaseException("Couldn't update entity either because it is not an entity or is a removed one.");
        } catch (TransactionRequiredException transactionRequiredException) {
            throw new DatabaseException("Couldn't update entity because there is no active resource transaction.");
        } finally {
            if (entityTransaction != null) {
                this.cleanUpEntityTransaction(entityTransaction);
            }
        }
    }

    public Entity find(ID id) throws DatabaseException {
        try {
            return this.entityManager.find(this.entityClass, id);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new DatabaseException("Couldn't find entity either because first argument does not denote an entity type or the second argument is not a valid type for that entity's primary key or is null.");
        }
    }

    public Query createQuery(String string) throws DatabaseException {
        try {
            return this.entityManager.createQuery(string);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new DatabaseException("Couldn't create query because it was found to be invalid.");
        }
    }

    @SuppressWarnings("unchecked")
    public ID createQueryWithSingleResult(String string, Class<ID> resultClass) throws DatabaseException {
        try {
            Query query = this.entityManager.createNativeQuery(string);
            return (ID)query.getSingleResult();
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new DatabaseException("Couldn't create query because it was found to be invalid.");
        }
    }

    @SuppressWarnings("unchecked")
    public List<Entity> find() throws DatabaseException {
        try {
            String string = String.format("from %s", this.entityClass.getSimpleName());
            Query query = this.createQuery(string);
            return query.getResultList();
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new DatabaseException("\"getResultList\" can't be called for a Java Persistence query language UPDATE or DELETE statement.");
        } catch (QueryTimeoutException queryTimeoutException) {
            throw new DatabaseException("Couldn't get result list because the query execution exceeded the query timeout value set and only the statement was rolled back.");
        } catch (TransactionRequiredException transactionRequiredException) {
            throw new DatabaseException("Couldn't find entities because there is no active resource transaction.");
        } catch (PessimisticLockException pessimisticLockException) {
            throw new DatabaseException("Couldn't find entities because pessimistic locking failed and the transaction was rolled back.");
        } catch (LockTimeoutException lockTimeoutException) {
            throw new DatabaseException("Couldn't find entities because pessimistic locking failed and only the statement was rolled back.");
        } catch (PersistenceException persistenceException) {
            throw new DatabaseException("Couldn't get result list because the query execution exceeded the query timeout value set and the transaction was rolled back.");
        }
    }

    public void delete(ID id) throws DatabaseException {
        EntityTransaction entityTransaction = null;

        try {
            entityTransaction = this.getEntityTransaction();
            this.beginEntityTransaction(entityTransaction);
            Entity entity = this.find(id);
            this.entityManager.remove(entity);
            this.commitEntityTransaction(entityTransaction);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new DatabaseException("Couldn't delete entity either because it is not an entity or is a detached one.");
        } catch (TransactionRequiredException transactionRequiredException) {
            throw new DatabaseException("Couldn't update entity because there is no active resource transaction.");
        } finally {
            if (entityTransaction != null) {
                this.cleanUpEntityTransaction(entityTransaction);
            }
        }
    }

    public void delete(Entity entity) throws DatabaseException {
        EntityTransaction entityTransaction = null;

        try {
            entityTransaction = this.getEntityTransaction();
            this.beginEntityTransaction(entityTransaction);
            this.entityManager.remove(entity);
            this.commitEntityTransaction(entityTransaction);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new DatabaseException("Couldn't delete entity either because it is not an entity or is a detached one.");
        } catch (TransactionRequiredException transactionRequiredException) {
            throw new DatabaseException("Couldn't update entity because there is no active resource transaction.");
        } finally {
            if (entityTransaction != null) {
                this.cleanUpEntityTransaction(entityTransaction);
            }
        }
    }

    public void delete() throws DatabaseException {
        EntityTransaction entityTransaction = null;

        try {
            entityTransaction = this.getEntityTransaction();
            this.beginEntityTransaction(entityTransaction);

            List<Entity> entities = this.find();

            for (Entity entity : entities) {
                this.delete(entity);
            }

            this.commitEntityTransaction(entityTransaction);
        } finally {
            if (entityTransaction != null) {
                this.cleanUpEntityTransaction(entityTransaction);
            }
        }
    }
}
