package consulo.css.it.xml;

import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.Application;
import consulo.codeEditor.markup.GutterIconRenderer;
import consulo.language.Language;
import consulo.language.editor.Pass;
import consulo.language.editor.gutter.LineMarkerInfo;
import consulo.language.editor.gutter.LineMarkerProvider;
import consulo.language.editor.gutter.MergeableLineMarkerInfo;
import consulo.language.psi.ElementColorProvider;
import consulo.language.psi.PsiElement;
import consulo.ui.color.ColorValue;
import consulo.ui.image.Image;
import consulo.ui.image.ImageEffects;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

/**
 * As the ColorLineMarkerProvider of ide-impl (not on the consulo-it classpath): the color of the {@link ElementColorProvider}s
 * for the elements of any language.
 */
@ExtensionImpl
public class TestColorLineMarkerProvider implements LineMarkerProvider {
    public static class ColorInfo extends MergeableLineMarkerInfo<PsiElement> {
        @RequiredReadAction
        ColorInfo(PsiElement element, ColorValue color) {
            super(element, element.getTextRange(), ImageEffects.colorFilled(12, 12, color), Pass.UPDATE_ALL, a -> null, null,
                GutterIconRenderer.Alignment.LEFT);
        }

        @Override
        public boolean canMergeWith(MergeableLineMarkerInfo<?> info) {
            return info instanceof ColorInfo;
        }

        @Override
        public Image getCommonIcon(List<MergeableLineMarkerInfo> infos) {
            return ImageEffects.colorFilled(12, 12, new consulo.ui.color.RGBColor(255, 255, 255));
        }

        @Override
        public Function<? super PsiElement, String> getCommonTooltip(List<MergeableLineMarkerInfo> infos) {
            return a -> null;
        }
    }

    @Override
    public Language getLanguage() {
        return Language.ANY;
    }

    @Override
    @RequiredReadAction
    public @Nullable LineMarkerInfo getLineMarkerInfo(PsiElement element) {
        ColorValue color = Application.get().getExtensionPoint(ElementColorProvider.class).computeSafeIfAny(it -> it.getColorFrom(element));
        return color != null ? new ColorInfo(element, color) : null;
    }
}
