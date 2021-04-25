package com.example.demo.java;

import static java.util.Collections.emptyList;
import static java.util.concurrent.CompletableFuture.completedFuture;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterators;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class DemoApplication {

  private final UserService userService = new UserService();
  private final CacheService cacheService = new CacheService();
  private final FavoriteService favoriteService = new FavoriteService();
  private final SuggestionService suggestionService = new SuggestionService();

  public Flux<FavouriteDetail> reactive(UserId userId) {
    return userService.getFavorites(userId)
        .timeout(Duration.ofMillis(800))
        .onErrorResume((throwable) -> cacheService.cachedFavoritesFor(userId))
        .flatMap(favoriteService::getDetails)
        .switchIfEmpty(suggestionService.getSuggestions())
        .take(5);
  }

  public CompletableFuture<List<FavouriteDetail>> futures(UserId userId) {
    return userService.getFavoritesF(userId)
        .exceptionallyCompose((throwable) -> cacheService.cachedFavoritesForF(userId))
        .thenCompose((favouritesIds) -> composeMany(favouritesIds.stream().map(favoriteService::getDetailsF).collect(Collectors.toList())))
        .thenCompose((favourites) -> defaultIfEmpty(favourites, suggestionService::getSuggestionsF))
        .thenApply((favourites) -> favourites.stream().limit(5).collect(Collectors.toList()));
  }

  static <T> CompletableFuture<List<T>> composeMany(List<CompletableFuture<T>> futures) {
    return futures.stream()
        .map(f -> f.thenApply(Stream::of))
        .reduce((a, b) -> a.thenCompose(xs -> b.thenApply(ys -> Stream.concat(xs, ys))))
        .map(f -> f.thenApply(s -> s.collect(Collectors.toList())))
        .orElse(completedFuture(emptyList()));
  }

  static <T> CompletableFuture<List<T>> defaultIfEmpty(List<T> mainStream, Supplier<CompletableFuture<List<T>>> fallbackStream) {
    Iterator<T> iterator = mainStream.iterator();
    if (iterator.hasNext()) {
      return completedFuture(StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false).collect(Collectors.toList()));
    } else {
      return fallbackStream.get();
    }
  }

}


class SuggestionService {

  Flux<FavouriteDetail> getSuggestions() {
    throw new IllegalArgumentException("Not yet implemented");
  }

  CompletableFuture<List<FavouriteDetail>> getSuggestionsF() {
    throw new IllegalArgumentException("Not yet implemented");
  }
}

class FavoriteService {

  Mono<FavouriteDetail> getDetails(FavouriteId id) {
    throw new IllegalArgumentException("Not yet implemented");
  }

  CompletableFuture<FavouriteDetail> getDetailsF(FavouriteId id) {
    throw new IllegalArgumentException("Not yet implemented");
  }
}

class UserService {

  Flux<FavouriteId> getFavorites(UserId userId) {
    throw new IllegalArgumentException("Not yet implemented");
  }

  CompletableFuture<List<FavouriteId>> getFavoritesF(UserId userId) {
    throw new IllegalArgumentException("Not yet implemented");
  }


}

class CacheService {

  Flux<FavouriteId> cachedFavoritesFor(UserId userId) {
    throw new IllegalArgumentException("Not yet implemented");
  }

  CompletableFuture<List<FavouriteId>> cachedFavoritesForF(UserId userId) {
    throw new IllegalArgumentException("Not yet implemented");
  }
}

class FavouriteDetail {

  private UUID id;
}

class UserId {

  private UUID id;
}

class FavouriteId {

  private UUID id;
}