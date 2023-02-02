package io.github.ms100.paramsplittersample.service;

import io.github.ms100.paramsplitter.annotation.SplitParam;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author zhumengshuai
 */
@Service
public class FoxService {

    public List<String> getFoxList(@SplitParam(3) List<Integer> ids, String str) {
        System.out.println("getFoxList====----" + ids.toString());
        return ids.stream().map(id -> String.format("id:%d,name:%s", id, id)).collect(Collectors.toList());
    }

    public boolean getFoxBoolean(@SplitParam(3) Integer[] ids, String str) {
        System.out.println("getFoxBoolean====----" + Arrays.toString(ids));
        return ids.length >= 2;
    }

    public Map<Integer, String> getFoxMap(@SplitParam(4) Set<Integer> ids, String str) {
        System.out.println("getFoxMap====----" + ids.toString());
        return ids.stream().collect(Collectors.toMap(
                id -> id,
                id -> String.format("id:%d,name:%s", id, id)
        ));
    }


    public Map<Integer, String> getFoxMap(@SplitParam(3) List<Integer> ids, String str) {
        System.out.println("getFoxList2====----" + ids.toString());
        return ids.stream().collect(Collectors.toMap(
                id -> id,
                id -> String.format("id:%d,name:%s", id, id)
        ));
    }

    public int getFoxInt(@SplitParam(3) int[] ids, String str) {
        System.out.println("getFoxInt====----" + Arrays.toString(ids));
        return Arrays.stream(ids).sum();
    }

    public Object[] getFoxObjectArray(@SplitParam(3) Integer[] ids, String str) {
        System.out.println("getFoxObjectArray====----" + Arrays.toString(ids));
        return ids;
    }

    public int[] getFoxIntArray(@SplitParam(3) int[] ids, String str) {
        System.out.println("getFoxIntArray====----" + Arrays.toString(ids));
        return ids;
    }

    public void putFoxList(@SplitParam(5) List<Integer> ids, String str) {
        System.out.println("putFoxList====----" + ids.toString());
    }

    public void putFoxSet(@SplitParam(6) Set<Integer> ids, String str) {
        System.out.println("putFoxSet====----" + ids.toString());
    }

    public CompletableFuture<List<String>> getFoxListCF(@SplitParam(3) List<Integer> ids, String str) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("getFoxListCF====----" + ids.toString() + "---" + Thread.currentThread().getName());
            return ids.stream().map(id -> String.format("id:%d,name:%s", id, id)).collect(Collectors.toList());
        });
    }

    public CompletableFuture<Map<Integer, String>> getFoxMapCF(@SplitParam(4) Set<Integer> ids, String str) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("getFoxMapCF====----" + ids.toString() + "---" + Thread.currentThread().getName());
            return ids.stream().collect(Collectors.toMap(
                    id -> id,
                    id -> String.format("id:%d,name:%s", id, id)
            ));
        });
    }

    public CompletableFuture<Set<String>> getFoxSetCF(@SplitParam(3) List<Integer> ids, String str) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("getFoxSetCF====----" + ids.toString() + "---" + Thread.currentThread().getName());
            return ids.stream().map(id -> String.format("id:%d,name:%s", id, id)).collect(Collectors.toSet());
        });
    }

    public CompletableFuture<Boolean> getFoxBooleanCF(@SplitParam(4) List<Integer> ids, String str) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("putFoxListCF====----" + ids.toString() + "---" + Thread.currentThread().getName());
            return ids.size() > 3;
        });
    }

    public CompletableFuture<Void> putFoxListCF(@SplitParam(4) List<Integer> ids, String str) {
        return CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("putFoxListCF====----" + ids.toString() + "---" + Thread.currentThread().getName());
        });
    }

    public CompletableFuture<Void> putFoxSetCF(@SplitParam(3) Set<Integer> ids, String str) {
        return CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("putFoxSetCF====----" + ids.toString() + "---" + Thread.currentThread().getName());
        });
    }

    public Mono<List<String>> getFoxListMono(@SplitParam(3) List<Integer> ids, String str) {
        return Mono.fromCompletionStage(getFoxListCF(ids, str));
    }

    public Mono<Map<Integer, String>> getFoxMapMono(@SplitParam(4) Set<Integer> ids, String str) {
        return Mono.fromCompletionStage(getFoxMapCF(ids, str));
    }

    public Mono<Set<String>> getFoxSetMono(@SplitParam(3) List<Integer> ids, String str) {
        return Mono.fromCompletionStage(getFoxSetCF(ids, str));
    }

    public Mono<Void> putFoxListMono(@SplitParam(5) List<Integer> ids, String str) {
        return Mono.fromCompletionStage(putFoxListCF(ids, str));
    }

    public Mono<Void> putFoxSetMono(@SplitParam(6) Set<Integer> ids, String str) {
        return Mono.fromCompletionStage(putFoxSetCF(ids, str));
    }

}
