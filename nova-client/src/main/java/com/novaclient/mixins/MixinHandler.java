package com.novaclient.mixins;

import com.novaclient.utils.ClientUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Gerenciador de Mixins do cliente
 * Responsável por registrar e gerenciar os mixins injetados
 */
public class MixinHandler {
    
    private static final List<String> registeredMixins = new ArrayList<>();
    
    /**
     * Registra os mixins de animações
     */
    public static void registerAnimationsMixin() {
        if (!registeredMixins.contains("animations")) {
            ClientUtils.logInfo("Registrando mixins de animações...");
            registeredMixins.add("animations");
            // Em implementação real, isso registraria os mixins
        }
    }
    
    /**
     * Desregistra os mixins de animações
     */
    public static void unregisterAnimationsMixin() {
        if (registeredMixins.contains("animations")) {
            ClientUtils.logInfo("Desregistrando mixins de animações...");
            registeredMixins.remove("animations");
            // Em implementação real, isso desregistraria os mixins
        }
    }
    
    /**
     * Registra os mixins de renderização
     */
    public static void registerRenderMixins() {
        if (!registeredMixins.contains("render")) {
            ClientUtils.logInfo("Registrando mixins de renderização...");
            registeredMixins.add("render");
        }
    }
    
    /**
     * Desregistra os mixins de renderização
     */
    public static void unregisterRenderMixins() {
        if (registeredMixins.contains("render")) {
            ClientUtils.logInfo("Desregistrando mixins de renderização...");
            registeredMixins.remove("render");
        }
    }
    
    /**
     * Registra os mixins de performance
     */
    public static void registerPerformanceMixins() {
        if (!registeredMixins.contains("performance")) {
            ClientUtils.logInfo("Registrando mixins de performance...");
            registeredMixins.add("performance");
        }
    }
    
    /**
     * Desregistra os mixins de performance
     */
    public static void unregisterPerformanceMixins() {
        if (registeredMixins.contains("performance")) {
            ClientUtils.logInfo("Desregistrando mixins de performance...");
            registeredMixins.remove("performance");
        }
    }
    
    /**
     * Registra todos os mixins
     */
    public static void registerAllMixins() {
        registerAnimationsMixin();
        registerRenderMixins();
        registerPerformanceMixins();
    }
    
    /**
     * Desregistra todos os mixins
     */
    public static void unregisterAllMixins() {
        unregisterAnimationsMixin();
        unregisterRenderMixins();
        unregisterPerformanceMixins();
    }
    
    /**
     * Verifica se um mixin está registrado
     */
    public static boolean isMixinRegistered(String mixinName) {
        return registeredMixins.contains(mixinName);
    }
    
    /**
     * Obtém a lista de mixins registrados
     */
    public static List<String> getRegisteredMixins() {
        return new ArrayList<>(registeredMixins);
    }
}
