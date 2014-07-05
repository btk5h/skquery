package com.w00tmast3r.skquery.elements.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.w00tmast3r.skquery.api.PropertyPatterns;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;


@PropertyPatterns(
        fromType = "player",
        property = "(current|open) inventory"
)
public class ExprOpenInventory extends SimplePropertyExpression<Player, Inventory> {

    @Override
    protected String getPropertyName() {
        return "open inventory";
    }

    @Override
    public Inventory convert(Player player) {
        return player.getOpenInventory().getTopInventory();
    }

    @Override
    public Class<? extends Inventory> getReturnType() {
        return Inventory.class;
    }
}
