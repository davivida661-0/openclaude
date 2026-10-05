package com.novaclient.mods.pvp;

import com.novaclient.mods.Mod;
import com.novaclient.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;

import java.util.HashMap;
import java.util.Map;

/**
 * Mod Keystrokes - Exibe as teclas pressionadas na tela
 * Mostra WASD, LMB, RMB, Space e CPS das teclas
 */
public class KeystrokesMod extends Mod {
    
    // Mapa para armazenar o estado das teclas
    private final Map<Integer, Boolean> keyStates = new HashMap<>();
    
    // Mapa para armazenar o CPS de cada tecla
    private final Map<Integer, Integer> keyCPS = new HashMap<>();
    private final Map<Integer, Long> lastClickTime = new HashMap<>();
    private final Map<Integer, Integer> clickCount = new HashMap<>();
    
    // Teclas monitoradas
    private static final int KEY_W = 17; // W
    private static final int KEY_A = 30; // A
    private static final int KEY_S = 31; // S
    private static final int KEY_D = 32; // D
    private static final int KEY_SPACE = 57; // Space
    private static final int KEY_LMB = 0; // Left Mouse Button
    private static final int KEY_RMB = 1; // Right Mouse Button
    
    // Configurações
    private boolean showCPS = true;
    private boolean showKeys = true;
    private int bgColor = 0x80000000; // Preto semi-transparente
    private int textColor = 0xFFFFFFFF; // Branco
    private int pressedColor = 0xFF00FF00; // Verde para teclas pressionadas
    
    public KeystrokesMod() {
        super("Keystrokes", "Exibe as teclas pressionadas e CPS", ModCategory.PVP);
        
        // Inicializa as teclas monitoradas
        keyStates.put(KEY_W, false);
        keyStates.put(KEY_A, false);
        keyStates.put(KEY_S, false);
        keyStates.put(KEY_D, false);
        keyStates.put(KEY_SPACE, false);
        keyStates.put(KEY_LMB, false);
        keyStates.put(KEY_RMB, false);
        
        // Inicializa os contadores de CPS
        keyCPS.put(KEY_LMB, 0);
        keyCPS.put(KEY_RMB, 0);
        
        // Posição padrão
        setX(10);
        setY(100);
    }
    
    @Override
    public void onTick() {
        // Atualiza o estado das teclas
        updateKeyStates();
        
        // Atualiza o CPS
        updateCPS();
    }
    
    @Override
    public void onRender() {
        if (!isEnabled()) return;
        
        ScaledResolution sr = new ScaledResolution(mc);
        int screenWidth = sr.getScaledWidth();
        int screenHeight = sr.getScaledHeight();
        
        // Desenha o background
        int width = 120;
        int height = showCPS ? 100 : 60;
        RenderUtils.drawRect(getX(), getY(), getX() + width, getY() + height, bgColor);
        
        // Desenha as teclas
        if (showKeys) {
            drawKeys();
        }
        
        // Desenha o CPS
        if (showCPS) {
            drawCPS();
        }
    }
    
    /**
     * Atualiza o estado das teclas
     */
    private void updateKeyStates() {
        // Atualiza as teclas WASD e Space
        keyStates.put(KEY_W, mc.gameSettings.keyBindForward.isKeyDown());
        keyStates.put(KEY_A, mc.gameSettings.keyBindLeft.isKeyDown());
        keyStates.put(KEY_S, mc.gameSettings.keyBindBack.isKeyDown());
        keyStates.put(KEY_D, mc.gameSettings.keyBindRight.isKeyDown());
        keyStates.put(KEY_SPACE, mc.gameSettings.keyBindJump.isKeyDown());
        
        // Atualiza os botões do mouse
        keyStates.put(KEY_LMB, mc.gameSettings.keyBindAttack.isKeyDown());
        keyStates.put(KEY_RMB, mc.gameSettings.keyBindUseItem.isKeyDown());
    }
    
    /**
     * Atualiza o CPS das teclas
     */
    private void updateCPS() {
        long currentTime = System.currentTimeMillis();
        
        // Atualiza CPS para LMB
        if (keyStates.get(KEY_LMB)) {
            Long lastTime = lastClickTime.get(KEY_LMB);
            if (lastTime == null || currentTime - lastTime > 1000) {
                // Mais de 1 segundo desde o último clique, reinicia a contagem
                clickCount.put(KEY_LMB, 1);
                lastClickTime.put(KEY_LMB, currentTime);
            } else {
                clickCount.put(KEY_LMB, clickCount.getOrDefault(KEY_LMB, 0) + 1);
            }
            keyCPS.put(KEY_LMB, clickCount.getOrDefault(KEY_LMB, 0));
        } else {
            // Se não estiver pressionado, verifica se passou 1 segundo para resetar
            Long lastTime = lastClickTime.get(KEY_LMB);
            if (lastTime != null && currentTime - lastTime > 1000) {
                keyCPS.put(KEY_LMB, 0);
                clickCount.put(KEY_LMB, 0);
            }
        }
        
        // Atualiza CPS para RMB
        if (keyStates.get(KEY_RMB)) {
            Long lastTime = lastClickTime.get(KEY_RMB);
            if (lastTime == null || currentTime - lastTime > 1000) {
                clickCount.put(KEY_RMB, 1);
                lastClickTime.put(KEY_RMB, currentTime);
            } else {
                clickCount.put(KEY_RMB, clickCount.getOrDefault(KEY_RMB, 0) + 1);
            }
            keyCPS.put(KEY_RMB, clickCount.getOrDefault(KEY_RMB, 0));
        } else {
            Long lastTime = lastClickTime.get(KEY_RMB);
            if (lastTime != null && currentTime - lastTime > 1000) {
                keyCPS.put(KEY_RMB, 0);
                clickCount.put(KEY_RMB, 0);
            }
        }
    }
    
    /**
     * Desenha as teclas na tela
     */
    private void drawKeys() {
        int x = getX() + 5;
        int y = getY() + 5;
        
        // Linha 1: WASD
        drawKey(x, y, "W", keyStates.get(KEY_W));
        x += 20;
        drawKey(x, y, "A", keyStates.get(KEY_A));
        x += 20;
        drawKey(x, y, "S", keyStates.get(KEY_S));
        x += 20;
        drawKey(x, y, "D", keyStates.get(KEY_D));
        
        // Linha 2: Space
        x = getX() + 35;
        y += 20;
        drawKey(x, y, "SPACE", keyStates.get(KEY_SPACE));
        
        // Linha 3: Mouse Buttons
        x = getX() + 5;
        y += 20;
        drawKey(x, y, "LMB", keyStates.get(KEY_LMB));
        x += 30;
        drawKey(x, y, "RMB", keyStates.get(KEY_RMB));
    }
    
    /**
     * Desenha uma tecla individual
     */
    private void drawKey(int x, int y, String text, boolean pressed) {
        int color = pressed ? pressedColor : textColor;
        mc.fontRendererObj.drawStringWithShadow(text, x, y, color);
    }
    
    /**
     * Desenha o CPS na tela
     */
    private void drawCPS() {
        int x = getX() + 5;
        int y = getY() + 55;
        
        String lmbCPS = EnumChatFormatting.GREEN + "LMB: " + EnumChatFormatting.WHITE + keyCPS.getOrDefault(KEY_LMB, 0) + " CPS";
        String rmCPS = EnumChatFormatting.GREEN + "RMB: " + EnumChatFormatting.WHITE + keyCPS.getOrDefault(KEY_RMB, 0) + " CPS";
        
        mc.fontRendererObj.drawStringWithShadow(lmbCPS, x, y, textColor);
        mc.fontRendererObj.drawStringWithShadow(rmCPS, x, y + 10, textColor);
    }
    
    // Getters e Setters para configurações
    public boolean isShowCPS() {
        return showCPS;
    }
    
    public void setShowCPS(boolean showCPS) {
        this.showCPS = showCPS;
    }
    
    public boolean isShowKeys() {
        return showKeys;
    }
    
    public void setShowKeys(boolean showKeys) {
        this.showKeys = showKeys;
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
    
    public int getPressedColor() {
        return pressedColor;
    }
    
    public void setPressedColor(int pressedColor) {
        this.pressedColor = pressedColor;
    }
}
