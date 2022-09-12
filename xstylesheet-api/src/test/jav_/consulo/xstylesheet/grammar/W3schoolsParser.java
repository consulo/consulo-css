/*
 * Copyright 2013-2020 consulo.io
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

package consulo.xstylesheet.grammar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author VISTALL
 * @since 2020-07-03
 */
public class W3schoolsParser
{
	public static void main(String[] args) throws Exception
	{
		Document doc = Jsoup.connect("https://www.w3schools.com/cssref/default.asp").get();

		Elements elementsByClass = doc.getElementsByClass("w3-table-all notranslate");

		Map<String, CssPropery> properties = new TreeMap<>();

		for(Element elementByClass : elementsByClass)
		{
			Elements trs = elementByClass.getElementsByTag("tr");

			for(Element tr : trs)
			{
				Elements tds = tr.getElementsByTag("td");

				Element aLinkTr = tds.get(0);

				String href;
				String cssProperty;
				if(aLinkTr.childrenSize() == 0)
				{
					cssProperty = aLinkTr.text();
					href = null;
				}
				else
				{
					Element aLink = aLinkTr.child(0);

					href = aLink.attributes().get("href");

					cssProperty = aLink.text();
				}

				Element docTr = tds.get(1);

				String documentation = docTr.text();

				properties.put(cssProperty, new CssPropery(cssProperty, documentation, href));
			}
		}

		for(CssPropery cssPropery : properties.values())
		{
			String href = cssPropery.getHref();
			if(href == null)
			{
				continue;
			}

			String url;
			if(href.startsWith("/"))
			{
				url = "https://www.w3schools.com" + href;
			}
			else
			{
				url = "https://www.w3schools.com/cssref/" + href;
			}
			//System.out.println("loading: " + cssPropery.getName() + " " + url);
			Document document = Jsoup.connect(url).get();

			Elements cssSyntaxes = document.getElementsByClass("w3-code w3-border notranslate");

			if(cssSyntaxes.size() == 1)
			{
				Element firstDiv = cssSyntaxes.get(0);

				if(firstDiv.childrenSize() == 1)
				{
					Element singleDiv = firstDiv.child(0);

					Elements children = singleDiv.children();
					
					//System.out.println(singleDiv);
				}
			}
		}

		for(String s : properties.keySet())
		{
			System.out.println(s);
		}
	}
}
