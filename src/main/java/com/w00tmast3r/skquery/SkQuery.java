package com.w00tmast3r.skquery;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.w00tmast3r.skquery.elements.virtualchests.v2.FormattedSlotManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SkQuery extends JavaPlugin {

    private static SkQuery instance = null;
    private static SkriptAddon addonInstance = null;

    public SkQuery() {
        instance = this;
    }

    @Override
    public void onEnable() {
        addonInstance = Skript.registerAddon(this);
        Registration.enableSnooper();
        Bukkit.getPluginManager().registerEvents(new FormattedSlotManager(), this);
    }

    public static SkQuery getInstance() {
        return instance;
    }

    public static SkriptAddon getAddonInstance() {
        return addonInstance;
    }
}
