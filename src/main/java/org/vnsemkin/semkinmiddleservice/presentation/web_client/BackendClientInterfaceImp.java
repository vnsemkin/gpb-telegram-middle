package org.vnsemkin.semkinmiddleservice.presentation.web_client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.AccountInfoResponse;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.AccountRegistrationReq;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendErrorResponse;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendRegistrationReq;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendRespUuid;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.TransferRequest;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.TransferResponse;
import org.vnsemkin.semkinmiddleservice.application.external.BackendClientInterface;
import org.vnsemkin.semkinmiddleservice.domain.models.Result;
import org.vnsemkin.semkinmiddleservice.presentation.exception.UuidValidationException;
import reactor.core.publisher.Mono;

@Component
public class BackendClientInterfaceImp implements BackendClientInterface {
    private final static String EMPTY_STRING = "";
    private final static String INVALID_UUID = "Invalid UUID format";
    private final static String USER_CREATED = "User created.";
    private final static String ACCOUNT_CREATED = "Account created.";
    private final static String USERS_URL = "/v2/users";
    private final static String USER_URL_BY_USERNAME = "/v2/users/%s";
    private final static String USER_URL_BY_ID = "/v2/users/%d";
    private final static String ACCOUNTS_URL = "/v2/users/%d/accounts";
    private final static String TRANSFERS_URL = "/v2/transfers";
    private final WebClient webClient;

    public BackendClientInterfaceImp(WebClient.Builder webClientBuilder,
                                     @Value("${backend.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public Result<String, BackendErrorResponse> registerCustomer(BackendRegistrationReq req) {
        return webClient
            .post()
            .uri(USERS_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(req))
            .exchangeToMono(response ->
                response.statusCode().is2xxSuccessful() ?
                    Mono.just(Result.<String, BackendErrorResponse>success(USER_CREATED)) :
                    response.bodyToMono(BackendErrorResponse.class)
                        .map(Result::<String, BackendErrorResponse>error)
            )
            .onErrorResume(throwable -> {
                BackendErrorResponse backendErrorResponse = new BackendErrorResponse(throwable.getMessage(),
                    EMPTY_STRING, EMPTY_STRING, EMPTY_STRING);
                Result<String, BackendErrorResponse> errorResult =
                    Result.error(backendErrorResponse);
                return Mono.just(errorResult);

            })
            .block();
    }

    public Result<BackendRespUuid, BackendErrorResponse> getCustomerUuid(String username) {
        return webClient
            .get()
            .uri(String.format(USER_URL_BY_USERNAME, username))
            .exchangeToMono(response ->
                response.statusCode().is2xxSuccessful() ?
                    response.bodyToMono(BackendRespUuid.class)
                        .map(Result::<BackendRespUuid, BackendErrorResponse>success)
                        .onErrorResume(UuidValidationException.class, e ->
                            Mono.just(Result.error(
                                new BackendErrorResponse(INVALID_UUID,
                                    EMPTY_STRING, EMPTY_STRING, EMPTY_STRING))
                            )) : response.bodyToMono(BackendErrorResponse.class)
                    .map(Result::<BackendRespUuid, BackendErrorResponse>error))
            .onErrorResume(throwable ->
                Mono.just(Result.error(new BackendErrorResponse(throwable.getMessage(),
                    EMPTY_STRING, EMPTY_STRING, EMPTY_STRING))
                )
            )
            .block();
    }

    public Result<BackendRespUuid, BackendErrorResponse> getCustomerUuid(long id) {
        return webClient
            .get()
            .uri(String.format(USER_URL_BY_ID, id))
            .exchangeToMono(response ->
                response.statusCode().is2xxSuccessful() ?
                    response.bodyToMono(BackendRespUuid.class)
                        .map(Result::<BackendRespUuid, BackendErrorResponse>success)
                        .onErrorResume(UuidValidationException.class, e ->
                            Mono.just(Result.error(
                                new BackendErrorResponse(INVALID_UUID,
                                    EMPTY_STRING, EMPTY_STRING, EMPTY_STRING))
                            )) : response.bodyToMono(BackendErrorResponse.class)
                    .map(Result::<BackendRespUuid, BackendErrorResponse>error))
            .onErrorResume(throwable ->
                Mono.just(Result.error(new BackendErrorResponse(throwable.getMessage(),
                    EMPTY_STRING, EMPTY_STRING, EMPTY_STRING))
                )
            )
            .block();
    }

    public Result<String, BackendErrorResponse> registerAccount(AccountRegistrationReq req) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode accountName = mapper.createObjectNode().put("accountName", req.accountName());
        String accountNameJson;
        try {
            accountNameJson = mapper.writeValueAsString(accountName);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return webClient
            .post()
            .uri(String.format(ACCOUNTS_URL, req.id()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(accountNameJson))
            .exchangeToMono(response -> response.statusCode().is2xxSuccessful() ?
                Mono.just(Result.<String, BackendErrorResponse>success(ACCOUNT_CREATED)) :
                response.bodyToMono(BackendErrorResponse.class)
                    .map(Result::<String, BackendErrorResponse>error))
            .onErrorResume(throwable -> {
                BackendErrorResponse backendErrorResponse = new BackendErrorResponse(throwable.getMessage(),
                    EMPTY_STRING, EMPTY_STRING, EMPTY_STRING);
                Result<String, BackendErrorResponse> errorResult =
                    Result.error(backendErrorResponse);
                return Mono.just(errorResult);
            })
            .block();
    }

    public Result<AccountInfoResponse, BackendErrorResponse> getAccount(long tgId) {
        return webClient
            .get()
            .uri(String.format(ACCOUNTS_URL, tgId))
            .exchangeToMono(response ->
                response.statusCode().is2xxSuccessful() ?
                    response.bodyToMono(AccountInfoResponse.class)
                        .map(Result::<AccountInfoResponse, BackendErrorResponse>success) :
                    response.bodyToMono(BackendErrorResponse.class)
                        .map(Result::<AccountInfoResponse, BackendErrorResponse>error))
            .onErrorResume(throwable ->
                Mono.just(Result.error(new BackendErrorResponse(throwable.getMessage(),
                    EMPTY_STRING, EMPTY_STRING, EMPTY_STRING))
                )
            )
            .block();
    }

    public Result<TransferResponse, BackendErrorResponse> transferMoney(TransferRequest request) {
        return webClient
            .post()
            .uri(String.format(TRANSFERS_URL))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .exchangeToMono(response ->
                response.statusCode().is2xxSuccessful() ?
                    response.bodyToMono(TransferResponse.class)
                        .map(Result::<TransferResponse, BackendErrorResponse>success) :
                    response.bodyToMono(BackendErrorResponse.class)
                        .map(Result::<TransferResponse, BackendErrorResponse>error))
            .onErrorResume(throwable ->
                Mono.just(Result.error(new BackendErrorResponse(throwable.getMessage(),
                    EMPTY_STRING, EMPTY_STRING, EMPTY_STRING))
                )
            )
            .block();
    }
}