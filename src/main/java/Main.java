import services.generator.CRUDGenerator;

public class Main {
    public static void main(String[] args) throws Exception {
        var crudGenerator = new CRUDGenerator();
        crudGenerator.run();
    }
}
