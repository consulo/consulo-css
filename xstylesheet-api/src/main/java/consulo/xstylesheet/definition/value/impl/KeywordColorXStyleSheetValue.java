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

package consulo.xstylesheet.definition.value.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.daemon.impl.HighlightInfoType;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.ui.ColorUtil;
import com.intellij.util.ui.ColorIcon;
import com.intellij.util.ui.JBUI;
import consulo.ui.shared.ColorValue;
import consulo.ui.util.ColorValueUtil;
import consulo.xstylesheet.highlight.XStyleSheetColors;
import consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class KeywordColorXStyleSheetValue extends TextBasedXStyleSheetPropertyValuePartParser
{
	private Map<String, String> myDefaultColors = new HashMap<String, String>()
	{
		{
			put("maroon", "#800000");
			put("red", "#ff0000");
			put("orange", "#ffA500");
			put("yellow", "#ffff00");
			put("olive", "#808000");
			put("purple", "#800080");
			put("fuchsia", "#ff00ff");
			put("white", "#ffffff");
			put("lime", "#00ff00");
			put("green", "#008000");
			put("navy", "#000080");
			put("blue", "#0000f0");
			put("faqua", "#00ffff");
			put("teal", "#008080");
			put("black", "#000000");
			put("silver", "#c0c0c0");
			put("gray", "#808080");
		}
	};

	@Nonnull
	@Override
	public List<HighlightInfo> createHighlights(@Nonnull PsiXStyleSheetPropertyValuePart valuePart)
	{
		HighlightInfo.Builder builder = HighlightInfo.newHighlightInfo(HighlightInfoType.INFORMATION);
		builder.textAttributes(XStyleSheetColors.KEYWORD);
		builder.range(valuePart);
		return Collections.singletonList(builder.create());
	}

	@Nullable
	@Override
	public ColorValue fromString(@Nonnull String stringValue, String value)
	{
		stringValue = stringValue.toLowerCase();
		stringValue = myDefaultColors.get(stringValue);
		if(stringValue == null)
		{
			return null;
		}

		try
		{
			return ColorValueUtil.fromHex(stringValue);
		}
		catch(Exception e)
		{
			return null;
		}
	}

	@Nonnull
	@Override
	public List<LookupElement> getLookupElements(String value)
	{
		List<LookupElement> list = new ArrayList<>();
		for(Map.Entry<String, String> entry : myDefaultColors.entrySet())
		{
			LookupElementBuilder builder = LookupElementBuilder.create(entry.getKey());
			builder = builder.withIcon((javax.swing.Icon) new ColorIcon(JBUI.scale(12), ColorUtil.fromHex(entry.getValue())));
			builder = builder.withTypeText(entry.getValue(), true);
			list.add(builder);
		}
		return list;
	}
}