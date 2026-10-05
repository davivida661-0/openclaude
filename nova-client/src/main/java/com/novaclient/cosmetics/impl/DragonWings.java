package com.novaclient.cosmetics.impl;

import com.novaclient.cosmetics.Cosmetic;
import com.novaclient.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Cosmético Dragon Wings - Asas de dragão animadas
 */
public class DragonWings extends Cosmetic {
    
    private static final Minecraft mc = Minecraft.getMinecraft();
    
    // Texturas das asas
    private static final ResourceLocation DRAGON_WINGS_TEXTURE = new ResourceLocation("nova:textures/cosmetics/wings/dragon.png");
    private static final ResourceLocation FIRE_DRAGON_WINGS_TEXTURE = new ResourceLocation("nova:textures/cosmetics/wings/fire_dragon.png");
    private static final ResourceLocation ICE_DRAGON_WINGS_TEXTURE = new ResourceLocation("nova:textures/cosmetics/wings/ice_dragon.png");
    
    // Configurações da animação
    private float flapSpeed = 0.8f;
    private float flapIntensity = 0.5f;
    private float wingSize = 1.0f;
    
    // Estado da animação
    private float animationTimer = 0f;
    private float flapAngle = 0f;
    
    // Tipo de dragão
    private DragonType dragonType = DragonType.RED;
    
    public DragonWings(String id, String name, String description, CosmeticType type, CosmeticRarity rarity) {
        super(id, name, description, type, rarity);
        
        // Define a textura com base no tipo
        switch (id) {
            case "fire_dragon_wings":
                setTexturePath("nova:textures/cosmetics/wings/fire_dragon.png");
                dragonType = DragonType.FIRE;
                break;
            case "ice_dragon_wings":
                setTexturePath("nova:textures/cosmetics/wings/ice_dragon.png");
                dragonType = DragonType.ICE;
                break;
            default:
                setTexturePath("nova:textures/cosmetics/wings/dragon.png");
                dragonType = DragonType.RED;
        }
    }
    
    @Override
    public void onTick() {
        // Atualiza o timer de animação
        animationTimer += flapSpeed * 0.05f;
        
        // Calcula o ângulo de batida das asas
        flapAngle = MathHelper.sin(animationTimer) * flapIntensity;
        
        // Mantém o timer dentro de um range
        if (animationTimer > Math.PI * 2) {
            animationTimer -= Math.PI * 2;
        }
    }
    
    @Override
    public void onRender(AbstractClientPlayer player, float partialTicks) {
        if (!isEnabled() || !isUnlocked() || !isEquipped()) return;
        
        // Obtém a textura
        ResourceLocation texture = getWingsTexture();
        if (texture == null) return;
        
        // Salva o estado OpenGL
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        
        try {
            // Configura o blending
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
            
            // Configura a textura
            mc.renderEngine.bindTexture(texture);
            
            // Aplica a transformação com base no jogador
            applyPlayerTransformations(player, partialTicks);
            
            // Desenha as asas
            drawWings(player, partialTicks);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Restaura o estado OpenGL
            GlStateManager.popAttrib();
            GlStateManager.popMatrix();
        }
    }
    
    @Override
    public void onRenderGUI(int x, int y, float scale) {
        // Renderiza as asas na GUI (para visualização)
        ResourceLocation texture = getWingsTexture();
        if (texture == null) return;
        
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(scale, scale, scale);
        
        try {
            mc.renderEngine.bindTexture(texture);
            
            // Desenha as asas em miniatura
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(0, 0, 0).tex(0, 0).endVertex();
            worldRenderer.pos(0, 60, 0).tex(0, 1).endVertex();
            worldRenderer.pos(80, 60, 0).tex(1, 1).endVertex();
            worldRenderer.pos(80, 0, 0).tex(1, 0).endVertex();
            tessellator.draw();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            GlStateManager.popMatrix();
        }
    }
    
    /**
     * Aplica as transformações com base no jogador
     */
    private void applyPlayerTransformations(AbstractClientPlayer player, float partialTicks) {
        // Calcula a posição do jogador
        double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
        
        // Calcula a rotação
        float rotationYaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks;
        float rotationPitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;
        
        // Aplica a transformação
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(-rotationYaw, 0, 1, 0);
        GlStateManager.rotate(rotationPitch, 1, 0, 0);
    }
    
    /**
     * Desenha as asas
     */
    private void drawWings(AbstractClientPlayer player, float partialTicks) {
        // Tamanho das asas
        float width = 1.0f * wingSize;
        float height = 1.5f * wingSize;
        
        // Posição das asas (nas costas do jogador)
        float yOffset = 0.2f;
        float zOffset = -0.3f;
        
        // Desenha a asa esquerda
        drawWing(-width / 2, yOffset, zOffset, width, height, flapAngle, true);
        
        // Desenha a asa direita
        drawWing(width / 2, yOffset, zOffset, width, height, -flapAngle, false);
    }
    
    /**
     * Desenha uma asa individual
     */
    private void drawWing(float xOffset, float yOffset, float zOffset, float width, float height, float flapAngle, boolean isLeft) {
        // Aplica a transformação da asa
        GlStateManager.pushMatrix();
        GlStateManager.translate(xOffset, yOffset, zOffset);
        
        // Aplica o ângulo de batida
        if (isLeft) {
            GlStateManager.rotate(flapAngle * 45f, 0, 0, 1);
        } else {
            GlStateManager.rotate(-flapAngle * 45f, 0, 0, 1);
        }
        
        // Aplica a inclinação para frente
        GlStateManager.rotate(-15f, 1, 0, 0);
        
        // Desenha a asa
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        
        // Coordenadas da asa
        float halfWidth = width / 2;
        
        // Vértices da asa
        worldRenderer.pos(-halfWidth, 0, 0).tex(0, 0).endVertex();
        worldRenderer.pos(-halfWidth, height, 0).tex(0, 1).endVertex();
        worldRenderer.pos(halfWidth, height, 0).tex(1, 1).endVertex();
        worldRenderer.pos(halfWidth, 0, 0).tex(1, 0).endVertex();
        
        tessellator.draw();
        
        GlStateManager.popMatrix();
    }
    
    /**
     * Obtém a textura das asas com base no tipo
     */
    private ResourceLocation getWingsTexture() {
        switch (dragonType) {
            case FIRE:
                return FIRE_DRAGON_WINGS_TEXTURE;
            case ICE:
                return ICE_DRAGON_WINGS_TEXTURE;
            default:
                return DRAGON_WINGS_TEXTURE;
        }
    }
    
    // Getters e Setters
    public float getFlapSpeed() {
        return flapSpeed;
    }
    
    public void setFlapSpeed(float flapSpeed) {
        this.flapSpeed = flapSpeed;
    }
    
    public float getFlapIntensity() {
        return flapIntensity;
    }
    
    public void setFlapIntensity(float flapIntensity) {
        this.flapIntensity = flapIntensity;
    }
    
    public float getWingSize() {
        return wingSize;
    }
    
    public void setWingSize(float wingSize) {
        this.wingSize = wingSize;
    }
    
    public DragonType getDragonType() {
        return dragonType;
    }
    
    public void setDragonType(DragonType dragonType) {
        this.dragonType = dragonType;
    }
    
    /**
     * Tipos de dragão
     */
    public enum DragonType {
        RED,
        FIRE,
        ICE,
        BLUE,
        GREEN,
        BLACK
    }
}
