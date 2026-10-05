package com.novaclient.events;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Barramento de eventos do cliente
 * Responsável por registrar e disparar eventos para os ouvintes
 */
public class EventBus {
    
    private final Map<Class<? extends Event>, List<EventListener>> listeners = new HashMap<>();
    
    /**
     * Registra um ouvinte de eventos
     * 
     * @param listener Ouvinte a ser registrado
     */
    public void register(Object listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventHandler.class)) {
                Class<?>[] params = method.getParameterTypes();
                
                if (params.length == 1 && Event.class.isAssignableFrom(params[0])) {
                    Class<? extends Event> eventClass = (Class<? extends Event>) params[0];
                    
                    if (!listeners.containsKey(eventClass)) {
                        listeners.put(eventClass, new ArrayList<>());
                    }
                    
                    listeners.get(eventClass).add(new EventListener(listener, method));
                }
            }
        }
    }
    
    /**
     * Desregistra um ouvinte de eventos
     * 
     * @param listener Ouvinte a ser desregistrado
     */
    public void unregister(Object listener) {
        for (List<EventListener> listenerList : listeners.values()) {
            listenerList.removeIf(l -> l.getListener().equals(listener));
        }
    }
    
    /**
     * Dispara um evento
     * 
     * @param event Evento a ser disparado
     */
    public void post(Event event) {
        List<EventListener> listenerList = listeners.get(event.getClass());
        
        if (listenerList != null) {
            for (EventListener listener : listenerList) {
                try {
                    listener.getMethod().invoke(listener.getListener(), event);
                } catch (Exception e) {
                    System.err.println("Erro ao disparar evento " + event.getClass().getSimpleName() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Limpa todos os ouvintes
     */
    public void clear() {
        listeners.clear();
    }
    
    /**
     * Classe interna para representar um ouvinte de eventos
     */
    private static class EventListener {
        private final Object listener;
        private final Method method;
        
        public EventListener(Object listener, Method method) {
            this.listener = listener;
            this.method = method;
        }
        
        public Object getListener() {
            return listener;
        }
        
        public Method getMethod() {
            return method;
        }
    }
}
