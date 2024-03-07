package net.strokkur.config;

import java.util.List;

/**
 * Class for holding the information needed to convert a string to a colored String <br>
 * > Use {@link ColorConfig#getColor(String)} to get the color <br>
 * > Use {@link #colorString(String)} to apply a color
 */
public class NameColor {

    private final String name;
    private final List<String> colorcodes;

    protected NameColor(String name, List<String> colorcodes) {
        this.name = name;
        this.colorcodes = colorcodes;
    }

    /**
     * Use this method to apply a color format to a String
     * @param str The String to apply the color format to
     * @return A formatted String
     *
     * Example:<br>
     * - Colors: <code>[&a, &b]</code><br>
     * - Original String: <code>Hello</code><br>
     * - new String: <code>&aH&be&al&bl&ao</code><br>
     */
    public String colorString(String str) {
        StringBuilder out = new StringBuilder();
        for (int i = str.length() - 1; i >= 0; i--) {
            String currentColor = colorcodes.get(str.length() - 1 - i % colorcodes.size());
            out.insert(0, currentColor + str.charAt(i));
        }

        return out.toString();
    }

    public String getColorArray() {
        StringBuilder out = new StringBuilder("&8[");
        for (int i = 0; i < colorcodes.size(); i++) {
            out.append(colorcodes.get(i)).append(colorcodes.get(i).replaceAll("[§&]", ".._;_.."));
            if (i < colorcodes.size() - 2) {
                out.append("&7, ");
            }
            else {
                out.append("&7 and ");
            }
        }

        return out.append("&8]").toString();
    }

    public String getName() {
        return name;
    }

}
