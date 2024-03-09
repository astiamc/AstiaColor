package net.strokkur.config;

import java.util.List;

/**
 * Class for holding the information needed to convert a string to a colored String <br>
 * > Use {@link NameColorConfig#getColor(String)} to get the color <br>
 * > Use {@link #colorString(String)} to apply a color
 */
public class NameColor {

    private final String name;
    private final List<String> colorcodes;

    protected NameColor(String name, List<String> colorcodes) {
        this.name = name;
        this.colorcodes = colorcodes;

        if (colorcodes == null || colorcodes.isEmpty()) {
            throw new RuntimeException("For some reason the colorcodes for " + name + " are empty / null. What the actual fuck.");
        }
    }

    /**
     * Use this method to apply a color format to a String
     * @param str The String to apply the color format to
     * @return A formatted String
     * <p>
     * Example:<br>
     * - Colors: <code>[&a, &b]</code><br>
     * - Original String: <code>Hello</code><br>
     * - new String: <code>&aH&be&al&bl&ao</code><br>
     */
    public String colorString(String str) {
        StringBuilder out = new StringBuilder();
        int n = str.length() - 1;

        for (int i = str.length() - 1; i >= 0; i--) {
            String currentColor = colorcodes.get(n % colorcodes.size());
            if (currentColor.length() % 2 == 1) {
                currentColor = currentColor.substring(0, 2);
            }

            out.insert(0, currentColor + str.charAt(i));
            n--;
        }

        return out.toString();
    }

    public String getColorArray() {
        StringBuilder out = new StringBuilder("&8[");
        if (colorcodes.size() == 1) {
            return "§8[" + colorcodes.get(0).substring(0, 2) + colorcodes.get(0).substring(0, 2).replaceAll("[§&]", ".._;_..") + "§8]";
        }

        for (int i = 0; i < colorcodes.size(); i++) {
            out.append(colorcodes.get(i), 0, 2).append(colorcodes.get(i).replaceAll("[§&]", ".._;_.."));
            if (i == colorcodes.size() - 1) {
                break;
            }

            if (i < colorcodes.size() - 2) {
                out.append("&7, ");
            }
            else {
                out.append("&7 .._;_.. ");
            }
        }

        return out.append("&8]").toString();
    }

    public String getName() {
        return name;
    }

}
