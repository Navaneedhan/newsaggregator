package com.tekion.consumer.normalize;

import com.tekion.common.model.NewsArticle;

/**
 * Maps a transport-specific or source-specific payload into the unified {@link NewsArticle}.
 *
 * @param <T> raw message type
 */
public interface NewsNormalizer<T> {

    NewsArticle normalize(T message);
}
