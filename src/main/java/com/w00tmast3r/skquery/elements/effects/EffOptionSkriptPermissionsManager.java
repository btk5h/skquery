package com.w00tmast3r.skquery.elements.effects;

import ch.njol.skript.lang.SkriptParser;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skquery.elements.effects.base.OptionsPragma;
import com.w00tmast3r.skquery.skript.PermissionsHandler;

import java.io.File;

@Patterns("$ use permissions")
public class EffOptionSkriptPermissionsManager extends OptionsPragma {
    @Override
    protected void register(File executingScript, SkriptParser.ParseResult parseResult) {
        PermissionsHandler.enable();
    }
}
