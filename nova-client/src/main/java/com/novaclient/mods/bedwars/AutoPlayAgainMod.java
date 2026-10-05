package com.novaclient.mods.bedwars;

import com.novaclient.mods.Mod;
import com.novaclient.utils.ClientUtils;
import net.minecraft.client.Minecraft;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Mod Auto Play Again - Automaticamente clica em "Play Again" no final do jogo
 */
public class AutoPlayAgainMod extends Mod {
    
    private static final Minecraft mc = Minecraft.getMinecraft();
    
    // Configurações
    private boolean onlyInBedWars = true;
    private int delaySeconds = 2;
    private boolean onlyOnWin = false;
    private boolean onlyOnLoss = false;
    
    // Estado
    private boolean gameEnded = false;
    private Timer playAgainTimer = null;
    
    public AutoPlayAgainMod() {
        super("Auto Play Again", "Automaticamente clica em Play Again", ModCategory.BEDWARS);
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
        
        // Detecta o final do jogo
        if (isGameEndDetected() && !gameEnded) {
            gameEnded = true;
            
            // Verifica as condições
            boolean shouldPlayAgain = true;
            
            if (onlyOnWin && !wasWin()) {
                shouldPlayAgain = false;
            }
            
            if (onlyOnLoss && wasWin()) {
                shouldPlayAgain = false;
            }
            
            if (shouldPlayAgain) {
                // Aguarda o delay e clica em Play Again
                schedulePlayAgain();
            }
        }
    }
    
    /**
     * Simula a detecção do final do jogo
     */
    private boolean isGameEndDetected() {
        // Em implementação real, isso verificaria:
        // - Botão "Play Again" na tela
        // - Mensagem de final de jogo
        // - etc.
        return false;
    }
    
    /**
     * Verifica se o jogador venceu
     */
    private boolean wasWin() {
        // Em implementação real, isso verificaria:
        // - Mensagem de vitória
        // - Scoreboard
        // - etc.
        return false;
    }
    
    /**
     * Agenda o clique em Play Again
     */
    private void schedulePlayAgain() {
        // Cancela o timer anterior se existir
        if (playAgainTimer != null) {
            playAgainTimer.cancel();
        }
        
        playAgainTimer = new Timer();
        playAgainTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                clickPlayAgain();
                gameEnded = false;
            }
        }, delaySeconds * 1000L);
    }
    
    /**
     * Clica no botão Play Again
     */
    private void clickPlayAgain() {
        // Em implementação real, isso usaria:
        // - Packet para clicar no botão
        // - ou simularia clique do mouse na posição do botão
        
        ClientUtils.logInfo("Clicando em Play Again...");
        
        // Simula o clique (em implementação real, isso seria mais complexo)
        // mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, null);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        gameEnded = false;
        ClientUtils.logInfo("Auto Play Again habilitado");
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        if (playAgainTimer != null) {
            playAgainTimer.cancel();
        }
        gameEnded = false;
        ClientUtils.logInfo("Auto Play Again desabilitado");
    }
    
    // Getters e Setters
    public boolean isOnlyInBedWars() {
        return onlyInBedWars;
    }
    
    public void setOnlyInBedWars(boolean onlyInBedWars) {
        this.onlyInBedWars = onlyInBedWars;
    }
    
    public int getDelaySeconds() {
        return delaySeconds;
    }
    
    public void setDelaySeconds(int delaySeconds) {
        this.delaySeconds = Math.max(0, delaySeconds);
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
