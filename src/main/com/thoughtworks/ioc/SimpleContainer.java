package com.thoughtworks.ioc;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SimpleContainer {

    private Map<Class, Class> serviceMap = new HashMap<Class, Class>();
    private String packagePath;

    public SimpleContainer(String packagePath) {
        this.packagePath = packagePath;
    }

    public <T> T getComponent(Class<T> clazz) {
        T instance = null;
        try {
            instance = getInstance(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }

    private <T> T getInstance(Class<T> clazz) throws Exception{
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            Type[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes.length > 0) {
                ArrayList<Object> args = new ArrayList<Object>(parameterTypes.length);
                for (Type typeVariable : parameterTypes) {
                    Class serviceImpl = serviceMap.get(typeVariable);
                    args.add(serviceImpl.newInstance());
                }
                return (T) constructor.newInstance(args.toArray());
            }
        }
        return clazz.newInstance();
    }

    private Object findImplement(Class serviceImpl) throws InstantiationException, IllegalAccessException, IOException {
        Class implementationClass = null;
        ClassPath classPath = ClassPath.from(ClassLoader.getSystemClassLoader());
        ImmutableSet<ClassPath.ClassInfo> classInfos = classPath.getTopLevelClassesRecursive(packagePath);

        for (ClassPath.ClassInfo classInfo : classInfos) {
            Class<?> clazz = classInfo.load();
            if (clazz != serviceImpl && serviceImpl.isAssignableFrom(clazz)) {
                //TODO throw exception if there exist two different implementations
                serviceMap.put(serviceImpl, clazz);
                implementationClass = clazz;
            }
        }
        return implementationClass;
    }

    public void addComponent(Class interfaceClazz, Class implementClazz) {
        serviceMap.put(interfaceClazz, implementClazz);
    }
}
