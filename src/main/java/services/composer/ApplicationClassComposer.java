package services.composer;

import services.composer.entity.*;
import util.StringUtil;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ApplicationClassComposer {
    public static BaseClass composeClass(List<ExampleClass> exampleClasses) {
        List<String> imports = List.of("example.*", "util.PropertiesUtil", "exception.FileException", "javax.persistence.*", "java.util.Properties");
        String classPackage = "";
        List<Integer> modifiers = List.of(Modifier.PUBLIC);
        String className = "Application";
        List<Attribute> attributes = new ArrayList<>();
        Method method = composeMainMethod(exampleClasses);
        List<Method> methods = List.of(method);
        List<String> classesToExtend = new ArrayList<>();
        List<String> classesToImplement = new ArrayList<>();

        return new BaseClass(imports, classPackage, modifiers, className, attributes, methods, classesToExtend, classesToImplement);
    }

    private static Method composeMainMethod(List<ExampleClass> exampleClasses) {
        List<Integer> modifiers = List.of(Modifier.PUBLIC, Modifier.STATIC);
        List<String> thrownExceptions = new ArrayList<String>();
        String returnType = "void";
        String name = "main";
        Parameter parameter = new Parameter("args", "String[]");
        List<Parameter> parameters = List.of(parameter);
        String body = composeMainMethodBody(exampleClasses);

        return new Method(modifiers, thrownExceptions, returnType, name, parameters, body);
    }

    private static String composeMainMethodBody(List<ExampleClass> exampleClasses) {
        StringBuilder stringBuilder = new StringBuilder();

        for (ExampleClass exampleClass : exampleClasses) {
            String exampleClassName = exampleClass.getName();
            String classInstantiationString = String.format("new %s(entityManager).run();", exampleClassName);

            stringBuilder.append(classInstantiationString);
        }

        String classesHandling = stringBuilder.toString();

        return """
            EntityManagerFactory entityManagerFactory = null;
            EntityManager entityManager = null;
                    
            try {
                Properties properties = PropertiesUtil.getProperties();
                String persistenceUnitName = properties.getProperty("persistence.unit.name");
                entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
                entityManager = entityManagerFactory.createEntityManager();
                
                %s
            } catch (IllegalStateException illegalStateException) {
                String message = "\\nCouldn't create entity manager because the entity manager factory has already been closed.\\n";
                System.err.println(message);
            } catch (FileException fileException) {
                String message = fileException.getMessage();
                System.out.println(message);
            } finally {
                if (entityManager != null) {
                    try {
                        entityManager.close();
                    } catch (IllegalStateException illegalStateException) {
                        String message = "\\nCouldn't close entity manager because it is container-managed.\\n";
                        System.err.println(message);
                    }
               }
         
               if (entityManagerFactory != null) {
                    try {
                        entityManagerFactory.close();
                    } catch (IllegalStateException illegalStateException) {
                        String message = "\\nCouldn't close entity manager factory because it has already been closed.\\n";
                        System.err.println(message);
                    }
               }
            }
        """.formatted(classesHandling);
    }
}
