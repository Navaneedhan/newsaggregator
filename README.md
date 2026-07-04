### Module Architecture
It consists of 3 main modules:
- api: Will take care of the presentation layer and will expose the API for users to access the news articles.
- common: Will take care of the common data structures and utilities that will be used across the modules.
- consumer: Will take care of the ingestion, normalization, and deduplication of news articles from multiple sources.

### System that does the following things:
#### Ingestion Service:
    - Fetches news from multiple resources
#### Normalization Service:
    - Normalizes the news data into a consistent format or unified internal format:
        - article_id
        - title
        - summary or description
        - source
        - published_date
        - article_url
        - category/topic
##### Deduplication Service:
    - Identifies and removes duplicate news articles based on unique identifiers (e.g., article_id, title, or URL)
    - In case of duplicates, then the system should preserve the source attribution
#### Presentation Service:
    - Provides an API or interface for users to access the normalized and deduplicated news articles
    - Supports filtering and searching based on:
        list aggregated news
        sorting by recency
        filtering by source
        view underlying source attribution
    - Returns results in a user-friendly format (e.g., JSON, XML)

#### Thought process for designing the system:
    1. Starting from the backwards:
        Will be first focusing on how the users will be making api calls
        Start with java module for presenting the data.
    Assumptions:
        - Going to use in-memory data for simplicity, but in a real-world scenario, a database would be used for persistence.
