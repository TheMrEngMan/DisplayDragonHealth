package com.MrEngMan.DisplayDragonHealth;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitScheduler;

public class Main extends JavaPlugin implements Listener {

    public static Main plugin;
    public static World endWorld;
    public static String endWorldName;
    public static BossBar dragonHealthBossBar;

    FileConfiguration config = getConfig();

    // When plugin is first enabled
    @SuppressWarnings("static-access")
    @Override
    public void onEnable() {
        this.plugin = this;

        // Configuration
        config.addDefault("world", "world_the_end");
        config.options().copyDefaults(true);
        saveConfig();

        // Register stuff
        new EventListeners(this);
        Bukkit.getPluginManager().registerEvents(this, this);
        endWorldName = config.getString("world");
        endWorld = getServer().getWorld(endWorldName);

        // Create boss bar
        dragonHealthBossBar = getServer().createBossBar("dragonHealthBossBar", BarColor.PINK, BarStyle.SEGMENTED_20);
        dragonHealthBossBar.setTitle("Ender Dragon");
        dragonHealthBossBar.setProgress(1.0);
        dragonHealthBossBar.setVisible(false);

        // Create repeating event to update dragon health bar once per second
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                Utils.updateDragonHealthBar();
            }
        }, 0L, 20L);

    }


}