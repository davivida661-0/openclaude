package com.novaclient.mods.bedwars;

import com.novaclient.mods.Mod;
import com.novaclient.utils.ClientUtils;
import com.novaclient.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;

import java.util.HashMap;
import java.util.Map;

/**
 * Mod Session Counter - Contador de estatísticas da sessão de BedWars
 * Mostra Final Kills, Beds Broken, Wins, etc.
 */
public class SessionCounterMod extends Mod {
    
    private static final Minecraft mc = Minecraft.getMinecraft();
    
    // Estatísticas da sessão
    private int finalKills = 0;
    private int bedsBroken = 0;
    private int wins = 0;
    private int losses = 0;
    private int gamesPlayed = 0;
    private int finalKillsSession = 0;
    private int bedsBrokenSession = 0;
    
    // Configurações
    private boolean showFinalKills = true;
    private boolean showBedsBroken = true;
    private boolean showWins = true;
    private boolean showGamesPlayed = true;
    private boolean showStatsPerGame = true;
    private int bgColor = 0x80000000;
    private int textColor = 0xFFFFFFFF;
    
    // Dados por jogo
    private Map<String, SessionStats> gameStats = new HashMap<>();
    
    public SessionCounterMod() {
        super("Session Counter", "Contador de estatísticas de BedWars", ModCategory.BEDWARS);
        setX(10);
        setY(150);
    }
    
    @Override
    public void onTick() {
        if (!isEnabled() || mc.theWorld == null || mc.thePlayer == null) return;
        
        // Detecta eventos do jogo
        detectGameEvents();
    }
    
    @Override
    public void onRender() {
        if (!isEnabled()) return;
        
        ScaledResolution sr = new ScaledResolution(mc);
        
        int x = getX();
        int y = getY();
        int width = 180;
        
        // Desenha o background
        RenderUtils.drawRect(x - 5, y - 5, x + width + 5, y + 100, bgColor);
        RenderUtils.drawBorder(x - 5, y - 5, x + width + 5, y + 100, 1, 0xFF00FFFF);
        
        // Desenha o título
        mc.fontRendererObj.drawStringWithShadow("§6Session Stats", x, y, textColor);
        y += 12;
        
        // Desenha as estatísticas
        if (showFinalKills) {
            String fkText = EnumChatFormatting.GREEN + "Final Kills: " + EnumChatFormatting.WHITE + finalKillsSession;
            mc.fontRendererObj.drawStringWithShadow(fkText, x, y, textColor);
            y += 10;
        }
        
        if (showBedsBroken) {
            String bbText = EnumChatFormatting.GOLD + "Beds Broken: " + EnumChatFormatting.WHITE + bedsBrokenSession;
            mc.fontRendererObj.drawStringWithShadow(bbText, x, y, textColor);
            y += 10;
        }
        
        if (showWins) {
            String winsText = EnumChatFormatting.AQUA + "Wins: " + EnumChatFormatting.WHITE + wins;
            mc.fontRendererObj.drawStringWithShadow(winsText, x, y, textColor);
            y += 10;
        }
        
        if (showGamesPlayed) {
            String gamesText = EnumChatFormatting.GRAY + "Games: " + EnumChatFormatting.WHITE + gamesPlayed;
            mc.fontRendererObj.drawStringWithShadow(gamesText, x, y, textColor);
            y += 12;
        }
        
        // Desenha estatísticas por jogo
        if (showStatsPerGame && !gameStats.isEmpty()) {
            mc.fontRendererObj.drawStringWithShadow("§7Per Game:", x, y, textColor);
            y += 10;
            
            for (Map.Entry<String, SessionStats> entry : gameStats.entrySet()) {
                SessionStats stats = entry.getValue();
                String gameText = String.format("§8%s: §c%d FK §6%d BB", 
                    entry.getKey(), stats.finalKills, stats.bedsBroken);
                mc.fontRendererObj.drawStringWithShadow(gameText, x, y, textColor);
                y += 10;
            }
        }
    }
    
    /**
     * Detecta eventos do jogo (Final Kill, Bed Broken, Win)
     */
    private void detectGameEvents() {
        if (mc.ingameGUI == null) return;
        
        // Verifica mensagens no chat para detectar eventos
        // Em implementação real, isso usaria eventos de chat
        
        // Detecta Final Kill
        // Detecta Bed Broken
        // Detecta Win
        // Detecta Game Start/End
    }
    
    /**
     * Adiciona uma Final Kill
     */
    public void addFinalKill() {
        finalKills++;
        finalKillsSession++;
        
        // Adiciona ao jogo atual
        String currentGame = getCurrentGameId();
        if (!gameStats.containsKey(currentGame)) {
            gameStats.put(currentGame, new SessionStats());
        }
        gameStats.get(currentGame).finalKills++;
        
        ClientUtils.logInfo("Final Kill registrada! Total: " + finalKills);
    }
    
    /**
     * Adiciona uma Bed Broken
     */
    public void addBedBroken() {
        bedsBroken++;
        bedsBrokenSession++;
        
        // Adiciona ao jogo atual
        String currentGame = getCurrentGameId();
        if (!gameStats.containsKey(currentGame)) {
            gameStats.put(currentGame, new SessionStats());
        }
        gameStats.get(currentGame).bedsBroken++;
        
        ClientUtils.logInfo("Bed Broken registrada! Total: " + bedsBroken);
    }
    
    /**
     * Adiciona uma vitória
     */
    public void addWin() {
        wins++;
        gamesPlayed++;
        
        ClientUtils.logInfo("Vitória registrada! Total: " + wins);
    }
    
    /**
     * Adiciona uma derrota
     */
    public void addLoss() {
        losses++;
        gamesPlayed++;
        
        ClientUtils.logInfo("Derrota registrada! Total: " + losses);
    }
    
    /**
     * Reseta as estatísticas da sessão
     */
    public void resetSession() {
        finalKillsSession = 0;
        bedsBrokenSession = 0;
        gameStats.clear();
        
        ClientUtils.logInfo("Estatísticas da sessão resetadas");
    }
    
    /**
     * Reseta todas as estatísticas
     */
    public void resetAll() {
        finalKills = 0;
        bedsBroken = 0;
        wins = 0;
        losses = 0;
        gamesPlayed = 0;
        finalKillsSession = 0;
        bedsBrokenSession = 0;
        gameStats.clear();
        
        ClientUtils.logInfo("Todas as estatísticas resetadas");
    }
    
    /**
     * Obtém o ID do jogo atual (simplificado)
     */
    private String getCurrentGameId() {
        if (mc.theWorld == null) return "unknown";
        return "game_" + mc.theWorld.getWorldTime();
    }
    
    // Getters e Setters
    public int getFinalKills() {
        return finalKills;
    }
    
    public int getBedsBroken() {
        return bedsBroken;
    }
    
    public int getWins() {
        return wins;
    }
    
    public int getLosses() {
        return losses;
    }
    
    public int getGamesPlayed() {
        return gamesPlayed;
    }
    
    public boolean isShowFinalKills() {
        return showFinalKills;
    }
    
    public void setShowFinalKills(boolean showFinalKills) {
        this.showFinalKills = showFinalKills;
    }
    
    public boolean isShowBedsBroken() {
        return showBedsBroken;
    }
    
    public void setShowBedsBroken(boolean showBedsBroken) {
        this.showBedsBroken = showBedsBroken;
    }
    
    public boolean isShowWins() {
        return showWins;
    }
    
    public void setShowWins(boolean showWins) {
        this.showWins = showWins;
    }
    
    public boolean isShowGamesPlayed() {
        return showGamesPlayed;
    }
    
    public void setShowGamesPlayed(boolean showGamesPlayed) {
        this.showGamesPlayed = showGamesPlayed;
    }
    
    public boolean isShowStatsPerGame() {
        return showStatsPerGame;
    }
    
    public void setShowStatsPerGame(boolean showStatsPerGame) {
        this.showStatsPerGame = showStatsPerGame;
    }
    
    public int getBgColor() {
        return bgColor;
    }
    
    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }
    
    public int getTextColor() {
        return textColor;
    }
    
    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
    
    /**
     * Classe interna para armazenar estatísticas por jogo
     */
    private static class SessionStats {
        int finalKills = 0;
        int bedsBroken = 0;
    }
}
