package me.damor.statplugin.commands;

import me.damor.statplugin.PlayerManager;
import me.damor.statplugin.PlayerStat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StatResetCommand implements CommandExecutor {
    private PlayerManager playerManager;

    public StatResetCommand(PlayerManager playerManager){
        this.playerManager = playerManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player)commandSender;
        PlayerStat playerStat = playerManager.getPlayerStat(player);

        playerManager.removeEffects(player);

        playerStat.levelOverall = 1;
        playerStat.xp = 0;
        playerStat.luckLevel = 0;
        playerStat.swiftnessLevel = 0;
        playerStat.strengthLevel = 0;

        player.sendMessage(ChatColor.YELLOW + "Level and stats have been reset! Good luck starting fresh!");

        return true;
    }
}
