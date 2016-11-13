package com.example.laurynas.fixtures;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HTMLParser {
    Document result;

    public void load(String url) {
        try {
            result = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return result.title();
    }

    public Elements getContentTable() {
        return result.select("#content table");
    }

    public Elements getContent() {
        return result.select("div.matches-block__match-list");
    }
}
