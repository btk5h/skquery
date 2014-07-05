package com.w00tmast3r.skquery.elements.conditions;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import org.bukkit.block.Block;
import org.bukkit.event.Event;


@Patterns({"%block% is [the] topmost block",
        "%block% is not [the] topmost block"})
public class CondIsTopmostBlock extends Condition {

    private Expression<Block> block;

    @Override
    public boolean check(Event event) {
        Block b = block.getSingle(event);
        if (b == null) return isNegated();
        return isNegated() ? !(b.getLocation().getWorld().getHighestBlockYAt(b.getLocation()) == b.getY()) : b.getLocation().getWorld().getHighestBlockYAt(b.getLocation()) == b.getY();
    }

    @Override
    public String toString(Event event, boolean b) {
        return "is topmost block";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        block = (Expression<Block>) expressions[0];
        setNegated(i == 1);
        return true;
    }
}
