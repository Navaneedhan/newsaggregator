package com.tekion.common.query;

import com.tekion.common.model.AggregatedNewsArticle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Fluent filters and sorts over aggregated news articles.
 * In real-world this will be implemented as database repository queries, but for demo purposes we do it in-memory.
 */
public class AggregatedNewsQuery {

    private final List<AggregatedNewsArticle> articles;

    public AggregatedNewsQuery(List<AggregatedNewsArticle> articles) {
        this.articles = articles == null ? List.of() : List.copyOf(articles);
    }

    public static AggregatedNewsQuery of(List<AggregatedNewsArticle> articles) {
        return new AggregatedNewsQuery(articles);
    }

    /**
     * Keeps articles that include the given source in their attributions.
     */
    public AggregatedNewsQuery filterBySource(String sourceName) {
        if (sourceName == null || sourceName.isBlank()) {
            return this;
        }
        List<AggregatedNewsArticle> filtered = new ArrayList<>();
        for (AggregatedNewsArticle article : articles) {
            if (article.isFromSource(sourceName)) {
                filtered.add(article);
            }
        }
        return new AggregatedNewsQuery(filtered);
    }

    /**
     * Keeps articles in the given category (case-insensitive).
     */
    public AggregatedNewsQuery filterByCategory(String category) {
        if (category == null || category.isBlank()) {
            return this;
        }
        String normalized = category.trim();
        List<AggregatedNewsArticle> filtered = new ArrayList<>();
        for (AggregatedNewsArticle article : articles) {
            if (article.getCategory() != null
                    && article.getCategory().equalsIgnoreCase(normalized)) {
                filtered.add(article);
            }
        }
        return new AggregatedNewsQuery(filtered);
    }

    /**
     * Sorts by published date, newest first.
     */
    public AggregatedNewsQuery sortByRecency() {
        List<AggregatedNewsArticle> sorted = new ArrayList<>(articles);
        sorted.sort(Comparator.comparing(AggregatedNewsArticle::getPublishedDate).reversed());
        return new AggregatedNewsQuery(sorted);
    }

    public List<AggregatedNewsArticle> toList() {
        return articles;
    }
}
