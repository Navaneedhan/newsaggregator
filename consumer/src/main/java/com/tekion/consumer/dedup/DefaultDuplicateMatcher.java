package com.tekion.consumer.dedup;

import com.tekion.common.model.AggregatedNewsArticle;
import com.tekion.common.model.NewsArticle;
import com.tekion.common.model.SourceAttribution;

/**
 * Treats articles as duplicates when they share an article id, URL, or title
 * (case-insensitive), including ids/urls already recorded in source attributions.
 */
public class DefaultDuplicateMatcher implements DuplicateMatcher {

    @Override
    public boolean isDuplicate(NewsArticle candidate, AggregatedNewsArticle existing) {
        if (candidate == null || existing == null) {
            return false;
        }

        String candidateId = normalize(candidate.getArticleId());
        String candidateUrl = normalizeUrl(candidate.getArticleUrl());
        String candidateTitle = normalize(candidate.getTitle());

        if (candidateId != null && candidateId.equals(normalize(existing.getArticleId()))) {
            return true;
        }
        if (candidateUrl != null && candidateUrl.equals(normalizeUrl(existing.getArticleUrl()))) {
            return true;
        }
        if (candidateTitle != null && candidateTitle.equals(normalize(existing.getTitle()))) {
            return true;
        }

        for (SourceAttribution source : existing.getSources()) {
            if (candidateId != null && candidateId.equals(normalize(source.getOriginalArticleId()))) {
                return true;
            }
            if (candidateUrl != null && candidateUrl.equals(normalizeUrl(source.getArticleUrl()))) {
                return true;
            }
        }

        return false;
    }

    private static String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed.toLowerCase();
    }

    private static String normalizeUrl(String url) {
        String normalized = normalize(url);
        if (normalized == null) {
            return null;
        }
        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }
}
