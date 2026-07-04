package com.tekion.consumer.ingest;

import com.tekion.common.model.AggregatedNewsArticle;
import com.tekion.common.model.NewsArticle;

import java.util.List;

/**
 * Persists normalized articles, applying deduplication on ingest.
 * Implementations may use memory, a database, or a message bus.
 */
public interface NewsIngestor {

    /**
     * Ingests a normalized article. Duplicates are merged into an existing
     * {@link AggregatedNewsArticle}, preserving source attribution.
     */
    void ingest(NewsArticle article);

    List<AggregatedNewsArticle> findAll();
}
