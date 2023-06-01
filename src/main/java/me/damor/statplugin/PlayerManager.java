package me.damor.statplugin;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class PlayerManager {
    private StatPlugin plugin;
    private HashMap<String, PlayerStat> playerStats = new HashMap<>();
    Gson gson = new Gson();

    private static final int BASE_XP_LEVEL_UP = 200;
    private static final int XP_PER_KILL = 50;

    public PlayerManager(StatPlugin plugin){
        this.plugin = plugin;
    }

    public boolean hasPlayer(Player player){
        return playerStats.containsKey(player.getName());
    }

    private String getPlayerDir(String name){
        return plugin.getDataFolder() + File.separator + name + ".json";
    }

    public PlayerStat getPlayerStat(Player player){
        return playerStats.get(player.getName());
    }

    public void rollPlayerStats(Player player){
        PlayerStat playerStat = getPlayerStat(player);

        // Get number in range of 0 - 2 inclusive
        int random = (int)(Math.random() * (2 + 1));

        switch (random){
            case 0:
                player.sendMessage(ChatColor.BLUE + "Swiftness increased!");
                playerStat.swiftnessLevel++;
                break;
            case 1:
                player.sendMessage(ChatColor.RED + "Strength increased!");
                playerStat.strengthLevel++;
                break;
            case 2:
                player.sendMessage(ChatColor.DARK_GREEN + "Luck increased!");
                playerStat.luckLevel++;
                break;
        }
    }

    public void resetPlayerStats(Player player){
        PlayerStat playerStat = getPlayerStat(player);

        playerStat.levelOverall = 1;
        playerStat.xp = 0;
        playerStat.luckLevel = 0;
        playerStat.swiftnessLevel = 0;
        playerStat.strengthLevel = 0;
    }

    public void givePlayerXp(Player player){
        PlayerStat playerStat = playerStats.get(player.getName());

        playerStat.xp += XP_PER_KILL;
        // Algorithm: BASE_XP_LEVEL_UP + (XP_PER_KILL * level^3)
        int requiredXp = BASE_XP_LEVEL_UP + (XP_PER_KILL * (int)Math.pow(playerStat.levelOverall, 3));

        while (playerStat.xp >= requiredXp){
            // Level up player
            playerStat.xp -= requiredXp;
            playerStat.levelOverall++;

            // Send a message to every person about player leveling up
            for (Player p: Bukkit.getOnlinePlayers()){
                p.sendMessage(ChatColor.YELLOW + player.getName() + " has reached level " + playerStat.levelOverall + "!");
            }

            rollPlayerStats(player);

            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 10);
            applyEffects(player);
        }
    }

    public void applyEffects(Player player){
        PlayerStat playerStat = playerStats.get(player.getName());

        if (playerStat.swiftnessLevel > 0){
            player.removePotionEffect(PotionEffectType.SPEED);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, playerStat.swiftnessLevel - 1));
        }

        if (playerStat.strengthLevel > 0){
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, PotionEffect.INFINITE_DURATION, playerStat.strengthLevel - 1));
        }

        if (playerStat.luckLevel > 0){
            player.removePotionEffect(PotionEffectType.LUCK);
            player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, PotionEffect.INFINITE_DURATION, playerStat.luckLevel - 1));
        }
    }

    public void removeEffects(Player player){
        PlayerStat playerStat = playerStats.get(player.getName());

        if (playerStat.swiftnessLevel > 0){
            player.removePotionEffect(PotionEffectType.SPEED);
        }

        if (playerStat.strengthLevel > 0){
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        }

        if (playerStat.luckLevel > 0){
            player.removePotionEffect(PotionEffectType.LUCK);
        }
    }

    public void addPlayer(Player player){
        String name = player.getName();

        File playerFile = new File(getPlayerDir(name));

        // If JSON doesn't exist, add beginner data to hashmap which will save file when player leaves
        // Otherwise, create player stat object from JSON data and add to hashmap
        if (!playerFile.isFile()){
            playerStats.put(name, new PlayerStat());
        } else {
            try{
                FileReader reader = new FileReader(playerFile);
                PlayerStat playerStat = gson.fromJson(reader, PlayerStat.class);
                playerStats.put(name, playerStat);
                plugin.getLogger().info("Loading " + name + "'s file from: " + getPlayerDir(name));
                reader.close();
            } catch (IOException e){
                plugin.getLogger().severe("Error loading data file from player " + name);
            }
        }

        applyEffects(player);
    }

    public void removePlayer(Player player){
        // Save JSON data for player. If json file doesn't exist, then create one

        String name = player.getName();

        PlayerStat playerStat = playerStats.get(name);

        File playerFile = new File(getPlayerDir(name));

        // If player JSON data file doesn't exist, create it
        if (!playerFile.isFile()){
            try{
                playerFile.createNewFile();
                plugin.getLogger().info("Creating new file for " + name + " at: " + getPlayerDir(name));
            } catch (IOException e){
                plugin.getLogger().severe("Error creating data file for player " + name);
            }
        }

        // Write player data to JSON file
        try{
            FileWriter writer = new FileWriter(playerFile);
            gson.toJson(playerStat, writer);
            writer.flush();
            writer.close();
        } catch (IOException e){
            plugin.getLogger().severe("Error writing to data file for player " + name);
        }

        // Finally, remove player data from hashmap
        playerStats.remove(name);
    }
}
