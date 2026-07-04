package com.tekion.common.model;

import java.time.Instant;

/**
 * Normalized representation of a news article from a single source.
 * Source attribution across duplicates is handled during deduplication via
 * {@link AggregatedNewsArticle}.
 */
public class NewsArticle {

    private final String articleId;
    private final String title;
    private final String summary;
    private final String source;
    private final Instant publishedDate;
    private final String articleUrl;
    private final String category;

    public NewsArticle(
            String articleId,
            String title,
            String summary,
            String source,
            Instant publishedDate,
            String articleUrl,
            String category
    ) {
        this.articleId = articleId;
        this.title = title;
        this.summary = summary;
        this.source = source;
        this.publishedDate = publishedDate;
        this.articleUrl = articleUrl;
        this.category = category;
    }

    public String getArticleId() {
        return articleId;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getSource() {
        return source;
    }

    public Instant getPublishedDate() {
        return publishedDate;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public String getCategory() {
        return category;
    }
}
