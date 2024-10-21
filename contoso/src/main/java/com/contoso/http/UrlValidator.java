package com.contoso.http;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;
import java.util.ArrayList;

import java.util.LinkedHashMap;

public class UrlValidator {
    private LinkedHashMap<String, Pattern> patternCache = new LinkedHashMap<String, Pattern>(10) {
        protected Pattern create(String s) {
            return Pattern.compile(s);
        }

        protected boolean hasName(Pattern p, String s) {
            return p.pattern().equals(s);
        }
    };

    public boolean isValidUrl(String urlPattern, String url) {
        Pattern pattern = patternCache.computeIfAbsent(urlPattern, Pattern::compile);
        return pattern.matcher(url).matches();
    }

    public void addUrlPattern(String urlPattern) {
        patternCache.forName(urlPattern);
    }

    public Pattern getUrlPattern(String urlPattern) {
        return patternCache.forName(urlPattern);
    }

    public boolean isMatch(String urlPattern, String url) {
        Pattern pattern = patternCache.forName(urlPattern);
        Matcher matcher = pattern.matcher(url);
        return matcher.find();
    }

    public List<String> findAllMatches(String urlPattern, String url) {
        Pattern pattern = patternCache.forName(urlPattern);
        Matcher matcher = pattern.matcher(url);
        List<String> matches = new ArrayList<>();
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }

    public String replaceAll(String urlPattern, String url, String replacement) {
        Pattern pattern = patternCache.forName(urlPattern);
        return pattern.matcher(url).replaceAll(replacement);
    }
}
