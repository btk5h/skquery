package com.w00tmast3r.skquery.util.maps;

public enum MapColor {

    TRANSPARENT((byte) 0),
    GRASS((byte) 1),
    SAND((byte) 2),
    @Deprecated
    AMBIGUOUS_COLOR((byte) 3),
    RED((byte) 4),
    SKY_BLUE((byte) 5),
    GRAY((byte) 6),
    GREEN((byte) 7),
    WHITE((byte) 8),
    LIGHT_GRAY((byte) 9),
    LIGHT_BROWN((byte) 10),
    DARK_GRAY((byte) 11),
    BLUE((byte) 12),
    FADED_BROWN((byte) 13),
    FADED_WHITE((byte) 14),
    GENERAL_ORANGE((byte) 15),
    GENERAL_MAGENTA((byte) 16),
    GENERAL_LIGHT_BLUE((byte) 17),
    GENERAL_YELLOW((byte) 18),
    GENERAL_LIME((byte) 19),
    GENERAL_PINK((byte) 20),
    GENERAL_GRAY((byte) 21),
    GENERAL_LIGHT_GRAY((byte) 22),
    GENERAL_CYAN((byte) 23),
    GENERAL_PURPLE((byte) 24),
    GENERAL_BLUE((byte) 25),
    GENERAL_BROWN((byte) 26),
    GENERAL_GREEN((byte) 27),
    GENERAL_RED((byte) 28),
    GENERAL_BLACK((byte) 29),
    GOLD((byte) 30),
    DIAMOND_BLUE((byte) 31),
    LAPIS_BLUE((byte) 32),
    EMERALD_GREEN((byte) 33),
    OBSIDIAN_BLACK((byte) 34),
    COLOR_NETHER((byte) 35);

    private final byte baseColor;

    MapColor(byte baseColor) {
        this.baseColor = baseColor;
    }

    public byte color() {
        return color(Variant.LIGHTEST);
    }

    public byte color(Variant shade) {
        return Variant.add(this, shade);
    }

    public String format() {
        return format(Variant.LIGHTEST);
    }

    public String format(Variant shade) {
        return "\u00A7" + String.valueOf(Variant.add(this, shade));
    }

    public static enum Variant {
        LIGHTEST((byte) 2),
        LIGHT((byte) 1),
        DARK((byte) 0),
        DARKEST((byte) 3);

        private final byte baseShift;

        Variant(byte baseShift) {
            this.baseShift = baseShift;
        }

        public static byte add(MapColor color, Variant shade) {
            return (byte) ((color.baseColor * 4) + shade.baseShift);
        }
    }
}
