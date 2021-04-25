package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.*

@SpringBootApplication
class DemoApplication {

	val userService: UserService = UserService()
	val cacheService: CacheService = CacheService()
	val favoriteService: FavoriteService = FavoriteService()
	val suggestionService: SuggestionService = SuggestionService()

	fun reactive(userId: String): Flux<FavouriteDetail> {
		return userService.getFavorites(userId)
				.timeout(Duration.ofMillis(800))
				.onErrorResume { cacheService.cachedFavoritesFor(userId) }
				.flatMap(favoriteService::getDetails)
				.switchIfEmpty(suggestionService.getSuggestions())
				.take(5)
	}

}

class SuggestionService {
	fun getSuggestions(): Flux<FavouriteDetail> {
		TODO("Not yet implemented")
	}
}

class FavoriteService {
	fun getDetails(id: UUID): Mono<FavouriteDetail> {
		TODO("Not yet implemented")
	}
}

class CacheService {
	fun cachedFavoritesFor(userId: String): Flux<UUID> {
		TODO("Not yet implemented")
	}
}

class UserService {
	fun getFavorites(userId: String): Flux<UUID> {
		TODO("Not yet implemented")
	}

}

data class FavouriteDetail(val id: UUID)


fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}
