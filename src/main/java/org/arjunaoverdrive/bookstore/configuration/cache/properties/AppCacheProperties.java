package org.arjunaoverdrive.bookstore.configuration.cache.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "app.cache")
@Data
public class AppCacheProperties {

    private final List<String> cacheNames = new ArrayList<>();

    private final Map<String, CacheProperties> caches = new HashMap<>();

    private CacheType cacheType;

    @Data
    public static class CacheProperties{
        private Duration expiry = Duration.ZERO;
    }

    public interface CacheNames{
        String FILTERED_BY_TITLE_AND_AUTHOR = "filterByTitleAndAuthorCache";

        String BOOKS_BY_CATEGORY = "categoryCache";
    }

    public enum CacheType{
        REDIS
    }

}
