package com.learnreactiveprogramming.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import reactor.test.StepVerifier;

//@ExtendWith(MockitoExtension.class)
class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService service = new FluxAndMonoGeneratorService();

    @Test
    void nameFlux(){
        //given

        //when
        var nameFlux = service.namesFlux();
        //then
        StepVerifier.create(nameFlux)//create fait aussi le subscribe
        //.expectNext("Tidiane", "Alex", "Ben")//verifie tout le contenue
        //.expectNextCount(3)//compte le nb delements
        .expectNext("alex")
        .expectNextCount(2)//ne reste que 2
        .verifyComplete();

    }

    @Test
    void testNamesFlux_map() {
        int stringLength = 3;
        var nameFlux = service.namesFlux_map(stringLength);
        StepVerifier.create(nameFlux)
        .expectNext("4-ALEX", "5-CHLOE")
        .verifyComplete();
    }

    @Test
    void testNamesFlux_flatMap() {
        int stringLength = 3;
        var nameFlux = service.namesFlux_flatMap(stringLength);
        StepVerifier.create(nameFlux)
        .expectNext("A", "L", "E","X", "C","H","L","O","E")
        .verifyComplete();
    }

    @Test
    void testNamesFlux_flatMap_async() {
        int stringLength = 3;
        var nameFlux = service.namesFlux_flatMap_async(stringLength);
        StepVerifier.create(nameFlux)
        //.expectNext("A", "L", "E","X", "C","H","L","O","E")//l'ordre n'est pas garantie
        .expectNextCount(9)
        .verifyComplete();
    }

    @Test
    void testNamesFlux_concatMap() {
        int stringLength = 3;
        var nameFlux = service.namesFlux_concatMap(stringLength);
        StepVerifier.create(nameFlux)
        .expectNext("A", "L", "E","X", "C","H","L","O","E")
        .verifyComplete();
    }

    @Test
    void testNameMono_flatMmap() {
        int stringLength = 3;
        var nameFlux = service.nameMono_flatMmap(stringLength);
        StepVerifier.create(nameFlux)
        .expectNext(List.of("A", "L", "E","X"))
        .verifyComplete();
    }

    @Test
    void testNameMono_flatMmapMany() {
        int stringLength = 3;
        var nameFlux = service.nameMono_flatMmapMany(stringLength);
        StepVerifier.create(nameFlux)
        .expectNext("A", "L", "E","X")
        .verifyComplete();
    }

    @Test
    void testNamesFlux_transform() {
        int stringLength = 3;
        var nameFlux = service.namesFlux_transform(stringLength);
        StepVerifier.create(nameFlux)
        .expectNext("A", "L", "E","X", "C","H","L","O","E")
        .verifyComplete();
    }

    @Test
    void testNamesFlux_transform_1() {
        int stringLength = 6;
        var nameFlux = service.namesFlux_transform(stringLength);
        StepVerifier.create(nameFlux)
        .expectNext("default")
        .verifyComplete();
    }

    @Test
    void testNamesFlux_transform_switchIfEmpty() {
        int stringLength = 6;
        var nameFlux = service.namesFlux_transform_switchIfEmpty(stringLength);
        StepVerifier.create(nameFlux)
        .expectNext("D", "E", "F","A","U","L","T")
        .verifyComplete();
    }

    @Test
    void testExplore_concat() {
        var concatFlux = service.explore_concat();
        StepVerifier.create(concatFlux)
        .expectNext("A", "B", "C","D","E","F")
        .verifyComplete();
    }

    @Test
    void testExplore_merge() {
        
        var value = service.explore_merge();
        StepVerifier.create(value)
        .expectNext("A", "D", "B","E","C","F")
        .verifyComplete();
    }

    @Test
    void testExplore_mergeSequential() {
        
        var value = service.explore_mergeSequential();
        StepVerifier.create(value)
        .expectNext("A", "B", "C","D","E","F")
        .verifyComplete();
    }

    @Test
    void testExplore_zip() {
        var value = service.explore_zip();
        StepVerifier.create(value)
        .expectNext("AD", "BE", "CF")
        .verifyComplete();
    }

    @Test
    void testExplore_zip_1() {
        var value = service.explore_zip_1();
        StepVerifier.create(value)
        .expectNext("AD14", "BE25", "CF36")
        .verifyComplete();
    }

    @Test
    void testExplore_zipWith() {
        var value = service.explore_zipWith();
        StepVerifier.create(value)
        .expectNext("AD", "BE", "CF")
        .verifyComplete();
    }
}