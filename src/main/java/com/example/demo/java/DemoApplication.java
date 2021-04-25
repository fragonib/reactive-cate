package com.example.demo.java;

import java.time.Duration;
import java.util.Iterator;
import java.util.Spliterators;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
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
        .onErrorResume((t) -> cacheService.cachedFavoritesFor(userId))
        .flatMap(favoriteService::getDetails)
        .switchIfEmpty(suggestionService.getSuggestions())
        .take(5);
  }

  public CompletableFuture<Stream<FavouriteDetail>> futures(UserId userId) {
    return userService.getFavoritesF(userId)
        .exceptionallyComposeAsync((th) -> cacheService.cachedFavoritesForF(userId))
        .thenApplyAsync((uuids) -> uuids.map(favoriteService::getDetailsF))
        .thenApply((fv) -> defaultIfEmpty(fv, suggestionService::getSuggestionsF))
        .thenApply((fv) -> fv.limit(5));
  }

  static <T> Stream<T> defaultIfEmpty(Stream<T> mainStream, Supplier<Stream<T>> fallbackStream) {
    Iterator<T> iterator = mainStream.iterator();
    if (iterator.hasNext()) {
      return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
    } else {
      return fallbackStream.get();
    }
  }

}


class SuggestionService {

  Flux<FavouriteDetail> getSuggestions() {
    throw new IllegalArgumentException("Not yet implemented");
  }

  Stream<FavouriteDetail> getSuggestionsF() {
    throw new IllegalArgumentException("Not yet implemented");
  }
}

class FavoriteService {

  Mono<FavouriteDetail> getDetails(FavouriteId id) {
    throw new IllegalArgumentException("Not yet implemented");
  }

  FavouriteDetail getDetailsF(FavouriteId id) {
    throw new IllegalArgumentException("Not yet implemented");
  }
}

class UserService {

  Flux<FavouriteId> getFavorites(UserId userId) {
    throw new IllegalArgumentException("Not yet implemented");
  }

  CompletableFuture<Stream<FavouriteId>> getFavoritesF(UserId userId) {
    throw new IllegalArgumentException("Not yet implemented");
  }


}

class CacheService {

  Flux<FavouriteId> cachedFavoritesFor(UserId userId) {
    throw new IllegalArgumentException("Not yet implemented");
  }

  CompletableFuture<Stream<FavouriteId>> cachedFavoritesForF(UserId userId) {
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