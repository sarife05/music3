package org.example.musiclibrary.utils;

import java.lang.reflect.*;
import java.util.Arrays;

/**
 * Utility class demonstrating Reflection and RTTI (Run-Time Type Information).
 * Provides methods to inspect classes at runtime.
 */
public class ReflectionUtils {

    /**
     * Display comprehensive information about a class
     */
    public static void inspectClass(Object obj) {
        if (obj == null) {
            System.out.println("Cannot inspect null object");
            return;
        }

        Class<?> clazz = obj.getClass();

        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║           REFLECTION INSPECTION REPORT               ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("  Class Name     : " + clazz.getName());
        System.out.println("  Simple Name    : " + clazz.getSimpleName());
        System.out.println("  Package        : " + clazz.getPackage().getName());
        System.out.println("  Superclass     : " + (clazz.getSuperclass() != null ? clazz.getSuperclass().getSimpleName() : "None"));
        System.out.println("  Is Abstract    : " + Modifier.isAbstract(clazz.getModifiers()));
        System.out.println("  Is Interface   : " + clazz.isInterface());
        System.out.println("╚══════════════════════════════════════════════════════╝");

        // Display interfaces
        displayInterfaces(clazz);

        // Display fields
        displayFields(clazz);

        // Display methods
        displayMethods(clazz);

        // Display constructors
        displayConstructors(clazz);
    }

    /**
     * Display all interfaces implemented by the class
     */
    private static void displayInterfaces(Class<?> clazz) {
        Class<?>[] interfaces = clazz.getInterfaces();
        System.out.println("\n▼ IMPLEMENTED INTERFACES (" + interfaces.length + "):");
        if (interfaces.length == 0) {
            System.out.println("  (none)");
        } else {
            for (Class<?> iface : interfaces) {
                System.out.println("  • " + iface.getSimpleName());
            }
        }
    }

    /**
     * Display all fields (variables) of the class
     */
    private static void displayFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        System.out.println("\n▼ DECLARED FIELDS (" + fields.length + "):");
        if (fields.length == 0) {
            System.out.println("  (none)");
        } else {
            for (Field field : fields) {
                String modifier = Modifier.toString(field.getModifiers());
                String type = field.getType().getSimpleName();
                String name = field.getName();
                System.out.printf("  • %s %s %s%n", modifier, type, name);
            }
        }
    }

    /**
     * Display all methods of the class
     */
    private static void displayMethods(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        System.out.println("\n▼ DECLARED METHODS (" + methods.length + "):");
        if (methods.length == 0) {
            System.out.println("  (none)");
        } else {
            for (Method method : methods) {
                String modifier = Modifier.toString(method.getModifiers());
                String returnType = method.getReturnType().getSimpleName();
                String name = method.getName();
                String params = Arrays.toString(method.getParameterTypes())
                        .replaceAll("class ", "")
                        .replaceAll("\\[", "(")
                        .replaceAll("\\]", ")");
                System.out.printf("  • %s %s %s%s%n", modifier, returnType, name, params);
            }
        }
    }

    /**
     * Display all constructors of the class
     */
    private static void displayConstructors(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        System.out.println("\n▼ CONSTRUCTORS (" + constructors.length + "):");
        for (Constructor<?> constructor : constructors) {
            String modifier = Modifier.toString(constructor.getModifiers());
            Parameter[] params = constructor.getParameters();
            StringBuilder paramStr = new StringBuilder("(");
            for (int i = 0; i < params.length; i++) {
                paramStr.append(params[i].getType().getSimpleName())
                        .append(" ")
                        .append(params[i].getName());
                if (i < params.length - 1) {
                    paramStr.append(", ");
                }
            }
            paramStr.append(")");
            System.out.printf("  • %s %s%s%n", modifier, clazz.getSimpleName(), paramStr);
        }
        System.out.println();
    }

    /**
     * Get field value using reflection
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Error accessing field: " + e.getMessage());
            return null;
        }
    }

    /**
     * Check if class has a specific method
     */
    public static boolean hasMethod(Class<?> clazz, String methodName) {
        try {
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Invoke a method by name using reflection
     */
    public static Object invokeMethod(Object obj, String methodName, Object... args) {
        try {
            Class<?>[] paramTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                paramTypes[i] = args[i].getClass();
            }
            Method method = obj.getClass().getMethod(methodName, paramTypes);
            return method.invoke(obj, args);
        } catch (Exception e) {
            System.err.println("Error invoking method: " + e.getMessage());
            return null;
        }
    }
}