package com.w00tmast3r.skquery.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skquery.util.Collect;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Patterns("yaml (1¦value|2¦nodes|3¦nodes with keys|4¦list) %string% from [file] %string%")
public class ExprYAMLValue extends SimpleExpression<Object> {

    private static enum State {
        VALUE, NODES, NODES_KEYS, LIST
    }

    private State type;
    private Expression<String> path, file;

    @Override
    protected Object[] get(Event e) {
        String p = path.getSingle(e);
        String f = file.getSingle(e);
        if (p == null || f == null) return null;
        File file = new File(Skript.getInstance().getDataFolder() + "/scripts/" + (f.contains(".") ? f : f + ".yml"));
        try {
            file.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if (!cfg.contains(p)) return null;
        switch (type) {
            case VALUE:
                if (cfg.isConfigurationSection(p)) return null;
                return Collect.asArray(cfg.get(p));
            case NODES:
                if (!cfg.isConfigurationSection(p)) return null;
                Set nodes = cfg.getConfigurationSection(p).getKeys(false);
                return nodes.toArray(new String[nodes.size()]);
            case NODES_KEYS:
                if (!cfg.isConfigurationSection(p)) return null;
                Set nodesKeys = cfg.getConfigurationSection(p).getKeys(true);
                return nodesKeys.toArray(new String[nodesKeys.size()]);
            case LIST:
                if (cfg.isConfigurationSection(p)) return null;
                List items = cfg.getList(p);
                return items.toArray();
        }
        assert false;
        return null;
    }

    @Override
    public boolean isSingle() {
        return type == State.VALUE;
    }

    @Override
    public Class<?> getReturnType() {
        return Object.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "yaml";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        switch (parseResult.mark) {
            case 1:
                type = State.VALUE;
                break;
            case 2:
                type = State.NODES;
                break;
            case 3:
                type = State.NODES_KEYS;
                break;
            case 4:
                type = State.LIST;
                break;
        }
        path = (Expression<String>) exprs[0];
        file = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.DELETE) return Collect.asArray(Object.class);
        switch (type) {
            case VALUE:
                if (mode == Changer.ChangeMode.SET) return Collect.asArray(Object.class);
                break;
            case NODES:
            case NODES_KEYS:
            case LIST:
                if (mode == Changer.ChangeMode.ADD || mode == Changer.ChangeMode.REMOVE) return Collect.asArray(Object.class);
                break;
        }
        return null;
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        String p = path.getSingle(e);
        String f = file.getSingle(e);
        if (p == null || f == null) return;
        File file = new File(Skript.getInstance().getDataFolder() + "/scripts/" + (f.contains(".") ? f : f + ".yml"));
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        try {
            if (mode == Changer.ChangeMode.DELETE) {
                cfg.set(p, null);
                return;
            }
            Object target = delta[0] == null ? "" : delta[0];
            switch (type) {
                case VALUE:
                    switch (mode) {
                        case SET:
                            cfg.set(p, delta[0]);
                            break;
                    }
                    break;
                case NODES:
                case NODES_KEYS:
                    switch (mode) {
                        case ADD:
                            cfg.createSection(p);
                            break;
                        case REMOVE:
                            cfg.set(p + "." + target, null);
                            break;
                    }
                    break;
                case LIST:
                    switch (mode) {
                        case ADD:
                            ((List<Object>) cfg.getList(p)).add(delta[0]);
                            break;
                        case REMOVE:
                            cfg.getList(p).remove(delta[0]);
                            break;
                    }
                    break;
            }
        } finally {
            try {
                cfg.save(file);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
