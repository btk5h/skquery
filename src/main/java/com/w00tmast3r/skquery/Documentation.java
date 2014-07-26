package com.w00tmast3r.skquery;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import com.w00tmast3r.skquery.api.*;
import com.w00tmast3r.skquery.util.Reflection;
import com.w00tmast3r.skquery.util.SkQueryInternalException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Documentation {

    private static HashMap<Class, String[]> events = new HashMap<Class, String[]>();
    private static ArrayList<Class<? extends Condition>> conditions = new ArrayList<Class<? extends Condition>>();
    private static ArrayList<Class<? extends Effect>> effects = new ArrayList<Class<? extends Effect>>();
    private static ArrayList<Class<? extends Expression>> expressions = new ArrayList<Class<? extends Expression>>();



    public static void generateDocs() {

        if (Reflection.getCaller().getProtectionDomain().getCodeSource().getLocation().sameFile(Documentation.class.getProtectionDomain().getCodeSource().getLocation())) {
            BufferedWriter writer = null;
            try {
                File htmlevents = new File(SkQuery.getInstance().getDataFolder().getAbsolutePath() + File.separator + "events.html");
                htmlevents.createNewFile();
                writer = new BufferedWriter(new FileWriter(htmlevents));
                for (Map.Entry<Class, String[]> e : events.entrySet()) {
                    writer.write("<div id=\""+ e.getKey().getSimpleName() + "\" class=\"content-group\">");
                    writer.newLine();
                    writer.write("<h4>" + e.getKey().getSimpleName() + "</h4>");
                    writer.newLine();
                    writer.write("<p>");
                    writer.newLine();
                    writer.write("Events cannot have descriptions right now");
                    writer.newLine();
                    writer.write("</p>");
                    writer.newLine();
                    writer.write("<div class=\"content-group\">");
                    writer.newLine();
                    writer.write("<h6>Usage</h6>");
                    writer.newLine();
                    writer.write("<code>");
                    writer.newLine();
                    for (String s : e.getValue()) {
                        writer.write("<span>[on] " + s +"</span>");
                        writer.write("<br/>");
                    }
                    writer.newLine();
                    writer.write("</code>");
                    writer.newLine();
                    writer.write("</div>");
                    writer.newLine();
                    writer.write("<div class=\"content-group\">");
                    writer.newLine();
                    writer.write("<h6>Examples</h6>");
                    writer.newLine();
                    writer.write("<span>&nbsp;</span>");
                    writer.newLine();
                    writer.write("</div>");
                    writer.newLine();
                    writer.write("</div>");
                    writer.newLine();
                }
                for (Class e : events.keySet()) {
                    writer.newLine();
                    writer.write("<a href=\"#" + e.getSimpleName() + "\" class=\"nav-item\">" + e.getSimpleName() + "</a>");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                File htmlconditions = new File(SkQuery.getInstance().getDataFolder().getAbsolutePath() + File.separator + "conditions.html");
                htmlconditions.createNewFile();
                writer = new BufferedWriter(new FileWriter(htmlconditions));
                for (Class<? extends Condition> e : conditions) {
                    if (e.isAnnotationPresent(DocumentationHidden.class)) continue;
                    writer.write("<div id=\""+ e.getSimpleName() + "\" class=\"content-group\">");
                    writer.newLine();
                    writer.write("<h4>" + (e.isAnnotationPresent(Name.class) ? e.getAnnotation(Name.class).value() : (e.getSimpleName().startsWith("Eff") ? e.getSimpleName().substring(3) : e.getSimpleName())) + "</h4>");
                    writer.newLine();
                    if (e.isAnnotationPresent(Description.class)) {
                        writer.write("<p>");
                        writer.newLine();
                        writer.write(e.getAnnotation(Description.class).value().replaceAll("\\(\\((.+)\\)(.+)\\)", "<a href=\"#$1\">$2</a>"));
                        writer.newLine();
                        writer.write("</p>");
                    }
                    writer.newLine();
                    writer.write("<div class=\"content-group\">");
                    writer.newLine();
                    writer.write("<h6>Usage</h6>");
                    writer.newLine();
                    writer.write("<code>");
                    writer.newLine();
                    for (String s : e.getAnnotation(Patterns.class).value()) {
                        writer.write("<span>" + s +"</span>");
                        writer.write("<br/>");
                    }
                    writer.newLine();
                    writer.write("</code>");
                    writer.newLine();
                    writer.write("</div>");
                    writer.newLine();
                    writer.write("<div class=\"content-group\">");
                    writer.newLine();
                    writer.write("<h6>Examples</h6>");
                    writer.newLine();
                    if (e.isAnnotationPresent(Examples.class)) {
                        writer.write("<code>");
                        writer.newLine();
                        for (String s : e.getAnnotation(Examples.class).value()) {
                            writer.write("<span>" + s +"</span>");
                            writer.write("<br/>");
                        }
                        writer.newLine();
                        writer.write("</code>");
                    } else {
                        writer.write("<span>&nbsp;</span>");
                    }
                    writer.newLine();
                    writer.write("</div>");
                    writer.newLine();
                    writer.write("</div>");
                    writer.newLine();
                }
                writer.newLine();
                for (Class<? extends Condition> e : conditions) {
                    writer.newLine();
                    writer.write("<a href=\"#" + e.getSimpleName() + "\" class=\"nav-item\">" + (e.isAnnotationPresent(Name.class) ? e.getAnnotation(Name.class).value() : (e.getSimpleName().startsWith("Expr") ? e.getSimpleName().substring(4) : e.getSimpleName())) + "</a>");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                File htmleffects = new File(SkQuery.getInstance().getDataFolder().getAbsolutePath() + File.separator + "effects.html");
                htmleffects.createNewFile();
                writer = new BufferedWriter(new FileWriter(htmleffects));
                for (Class<? extends Effect> e : effects) {
                    if (e.isAnnotationPresent(DocumentationHidden.class)) continue;
                    writer.write("<div id=\""+ e.getSimpleName() + "\" class=\"content-group\">");
                    writer.newLine();
                    writer.write("<h4>" + (e.isAnnotationPresent(Name.class) ? e.getAnnotation(Name.class).value() : (e.getSimpleName().startsWith("Eff") ? e.getSimpleName().substring(3) : e.getSimpleName())) + "</h4>");
                    writer.newLine();
                    if (e.isAnnotationPresent(Description.class)) {
                        writer.write("<p>");
                        writer.newLine();
                        writer.write(e.getAnnotation(Description.class).value().replaceAll("\\(\\((.+)\\)(.+)\\)", "<a href=\"#$1\">$2</a>"));
                        writer.newLine();
                        writer.write("</p>");
                    }
                    writer.newLine();
                    writer.write("<div class=\"content-group\">");
                    writer.newLine();
                    writer.write("<h6>Usage</h6>");
                    writer.newLine();
                    writer.write("<code>");
                    writer.newLine();
                    for (String s : e.getAnnotation(Patterns.class).value()) {
                        writer.write("<span>" + s +"</span>");
                        writer.write("<br/>");
                    }
                    writer.newLine();
                    writer.write("</code>");
                    writer.newLine();
                    writer.write("</div>");
                    writer.newLine();
                    writer.write("<div class=\"content-group\">");
                    writer.newLine();
                    writer.write("<h6>Examples</h6>");
                    writer.newLine();
                    if (e.isAnnotationPresent(Examples.class)) {
                        writer.write("<code>");
                        writer.newLine();
                        for (String s : e.getAnnotation(Examples.class).value()) {
                            writer.write("<span>" + s +"</span>");
                            writer.write("<br/>");
                        }
                        writer.newLine();
                        writer.write("</code>");
                    } else {
                        writer.write("<span>&nbsp;</span>");
                    }
                    writer.newLine();
                    writer.write("</div>");
                    writer.newLine();
                    writer.write("</div>");
                    writer.newLine();
                }
                writer.newLine();
                for (Class<? extends Effect> e : effects) {
                    writer.newLine();
                    writer.write("<a href=\"#" + e.getSimpleName() + "\" class=\"nav-item\">" + (e.isAnnotationPresent(Name.class) ? e.getAnnotation(Name.class).value() : (e.getSimpleName().startsWith("Expr") ? e.getSimpleName().substring(4) : e.getSimpleName())) + "</a>");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                File exprhtml = new File(SkQuery.getInstance().getDataFolder().getAbsolutePath() + File.separator + "expressions.html");
                exprhtml.createNewFile();
                writer = new BufferedWriter(new FileWriter(exprhtml));
                for (Class<? extends Expression> e : expressions) {
                    if (e.isAnnotationPresent(DocumentationHidden.class)) continue;
                    writer.write("<div id=\""+ e.getSimpleName() + "\" class=\"content-group\">");
                    writer.newLine();
                    writer.write("<h4>" + (e.isAnnotationPresent(Name.class) ? e.getAnnotation(Name.class).value() : (e.getSimpleName().startsWith("Expr") ? e.getSimpleName().substring(4) : e.getSimpleName())) + "</h4>");
                    writer.newLine();
                    if (e.isAnnotationPresent(Description.class)) {
                        writer.write("<p>");
                        writer.newLine();
                        writer.write(e.getAnnotation(Description.class).value().replaceAll("\\(\\((.+)\\)(.+)\\)", "<a href=\"#$1\">$2</a>"));
                        writer.newLine();
                        writer.write("</p>");
                    }
                    writer.newLine();
                    writer.write("<div class=\"content-group\">");
                    writer.newLine();
                    writer.write("<h6>Usage</h6>");
                    writer.newLine();
                    writer.write("<code>");
                    writer.newLine();
                    if (e.isAnnotationPresent(UsePropertyPatterns.class)) {
                        writer.write("<span>%" + e.getAnnotation(PropertyFrom.class).value() + "%'s " + e.getAnnotation(PropertyTo.class).value() + "</span><br/>");
                        writer.write("<span>[the] " + e.getAnnotation(PropertyTo.class).value() + " of %" + e.getAnnotation(PropertyFrom.class).value() + "%</span>");
                    } else {
                        for (String s : e.getAnnotation(Patterns.class).value()) {
                            writer.write("<span>" + s + "</span><br/>");
                        }
                    }
                    writer.newLine();
                    writer.write("</code>");
                    writer.newLine();
                    writer.write("</div>");
                    writer.newLine();
                    writer.write("<div class=\"content-group\">");
                    writer.newLine();
                    writer.write("<h6>Examples</h6>");
                    writer.newLine();
                    if (e.isAnnotationPresent(Examples.class)) {
                        writer.write("<code>");
                        writer.newLine();
                        for (String s : e.getAnnotation(Examples.class).value()) {
                            writer.write("<span>" + s +"</span>");
                            writer.write("<br/>");
                        }
                        writer.newLine();
                        writer.write("</code>");
                    } else {
                        writer.write("<span>&nbsp;</span>");
                    }
                    writer.newLine();
                    writer.write("</div>");
                    writer.newLine();
                    writer.write("</div>");
                    writer.newLine();
                }
                writer.newLine();
                for (Class<? extends Expression> e : expressions) {
                    writer.newLine();
                    writer.write("<a href=\"#" + e.getSimpleName() + "\" class=\"nav-item\">" + (e.isAnnotationPresent(Name.class) ? e.getAnnotation(Name.class).value() : (e.getSimpleName().startsWith("Expr") ? e.getSimpleName().substring(4) : e.getSimpleName())) + "</a>");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            throw new SkQueryInternalException("Documentation cannot be generated externally");
        }
    }

    public static void addEvent(Class c, String... patterns) {
        events.put(c, patterns);
    }

    public static void addCondition(Class<? extends Condition> c) {
        conditions.add(c);
    }

    public static void addEffect(Class<? extends Effect> c) {
        effects.add(c);
    }

    public static void addExpression(Class<? extends Expression> c) {
        expressions.add(c);
    }
}
