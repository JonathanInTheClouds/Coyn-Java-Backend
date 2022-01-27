package dev.jonathandlab.com.Coyn.server.config.location;

import com.maxmind.geoip2.DatabaseReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import ua_parser.Parser;

import java.io.File;
import java.io.IOException;

@Configuration
public class LocationConfig {

    @Bean(name="GeoIPCity")
    public DatabaseReader databaseReader() throws IOException {
        File file = ResourceUtils.getFile("classpath:maxmind/GeoLite2-City.mmdb");
        return new DatabaseReader.Builder(file)
                .build();
    }

    @Bean
    public Parser uaParser() throws IOException {
        return new Parser();
    }

}
