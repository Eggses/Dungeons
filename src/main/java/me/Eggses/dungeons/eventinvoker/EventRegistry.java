package me.Eggses.dungeons.eventinvoker;

import org.bukkit.event.Event;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class EventRegistry {

    private static final MethodType METHOD_TYPE = MethodType.methodType(void.class, Event.class, EventContext.class);

    private final Map<Class<? extends Event>, List<MethodHandle>> eventMethodHandles = new HashMap<>();
    private final Logger logger;

    public EventRegistry(Logger logger) {
        this.logger = logger;
    }

    public void registerInvoker(Invoker invoker) {

        Class<?> invokerClass = invoker.getClass();

        Method[] methods = invokerClass.getDeclaredMethods();

        boolean foundAnnotatedPublicMethod = false;

        for (Method method : methods) {
            if (!method.isAnnotationPresent(EventInvoker.class)) continue;
            if (!Modifier.isPublic(method.getModifiers())) continue;

            foundAnnotatedPublicMethod = true;

            Class<?>[] parameters = method.getParameterTypes();

            String errorMessage = "Class: " + invokerClass.getName() + " Method: " + method.getName()
                    + " does not exactly match the required parameters of (Return: void) (SubType of Event) (EventContext)";

            if (parameters.length != 2) {
                logger.warning(errorMessage);
                continue;
            }
            if (method.getReturnType() != void.class) {
                logger.warning(errorMessage);
                continue;
            }
            if (!Event.class.isAssignableFrom(parameters[0])) {
                logger.warning(errorMessage);
                continue;
            }
            if (parameters[1] != EventContext.class) {
                logger.warning(errorMessage);
                continue;
            }

            @SuppressWarnings("unchecked")
            Class<? extends Event> eventType = (Class<? extends Event>) parameters[0];

            try {
                MethodHandle mh = MethodHandles.lookup().unreflect(method);
                mh = mh.bindTo(invoker);
                mh = mh.asType(METHOD_TYPE);

                List<MethodHandle> methodHandlesFor = eventMethodHandles.computeIfAbsent(eventType, k -> new ArrayList<>());
                methodHandlesFor.add(mh);

            } catch (IllegalAccessException ignored) {
                logger.warning("Could not access method " + method.getName() + " in " + invokerClass.getSimpleName() + ".");
            }
        }

        if (!foundAnnotatedPublicMethod) {
            logger.warning("No public methods with the "
                    + EventInvoker.class.getName() + " annotation in " + invokerClass.getName() + ".");
        }
    }

    public void registerInvoker(List<Invoker> invokers) {
        invokers.forEach(this::registerInvoker);
    }

    public void handleEvent(Event event, EventContext eventContext) {
        List<MethodHandle> methodHandlesFor = eventMethodHandles.get(event.getClass());
        if (methodHandlesFor == null) return;

        for (MethodHandle methodHandle : methodHandlesFor) {
            try {
                methodHandle.invokeExact(event, eventContext);
            } catch (Throwable throwable) {
                logger.warning("Error calling a MethodHandle");
            }
        }
    }
}
