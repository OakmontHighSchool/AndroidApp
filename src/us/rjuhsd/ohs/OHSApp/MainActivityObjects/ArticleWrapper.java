package us.rjuhsd.ohs.OHSApp.MainActivityObjects;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ArticleWrapper {
	public final Elements articleHeader;
	public final Elements articleText;
	public final String urlString;

	public ArticleWrapper(Element el) {
		articleHeader = el.select("h1.ui-article-title span");
		articleText = el.select("p.ui-article-description");
		urlString = el.select("h1.ui-article-title a").attr("abs:href");
	}
}