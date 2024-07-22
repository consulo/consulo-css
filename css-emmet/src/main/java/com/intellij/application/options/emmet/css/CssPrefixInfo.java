/*
 * Copyright 2000-2013 JetBrains s.r.o.
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
package com.intellij.application.options.emmet.css;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * User: zolotov
 * Date: 2/20/13
 */
public class CssPrefixInfo {
    @Nonnull
    private final String myPropertyName;
    @Nonnull
    private final Collection<CssPrefix> myEnabledPrefixes;

    public CssPrefixInfo(@Nonnull String propertyName, CssPrefix... enabledPrefixes) {
        myPropertyName = propertyName;
        myEnabledPrefixes = new HashSet<>(Set.of(enabledPrefixes));
        for (CssPrefix prefix : enabledPrefixes) {
            setValue(prefix, true);
        }
    }

    public CssPrefixInfo(@Nonnull String propertyName, Collection<CssPrefix> enabledPrefixes) {
        myPropertyName = propertyName;
        myEnabledPrefixes = new HashSet<>(enabledPrefixes);
        for (CssPrefix prefix : enabledPrefixes) {
            setValue(prefix, true);
        }
    }

    @Nonnull
    public Collection<CssPrefix> getEnabledPrefixes() {
        return myEnabledPrefixes;
    }

    public void setValue(CssPrefix prefix, boolean value) {
        if (value) {
            myEnabledPrefixes.add(prefix);
        }
        else {
            myEnabledPrefixes.remove(prefix);
        }
    }

    public boolean getValue(CssPrefix prefix) {
        return myEnabledPrefixes.contains(prefix);
    }

    @Nonnull
    public String getPropertyName() {
        return myPropertyName;
    }

    public static CssPrefixInfo fromIntegerValue(String propertyName, Integer value) {
        if (value == null) {
            return new CssPrefixInfo(propertyName);
        }
        List<CssPrefix> enabledPrefixes = new ArrayList<>();
        for (CssPrefix prefix : CssPrefix.values()) {
            if ((value & prefix.myIntMask) > 0) {
                enabledPrefixes.add(prefix);
            }
        }
        return new CssPrefixInfo(propertyName, enabledPrefixes);
    }

    public int toIntegerValue() {
        int result = 0;
        for (CssPrefix prefix : myEnabledPrefixes) {
            if (getValue(prefix)) {
                result |= prefix.myIntMask;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "CssPrefixInfo{" +
            "myPropertyName='" + myPropertyName + '\'' +
            ", myEnabledPrefixes=" + myEnabledPrefixes +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CssPrefixInfo that = (CssPrefixInfo)o;

        if (!myEnabledPrefixes.equals(that.myEnabledPrefixes)) {
            return false;
        }
        if (!myPropertyName.equals(that.myPropertyName)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = myPropertyName.hashCode();
        result = 31 * result + myEnabledPrefixes.hashCode();
        return result;
    }
}
