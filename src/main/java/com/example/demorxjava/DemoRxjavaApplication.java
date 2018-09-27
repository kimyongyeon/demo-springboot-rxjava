package com.example.demorxjava;

import io.reactivex.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
@RestController
public class DemoRxjavaApplication implements ApplicationRunner {

    @Autowired
    BoardRepository boardRepository;

    public static void main(String[] args) {
        SpringApplication.run(DemoRxjavaApplication.class, args);
    }

    @RequestMapping("/board")
    public Mono<Board> board() {
        return boardRepository.findOne(new Long(1));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/invoice")
    public Observable<Invoice> getVoices() {
        return Observable.just(new Invoice("kkk"), new Invoice("yyy"));
    }

    // invoice instance -> string
    private Observable<String> getNames(Observable<Invoice> invoiceObservable) {
        return invoiceObservable.map(r -> r.name);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();


        Board board = new Board();
        List<Board> list = new ArrayList<>();
        list.add(board);
        list.add(board);
        list.add(board);
        list.add(board);
        boardRepository.saveAll(list);


        Observable<Invoice> observable = Observable.just(new Invoice("kkk"), new Invoice("yyy"));

        observable.doOnNext(r -> System.out.println("doOnNext => " + r.name)).subscribe();

        observable.subscribe(r -> System.out.println("subscribe => " + r.name));

        observable.forEach(r -> System.out.println("just => " + r.name));
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }

}
