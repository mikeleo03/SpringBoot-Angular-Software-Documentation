package com.example.fpt_midterm_pos.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExcelGeneratorTest {

    @Test
    void testPrivateConstructor() {
        Constructor<ExcelGenerator> constructor = getPrivateConstructor();
        IllegalStateException thrownException = assertThrows(IllegalStateException.class, () -> invokeConstructor(constructor));
        assertEquals("Utility class", thrownException.getMessage());
    }

    private Constructor<ExcelGenerator> getPrivateConstructor() {
        try {
            Constructor<ExcelGenerator> constructor = ExcelGenerator.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor;
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException("Reflection exception occurred", e);
        }
    }

    private void invokeConstructor(Constructor<ExcelGenerator> constructor) throws Throwable {
        try {
            constructor.newInstance();
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
