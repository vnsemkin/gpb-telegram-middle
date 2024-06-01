package org.vnsemkin.semkinmiddleservice.presentation.web_client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendErrorResponse;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendRespUuid;
import org.vnsemkin.semkinmiddleservice.application.external.BackendClientInterface;
import org.vnsemkin.semkinmiddleservice.domain.models.Result;
import org.vnsemkin.semkinmiddleservice.presentation.exception.UuidValidationException;
import reactor.core.publisher.Mono;

@Component
public class BackendClientInterfaceImp implements BackendClientInterface {
    private final static String EMPTY_STRING ="";
    private final static String INVALID_UUID = "Invalid UUID format";
    private final static String REG_ENDPOINT = "/users";
    private final static String USER_CREATED = "User created.";
    private final static String GET_USER_UUID = "/user/%d";
    private final WebClient webClient;

    public BackendClientInterfaceImp(WebClient.Builder webClientBuilder,
                                     @Value("${backend.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public Result<String, BackendErrorResponse> registerCustomerOnBackend(long customerId) {
        return webClient
            .post()
            .uri(REG_ENDPOINT)
            .body(BodyInserters.fromValue(customerId))
            .exchangeToMono(response -> {
                if (response.statusCode().is2xxSuccessful()) {
                    Result<String, BackendErrorResponse> userCreated = Result.success(USER_CREATED);
                    return Mono.just(userCreated);
                } else {
                    return response.bodyToMono(BackendErrorResponse.class)
                        .map(Result::<String, BackendErrorResponse>error);
                }
            })
            .onErrorResume(throwable -> {
                Result<String, BackendErrorResponse> errorResult =
                    Result.error(new BackendErrorResponse(throwable.getMessage(),
                        EMPTY_STRING, EMPTY_STRING, EMPTY_STRING));
                return Mono.just(errorResult);
            })
            .block();
    }

    public Result<BackendRespUuid, BackendErrorResponse> getCustomerUuid(long id) {
        return webClient
            .get()
            .uri(String.format(GET_USER_UUID, id))
            .exchangeToMono(response -> {
                if (response.statusCode().is2xxSuccessful()) {
                    return response.bodyToMono(BackendRespUuid.class)
                        .map(Result::<BackendRespUuid, BackendErrorResponse>success)
                        .onErrorResume(UuidValidationException.class, e ->
                            Mono.just(Result.error(
                                new BackendErrorResponse(INVALID_UUID,
                                    null, null, null))
                            )
                        );
                } else {
                    return response.bodyToMono(BackendErrorResponse.class)
                        .map(Result::<BackendRespUuid, BackendErrorResponse>error);
                }
            })
            .onErrorResume(throwable ->
                Mono.just(Result.error(
                    new BackendErrorResponse(throwable.getMessage(),
                        null, null, null))
                )
            )
            .block();
    }
}