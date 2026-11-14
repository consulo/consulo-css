/*
 * Copyright 2013-2026 consulo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package consulo.xstylesheet.color;

import consulo.ui.color.ColorValue;
import consulo.ui.color.RGBColor;
import consulo.ui.util.ColorValueUtil;
import consulo.xstylesheet.definition.XStyleSheetColor;
import org.jspecify.annotations.Nullable;

/**
 * Parses the CSS colors that have a value without the page: hex colors, named colors and the {@code rgb()},
 * {@code hsl()}, {@code oklab()} and {@code oklch()} functions, with or without alpha
 *
 * @since 2026-11-06
 */
public final class CssColorParser {
    private CssColorParser() {
    }

    /**
     * @return the color of {@code text}, or {@code null} when it isn't one of the colors above
     */
    public static @Nullable ColorValue parse(String text) {
        String lower = text.trim().toLowerCase();
        if (lower.startsWith("#")) {
            try {
                return ColorValueUtil.fromHex(lower);
            }
            catch (Exception e) {
                return null;
            }
        }
        XStyleSheetColor color = XStyleSheetColor.getColor(lower);
        if (color != null) {
            try {
                return ColorValueUtil.fromHex("#" + color.colorCode());
            }
            catch (Exception e) {
                return null;
            }
        }
        try {
            if (lower.startsWith("rgb")) {
                return parseRgb(arguments(lower));
            }
            if (lower.startsWith("hsl")) {
                return parseHsl(arguments(lower));
            }
            if (lower.startsWith("oklch(")) {
                String[] parts = arguments(lower);
                double hue = Math.toRadians(number(parts[2], 360));
                double chroma = parts[1].endsWith("%") ? number(parts[1], 0.4) : number(parts[1], 1);
                return fromOklab(lightness(parts[0]), chroma * Math.cos(hue), chroma * Math.sin(hue), alpha(parts));
            }
            if (lower.startsWith("oklab(")) {
                String[] parts = arguments(lower);
                return fromOklab(lightness(parts[0]), axis(parts[1]), axis(parts[2]), alpha(parts));
            }
        }
        catch (RuntimeException e) {
            return null;
        }
        return null;
    }

    /**
     * The arguments of a color function, separated by commas, whitespace or the slash before the alpha
     */
    private static String[] arguments(String lower) {
        int start = lower.indexOf('(');
        int end = lower.lastIndexOf(')');
        if (start < 0 || end < start) {
            throw new IllegalArgumentException(lower);
        }
        String[] parts = lower.substring(start + 1, end).trim().split("[,/\\s]+");
        if (parts.length < 3) {
            throw new IllegalArgumentException(lower);
        }
        return parts;
    }

    private static ColorValue parseRgb(String[] parts) {
        return color(component(parts[0]) / 255, component(parts[1]) / 255, component(parts[2]) / 255, alpha(parts));
    }

    private static ColorValue parseHsl(String[] parts) {
        double h = number(parts[0].replace("deg", ""), 360) / 360;
        double s = percent(parts[1]);
        double l = percent(parts[2]);
        double r;
        double g;
        double b;
        if (s == 0) {
            r = g = b = l;
        }
        else {
            double q = l < 0.5 ? l * (1 + s) : l + s - l * s;
            double p = 2 * l - q;
            r = hueToRgb(p, q, h + 1d / 3);
            g = hueToRgb(p, q, h);
            b = hueToRgb(p, q, h - 1d / 3);
        }
        return color(r, g, b, alpha(parts));
    }

    private static double hueToRgb(double p, double q, double t) {
        double hue = t < 0 ? t + 1 : t > 1 ? t - 1 : t;
        if (hue < 1d / 6) {
            return p + (q - p) * 6 * hue;
        }
        if (hue < 1d / 2) {
            return q;
        }
        if (hue < 2d / 3) {
            return p + (q - p) * (2d / 3 - hue) * 6;
        }
        return p;
    }

    /**
     * OKLab to sRGB, after https://bottosson.github.io/posts/oklab/
     */
    private static ColorValue fromOklab(double lightness, double a, double b, float alpha) {
        double l = Math.pow(lightness + 0.3963377774 * a + 0.2158037573 * b, 3);
        double m = Math.pow(lightness - 0.1055613458 * a - 0.0638541728 * b, 3);
        double s = Math.pow(lightness - 0.0894841775 * a - 1.2914855480 * b, 3);
        double red = 4.0767416621 * l - 3.3077115913 * m + 0.2309699292 * s;
        double green = -1.2684380046 * l + 2.6097574011 * m - 0.3413193965 * s;
        double blue = -0.0041960863 * l - 0.7034186147 * m + 1.7076147010 * s;
        return color(gamma(red), gamma(green), gamma(blue), alpha);
    }

    private static double gamma(double linear) {
        return linear <= 0.0031308 ? 12.92 * linear : 1.055 * Math.pow(linear, 1 / 2.4) - 0.055;
    }

    private static ColorValue color(double red, double green, double blue, float alpha) {
        return new RGBColor(channel(red), channel(green), channel(blue), alpha);
    }

    private static int channel(double value) {
        return (int) Math.round(Math.min(Math.max(value, 0), 1) * 255);
    }

    /**
     * A channel of {@code rgb()}: 0 to 255, or a percentage
     */
    private static double component(String value) {
        return value.endsWith("%") ? number(value, 100) * 255 / 100 : number(value, 255);
    }

    /**
     * The lightness of {@code oklab()} and {@code oklch()}: 0 to 1, or a percentage
     */
    private static double lightness(String value) {
        return value.endsWith("%") ? number(value, 100) / 100 : number(value, 1);
    }

    /**
     * The a or b axis of {@code oklab()}: a number, or a percentage of 0.4
     */
    private static double axis(String value) {
        return value.endsWith("%") ? number(value, 100) * 0.4 / 100 : number(value, 1);
    }

    private static double percent(String value) {
        return number(value, 100) / 100;
    }

    private static float alpha(String[] parts) {
        if (parts.length < 4) {
            return 1f;
        }
        String value = parts[3];
        return (float) (value.endsWith("%") ? number(value, 100) / 100 : number(value, 1));
    }

    /**
     * A number, without its unit; {@code none} is 0
     */
    private static double number(String value, double max) {
        String number = value.replaceAll("[a-z%]+$", "");
        if (value.equals("none") || number.isEmpty()) {
            return 0;
        }
        return Math.min(Double.parseDouble(number), max);
    }
}
