package ro.iss.model;

import java.io.Serial;
import java.io.Serializable;

public abstract class Entity<ID> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1234987612L;

    protected ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}