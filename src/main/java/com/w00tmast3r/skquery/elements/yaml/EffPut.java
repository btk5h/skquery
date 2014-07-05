package com.w00tmast3r.skquery.elements.yaml;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

import java.io.File;
import java.io.IOException;


@Patterns("put %object% for %string% in %string%")
public class EffPut extends Effect {

    private Expression<String> cfg, path;
    private Expression<Object> item;

    @Override
    protected void execute(Event event) {
        String c = cfg.getSingle(event);
        String p = path.getSingle(event);
        Object i = item.getSingle(event);
        if(c == null || p == null || i == null) return;
        File file = new File(Skript.getInstance().getDataFolder() + "/scripts/" + c + ".yml");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileConfiguration fcfg = YamlConfiguration.loadConfiguration(file);
        fcfg.set(p, i);
        try {
            fcfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString(Event event, boolean b) {
        return "put";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        item = (Expression<Object>) expressions[0];
        path = (Expression<String>) expressions[1];
        cfg = (Expression<String>) expressions[2];
        return true;
    }
}
