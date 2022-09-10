package services.composer.entity;

import util.ComposerUtil;

import java.util.List;

public class Attribute {
    private String name;
    private String type;
    private int size;
    private boolean isPrimaryKey;
    private List<Annotation> annotations;
    private List<Integer> modifiers;
    private String columnName;

    public Attribute(String name, String type, List<Integer> modifiers) {
        this.name = name;
        this.type = type;
        this.modifiers = modifiers;
    }

    public Attribute(String name, String type, int size, boolean isPrimaryKey, List<Annotation> annotations, List<Integer> modifiers, String columnName) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.isPrimaryKey = isPrimaryKey;
        this.annotations = annotations;
        this.modifiers = modifiers;
        this.columnName = columnName;
    }

    @Override
    public String toString() {
        String annotations = ComposerUtil.composeAnnotations(this.annotations);
        String modifiers = ComposerUtil.composeModifiers(this.modifiers);

        String string = String.format("%s %s %s %s", annotations, modifiers, this.type, this.name);
        return string.trim();
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

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean getIsPrimaryKey() {
        return this.isPrimaryKey;
    }

    public void setIsPrimaryKey(boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    public List<Annotation> getAnnotations() {
        return this.annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }

    public List<Integer> getModifiers() {
        return this.modifiers;
    }

    public void setModifiers(List<Integer> modifiers) {
        this.modifiers = modifiers;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}
