package com.w00tmast3r.skquery.elements.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.w00tmast3r.skquery.api.ManualDoc;
import com.w00tmast3r.skquery.api.PropertyPatterns;
import com.w00tmast3r.skriptaddon.skaddonlib.util.Collect;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;


@PropertyPatterns(
        property = "fl(y|ight) (state|ability|mode)",
        fromType = "players"
)
@ManualDoc(
        name = "Player Fly Mode",
        description = "A true/false expression representing whether or not a player can fly. This value may be reset to change a player's flight mode to it's default state."
)
public class ExprFlightMode extends SimplePropertyExpression<Player, Boolean> {

    @Override
    protected String getPropertyName() {
        return "fly state";
    }

    @Override
    public Boolean convert(Player player) {
        return player.getAllowFlight();
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.RESET || mode == Changer.ChangeMode.SET) return Collect.asArray(Boolean.class);
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        boolean b = delta != null && (Boolean) delta[0];
        switch (mode) {
            case SET:
                for (Player p : getExpr().getAll(e))  {
                    p.setAllowFlight(b);
                }
                break;
            case RESET:
                for (Player p : getExpr().getAll(e))  {
                    p.setAllowFlight(p.getGameMode() == GameMode.CREATIVE);
                }
            case ADD:
            case REMOVE:
            case REMOVE_ALL:
            case DELETE:
                assert false;
        }
    }
}
