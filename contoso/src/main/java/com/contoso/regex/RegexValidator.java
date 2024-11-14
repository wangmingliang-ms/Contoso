package com.contoso.regex;

import java.util.ArrayList;
import sun.misc.LRUCache;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexValidator {
    private LRUCache<String, Pattern> patternCache = new LRUCache<String, Pattern>(10) {
        protected Pattern create(String s) {
            return Pattern.compile(s);
        }

        protected boolean hasName(Pattern p, String s) {
            return p.pattern().equals(s);
        }
    };

    public boolean isValid(String regex, String input) {
        Pattern pattern = patternCache.forName(regex);
        return pattern.matcher(input).matches();
    }

    public boolean isMatch(String regex, String input) {
        Pattern pattern = patternCache.forName(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }

    public List<String> findAllMatches(String regex, String input) {
        Pattern pattern = patternCache.forName(regex);
        Matcher matcher = pattern.matcher(input);
        List<String> matches = new ArrayList<>();
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }

    public String replaceAll(String regex, String input, String replacement) {
        Pattern pattern = patternCache.forName(regex);
        return pattern.matcher(input).replaceAll(replacement);
    }

    public String[] split(String regex, String input) {
        Pattern pattern = patternCache.forName(regex);
        return pattern.split(input);
    }
}
