package com.w00tmast3r.skquery.elements.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.w00tmast3r.skquery.api.Dependency;
import com.w00tmast3r.skquery.api.Patterns;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

@Dependency("ProtocolLib")
@Patterns("glowing %itemstacks%")
public class ExprGlowingItemStack extends SimplePropertyExpression<ItemStack, ItemStack> {

    @Override
    protected String getPropertyName() {
        return "glowy forme";
    }

    @Override
    public ItemStack convert(ItemStack itemStack) {
        if (itemStack.getType() == Material.BOW) itemStack.addUnsafeEnchantment(Enchantment.WATER_WORKER, 69);
        else itemStack.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 69);
        return itemStack;
    }

    @Override
    public Class<? extends ItemStack> getReturnType() {
        return ItemStack.class;
    }
}
