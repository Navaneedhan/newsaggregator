package com.tekion.consumer.memory;

import com.tekion.consumer.AbstractNewsConsumer;
import com.tekion.consumer.ingest.NewsIngestor;
import com.tekion.consumer.model.RawNewsMessage;
import com.tekion.consumer.normalize.NewsNormalizer;

import java.util.List;

/**
 * Demo consumer that accepts messages in-process.
 * Kafka/SQS consumers would extend {@link AbstractNewsConsumer} the same way,
 * calling {@link #process} from their poll/receive loop.
 */
public class InMemoryNewsConsumer extends AbstractNewsConsumer<RawNewsMessage> {

    public InMemoryNewsConsumer(NewsNormalizer<RawNewsMessage> normalizer, NewsIngestor ingestor) {
        super(normalizer, ingestor);
    }

    /**
     * Accept a single raw message (simulates delivery from a queue/topic).
     */
    public void accept(RawNewsMessage message) {
        process(message);
    }

    /**
     * Accept a batch of raw messages.
     */
    public void acceptAll(List<RawNewsMessage> messages) {
        if (messages == null) {
            throw new IllegalArgumentException("messages must not be null");
        }
        for (RawNewsMessage message : messages) {
            process(message);
        }
    }
}
