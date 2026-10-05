package com.novaclient.gui.clickgui.components;

import com.novaclient.mods.Mod;
import com.novaclient.utils.RenderUtils;
import net.minecraft.client.Minecraft;

/**
 * Botão de categoria para a ClickGUI
 */
public class CategoryButton {
    
    private final Minecraft mc = Minecraft.getMinecraft();
    
    private final String name;
    private final int id;
    private final Mod.ModCategory category;
    
    private int x;
    private int y;
    private int width;
    private int height;
    
    private boolean selected;
    private boolean hovered;
    
    public CategoryButton(String name, int id, Mod.ModCategory category) {
        this.name = name;
        this.id = id;
        this.category = category;
    }
    
    /**
     * Desenha o botão
     */
    public void draw(int mouseX, int mouseY) {
        hovered = isHovered(mouseX, mouseY);
        
        // Cor do botão
        int bgColor = selected ? 0xFF00FFFF : (hovered ? 0x8000FFFF : 0x4000FFFF);
        
        // Desenha o background
        RenderUtils.drawRect(x, y, x + width, y + height, bgColor);
        
        // Desenha a borda
        RenderUtils.drawBorder(x, y, x + width, y + height, 1, 0xFF00FFFF);
        
        // Desenha o texto
        int textWidth = mc.fontRendererObj.getStringWidth(name);
        int textX = x + (width - textWidth) / 2;
        int textY = y + (height - mc.fontRendererObj.FONT_HEIGHT) / 2;
        
        mc.fontRendererObj.drawStringWithShadow(name, textX, textY, selected ? 0xFFFFFFFF : 0xFFAAAAAA);
    }
    
    /**
     * Verifica se o mouse está sobre o botão
     */
    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
    
    // Getters e Setters
    public String getName() {
        return name;
    }
    
    public int getId() {
        return id;
    }
    
    public Mod.ModCategory getCategory() {
        return category;
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
    
    public boolean isSelected() {
        return selected;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    public boolean isHovered() {
        return hovered;
    }
}
