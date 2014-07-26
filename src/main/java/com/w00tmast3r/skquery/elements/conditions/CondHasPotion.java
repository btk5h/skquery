package com.w00tmast3r.skquery.elements.conditions;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Description;
import com.w00tmast3r.skquery.api.Name;
import com.w00tmast3r.skquery.api.Patterns;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffectType;

@Name("Has Potion")
@Description("Checks whether or not an entity has a certain potion effect on it.")
@Patterns({"%livingentities% has %potioneffecttype%","%livingentities% does(n't| not) have %potioneffecttype%"})
public class CondHasPotion extends Condition {

    private Expression<LivingEntity> entity;
    private Expression<PotionEffectType> eff;

    @Override
    public boolean check(Event event) {
        LivingEntity e = entity.getSingle(event);
        PotionEffectType t = eff.getSingle(event);
        if(e == null || t == null) return false;
        if(isNegated()) return !e.hasPotionEffect(t);
        else return e.hasPotionEffect(t);
    }

    @Override
    public String toString(Event event, boolean b) {
        return "can has potion";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        entity = (Expression<LivingEntity>) expressions[0];
        eff = (Expression<PotionEffectType>) expressions[1];
        setNegated(i == 1);
        return true;
    }
}
