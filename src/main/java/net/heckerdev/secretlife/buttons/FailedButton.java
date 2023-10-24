package net.heckerdev.secretlife.buttons;

import net.heckerdev.secretlife.util.Color;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class FailedButton {
    public static void Pressed(Player player, Block block) {
        player.sendMessage(Component.text("✔ You clicked The Failed Button!").color(Color.DARK_RED).decoration(TextDecoration.BOLD, true));
    }
}
//❌❌