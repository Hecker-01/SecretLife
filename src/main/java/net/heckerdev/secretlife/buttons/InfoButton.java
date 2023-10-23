package net.heckerdev.secretlife.buttons;

import net.heckerdev.secretlife.util.Color;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class InfoButton {
    public static void Pressed(Player player, Block block) {
        player.sendMessage(Component.text("✔ You clicked The Info Button!").color(Color.AQUA).decoration(TextDecoration.BOLD, true));
    }
}
