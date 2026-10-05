package com.novaclient.cosmetics;

import com.novaclient.NovaClient;
import com.novaclient.api.CosmeticsAPI;
import com.novaclient.cosmetics.impl.AnimatedCape;
import com.novaclient.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;

import java.util.*;

/**
 * Gerenciador de cosméticos do cliente
 * Responsável por carregar, gerenciar e renderizar todos os cosméticos
 */
public class CosmeticManager {
    
    private final Minecraft mc = NovaClient.getInstance().getMc();
    private final CosmeticsAPI api = new CosmeticsAPI();
    
    // Lista de todos os cosméticos disponíveis
    private final List<Cosmetic> availableCosmetics = new ArrayList<>();
    
    // Lista de cosméticos do jogador atual
    private final List<Cosmetic> playerCosmetics = new ArrayList<>();
    
    // Mapa de cosméticos equipados por tipo
    private final Map<Cosmetic.CosmeticType, Cosmetic> equippedCosmetics = new HashMap<>();
    
    // Cosméticos de outros jogadores (visíveis apenas para quem tem o client)
    private final Map<UUID, List<Cosmetic>> otherPlayersCosmetics = new HashMap<>();
    
    // Estado
    private boolean initialized = false;
    private boolean cosmeticsEnabled = true;
    private boolean showOwnCosmetics = true;
    private boolean showOtherPlayersCosmetics = true;
    
    public CosmeticManager() {
        // Inicializa os cosméticos padrão
        initializeDefaultCosmetics();
    }
    
    /**
     * Inicializa os cosméticos padrão do cliente
     */
    private void initializeDefaultCosmetics() {
        // Adiciona cosméticos padrão
        availableCosmetics.add(new AnimatedCape(
            "default_cape", 
            "Default Cape", 
            "A cape animada padrão", 
            Cosmetic.CosmeticType.CAPE, 
            Cosmetic.CosmeticRarity.COMMON
        ));
        
        // Adiciona mais cosméticos de exemplo
        availableCosmetics.add(new AnimatedCape(
            "galaxy_cape", 
            "Galaxy Cape", 
            "Uma cape com animação de galáxia", 
            Cosmetic.CosmeticType.CAPE, 
            Cosmetic.CosmeticRarity.EPIC
        ));
        
        availableCosmetics.add(new AnimatedCape(
            "fire_cape", 
            "Fire Cape", 
            "Uma cape com animação de fogo", 
            Cosmetic.CosmeticType.CAPE, 
            Cosmetic.CosmeticRarity.LEGENDARY
        ));
    }
    
    /**
     * Carrega os cosméticos do jogador a partir da API
     */
    public void loadCosmetics() {
        if (initialized) return;
        
        ClientUtils.logInfo("Carregando cosméticos...");
        
        // Obtém o UUID do jogador
        if (mc.thePlayer != null) {
            String uuid = mc.thePlayer.getUniqueID().toString();
            
            // Carrega cosméticos da API (simulado por enquanto)
            // Em produção, isso seria uma chamada assíncrona para a API
            List<Cosmetic> apiCosmetics = api.fetchPlayerCosmetics(uuid);
            
            if (apiCosmetics != null) {
                playerCosmetics.addAll(apiCosmetics);
                ClientUtils.logInfo("Cosméticos carregados da API: " + apiCosmetics.size());
            }
        }
        
        // Adiciona cosméticos padrão que o jogador já tem
        for (Cosmetic cosmetic : availableCosmetics) {
            if (cosmetic.getId().equals("default_cape")) {
                cosmetic.setUnlocked(true);
                playerCosmetics.add(cosmetic);
            }
        }
        
        initialized = true;
        ClientUtils.logInfo("Cosméticos carregados com sucesso!");
    }
    
    /**
     * Atualiza os cosméticos do jogador
     */
    public void updateCosmetics() {
        if (mc.thePlayer == null) return;
        
        String uuid = mc.thePlayer.getUniqueID().toString();
        List<Cosmetic> updatedCosmetics = api.fetchPlayerCosmetics(uuid);
        
        if (updatedCosmetics != null) {
            playerCosmetics.clear();
            playerCosmetics.addAll(updatedCosmetics);
        }
    }
    
    /**
     * Método chamado a cada tick do jogo
     */
    public void onTick() {
        if (!cosmeticsEnabled) return;
        
        // Atualiza os cosméticos do jogador
        if (mc.theWorld != null && mc.theWorld.getTotalWorldTime() % 20 == 0) {
            // Atualiza a cada 20 ticks (1 segundo)
            updateCosmetics();
        }
        
        // Atualiza os cosméticos equipados
        for (Cosmetic cosmetic : equippedCosmetics.values()) {
            cosmetic.onTick();
        }
    }
    
    /**
     * Método chamado a cada frame de renderização
     */
    public void onRender() {
        if (!cosmeticsEnabled || mc.thePlayer == null) return;
        
        // Renderiza os cosméticos do jogador
        if (showOwnCosmetics) {
            renderPlayerCosmetics(mc.thePlayer);
        }
        
        // Renderiza os cosméticos de outros jogadores
        if (showOtherPlayersCosmetics) {
            for (Object entity : mc.theWorld.loadedEntityList) {
                if (entity instanceof AbstractClientPlayer) {
                    AbstractClientPlayer player = (AbstractClientPlayer) entity;
                    if (player != mc.thePlayer) {
                        renderOtherPlayerCosmetics(player);
                    }
                }
            }
        }
    }
    
    /**
     * Renderiza os cosméticos do jogador
     */
    private void renderPlayerCosmetics(AbstractClientPlayer player) {
        for (Cosmetic cosmetic : equippedCosmetics.values()) {
            if (cosmetic.isEquipped() && cosmetic.isUnlocked()) {
                cosmetic.onRender(player, mc.timer.renderPartialTicks);
            }
        }
    }
    
    /**
     * Renderiza os cosméticos de outros jogadores
     */
    private void renderOtherPlayerCosmetics(AbstractClientPlayer player) {
        UUID playerUUID = player.getUniqueID();
        List<Cosmetic> cosmetics = otherPlayersCosmetics.get(playerUUID);
        
        if (cosmetics != null) {
            for (Cosmetic cosmetic : cosmetics) {
                if (cosmetic.isEquipped()) {
                    cosmetic.onRender(player, mc.timer.renderPartialTicks);
                }
            }
        }
    }
    
    /**
     * Equipa um cosmético
     */
    public void equipCosmetic(Cosmetic cosmetic) {
        if (cosmetic == null || !cosmetic.isUnlocked()) return;
        
        // Verifica se pode equipar com outros cosméticos já equipados
        for (Cosmetic equipped : equippedCosmetics.values()) {
            if (!cosmetic.canEquipWith(equipped)) {
                // Deséquipa o cosmético conflitante
                unequipCosmetic(equipped.getType());
            }
        }
        
        // Equipa o cosmético
        cosmetic.setEquipped(true);
        equippedCosmetics.put(cosmetic.getType(), cosmetic);
        
        // Salva na API
        if (mc.thePlayer != null) {
            api.equipCosmetic(mc.thePlayer.getUniqueID().toString(), cosmetic.getId());
        }
        
        ClientUtils.logInfo("Cosmético equipado: " + cosmetic.getName());
    }
    
    /**
     * Deséquipa um cosmético pelo tipo
     */
    public void unequipCosmetic(Cosmetic.CosmeticType type) {
        Cosmetic cosmetic = equippedCosmetics.get(type);
        if (cosmetic != null) {
            cosmetic.setEquipped(false);
            equippedCosmetics.remove(type);
            
            // Salva na API
            if (mc.thePlayer != null) {
                api.unequipCosmetic(mc.thePlayer.getUniqueID().toString(), type.name());
            }
            
            ClientUtils.logInfo("Cosmético desequipado: " + cosmetic.getName());
        }
    }
    
    /**
     * Deséquipa todos os cosméticos
     */
    public void unequipAllCosmetics() {
        for (Cosmetic.CosmeticType type : Cosmetic.CosmeticType.values()) {
            unequipCosmetic(type);
        }
    }
    
    /**
     * Adiciona um cosmético ao jogador
     */
    public void addCosmetic(Cosmetic cosmetic) {
        if (cosmetic == null) return;
        
        if (!playerCosmetics.contains(cosmetic)) {
            playerCosmetics.add(cosmetic);
            cosmetic.setUnlocked(true);
            
            // Salva na API
            if (mc.thePlayer != null) {
                api.addCosmetic(mc.thePlayer.getUniqueID().toString(), cosmetic.getId());
            }
        }
    }
    
    /**
     * Remove um cosmético do jogador
     */
    public void removeCosmetic(String cosmeticId) {
        playerCosmetics.removeIf(cosmetic -> cosmetic.getId().equals(cosmeticId));
    }
    
    /**
     * Obtém todos os cosméticos disponíveis
     */
    public List<Cosmetic> getAvailableCosmetics() {
        return new ArrayList<>(availableCosmetics);
    }
    
    /**
     * Obtém todos os cosméticos do jogador
     */
    public List<Cosmetic> getPlayerCosmetics() {
        return new ArrayList<>(playerCosmetics);
    }
    
    /**
     * Obtém os cosméticos equipados
     */
    public Map<Cosmetic.CosmeticType, Cosmetic> getEquippedCosmetics() {
        return new HashMap<>(equippedCosmetics);
    }
    
    /**
     * Obtém os cosméticos de um tipo específico
     */
    public List<Cosmetic> getCosmeticsByType(Cosmetic.CosmeticType type) {
        List<Cosmetic> result = new ArrayList<>();
        for (Cosmetic cosmetic : playerCosmetics) {
            if (cosmetic.getType() == type) {
                result.add(cosmetic);
            }
        }
        return result;
    }
    
    /**
     * Obtém o cosmético equipado de um tipo específico
     */
    public Cosmetic getEquippedCosmetic(Cosmetic.CosmeticType type) {
        return equippedCosmetics.get(type);
    }
    
    /**
     * Verifica se o jogador tem um cosmético específico
     */
    public boolean hasCosmetic(String cosmeticId) {
        return playerCosmetics.stream().anyMatch(c -> c.getId().equals(cosmeticId));
    }
    
    /**
     * Verifica se o cosmético está equipado
     */
    public boolean isCosmeticEquipped(String cosmeticId) {
        return equippedCosmetics.values().stream().anyMatch(c -> c.getId().equals(cosmeticId));
    }
    
    /**
     * Atualiza os cosméticos de outros jogadores
     */
    public void updateOtherPlayerCosmetics(UUID playerUUID, List<Cosmetic> cosmetics) {
        otherPlayersCosmetics.put(playerUUID, cosmetics);
    }
    
    /**
     * Remove os cosméticos de um jogador
     */
    public void removeOtherPlayerCosmetics(UUID playerUUID) {
        otherPlayersCosmetics.remove(playerUUID);
    }
    
    // Getters e Setters
    public boolean isCosmeticsEnabled() {
        return cosmeticsEnabled;
    }
    
    public void setCosmeticsEnabled(boolean cosmeticsEnabled) {
        this.cosmeticsEnabled = cosmeticsEnabled;
    }
    
    public boolean isShowOwnCosmetics() {
        return showOwnCosmetics;
    }
    
    public void setShowOwnCosmetics(boolean showOwnCosmetics) {
        this.showOwnCosmetics = showOwnCosmetics;
    }
    
    public boolean isShowOtherPlayersCosmetics() {
        return showOtherPlayersCosmetics;
    }
    
    public void setShowOtherPlayersCosmetics(boolean showOtherPlayersCosmetics) {
        this.showOtherPlayersCosmetics = showOtherPlayersCosmetics;
    }
    
    /**
     * Desliga o gerenciador de cosméticos
     */
    public void shutdown() {
        unequipAllCosmetics();
        playerCosmetics.clear();
        availableCosmetics.clear();
        otherPlayersCosmetics.clear();
        initialized = false;
    }
}
