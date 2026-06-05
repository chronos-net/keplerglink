package com.edomex.kiliantRSP.service.DavsService.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DestinatarioChequeStrategyFactory {

    private final Map<String, DestinatarioChequeStrategy> strategyMap = new HashMap<>();

    public DestinatarioChequeStrategyFactory(
            List<DestinatarioChequeStrategy> strategies
    ) {
        for (DestinatarioChequeStrategy strategy : strategies) {
            strategyMap.put(
                    strategy.getTipo().toUpperCase(),
                    strategy
            );
        }
    }

    public DestinatarioChequeStrategy getStrategy(String tipo) {
        DestinatarioChequeStrategy strategy =
                strategyMap.get(tipo.toUpperCase());

        if (strategy == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Destinatario de cheque no soportado: " + tipo
            );
        }

        return strategy;
    }
}

