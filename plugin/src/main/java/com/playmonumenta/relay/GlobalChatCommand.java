package com.playmonumenta.relay;

import com.playmonumenta.relay.network.SocketManager;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.GreedyStringArgument;

public class GlobalChatCommand {
	static final String COMMAND = "g";

	public static void register(Plugin plugin) {
		new CommandAPICommand(COMMAND)
			.withArguments(new GreedyStringArgument("message"))
			.executes((sender, args) -> {
				run(plugin, sender, (String)args[0]);
			})
			.register();
	}

	private static void run(Plugin plugin, CommandSender sender, String message) {
		/* Get the player's name, if any */
		String name = ServerProperties.getShardName();
		if (sender instanceof Player) {
			name = ((Player)sender).getName();
		} else if (sender instanceof ProxiedCommandSender) {
			CommandSender callee = ((ProxiedCommandSender) sender).getCallee();
			if (callee instanceof Player) {
				name = ((Player)callee).getName();
			}
		}

		name = name.replace("\\", "\\\\").replace("\"", "\\\"");
		message = message.replace("\\", "\\\\").replace("\"", "\\\"");
		String commandStr = "tellraw @a [\"§7<§fG§7> §f" + name + " §7»§f" + message + "\"]";

		try {
			SocketManager.broadcastCommand(plugin, commandStr);
		} catch (Exception e) {
			sender.sendMessage(ChatColor.RED + "Broadcasting command failed");
		}
	}
}
