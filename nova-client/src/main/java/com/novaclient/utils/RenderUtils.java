package com.novaclient.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * Classe utilitária para renderização
 * Contém métodos para desenhar formas geométricas, texturas e efeitos visuais
 */
public class RenderUtils {
    
    private static final Minecraft mc = Minecraft.getMinecraft();
    
    /**
     * Desenha um retângulo preenchido
     * 
     * @param x Posição X
     * @param y Posição Y
     * @param x2 Posição X final
     * @param y2 Posição Y final
     * @param color Cor no formato ARGB (0xAARRGGBB)
     */
    public static void drawRect(float x, float y, float x2, float y2, int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        
        drawRect(x, y, x2, y2, red, green, blue, alpha);
    }
    
    /**
     * Desenha um retângulo preenchido com cores RGBA
     * 
     * @param x Posição X
     * @param y Posição Y
     * @param x2 Posição X final
     * @param y2 Posição Y final
     * @param red Componente vermelho (0.0 - 1.0)
     * @param green Componente verde (0.0 - 1.0)
     * @param blue Componente azul (0.0 - 1.0)
     * @param alpha Componente alpha (0.0 - 1.0)
     */
    public static void drawRect(float x, float y, float x2, float y2, float red, float green, float blue, float alpha) {
        if (x < x2) {
            float temp = x;
            x = x2;
            x2 = temp;
        }
        
        if (y < y2) {
            float temp = y;
            y = y2;
            y2 = temp;
        }
        
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.color(red, green, blue, alpha);
        
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x, y2, 0).endVertex();
        worldRenderer.pos(x2, y2, 0).endVertex();
        worldRenderer.pos(x2, y, 0).endVertex();
        worldRenderer.pos(x, y, 0).endVertex();
        tessellator.draw();
        
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    /**
     * Desenha um retângulo com gradiente vertical
     * 
     * @param x Posição X
     * @param y Posição Y
     * @param x2 Posição X final
     * @param y2 Posição Y final
     * @param startColor Cor inicial (ARGB)
     * @param endColor Cor final (ARGB)
     */
    public static void drawGradientRect(float x, float y, float x2, float y2, int startColor, int endColor) {
        float startAlpha = (float) (startColor >> 24 & 255) / 255.0F;
        float startRed = (float) (startColor >> 16 & 255) / 255.0F;
        float startGreen = (float) (startColor >> 8 & 255) / 255.0F;
        float startBlue = (float) (startColor & 255) / 255.0F;
        
        float endAlpha = (float) (endColor >> 24 & 255) / 255.0F;
        float endRed = (float) (endColor >> 16 & 255) / 255.0F;
        float endGreen = (float) (endColor >> 8 & 255) / 255.0F;
        float endBlue = (float) (endColor & 255) / 255.0F;
        
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(x2, y, 0).color(endRed, endGreen, endBlue, endAlpha).endVertex();
        worldRenderer.pos(x, y, 0).color(startRed, startGreen, startBlue, startAlpha).endVertex();
        worldRenderer.pos(x, y2, 0).color(startRed, startGreen, startBlue, startAlpha).endVertex();
        worldRenderer.pos(x2, y2, 0).color(endRed, endGreen, endBlue, endAlpha).endVertex();
        tessellator.draw();
        
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    /**
     * Desenha um retângulo com gradiente horizontal
     * 
     * @param x Posição X
     * @param y Posição Y
     * @param x2 Posição X final
     * @param y2 Posição Y final
     * @param leftColor Cor esquerda (ARGB)
     * @param rightColor Cor direita (ARGB)
     */
    public static void drawHorizontalGradientRect(float x, float y, float x2, float y2, int leftColor, int rightColor) {
        float leftAlpha = (float) (leftColor >> 24 & 255) / 255.0F;
        float leftRed = (float) (leftColor >> 16 & 255) / 255.0F;
        float leftGreen = (float) (leftColor >> 8 & 255) / 255.0F;
        float leftBlue = (float) (leftColor & 255) / 255.0F;
        
        float rightAlpha = (float) (rightColor >> 24 & 255) / 255.0F;
        float rightRed = (float) (rightColor >> 16 & 255) / 255.0F;
        float rightGreen = (float) (rightColor >> 8 & 255) / 255.0F;
        float rightBlue = (float) (rightColor & 255) / 255.0F;
        
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(x2, y, 0).color(rightRed, rightGreen, rightBlue, rightAlpha).endVertex();
        worldRenderer.pos(x, y, 0).color(leftRed, leftGreen, leftBlue, leftAlpha).endVertex();
        worldRenderer.pos(x, y2, 0).color(leftRed, leftGreen, leftBlue, leftAlpha).endVertex();
        worldRenderer.pos(x2, y2, 0).color(rightRed, rightGreen, rightBlue, rightAlpha).endVertex();
        tessellator.draw();
        
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    /**
     * Desenha uma borda ao redor de um retângulo
     * 
     * @param x Posição X
     * @param y Posição Y
     * @param x2 Posição X final
     * @param y2 Posição Y final
     * @param width Largura da borda
     * @param color Cor da borda (ARGB)
     */
    public static void drawBorder(float x, float y, float x2, float y2, float width, int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        
        drawBorder(x, y, x2, y2, width, red, green, blue, alpha);
    }
    
    /**
     * Desenha uma borda ao redor de um retângulo com cores RGBA
     * 
     * @param x Posição X
     * @param y Posição Y
     * @param x2 Posição X final
     * @param y2 Posição Y final
     * @param width Largura da borda
     * @param red Componente vermelho
     * @param green Componente verde
     * @param blue Componente azul
     * @param alpha Componente alpha
     */
    public static void drawBorder(float x, float y, float x2, float y2, float width, float red, float green, float blue, float alpha) {
        // Borda superior
        drawRect(x - width, y - width, x2 + width, y, red, green, blue, alpha);
        // Borda inferior
        drawRect(x - width, y2, x2 + width, y2 + width, red, green, blue, alpha);
        // Borda esquerda
        drawRect(x - width, y, x, y2, red, green, blue, alpha);
        // Borda direita
        drawRect(x2, y, x2 + width, y2, red, green, blue, alpha);
    }
    
    /**
     * Desenha um círculo preenchido
     * 
     * @param x Posição X do centro
     * @param y Posição Y do centro
     * @param radius Raio do círculo
     * @param color Cor do círculo (ARGB)
     */
    public static void drawCircle(float x, float y, float radius, int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        
        drawCircle(x, y, radius, red, green, blue, alpha);
    }
    
    /**
     * Desenha um círculo preenchido com cores RGBA
     */
    public static void drawCircle(float x, float y, float radius, float red, float green, float blue, float alpha) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.color(red, green, blue, alpha);
        
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        GL11.glVertex2f(x, y);
        
        for (int i = 0; i <= 360; i++) {
            float angle = (float) (i * Math.PI / 180.0);
            float dx = (float) (Math.cos(angle) * radius);
            float dy = (float) (Math.sin(angle) * radius);
            GL11.glVertex2f(x + dx, y + dy);
        }
        
        GL11.glEnd();
        
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    /**
     * Desenha um círculo vazio (apenas a borda)
     * 
     * @param x Posição X do centro
     * @param y Posição Y do centro
     * @param radius Raio do círculo
     * @param width Largura da borda
     * @param color Cor da borda (ARGB)
     */
    public static void drawCircleOutline(float x, float y, float radius, float width, int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        
        drawCircleOutline(x, y, radius, width, red, green, blue, alpha);
    }
    
    /**
     * Desenha um círculo vazio com cores RGBA
     */
    public static void drawCircleOutline(float x, float y, float radius, float width, float red, float green, float blue, float alpha) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.color(red, green, blue, alpha);
        
        GL11.glLineWidth(width);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        
        for (int i = 0; i <= 360; i++) {
            float angle = (float) (i * Math.PI / 180.0);
            float dx = (float) (Math.cos(angle) * radius);
            float dy = (float) (Math.sin(angle) * radius);
            GL11.glVertex2f(x + dx, y + dy);
        }
        
        GL11.glEnd();
        
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    /**
     * Desenha um retângulo arredondado
     * 
     * @param x Posição X
     * @param y Posição Y
     * @param x2 Posição X final
     * @param y2 Posição Y final
     * @param radius Raio dos cantos
     * @param color Cor do retângulo (ARGB)
     */
    public static void drawRoundedRect(float x, float y, float x2, float y2, float radius, int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        
        drawRoundedRect(x, y, x2, y2, radius, red, green, blue, alpha);
    }
    
    /**
     * Desenha um retângulo arredondado com cores RGBA
     */
    public static void drawRoundedRect(float x, float y, float x2, float y2, float radius, float red, float green, float blue, float alpha) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.color(red, green, blue, alpha);
        
        GL11.glBegin(GL11.GL_QUADS);
        
        // Cantos arredondados
        for (int i = 0; i < 360; i += 90) {
            float angle = (float) (i * Math.PI / 180.0);
            float dx = (float) (Math.cos(angle) * radius);
            float dy = (float) (Math.sin(angle) * radius);
            
            // Canto superior esquerdo
            GL11.glVertex2f(x + radius + dx, y + radius + dy);
            // Canto inferior esquerdo
            GL11.glVertex2f(x + radius + dx, y2 - radius + dy);
            // Canto inferior direito
            GL11.glVertex2f(x2 - radius + dx, y2 - radius + dy);
            // Canto superior direito
            GL11.glVertex2f(x2 - radius + dx, y + radius + dy);
        }
        
        GL11.glEnd();
        
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    /**
     * Desenha uma linha
     * 
     * @param x1 Posição X inicial
     * @param y1 Posição Y inicial
     * @param x2 Posição X final
     * @param y2 Posição Y final
     * @param width Largura da linha
     * @param color Cor da linha (ARGB)
     */
    public static void drawLine(float x1, float y1, float x2, float y2, float width, int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        
        drawLine(x1, y1, x2, y2, width, red, green, blue, alpha);
    }
    
    /**
     * Desenha uma linha com cores RGBA
     */
    public static void drawLine(float x1, float y1, float x2, float y2, float width, float red, float green, float blue, float alpha) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.color(red, green, blue, alpha);
        
        GL11.glLineWidth(width);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x2, y2);
        GL11.glEnd();
        
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    /**
     * Desenha um triângulo
     * 
     * @param x1 Posição X do primeiro vértice
     * @param y1 Posição Y do primeiro vértice
     * @param x2 Posição X do segundo vértice
     * @param y2 Posição Y do segundo vértice
     * @param x3 Posição X do terceiro vértice
     * @param y3 Posição Y do terceiro vértice
     * @param color Cor do triângulo (ARGB)
     */
    public static void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3, int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        
        drawTriangle(x1, y1, x2, y2, x3, y3, red, green, blue, alpha);
    }
    
    /**
     * Desenha um triângulo com cores RGBA
     */
    public static void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3, float red, float green, float blue, float alpha) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.color(red, green, blue, alpha);
        
        GL11.glBegin(GL11.GL_TRIANGLES);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x2, y2);
        GL11.glVertex2f(x3, y3);
        GL11.glEnd();
        
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    /**
     * Desenha um texto com sombra
     * 
     * @param text Texto a ser desenhado
     * @param x Posição X
     * @param y Posição Y
     * @param color Cor do texto (ARGB)
     */
    public static void drawStringWithShadow(String text, float x, float y, int color) {
        mc.fontRendererObj.drawStringWithShadow(text, x, y, color);
    }
    
    /**
     * Desenha um texto centralizado com sombra
     * 
     * @param text Texto a ser desenhado
     * @param x Posição X do centro
     * @param y Posição Y
     * @param color Cor do texto (ARGB)
     */
    public static void drawCenteredStringWithShadow(String text, float x, float y, int color) {
        int width = mc.fontRendererObj.getStringWidth(text);
        mc.fontRendererObj.drawStringWithShadow(text, x - width / 2.0f, y, color);
    }
    
    /**
     * Desenha um texto com sombra e background
     * 
     * @param text Texto a ser desenhado
     * @param x Posição X
     * @param y Posição Y
     * @param textColor Cor do texto (ARGB)
     * @param bgColor Cor do background (ARGB)
     */
    public static void drawStringWithBackground(String text, float x, float y, int textColor, int bgColor) {
        int width = mc.fontRendererObj.getStringWidth(text);
        int height = mc.fontRendererObj.FONT_HEIGHT;
        
        drawRect(x - 1, y - 1, x + width + 1, y + height + 1, bgColor);
        mc.fontRendererObj.drawStringWithShadow(text, x, y, textColor);
    }
    
    /**
     * Desenha uma textura
     * 
     * @param x Posição X
     * @param y Posição Y
     * @param width Largura
     * @param height Altura
     * @param u Coordenada U inicial
     * @param v Coordenada V inicial
     * @param uWidth Largura U
     * @param vHeight Altura V
     */
    public static void drawTexturedRect(float x, float y, float width, float height, float u, float v, float uWidth, float vHeight) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(x, y + height, 0).tex(u, v + vHeight).endVertex();
        worldRenderer.pos(x + width, y + height, 0).tex(u + uWidth, v + vHeight).endVertex();
        worldRenderer.pos(x + width, y, 0).tex(u + uWidth, v).endVertex();
        worldRenderer.pos(x, y, 0).tex(u, v).endVertex();
        tessellator.draw();
    }
    
    /**
     * Desenha uma bounding box 3D
     * 
     * @param bb Bounding box a ser desenhada
     * @param color Cor da box (ARGB)
     */
    public static void drawBox(net.minecraft.util.AxisAlignedBB bb, int color) {
        if (bb == null) return;
        
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        
        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(red, green, blue, alpha);
        
        GL11.glBegin(GL11.GL_LINES);
        
        // Base inferior
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        
        // Base superior
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        
        // Linhas verticais
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        
        GL11.glEnd();
        
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
    
    /**
     * Desenha uma linha 3D
     * 
     * @param x1 Posição X inicial
     * @param y1 Posição Y inicial
     * @param z1 Posição Z inicial
     * @param x2 Posição X final
     * @param y2 Posição Y final
     * @param z2 Posição Z final
     * @param width Largura da linha
     * @param color Cor da linha (ARGB)
     */
    public static void drawLine3D(float x1, float y1, float z1, float x2, float y2, float z2, float width, int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        
        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(red, green, blue, alpha);
        
        GL11.glLineWidth(width);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3f(x1, y1, z1);
        GL11.glVertex3f(x2, y2, z2);
        GL11.glEnd();
        
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
    
    /**
     * Desenha um retângulo com textura
     * 
     * @param x Posição X
     * @param y Posição Y
     * @param width Largura
     * @param height Altura
     */
    public static void drawTexturedRect(float x, float y, float width, float height) {
        drawTexturedRect(x, y, width, height, 0, 0, 1, 1);
    }
    
    /**
     * Desenha um ícone de item
     * 
     * @param x Posição X
     * @param y Posição Y
     * @param item Item a ser desenhado
     */
    public static void drawItemStack(int x, int y, net.minecraft.item.ItemStack item) {
        if (item == null) return;
        
        mc.getRenderItem().renderItemIntoGUI(item, x, y);
    }
    
    /**
     * Desenha um progress bar
     * 
     * @param x Posição X
     * @param y Posição Y
     * @param width Largura total
     * @param height Altura
     * @param progress Progresso (0.0 - 1.0)
     * @param bgColor Cor do background (ARGB)
     * @param progressColor Cor do progresso (ARGB)
     */
    public static void drawProgressBar(float x, float y, float width, float height, float progress, int bgColor, int progressColor) {
        // Desenha o background
        drawRect(x, y, x + width, y + height, bgColor);
        
        // Desenha o progresso
        float progressWidth = width * Math.min(1.0f, Math.max(0.0f, progress));
        drawRect(x, y, x + progressWidth, y + height, progressColor);
    }
    
    /**
     * Desenha um progress bar com gradiente
     * 
     * @param x Posição X
     * @param y Posição Y
     * @param width Largura total
     * @param height Altura
     * @param progress Progresso (0.0 - 1.0)
     * @param bgColor Cor do background (ARGB)
     * @param startColor Cor inicial do gradiente (ARGB)
     * @param endColor Cor final do gradiente (ARGB)
     */
    public static void drawGradientProgressBar(float x, float y, float width, float height, float progress, 
                                               int bgColor, int startColor, int endColor) {
        // Desenha o background
        drawRect(x, y, x + width, y + height, bgColor);
        
        // Desenha o progresso com gradiente
        float progressWidth = width * Math.min(1.0f, Math.max(0.0f, progress));
        drawGradientRect(x, y, x + progressWidth, y + height, startColor, endColor);
    }
    
    /**
     * Aplica um blur (desfoque) em uma área da tela
     * 
     * @param x Posição X
     * @param y Posição Y
     * @param width Largura
     * @param height Altura
     * @param blurRadius Raio do blur
     */
    public static void drawBlur(float x, float y, float width, float height, float blurRadius) {
        // Implementação simplificada de blur
        // Em uma implementação real, isso usaria shaders
        
        // Desenha um retângulo semi-transparente para simular blur
        int alpha = (int) (150 * (1.0f - Math.min(1.0f, blurRadius / 20.0f)));
        int color = (alpha << 24) | 0x000000;
        
        drawRect(x, y, x + width, y + height, color);
    }
    
    /**
     * Aplica um glow (brilho) em uma área
     * 
     * @param x Posição X
     * @param y Posição Y
     * @param width Largura
     * @param height Altura
     * @param color Cor do glow (ARGB)
     * @param intensity Intensidade do glow
     */
    public static void drawGlow(float x, float y, float width, float height, int color, float intensity) {
        // Desenha múltiplas camadas com transparência decrescente
        int alpha = (int) (255 * intensity);
        
        for (int i = 0; i < 5; i++) {
            float offset = i * 2.0f;
            int currentAlpha = (int) (alpha * (1.0f - i * 0.2f));
            int currentColor = (currentAlpha << 24) | (color & 0x00FFFFFF);
            
            drawRect(x - offset, y - offset, x + width + offset, y + height + offset, currentColor);
        }
    }
}
