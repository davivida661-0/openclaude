package com.novaclient.events;

/**
 * Classe base para todos os eventos do cliente
 */
public abstract class Event {
    
    private boolean cancelled;
    
    /**
     * Cancela o evento
     */
    public void cancel() {
        cancelled = true;
    }
    
    /**
     * Verifica se o evento foi cancelado
     */
    public boolean isCancelled() {
        return cancelled;
    }
    
    /**
     * Define se o evento está cancelado
     */
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
