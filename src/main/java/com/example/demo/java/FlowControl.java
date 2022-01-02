package com.example.demo.java;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

public class FlowControl {

  void f() {

    Flux.just(1, 2, 3)
        .subscribe(new Subscriber<Integer>() {
          @Override
          public void onSubscribe(Subscription subscription) {
            subscription.request(1);
          }

          @Override
          public void onNext(Integer integer) {

          }

          @Override
          public void onError(Throwable throwable) {

          }

          @Override
          public void onComplete() {

          }
        });

  }

}
