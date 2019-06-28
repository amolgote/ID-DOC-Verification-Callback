package com.jumio.callback.api.swagger;

import com.google.common.base.Predicate;
import com.jumio.callback.api.utils.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket postsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(Constants.SWAGGER_API_GROUP)
                .apiInfo(apiInfo())
                .select()
                .paths(postPaths())
                .build()
                .globalOperationParameters(authHeader());
    }

    List<Parameter> authHeader(){
        ParameterBuilder apiHandShakeKey = new ParameterBuilder();
        java.util.List<Parameter> aParameters = new ArrayList<>();
        return aParameters;
    }

    private Predicate<String> postPaths() {
        return or(regex(Constants.SWAGGER_PATH), regex(Constants.SWAGGER_PATH));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(Constants.SWAGGER_TITLE)
                .description(Constants.SWAGGER_DESC)
                .termsOfServiceUrl(Constants.ICW_WEBSITE_URL)
                .licenseUrl(Constants.ICW_WEBSITE_URL)
                .version(Constants.SWAGGER_API_VERSION)
                .build();
    }

}
