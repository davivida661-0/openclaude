package com.novaclient.mixins;

import com.novaclient.NovaClient;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin para Minecraft
 * Permite modificar o comportamento principal do jogo
 */
@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    
    /**
     * Injetado no método startGame para inicializar o cliente
     */
    @Inject(method = "startGame", at = @At("RETURN"))
    private void onStartGame(CallbackInfo ci) {
        NovaClient.getInstance().init();
    }
    
    /**
     * Injetado no método runTick para executar lógica a cada tick
     */
    @Inject(method = "runTick", at = @At("RETURN"))
    private void onRunTick(CallbackInfo ci) {
        NovaClient client = NovaClient.getInstance();
        if (client.isInitialized()) {
            client.onTick();
        }
    }
    
    /**
     * Injetado no método shutdown para desligar o cliente
     */
    @Inject(method = "shutdown", at = @At("HEAD"))
    private void onShutdown(CallbackInfo ci) {
        NovaClient.getInstance().shutdown();
    }
}
