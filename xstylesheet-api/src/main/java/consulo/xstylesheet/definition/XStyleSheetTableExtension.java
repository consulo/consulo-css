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

package consulo.xstylesheet.definition;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.jdom.Document;
import org.jdom.Element;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.AbstractExtensionPointBean;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.openapi.util.NotNullLazyValue;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ReflectionUtil;
import com.intellij.util.xmlb.annotations.Attribute;
import consulo.xstylesheet.definition.impl.EmptyXStyleSheetTable;
import consulo.xstylesheet.definition.impl.XStyleSheetPropertyImpl;
import consulo.xstylesheet.definition.impl.XStyleSheetPropertyValueEntryImpl;
import consulo.xstylesheet.definition.impl.XStyleSheetPropertyValuePartImpl;
import consulo.xstylesheet.definition.impl.XStyleSheetTableImpl;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class XStyleSheetTableExtension extends AbstractExtensionPointBean
{
	private static final Logger LOGGER = Logger.getInstance(XStyleSheetTableExtension.class);

	public static final ExtensionPointName<XStyleSheetTableExtension> EP_NAME = ExtensionPointName.create("consulo.xstylesheet.table");

	@Attribute("file")
	public String file;

	//@Attribute("condition")
	// public Condition<PsiFile> condition = Condition.TRUE;

	private NotNullLazyValue<XStyleSheetTable> myLazyTableValue = new NotNullLazyValue<XStyleSheetTable>()
	{
		@Nonnull
		@Override
		protected XStyleSheetTable compute()
		{
			InputStream stream = getLoaderForClass().getResourceAsStream(file);
			if(stream == null)
			{
				LOGGER.error("File " + file + " not found for loader: " + getLoaderForClass());
				return EmptyXStyleSheetTable.INSTANCE;
			}
			return loadDocument(stream);
		}
	};

	public XStyleSheetTable getTable()
	{
		return myLazyTableValue.getValue();
	}

	private XStyleSheetTable loadDocument(InputStream stream)
	{
		try
		{
			Map<String, XStyleSheetPropertyValuePartParser> valueDefinitions = new HashMap<String, XStyleSheetPropertyValuePartParser>();
			List<XStyleSheetProperty> properties = new ArrayList<XStyleSheetProperty>();

			Document document = JDOMUtil.loadDocument(stream);
			for(Element element : document.getRootElement().getChildren())
			{
				String name = element.getName();
				if("value-definition".equals(name))
				{
					String tempName = element.getAttributeValue("name");
					Class<? extends XStyleSheetPropertyValuePartParser> clazz = findClass(element.getAttributeValue("implClass"));

					valueDefinitions.put(tempName, ReflectionUtil.createInstance(ReflectionUtil.getDefaultConstructor(clazz)));
				}
				else if("property".equals(name))
				{
					String propertyName = element.getAttributeValue("name");

					List<XStyleSheetPropertyValueEntry> validEntries = parseEntries(valueDefinitions, element.getChild("valid-values"));
					List<XStyleSheetPropertyValueEntry> initialEntries = parseEntries(valueDefinitions, element.getChild("initial-values"));

					properties.add(new XStyleSheetPropertyImpl(propertyName, validEntries, initialEntries));
				}
			}

			return new XStyleSheetTableImpl(properties);
		}
		catch(Exception e)
		{
			LOGGER.error(e);
		}
		return EmptyXStyleSheetTable.INSTANCE;
	}

	private List<XStyleSheetPropertyValueEntry> parseEntries(Map<String, XStyleSheetPropertyValuePartParser> valueDefinitions, Element parent)
	{
		if(parent == null)
		{
			return Collections.emptyList();
		}

		List<XStyleSheetPropertyValueEntry> entries = new ArrayList<XStyleSheetPropertyValueEntry>();
		// entry
		for(Element child : parent.getChildren())
		{
			List<XStyleSheetPropertyValuePart> parts = new ArrayList<XStyleSheetPropertyValuePart>();
			for(Element child2 : child.getChildren())
			{
				XStyleSheetPropertyValuePartParser parser = valueDefinitions.get(child2.getName());

				parts.add(new XStyleSheetPropertyValuePartImpl(parser, StringUtil.nullize(child2.getText(), true)));
			}
			entries.add(new XStyleSheetPropertyValueEntryImpl(parts));
		}
		return entries;
	}
}
