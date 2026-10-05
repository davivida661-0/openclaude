package com.novaclient.gui.clickgui.components;

import com.novaclient.mods.Mod;
import com.novaclient.utils.RenderUtils;
import net.minecraft.client.Minecraft;

/**
 * Botão de mod para a ClickGUI
 */
public class ModButton {
    
    private final Minecraft mc = Minecraft.getMinecraft();
    private final Mod mod;
    
    private int x;
    private int y;
    private int width;
    private int height = 20;
    
    private boolean hovered;
    
    public ModButton(Mod mod, int x, int y) {
        this.mod = mod;
        this.x = x;
        this.y = y;
    }
    
    /**
     * Desenha o botão
     */
    public void draw(int mouseX, int mouseY) {
        hovered = isHovered(mouseX, mouseY);
        
        // Cor do botão
        int bgColor = mod.isEnabled() ? 0x8000FF00 : (hovered ? 0x40FFFFFF : 0x20FFFFFF);
        
        // Desenha o background
        RenderUtils.drawRect(x, y, x + width, y + height, bgColor);
        
        // Desenha a borda
        RenderUtils.drawBorder(x, y, x + width, y + height, 1, 0xFF000000);
        
        // Desenha o nome do mod
        String displayName = mod.getName() + (mod.isEnabled() ? " §a[ON]" : " §c[OFF]");
        mc.fontRendererObj.drawStringWithShadow(displayName, x + 5, y + 5, 0xFFFFFFFF);
        
        // Desenha a descrição (se hover)
        if (hovered) {
            String description = mod.getDescription();
            int descWidth = mc.fontRendererObj.getStringWidth(description);
            
            // Desenha o background da descrição
            RenderUtils.drawRect(
                x + width + 10, y, 
                x + width + 10 + descWidth + 10, y + height, 
                0xC0000000
            );
            
            // Desenha a descrição
            mc.fontRendererObj.drawStringWithShadow(
                description, 
                x + width + 15, y + 5, 
                0xFFFFFFFF
            );
        }
    }
    
    /**
     * Método chamado ao clicar no botão
     */
    public void onClick(int mouseX, int mouseY, int button) {
        if (button == 0) { // Botão esquerdo
            mod.toggle();
        } else if (button == 1) { // Botão direito
            // Abre o menu de configurações do mod
            // (Implementação futura)
        }
    }
    
    /**
     * Verifica se o mouse está sobre o botão
     */
    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
    
    // Getters e Setters
    public Mod getMod() {
        return mod;
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
    
    public boolean isHovered() {
        return hovered;
    }
}
