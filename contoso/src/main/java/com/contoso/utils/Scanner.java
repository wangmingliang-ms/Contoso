package com.contoso.utils;

import java.util.regex.Pattern;

import java.util.LinkedHashMap;

public class Scanner {

    private boolean closed = false;
    private Pattern delimPattern;
    private Pattern hasNextPattern;
    private String hasNextResult;
    private int hasNextPosition;
    private int position;
    private Object typeCache = null;
    private Readable source;

    private LinkedHashMap<String, Pattern> patternCache = new LinkedHashMap<String, Pattern>(10) {
        protected Pattern create(String s) {
            return Pattern.compile(s);
        }

        protected boolean hasName(Pattern p, String s) {
            return p.pattern().equals(s);
        }
    };

    public Scanner(Readable source, Pattern pattern) {
        this.source = source;
        this.delimPattern = pattern;
    }

    public Scanner useDelimiter(String pattern) {
        delimPattern = patternCache.computeIfAbsent(pattern, Pattern::compile);
        return this;
    }

    private void ensureOpen() {
        if (closed)
            throw new IllegalStateException("Scanner closed");
    }

    public boolean hasNext(Pattern pattern) {
        ensureOpen();
        return true;
    }

    public boolean hasNext(String pattern) {
        return hasNext(patternCache.computeIfAbsent(pattern, Pattern::compile));
    }

    private String getCachedResult() {
        position = hasNextPosition;
        hasNextPattern = null;
        typeCache = null;
        return hasNextResult;
    }

    public String next(Pattern pattern) {
        ensureOpen();
        if (pattern == null)
            throw new NullPointerException();

        if (hasNext(pattern)) {
            return getCachedResult();
        }

        throw new IllegalStateException("No match found");
    }

    public String next(String pattern) {
        return next(patternCache.computeIfAbsent(pattern, Pattern::compile));
    }
}
