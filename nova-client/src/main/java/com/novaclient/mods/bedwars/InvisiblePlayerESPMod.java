package com.novaclient.mods.bedwars;

import com.novaclient.mods.Mod;
import com.novaclient.utils.ClientUtils;
import com.novaclient.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

import java.util.ArrayList;
import java.util.List;

/**
 * Mod Invisible Player ESP - Avisa quando há jogadores invisíveis próximos
 */
public class InvisiblePlayerESPMod extends Mod {
    
    private static final Minecraft mc = Minecraft.getMinecraft();
    
    // Configurações
    private boolean showESP = true;
    private boolean playSound = true;
    private boolean showWarning = true;
    private double detectionRange = 10.0;
    private int espColor = 0x80FF0000;
    private int warningColor = 0xFFFF0000;
    
    // Estado
    private List<EntityPlayer> invisiblePlayers = new ArrayList<>();
    private long lastWarningTime = 0;
    private int warningCooldown = 2000; // 2 segundos
    
    public InvisiblePlayerESPMod() {
        super("Invisible Player ESP", "Avisa quando há jogadores invisíveis próximos", ModCategory.BEDWARS);
    }
    
    @Override
    public void onTick() {
        if (!isEnabled() || mc.theWorld == null || mc.thePlayer == null) return;
        
        // Detecta jogadores invisíveis
        detectInvisiblePlayers();
        
        // Toca som de aviso
        playWarningSound();
    }
    
    @Override
    public void onRender() {
        if (!isEnabled() || !showESP) return;
        
        // Desenha o ESP nos jogadores invisíveis
        for (EntityPlayer player : invisiblePlayers) {
            if (player != mc.thePlayer) {
                drawESP(player);
            }
        }
        
        // Desenha o aviso
        if (showWarning && !invisiblePlayers.isEmpty()) {
            drawWarning();
        }
    }
    
    /**
     * Detecta jogadores invisíveis próximos
     */
    private void detectInvisiblePlayers() {
        invisiblePlayers.clear();
        
        if (mc.theWorld == null || mc.thePlayer == null) return;
        
        for (Object entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                
                // Ignora o próprio jogador
                if (player == mc.thePlayer) continue;
                
                // Verifica se o jogador está invisível
                if (isPlayerInvisible(player)) {
                    // Verifica se está no range de detecção
                    double distance = mc.thePlayer.getDistanceToEntity(player);
                    if (distance <= detectionRange) {
                        invisiblePlayers.add(player);
                    }
                }
            }
        }
    }
    
    /**
     * Verifica se um jogador está invisível
     */
    private boolean isPlayerInvisible(EntityPlayer player) {
        // Verifica se o jogador tem o efeito de invisibilidade
        if (player.isPotionActive(14)) { // Potion ID 14 = Invisibility
            return true;
        }
        
        // Verifica se o jogador está usando armadura que o torna invisível
        // (ex: Armadura de invisibilidade em alguns servidores)
        
        // Verifica se o jogador não tem hitbox visível
        AxisAlignedBB boundingBox = player.getEntityBoundingBox();
        if (boundingBox == null) {
            return true;
        }
        
        // Verifica se o jogador não está renderizando
        // (Em implementação real, isso verificaria o renderer)
        
        return false;
    }
    
    /**
     * Desenha o ESP no jogador invisível
     */
    private void drawESP(EntityPlayer player) {
        if (mc.gameSettings.thirdPersonView != 0) return;
        
        double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * mc.timer.renderPartialTicks;
        double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * mc.timer.renderPartialTicks;
        double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * mc.timer.renderPartialTicks;
        
        // Desenha a bounding box
        AxisAlignedBB bb = player.getEntityBoundingBox();
        if (bb != null) {
            RenderUtils.drawBox(bb, espColor);
        }
        
        // Desenha uma linha até o jogador
        double playerX = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * mc.timer.renderPartialTicks;
        double playerY = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * mc.timer.renderPartialTicks;
        double playerZ = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * mc.timer.renderPartialTicks;
        
        RenderUtils.drawLine(
            (float) playerX, (float) playerY + mc.thePlayer.getEyeHeight(), (float) playerZ,
            (float) x, (float) y + player.getEyeHeight(), (float) z,
            2.0f, warningColor
        );
    }
    
    /**
     * Desenha o aviso na tela
     */
    private void drawWarning() {
        String warning = "§c⚠ INVISIBLE PLAYER NEARBY!";
        int width = mc.fontRendererObj.getStringWidth(warning);
        int x = mc.displayWidth / 2 - width / 2;
        int y = mc.displayHeight / 2 - 20;
        
        // Desenha o background
        RenderUtils.drawRect(x - 5, y - 5, x + width + 5, y + 15, 0x80000000);
        
        // Desenha o texto
        mc.fontRendererObj.drawStringWithShadow(warning, x, y, warningColor);
    }
    
    /**
     * Toca o som de aviso
     */
    private void playWarningSound() {
        if (!playSound || invisiblePlayers.isEmpty()) return;
        
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastWarningTime < warningCooldown) return;
        
        // Toca o som
        mc.theWorld.playSound(
            mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ,
            "random.explode", 1.0f, 1.0f, false
        );
        
        lastWarningTime = currentTime;
    }
    
    // Getters e Setters
    public boolean isShowESP() {
        return showESP;
    }
    
    public void setShowESP(boolean showESP) {
        this.showESP = showESP;
    }
    
    public boolean isPlaySound() {
        return playSound;
    }
    
    public void setPlaySound(boolean playSound) {
        this.playSound = playSound;
    }
    
    public boolean isShowWarning() {
        return showWarning;
    }
    
    public void setShowWarning(boolean showWarning) {
        this.showWarning = showWarning;
    }
    
    public double getDetectionRange() {
        return detectionRange;
    }
    
    public void setDetectionRange(double detectionRange) {
        this.detectionRange = Math.max(5.0, Math.min(50.0, detectionRange));
    }
    
    public int getEspColor() {
        return espColor;
    }
    
    public void setEspColor(int espColor) {
        this.espColor = espColor;
    }
    
    public int getWarningColor() {
        return warningColor;
    }
    
    public void setWarningColor(int warningColor) {
        this.warningColor = warningColor;
    }
    
    public List<EntityPlayer> getInvisiblePlayers() {
        return new ArrayList<>(invisiblePlayers);
    }
}
