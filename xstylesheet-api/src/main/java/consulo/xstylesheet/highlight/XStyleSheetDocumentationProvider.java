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

package consulo.xstylesheet.highlight;

import consulo.annotation.access.RequiredReadAction;
import consulo.language.editor.documentation.LanguageDocumentationProvider;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiManager;
import consulo.xstylesheet.definition.XStyleSheetProperty;
import consulo.xstylesheet.psi.PsiXStyleSheetProperty;

import org.jspecify.annotations.Nullable;
import java.util.List;

/**
 * Provides documentation for CSS/stylesheet properties using data from the
 * property definition (syntax, initial value, inheritance, MDN URL).
 *
 * @author VISTALL
 * @since 2026-03-16
 */
public abstract class XStyleSheetDocumentationProvider implements LanguageDocumentationProvider {
    @Nullable
    @Override
    public String getQuickNavigateInfo(PsiElement element, PsiElement originalElement) {
        XStyleSheetProperty property = resolveProperty(element);
        if (property == null) {
            return null;
        }

        String syntax = property.getSyntax();
        if (syntax.isEmpty()) {
            return property.getName();
        }

        return property.getName() + ": " + syntax;
    }

    @Nullable
    @Override
    @RequiredReadAction
    public String generateDoc(PsiElement element, @Nullable PsiElement originalElement) {
        XStyleSheetProperty property = resolveProperty(element);
        if (property == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        // Definition section
        sb.append("<div class='definition'><pre>");
        sb.append("<b>").append(escapeHtml(property.getName())).append("</b>");
        sb.append("</pre></div>");

        // Content sections
        sb.append("<div class='content'>");
        sb.append("<table class='sections'>");

        // Syntax
        String syntax = property.getSyntax();
        if (!syntax.isEmpty()) {
            appendSection(sb, "Syntax", "<code>" + escapeHtml(syntax) + "</code>");
        }

        // Initial value
        String initial = property.getInitialValue();
        if (!initial.isEmpty()) {
            appendSection(sb, "Initial value", escapeHtml(initial));
        }

        // Inherited
        appendSection(sb, "Inherited", property.isInherited() ? "yes" : "no");

        // MDN URL
        String mdnUrl = property.getMdnUrl();
        if (!mdnUrl.isEmpty()) {
            appendSection(sb, "Reference", "<a href=\"" + escapeHtml(mdnUrl) + "\">MDN Web Docs</a>");
        }

        sb.append("</table>");
        sb.append("</div>");

        return sb.toString();
    }

    @Nullable
    @Override
    public List<String> getUrlFor(PsiElement element, PsiElement originalElement) {
        XStyleSheetProperty property = resolveProperty(element);
        if (property == null) {
            return null;
        }

        String mdnUrl = property.getMdnUrl();
        if (!mdnUrl.isEmpty()) {
            return List.of(mdnUrl);
        }

        return null;
    }

    @Nullable
    @Override
    public PsiElement getDocumentationElementForLookupItem(PsiManager psiManager, Object object, PsiElement element) {
        return null;
    }

    @Nullable
    @Override
    public PsiElement getDocumentationElementForLink(PsiManager psiManager, String link, PsiElement context) {
        return null;
    }

    @Nullable
    @RequiredReadAction
    private static XStyleSheetProperty resolveProperty(PsiElement element) {
        if (element instanceof PsiXStyleSheetProperty psiProperty) {
            return psiProperty.getXStyleSheetProperty();
        }
        // When hovering on the property name identifier token, check the parent
        PsiElement parent = element.getParent();
        if (parent instanceof PsiXStyleSheetProperty psiProperty) {
            return psiProperty.getXStyleSheetProperty();
        }
        return null;
    }

    private static void appendSection(StringBuilder sb, String title, String content) {
        sb.append("<tr>");
        sb.append("<td valign='top' class='section'><p>").append(title).append(":");
        sb.append("</td>");
        sb.append("<td valign='top'><p>").append(content);
        sb.append("</td>");
        sb.append("</tr>");
    }

    private static String escapeHtml(String text) {
        return text.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;");
    }
}
