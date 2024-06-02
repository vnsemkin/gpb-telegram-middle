package org.vnsemkin.semkinmiddleservice.presentation.web_client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendErrorResponse;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendRegistrationReq;
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
    private final static String GET_USER_UUID = "/users/%d";
    private final WebClient webClient;

    public BackendClientInterfaceImp(WebClient.Builder webClientBuilder,
                                     @Value("${backend.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public Result<String, BackendErrorResponse> registerCustomerOnBackend(BackendRegistrationReq req) {
        return webClient
            .post()
            .uri(REG_ENDPOINT)
            .body(BodyInserters.fromValue(req))
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
                BackendErrorResponse backendErrorResponse = new BackendErrorResponse(throwable.getMessage(),
                    EMPTY_STRING, EMPTY_STRING, EMPTY_STRING);
                Result<String, BackendErrorResponse> errorResult =
                    Result.error(backendErrorResponse);
                System.out.println(errorResult);
                return Mono.just(errorResult);
            })
            .block();
    }

    public Result<BackendRespUuid, BackendErrorResponse> getCustomerUuid(BackendRegistrationReq req) {
        return webClient
            .get()
            .uri(String.format(GET_USER_UUID, req.id()))
            .exchangeToMono(response -> {
                if (response.statusCode().is2xxSuccessful()) {
                    return response.bodyToMono(BackendRespUuid.class)
                        .map(Result::<BackendRespUuid, BackendErrorResponse>success)
                        .onErrorResume(UuidValidationException.class, e ->
                            Mono.just(Result.error(
                                new BackendErrorResponse(INVALID_UUID,
                                    EMPTY_STRING, EMPTY_STRING, EMPTY_STRING))
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
                        EMPTY_STRING, EMPTY_STRING, EMPTY_STRING))
                )
            )
            .block();
    }
}