package com.w00tmast3r.skquery.elements.conditions;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffectType;


@Patterns({"%player% has %potioneffecttype%","%player% does(n't| not) have %potioneffecttype%"})
public class CondHasPotion extends Condition {

    private Expression<Player> player;
    private Expression<PotionEffectType> eff;

    @Override
    public boolean check(Event event) {
        Player p = player.getSingle(event);
        PotionEffectType t = eff.getSingle(event);
        if(p == null || t == null) return false;
        if(isNegated()) return !p.hasPotionEffect(t);
        else return p.hasPotionEffect(t);
    }

    @Override
    public String toString(Event event, boolean b) {
        return "can has potion";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        player = (Expression<Player>) expressions[0];
        eff = (Expression<PotionEffectType>) expressions[1];
        setNegated(i == 1);
        return true;
    }
}
