package com.baltimore;

import com.baltimore.google.GeoCodeCache;
import com.baltimore.google.GeoCodeHttpClient;
import com.baltimore.google.GoogleBatchService;
import com.baltimore.google.GoogleClient;
import com.baltimore.opendata.Task;
import com.baltimore.opendata.consumer.BaltimoreDataConsumer;
import com.baltimore.opendata.consumer.OpenDataAPIClient;
import com.baltimore.opendata.scrubber.parking.ParkingController;
import com.baltimore.opendata.scrubber.reader.BatchCitationReader;
import com.baltimore.opendata.scrubber.writer.BatchWriter;
import com.baltimore.persistence.Cache;
import com.baltimore.persistence.DAOImpl;
import com.baltimore.persistence.FileSystemStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by paul on 20.03.19.
 */
public class AppConfiguration {


    @Value("${db.host}")
    private String host;
    @Value("${db.port}")
    private String port;

    @Bean
    public static PropertySourcesPlaceholderConfigurer devPropertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer p = new PropertySourcesPlaceholderConfigurer();
        p.setLocation(new ClassPathResource("application.properties"));
        return p;
    }

    @Bean
    public ParkingController parkingController(@Autowired GoogleBatchService googleBatchService) throws Exception {
        BatchCitationReader batchCitationReader = BatchCitationReader.init();
        BatchWriter writer = BatchWriter.init();
        return new ParkingController(batchCitationReader, googleBatchService, writer);
    }

    @Bean
    public Application app() {
        return new Application();
    }

    @Bean
    public BaltimoreDataConsumer consumer() {
        return new BaltimoreDataConsumer(new OpenDataAPIClient(), new FileSystemStore());
    }

    @Bean
    public List<Task> tasks(@Autowired BaltimoreDataConsumer consumer, @Autowired ParkingController parkingController) {
        return Arrays.asList(consumer, parkingController);
    }

    @Bean
    public Cache cache() throws SQLException {
        return new Cache(DAOImpl.init(host, port));
    }

    @Bean
    public GoogleBatchService googleBatchService(@Autowired Cache cache) throws Exception {
        GeoCodeHttpClient geoCodeHttpClient = new GeoCodeHttpClient();
        GeoCodeCache geoCodeCache = new GeoCodeCache(cache);
        GoogleClient googleClient = new GoogleClient(geoCodeHttpClient, geoCodeCache);
        GoogleBatchService googleBatchService = new GoogleBatchService(googleClient);

        return googleBatchService;
    }

}
