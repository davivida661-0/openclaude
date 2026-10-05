package com.novaclient.mods.bedwars;

import com.novaclient.cosmetics.CosmeticManager;
import com.novaclient.gui.clickgui.ClickGUI;
import com.novaclient.mods.Mod;
import com.novaclient.NovaClient;
import com.novaclient.utils.ClientUtils;

/**
 * Mod de Cosméticos - Permite gerenciar cosméticos no jogo
 */
public class CosmeticsMod extends Mod {
    
    private final CosmeticManager cosmeticManager;
    
    // Configurações
    private boolean showOwnCosmetics = true;
    private boolean showOtherPlayersCosmetics = true;
    
    public CosmeticsMod() {
        super("Cosmetics", "Gerencia cosméticos do jogador", ModCategory.COSMETICS);
        this.cosmeticManager = NovaClient.getInstance().getCosmeticManager();
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        cosmeticManager.setCosmeticsEnabled(true);
        cosmeticManager.setShowOwnCosmetics(showOwnCosmetics);
        cosmeticManager.setShowOtherPlayersCosmetics(showOtherPlayersCosmetics);
        ClientUtils.logInfo("Cosmetics mod habilitado");
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        cosmeticManager.setCosmeticsEnabled(false);
        ClientUtils.logInfo("Cosmetics mod desabilitado");
    }
    
    @Override
    public void onTick() {
        if (!isEnabled()) return;
        cosmeticManager.onTick();
    }
    
    @Override
    public void onRender() {
        if (!isEnabled()) return;
        cosmeticManager.onRender();
    }
    
    // Getters e Setters
    public boolean isShowOwnCosmetics() {
        return showOwnCosmetics;
    }
    
    public void setShowOwnCosmetics(boolean showOwnCosmetics) {
        this.showOwnCosmetics = showOwnCosmetics;
        if (isEnabled()) {
            cosmeticManager.setShowOwnCosmetics(showOwnCosmetics);
        }
    }
    
    public boolean isShowOtherPlayersCosmetics() {
        return showOtherPlayersCosmetics;
    }
    
    public void setShowOtherPlayersCosmetics(boolean showOtherPlayersCosmetics) {
        this.showOtherPlayersCosmetics = showOtherPlayersCosmetics;
        if (isEnabled()) {
            cosmeticManager.setShowOtherPlayersCosmetics(showOtherPlayersCosmetics);
        }
    }
    
    public CosmeticManager getCosmeticManager() {
        return cosmeticManager;
    }
}
