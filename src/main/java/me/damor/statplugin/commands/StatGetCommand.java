package me.damor.statplugin.commands;

import me.damor.statplugin.PlayerManager;
import me.damor.statplugin.PlayerStat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StatGetCommand implements CommandExecutor {
    private PlayerManager playerManager;

    public StatGetCommand(PlayerManager playerManager){
        this.playerManager = playerManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player)commandSender;
        PlayerStat playerStat = playerManager.getPlayerStat(player);

        player.sendMessage(ChatColor.YELLOW + "-------------\n" +
                "Your current stats: \nLevel: "
                + playerStat.levelOverall
                + "\n" + ChatColor.BLUE + "Swiftness: " + playerStat.swiftnessLevel
                + "\n" + ChatColor.RED + "Strength: " + playerStat.strengthLevel
                + "\n" + ChatColor.DARK_GREEN + "Luck: " + playerStat.luckLevel
                + ChatColor.YELLOW + "\n-------------");

        return true;
    }
}
