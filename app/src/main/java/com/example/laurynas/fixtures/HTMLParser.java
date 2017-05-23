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
        return result.select("div.fixres.matches-block--large");
    }
    public Elements getSeparateGameExtendedContent() {
        return result.select("div.live-text");
    }
    public Elements getFixturesResultsContent() {
        return result.select("div.fixres matches-block--large");
    }
    public Elements getSeparateTeamsContent(){
        return result.select("div.page-nav__body");
    }
    public Elements getSeparateTeamContent(){
        return result.select("div.matches-block__match-list");
    }
    public Elements getLeaguesContent(){
        return result.select("div.grid__col.site-layout-secondary__col1");
    }
    public Elements getSeparateGameContent(){
        return result.select("div.site-wrapper");
    }
    public Elements getLeagueTableContent(){
        return  result.select("div.standing-table");
    }
}
