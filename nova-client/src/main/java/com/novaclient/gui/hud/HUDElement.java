package com.novaclient.gui.hud;

import com.novaclient.NovaClient;
import com.novaclient.utils.RenderUtils;
import net.minecraft.client.Minecraft;

/**
 * Classe base para elementos da HUD
 * Representa um elemento individual que pode ser renderizado na HUD
 */
public class HUDElement {
    
    protected final Minecraft mc = NovaClient.getInstance().getMc();
    
    // Propriedades do elemento
    private final String name;
    private int x;
    private int y;
    private int width;
    private int height;
    
    // Estado
    private boolean enabled;
    private boolean visible;
    
    // Configurações de display
    private int bgColor = 0x80000000; // Preto semi-transparente
    private int borderColor = 0xFF000000; // Preto
    private int textColor = 0xFFFFFFFF; // Branco
    private boolean showBackground = false;
    private boolean showBorder = false;
    
    // Escala
    private float scale = 1.0f;
    
    // Posição do mouse
    private boolean hovered = false;
    
    /**
     * Construtor do elemento da HUD
     * 
     * @param name Nome do elemento
     * @param x Posição X
     * @param y Posição Y
     * @param width Largura
     * @param height Altura
     * @param enabled Habilitado
     * @param visible Visível
     */
    public HUDElement(String name, int x, int y, int width, int height, boolean enabled, boolean visible) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.enabled = enabled;
        this.visible = visible;
    }
    
    /**
     * Método chamado a cada tick do jogo
     */
    public void onTick() {
        // Implementado pelas subclasses se necessário
    }
    
    /**
     * Método chamado a cada frame de renderização
     * 
     * @param mouseX Posição X do mouse
     * @param mouseY Posição Y do mouse
     */
    public void onRender(int mouseX, int mouseY) {
        if (!enabled || !visible) return;
        
        // Atualiza o estado de hover
        hovered = isHovered(mouseX, mouseY);
        
        // Desenha o background
        if (showBackground) {
            RenderUtils.drawRect(x, y, x + width, y + height, bgColor);
        }
        
        // Desenha a borda
        if (showBorder) {
            RenderUtils.drawBorder(x, y, x + width, y + height, 1, borderColor);
        }
        
        // Desenha o conteúdo
        renderContent();
    }
    
    /**
     * Método para renderizar o conteúdo do elemento
     * Deve ser implementado pelas subclasses
     */
    protected void renderContent() {
        // Desenha o nome do elemento (para depuração)
        mc.fontRendererObj.drawStringWithShadow(name, x + 2, y + 2, textColor);
    }
    
    /**
     * Método chamado ao clicar no elemento
     * 
     * @param mouseX Posição X do mouse
     * @param mouseY Posição Y do mouse
     * @param button Botão do mouse (0 = esquerdo, 1 = direito, 2 = meio)
     */
    public void onClick(int mouseX, int mouseY, int button) {
        // Implementado pelas subclasses se necessário
    }
    
    /**
     * Verifica se o mouse está sobre o elemento
     */
    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
    
    /**
     * Define a posição do elemento
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Define o tamanho do elemento
     */
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    // Getters e Setters
    public String getName() {
        return name;
    }
    
    public int getX() {
        return x;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public int getWidth() {
        return width;
    }
    
    public void setWidth(int width) {
        this.width = width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void setHeight(int height) {
        this.height = height;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    public int getBgColor() {
        return bgColor;
    }
    
    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }
    
    public int getBorderColor() {
        return borderColor;
    }
    
    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }
    
    public int getTextColor() {
        return textColor;
    }
    
    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
    
    public boolean isShowBackground() {
        return showBackground;
    }
    
    public void setShowBackground(boolean showBackground) {
        this.showBackground = showBackground;
    }
    
    public boolean isShowBorder() {
        return showBorder;
    }
    
    public void setShowBorder(boolean showBorder) {
        this.showBorder = showBorder;
    }
    
    public float getScale() {
        return scale;
    }
    
    public void setScale(float scale) {
        this.scale = Math.max(0.5f, Math.min(2.0f, scale));
    }
    
    public boolean isHovered() {
        return hovered;
    }
    
    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }
}
