package org.acme;

import jakarta.enterprise.context.ApplicationScoped;
import org.jsoup.Jsoup;

@ApplicationScoped
public class HtmlCleaner {

    public String clean(String html) {
        return Jsoup.parse(html).text();
    }

}
