package com.novaclient.cosmetics.impl;

import com.novaclient.cosmetics.Cosmetic;
import com.novaclient.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
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
 * Cosmético Animated Cape - Cape animada
 * Renderiza uma cape animada nas costas do jogador
 */
public class AnimatedCape extends Cosmetic {
    
    private static final Minecraft mc = Minecraft.getMinecraft();
    
    // Texturas da cape
    private static final ResourceLocation DEFAULT_CAPE_TEXTURE = new ResourceLocation("nova:textures/cosmetics/capes/default.png");
    private static final ResourceLocation GALAXY_CAPE_TEXTURE = new ResourceLocation("nova:textures/cosmetics/capes/galaxy.png");
    private static final ResourceLocation FIRE_CAPE_TEXTURE = new ResourceLocation("nova:textures/cosmetics/capes/fire.png");
    
    // Configurações da animação
    private float waveSpeed = 0.5f;
    private float waveHeight = 0.1f;
    private float windSpeed = 0.3f;
    private float windStrength = 0.05f;
    
    // Estado da animação
    private float animationTimer = 0f;
    private float windTimer = 0f;
    
    // Tipo de animação
    private CapeAnimationType animationType = CapeAnimationType.WAVE;
    
    /**
     * Construtor da cape animada
     */
    public AnimatedCape(String id, String name, String description, CosmeticType type, CosmeticRarity rarity) {
        super(id, name, description, type, rarity);
        
        // Define as texturas com base no ID
        switch (id) {
            case "galaxy_cape":
                setTexturePath("nova:textures/cosmetics/capes/galaxy.png");
                animationType = CapeAnimationType.GALAXY;
                break;
            case "fire_cape":
                setTexturePath("nova:textures/cosmetics/capes/fire.png");
                animationType = CapeAnimationType.FIRE;
                waveSpeed = 0.8f;
                waveHeight = 0.15f;
                break;
            default:
                setTexturePath("nova:textures/cosmetics/capes/default.png");
                animationType = CapeAnimationType.WAVE;
        }
    }
    
    @Override
    public void onTick() {
        // Atualiza os timers de animação
        animationTimer += waveSpeed * 0.05f;
        windTimer += windSpeed * 0.02f;
        
        // Mantém os timers dentro de um range
        if (animationTimer > Math.PI * 2) {
            animationTimer -= Math.PI * 2;
        }
        
        if (windTimer > Math.PI * 2) {
            windTimer -= Math.PI * 2;
        }
    }
    
    @Override
    public void onRender(AbstractClientPlayer player, float partialTicks) {
        if (!isEnabled() || !isUnlocked() || !isEquipped()) return;
        
        // Obtém a textura da cape
        ResourceLocation texture = getCapeTexture();
        if (texture == null) return;
        
        // Salva o estado OpenGL
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        
        try {
            // Configura o blending para transparência
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
            
            // Configura a textura
            mc.renderEngine.bindTexture(texture);
            
            // Obtém a posição do jogador
            double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
            double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
            double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
            
            // Calcula a rotação com base na direção do jogador
            float rotationYaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks;
            float rotationPitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;
            
            // Aplica a animação
            applyCapeAnimation(player, partialTicks, x, y, z, rotationYaw, rotationPitch);
            
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
        // Renderiza a cape na GUI (para visualização)
        ResourceLocation texture = getCapeTexture();
        if (texture == null) return;
        
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(scale, scale, scale);
        
        try {
            mc.renderEngine.bindTexture(texture);
            
            // Desenha a cape em miniatura
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(0, 0, 0).tex(0, 0).endVertex();
            worldRenderer.pos(0, 60, 0).tex(0, 1).endVertex();
            worldRenderer.pos(40, 60, 0).tex(1, 1).endVertex();
            worldRenderer.pos(40, 0, 0).tex(1, 0).endVertex();
            tessellator.draw();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            GlStateManager.popMatrix();
        }
    }
    
    /**
     * Aplica a animação da cape
     */
    private void applyCapeAnimation(AbstractClientPlayer player, float partialTicks, 
                                    double x, double y, double z, float rotationYaw, float rotationPitch) {
        
        // Calcula os vértices da cape com animação
        float[] vertices = calculateCapeVertices(player, partialTicks);
        
        // Desenha a cape
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        
        // Lado esquerdo superior
        worldRenderer.pos(vertices[0], vertices[1], vertices[2]).tex(0, 0).endVertex();
        // Lado esquerdo inferior
        worldRenderer.pos(vertices[3], vertices[4], vertices[5]).tex(0, 1).endVertex();
        // Lado direito inferior
        worldRenderer.pos(vertices[6], vertices[7], vertices[8]).tex(1, 1).endVertex();
        // Lado direito superior
        worldRenderer.pos(vertices[9], vertices[10], vertices[11]).tex(1, 0).endVertex();
        
        tessellator.draw();
    }
    
    /**
     * Calcula os vértices da cape com animação
     */
    private float[] calculateCapeVertices(AbstractClientPlayer player, float partialTicks) {
        float[] vertices = new float[12];
        
        // Tamanho da cape
        float capeWidth = 1.0f;
        float capeHeight = 1.8f;
        
        // Posição base (nas costas do jogador)
        double baseX = 0;
        double baseY = 0.2;
        double baseZ = 0;
        
        // Calcula a animação com base no tipo
        switch (animationType) {
            case WAVE:
                calculateWaveAnimation(vertices, baseX, baseY, baseZ, capeWidth, capeHeight);
                break;
            case GALAXY:
                calculateGalaxyAnimation(vertices, baseX, baseY, baseZ, capeWidth, capeHeight);
                break;
            case FIRE:
                calculateFireAnimation(vertices, baseX, baseY, baseZ, capeWidth, capeHeight);
                break;
            default:
                calculateWaveAnimation(vertices, baseX, baseY, baseZ, capeWidth, capeHeight);
        }
        
        return vertices;
    }
    
    /**
     * Animação de onda (padrão)
     */
    private void calculateWaveAnimation(float[] vertices, double baseX, double baseY, double baseZ, 
                                        float capeWidth, float capeHeight) {
        // Vértices superiores
        vertices[0] = (float) (baseX - capeWidth / 2);
        vertices[1] = (float) (baseY + capeHeight);
        vertices[2] = (float) baseZ;
        
        vertices[9] = (float) (baseX + capeWidth / 2);
        vertices[10] = (float) (baseY + capeHeight);
        vertices[11] = (float) baseZ;
        
        // Vértices inferiores com animação de onda
        float waveOffset = MathHelper.sin(animationTimer) * waveHeight;
        float windOffset = MathHelper.sin(windTimer) * windStrength;
        
        vertices[3] = (float) (baseX - capeWidth / 2 + windOffset);
        vertices[4] = (float) (baseY + waveOffset);
        vertices[5] = (float) (baseZ + 0.5f);
        
        vertices[6] = (float) (baseX + capeWidth / 2 + windOffset);
        vertices[7] = (float) (baseY + waveOffset);
        vertices[8] = (float) (baseZ + 0.5f);
    }
    
    /**
     * Animação de galáxia (movimento fluido)
     */
    private void calculateGalaxyAnimation(float[] vertices, double baseX, double baseY, double baseZ, 
                                          float capeWidth, float capeHeight) {
        // Vértices superiores
        vertices[0] = (float) (baseX - capeWidth / 2);
        vertices[1] = (float) (baseY + capeHeight);
        vertices[2] = (float) baseZ;
        
        vertices[9] = (float) (baseX + capeWidth / 2);
        vertices[10] = (float) (baseY + capeHeight);
        vertices[11] = (float) baseZ;
        
        // Vértices inferiores com movimento circular
        float radius = 0.3f;
        float angle = animationTimer * 2;
        
        vertices[3] = (float) (baseX - capeWidth / 2 + MathHelper.cos(angle) * radius);
        vertices[4] = (float) (baseY + MathHelper.sin(angle) * waveHeight);
        vertices[5] = (float) (baseZ + 0.5f + MathHelper.sin(angle * 0.5f) * radius);
        
        vertices[6] = (float) (baseX + capeWidth / 2 + MathHelper.cos(angle + MathHelper.PI) * radius);
        vertices[7] = (float) (baseY + MathHelper.sin(angle + MathHelper.PI) * waveHeight);
        vertices[8] = (float) (baseZ + 0.5f + MathHelper.sin(angle * 0.5f + MathHelper.PI) * radius);
    }
    
    /**
     * Animação de fogo (movimento caótico)
     */
    private void calculateFireAnimation(float[] vertices, double baseX, double baseY, double baseZ, 
                                        float capeWidth, float capeHeight) {
        // Vértices superiores
        vertices[0] = (float) (baseX - capeWidth / 2);
        vertices[1] = (float) (baseY + capeHeight);
        vertices[2] = (float) baseZ;
        
        vertices[9] = (float) (baseX + capeWidth / 2);
        vertices[10] = (float) (baseY + capeHeight);
        vertices[11] = (float) baseZ;
        
        // Vértices inferiores com movimento aleatório
        float randomOffset1 = (float) (Math.random() * 0.1f - 0.05f);
        float randomOffset2 = (float) (Math.random() * 0.1f - 0.05f);
        float waveOffset = MathHelper.sin(animationTimer * 3) * waveHeight * 1.5f;
        
        vertices[3] = (float) (baseX - capeWidth / 2 + randomOffset1);
        vertices[4] = (float) (baseY + waveOffset + randomOffset2);
        vertices[5] = (float) (baseZ + 0.5f);
        
        vertices[6] = (float) (baseX + capeWidth / 2 - randomOffset1);
        vertices[7] = (float) (baseY + waveOffset - randomOffset2);
        vertices[8] = (float) (baseZ + 0.5f);
    }
    
    /**
     * Obtém a textura da cape com base no ID
     */
    private ResourceLocation getCapeTexture() {
        switch (getId()) {
            case "galaxy_cape":
                return GALAXY_CAPE_TEXTURE;
            case "fire_cape":
                return FIRE_CAPE_TEXTURE;
            default:
                return DEFAULT_CAPE_TEXTURE;
        }
    }
    
    // Getters e Setters para configurações de animação
    public float getWaveSpeed() {
        return waveSpeed;
    }
    
    public void setWaveSpeed(float waveSpeed) {
        this.waveSpeed = waveSpeed;
    }
    
    public float getWaveHeight() {
        return waveHeight;
    }
    
    public void setWaveHeight(float waveHeight) {
        this.waveHeight = waveHeight;
    }
    
    public float getWindSpeed() {
        return windSpeed;
    }
    
    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }
    
    public float getWindStrength() {
        return windStrength;
    }
    
    public void setWindStrength(float windStrength) {
        this.windStrength = windStrength;
    }
    
    public CapeAnimationType getAnimationType() {
        return animationType;
    }
    
    public void setAnimationType(CapeAnimationType animationType) {
        this.animationType = animationType;
    }
    
    /**
     * Tipos de animação da cape
     */
    public enum CapeAnimationType {
        WAVE,       // Animação de onda
        GALAXY,     // Animação de galáxia
        FIRE,       // Animação de fogo
        STATIC      // Sem animação
    }
}
