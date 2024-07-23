package com.intellij.application.options.emmet.css;

import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ServiceAPI;
import consulo.annotation.component.ServiceImpl;
import consulo.component.persist.PersistentStateComponent;
import consulo.component.persist.State;
import consulo.component.persist.Storage;
import consulo.ide.ServiceManager;
import consulo.logging.Logger;
import consulo.util.jdom.JDOMUtil;
import consulo.util.lang.StringUtil;
import consulo.util.xml.serializer.XmlSerializerUtil;
import jakarta.inject.Singleton;
import org.jdom.Document;
import org.jdom.Element;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * @author VISTALL
 * @since 23.08.13.
 */
@Singleton
@State(name = "CssEmmetOptions", storages = @Storage("editor.xml"))
@ServiceAPI(ComponentScope.APPLICATION)
@ServiceImpl
public class CssEmmetOptions implements PersistentStateComponent<CssEmmetOptions> {
    @Nonnull
    public static CssEmmetOptions getInstance() {
        return ServiceManager.getService(CssEmmetOptions.class);
    }

    private static final Logger LOGGER = Logger.getInstance(CssEmmetOptions.class);

    private boolean myFuzzySearchEnabled = true;
    private boolean myAutoInsertCssPrefixedEnabled = true;
    @Nullable
    private Map<String, Integer> prefixes = null;

    public void setPrefixInfo(Collection<CssPrefixInfo> prefixInfos) {
        prefixes = new HashMap<>();

        for (CssPrefixInfo state : prefixInfos) {
            prefixes.put(state.getPropertyName(), state.toIntegerValue());
        }
    }

    public CssPrefixInfo getPrefixStateForProperty(String propertyName) {
        return CssPrefixInfo.fromIntegerValue(propertyName, getPrefixes().get(propertyName));
    }

    public Set<CssPrefixInfo> getAllPrefixInfo() {
        Set<CssPrefixInfo> result = new HashSet<>(getPrefixes().size());
        for (Map.Entry<String, Integer> entry : getPrefixes().entrySet()) {
            result.add(CssPrefixInfo.fromIntegerValue(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    public boolean isAutoInsertCssPrefixedEnabled() {
        return myAutoInsertCssPrefixedEnabled;
    }

    public void setAutoInsertCssPrefixedEnabled(boolean autoInsertCssPrefixedEnabled) {
        myAutoInsertCssPrefixedEnabled = autoInsertCssPrefixedEnabled;
    }

    public void setFuzzySearchEnabled(boolean fuzzySearchEnabled) {
        myFuzzySearchEnabled = fuzzySearchEnabled;
    }

    public boolean isFuzzySearchEnabled() {
        return myFuzzySearchEnabled;
    }

    @Nonnull
    public Map<String, Integer> getPrefixes() {
        if (prefixes == null) {
            prefixes = loadDefaultPrefixes();
        }
        return prefixes;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setPrefixes(@Nullable Map<String, Integer> prefixes) {
        this.prefixes = prefixes;
    }

    public Map<String, Integer> loadDefaultPrefixes() {
        Map<String, Integer> result = new HashMap<>();
        try {
            Document document = JDOMUtil.loadDocument(CssEmmetOptions.class.getResourceAsStream("emmet_default_options.xml"));
            Element prefixesElement = document.getRootElement().getChild("prefixes");
            if (prefixesElement != null) {
                for (Element entry : prefixesElement.getChildren("entry")) {
                    String propertyName = entry.getAttributeValue("key");
                    Integer value = StringUtil.parseInt(entry.getAttributeValue("value"), 0);
                    result.put(propertyName, value);
                }
            }
        }
        catch (Exception e) {
            LOGGER.warn(e);
            return result;
        }
        return result;
    }

    @Nullable
    @Override
    public CssEmmetOptions getState() {
        return this;
    }

    @Override
    public void loadState(CssEmmetOptions state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
