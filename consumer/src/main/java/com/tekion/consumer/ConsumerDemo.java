package com.tekion.consumer;

import com.tekion.common.model.AggregatedNewsArticle;
import com.tekion.common.model.SourceAttribution;
import com.tekion.consumer.ingest.InMemoryNewsIngestor;
import com.tekion.consumer.memory.InMemoryNewsConsumer;
import com.tekion.consumer.model.RawNewsMessage;
import com.tekion.consumer.normalize.RawNewsNormalizer;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Simple in-memory demo of normalize, ingest, and deduplication.
 */
public class ConsumerDemo {

    public static void main(String[] args) {
        InMemoryNewsIngestor ingestor = new InMemoryNewsIngestor();
        InMemoryNewsConsumer consumer = new InMemoryNewsConsumer(new RawNewsNormalizer(), ingestor);

        consumer.acceptAll(List.of(
                new RawNewsMessage(
                        "bbc-1",
                        "Markets rally on rate news",
                        "Stocks rose after the announcement.",
                        "BBC",
                        Instant.parse("2026-07-04T08:00:00Z"),
                        "https://bbc.example/markets-rally",
                        "Business"
                ),
                // Same title from another source → merged with BBC attribution preserved
                new RawNewsMessage(
                        "reuters-1",
                        "Markets rally on rate news",
                        "Equities climbed following the rate decision.",
                        "Reuters",
                        Instant.parse("2026-07-04T08:05:00Z"),
                        "https://reuters.example/markets-rally",
                        "Business"
                ),
                new RawNewsMessage(
                        "ap-1",
                        "Storms disrupt travel across Europe",
                        "Airlines cancel flights amid severe weather.",
                        "AP",
                        Instant.parse("2026-07-04T09:15:00Z"),
                        "https://ap.example/storms-travel",
                        "World"
                )
        ));

        System.out.println("Stored articles: " + ingestor.size());
        for (AggregatedNewsArticle article : ingestor.findAll()) {
            String sources = article.getSources().stream()
                    .map(SourceAttribution::getSourceName)
                    .collect(Collectors.joining(", "));
            System.out.printf(
                    "%s | %s | sources=(%s)%n",
                    article.getTitle(),
                    article.getCategory(),
                    sources
            );
        }
    }
}
