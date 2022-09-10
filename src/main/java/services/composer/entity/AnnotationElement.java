package services.composer.entity;

public class AnnotationElement {
    private String name;
    private String value;

    public AnnotationElement() {

    }

    public AnnotationElement(String value) {
        this.value = value;
    }

    public AnnotationElement(String name, String value) {
        this(value);
        this.name = name;
    }

    @Override
    public String toString() {
        String string = "";

        if (this.name == null) {
            string = String.format("\"%s\"", this.value);
        } else {
            string = String.format("%s = %s", this.name, this.value);
        }

        return string;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
