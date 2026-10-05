package com.novaclient.mods.bedwars;

import com.novaclient.mods.Mod;
import com.novaclient.utils.ClientUtils;
import net.minecraft.client.Minecraft;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Mod AutoGG - Automaticamente envia "GG" no final do jogo
 */
public class AutoGGMod extends Mod {
    
    private static final Minecraft mc = Minecraft.getMinecraft();
    
    // Configurações
    private boolean enabled = false;
    private String ggMessage = "GG";
    private int delaySeconds = 1;
    private boolean onlyInBedWars = true;
    private boolean onlyOnWin = false;
    private boolean onlyOnLoss = false;
    
    // Estado
    private boolean gameEnded = false;
    private Timer ggTimer = null;
    
    public AutoGGMod() {
        super("AutoGG", "Automaticamente envia GG no final do jogo", ModCategory.BEDWARS);
    }
    
    @Override
    public void onTick() {
        if (!isEnabled() || mc.thePlayer == null || mc.theWorld == null) return;
        
        // Detecta o final do jogo
        detectGameEnd();
    }
    
    /**
     * Detecta quando o jogo termina
     */
    private void detectGameEnd() {
        // Verifica se está em BedWars
        if (onlyInBedWars && !ClientUtils.isInBedWars()) {
            gameEnded = false;
            return;
        }
        
        // Detecta o final do jogo a partir de mensagens do chat
        // Em implementação real, isso usaria eventos de chat
        
        // Verifica se o jogo terminou (ex: "Game Over!", "Victory!", etc.)
        // Por enquanto, simula a detecção
        if (isGameEndDetected() && !gameEnded) {
            gameEnded = true;
            
            // Verifica as condições
            boolean shouldSendGG = true;
            
            if (onlyOnWin && !wasWin()) {
                shouldSendGG = false;
            }
            
            if (onlyOnLoss && wasWin()) {
                shouldSendGG = false;
            }
            
            if (shouldSendGG) {
                // Aguarda o delay e envia o GG
                scheduleGGMessage();
            }
        }
    }
    
    /**
     * Simula a detecção do final do jogo
     */
    private boolean isGameEndDetected() {
        // Em implementação real, isso verificaria:
        // - Mensagens do chat como "Game Over!"
        // - Mudança no scoreboard
        // - Teleporte para o lobby
        // - etc.
        return false;
    }
    
    /**
     * Verifica se o jogador venceu
     */
    private boolean wasWin() {
        // Em implementação real, isso verificaria:
        // - Mensagem de vitória no chat
        // - Scoreboard mostrando vitória
        // - etc.
        return false;
    }
    
    /**
     * Agenda o envio da mensagem GG
     */
    private void scheduleGGMessage() {
        // Cancela o timer anterior se existir
        if (ggTimer != null) {
            ggTimer.cancel();
        }
        
        ggTimer = new Timer();
        ggTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendGGMessage();
                gameEnded = false;
            }
        }, delaySeconds * 1000L);
    }
    
    /**
     * Envia a mensagem GG
     */
    private void sendGGMessage() {
        if (mc.thePlayer == null) return;
        
        String message = ggMessage;
        
        // Adiciona prefixo se necessário
        if (!message.startsWith("/")) {
            message = "/ac " + message; // Comando de chat do Hypixel
        }
        
        mc.thePlayer.sendChatMessage(message);
        ClientUtils.logInfo("Mensagem GG enviada: " + message);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        gameEnded = false;
        ClientUtils.logInfo("AutoGG habilitado");
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        if (ggTimer != null) {
            ggTimer.cancel();
        }
        gameEnded = false;
        ClientUtils.logInfo("AutoGG desabilitado");
    }
    
    // Getters e Setters
    public String getGgMessage() {
        return ggMessage;
    }
    
    public void setGgMessage(String ggMessage) {
        this.ggMessage = ggMessage;
    }
    
    public int getDelaySeconds() {
        return delaySeconds;
    }
    
    public void setDelaySeconds(int delaySeconds) {
        this.delaySeconds = Math.max(0, delaySeconds);
    }
    
    public boolean isOnlyInBedWars() {
        return onlyInBedWars;
    }
    
    public void setOnlyInBedWars(boolean onlyInBedWars) {
        this.onlyInBedWars = onlyInBedWars;
    }
    
    public boolean isOnlyOnWin() {
        return onlyOnWin;
    }
    
    public void setOnlyOnWin(boolean onlyOnWin) {
        this.onlyOnWin = onlyOnWin;
    }
    
    public boolean isOnlyOnLoss() {
        return onlyOnLoss;
    }
    
    public void setOnlyOnLoss(boolean onlyOnLoss) {
        this.onlyOnLoss = onlyOnLoss;
    }
}
