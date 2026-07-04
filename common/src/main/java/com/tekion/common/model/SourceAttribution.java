package com.tekion.common.model;

import java.time.Instant;

/**
 * Attribution for one upstream source that contributed to an aggregated article.
 * Built when the deduplication service merges duplicate {@link NewsArticle}s.
 */
public class SourceAttribution {

    private final String sourceName;
    private final String originalArticleId;
    private final String articleUrl;
    private final Instant publishedDate;

    public SourceAttribution(
            String sourceName,
            String originalArticleId,
            String articleUrl,
            Instant publishedDate
    ) {
        this.sourceName = sourceName;
        this.originalArticleId = originalArticleId;
        this.articleUrl = articleUrl;
        this.publishedDate = publishedDate;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getOriginalArticleId() {
        return originalArticleId;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public Instant getPublishedDate() {
        return publishedDate;
    }
}
