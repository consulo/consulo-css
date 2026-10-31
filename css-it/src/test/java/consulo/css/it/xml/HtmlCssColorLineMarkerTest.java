package consulo.css.it.xml;

import consulo.it.AllowLogError;
import consulo.it.CodeInsightTestFixture;
import consulo.it.HeadlessProjectExtension;
import consulo.language.editor.gutter.LineMarkerInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The color gutter of a stylesheet embedded into HTML: one icon per color value, as in a CSS file. The markers are
 * collected by the line marker pass, with {@link TestColorLineMarkerProvider} in place of the ColorLineMarkerProvider
 * of the IDE.
 */
@ExtendWith(HeadlessProjectExtension.class)
@AllowLogError({
    "consulo.language.impl.internal.psi.resolve.reference.ReferenceProvidersRegistryImpl",
    "consulo.xml.impl.internal.ExternalResource",
    "consulo.component.internal.NewExtensionPointImpl"
})
public class HtmlCssColorLineMarkerTest {
    @Test
    public void testStyleTag(CodeInsightTestFixture fixture) throws Exception {
        fixture.configureByText("test.html", """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <title>Title </title>
                <style>
                    .color {
                        border: 1px solid #bbb;
                    }
                </style>
            </head>
            <body>
            </body>
            </html>
            """);
        assertSingleColor(fixture);
    }

    @Test
    public void testCssFile(CodeInsightTestFixture fixture) throws Exception {
        fixture.configureByText("test.css", """
            .color {
                border: 1px solid #bbb;
            }
            """);
        assertSingleColor(fixture);
    }

    private static void assertSingleColor(CodeInsightTestFixture fixture) {
        List<LineMarkerInfo<?>> markers = fixture.getLineMarkers();
        // the markers of one line are merged: two colors for #bbb come out as one merged marker, not a ColorInfo
        assertThat(markers).singleElement().isInstanceOf(TestColorLineMarkerProvider.ColorInfo.class);
        assertThat(markers.get(0).startOffset).isEqualTo(fixture.getDocument().getText().indexOf("#bbb"));
    }
}
