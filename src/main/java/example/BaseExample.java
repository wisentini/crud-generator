package example;

import dao.BaseDAO;
import entity.BaseEntity;
import exception.DatabaseException;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

public abstract class BaseExample<PK extends Serializable, Entity extends BaseEntity<PK>> {
    protected Entity entity;
    protected EntityManager entityManager;
    protected BaseDAO<PK, Entity> dao;

    public BaseExample(EntityManager entityManager, Class<Entity> entityClass) {
        this.entityManager = entityManager;
        this.dao = new BaseDAO<>(this.entityManager, entityClass);
    }

    public void run() {
        try {
            this.save();
            this.findAll();
            this.update();
            this.findOne();
            this.findAll();
            this.deleteOneByID();
            this.findAll();
        } catch (DatabaseException databaseException) {
            var message = databaseException.getMessage();
            System.err.println(message);
        }
    }

    protected abstract void save() throws DatabaseException;

    protected abstract void update() throws DatabaseException;

    protected Entity findOne() throws DatabaseException {
        System.out.println("\nFinding entity...");

        var id = this.entity.getid();
        var entity = this.dao.find(id);

        System.out.print("\nEntity found:");
        System.out.printf("\n\t%s\n", entity);

        return entity;
    }

    protected List<Entity> findAll() throws DatabaseException {
        var entities = this.dao.find();

        System.out.println("\nFinding entities...");
        System.out.print("\nEntities found:");

        for (var entity : entities) {
            System.out.printf("\n\t%s", entity);
        }

        System.out.println();

        return entities;
    }

    protected void deleteOneByID() throws DatabaseException {
        System.out.print("\nDeleting entity:");

        var id = this.entity.getid();

        System.out.printf("\n\t%s\n", entity);

        this.dao.delete(id);
    }

    protected void deleteOneByEntity() throws DatabaseException {
        System.out.print("\nDeleting entity:");

        var entity = this.findOne();

        System.out.printf("\n\t%s\n", entity);

        this.dao.delete(entity);
    }

    protected void deleteAll() throws DatabaseException {
        System.out.println("\nDeleting entities...");

        this.dao.delete();
    }
}
