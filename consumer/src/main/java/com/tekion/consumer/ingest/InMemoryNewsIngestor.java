package com.tekion.consumer.ingest;

import com.tekion.common.model.AggregatedNewsArticle;
import com.tekion.common.model.NewsArticle;
import com.tekion.consumer.dedup.DefaultDuplicateMatcher;
import com.tekion.consumer.dedup.DuplicateMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * In-memory store for demo purposes. Deduplicates on ingest and keeps source attributions.
 */
public class InMemoryNewsIngestor implements NewsIngestor {

    private final List<AggregatedNewsArticle> articles = new ArrayList<>();
    private final DuplicateMatcher duplicateMatcher;
    private final Object lock = new Object();

    public InMemoryNewsIngestor() {
        this(new DefaultDuplicateMatcher());
    }

    public InMemoryNewsIngestor(DuplicateMatcher duplicateMatcher) {
        this.duplicateMatcher = duplicateMatcher;
    }

    @Override
    public void ingest(NewsArticle article) {
        if (article == null) {
            throw new IllegalArgumentException("article must not be null");
        }

        // Kind of @Transactional annotation, but for in-memory. We want to ensure that the deduplication and addition of the article is atomic.
        synchronized (lock) {
            for (int i = 0; i < articles.size(); i++) {
                AggregatedNewsArticle existing = articles.get(i);
                if (duplicateMatcher.isDuplicate(article, existing)) {
                    articles.set(i, existing.merge(article));
                    return;
                }
            }
            articles.add(AggregatedNewsArticle.fromArticle(article));
        }
    }

    @Override
    public List<AggregatedNewsArticle> findAll() {
        synchronized (lock) {
            return List.copyOf(articles);
        }
    }

    public int size() {
        synchronized (lock) {
            return articles.size();
        }
    }

}
