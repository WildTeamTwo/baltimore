package com.baltimore;

import com.baltimore.common.Console;
import com.baltimore.opendata.Task;
import com.baltimore.opendata.consumer.BaltimoreConsumer;
import com.baltimore.opendata.scrubber.parking.ParkingController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

/**
 * Created by paul on 20.03.19.
 */
public class AppConfiguration {


    @Bean
    public Console console(){
        return new Console();
    }
    @Bean
    public ParkingController parkingController(){
        return new ParkingController();
    }

    @Bean
    public Application app(){
        return new Application();
    }
    @Bean
    public BaltimoreConsumer consumer(){
        return new BaltimoreConsumer();
    }

    @Bean
    public List<Task> tasks(@Autowired BaltimoreConsumer consumer, @Autowired ParkingController parkingController){
        return Arrays.asList(consumer, parkingController);
    }


}
