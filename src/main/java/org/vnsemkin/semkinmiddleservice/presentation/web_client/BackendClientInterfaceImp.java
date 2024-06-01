package org.vnsemkin.semkinmiddleservice.presentation.web_client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendErrorResponse;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.ResultBackendDto;
import org.vnsemkin.semkinmiddleservice.application.external.BackendClientInterface;
import reactor.core.publisher.Mono;

@Component
public class BackendClientInterfaceImp implements BackendClientInterface {
    private final static String REG_ENDPOINT = "/users";
    private final static String USER_CREATED = "User created.";
    private final static String GET_USER_UUID = "/user/%d";
    private final WebClient webClient;

    public BackendClientInterfaceImp(WebClient.Builder webClientBuilder,
                                     @Value("${backend.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public ResultBackendDto<?> registerCustomerOnBackend(long customerId) {
        ParameterizedTypeReference<BackendErrorResponse> responseType =
            new ParameterizedTypeReference<>() {
            };
        return webClient
            .post()
            .uri(REG_ENDPOINT)
            .body(BodyInserters.fromValue(customerId))
            .exchangeToMono(response -> {
                if (response.statusCode().is2xxSuccessful()) {
                    return Mono.just(ResultBackendDto.success(USER_CREATED));
                } else {
                    return response.bodyToMono(responseType)
                        .map(ResultBackendDto::failure);
                }
            })
            .onErrorResume(throwable -> Mono.just(ResultBackendDto
                .failure(new BackendErrorResponse(throwable.getMessage(),
                    "",
                    "",
                    ""))))
            .block();
    }

    public ResultBackendDto<?> getCustomerUuid(long id) {
        ParameterizedTypeReference<BackendErrorResponse> responseType =
            new ParameterizedTypeReference<>() {
            };
        return webClient
            .get()
            .uri(String.format(GET_USER_UUID, id))
            .exchangeToMono(response -> {
                if (response.statusCode().is2xxSuccessful()) {
                    return Mono.just(ResultBackendDto.success(response.bodyToMono(String.class)));
                } else {
                    return response.bodyToMono(responseType)
                        .map(ResultBackendDto::failure);
                }
            })
            .onErrorResume(throwable -> Mono.just(ResultBackendDto
                .failure(new BackendErrorResponse(throwable.getMessage(),
                    "",
                    "",
                    ""))))
            .block();
    }
}
