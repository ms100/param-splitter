package io.github.ms100.paramsplittersample.controller;

import io.github.ms100.paramsplittersample.service.FoxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FoxController {

    private final FoxService foxService;

    private final List<Integer> ids = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 5);

    @GetMapping("getFox")
    void getFox() {
        int[] idIntArray = ids.stream().mapToInt(Integer::valueOf).toArray();
        Integer[] idIntegerArray = ids.toArray(new Integer[0]);
        List<String> foxList = foxService.getFoxList(ids, "a");
        log.info("controller log--------{}", foxList);
        Assert.isTrue(ids.size() == foxList.size(), "sorry1");
        Map<Integer, String> foxMap = foxService.getFoxMap(ids, "a");
        log.info("controller log--------{}", foxMap);
        Assert.isTrue(ids.stream().distinct().count() == foxMap.size(), "sorry2");
        log.info("controller log--------{}", foxService.getFoxMap(new HashSet<>(ids), "a"));
        foxService.putFoxList(ids, "a");
        foxService.putFoxSet(new HashSet<>(ids), "a");
        boolean foxBoolean = foxService.getFoxBoolean(idIntegerArray, "a");
        log.info("controller log--------{}", foxBoolean);
        Assert.isTrue(foxBoolean, "sorry3");
        int foxInt = foxService.getFoxInt(idIntArray, "a");
        log.info("controller log--------{}", foxInt);
        Assert.isTrue(ids.stream().mapToInt(Integer::valueOf).sum() == foxInt, "sorry4");
        Object[] foxObjectArray = foxService.getFoxObjectArray(idIntegerArray, "a");
        log.info("controller log--------{}", Arrays.toString(foxObjectArray));
        Assert.isTrue(Arrays.toString(idIntegerArray).equals(Arrays.toString(foxObjectArray)), "sorry5");
        int[] foxIntArray = foxService.getFoxIntArray(idIntArray, "a");
        log.info("controller log--------{}", Arrays.toString(foxIntArray));
        Assert.isTrue(Arrays.toString(idIntArray).equals(Arrays.toString(foxIntArray)), "sorry6");
    }

    @GetMapping("getFoxCF")
    void getFoxCF() {
        CompletableFuture<List<String>> cf1 = foxService.getFoxListCF(ids, "a");
        log.info("cf1 join");
        log.info("controller log--------{}", cf1.join());
        CompletableFuture<Map<Integer, String>> cf2 = foxService.getFoxMapCF(new HashSet<>(ids), "a");
        log.info("cf2 join");
        log.info("controller log--------{}", cf2.join());
        CompletableFuture<Set<String>> cf3 = foxService.getFoxSetCF(ids, "a");
        log.info("cf3 join");
        log.info("controller log--------{}", cf3.join());
        CompletableFuture<Void> cf4 = foxService.putFoxListCF(ids, "a");
        log.info("cf4 join");
        cf4.join();
        CompletableFuture<Void> cf5 = foxService.putFoxSetCF(new HashSet<>(ids), "a");
        log.info("cf5 join");
        cf5.join();
        CompletableFuture<Boolean> cf6 = foxService.getFoxBooleanCF(ids, "a");
        log.info("cf6 join");
        log.info("controller log--------{}", cf6.join());
    }

    @GetMapping("getFoxMono")
    void getFoxMono() {
        Mono<List<String>> mono1 = foxService.getFoxListMono(ids, "a");
        log.info("mono1 block");
        log.info("controller log---------{}", mono1.block());
        Mono<Map<Integer, String>> mono2 = foxService.getFoxMapMono(new HashSet<>(ids), "a");
        log.info("mono2 block");
        log.info("controller log---------{}", mono2.block());
        Mono<Set<String>> mono3 = foxService.getFoxSetMono(ids, "a");
        log.info("mono3 block");
        log.info("controller log---------{}", mono3.block());
        Mono<Void> mono4 = foxService.putFoxListMono(ids, "a");
        log.info("mono4 block");
        mono4.block();
        Mono<Void> mono5 = foxService.putFoxSetMono(new HashSet<>(ids), "a");
        log.info("mono5 block");
        mono5.block();
    }
}
