package com.novaclient.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe utilitária com métodos únicos para o cliente
 */
public class ClientUtils {
    
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final String PREFIX = "§8[§5Nova§8] §r";
    
    /**
     * Envia uma mensagem no chat do jogo
     * 
     * @param message Mensagem a ser enviada
     */
    public static void sendChatMessage(String message) {
        if (mc.thePlayer != null) {
            mc.thePlayer.addChatMessage(new ChatComponentText(PREFIX + message));
        }
    }
    
    /**
     * Envia uma mensagem de erro no chat do jogo
     * 
     * @param message Mensagem de erro
     */
    public static void sendErrorMessage(String message) {
        if (mc.thePlayer != null) {
            mc.thePlayer.addChatMessage(new ChatComponentText(PREFIX + "§c" + message));
        }
    }
    
    /**
     * Envia uma mensagem de sucesso no chat do jogo
     * 
     * @param message Mensagem de sucesso
     */
    public static void sendSuccessMessage(String message) {
        if (mc.thePlayer != null) {
            mc.thePlayer.addChatMessage(new ChatComponentText(PREFIX + "§a" + message));
        }
    }
    
    /**
     * Loga uma mensagem de informação no console
     * 
     * @param message Mensagem a ser logada
     */
    public static void logInfo(String message) {
        System.out.println("[INFO] [NovaClient] " + getCurrentTime() + " - " + message);
    }
    
    /**
     * Loga uma mensagem de erro no console
     * 
     * @param message Mensagem de erro
     */
    public static void logError(String message) {
        System.err.println("[ERROR] [NovaClient] " + getCurrentTime() + " - " + message);
    }
    
    /**
     * Loga uma mensagem de debug no console
     * 
     * @param message Mensagem de debug
     */
    public static void logDebug(String message) {
        System.out.println("[DEBUG] [NovaClient] " + getCurrentTime() + " - " + message);
    }
    
    /**
     * Obtém a hora atual formatada
     */
    private static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }
    
    /**
     * Verifica se o jogador está em um servidor Hypixel
     */
    public static boolean isOnHypixel() {
        if (mc.thePlayer == null || mc.thePlayer.getClientBrand() == null) {
            return false;
        }
        return mc.thePlayer.getClientBrand().toLowerCase().contains("hypixel");
    }
    
    /**
     * Verifica se o jogador está em um jogo BedWars
     */
    public static boolean isInBedWars() {
        if (mc.thePlayer == null) return false;
        
        // Verifica pelo nome do mundo ou scoreboard
        if (mc.theWorld != null) {
            String worldName = mc.theWorld.getWorldInfo().getWorldName();
            if (worldName != null && worldName.toLowerCase().contains("bedwars")) {
                return true;
            }
        }
        
        // Verifica pelo scoreboard
        if (mc.ingameGUI != null && mc.ingameGUI.getScoreboard() != null) {
            String scoreboardTitle = mc.ingameGUI.getScoreboard().getObjectiveInDisplaySlot(1).getDisplayName();
            if (scoreboardTitle != null && scoreboardTitle.toLowerCase().contains("bed wars")) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Verifica se o jogador está em um jogo SkyWars
     */
    public static boolean isInSkyWars() {
        if (mc.thePlayer == null) return false;
        
        if (mc.theWorld != null) {
            String worldName = mc.theWorld.getWorldInfo().getWorldName();
            if (worldName != null && worldName.toLowerCase().contains("skywars")) {
                return true;
            }
        }
        
        if (mc.ingameGUI != null && mc.ingameGUI.getScoreboard() != null) {
            String scoreboardTitle = mc.ingameGUI.getScoreboard().getObjectiveInDisplaySlot(1).getDisplayName();
            if (scoreboardTitle != null && scoreboardTitle.toLowerCase().contains("sky wars")) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Verifica se o jogador está em um lobby
     */
    public static boolean isInLobby() {
        if (mc.thePlayer == null) return false;
        
        if (mc.theWorld != null) {
            String worldName = mc.theWorld.getWorldInfo().getWorldName();
            if (worldName != null && (worldName.toLowerCase().contains("lobby") || 
                                       worldName.toLowerCase().contains("hub"))) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Obtém o FPS atual do jogo
     */
    public static int getFPS() {
        return Minecraft.getDebugFPS();
    }
    
    /**
     * Obtém o ping atual do jogador
     */
    public static int getPing() {
        if (mc.thePlayer == null || mc.getNetHandler() == null) {
            return 0;
        }
        return mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime();
    }
    
    /**
     * Formata um número com separadores de milhar
     */
    public static String formatNumber(long number) {
        return String.format("%,d", number);
    }
    
    /**
     * Formata um tempo em milissegundos para um formato legível
     */
    public static String formatTime(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        
        seconds = seconds % 60;
        minutes = minutes % 60;
        
        if (hours > 0) {
            return String.format("%02dh %02dm %02ds", hours, minutes, seconds);
        } else if (minutes > 0) {
            return String.format("%02dm %02ds", minutes, seconds);
        } else {
            return String.format("%02ds", seconds);
        }
    }
    
    /**
     * Converte uma cor hexadecimal para RGBA
     */
    public static float[] hexToRGBA(int hexColor) {
        float alpha = ((hexColor >> 24) & 0xFF) / 255.0f;
        float red = ((hexColor >> 16) & 0xFF) / 255.0f;
        float green = ((hexColor >> 8) & 0xFF) / 255.0f;
        float blue = (hexColor & 0xFF) / 255.0f;
        
        return new float[]{red, green, blue, alpha};
    }
    
    /**
     * Interpola entre dois valores
     */
    public static float lerp(float start, float end, float delta) {
        return start + (end - start) * delta;
    }
    
    /**
     * Limita um valor entre min e max
     */
    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
    
    /**
     * Limita um valor entre 0 e 1
     */
    public static float clamp01(float value) {
        return clamp(value, 0, 1);
    }
}
