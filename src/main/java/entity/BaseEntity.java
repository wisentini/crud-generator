package entity;

import java.io.Serializable;

public interface BaseEntity<PK extends Serializable> {
    public PK getid();
}
