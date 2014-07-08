package com.w00tmast3r.skquery;

import ch.njol.skript.Skript;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.lang.util.SimpleLiteral;
import com.w00tmast3r.skquery.api.*;
import com.w00tmast3r.skquery.elements.effects.base.OptionsPragma;
import com.w00tmast3r.skquery.elements.effects.base.Pragma;
import com.w00tmast3r.skquery.util.IterableEnumeration;
import com.w00tmast3r.skquery.util.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Registration {

    /**
     * Allow skQuery to iterate through your plugin source and reflectively
     * register your classes. This method MUST be called from within your
     * plugin, for safety reasons.
     */
    public static void enableSnooper() {
        final Class caller = Reflection.getCaller();
        final URL callerLocation = caller.getProtectionDomain().getCodeSource().getLocation();
        Bukkit.getLogger().info("[skQuery] Snooping enabled from " + caller.getCanonicalName());
        try {
            File src;
            try {
                src = new File(callerLocation.toURI());
            } catch (URISyntaxException e) {
                src = new File(callerLocation.getPath());
            }
            PluginDescriptionFile desc = SkQuery.getInstance().getPluginLoader().getPluginDescription(src);
            Bukkit.getLogger().info("[skQuery] Locating classes from " + desc.getName() + "...");
            try {
                ArrayList<Class> classes = new ArrayList<Class>();
                JarFile jar = new JarFile(src);
                for (JarEntry e : new IterableEnumeration<JarEntry>(jar.entries())) {
                    if (e.getName().endsWith(".class")) {
                        String className = e.getName().replace('/', '.').substring(0, e.getName().length() - 6);
                        try {
                            Class c = Class.forName(className, false, caller.getClassLoader());
                            if (c == Pragma.class || c == OptionsPragma.class || c == AbstractTask.class) continue;
                            if (Effect.class.isAssignableFrom(c)
                                    || Condition.class.isAssignableFrom(c)
                                    || Expression.class.isAssignableFrom(c)
                                    || SimpleExpression.class.isAssignableFrom(c)
                                    || PropertyExpression.class.isAssignableFrom(c)
                                    || SimplePropertyExpression.class.isAssignableFrom(c)
                                    || SimpleLiteral.class.isAssignableFrom(c)
                                    || AbstractTask.class.isAssignableFrom(c)) {
                                classes.add(c);
                            }
                        } catch (ClassNotFoundException error) {
                            error.printStackTrace();
                        } catch (NoClassDefFoundError ignored) {
                        } catch (ExceptionInInitializerError ignored) {}
                    }
                }
                Bukkit.getLogger().info("[skQuery] Finished snooping of " + desc.getName() + " with " + classes.size() + " classes.");
                register(desc, classes.toArray(new Class[classes.size()]));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (InvalidDescriptionException e) {
            e.printStackTrace();
        }
    }

    private static void register(PluginDescriptionFile info, Class[] classes) {
        int success = 0;
        Bukkit.getLogger().info("[skQuery] Beginning to process a total of " + classes.length + " from " + info.getName());
        main: for (final Class c : classes) {
            Annotation[] annotations = c.getAnnotations();
            Patterns patterns = null;
            for (Annotation a : annotations) {
                if (a instanceof Dependency) {
                    for (String s : ((Dependency) a).value()) {
                        if (Bukkit.getPluginManager().getPlugin(s) == null) {
                            continue main;
                        }
                    }
                }
            }
            if (Effect.class.isAssignableFrom(c) || Pragma.class.isAssignableFrom(c)) {
                if (c.isAnnotationPresent(Patterns.class)) {
                    Skript.registerEffect(c, ((Patterns) c.getAnnotation(Patterns.class)).value());
                    success++;
                } else {
                    Bukkit.getLogger().info("[skQuery] " + c.getCanonicalName() + " is patternless and failed to register. This is most likely a code error.");
                }
            } else if (Condition.class.isAssignableFrom(c)) {
                if (c.isAnnotationPresent(Patterns.class)) {
                    Skript.registerCondition(c, ((Patterns) c.getAnnotation(Patterns.class)).value());
                    success++;
                } else {
                    Bukkit.getLogger().info("[skQuery] " + c.getCanonicalName() + " is patternless and failed to register. This is most likely a code error.");
                }
            } else if ((Expression.class.isAssignableFrom(c))
                    || (SimpleExpression.class.isAssignableFrom(c))
                    || (PropertyExpression.class.isAssignableFrom(c))
                    || (SimplePropertyExpression.class.isAssignableFrom(c))) {
                if (c.isAnnotationPresent(Patterns.class)) {
                    try {
                        Expression ex = (Expression) c.newInstance();
                        Skript.registerExpression(c, ex.getReturnType(), ExpressionType.PROPERTY, ((Patterns) c.getAnnotation(Patterns.class)).value());
                        success++;
                    } catch (InstantiationException e) {
                        Bukkit.getLogger().info("[skQuery] " + c.getCanonicalName() + " could not be instantiated by skQuery!");
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } else if (c.isAnnotationPresent(UsePropertyPatterns.class) && c.isAnnotationPresent(PropertyFrom.class) && c.isAnnotationPresent(PropertyTo.class) ) {
                    try {
                        Expression ex = (Expression) c.newInstance();
                        Skript.registerExpression(c, ex.getReturnType(), ExpressionType.PROPERTY,
                                "[the] " + ((PropertyTo) c.getAnnotation(PropertyTo.class)).value() + " of %" + ((PropertyFrom) c.getAnnotation(PropertyFrom.class)).value() + "%",
                                "%" + ((PropertyFrom) c.getAnnotation(PropertyFrom.class)).value() + "%'[s] " + ((PropertyTo) c.getAnnotation(PropertyTo.class)).value());
                        success++;
                    } catch (InstantiationException e) {
                        Bukkit.getLogger().info("[skQuery] " + c.getCanonicalName() + " could not be instantiated by skQuery!");
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } else {
                    Bukkit.getLogger().info("[skQuery] " + c.getCanonicalName() + " is patternless and failed to register. This is most likely a code error.");
                }
            } else if (AbstractTask.class.isAssignableFrom(c)) {
                try {
                    AbstractTask task = (AbstractTask) c.newInstance();
                    task.run();
                    success++;
                } catch (InstantiationException e) {
                    Bukkit.getLogger().info("[skQuery] " + c.getCanonicalName() + " could not be instantiated by skQuery!");
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else assert false;
        }
        Bukkit.getLogger().info("[skQuery] Out of " + classes.length + " classes, " + success + " classes were loaded from " + info.getName());
    }

}
