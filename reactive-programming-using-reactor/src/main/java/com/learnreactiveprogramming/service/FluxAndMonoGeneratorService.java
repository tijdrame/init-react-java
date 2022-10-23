package com.learnreactiveprogramming.service;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux(){
        return Flux.fromIterable(List.of("alex", "ben", "chloe")).log();//db or remote service call
    }

    public Mono<String> nameMono() {
        return Mono.just("Alex").log();
    }

    public Mono<String> nameMono_map_filter(int stringLength) {
        return Mono.just("alex")
        .map(String::toUpperCase)
        .filter(s -> s.length() > stringLength);
    }

    public Mono<List<String>> nameMono_flatMmap(int stringLength) {
        return Mono.just("alex")
        .map(String::toUpperCase)
        .filter(s -> s.length() > stringLength)
        .flatMap(this::splitStringMono);
    }

    public Flux<String> nameMono_flatMmapMany(int stringLength) {
        return Mono.just("alex")
        .map(String::toUpperCase)
        .filter(s -> s.length() > stringLength)
        .flatMapMany(this::splitString);
    }

    private Mono<List<String>> splitStringMono(String s){
        var charArray = s.split("");
        var charList = List.of(charArray);
        return Mono.just(charList);
    }

    public Flux<String> namesFlux_map(int stringLength){
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
        .map(String::toUpperCase)
        .filter(s -> s.length() > stringLength)
        .map(s -> String.join("-", s.length()+"", s))//au lieu de ALEX,  4-ALEX
        .log();//db or remote service call
    }

    public Flux<String> namesFlux_flatMap(int stringLength){
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
        .map(String::toUpperCase)
        .filter(s -> s.length() > stringLength)
        .flatMap(s -> splitString(s))//"A", "L", "E","X", "C","H","L","O","E"
        .log();//db or remote service call
    }

    public Flux<String> namesFlux_transform(int stringLength){
        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
        .filter(s -> s.length() > stringLength);
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
        .transform(filterMap)
        //.map(String::toUpperCase)
        //.filter(s -> s.length() > stringLength)
        .flatMap(s -> splitString(s))//"A", "L", "E","X", "C","H","L","O","E"
        .defaultIfEmpty("default")
        .log();//db or remote service call
    }

    public Flux<String> namesFlux_transform_switchIfEmpty(int stringLength){
        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
        .filter(s -> s.length() > stringLength)
        .flatMap(s-> splitString(s));//sans ça on aura "DEFAULT"
        var defaultFlux = Flux.just("default").transform(filterMap).log();


        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
        .transform(filterMap)
        .switchIfEmpty(defaultFlux)
        .log();//db or remote service call
    }

    public Flux<String> namesFlux_flatMap_async(int stringLength){
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
        .map(String::toUpperCase)
        .filter(s -> s.length() > stringLength)
        .flatMap(s -> splitString_withDelay(s))
        .log();//db or remote service call
    }
    
    public Flux<String> namesFlux_concatMap(int stringLength){
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
        .map(String::toUpperCase)
        .filter(s -> s.length() > stringLength)
        .concatMap(s -> splitString_withDelay(s))
        .log();//db or remote service call
    }

    public Flux<String> splitString_withDelay(String name) {
        var delay = 200;//new Random().nextInt(1000);
        var charArray = name.split("");
        return Flux.fromArray(charArray).delayElements(Duration.ofMillis(delay));
    }

    public Flux<String> splitString(String name) {
        var charArray = name.split("");
        return Flux.fromArray(charArray);
    }

    public Flux<String> namesFlux_immutability(){
        var nameFlux =  Flux.fromIterable(List.of("alex", "ben", "chloe"));
        nameFlux.map(String::toUpperCase);
        return nameFlux;
    }

    public Flux<String> explore_concat() {
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");
        return Flux.concat(abcFlux, defFlux);
    }

    public Flux<String> explore_concatWith() {
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");
        return abcFlux.concatWith(defFlux);
    }

    public Flux<String> explore_concatWith_mono() {
        var aMono = Mono.just("A");
        var bMono = Mono.just("B");
        return aMono.concatWith(bMono);
    }

    public Flux<String> explore_merge() {
        var abcFlux = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(125));
        return Flux.merge(abcFlux, defFlux);
    }

    public Flux<String> explore_mergeWith() {
        var abcFlux = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(125));
        return abcFlux.mergeWith(defFlux);
    }

    public Flux<String> explore_mergeWith_mono() {
        var aMono = Mono.just("A");
        var bMono = Mono.just("B");
        return aMono.mergeWith(bMono);//res = A, B
    }

    public Flux<String> explore_mergeSequential() {
        var abcFlux = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(125));
        return Flux.mergeSequential(abcFlux, defFlux);
    }

    public Flux<String> explore_zip() {
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");
        return Flux.zip(abcFlux, defFlux, (f, s)-> f + s);
    }

    public Flux<String> explore_zip_1() {
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");
        var _123Flux = Flux.just("1", "2", "3");
        var _456Flux = Flux.just("4", "5", "6");
        return Flux.zip(abcFlux, defFlux, _123Flux, _456Flux)
        .map(t4 -> t4.getT1() + t4.getT2() + t4.getT3() + t4.getT4())
        .log();
    }

    public Flux<String> explore_zipWith() {
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");
        return abcFlux.zipWith(defFlux, (f, s)-> f + s);
    }

    public Mono<String> explore_mergeZipWith_mono() {
        var aMono = Mono.just("A");
        var bMono = Mono.just("B");
        return aMono.zipWith(bMono).map(t2 -> t2.getT1()+t2.getT2());//res = AB
    }

    public static void main(String[] args) {
        
        System.out.println(String.join(" - ", "Tidiane", "Dramé"));
        /*fluxAndMonoGeneratorService.nameMono()
        .subscribe(name -> {
            System.out.println("Mono name is : "+name);
        });*/
    }
}
