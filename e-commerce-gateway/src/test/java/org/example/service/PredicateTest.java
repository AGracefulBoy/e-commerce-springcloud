package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author zhoudashuai
 * @date 2021年12月05日 5:44 下午
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class PredicateTest {
    public static List<String> MICRO_SERVICE= Arrays.asList(
            "nacos","authority","gateway","ribbon","feign"
    );

    @Test
    public void testPredicateTest(){
        Predicate<String> letterLengthLimit = s -> s.length()>5;
        MICRO_SERVICE.stream().filter(letterLengthLimit).forEach(System.out::println);
    }

    @Test
    public void testPredicateAnd(){
        Predicate<String> letterLengthLimit = s->s.length()>5;
        Predicate<String> letterStartWith = s->s.startsWith("gate");
        MICRO_SERVICE.stream().filter(letterLengthLimit.and(letterStartWith))
                .forEach(System.out::println);
    }

    @Test
    public void testPredicateOr(){
        Predicate<String> letterLengthLimit = s->s.length()>5;
        Predicate<String> letterStartWith = s->s.startsWith("gate");
        MICRO_SERVICE.stream().filter(letterLengthLimit.or(letterStartWith))
                .forEach(System.out::println);
    }
    @Test
    public void testPredicateIsEqual(){
        Predicate<String> equalGateway = s->Predicate.isEqual("gateway").test(s);
        MICRO_SERVICE.stream().filter(equalGateway)
                .forEach(System.out::println);
    }
}
