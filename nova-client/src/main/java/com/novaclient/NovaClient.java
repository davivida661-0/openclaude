package com.novaclient;

import com.novaclient.config.ConfigManager;
import com.novaclient.cosmetics.CosmeticManager;
import com.novaclient.events.EventBus;
import com.novaclient.gui.clickgui.ClickGUI;
import com.novaclient.gui.hud.HUDManager;
import com.novaclient.mods.ModManager;
import com.novaclient.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.opengl.Display;

/**
 * Classe principal do Nova Client
 * Gerencia a inicialização e o ciclo de vida do cliente
 */
public class NovaClient {
    
    public static final String NAME = "Nova Client";
    public static final String VERSION = "1.0.0";
    public static final String MINECRAFT_VERSION = "1.8.9";
    
    private static NovaClient instance;
    private final Minecraft mc = Minecraft.getMinecraft();
    
    // Managers
    private ModManager modManager;
    private CosmeticManager cosmeticManager;
    private ConfigManager configManager;
    private HUDManager hudManager;
    private EventBus eventBus;
    
    // GUI
    private ClickGUI clickGUI;
    
    // Estado do cliente
    private boolean initialized = false;
    
    /**
     * Construtor privado para singleton
     */
    private NovaClient() {
        instance = this;
    }
    
    /**
     * Obtém a instância do cliente
     */
    public static NovaClient getInstance() {
        if (instance == null) {
            new NovaClient();
        }
        return instance;
    }
    
    /**
     * Inicializa o cliente
     */
    public void init() {
        if (initialized) return;
        
        ClientUtils.logInfo("Inicializando " + NAME + " v" + VERSION);
        
        // Inicializa o barramento de eventos
        this.eventBus = new EventBus();
        
        // Inicializa os gerenciadores
        this.configManager = new ConfigManager();
        this.modManager = new ModManager();
        this.cosmeticManager = new CosmeticManager();
        this.hudManager = new HUDManager();
        
        // Inicializa a GUI
        this.clickGUI = new ClickGUI();
        
        // Carrega as configurações
        configManager.loadConfigs();
        
        // Registra os mods
        modManager.registerMods();
        
        // Registra os cosméticos
        cosmeticManager.loadCosmetics();
        
        // Define o título da janela
        Display.setTitle(NAME + " v" + VERSION + " | Minecraft " + MINECRAFT_VERSION);
        
        ClientUtils.logInfo("Nova Client inicializado com sucesso!");
        initialized = true;
    }
    
    /**
     * Método chamado a cada tick do jogo
     */
    public void onTick() {
        if (!initialized) return;
        
        modManager.onTick();
        cosmeticManager.onTick();
        hudManager.onTick();
    }
    
    /**
     * Método chamado a cada frame de renderização
     */
    public void onRender() {
        if (!initialized) return;
        
        modManager.onRender();
        cosmeticManager.onRender();
        hudManager.onRender();
    }
    
    /**
     * Abre a GUI do cliente
     */
    public void openClickGUI() {
        if (mc.currentScreen == null) {
            mc.displayGuiScreen(clickGUI);
        }
    }
    
    /**
     * Método chamado ao fechar o jogo
     */
    public void shutdown() {
        ClientUtils.logInfo("Desligando Nova Client...");
        
        configManager.saveConfigs();
        modManager.shutdown();
        cosmeticManager.shutdown();
        
        ClientUtils.logInfo("Nova Client desligado!");
    }
    
    // Getters
    public ModManager getModManager() {
        return modManager;
    }
    
    public CosmeticManager getCosmeticManager() {
        return cosmeticManager;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public HUDManager getHudManager() {
        return hudManager;
    }
    
    public EventBus getEventBus() {
        return eventBus;
    }
    
    public ClickGUI getClickGUI() {
        return clickGUI;
    }
    
    public Minecraft getMc() {
        return mc;
    }
    
    public boolean isInitialized() {
        return initialized;
    }
}
