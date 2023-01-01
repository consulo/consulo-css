/**
 * @author VISTALL
 * @since 11-Sep-22
 */
module consulo.css.xstylesheet.api
{
	requires transitive consulo.language.api;
	requires transitive consulo.language.editor.api;
	requires transitive com.intellij.xml;

	exports consulo.xstylesheet.definition;
	exports consulo.xstylesheet.definition.impl;
	exports consulo.xstylesheet.definition.value.impl;
	exports consulo.xstylesheet.highlight;
	exports consulo.xstylesheet.html;
	exports consulo.xstylesheet.html.psi.reference.classOrId;
	exports consulo.xstylesheet.html.psi.reference.file;
	exports consulo.xstylesheet.icon;
	exports consulo.xstylesheet.psi;
	exports consulo.xstylesheet.psi.reference;
	exports consulo.xstylesheet.psi.reference.nameResolving;
}