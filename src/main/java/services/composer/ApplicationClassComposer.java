package services.composer;

import services.composer.entity.*;
import util.StringUtil;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ApplicationClassComposer {
    public static BaseClass composeClass(List<ExampleClass> exampleClasses) {
        var imports = List.of("example.*", "util.PropertiesUtil", "exception.FileException", "javax.persistence.*");
        var classPackage = "";
        var modifiers = List.of(Modifier.PUBLIC);
        var className = "Application";
        var attributes = new ArrayList<Attribute>();
        var method = composeMainMethod(exampleClasses);
        var methods = List.of(method);
        var classesToExtend = new ArrayList<String>();
        var classesToImplement = new ArrayList<String>();

        var applicationClass = new BaseClass(imports, classPackage, modifiers, className, attributes, methods, classesToExtend, classesToImplement);
        return applicationClass;
    }

    private static Method composeMainMethod(List<ExampleClass> exampleClasses) {
        var modifiers = List.of(Modifier.PUBLIC, Modifier.STATIC);
        var thrownExceptions = new ArrayList<String>();
        var returnType = "void";
        var name = "main";
        var parameter = new Parameter("args", "String[]");
        var parameters = List.of(parameter);
        var body = composeMainMethodBody(exampleClasses);
        var method = new Method(modifiers, thrownExceptions, returnType, name, parameters, body);
        return method;
    }

    private static String composeMainMethodBody(List<ExampleClass> exampleClasses) {
        var stringBuilder = new StringBuilder();

        for (var exampleClass : exampleClasses) {
            var exampleClassName = exampleClass.getName();
            var variableName = StringUtil.decapitalize(exampleClassName);
            var classInstantiationString = String.format("var %s = new %s(entityManager);\n", variableName, exampleClassName);
            var methodExecutionString = String.format("%s.run();\n", variableName);

            stringBuilder.append(classInstantiationString);
            stringBuilder.append(methodExecutionString);
        }

        var classesHandling = stringBuilder.toString();

        var body = """
                EntityManagerFactory entityManagerFactory = null;
                EntityManager entityManager = null;
                        
                try {
                    var properties = PropertiesUtil.getProperties();
                    var persistenceUnitName = properties.getProperty("persistence.unit.name");
                    entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
                    entityManager = entityManagerFactory.createEntityManager();
                    
                    %s
                } catch (IllegalStateException illegalStateException) {
                    var message = "\\nCouldn't create entity manager because the entity manager factory has already been closed.\\n";
                    System.err.println(message);
                } catch (FileException fileException) {
                    var message = fileException.getMessage();
                    System.err.println(message);
                } finally {
                    if (entityManager != null) {
                        try {
                            entityManager.close();
                        } catch (IllegalStateException illegalStateException) {
                            var message = "\\nCouldn't close entity manager because it is container-managed.\\n";
                            System.err.println(message);
                        }
                   }
             
                   if (entityManagerFactory != null) {
                        try {
                            entityManagerFactory.close();
                        } catch (IllegalStateException illegalStateException) {
                            var message = "\\nCouldn't close entity manager factory because it has already been closed.\\n";
                            System.err.println(message);
                        }
                   }
                }
                """.formatted(classesHandling);

        return body;
    }
}
