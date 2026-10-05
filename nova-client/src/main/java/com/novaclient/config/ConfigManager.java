package com.novaclient.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.novaclient.NovaClient;
import com.novaclient.gui.hud.HUDManager;
import com.novaclient.mods.Mod;
import com.novaclient.utils.ClientUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Gerenciador de configurações do cliente
 * Responsável por salvar e carregar todas as configurações em arquivos JSON
 */
public class ConfigManager {
    
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final NovaClient client = NovaClient.getInstance();
    
    // Caminho da pasta de configurações
    private final Path configDir;
    
    // Arquivos de configuração
    private static final String MODS_CONFIG_FILE = "mods.json";
    private static final String HUD_CONFIG_FILE = "hud.json";
    private static final String COSMETICS_CONFIG_FILE = "cosmetics.json";
    private static final String SETTINGS_CONFIG_FILE = "settings.json";
    
    public ConfigManager() {
        // Cria a pasta de configurações
        String mcDir = System.getProperty("user.home") + File.separator + ".nova-client";
        configDir = Paths.get(mcDir);
        
        try {
            if (!Files.exists(configDir)) {
                Files.createDirectories(configDir);
                ClientUtils.logInfo("Pasta de configurações criada: " + configDir);
            }
        } catch (IOException e) {
            ClientUtils.logError("Erro ao criar pasta de configurações: " + e.getMessage());
        }
    }
    
    /**
     * Carrega todas as configurações
     */
    public void loadConfigs() {
        ClientUtils.logInfo("Carregando configurações...");
        
        try {
            loadModsConfig();
            loadHUDConfig();
            loadCosmeticsConfig();
            loadSettingsConfig();
            
            ClientUtils.logInfo("Configurações carregadas com sucesso!");
        } catch (Exception e) {
            ClientUtils.logError("Erro ao carregar configurações: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Salva todas as configurações
     */
    public void saveConfigs() {
        ClientUtils.logInfo("Salvando configurações...");
        
        try {
            saveModsConfig();
            saveHUDConfig();
            saveCosmeticsConfig();
            saveSettingsConfig();
            
            ClientUtils.logInfo("Configurações salvas com sucesso!");
        } catch (Exception e) {
            ClientUtils.logError("Erro ao salvar configurações: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Carrega a configuração dos mods
     */
    private void loadModsConfig() {
        Path configPath = configDir.resolve(MODS_CONFIG_FILE);
        
        if (!Files.exists(configPath)) {
            ClientUtils.logInfo("Arquivo de configuração de mods não encontrado, criando novo...");
            return;
        }
        
        try (Reader reader = Files.newBufferedReader(configPath)) {
            JsonObject config = JsonParser.parseReader(reader).getAsJsonObject();
            
            if (config.has("mods")) {
                JsonObject modsConfig = config.getAsJsonObject("mods");
                
                for (Mod mod : client.getModManager().getMods()) {
                    String modName = mod.getName();
                    if (modsConfig.has(modName)) {
                        JsonObject modConfig = modsConfig.getAsJsonObject(modName);
                        
                        // Carrega o estado do mod
                        if (modConfig.has("enabled")) {
                            mod.setEnabled(modConfig.get("enabled").getAsBoolean());
                        }
                        
                        // Carrega a visibilidade
                        if (modConfig.has("visible")) {
                            mod.setVisible(modConfig.get("visible").getAsBoolean());
                        }
                        
                        // Carrega a posição
                        if (modConfig.has("x")) {
                            mod.setX(modConfig.get("x").getAsInt());
                        }
                        if (modConfig.has("y")) {
                            mod.setY(modConfig.get("y").getAsInt());
                        }
                        
                        // Carrega a escala
                        if (modConfig.has("scale")) {
                            mod.setScale(modConfig.get("scale").getAsFloat());
                        }
                        
                        // Carrega a tecla de atalho
                        if (modConfig.has("keyBind")) {
                            mod.setKeyBind(modConfig.get("keyBind").getAsInt());
                        }
                    }
                }
            }
            
        } catch (IOException e) {
            ClientUtils.logError("Erro ao carregar configuração de mods: " + e.getMessage());
        }
    }
    
    /**
     * Salva a configuração dos mods
     */
    private void saveModsConfig() {
        Path configPath = configDir.resolve(MODS_CONFIG_FILE);
        
        JsonObject config = new JsonObject();
        JsonObject modsConfig = new JsonObject();
        
        for (Mod mod : client.getModManager().getMods()) {
            JsonObject modConfig = new JsonObject();
            
            modConfig.addProperty("enabled", mod.isEnabled());
            modConfig.addProperty("visible", mod.isVisible());
            modConfig.addProperty("x", mod.getX());
            modConfig.addProperty("y", mod.getY());
            modConfig.addProperty("scale", mod.getScale());
            modConfig.addProperty("keyBind", mod.getKeyBind());
            
            modsConfig.add(mod.getName(), modConfig);
        }
        
        config.add("mods", modsConfig);
        
        try (Writer writer = Files.newBufferedWriter(configPath)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            ClientUtils.logError("Erro ao salvar configuração de mods: " + e.getMessage());
        }
    }
    
    /**
     * Carrega a configuração da HUD
     */
    private void loadHUDConfig() {
        Path configPath = configDir.resolve(HUD_CONFIG_FILE);
        
        if (!Files.exists(configPath)) {
            ClientUtils.logInfo("Arquivo de configuração da HUD não encontrado, criando novo...");
            return;
        }
        
        try (Reader reader = Files.newBufferedReader(configPath)) {
            JsonObject config = JsonParser.parseReader(reader).getAsJsonObject();
            HUDManager hudManager = client.getHudManager();
            
            if (config.has("elements")) {
                JsonObject elementsConfig = config.getAsJsonObject("elements");
                
                for (com.novaclient.gui.hud.HUDElement element : hudManager.getHUDElements()) {
                    String elementName = element.getName();
                    if (elementsConfig.has(elementName)) {
                        JsonObject elementConfig = elementsConfig.getAsJsonObject(elementName);
                        
                        if (elementConfig.has("x")) {
                            element.setX(elementConfig.get("x").getAsInt());
                        }
                        if (elementConfig.has("y")) {
                            element.setY(elementConfig.get("y").getAsInt());
                        }
                        if (elementConfig.has("width")) {
                            element.setWidth(elementConfig.get("width").getAsInt());
                        }
                        if (elementConfig.has("height")) {
                            element.setHeight(elementConfig.get("height").getAsInt());
                        }
                        if (elementConfig.has("enabled")) {
                            element.setEnabled(elementConfig.get("enabled").getAsBoolean());
                        }
                        if (elementConfig.has("visible")) {
                            element.setVisible(elementConfig.get("visible").getAsBoolean());
                        }
                        if (elementConfig.has("scale")) {
                            element.setScale(elementConfig.get("scale").getAsFloat());
                        }
                    }
                }
            }
            
        } catch (IOException e) {
            ClientUtils.logError("Erro ao carregar configuração da HUD: " + e.getMessage());
        }
    }
    
    /**
     * Salva a configuração da HUD
     */
    private void saveHUDConfig() {
        Path configPath = configDir.resolve(HUD_CONFIG_FILE);
        
        JsonObject config = new JsonObject();
        JsonObject elementsConfig = new JsonObject();
        HUDManager hudManager = client.getHudManager();
        
        for (com.novaclient.gui.hud.HUDElement element : hudManager.getHUDElements()) {
            JsonObject elementConfig = new JsonObject();
            
            elementConfig.addProperty("x", element.getX());
            elementConfig.addProperty("y", element.getY());
            elementConfig.addProperty("width", element.getWidth());
            elementConfig.addProperty("height", element.getHeight());
            elementConfig.addProperty("enabled", element.isEnabled());
            elementConfig.addProperty("visible", element.isVisible());
            elementConfig.addProperty("scale", element.getScale());
            
            elementsConfig.add(element.getName(), elementConfig);
        }
        
        config.add("elements", elementsConfig);
        
        try (Writer writer = Files.newBufferedWriter(configPath)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            ClientUtils.logError("Erro ao salvar configuração da HUD: " + e.getMessage());
        }
    }
    
    /**
     * Carrega a configuração dos cosméticos
     */
    private void loadCosmeticsConfig() {
        Path configPath = configDir.resolve(COSMETICS_CONFIG_FILE);
        
        if (!Files.exists(configPath)) {
            ClientUtils.logInfo("Arquivo de configuração de cosméticos não encontrado, criando novo...");
            return;
        }
        
        try (Reader reader = Files.newBufferedReader(configPath)) {
            JsonObject config = JsonParser.parseReader(reader).getAsJsonObject();
            
            // Implementação para carregar configurações de cosméticos
            // (Será implementado quando o sistema de cosméticos estiver completo)
            
        } catch (IOException e) {
            ClientUtils.logError("Erro ao carregar configuração de cosméticos: " + e.getMessage());
        }
    }
    
    /**
     * Salva a configuração dos cosméticos
     */
    private void saveCosmeticsConfig() {
        Path configPath = configDir.resolve(COSMETICS_CONFIG_FILE);
        
        JsonObject config = new JsonObject();
        
        // Implementação para salvar configurações de cosméticos
        // (Será implementado quando o sistema de cosméticos estiver completo)
        
        try (Writer writer = Files.newBufferedWriter(configPath)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            ClientUtils.logError("Erro ao salvar configuração de cosméticos: " + e.getMessage());
        }
    }
    
    /**
     * Carrega as configurações gerais
     */
    private void loadSettingsConfig() {
        Path configPath = configDir.resolve(SETTINGS_CONFIG_FILE);
        
        if (!Files.exists(configPath)) {
            ClientUtils.logInfo("Arquivo de configuração de settings não encontrado, criando novo...");
            return;
        }
        
        try (Reader reader = Files.newBufferedReader(configPath)) {
            JsonObject config = JsonParser.parseReader(reader).getAsJsonObject();
            
            // Carrega configurações gerais
            if (config.has("hudEnabled")) {
                client.getHudManager().setHudEnabled(config.get("hudEnabled").getAsBoolean());
            }
            
            if (config.has("cosmeticsEnabled")) {
                client.getCosmeticManager().setCosmeticsEnabled(config.get("cosmeticsEnabled").getAsBoolean());
            }
            
        } catch (IOException e) {
            ClientUtils.logError("Erro ao carregar configuração de settings: " + e.getMessage());
        }
    }
    
    /**
     * Salva as configurações gerais
     */
    private void saveSettingsConfig() {
        Path configPath = configDir.resolve(SETTINGS_CONFIG_FILE);
        
        JsonObject config = new JsonObject();
        
        // Salva configurações gerais
        config.addProperty("hudEnabled", client.getHudManager().isHudEnabled());
        config.addProperty("cosmeticsEnabled", client.getCosmeticManager().isCosmeticsEnabled());
        
        try (Writer writer = Files.newBufferedWriter(configPath)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            ClientUtils.logError("Erro ao salvar configuração de settings: " + e.getMessage());
        }
    }
    
    /**
     * Obtém o caminho da pasta de configurações
     */
    public Path getConfigDir() {
        return configDir;
    }
}
