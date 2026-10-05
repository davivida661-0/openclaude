package com.novaclient.mods.bedwars;

import com.novaclient.mods.Mod;
import com.novaclient.utils.ClientUtils;

/**
 * Mod Auto Tip - Automaticamente envia uma dica no chat
 */
public class AutoTipMod extends Mod {
    
    // Configurações
    private boolean enabled = false;
    private String tipMessage = "/tip";
    private int delaySeconds = 5;
    private boolean onlyInBedWars = true;
    private boolean randomTip = true;
    
    // Dicas pré-definidas
    private static final String[] DEFAULT_TIPS = {
        "/tip Thanks for the game!",
        "/tip GG!",
        "/tip Nice game!",
        "/tip Well played!",
        "/tip Good luck!",
        "/tip Have fun!",
        "/tip Enjoy the game!",
        "/tip Nice shots!",
        "/tip Great teamwork!",
        "/tip Amazing game!"
    };
    
    // Estado
    private boolean tipped = false;
    
    public AutoTipMod() {
        super("Auto Tip", "Automaticamente envia uma dica no chat", ModCategory.BEDWARS);
    }
    
    @Override
    public void onTick() {
        if (!isEnabled()) return;
        
        // Detecta o início do jogo
        detectGameStart();
    }
    
    /**
     * Detecta o início do jogo
     */
    private void detectGameStart() {
        // Em implementação real, isso verificaria:
        // - Mudança de mundo (do lobby para o jogo)
        // - Mensagem de início do jogo
        // - etc.
        
        if (isGameStartDetected() && !tipped) {
            tipped = true;
            sendTip();
        }
    }
    
    /**
     * Simula a detecção do início do jogo
     */
    private boolean isGameStartDetected() {
        // Em implementação real, isso seria mais complexo
        return false;
    }
    
    /**
     * Envia a dica
     */
    private void sendTip() {
        String message = tipMessage;
        
        if (randomTip) {
            int randomIndex = (int) (Math.random() * DEFAULT_TIPS.length);
            message = DEFAULT_TIPS[randomIndex];
        }
        
        // Em implementação real, isso enviaria a mensagem no chat
        // mc.thePlayer.sendChatMessage(message);
        
        ClientUtils.logInfo("Dica enviada: " + message);
        
        // Reseta o estado após o delay
        resetAfterDelay();
    }
    
    /**
     * Reseta o estado após o delay
     */
    private void resetAfterDelay() {
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                tipped = false;
            }
        }, delaySeconds * 1000L);
    }
    
    // Getters e Setters
    public String getTipMessage() {
        return tipMessage;
    }
    
    public void setTipMessage(String tipMessage) {
        this.tipMessage = tipMessage;
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
    
    public boolean isRandomTip() {
        return randomTip;
    }
    
    public void setRandomTip(boolean randomTip) {
        this.randomTip = randomTip;
    }
    
    public String[] getDefaultTips() {
        return DEFAULT_TIPS.clone();
    }
}
