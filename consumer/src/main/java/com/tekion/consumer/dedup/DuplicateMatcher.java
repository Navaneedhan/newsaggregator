package com.tekion.consumer.dedup;

import com.tekion.common.model.AggregatedNewsArticle;
import com.tekion.common.model.NewsArticle;

/**
 * Decides whether an incoming article is a duplicate of an already ingested story.
 */
public interface DuplicateMatcher {

    boolean isDuplicate(NewsArticle candidate, AggregatedNewsArticle existing);
}
