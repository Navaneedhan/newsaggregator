package com.tekion.consumer.normalize;

import com.tekion.common.model.NewsArticle;
import com.tekion.consumer.model.RawNewsMessage;

/**
 * Normalizes a {@link RawNewsMessage} into the unified internal {@link NewsArticle} format.
 */
public class RawNewsNormalizer implements NewsNormalizer<RawNewsMessage> {

    @Override
    public NewsArticle normalize(RawNewsMessage message) {
        if (message == null) {
            throw new IllegalArgumentException("message must not be null");
        }
        requireText(message.getArticleId(), "articleId");
        requireText(message.getTitle(), "title");
        requireText(message.getSource(), "source");
        requireText(message.getArticleUrl(), "articleUrl");
        if (message.getPublishedDate() == null) {
            throw new IllegalArgumentException("publishedDate must not be null");
        }

        return new NewsArticle(
                message.getArticleId().trim(),
                message.getTitle().trim(),
                trimToNull(message.getSummary()),
                message.getSource().trim(),
                message.getPublishedDate(),
                message.getArticleUrl().trim(),
                trimToNull(message.getCategory())
        );
    }

    private static void requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
