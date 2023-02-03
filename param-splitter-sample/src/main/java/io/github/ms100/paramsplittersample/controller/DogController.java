package io.github.ms100.paramsplittersample.controller;

import io.github.ms100.paramsplittersample.bean.Dog;
import io.github.ms100.paramsplittersample.service.DogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dog")
public class DogController {

    private final DogService dogService;

    @RequestMapping("/getDog")
    public Dog getDog(@RequestParam("id") Long dogId) {
        return dogService.getDog(dogId);
    }


    @RequestMapping("/getDogs")
    public Map<Long, Dog> getDogs(@RequestParam("id") Long[] dogIds) {
        return dogService.getDogs(Arrays.asList(dogIds));
    }

    @RequestMapping("/getDogList")
    public List<Dog> getDogList(@RequestParam("id") Long[] dogIds) {
        return dogService.getDogList(Arrays.asList(dogIds));
    }

}
