import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class MyClassLoader extends ClassLoader {

    public static void main(String[] args) {
        MyClassLoader myClassLoader = new MyClassLoader();
        try {
            Class<?> aClass = Class.forName("Hello", true, myClassLoader);
            Constructor<?>[] constructors = aClass.getConstructors();
            if (constructors != null) {
                System.out.println("constructors:");
                for (Constructor<?> constructor : constructors) {
                    System.out.println(constructor);
                }
            }
            Method[] declaredMethods = aClass.getDeclaredMethods();
            if (declaredMethods != null) {
                System.out.println("methods:");
                for (Method method : declaredMethods) {
                    System.out.println(method);
                }
                System.out.println("invoke method:");
                Object instance = aClass.newInstance();
                Method hello = aClass.getDeclaredMethod("hello");
                hello.invoke(instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] data = loadClassData(name);
        if (data == null) {
            throw new ClassNotFoundException();
        }
        return defineClass(name, data, 0, data.length);
    }

    private byte[] loadClassData(String name) {
        InputStream inputStream = getResourceAsStream(name.replace('.', File.separatorChar) + ".xlass");
        if (inputStream != null) {
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                int b;
                while ((b = inputStream.read()) != -1) {
                    outputStream.write(255 - b);
                }
                return outputStream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
