package com.novaclient.mods.render;

import com.novaclient.mixins.MixinHandler;
import com.novaclient.mods.Mod;
import com.novaclient.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;

/**
 * Mod 1.7 Animations - Animações de combate do Minecraft 1.7
 * Altera as animações de ataque, bloqueio e movimento para o estilo 1.7
 */
public class OneSevenAnimationsMod extends Mod {
    
    // Configurações
    private boolean oldHitAnimation = true;
    private boolean oldBlockAnimation = true;
    private boolean oldSwordAnimation = true;
    private boolean oldRodAnimation = true;
    private boolean oldBowAnimation = true;
    private boolean oldEatAnimation = true;
    private boolean oldDrinkAnimation = true;
    
    // Estado
    private boolean wasAttacking = false;
    private boolean wasUsingItem = false;
    private int attackTimer = 0;
    private int useItemTimer = 0;
    
    public OneSevenAnimationsMod() {
        super("1.7 Animations", "Animações de combate do Minecraft 1.7", ModCategory.RENDER);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        ClientUtils.logInfo("1.7 Animations habilitado");
        // Registra os mixins necessários
        MixinHandler.registerAnimationsMixin();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtils.logInfo("1.7 Animations desabilitado");
        // Desregistra os mixins
        MixinHandler.unregisterAnimationsMixin();
    }
    
    @Override
    public void onTick() {
        if (mc.thePlayer == null) return;
        
        EntityPlayerSP player = mc.thePlayer;
        
        // Atualiza o estado das animações
        boolean isAttacking = player.isSwingInProgress && player.swingProgressInt > 0;
        boolean isUsingItem = player.isUsingItem();
        
        // Detecta a transição de estados
        if (isAttacking && !wasAttacking) {
            // Começou a atacar
            attackTimer = 0;
        }
        
        if (isUsingItem && !wasUsingItem) {
            // Começou a usar item
            useItemTimer = 0;
        }
        
        // Incrementa os timers
        if (isAttacking) {
            attackTimer++;
        }
        
        if (isUsingItem) {
            useItemTimer++;
        }
        
        // Atualiza o estado anterior
        wasAttacking = isAttacking;
        wasUsingItem = isUsingItem;
        
        // Aplica as animações 1.7
        apply17Animations(player);
    }
    
    /**
     * Aplica as animações do estilo 1.7 ao jogador
     */
    private void apply17Animations(EntityPlayerSP player) {
        if (oldHitAnimation) {
            // Animação de hit 1.7 - mais rápida e abrupta
            if (player.isSwingInProgress) {
                // Reduz o tempo de animação de swing
                player.swingProgressInt = Math.min(player.swingProgressInt, 3);
            }
        }
        
        if (oldBlockAnimation) {
            // Animação de bloqueio 1.7
            if (player.isBlocking()) {
                // Ajusta a posição do braço para o estilo 1.7
                player.renderArmPitch = 0.0f;
                player.renderArmYaw = 0.0f;
            }
        }
        
        if (oldSwordAnimation) {
            // Animação de espada 1.7
            // No 1.7, a espada não se move tanto durante o swing
            if (player.isSwingInProgress) {
                float progress = (float) attackTimer / 6.0f;
                if (progress < 0.5f) {
                    // Primeira metade do swing - movimento rápido
                    player.renderArmYaw = -Math.sin(progress * Math.PI * 2) * 1.5f;
                } else {
                    // Segunda metade - retorno rápido
                    player.renderArmYaw = Math.sin((progress - 0.5f) * Math.PI * 2) * 1.5f;
                }
            }
        }
        
        if (oldRodAnimation) {
            // Animação de vara 1.7
            if (player.isUsingItem() && player.getHeldItem() != null && 
                player.getHeldItem().getItem().toString().contains("fishing_rod")) {
                // Animação mais simples para a vara
                player.renderArmPitch = -0.5f;
            }
        }
        
        if (oldBowAnimation) {
            // Animação de arco 1.7
            if (player.isUsingItem() && player.getHeldItem() != null && 
                player.getHeldItem().getItem().toString().contains("bow")) {
                // Animação de puxar o arco
                float progress = Math.min(1.0f, (float) useItemTimer / 20.0f);
                player.renderArmPitch = -Math.sin(progress * Math.PI) * 0.3f;
            }
        }
        
        if (oldEatAnimation) {
            // Animação de comer 1.7
            if (player.isEating()) {
                // Movimento mais suave ao comer
                player.renderArmPitch = Math.sin((float) useItemTimer / 10.0f) * 0.1f;
            }
        }
        
        if (oldDrinkAnimation) {
            // Animação de beber 1.7
            if (player.isDrinking()) {
                // Movimento mais suave ao beber
                player.renderArmPitch = -Math.sin((float) useItemTimer / 10.0f) * 0.1f;
            }
        }
    }
    
    /**
     * Verifica se o jogador está no meio de uma animação de ataque 1.7
     */
    public boolean is17AttackAnimationActive() {
        return attackTimer > 0 && attackTimer < 6;
    }
    
    /**
     * Verifica se o jogador está no meio de uma animação de uso de item 1.7
     */
    public boolean is17UseItemAnimationActive() {
        return useItemTimer > 0;
    }
    
    // Getters e Setters para configurações
    public boolean isOldHitAnimation() {
        return oldHitAnimation;
    }
    
    public void setOldHitAnimation(boolean oldHitAnimation) {
        this.oldHitAnimation = oldHitAnimation;
    }
    
    public boolean isOldBlockAnimation() {
        return oldBlockAnimation;
    }
    
    public void setOldBlockAnimation(boolean oldBlockAnimation) {
        this.oldBlockAnimation = oldBlockAnimation;
    }
    
    public boolean isOldSwordAnimation() {
        return oldSwordAnimation;
    }
    
    public void setOldSwordAnimation(boolean oldSwordAnimation) {
        this.oldSwordAnimation = oldSwordAnimation;
    }
    
    public boolean isOldRodAnimation() {
        return oldRodAnimation;
    }
    
    public void setOldRodAnimation(boolean oldRodAnimation) {
        this.oldRodAnimation = oldRodAnimation;
    }
    
    public boolean isOldBowAnimation() {
        return oldBowAnimation;
    }
    
    public void setOldBowAnimation(boolean oldBowAnimation) {
        this.oldBowAnimation = oldBowAnimation;
    }
    
    public boolean isOldEatAnimation() {
        return oldEatAnimation;
    }
    
    public void setOldEatAnimation(boolean oldEatAnimation) {
        this.oldEatAnimation = oldEatAnimation;
    }
    
    public boolean isOldDrinkAnimation() {
        return oldDrinkAnimation;
    }
    
    public void setOldDrinkAnimation(boolean oldDrinkAnimation) {
        this.oldDrinkAnimation = oldDrinkAnimation;
    }
}
