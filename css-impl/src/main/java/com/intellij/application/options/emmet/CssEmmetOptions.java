package com.intellij.application.options.emmet;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.google.common.collect.Sets;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.xmlb.XmlSerializerUtil;

/**
 * @author VISTALL
 * @since 23.08.13.
 */
@State(
		name = "CssEmmetOptions",
		storages = {
				@Storage(
						file = StoragePathMacros.APP_CONFIG + "/editor.xml"
				)
		}
)
public class CssEmmetOptions implements PersistentStateComponent<CssEmmetOptions>
{
	@NotNull
	public static CssEmmetOptions getInstance()
	{
		return ServiceManager.getService(CssEmmetOptions.class);
	}

	private static final Logger LOGGER = Logger.getInstance(CssEmmetOptions.class);

	private boolean myFuzzySearchEnabled = true;
	private boolean myAutoInsertCssPrefixedEnabled = true;
	@Nullable
	private Map<String, Integer> prefixes = null;

	public void setPrefixInfo(Collection<CssPrefixInfo> prefixInfos)
	{
		prefixes = new HashMap<String, Integer>();

		for(CssPrefixInfo state : prefixInfos)
		{
			prefixes.put(state.getPropertyName(), state.toIntegerValue());
		}
	}

	public CssPrefixInfo getPrefixStateForProperty(String propertyName)
	{
		return CssPrefixInfo.fromIntegerValue(propertyName, getPrefixes().get(propertyName));
	}

	public Set<CssPrefixInfo> getAllPrefixInfo()
	{
		Set<CssPrefixInfo> result = Sets.newHashSetWithExpectedSize(getPrefixes().size());
		for(Map.Entry<String, Integer> entry : getPrefixes().entrySet())
		{
			result.add(CssPrefixInfo.fromIntegerValue(entry.getKey(), entry.getValue()));
		}
		return result;
	}

	public boolean isAutoInsertCssPrefixedEnabled()
	{
		return myAutoInsertCssPrefixedEnabled;
	}

	public void setAutoInsertCssPrefixedEnabled(boolean autoInsertCssPrefixedEnabled)
	{
		myAutoInsertCssPrefixedEnabled = autoInsertCssPrefixedEnabled;
	}

	public void setFuzzySearchEnabled(boolean fuzzySearchEnabled)
	{
		myFuzzySearchEnabled = fuzzySearchEnabled;
	}

	public boolean isFuzzySearchEnabled()
	{
		return myFuzzySearchEnabled;
	}

	@NotNull
	public Map<String, Integer> getPrefixes()
	{
		if(prefixes == null)
		{
			prefixes = loadDefaultPrefixes();
		}
		return prefixes;
	}

	@SuppressWarnings("UnusedDeclaration")
	public void setPrefixes(@Nullable Map<String, Integer> prefixes)
	{
		this.prefixes = prefixes;
	}

	public Map<String, Integer> loadDefaultPrefixes()
	{
		Map<String, Integer> result = new HashMap<String, Integer>();
		try
		{
			Document document = JDOMUtil.loadDocument(CssEmmetOptions.class.getResourceAsStream("emmet_default_options.xml"));
			Element prefixesElement = document.getRootElement().getChild("prefixes");
			if(prefixesElement != null)
			{
				for(Element entry : prefixesElement.getChildren("entry"))
				{
					String propertyName = entry.getAttributeValue("key");
					Integer value = StringUtil.parseInt(entry.getAttributeValue("value"), 0);
					result.put(propertyName, value);
				}
			}
		}
		catch(Exception e)
		{
			LOGGER.warn(e);
			return result;
		}
		return result;
	}

	@Nullable
	@Override
	public CssEmmetOptions getState()
	{
		return this;
	}

	@Override
	public void loadState(CssEmmetOptions state)
	{
		XmlSerializerUtil.copyBean(state, this);
	}
}
