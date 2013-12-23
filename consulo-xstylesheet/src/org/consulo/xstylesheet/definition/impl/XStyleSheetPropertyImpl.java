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

package org.consulo.xstylesheet.definition.impl;

import org.consulo.xstylesheet.definition.XStyleSheetProperty;
import org.consulo.xstylesheet.definition.XStyleSheetPropertyValueEntry;
import org.consulo.xstylesheet.definition.XStyleSheetPropertyValuePart;

import java.util.List;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class XStyleSheetPropertyImpl implements XStyleSheetProperty {
  private String myName;
  private XStyleSheetPropertyValueEntry[] myValidEntries;
  private XStyleSheetPropertyValueEntry[] myInitialEntries;

  public XStyleSheetPropertyImpl(String name, List<XStyleSheetPropertyValueEntry> validEntries, List<XStyleSheetPropertyValueEntry> initialEntries) {
    myName = name;
    myValidEntries = validEntries.toArray(new XStyleSheetPropertyValueEntry[validEntries.size()]);
    myInitialEntries = initialEntries.toArray(new XStyleSheetPropertyValueEntry[initialEntries.size()]);
  }

  @Override
  public String getName() {
    return myName;
  }

  @Override
  public XStyleSheetPropertyValueEntry[] getValidEntries() {
    return myValidEntries;
  }

  @Override
  public XStyleSheetPropertyValueEntry[] getInitialEntries() {
    return myInitialEntries;
  }
}
