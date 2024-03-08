package net.strokkur;

import net.strokkur.color.Database;
import net.strokkur.color.Expansion;
import net.strokkur.config.ColorConfig;
import net.strokkur.config.GuiConfig;
import net.strokkur.util.fastinv.FastInv;
import net.strokkur.util.fastinv.FastInvManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    public static Main plugin;

    @Override
    public void onEnable() {
        plugin = this;

        Database.init();

        FastInvManager.register(this);
        new Expansion().register();

        ColorConfig.init();
        GuiConfig.init();

        getCommand("color").setExecutor(new ColorCMD());
        getCommand("color").setTabCompleter(new ColorCMD());
    }

    @Override
    public void onDisable() {
        new Expansion().unregister();
    }
}
