package com.MrEngMan.DisplayDragonHealth;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EnderDragon;

import static com.MrEngMan.DisplayDragonHealth.Main.dragonHealthBossBar;
import static com.MrEngMan.DisplayDragonHealth.Main.endWorld;

public class Utils {

    // Translate '&' as formatting codes
    public static String SendChatMessage(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    // Does what the name says
    public static Boolean checkIfSomePlayersAreInEnd() {

        try {
            return getNumberOfPlayersInEnd() > 0;
        } catch (NullPointerException npe) {
            return false;
        }

    }

    // Does what the name says
    public static int getNumberOfPlayersInEnd() {

        try {
            return endWorld.getPlayers().size();
        } catch (NullPointerException npe) {
            return 0;
        }

    }

    public static void updateDragonHealthBar() {

        // If the dragon health bar is not visible, don't update it
        if(!dragonHealthBossBar.isVisible()) return;

        // Otherwise, try to update it (will fail if the dragon is no longer alive)
        try {
            EnderDragon enderDragon = endWorld.getEnderDragonBattle().getEnderDragon();
            dragonHealthBossBar.setProgress(enderDragon.getHealth() / enderDragon.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        } catch (Exception e) {
            //e.printStackTrace();
        }

    }

}