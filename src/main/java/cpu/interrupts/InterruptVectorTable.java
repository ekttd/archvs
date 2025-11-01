package cpu.interrupts;

import cpu.interrupts.exceptions.InterruptException;
import cpu.interrupts.handlers.InterruptHandler;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public class InterruptVectorTable {
    private Map<Integer, InterruptHandler> table;

    public InterruptVectorTable() {
        Map<Integer, InterruptHandler> table = new HashMap<>();
        try {
            var reflections = new Reflections("cpu.interrupts");
            var subclasses = reflections.getSubTypesOf(InterruptHandler.class);

            for (var interruptHandlerClass : subclasses) {
                try {
                    // Создаем экземпляр обработчика
                    var interruptHandler = interruptHandlerClass.getConstructor().newInstance();

                    // Получаем generic параметр через getGenericSuperclass()
                    var genericSuperclass = interruptHandlerClass.getGenericSuperclass();

                    if (genericSuperclass instanceof ParameterizedType) {
                        ParameterizedType paramType = (ParameterizedType) genericSuperclass;
                        var typeArgs = paramType.getActualTypeArguments();

                        if (typeArgs.length > 0 && typeArgs[0] instanceof Class) {
                            @SuppressWarnings("unchecked")
                            Class<? extends InterruptException> interruptException =
                                    (Class<? extends InterruptException>) typeArgs[0];

                            // Создаем экземпляр исключения для получения вектора
                            var exceptionInstance = interruptException.getConstructor().newInstance();
                            int vector = exceptionInstance.getVector();

                            table.put(vector, interruptHandler);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Failed to initialize interrupt handler: " + interruptHandlerClass.getSimpleName());
                    e.printStackTrace();
                }
            }

            this.table = Collections.unmodifiableMap(table);
        } catch (Exception e) {
            System.err.println("Failed to initialize interrupt vector table");
            e.printStackTrace();
            this.table = Collections.unmodifiableMap(new HashMap<>()); // fallback
        }
    }

    public InterruptHandler getInterruptHandler(int vector) {
        return table.get(vector);
    }
}