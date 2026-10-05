package com.novaclient.mods.pvp;

import com.novaclient.mods.Mod;
import com.novaclient.utils.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;

import java.util.HashMap;
import java.util.Map;

/**
 * Mod CPS Counter - Contador de cliques por segundo
 * Mostra o CPS do botão esquerdo e direito do mouse
 */
public class CPSCounterMod extends Mod {
    
    // Mapa para armazenar o CPS de cada botão do mouse
    private final Map<Integer, Integer> cps = new HashMap<>();
    private final Map<Integer, Long> lastClickTime = new HashMap<>();
    private final Map<Integer, Integer> clickCount = new HashMap<>();
    
    // Botões do mouse
    private static final int LEFT_CLICK = 0;
    private static final int RIGHT_CLICK = 1;
    
    // Configurações de display
    private boolean showLeftCPS = true;
    private boolean showRightCPS = true;
    private boolean showBackground = true;
    private int bgColor = 0x80000000; // Preto semi-transparente
    private int textColor = 0xFFFFFFFF; // Branco
    private boolean showMaxCPS = true;
    private int maxLeftCPS = 0;
    private int maxRightCPS = 0;
    
    public CPSCounterMod() {
        super("CPS Counter", "Exibe o CPS dos botões do mouse", ModCategory.PVP);
        
        // Inicializa os contadores
        cps.put(LEFT_CLICK, 0);
        cps.put(RIGHT_CLICK, 0);
        clickCount.put(LEFT_CLICK, 0);
        clickCount.put(RIGHT_CLICK, 0);
        
        // Posição padrão
        setX(10);
        setY(50);
    }
    
    @Override
    public void onTick() {
        updateCPS();
    }
    
    @Override
    public void onRender() {
        if (!isEnabled()) return;
        
        ScaledResolution sr = new ScaledResolution(mc);
        
        int x = getX();
        int y = getY();
        
        // Calcula a largura e altura com base no que será exibido
        int width = 100;
        int height = 20;
        
        if (showLeftCPS && showRightCPS) {
            height = 40;
        }
        
        // Desenha o background
        if (showBackground) {
            RenderUtils.drawRect(x, y, x + width, y + height, bgColor);
        }
        
        // Desenha o texto do CPS
        int textX = x + 5;
        int textY = y + 5;
        
        if (showLeftCPS) {
            String leftText = EnumChatFormatting.GREEN + "Left CPS: " + EnumChatFormatting.WHITE + cps.getOrDefault(LEFT_CLICK, 0);
            if (showMaxCPS) {
                leftText += EnumChatFormatting.GRAY + " (Max: " + maxLeftCPS + ")";
            }
            mc.fontRendererObj.drawStringWithShadow(leftText, textX, textY, textColor);
            textY += 10;
        }
        
        if (showRightCPS) {
            String rightText = EnumChatFormatting.GREEN + "Right CPS: " + EnumChatFormatting.WHITE + cps.getOrDefault(RIGHT_CLICK, 0);
            if (showMaxCPS) {
                rightText += EnumChatFormatting.GRAY + " (Max: " + maxRightCPS + ")";
            }
            mc.fontRendererObj.drawStringWithShadow(rightText, textX, textY, textColor);
        }
    }
    
    /**
     * Atualiza o CPS dos botões do mouse
     */
    private void updateCPS() {
        long currentTime = System.currentTimeMillis();
        
        // Atualiza CPS para o botão esquerdo
        if (mc.gameSettings.keyBindAttack.isKeyDown()) {
            Long lastTime = lastClickTime.get(LEFT_CLICK);
            if (lastTime == null || currentTime - lastTime > 1000) {
                // Mais de 1 segundo desde o último clique, reinicia a contagem
                clickCount.put(LEFT_CLICK, 1);
                lastClickTime.put(LEFT_CLICK, currentTime);
            } else {
                clickCount.put(LEFT_CLICK, clickCount.getOrDefault(LEFT_CLICK, 0) + 1);
            }
            
            int currentCPS = clickCount.getOrDefault(LEFT_CLICK, 0);
            cps.put(LEFT_CLICK, currentCPS);
            
            // Atualiza o CPS máximo
            if (currentCPS > maxLeftCPS) {
                maxLeftCPS = currentCPS;
            }
        } else {
            // Se não estiver pressionado, verifica se passou 1 segundo para resetar
            Long lastTime = lastClickTime.get(LEFT_CLICK);
            if (lastTime != null && currentTime - lastTime > 1000) {
                cps.put(LEFT_CLICK, 0);
                clickCount.put(LEFT_CLICK, 0);
            }
        }
        
        // Atualiza CPS para o botão direito
        if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
            Long lastTime = lastClickTime.get(RIGHT_CLICK);
            if (lastTime == null || currentTime - lastTime > 1000) {
                clickCount.put(RIGHT_CLICK, 1);
                lastClickTime.put(RIGHT_CLICK, currentTime);
            } else {
                clickCount.put(RIGHT_CLICK, clickCount.getOrDefault(RIGHT_CLICK, 0) + 1);
            }
            
            int currentCPS = clickCount.getOrDefault(RIGHT_CLICK, 0);
            cps.put(RIGHT_CLICK, currentCPS);
            
            // Atualiza o CPS máximo
            if (currentCPS > maxRightCPS) {
                maxRightCPS = currentCPS;
            }
        } else {
            Long lastTime = lastClickTime.get(RIGHT_CLICK);
            if (lastTime != null && currentTime - lastTime > 1000) {
                cps.put(RIGHT_CLICK, 0);
                clickCount.put(RIGHT_CLICK, 0);
            }
        }
    }
    
    /**
     * Reseta os valores máximos de CPS
     */
    public void resetMaxCPS() {
        maxLeftCPS = 0;
        maxRightCPS = 0;
    }
    
    // Getters e Setters para configurações
    public boolean isShowLeftCPS() {
        return showLeftCPS;
    }
    
    public void setShowLeftCPS(boolean showLeftCPS) {
        this.showLeftCPS = showLeftCPS;
    }
    
    public boolean isShowRightCPS() {
        return showRightCPS;
    }
    
    public void setShowRightCPS(boolean showRightCPS) {
        this.showRightCPS = showRightCPS;
    }
    
    public boolean isShowBackground() {
        return showBackground;
    }
    
    public void setShowBackground(boolean showBackground) {
        this.showBackground = showBackground;
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
    
    public boolean isShowMaxCPS() {
        return showMaxCPS;
    }
    
    public void setShowMaxCPS(boolean showMaxCPS) {
        this.showMaxCPS = showMaxCPS;
    }
    
    public int getMaxLeftCPS() {
        return maxLeftCPS;
    }
    
    public int getMaxRightCPS() {
        return maxRightCPS;
    }
    
    public int getCurrentLeftCPS() {
        return cps.getOrDefault(LEFT_CLICK, 0);
    }
    
    public int getCurrentRightCPS() {
        return cps.getOrDefault(RIGHT_CLICK, 0);
    }
}
