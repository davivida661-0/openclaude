package com.novaclient.mods.bedwars;

import com.novaclient.mods.Mod;
import com.novaclient.utils.ClientUtils;
import com.novaclient.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Mod BedWars Overlay - Exibe informações do jogo BedWars
 * Mostra recursos da equipe, status da cama, eliminados, etc.
 */
public class BedWarsOverlayMod extends Mod {
    
    private static final Minecraft mc = Minecraft.getMinecraft();
    
    // Configurações
    private boolean showResources = true;
    private boolean showBedStatus = true;
    private boolean showEliminated = true;
    private boolean showForgeTier = true;
    private int bgColor = 0x80000000;
    private int textColor = 0xFFFFFFFF;
    
    // Dados do BedWars
    private int iron = 0;
    private int gold = 0;
    private int diamond = 0;
    private int emerald = 0;
    private boolean bedAlive = true;
    private String forgeTier = "Iron";
    private List<String> eliminatedTeams = new ArrayList<>();
    
    // Padrões para parsear o scoreboard
    private static final Pattern RESOURCE_PATTERN = Pattern.compile("(\d+) (Iron|Gold|Diamond|Emerald)");
    private static final Pattern BED_PATTERN = Pattern.compile("(Bed|Base) (Destroyed|Alive)");
    private static final Pattern FORGE_PATTERN = Pattern.compile("(Iron|Gold|Diamond|Emerald) (Forge|Generator)");
    private static final Pattern ELIMINATED_PATTERN = Pattern.compile("Eliminated: (.+)");
    
    public BedWarsOverlayMod() {
        super("BedWars Overlay", "Exibe informações do BedWars", ModCategory.BEDWARS);
        setX(10);
        setY(10);
    }
    
    @Override
    public void onTick() {
        if (!isEnabled() || mc.theWorld == null || mc.thePlayer == null) return;
        
        // Atualiza os dados do BedWars
        updateBedWarsData();
    }
    
    @Override
    public void onRender() {
        if (!isEnabled()) return;
        
        ScaledResolution sr = new ScaledResolution(mc);
        
        // Desenha o overlay
        int x = getX();
        int y = getY();
        int width = 200;
        int startY = y;
        
        // Desenha o background
        RenderUtils.drawRect(x - 5, y - 5, x + width + 5, y + 100, bgColor);
        RenderUtils.drawBorder(x - 5, y - 5, x + width + 5, y + 100, 1, 0xFF00FFFF);
        
        // Desenha o título
        mc.fontRendererObj.drawStringWithShadow("§6BedWars §7- §5Nova Client", x, y, textColor);
        y += 12;
        
        // Desenha os recursos
        if (showResources) {
            drawResource("§7Iron: §f" + iron, x, y, mc);
            y += 10;
            drawResource("§6Gold: §f" + gold, x, y, mc);
            y += 10;
            drawResource("§bDiamond: §f" + diamond, x, y, mc);
            y += 10;
            drawResource("§aEmerald: §f" + emerald, x, y, mc);
            y += 12;
        }
        
        // Desenha o status da cama
        if (showBedStatus) {
            String bedStatus = bedAlive ? "§aBed: §2Alive" : "§cBed: §4Destroyed";
            mc.fontRendererObj.drawStringWithShadow(bedStatus, x, y, textColor);
            y += 12;
        }
        
        // Desenha o tier do forge
        if (showForgeTier) {
            String forgeDisplay = "§7Forge: §f" + forgeTier;
            mc.fontRendererObj.drawStringWithShadow(forgeDisplay, x, y, textColor);
            y += 12;
        }
        
        // Desenha times eliminados
        if (showEliminated && !eliminatedTeams.isEmpty()) {
            String eliminatedText = "§cEliminated: §f" + String.join(", ", eliminatedTeams);
            mc.fontRendererObj.drawStringWithShadow(eliminatedText, x, y, textColor);
        }
    }
    
    /**
     * Atualiza os dados do BedWars a partir do scoreboard
     */
    private void updateBedWarsData() {
        if (mc.ingameGUI == null || mc.ingameGUI.getScoreboard() == null) return;
        
        Scoreboard scoreboard = mc.ingameGUI.getScoreboard();
        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
        
        if (objective == null) return;
        
        String scoreboardTitle = objective.getDisplayName();
        
        // Verifica se está em um jogo de BedWars
        if (!scoreboardTitle.toLowerCase().contains("bed wars")) return;
        
        // Reseta os valores
        iron = 0;
        gold = 0;
        diamond = 0;
        emerald = 0;
        eliminatedTeams.clear();
        
        // Obtém todas as linhas do scoreboard
        List<String> scoreboardLines = new ArrayList<>();
        for (String line : mc.ingameGUI.getScoreboard().getLines()) {
            scoreboardLines.add(line);
        }
        
        // Parseia as informações
        for (String line : scoreboardLines) {
            String cleanLine = line.replaceAll("§[0-9a-fA-F]", "");
            
            // Parseia recursos
            if (cleanLine.contains("Iron") || cleanLine.contains("Gold") || 
                cleanLine.contains("Diamond") || cleanLine.contains("Emerald")) {
                parseResources(cleanLine);
            }
            
            // Parseia status da cama
            if (cleanLine.toLowerCase().contains("bed") || cleanLine.toLowerCase().contains("base")) {
                bedAlive = !cleanLine.toLowerCase().contains("destroyed");
            }
            
            // Parseia forge tier
            if (cleanLine.toLowerCase().contains("forge") || cleanLine.toLowerCase().contains("generator")) {
                parseForgeTier(cleanLine);
            }
            
            // Parseia times eliminados
            if (cleanLine.toLowerCase().contains("eliminated")) {
                parseEliminated(cleanLine);
            }
        }
    }
    
    /**
     * Parseia os recursos do scoreboard
     */
    private void parseResources(String line) {
        try {
            if (line.contains("Iron")) {
                String[] parts = line.split(" ");
                for (String part : parts) {
                    if (part.matches("\\d+")) {
                        iron = Integer.parseInt(part);
                        break;
                    }
                }
            } else if (line.contains("Gold")) {
                String[] parts = line.split(" ");
                for (String part : parts) {
                    if (part.matches("\\d+")) {
                        gold = Integer.parseInt(part);
                        break;
                    }
                }
            } else if (line.contains("Diamond")) {
                String[] parts = line.split(" ");
                for (String part : parts) {
                    if (part.matches("\\d+")) {
                        diamond = Integer.parseInt(part);
                        break;
                    }
                }
            } else if (line.contains("Emerald")) {
                String[] parts = line.split(" ");
                for (String part : parts) {
                    if (part.matches("\\d+")) {
                        emerald = Integer.parseInt(part);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            ClientUtils.logError("Erro ao parsear recursos: " + e.getMessage());
        }
    }
    
    /**
     * Parseia o tier do forge
     */
    private void parseForgeTier(String line) {
        if (line.contains("Iron")) {
            forgeTier = "Iron";
        } else if (line.contains("Gold")) {
            forgeTier = "Gold";
        } else if (line.contains("Diamond")) {
            forgeTier = "Diamond";
        } else if (line.contains("Emerald")) {
            forgeTier = "Emerald";
        }
    }
    
    /**
     * Parseia os times eliminados
     */
    private void parseEliminated(String line) {
        String[] parts = line.split(":");
        if (parts.length > 1) {
            String[] teams = parts[1].split(",");
            for (String team : teams) {
                eliminatedTeams.add(team.trim());
            }
        }
    }
    
    /**
     * Desenha uma linha de recurso
     */
    private void drawResource(String text, int x, int y, Minecraft mc) {
        // Desenha o ícone do recurso
        if (text.contains("Iron")) {
            RenderUtils.drawRect(x - 12, y, x - 8, y + 8, 0xFFAAAAAA);
        } else if (text.contains("Gold")) {
            RenderUtils.drawRect(x - 12, y, x - 8, y + 8, 0xFFFFD700);
        } else if (text.contains("Diamond")) {
            RenderUtils.drawRect(x - 12, y, x - 8, y + 8, 0xFF00BFFF);
        } else if (text.contains("Emerald")) {
            RenderUtils.drawRect(x - 12, y, x - 8, y + 8, 0xFF50C878);
        }
        
        mc.fontRendererObj.drawStringWithShadow(text, x, y, textColor);
    }
    
    // Getters e Setters
    public boolean isShowResources() {
        return showResources;
    }
    
    public void setShowResources(boolean showResources) {
        this.showResources = showResources;
    }
    
    public boolean isShowBedStatus() {
        return showBedStatus;
    }
    
    public void setShowBedStatus(boolean showBedStatus) {
        this.showBedStatus = showBedStatus;
    }
    
    public boolean isShowEliminated() {
        return showEliminated;
    }
    
    public void setShowEliminated(boolean showEliminated) {
        this.showEliminated = showEliminated;
    }
    
    public boolean isShowForgeTier() {
        return showForgeTier;
    }
    
    public void setShowForgeTier(boolean showForgeTier) {
        this.showForgeTier = showForgeTier;
    }
    
    public int getBgColor() {
        return bgColor;
    }
    
    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }
    
    public int getTextColor() {
        return textColor;
    }
    
    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
}
