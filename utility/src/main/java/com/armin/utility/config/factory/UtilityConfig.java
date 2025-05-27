package com.armin.utility.config.factory;

import com.github.jknack.handlebars.Handlebars;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilityConfig {

//    @Bean
//    public ApplicationProperties applicationProperties() throws IOException {
//        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
//        ObjectMapper mapper = new ObjectMapper();
//        return mapper.readValue(new ClassPathResource(
//                "application-properties.json").getInputStream(), ApplicationProperties.class);
//    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    @Bean
    public Handlebars handlebars() {
        return new Handlebars();
    }
}
