package com.novaclient.mods.bedwars;

import com.novaclient.mods.Mod;
import com.novaclient.utils.ClientUtils;
import com.novaclient.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

/**
 * Mod Fireball Jump Timer - Timer para pular no momento certo de uma fireball
 */
public class FireballJumpTimerMod extends Mod {
    
    private static final Minecraft mc = Minecraft.getMinecraft();
    
    // Configurações
    private boolean showTimer = true;
    private boolean showWarning = true;
    private boolean playSound = true;
    private int warningTime = 1000; // 1 segundo antes
    private int bgColor = 0x80000000;
    private int textColor = 0xFFFFFFFF;
    private int warningColor = 0xFFFF0000;
    
    // Estado
    private List<FireballInfo> fireballs = new ArrayList<>();
    private long lastWarningTime = 0;
    private int warningCooldown = 2000; // 2 segundos
    
    public FireballJumpTimerMod() {
        super("Fireball Jump Timer", "Timer para pular de fireballs", ModCategory.BEDWARS);
        setX(10);
        setY(250);
    }
    
    @Override
    public void onTick() {
        if (!isEnabled() || mc.theWorld == null || mc.thePlayer == null) return;
        
        // Atualiza as fireballs
        updateFireballs();
        
        // Toca som de aviso
        playWarningSound();
    }
    
    @Override
    public void onRender() {
        if (!isEnabled()) return;
        
        ScaledResolution sr = new ScaledResolution(mc);
        
        // Desenha os timers
        if (showTimer) {
            drawTimers();
        }
        
        // Desenha os avisos
        if (showWarning) {
            drawWarnings();
        }
    }
    
    /**
     * Atualiza a lista de fireballs
     */
    private void updateFireballs() {
        fireballs.clear();
        
        if (mc.theWorld == null) return;
        
        for (Object entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityFireball) {
                EntityFireball fireball = (EntityFireball) entity;
                
                // Verifica se a fireball está vindo em direção ao jogador
                if (isFireballComingTowardsPlayer(fireball)) {
                    fireballs.add(new FireballInfo(fireball, System.currentTimeMillis()));
                }
            }
        }
    }
    
    /**
     * Verifica se a fireball está vindo em direção ao jogador
     */
    private boolean isFireballComingTowardsPlayer(EntityFireball fireball) {
        if (mc.thePlayer == null) return false;
        
        double fireballX = fireball.posX;
        double fireballY = fireball.posY;
        double fireballZ = fireball.posZ;
        
        double playerX = mc.thePlayer.posX;
        double playerY = mc.thePlayer.posY;
        double playerZ = mc.thePlayer.posZ;
        
        // Calcula a direção da fireball
        double dx = fireball.motionX;
        double dy = fireball.motionY;
        double dz = fireball.motionZ;
        
        // Calcula a distância até o jogador
        double distanceX = playerX - fireballX;
        double distanceY = playerY - fireballY;
        double distanceZ = playerZ - fireballZ;
        
        // Verifica se a fireball está se movendo em direção ao jogador
        // (produto escalar positivo significa que está se aproximando)
        double dotProduct = dx * distanceX + dy * distanceY + dz * distanceZ;
        
        // Verifica se a fireball está a uma distância razoável
        double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
        
        return dotProduct > 0 && distance < 50;
    }
    
    /**
     * Calcula o tempo até a fireball atingir o jogador
     */
    private long getTimeUntilImpact(EntityFireball fireball) {
        if (mc.thePlayer == null) return -1;
        
        double fireballX = fireball.posX;
        double fireballY = fireball.posY;
        double fireballZ = fireball.posZ;
        
        double playerX = mc.thePlayer.posX;
        double playerY = mc.thePlayer.posY + mc.thePlayer.getEyeHeight();
        double playerZ = mc.thePlayer.posZ;
        
        // Calcula a distância
        double distanceX = playerX - fireballX;
        double distanceY = playerY - fireballY;
        double distanceZ = playerZ - fireballZ;
        
        // Calcula a velocidade da fireball
        double speed = Math.sqrt(
            fireball.motionX * fireball.motionX +
            fireball.motionY * fireball.motionY +
            fireball.motionZ * fireball.motionZ
        );
        
        // Calcula o tempo (simplificado - assume movimento retilíneo)
        double distance = Math.sqrt(
            distanceX * distanceX +
            distanceY * distanceY +
            distanceZ * distanceZ
        );
        
        if (speed <= 0) return -1;
        
        long timeMillis = (long) (distance / speed * 1000);
        return Math.max(0, timeMillis);
    }
    
    /**
     * Toca o som de aviso
     */
    private void playWarningSound() {
        if (!playSound) return;
        
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastWarningTime < warningCooldown) return;
        
        for (FireballInfo fireball : fireballs) {
            long timeUntilImpact = getTimeUntilImpact(fireball.entity);
            
            if (timeUntilImpact >= 0 && timeUntilImpact <= warningTime) {
                mc.theWorld.playSound(
                    mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ,
                    "random.explode", 0.5f, 1.5f, false
                );
                lastWarningTime = currentTime;
                break; // Toca som apenas para uma fireball
            }
        }
    }
    
    /**
     * Desenha os timers das fireballs
     */
    private void drawTimers() {
        int x = getX();
        int y = getY();
        
        for (FireballInfo fireball : fireballs) {
            long timeUntilImpact = getTimeUntilImpact(fireball.entity);
            
            if (timeUntilImpact >= 0) {
                double seconds = timeUntilImpact / 1000.0;
                String timeText = String.format("Fireball: %.1fs", seconds);
                
                // Desenha o background
                RenderUtils.drawRect(x - 5, y - 5, x + 120, y + 15, bgColor);
                
                // Desenha o texto
                int color = timeUntilImpact <= warningTime ? warningColor : textColor;
                mc.fontRendererObj.drawStringWithShadow(timeText, x, y, color);
                
                y += 12;
            }
        }
    }
    
    /**
     * Desenha os avisos
     */
    private void drawWarnings() {
        for (FireballInfo fireball : fireballs) {
            long timeUntilImpact = getTimeUntilImpact(fireball.entity);
            
            if (timeUntilImpact >= 0 && timeUntilImpact <= warningTime) {
                String warning = String.format("§c⚠ FIREBALL INCOMING! %.1fs", timeUntilImpact / 1000.0);
                int width = mc.fontRendererObj.getStringWidth(warning);
                int x = mc.displayWidth / 2 - width / 2;
                int y = mc.displayHeight / 3;
                
                // Desenha o background
                RenderUtils.drawRect(x - 5, y - 5, x + width + 5, y + 15, bgColor);
                
                // Desenha o texto
                mc.fontRendererObj.drawStringWithShadow(warning, x, y, warningColor);
            }
        }
    }
    
    // Getters e Setters
    public boolean isShowTimer() {
        return showTimer;
    }
    
    public void setShowTimer(boolean showTimer) {
        this.showTimer = showTimer;
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
    
    public int getWarningTime() {
        return warningTime;
    }
    
    public void setWarningTime(int warningTime) {
        this.warningTime = Math.max(0, warningTime);
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
    
    public int getWarningColor() {
        return warningColor;
    }
    
    public void setWarningColor(int warningColor) {
        this.warningColor = warningColor;
    }
    
    /**
     * Classe interna para armazenar informações da fireball
     */
    private static class FireballInfo {
        EntityFireball entity;
        long detectionTime;
        
        FireballInfo(EntityFireball entity, long detectionTime) {
            this.entity = entity;
            this.detectionTime = detectionTime;
        }
    }
}
