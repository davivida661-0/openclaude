package com.novaclient.gui.clickgui;

import com.novaclient.NovaClient;
import com.novaclient.gui.clickgui.components.CategoryButton;
import com.novaclient.gui.clickgui.components.ModButton;
import com.novaclient.mods.Mod;
import com.novaclient.mods.ModManager;
import com.novaclient.utils.ClientUtils;
import com.novaclient.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * GUI principal do cliente (ClickGUI)
 * Interface gráfica para gerenciar os mods
 */
public class ClickGUI extends GuiScreen {
    
    private final Minecraft mc = NovaClient.getInstance().getMc();
    private final ModManager modManager = NovaClient.getInstance().getModManager();
    
    // Categorias
    private final List<CategoryButton> categories = new ArrayList<>();
    private CategoryButton selectedCategory = null;
    
    // Botões de mods
    private final List<ModButton> modButtons = new ArrayList<>();
    
    // Posição e tamanho
    private int guiX = 100;
    private int guiY = 50;
    private int guiWidth = 400;
    private int guiHeight = 300;
    
    // Estado
    private boolean dragging = false;
    private int dragX = 0;
    private int dragY = 0;
    
    // Animação
    private float animationProgress = 0f;
    
    public ClickGUI() {
        // Inicializa as categorias
        initializeCategories();
        
        // Inicializa os botões de mods
        updateModButtons();
    }
    
    /**
     * Inicializa as categorias da GUI
     */
    private void initializeCategories() {
        categories.clear();
        
        // Adiciona as categorias
        categories.add(new CategoryButton("PvP", 0, Mod.ModCategory.PVP));
        categories.add(new CategoryButton("Render", 1, Mod.ModCategory.RENDER));
        categories.add(new CategoryButton("BedWars", 2, Mod.ModCategory.BEDWARS));
        categories.add(new CategoryButton("Cosmetics", 3, Mod.ModCategory.COSMETICS));
        categories.add(new CategoryButton("Settings", 4, Mod.ModCategory.SETTINGS));
        
        // Define a categoria padrão
        if (!categories.isEmpty()) {
            selectedCategory = categories.get(0);
        }
    }
    
    /**
     * Atualiza os botões de mods com base na categoria selecionada
     */
    private void updateModButtons() {
        modButtons.clear();
        
        if (selectedCategory == null) return;
        
        // Obtém os mods da categoria selecionada
        List<Mod> mods = modManager.getModsByCategory(selectedCategory.getCategory());
        
        // Adiciona os botões de mods
        for (int i = 0; i < mods.size(); i++) {
            Mod mod = mods.get(i);
            modButtons.add(new ModButton(mod, 10, 30 + i * 25));
        }
    }
    
    @Override
    public void initGui() {
        super.initGui();
        
        // Centraliza a GUI
        ScaledResolution sr = new ScaledResolution(mc);
        guiX = (sr.getScaledWidth() - guiWidth) / 2;
        guiY = (sr.getScaledHeight() - guiHeight) / 2;
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Atualiza a animação
        animationProgress = ClientUtils.clamp01(animationProgress + partialTicks * 0.02f);
        
        // Desenha o background com blur
        drawBackground(mouseX, mouseY);
        
        // Desenha a GUI principal
        drawGUI(mouseX, mouseY);
        
        // Desenha o título
        drawTitle();
        
        // Desenha as categorias
        drawCategories(mouseX, mouseY);
        
        // Desenha os botões de mods
        drawModButtons(mouseX, mouseY);
        
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    /**
     * Desenha o background da GUI
     */
    private void drawBackground(int mouseX, int mouseY) {
        ScaledResolution sr = new ScaledResolution(mc);
        
        // Desenha o blur
        RenderUtils.drawBlur(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 5f);
        
        // Desenha o overlay escuro
        RenderUtils.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 0x80000000);
    }
    
    /**
     * Desenha a GUI principal
     */
    private void drawGUI(int mouseX, int mouseY) {
        // Desenha o background da GUI
        RenderUtils.drawRoundedRect(
            guiX, guiY, 
            guiX + guiWidth, guiY + guiHeight, 
            5f, 
            0xFF1A1A2E
        );
        
        // Desenha a borda
        RenderUtils.drawBorder(
            guiX, guiY, 
            guiX + guiWidth, guiY + guiHeight, 
            1f, 
            0xFF00FFFF
        );
    }
    
    /**
     * Desenha o título da GUI
     */
    private void drawTitle() {
        String title = "Nova Client v" + NovaClient.VERSION;
        int titleWidth = mc.fontRendererObj.getStringWidth(title);
        
        // Desenha o background do título
        RenderUtils.drawRect(
            guiX + 10, guiY + 10, 
            guiX + guiWidth - 10, guiY + 30, 
            0xFF000000
        );
        
        // Desenha o título
        mc.fontRendererObj.drawStringWithShadow(
            title, 
            guiX + (guiWidth - titleWidth) / 2, 
            guiY + 15, 
            0xFFFFFFFF
        );
    }
    
    /**
     * Desenha as categorias
     */
    private void drawCategories(int mouseX, int mouseY) {
        int categoryWidth = guiWidth / categories.size();
        
        for (int i = 0; i < categories.size(); i++) {
            CategoryButton category = categories.get(i);
            category.setX(guiX + i * categoryWidth);
            category.setY(guiY + 35);
            category.setWidth(categoryWidth);
            category.setHeight(20);
            category.setSelected(category == selectedCategory);
            category.draw(mouseX, mouseY);
        }
    }
    
    /**
     * Desenha os botões de mods
     */
    private void drawModButtons(int mouseX, int mouseY) {
        int startY = guiY + 60;
        
        for (ModButton button : modButtons) {
            button.setX(guiX + 10);
            button.setY(startY);
            button.setWidth(guiWidth - 20);
            button.draw(mouseX, mouseY);
            startY += 25;
        }
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        // Verifica se clicou na barra de título (para arrastar)
        if (mouseX >= guiX && mouseX <= guiX + guiWidth && 
            mouseY >= guiY && mouseY <= guiY + 30) {
            dragging = true;
            dragX = mouseX - guiX;
            dragY = mouseY - guiY;
            return;
        }
        
        // Verifica se clicou em uma categoria
        for (CategoryButton category : categories) {
            if (category.isHovered(mouseX, mouseY) && mouseButton == 0) {
                selectedCategory = category;
                updateModButtons();
                return;
            }
        }
        
        // Verifica se clicou em um botão de mod
        for (ModButton button : modButtons) {
            if (button.isHovered(mouseX, mouseY) && mouseButton == 0) {
                button.onClick(mouseX, mouseY, mouseButton);
                return;
            }
        }
        
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        dragging = false;
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (dragging) {
            guiX = mouseX - dragX;
            guiY = mouseY - dragY;
        }
        
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }
    
    @Override
    public void handleKeyboardInput() throws IOException {
        super.handleKeyboardInput();
        
        // Fecha a GUI com ESC
        if (isKeyPressed(1)) { // ESC
            mc.displayGuiScreen(null);
        }
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    // Getters e Setters
    public int getGuiX() {
        return guiX;
    }
    
    public void setGuiX(int guiX) {
        this.guiX = guiX;
    }
    
    public int getGuiY() {
        return guiY;
    }
    
    public void setGuiY(int guiY) {
        this.guiY = guiY;
    }
    
    public int getGuiWidth() {
        return guiWidth;
    }
    
    public void setGuiWidth(int guiWidth) {
        this.guiWidth = guiWidth;
    }
    
    public int getGuiHeight() {
        return guiHeight;
    }
    
    public void setGuiHeight(int guiHeight) {
        this.guiHeight = guiHeight;
    }
    
    public CategoryButton getSelectedCategory() {
        return selectedCategory;
    }
    
    public void setSelectedCategory(CategoryButton selectedCategory) {
        this.selectedCategory = selectedCategory;
        updateModButtons();
    }
}
