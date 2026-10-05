package com.novaclient.gui.hud;

import com.novaclient.NovaClient;
import com.novaclient.mods.Mod;
import com.novaclient.utils.ClientUtils;
import com.novaclient.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;
import java.util.List;

/**
 * Gerenciador de HUD do cliente
 * Responsável por gerenciar e renderizar todos os elementos da HUD
 */
public class HUDManager {
    
    private final Minecraft mc = NovaClient.getInstance().getMc();
    private final List<HUDElement> hudElements = new ArrayList<>();
    
    // Estado
    private boolean hudEnabled = true;
    private boolean editingMode = false;
    private HUDElement selectedElement = null;
    private HUDElement draggingElement = null;
    
    // Posição do mouse
    private int mouseX = 0;
    private int mouseY = 0;
    
    public HUDManager() {
        // Inicializa os elementos da HUD
        initializeHUDElements();
    }
    
    /**
     * Inicializa os elementos da HUD
     */
    private void initializeHUDElements() {
        // Adiciona elementos padrão da HUD
        hudElements.add(new HUDElement("FPS", 10, 10, 50, 12, true, true));
        hudElements.add(new HUDElement("CPS", 10, 25, 50, 12, true, true));
        hudElements.add(new HUDElement("Ping", 10, 40, 50, 12, true, true));
        hudElements.add(new HUDElement("Coordinates", 10, 55, 100, 12, true, true));
        hudElements.add(new HUDElement("Armor", 10, 70, 80, 12, true, true));
        hudElements.add(new HUDElement("PotionEffects", 10, 85, 120, 12, true, true));
        hudElements.add(new HUDElement("Keystrokes", 10, 100, 120, 60, true, true));
    }
    
    /**
     * Método chamado a cada tick do jogo
     */
    public void onTick() {
        if (!hudEnabled) return;
        
        // Atualiza todos os elementos da HUD
        for (HUDElement element : hudElements) {
            if (element.isEnabled()) {
                element.onTick();
            }
        }
    }
    
    /**
     * Método chamado a cada frame de renderização
     */
    public void onRender() {
        if (!hudEnabled || mc.gameSettings.showDebugInfo) return;
        
        ScaledResolution sr = new ScaledResolution(mc);
        int screenWidth = sr.getScaledWidth();
        int screenHeight = sr.getScaledHeight();
        
        // Renderiza todos os elementos da HUD
        for (HUDElement element : hudElements) {
            if (element.isEnabled()) {
                element.onRender(mouseX, mouseY);
            }
        }
        
        // Renderiza o modo de edição
        if (editingMode) {
            renderEditingMode();
        }
    }
    
    /**
     * Renderiza o modo de edição da HUD
     */
    private void renderEditingMode() {
        ScaledResolution sr = new ScaledResolution(mc);
        
        // Desenha o background semi-transparente
        RenderUtils.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 0x60000000);
        
        // Desenha os elementos da HUD com destaque
        for (HUDElement element : hudElements) {
            if (element.isEnabled()) {
                // Desenha o background do elemento
                int bgColor = element == selectedElement ? 0x80FF0000 : 0x40FFFFFF;
                RenderUtils.drawRect(
                    element.getX() - 2, 
                    element.getY() - 2, 
                    element.getX() + element.getWidth() + 2, 
                    element.getY() + element.getHeight() + 2, 
                    bgColor
                );
                
                // Desenha a borda
                RenderUtils.drawBorder(
                    element.getX() - 2, 
                    element.getY() - 2, 
                    element.getX() + element.getWidth() + 2, 
                    element.getY() + element.getHeight() + 2, 
                    1, 
                    element == selectedElement ? 0xFFFF0000 : 0xFF0000FF
                );
            }
        }
        
        // Desenha as instruções
        String instructions = "§eModo de Edição: Clique para selecionar, arraste para mover, R para redimensionar, DEL para remover, ESC para sair";
        mc.fontRendererObj.drawStringWithShadow(instructions, 10, 10, 0xFFFFFFFF);
    }
    
    /**
     * Método chamado ao clicar com o mouse
     */
    public void onMouseClick(int mouseX, int mouseY, int button) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        
        if (!hudEnabled) return;
        
        if (editingMode) {
            handleEditingMouseClick(mouseX, mouseY, button);
        } else {
            // Verifica se algum elemento foi clicado
            for (HUDElement element : hudElements) {
                if (element.isEnabled() && element.isHovered(mouseX, mouseY)) {
                    element.onClick(mouseX, mouseY, button);
                }
            }
        }
    }
    
    /**
     * Método chamado ao soltar o botão do mouse
     */
    public void onMouseRelease(int mouseX, int mouseY, int button) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        
        if (!hudEnabled) return;
        
        if (editingMode && draggingElement != null) {
            draggingElement = null;
        }
    }
    
    /**
     * Trata o clique do mouse no modo de edição
     */
    private void handleEditingMouseClick(int mouseX, int mouseY, int button) {
        // Verifica se algum elemento foi clicado
        for (HUDElement element : hudElements) {
            if (element.isEnabled() && element.isHovered(mouseX, mouseY)) {
                switch (button) {
                    case 0: // Botão esquerdo
                        selectedElement = element;
                        draggingElement = element;
                        break;
                    case 1: // Botão direito
                        // Toggle visibilidade
                        element.setEnabled(!element.isEnabled());
                        break;
                    case 2: // Botão do meio
                        // Remove o elemento
                        hudElements.remove(element);
                        break;
                }
                return;
            }
        }
        
        // Se clicou fora de todos os elementos
        if (button == 0) {
            selectedElement = null;
            draggingElement = null;
        }
    }
    
    /**
     * Método chamado ao mover o mouse
     */
    public void onMouseMove(int mouseX, int mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        
        if (!hudEnabled || !editingMode || draggingElement == null) return;
        
        // Move o elemento
        draggingElement.setX(draggingElement.getX() + (mouseX - this.mouseX));
        draggingElement.setY(draggingElement.getY() + (mouseY - this.mouseY));
    }
    
    /**
     * Método chamado ao pressionar uma tecla
     */
    public void onKeyPress(int keyCode) {
        if (!hudEnabled) return;
        
        // Verifica se é a tecla para entrar/sair do modo de edição
        if (keyCode == 29) { // Tecla 'P' (pode ser configurável)
            editingMode = !editingMode;
            if (!editingMode) {
                selectedElement = null;
                draggingElement = null;
                saveHUDLayout();
            }
        }
        
        // No modo de edição
        if (editingMode) {
            if (keyCode == 1) { // ESC
                editingMode = false;
                selectedElement = null;
                draggingElement = null;
            } else if (keyCode == 211) { // DELETE
                if (selectedElement != null) {
                    hudElements.remove(selectedElement);
                    selectedElement = null;
                }
            } else if (keyCode == 19) { // R - Redimensionar
                if (selectedElement != null) {
                    selectedElement.setWidth(selectedElement.getWidth() + 10);
                    selectedElement.setHeight(selectedElement.getHeight() + 5);
                }
            } else if (keyCode == 20) { // T - Reduzir tamanho
                if (selectedElement != null) {
                    selectedElement.setWidth(Math.max(10, selectedElement.getWidth() - 10));
                    selectedElement.setHeight(Math.max(10, selectedElement.getHeight() - 5));
                }
            }
        }
    }
    
    /**
     * Salva o layout da HUD
     */
    public void saveHUDLayout() {
        ClientUtils.logInfo("Salvando layout da HUD...");
        // Em implementação real, isso salvaria em um arquivo JSON
    }
    
    /**
     * Carrega o layout da HUD
     */
    public void loadHUDLayout() {
        ClientUtils.logInfo("Carregando layout da HUD...");
        // Em implementação real, isso carregaria de um arquivo JSON
    }
    
    /**
     * Adiciona um elemento à HUD
     */
    public void addHUDElement(HUDElement element) {
        hudElements.add(element);
    }
    
    /**
     * Remove um elemento da HUD
     */
    public void removeHUDElement(HUDElement element) {
        hudElements.remove(element);
    }
    
    /**
     * Remove um elemento da HUD pelo nome
     */
    public void removeHUDElement(String name) {
        hudElements.removeIf(element -> element.getName().equalsIgnoreCase(name));
    }
    
    /**
     * Obtém um elemento da HUD pelo nome
     */
    public HUDElement getHUDElement(String name) {
        for (HUDElement element : hudElements) {
            if (element.getName().equalsIgnoreCase(name)) {
                return element;
            }
        }
        return null;
    }
    
    /**
     * Obtém todos os elementos da HUD
     */
    public List<HUDElement> getHUDElements() {
        return new ArrayList<>(hudElements);
    }
    
    /**
     * Alternar o estado da HUD
     */
    public void toggleHUD() {
        hudEnabled = !hudEnabled;
    }
    
    /**
     * Alternar o modo de edição
     */
    public void toggleEditingMode() {
        editingMode = !editingMode;
        if (!editingMode) {
            selectedElement = null;
            draggingElement = null;
            saveHUDLayout();
        }
    }
    
    // Getters e Setters
    public boolean isHudEnabled() {
        return hudEnabled;
    }
    
    public void setHudEnabled(boolean hudEnabled) {
        this.hudEnabled = hudEnabled;
    }
    
    public boolean isEditingMode() {
        return editingMode;
    }
    
    public void setEditingMode(boolean editingMode) {
        this.editingMode = editingMode;
    }
    
    public HUDElement getSelectedElement() {
        return selectedElement;
    }
    
    public void setSelectedElement(HUDElement selectedElement) {
        this.selectedElement = selectedElement;
    }
    
    public int getMouseX() {
        return mouseX;
    }
    
    public int getMouseY() {
        return mouseY;
    }
}
