package com.w00tmast3r.skquery.elements.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.w00tmast3r.skquery.api.ManualDoc;
import com.w00tmast3r.skquery.api.PropertyPatterns;
import com.w00tmast3r.skriptaddon.skaddonlib.util.Collect;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;


@PropertyPatterns(
        property = "scaled health",
        fromType = "players"
)
@ManualDoc(
        name = "Scaled Health",
        description = "The health scale a player is shown. Health scale determines how many hearts a player can see and can be used to increase max hp without altering heart count."
)
public class ExprHealthScale extends SimplePropertyExpression<Player, Number> {

    @Override
    protected String getPropertyName() {
        return "scaled health";
    }

    @Override
    public Number convert(Player player) {
        return player.getHealthScale();
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.RESET || mode == Changer.ChangeMode.SET) return Collect.asArray(Number.class);
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        Number n = delta[0] == null || ((Number) delta[0]).doubleValue() < 0 ? 1 : (Number) delta[0] ;
        switch (mode) {
            case SET:
                for (Player p : getExpr().getAll(e))  {
                    p.setHealthScale(n.doubleValue() * 2);
                }
                break;
            case RESET:
                for (Player p : getExpr().getAll(e))  {
                    p.setHealthScaled(false);
                }
            case ADD:
            case REMOVE:
            case REMOVE_ALL:
            case DELETE:
                assert false;
        }
    }
}
