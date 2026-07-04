package com.tekion.consumer;

import com.tekion.common.model.NewsArticle;
import com.tekion.consumer.ingest.NewsIngestor;
import com.tekion.consumer.normalize.NewsNormalizer;

/**
 * Shared ingest pipeline for any transport-backed consumer.
 * Subclasses receive messages from their source (poll loop, push handler, etc.)
 * and delegate here to normalize and ingest.
 * <p>
 * In production the consumer process itself is the long-running daemon;
 * this class only owns per-message processing.
 *
 * @param <T> raw message type produced by the transport (Kafka record, SQS body, etc.)
 */
public abstract class AbstractNewsConsumer<T> {

    private final NewsNormalizer<T> normalizer;
    private final NewsIngestor ingestor;

    protected AbstractNewsConsumer(NewsNormalizer<T> normalizer, NewsIngestor ingestor) {
        this.normalizer = normalizer;
        this.ingestor = ingestor;
    }

    /**
     * Normalize a transport-specific message and ingest the resulting article.
     */
    protected final void process(T message) {
        NewsArticle article = normalizer.normalize(message);
        ingestor.ingest(article);
    }
}
