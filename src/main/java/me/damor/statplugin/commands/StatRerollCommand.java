package me.damor.statplugin.commands;

import me.damor.statplugin.PlayerManager;
import me.damor.statplugin.PlayerStat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StatRerollCommand implements CommandExecutor {
    private PlayerManager playerManager;

    public StatRerollCommand(PlayerManager playerManager){
        this.playerManager = playerManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player)commandSender;
        PlayerStat playerStat = playerManager.getPlayerStat(player);

        // Save variables that we will be adding back
        int tempLevel = playerStat.levelOverall;
        int tempXP = playerStat.xp;

        // Reset all stats
        playerManager.resetPlayerStats(player);

        for (int i = 0; i < tempLevel; i++){
            // Choose a random stat to lvl up per level
            playerManager.rollPlayerStats(player);
        }

        playerStat.levelOverall = tempLevel;
        playerStat.xp = tempXP;

        return true;
    }
}
