package com.w00tmast3r.skquery.elements.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.w00tmast3r.skquery.api.PropertyPatterns;
import com.w00tmast3r.skriptaddon.skriptplus.util.comphenix.InventorySerialUtils;
import org.bukkit.entity.Player;


@PropertyPatterns(
        property = "serialized inventory",
        fromType = "player"
)
public class ExprSerial extends SimplePropertyExpression<Player, String> {
    @Override
    protected String getPropertyName() {
        return "serial";
    }

    @Override
    public String convert(Player player) {
        return InventorySerialUtils.toBase64(player.getInventory());
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }
}
