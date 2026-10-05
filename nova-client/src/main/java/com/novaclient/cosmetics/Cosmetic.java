package com.novaclient.cosmetics;

import net.minecraft.client.entity.AbstractClientPlayer;

/**
 * Classe base para todos os cosméticos
 * Contém propriedades e métodos comuns a todos os cosméticos
 */
public abstract class Cosmetic {
    
    // Informações do cosmético
    private final String id;
    private final String name;
    private final String description;
    private final CosmeticType type;
    private final CosmeticRarity rarity;
    
    // Estado do cosmético
    private boolean unlocked;
    private boolean equipped;
    
    // Modelos e texturas
    private String modelPath;
    private String texturePath;
    
    // Dados do usuário
    private String ownerUUID;
    
    /**
     * Construtor do cosmético
     * 
     * @param id ID único do cosmético
     * @param name Nome do cosmético
     * @param description Descrição do cosmético
     * @param type Tipo do cosmético
     * @param rarity Raridade do cosmético
     */
    public Cosmetic(String id, String name, String description, CosmeticType type, CosmeticRarity rarity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.rarity = rarity;
        this.unlocked = false;
        this.equipped = false;
    }
    
    /**
     * Método chamado a cada tick do jogo
     */
    public void onTick() {
        // Implementado pelas subclasses se necessário
    }
    
    /**
     * Método chamado a cada frame de renderização
     * 
     * @param player Jogador que está usando o cosmético
     * @param partialTicks Ticks parciais para interpolação
     */
    public abstract void onRender(AbstractClientPlayer player, float partialTicks);
    
    /**
     * Método chamado para renderizar o cosmético na GUI
     * 
     * @param x Posição X na tela
     * @param y Posição Y na tela
     * @param scale Escala de renderização
     */
    public abstract void onRenderGUI(int x, int y, float scale);
    
    /**
     * Verifica se o cosmético pode ser equipado com outro cosmético
     * 
     * @param other Outro cosmético
     * @return true se pode ser equipado juntos, false caso contrário
     */
    public boolean canEquipWith(Cosmetic other) {
        // Por padrão, cosméticos do mesmo tipo não podem ser equipados juntos
        return this.type != other.type;
    }
    
    /**
     * Obtém a cor da raridade do cosmético
     */
    public int getRarityColor() {
        switch (rarity) {
            case COMMON:
                return 0xFFFFFFFF; // Branco
            case RARE:
                return 0xFF00FFFF; // Azul
            case EPIC:
                return 0xFF8000FF; // Roxo
            case LEGENDARY:
                return 0xFFFFFF00; // Amarelo/Gold
            default:
                return 0xFFFFFFFF;
        }
    }
    
    /**
     * Obtém o nome da raridade formatado
     */
    public String getRarityDisplayName() {
        switch (rarity) {
            case COMMON:
                return "§fCommon";
            case RARE:
                return "§9Rare";
            case EPIC:
                return "§5Epic";
            case LEGENDARY:
                return "§6Legendary";
            default:
                return "§fCommon";
        }
    }
    
    // Getters e Setters
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public CosmeticType getType() {
        return type;
    }
    
    public CosmeticRarity getRarity() {
        return rarity;
    }
    
    public boolean isUnlocked() {
        return unlocked;
    }
    
    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }
    
    public boolean isEquipped() {
        return equipped;
    }
    
    public void setEquipped(boolean equipped) {
        this.equipped = equipped;
    }
    
    public String getModelPath() {
        return modelPath;
    }
    
    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }
    
    public String getTexturePath() {
        return texturePath;
    }
    
    public void setTexturePath(String texturePath) {
        this.texturePath = texturePath;
    }
    
    public String getOwnerUUID() {
        return ownerUUID;
    }
    
    public void setOwnerUUID(String ownerUUID) {
        this.ownerUUID = ownerUUID;
    }
    
    /**
     * Tipos de cosméticos
     */
    public enum CosmeticType {
        CAPE("Cape"),
        DRAGON_WINGS("Dragon Wings"),
        ANGEL_WINGS("Angel Wings"),
        BANDANA("Bandana"),
        HAT("Hat"),
        GLASSES("Glasses"),
        CLOAK("Cloak"),
        PARTICLE_EFFECT("Particle Effect");
        
        private final String displayName;
        
        CosmeticType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Raridades dos cosméticos
     */
    public enum CosmeticRarity {
        COMMON,
        RARE,
        EPIC,
        LEGENDARY
    }
}
