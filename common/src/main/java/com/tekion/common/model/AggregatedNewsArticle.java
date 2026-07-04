package com.tekion.common.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Result of deduplicating one or more {@link NewsArticle}s that represent the same story.
 * Holds the canonical article fields and every contributing source attribution.
 */
public class AggregatedNewsArticle {

    private final String articleId;
    private final String title;
    private final String summary;
    private final Instant publishedDate;
    private final String articleUrl;
    private final String category;
    private final List<SourceAttribution> sources;

    public AggregatedNewsArticle(
            String articleId,
            String title,
            String summary,
            Instant publishedDate,
            String articleUrl,
            String category,
            List<SourceAttribution> sources
    ) {
        this.articleId = articleId;
        this.title = title;
        this.summary = summary;
        this.publishedDate = publishedDate;
        this.articleUrl = articleUrl;
        this.category = category;
        this.sources = List.copyOf(sources);
    }

    /**
     * Creates an aggregated entry from a single normalized article.
     */
    public static AggregatedNewsArticle fromArticle(NewsArticle article) {
        if (article == null) {
            throw new IllegalArgumentException("article must not be null");
        }
        return fromDuplicates(List.of(article));
    }

    /**
     * Aggregates duplicate articles into one entry, preserving attribution for each source.
     * The first article supplies the canonical fields; all articles contribute a source attribution.
     */
    public static AggregatedNewsArticle fromDuplicates(List<NewsArticle> duplicates) {
        if (duplicates == null || duplicates.isEmpty()) {
            throw new IllegalArgumentException("duplicates must not be empty");
        }

        NewsArticle primary = duplicates.get(0);
        List<SourceAttribution> attributions = new ArrayList<>(duplicates.size());
        for (NewsArticle article : duplicates) {
            attributions.add(toAttribution(article));
        }

        return new AggregatedNewsArticle(
                primary.getArticleId(),
                primary.getTitle(),
                primary.getSummary(),
                primary.getPublishedDate(),
                primary.getArticleUrl(),
                primary.getCategory(),
                attributions
        );
    }

    /**
     * Merges another occurrence of the same story, preserving the new source attribution.
     * Canonical fields stay unchanged. No-op if this source is already attributed.
     */
    public AggregatedNewsArticle merge(NewsArticle article) {
        if (article == null) {
            throw new IllegalArgumentException("article must not be null");
        }
        SourceAttribution attribution = toAttribution(article);
        if (alreadyAttributed(attribution)) {
            return this;
        }
        List<SourceAttribution> updated = new ArrayList<>(sources);
        updated.add(attribution);
        return new AggregatedNewsArticle(
                articleId,
                title,
                summary,
                publishedDate,
                articleUrl,
                category,
                updated
        );
    }

    private boolean alreadyAttributed(SourceAttribution attribution) {
        for (SourceAttribution existing : sources) {
            if (existing.getSourceName().equalsIgnoreCase(attribution.getSourceName())
                    && existing.getArticleUrl().equalsIgnoreCase(attribution.getArticleUrl())) {
                return true;
            }
        }
        return false;
    }

    private static SourceAttribution toAttribution(NewsArticle article) {
        return new SourceAttribution(
                article.getSource(),
                article.getArticleId(),
                article.getArticleUrl(),
                article.getPublishedDate()
        );
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

    public Instant getPublishedDate() {
        return publishedDate;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public String getCategory() {
        return category;
    }

    public List<SourceAttribution> getSources() {
        return sources;
    }

    /**
     * Whether any attributed source matches the given source name (case-insensitive).
     */
    public boolean isFromSource(String sourceName) {
        if (sourceName == null || sourceName.isBlank()) {
            return false;
        }
        String normalized = sourceName.trim();
        return sources.stream()
                .anyMatch(source -> source.getSourceName().equalsIgnoreCase(normalized));
    }
}

