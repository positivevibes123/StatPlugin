package me.damor.statplugin;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private PlayerManager playerManager;

    public PlayerListener(PlayerManager playerManager){
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        // If something bugged (person crashed, etc.) and data is still in game, remove first
        if (playerManager.hasPlayer(player)){
            playerManager.removePlayer(player);
        }

        playerManager.addPlayer(player);

        player.sendMessage(ChatColor.DARK_GREEN + "You are playing with Dan's Stat Plugin! Level up and gain buffs!\n" +
                "Type /help StatPlugin in order to see a full list of commands.");
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();

        playerManager.removePlayer(player);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        Player player = event.getEntity().getKiller();

        // If the entity was killed by a player, then give that player some xp
        if (player != null){
            playerManager.givePlayerXp(player);
        }
    }
}
