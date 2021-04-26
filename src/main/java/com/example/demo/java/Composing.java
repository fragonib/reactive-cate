package com.example.demo.java;

import java.net.URL;
import java.util.Collection;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public class Composing {

  Flux<EncodedLink> gatherLinks(Collection<String> paths) {
    return Flux.fromIterable(paths)
        .map(path -> composeUrl(path))
        .flatMap((url) -> findLinks(url))
        .map(this::encodeLink);
  }

  Flux<Link> findLinks(URL url) {
    return Mono.just(url)
        .map(this::requestContent)
        .flatMapMany(content -> extractLinks(content));
  }

  private URL composeUrl(String path) {
    return null;
  }

  private <V> V encodeLink(Object o) {
    return null;
  }

  private String requestContent(URL url) {
    return null;
  }

  private Flux<Link> extractLinks(String content) {
    return null;
  }

}

class EncodedLink {

}

class Link {

}
