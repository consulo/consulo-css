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
package consulo.xstylesheet.definition;

/**
 * An at-rule a stylesheet may contain, such as {@code @media} or, for a CSS framework, {@code @apply}
 *
 * @param name   the name without {@code @}
 * @param syntax how it is written, for example {@code @media <media-query-list> { <rule-list> }}; may be empty
 * @param url    the documentation, may be empty
 * @since 2026-11-06
 */
public record XStyleSheetAtRule(String name, String syntax, String url) {
}
