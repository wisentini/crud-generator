package services.composer.entity;

public class Parameter {
    private String name;
    private String type;
    private boolean isPrimaryKey;

    public Parameter(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Parameter(String name, String type, boolean isPrimaryKey) {
        this(name, type);
        this.isPrimaryKey = isPrimaryKey;
    }

    public Parameter(Attribute attribute) {
        this(attribute.getName(), attribute.getType(), attribute.getIsPrimaryKey());
    }

    @Override
    public String toString() {
        var string = String.format("%s %s", this.type, this.name);
        return string;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.name = type;
    }

    public boolean getIsPrimaryKey() {
        return this.isPrimaryKey;
    }

    public void setIsPrimaryKey(boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }
}
