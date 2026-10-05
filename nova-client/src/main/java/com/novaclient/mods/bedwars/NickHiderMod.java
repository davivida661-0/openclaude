package com.novaclient.mods.bedwars;

import com.novaclient.mods.Mod;
import com.novaclient.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Mod Nick Hider / Streamer Mode
 * Esconde os nicknames dos jogadores para proteção de privacidade
 */
public class NickHiderMod extends Mod {
    
    private static final Minecraft mc = Minecraft.getMinecraft();
    
    // Configurações
    private boolean hideAllNicks = true;
    private boolean hideOwnNick = false;
    private boolean showTeamColors = true;
    private boolean hideInTab = true;
    private boolean hideInChat = true;
    private boolean hideInScoreboard = true;
    
    // Nicks personalizados
    private String ownNickReplacement = "Player";
    private String otherNickReplacement = "Player";
    private Map<UUID, String> customNicks = new HashMap<>();
    
    // Nicks originais (para restaurar)
    private Map<UUID, String> originalNicks = new HashMap<>();
    
    public NickHiderMod() {
        super("Nick Hider", "Esconde os nicknames dos jogadores (Streamer Mode)", ModCategory.BEDWARS);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        ClientUtils.logInfo("Nick Hider habilitado - Streamer Mode ATIVO");
        
        // Salva os nicks originais
        saveOriginalNicks();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtils.logInfo("Nick Hider desabilitado - Streamer Mode DESATIVO");
        
        // Restaura os nicks originais
        restoreOriginalNicks();
    }
    
    /**
     * Salva os nicks originais dos jogadores
     */
    private void saveOriginalNicks() {
        if (mc.theWorld == null) return;
        
        for (Object entity : mc.theWorld.playerEntities) {
            if (entity instanceof AbstractClientPlayer) {
                AbstractClientPlayer player = (AbstractClientPlayer) entity;
                originalNicks.put(player.getUniqueID(), player.getName());
            }
        }
    }
    
    /**
     * Restaura os nicks originais
     */
    private void restoreOriginalNicks() {
        originalNicks.clear();
    }
    
    /**
     * Obtém o nickname a ser exibido para um jogador
     */
    public String getDisplayName(EntityPlayer player) {
        if (!isEnabled()) {
            return player.getName();
        }
        
        UUID playerUUID = player.getUniqueID();
        
        // Verifica se tem um nick personalizado
        if (customNicks.containsKey(playerUUID)) {
            return customNicks.get(playerUUID);
        }
        
        // Verifica se é o próprio jogador
        if (player == mc.thePlayer && hideOwnNick) {
            return formatNick(ownNickReplacement, player);
        }
        
        // Verifica se deve esconder todos os nicks
        if (hideAllNicks) {
            return formatNick(otherNickReplacement, player);
        }
        
        return player.getName();
    }
    
    /**
     * Formata o nickname com cores de time
     */
    private String formatNick(String baseNick, EntityPlayer player) {
        if (!showTeamColors) {
            return baseNick;
        }
        
        // Obtém a cor do time
        String teamColor = getTeamColor(player);
        return teamColor + baseNick;
    }
    
    /**
     * Obtém a cor do time do jogador
     */
    private String getTeamColor(EntityPlayer player) {
        if (mc.theWorld == null || mc.thePlayer == null) {
            return "§f"; // Branco
        }
        
        // Verifica o time do jogador
        if (mc.theWorld.getScoreboard() != null) {
            for (Object teamObj : mc.theWorld.getScoreboard().getTeams()) {
                if (teamObj instanceof net.minecraft.scoreboard.Team) {
                    net.minecraft.scoreboard.Team team = (net.minecraft.scoreboard.Team) teamObj;
                    if (team.isSameTeam(player.getName())) {
                        return team.getColor().toString();
                    }
                }
            }
        }
        
        return "§7"; // Cinza
    }
    
    /**
     * Define um nick personalizado para um jogador
     */
    public void setCustomNick(UUID playerUUID, String customNick) {
        customNicks.put(playerUUID, customNick);
        ClientUtils.logInfo("Nick personalizado definido para: " + customNick);
    }
    
    /**
     * Remove um nick personalizado
     */
    public void removeCustomNick(UUID playerUUID) {
        customNicks.remove(playerUUID);
        ClientUtils.logInfo("Nick personalizado removido");
    }
    
    /**
     * Adiciona o próprio jogador à lista de nicks escondidos
     */
    public void hideOwnNick() {
        hideOwnNick = true;
    }
    
    /**
     * Remove o próprio jogador da lista de nicks escondidos
     */
    public void showOwnNick() {
        hideOwnNick = false;
    }
    
    /**
     * Alternar o modo Streamer
     */
    public void toggleStreamerMode() {
        setEnabled(!isEnabled());
    }
    
    // Getters e Setters
    public boolean isHideAllNicks() {
        return hideAllNicks;
    }
    
    public void setHideAllNicks(boolean hideAllNicks) {
        this.hideAllNicks = hideAllNicks;
    }
    
    public boolean isHideOwnNick() {
        return hideOwnNick;
    }
    
    public void setHideOwnNick(boolean hideOwnNick) {
        this.hideOwnNick = hideOwnNick;
    }
    
    public boolean isShowTeamColors() {
        return showTeamColors;
    }
    
    public void setShowTeamColors(boolean showTeamColors) {
        this.showTeamColors = showTeamColors;
    }
    
    public boolean isHideInTab() {
        return hideInTab;
    }
    
    public void setHideInTab(boolean hideInTab) {
        this.hideInTab = hideInTab;
    }
    
    public boolean isHideInChat() {
        return hideInChat;
    }
    
    public void setHideInChat(boolean hideInChat) {
        this.hideInChat = hideInChat;
    }
    
    public boolean isHideInScoreboard() {
        return hideInScoreboard;
    }
    
    public void setHideInScoreboard(boolean hideInScoreboard) {
        this.hideInScoreboard = hideInScoreboard;
    }
    
    public String getOwnNickReplacement() {
        return ownNickReplacement;
    }
    
    public void setOwnNickReplacement(String ownNickReplacement) {
        this.ownNickReplacement = ownNickReplacement;
    }
    
    public String getOtherNickReplacement() {
        return otherNickReplacement;
    }
    
    public void setOtherNickReplacement(String otherNickReplacement) {
        this.otherNickReplacement = otherNickReplacement;
    }
    
    public Map<UUID, String> getCustomNicks() {
        return new HashMap<>(customNicks);
    }
}
