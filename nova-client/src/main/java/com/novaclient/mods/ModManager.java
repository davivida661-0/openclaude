package com.novaclient.mods;

import com.novaclient.NovaClient;
import com.novaclient.mods.pvp.*;
import com.novaclient.mods.render.*;
import com.novaclient.mods.bedwars.*;
import com.novaclient.mods.cosmetics.CosmeticsMod;
import com.novaclient.utils.ClientUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Gerenciador de mods do cliente
 * Responsável por registrar, gerenciar e executar todos os mods
 */
public class ModManager {
    
    private final List<Mod> mods = new ArrayList<>();
    private final NovaClient client = NovaClient.getInstance();
    
    /**
     * Registra todos os mods do cliente
     */
    public void registerMods() {
        ClientUtils.logInfo("Registrando mods...");
        
        // Mods de PvP
        registerMod(new KeystrokesMod());
        registerMod(new CPSCounterMod());
        registerMod(new FPSCounterMod());
        registerMod(new PingCounterMod());
        registerMod(new ArmorStatusMod());
        registerMod(new PotionStatusMod());
        registerMod(new CoordinatesMod());
        registerMod(new ToggleSprintMod());
        registerMod(new ToggleSneakMod());
        registerMod(new FullBrightMod());
        registerMod(new ClearGlassMod());
        registerMod(new NoHurtCamMod());
        registerMod(new HitColorMod());
        
        // Mods de Render
        registerMod(new OneSevenAnimationsMod());
        registerMod(new OneSevenBlockhitMod());
        registerMod(new OldSneakMod());
        registerMod(new OldRodMod());
        registerMod(new ChatMod());
        registerMod(new ScoreboardMod());
        registerMod(new TabMod());
        registerMod(new NameTagMod());
        registerMod(new PackDisplayMod());
        registerMod(new BossBarMod());
        
        // Mods de BedWars
        registerMod(new BedWarsOverlayMod());
        registerMod(new SessionCounterMod());
        registerMod(new AutoGGMod());
        registerMod(new AutoPlayAgainMod());
        registerMod(new AutoTipMod());
        registerMod(new InvisiblePlayerESPMod());
        registerMod(new NickHiderMod());
        registerMod(new LowHealthWarningMod());
        registerMod(new FireballJumpTimerMod());
        
        // Mods de Cosméticos
        registerMod(new CosmeticsMod());
        
        ClientUtils.logInfo("Mods registrados: " + mods.size());
    }
    
    /**
     * Registra um mod individual
     */
    public void registerMod(Mod mod) {
        mods.add(mod);
        ClientUtils.logDebug("Mod registrado: " + mod.getName());
    }
    
    /**
     * Remove um mod pelo nome
     */
    public void unregisterMod(String name) {
        mods.removeIf(mod -> mod.getName().equalsIgnoreCase(name));
    }
    
    /**
     * Obtém um mod pelo nome
     */
    public Mod getMod(String name) {
        for (Mod mod : mods) {
            if (mod.getName().equalsIgnoreCase(name)) {
                return mod;
            }
        }
        return null;
    }
    
    /**
     * Obtém todos os mods
     */
    public List<Mod> getMods() {
        return new ArrayList<>(mods);
    }
    
    /**
     * Obtém todos os mods de uma categoria específica
     */
    public List<Mod> getModsByCategory(Mod.ModCategory category) {
        return mods.stream()
                .filter(mod -> mod.getCategory() == category)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtém todos os mods habilitados
     */
    public List<Mod> getEnabledMods() {
        return mods.stream()
                .filter(Mod::isEnabled)
                .collect(Collectors.toList());
    }
    
    /**
     * Método chamado a cada tick do jogo
     */
    public void onTick() {
        for (Mod mod : mods) {
            if (mod.isEnabled()) {
                try {
                    mod.onTick();
                } catch (Exception e) {
                    ClientUtils.logError("Erro no mod " + mod.getName() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Método chamado a cada frame de renderização
     */
    public void onRender() {
        for (Mod mod : mods) {
            if (mod.isEnabled()) {
                try {
                    mod.onRender();
                } catch (Exception e) {
                    ClientUtils.logError("Erro ao renderizar mod " + mod.getName() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Método chamado ao renderizar a GUI
     */
    public void onRenderGUI(int mouseX, int mouseY) {
        for (Mod mod : mods) {
            if (mod.isEnabled()) {
                try {
                    mod.onRenderGUI(mouseX, mouseY);
                } catch (Exception e) {
                    ClientUtils.logError("Erro ao renderizar GUI do mod " + mod.getName() + ": " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Método chamado ao clicar com o mouse
     */
    public void onMouseClick(int mouseX, int mouseY, int button) {
        for (Mod mod : mods) {
            if (mod.isEnabled()) {
                try {
                    mod.onMouseClick(mouseX, mouseY, button);
                } catch (Exception e) {
                    ClientUtils.logError("Erro no clique do mouse do mod " + mod.getName() + ": " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Método chamado ao soltar o botão do mouse
     */
    public void onMouseRelease(int mouseX, int mouseY, int button) {
        for (Mod mod : mods) {
            if (mod.isEnabled()) {
                try {
                    mod.onMouseRelease(mouseX, mouseY, button);
                } catch (Exception e) {
                    ClientUtils.logError("Erro ao soltar mouse do mod " + mod.getName() + ": " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Método chamado ao pressionar uma tecla
     */
    public void onKeyPress(int keyCode) {
        // Verifica se a tecla corresponde a algum mod
        for (Mod mod : mods) {
            if (mod.getKeyBind() == keyCode) {
                mod.toggle();
                return;
            }
        }
        
        // Chama o evento para todos os mods
        for (Mod mod : mods) {
            if (mod.isEnabled()) {
                try {
                    mod.onKeyPress(keyCode);
                } catch (Exception e) {
                    ClientUtils.logError("Erro na tecla do mod " + mod.getName() + ": " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Desliga todos os mods
     */
    public void shutdown() {
        for (Mod mod : mods) {
            if (mod.isEnabled()) {
                mod.onDisable();
            }
        }
        mods.clear();
    }
    
    /**
     * Conta o número de mods registrados
     */
    public int getModCount() {
        return mods.size();
    }
    
    /**
     * Conta o número de mods habilitados
     */
    public int getEnabledModCount() {
        return (int) mods.stream().filter(Mod::isEnabled).count();
    }
}
