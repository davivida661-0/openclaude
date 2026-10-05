package com.novaclient.mods;

import com.novaclient.NovaClient;
import net.minecraft.client.Minecraft;

/**
 * Classe base para todos os mods do cliente
 * Contém métodos e propriedades comuns a todos os mods
 */
public abstract class Mod {
    
    protected final Minecraft mc = NovaClient.getInstance().getMc();
    
    // Informações do mod
    private final String name;
    private final String description;
    private final ModCategory category;
    
    // Estado do mod
    private boolean enabled;
    private boolean visible;
    
    // Posição na HUD (para mods que têm display)
    private int x;
    private int y;
    private float scale = 1.0f;
    
    // Tecla de atalho
    private int keyBind = 0;
    
    /**
     * Construtor do mod
     * 
     * @param name Nome do mod
     * @param description Descrição do mod
     * @param category Categoria do mod
     */
    public Mod(String name, String description, ModCategory category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.enabled = false; // Padrão: desabilitado
        this.visible = true; // Padrão: visível na GUI
        this.x = 10; // Posição inicial X
        this.y = 10; // Posição inicial Y
    }
    
    /**
     * Método chamado ao habilitar o mod
     */
    public void onEnable() {
        this.enabled = true;
        onToggle(true);
    }
    
    /**
     * Método chamado ao desabilitar o mod
     */
    public void onDisable() {
        this.enabled = false;
        onToggle(false);
    }
    
    /**
     * Método chamado ao alternar o estado do mod
     * 
     * @param state Novo estado (true = habilitado, false = desabilitado)
     */
    public void onToggle(boolean state) {
        // Implementado pelas subclasses se necessário
    }
    
    /**
     * Método chamado a cada tick do jogo
     */
    public void onTick() {
        // Implementado pelas subclasses se necessário
    }
    
    /**
     * Método chamado a cada frame de renderização
     */
    public void onRender() {
        // Implementado pelas subclasses se necessário
    }
    
    /**
     * Método chamado ao renderizar a GUI
     * 
     * @param mouseX Posicao X do mouse
     * @param mouseY Posicao Y do mouse
     */
    public void onRenderGUI(int mouseX, int mouseY) {
        // Implementado pelas subclasses se necessário
    }
    
    /**
     * Método chamado ao clicar com o mouse
     * 
     * @param mouseX Posicao X do mouse
     * @param mouseY Posicao Y do mouse
     * @param button Botão do mouse (0 = esquerdo, 1 = direito, 2 = meio)
     */
    public void onMouseClick(int mouseX, int mouseY, int button) {
        // Implementado pelas subclasses se necessário
    }
    
    /**
     * Método chamado ao soltar o botão do mouse
     * 
     * @param mouseX Posicao X do mouse
     * @param mouseY Posicao Y do mouse
     * @param button Botão do mouse
     */
    public void onMouseRelease(int mouseX, int mouseY, int button) {
        // Implementado pelas subclasses se necessário
    }
    
    /**
     * Método chamado ao pressionar uma tecla
     * 
     * @param keyCode Código da tecla
     */
    public void onKeyPress(int keyCode) {
        // Implementado pelas subclasses se necessário
    }
    
    /**
     * Verifica se o mod está habilitado
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Define se o mod está habilitado
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }
    
    /**
     * Alternar o estado do mod
     */
    public void toggle() {
        setEnabled(!enabled);
    }
    
    /**
     * Verifica se o mod é visível na GUI
     */
    public boolean isVisible() {
        return visible;
    }
    
    /**
     * Define se o mod é visível na GUI
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    // Getters e Setters para posição e escala
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
    
    public float getScale() {
        return scale;
    }
    
    public void setScale(float scale) {
        this.scale = Math.max(0.5f, Math.min(2.0f, scale));
    }
    
    public int getKeyBind() {
        return keyBind;
    }
    
    public void setKeyBind(int keyBind) {
        this.keyBind = keyBind;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public ModCategory getCategory() {
        return category;
    }
    
    /**
     * Categorias dos mods
     */
    public enum ModCategory {
        PVP("PvP"),
        RENDER("Render"),
        BEDWARS("BedWars"),
        COSMETICS("Cosmetics"),
        SETTINGS("Settings");
        
        private final String displayName;
        
        ModCategory(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
