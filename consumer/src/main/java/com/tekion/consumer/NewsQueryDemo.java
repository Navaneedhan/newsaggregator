package com.tekion.consumer;

import com.tekion.common.model.AggregatedNewsArticle;
import com.tekion.common.model.SourceAttribution;
import com.tekion.common.query.AggregatedNewsQuery;
import com.tekion.consumer.ingest.InMemoryNewsIngestor;
import com.tekion.consumer.memory.InMemoryNewsConsumer;
import com.tekion.consumer.model.RawNewsMessage;
import com.tekion.consumer.normalize.RawNewsNormalizer;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Demo of listing, filtering, and sorting aggregated news articles.
 */
public class NewsQueryDemo {

    public static void main(String[] args) {
        InMemoryNewsIngestor ingestor = seedArticles();

        printSection("All articles (newest first)");
        printArticles(AggregatedNewsQuery.of(ingestor.findAll()).sortByRecency().toList());

        printSection("Filter by source: Reuters");
        printArticles(AggregatedNewsQuery.of(ingestor.findAll())
                .filterBySource("Reuters")
                .sortByRecency()
                .toList());

        printSection("Filter by category: World");
        printArticles(AggregatedNewsQuery.of(ingestor.findAll())
                .filterByCategory("World")
                .sortByRecency()
                .toList());

        printSection("Filter by source: BBC, then show attributions");
        for (AggregatedNewsArticle article : AggregatedNewsQuery.of(ingestor.findAll())
                .filterBySource("BBC")
                .toList()) {
            printArticle(article);
            for (SourceAttribution source : article.getSources()) {
                System.out.printf(
                        "    - %s | id=%s | %s | %s%n",
                        source.getSourceName(),
                        source.getOriginalArticleId(),
                        source.getPublishedDate(),
                        source.getArticleUrl()
                );
            }
        }
    }

    private static InMemoryNewsIngestor seedArticles() {
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
                ),
                new RawNewsMessage(
                        "reuters-2",
                        "Central bank holds rates steady",
                        "Policy makers voted to keep rates unchanged.",
                        "Reuters",
                        Instant.parse("2026-07-04T10:00:00Z"),
                        "https://reuters.example/rates-steady",
                        "Business"
                )
        ));
        return ingestor;
    }

    private static void printSection(String title) {
        System.out.println();
        System.out.println("=== " + title + " ===");
    }

    private static void printArticles(List<AggregatedNewsArticle> articles) {
        if (articles.isEmpty()) {
            System.out.println("(none)");
            return;
        }
        for (AggregatedNewsArticle article : articles) {
            printArticle(article);
        }
    }

    private static void printArticle(AggregatedNewsArticle article) {
        String sources = article.getSources().stream()
                .map(SourceAttribution::getSourceName)
                .collect(Collectors.joining(", "));
        System.out.printf(
                "%s | %s | %s | sources=(%s)%n",
                article.getPublishedDate(),
                article.getCategory(),
                article.getTitle(),
                sources
        );
    }
}
