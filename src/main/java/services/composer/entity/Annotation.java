package services.composer.entity;

import java.util.List;
import java.util.stream.Collectors;

public class Annotation {
    private String name;
    private List<AnnotationElement> annotationElements;

    public Annotation() {

    }

    public Annotation(String name) {
        this.name = name;
    }

    public Annotation(String name, List<AnnotationElement> annotationElements) {
        this(name);
        this.annotationElements = annotationElements;
    }

    @Override
    public String toString() {
        var annotationNameString = String.format("@%s", this.name);
        var stringBuilder = new StringBuilder(annotationNameString);

        if (this.annotationElements != null && !this.annotationElements.isEmpty()) {
            var elementsString = this.annotationElements.stream().map(AnnotationElement::toString).collect(Collectors.joining(", "));
            var annotationBodyString = String.format("(%s)", elementsString);
            stringBuilder.append(annotationBodyString);
        }

        var string = stringBuilder.toString();
        return string;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AnnotationElement> getElements() {
        return this.annotationElements;
    }

    public void setElements(List<AnnotationElement> annotationElements) {
        this.annotationElements = annotationElements;
    }
}
