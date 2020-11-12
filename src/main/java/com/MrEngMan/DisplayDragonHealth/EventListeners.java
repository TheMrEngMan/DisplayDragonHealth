package com.MrEngMan.DisplayDragonHealth;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

import static com.MrEngMan.DisplayDragonHealth.Main.dragonHealthBossBar;

public class EventListeners implements Listener {

    public EventListeners(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    // ==================== Player Change World ====================

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();

        // If the player is now in the end
        if (player.getWorld().getName().equals("world_the_end")) {

            // Make the dragon health bar visible and set all the players who are not in the end to see it
            dragonHealthBossBar.setVisible(true);
            dragonHealthBossBar.removeAll();
            for (Player currentPlayer : Bukkit.getServer().getOnlinePlayers()) {
                if(!currentPlayer.getWorld().getName().equals("world_the_end")) {
                    dragonHealthBossBar.addPlayer(currentPlayer);
                }
            }

        }

        // If the player has left the end
        else if(event.getFrom().getName().equals("world_the_end")) {

            // If all players left the end, make the dragon health bar invisible (and don't update it anymore)
            if (!Utils.checkIfSomePlayersAreInEnd()) {
                dragonHealthBossBar.setVisible(false);
            }
            // Otherwise, set this player to see the dragon health bar
            else {
                dragonHealthBossBar.addPlayer(player);
            }

        }

    }

    // ==================== Damage / Regain Health ====================

    void checkToUpdateDragonHealth(Entity entity) {
        // If the entity is the Ender Dragon in the end dimension, update the dragon health bar according to the fractional remaining health it has
        if (entity.getType() == EntityType.ENDER_DRAGON && entity.getWorld().getName().equals("world_the_end")) {
            EnderDragon enderDragon = (EnderDragon) entity;
            dragonHealthBossBar.setProgress(enderDragon.getHealth() / Objects.requireNonNull(enderDragon.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        checkToUpdateDragonHealth(event.getEntity());
    }

    @EventHandler
    public void onEntityDamageByBlock(EntityDamageByBlockEvent event) {
        checkToUpdateDragonHealth(event.getEntity());

    }

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        checkToUpdateDragonHealth(event.getEntity());
    }

    // ==================== Player Death ====================

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        // If a player dies in the end and they were the only player there, make the dragon health bar invisible (and don't update it anymore)
        if (player.getWorld().getName().equals("world_the_end") && Utils.getNumberOfPlayersInEnd() == 1) {
            dragonHealthBossBar.setVisible(false);
        }

    }

    // ==================== Player Join / Quit ====================

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // If a player joins and they are not in th end, but someone is, set this player to see the dragon health bar
        if (!player.getWorld().getName().equals("world_the_end") && Utils.checkIfSomePlayersAreInEnd()) {
            dragonHealthBossBar.addPlayer(player);
        }
        // Otherwise, make the dragon health bar visible and set all the players who are not in the end to see it
        else if (player.getWorld().getName().equals("world_the_end")) {

            dragonHealthBossBar.setVisible(true);
            dragonHealthBossBar.removeAll();
            for (Player currentPlayer : Bukkit.getServer().getOnlinePlayers()) {
                if (!currentPlayer.getWorld().getName().equals("world_the_end")) {
                    dragonHealthBossBar.addPlayer(currentPlayer);
                }
            }
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // Stop showing this player the dragon health bar
        dragonHealthBossBar.removePlayer(player);

        // If a player leaves while in the end and they were the only player there, make the dragon health bar invisible (and don't update it anymore)
        if (player.getWorld().getName().equals("world_the_end") && Utils.getNumberOfPlayersInEnd() == 1) {
            dragonHealthBossBar.setVisible(false);
        }

    }

}
