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
 * Mod Low Health Warning - Avisa quando a vida está baixa
 */
public class LowHealthWarningMod extends Mod {
    
    private static final Minecraft mc = Minecraft.getMinecraft();
    
    // Configurações
    private float healthThreshold = 4.0f;
    private boolean showWarning = true;
    private boolean playSound = true;
    private boolean showInHUD = true;
    private int warningColor = 0xFFFF0000;
    private int bgColor = 0x80000000;
    
    // Estado
    private boolean wasLowHealth = false;
    private long lastWarningTime = 0;
    private int warningCooldown = 1000; // 1 segundo
    
    // Jogadores com vida baixa
    private Map<String, Float> lowHealthPlayers = new HashMap<>();
    
    public LowHealthWarningMod() {
        super("Low Health Warning", "Avisa quando a vida está baixa", ModCategory.BEDWARS);
        setX(10);
        setY(200);
    }
    
    @Override
    public void onTick() {
        if (!isEnabled() || mc.thePlayer == null || mc.theWorld == null) return;
        
        // Verifica a vida do jogador
        checkPlayerHealth();
        
        // Verifica a vida de outros jogadores
        checkOtherPlayersHealth();
        
        // Toca som de aviso
        playWarningSound();
    }
    
    @Override
    public void onRender() {
        if (!isEnabled() || !showInHUD) return;
        
        ScaledResolution sr = new ScaledResolution(mc);
        
        // Desenha o aviso de vida baixa do jogador
        if (mc.thePlayer.getHealth() <= healthThreshold && showWarning) {
            drawHealthWarning(mc.thePlayer.getHealth());
        }
        
        // Desenha os aviso de vida baixa de outros jogadores
        if (!lowHealthPlayers.isEmpty()) {
            drawOtherPlayersWarning();
        }
    }
    
    /**
     * Verifica a vida do jogador
     */
    private void checkPlayerHealth() {
        if (mc.thePlayer == null) return;
        
        float health = mc.thePlayer.getHealth();
        boolean isLowHealth = health <= healthThreshold;
        
        if (isLowHealth && !wasLowHealth) {
            // Vida baixa detectada
            wasLowHealth = true;
            ClientUtils.logDebug("Vida baixa detectada: " + health + " HP");
        } else if (!isLowHealth) {
            wasLowHealth = false;
        }
    }
    
    /**
     * Verifica a vida de outros jogadores
     */
    private void checkOtherPlayersHealth() {
        lowHealthPlayers.clear();
        
        if (mc.theWorld == null) return;
        
        for (Object entity : mc.theWorld.playerEntities) {
            if (entity instanceof net.minecraft.entity.player.EntityPlayer) {
                net.minecraft.entity.player.EntityPlayer player = (net.minecraft.entity.player.EntityPlayer) entity;
                
                if (player == mc.thePlayer) continue;
                
                float health = player.getHealth();
                if (health <= healthThreshold) {
                    lowHealthPlayers.put(player.getName(), health);
                }
            }
        }
    }
    
    /**
     * Desenha o aviso de vida baixa
     */
    private void drawHealthWarning(float health) {
        String warning = String.format("§c⚠ LOW HEALTH: %.1f HP!", health);
        int width = mc.fontRendererObj.getStringWidth(warning);
        int x = mc.displayWidth / 2 - width / 2;
        int y = mc.displayHeight / 2 - 20;
        
        // Desenha o background
        RenderUtils.drawRect(x - 5, y - 5, x + width + 5, y + 15, bgColor);
        
        // Desenha o texto
        mc.fontRendererObj.drawStringWithShadow(warning, x, y, warningColor);
    }
    
    /**
     * Desenha os avisos de vida baixa de outros jogadores
     */
    private void drawOtherPlayersWarning() {
        int x = getX();
        int y = getY();
        
        // Desenha o background
        RenderUtils.drawRect(x - 5, y - 5, x + 200, y + 20 + (lowHealthPlayers.size() * 10), bgColor);
        
        // Desenha o título
        mc.fontRendererObj.drawStringWithShadow("§cLow Health Players:", x, y, warningColor);
        y += 10;
        
        // Desenha os jogadores
        for (Map.Entry<String, Float> entry : lowHealthPlayers.entrySet()) {
            String playerText = String.format("§7%s: %.1f HP", entry.getKey(), entry.getValue());
            mc.fontRendererObj.drawStringWithShadow(playerText, x, y, warningColor);
            y += 10;
        }
    }
    
    /**
     * Toca o som de aviso
     */
    private void playWarningSound() {
        if (!playSound) return;
        
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastWarningTime < warningCooldown) return;
        
        // Toca o som apenas se a vida estiver baixa
        if (mc.thePlayer.getHealth() <= healthThreshold) {
            mc.theWorld.playSound(
                mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ,
                "random.anvil_land", 1.0f, 0.5f, false
            );
            lastWarningTime = currentTime;
        }
    }
    
    // Getters e Setters
    public float getHealthThreshold() {
        return healthThreshold;
    }
    
    public void setHealthThreshold(float healthThreshold) {
        this.healthThreshold = Math.max(0.5f, Math.min(20.0f, healthThreshold));
    }
    
    public boolean isShowWarning() {
        return showWarning;
    }
    
    public void setShowWarning(boolean showWarning) {
        this.showWarning = showWarning;
    }
    
    public boolean isPlaySound() {
        return playSound;
    }
    
    public void setPlaySound(boolean playSound) {
        this.playSound = playSound;
    }
    
    public boolean isShowInHUD() {
        return showInHUD;
    }
    
    public void setShowInHUD(boolean showInHUD) {
        this.showInHUD = showInHUD;
    }
    
    public int getWarningColor() {
        return warningColor;
    }
    
    public void setWarningColor(int warningColor) {
        this.warningColor = warningColor;
    }
    
    public int getBgColor() {
        return bgColor;
    }
    
    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }
    
    public Map<String, Float> getLowHealthPlayers() {
        return new HashMap<>(lowHealthPlayers);
    }
}
