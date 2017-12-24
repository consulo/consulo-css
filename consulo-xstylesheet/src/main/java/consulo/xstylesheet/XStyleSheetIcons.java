/*
 * Copyright 2013 must-be.org
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

package consulo.xstylesheet;

import javax.swing.Icon;

import com.intellij.openapi.util.IconLoader;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
public interface XStyleSheetIcons
{
	Icon AtRule = IconLoader.findIcon("/consulo/xstylesheet/icons/atrule.png");
	Icon CssClass = IconLoader.findIcon("/consulo/xstylesheet/icons/css_class.png");
	Icon HtmlId = IconLoader.findIcon("/consulo/xstylesheet/icons/html_id.png");
	Icon Property = IconLoader.findIcon("/consulo/xstylesheet/icons/property.png");
	Icon PseudoElement = IconLoader.findIcon("/consulo/xstylesheet/icons/pseudo-element.png");
}