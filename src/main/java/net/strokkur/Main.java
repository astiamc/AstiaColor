package net.strokkur;

import net.strokkur.color.Expansion;
import net.strokkur.util.fastinv.FastInv;
import net.strokkur.util.fastinv.FastInvManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Main plugin;

    @Override
    public void onEnable() {
        plugin = this;

        FastInvManager.register(this);

        new Expansion().register();
    }

    @Override
    public void onDisable() {
        new Expansion().unregister();
    }
}
