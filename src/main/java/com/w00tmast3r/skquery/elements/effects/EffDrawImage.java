package com.w00tmast3r.skquery.elements.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.ManualDoc;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skriptaddon.skriptplus.util.maps.RenderTask;
import com.w00tmast3r.skriptaddon.skriptplus.util.maps.SkriptMapRenderer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;


@Patterns({"draw [buffered[ ]]image %image% on [map] %number%",
        "draw [buffered[ ]]image %image% on [map] %number% [starting] from %number%, %number%"})
@ManualDoc(
        name = "Draw Image On Map",
        description = "Draws an image onto a map. If coordinates are not specified, the image will be drawn from the top left corner of the map canvas. Maps must first be managed by the Manage Map effect."
)
public class EffDrawImage extends Effect {

    private Expression<BufferedImage> image;
    private Expression<Number> id, x, y;
    private boolean useCoordinates;

    @Override
    protected void execute(Event event) {
        final BufferedImage i = image.getSingle(event);
        Number n = id.getSingle(event);
        int xO = 0;
        int yO = 0;
        if (useCoordinates) {
            Number xC = x.getSingle(event);
            Number yC = y.getSingle(event);
            if (xC == null || yC == null) return;
            xO = xC.intValue();
            yO = yC.intValue();
        }
        if (n == null || i == null) return;
        short id = n.shortValue();
        final int fXO = xO;
        final int fYO = yO;
        MapView mapView;
        try {
            mapView = Bukkit.getMap(id);
        } catch (Exception e) {
            Bukkit.getLogger().warning("Map " + id + " has not been initialized yet!");
            return;
        }
        SkriptMapRenderer renderer = SkriptMapRenderer.getRenderer(mapView);
        if (renderer == null) return;
        renderer.update(new RenderTask() {
            @Override
            public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
                drawImage(mapCanvas, i, fXO, fYO);
            }
        });
    }

    @Override
    public String toString(Event event, boolean b) {
        return "manage skript maps";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        image = (Expression<BufferedImage>) expressions[0];
        id = (Expression<Number>) expressions[1];
        useCoordinates = i == 1;
        if (useCoordinates) {
            x = (Expression<Number>) expressions[2];
            y = (Expression<Number>) expressions[3];
        }
        return true;
    }

    public void drawImage(MapCanvas canvas, BufferedImage img, int posX, int posY) {
        try {
            int h = img.getHeight();
            int w = img.getWidth();
            int i = 0;
            int[] pixels = new int[w * h];
            PixelGrabber grabber = new PixelGrabber(img, 0, 0, w, h, pixels, 0, w);
            grabber.grabPixels();
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int pixel = pixels[i++];
                    if (((pixel >> 24) & 0xFF) >= 127) {
                        canvas.setPixel(posX + x, posY + y, MapPalette.matchColor(((pixel >> 16) & 0xFF), ((pixel >> 8) & 0xFF), (pixel & 0xFF)));
                    }
                }
            }
        } catch (InterruptedException ignored) {
        }
    }
}
