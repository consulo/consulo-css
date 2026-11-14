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
package consulo.css.it;

import consulo.ui.color.ColorValue;
import consulo.ui.color.RGBColor;
import consulo.xstylesheet.color.CssColorParser;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The CSS colors with a value of their own, as the gutter and the color picker read them
 */
public class CssColorParserTest {
    @Test
    public void hexNamedRgbHsl() {
        assertThat(rgb("#ff8000")).isEqualTo(new RGBColor(255, 128, 0, 1f));
        assertThat(rgb("white")).isEqualTo(new RGBColor(255, 255, 255, 1f));
        assertThat(rgb("rgb(0 128 255 / 50%)")).isEqualTo(new RGBColor(0, 128, 255, 0.5f));
        assertThat(rgb("rgba(0, 0, 0, 0.25)")).isEqualTo(new RGBColor(0, 0, 0, 0.25f));
        assertThat(rgb("hsl(120deg 100% 50%)")).isEqualTo(new RGBColor(0, 255, 0, 1f));
    }

    /**
     * The colors of the default theme of Tailwind CSS v4 against their sRGB values
     */
    @Test
    public void oklchAndOklab() {
        assertThat(rgb("oklch(63.7% 0.237 25.331)")).isEqualTo(new RGBColor(0xfb, 0x2c, 0x36, 1f));
        assertThat(rgb("oklch(0.623 0.214 259.815)")).isEqualTo(new RGBColor(0x2b, 0x7f, 0xff, 1f));
        assertThat(rgb("oklch(100% 0 0)")).isEqualTo(new RGBColor(255, 255, 255, 1f));
        assertThat(rgb("oklch(0% 0 none / 0.5)")).isEqualTo(new RGBColor(0, 0, 0, 0.5f));
        assertThat(rgb("oklab(0.628 0.2249 0.1258)")).isEqualTo(new RGBColor(255, 0, 0, 1f));
    }

    @Test
    public void notAColor() {
        assertThat(CssColorParser.parse("var(--color-red-500)")).isNull();
        assertThat(CssColorParser.parse("oklch(1)")).isNull();
        assertThat(CssColorParser.parse("12px")).isNull();
    }

    private static RGBColor rgb(String text) {
        ColorValue value = CssColorParser.parse(text);
        assertThat(value).as(text).isNotNull();
        return value.toRGB();
    }
}
