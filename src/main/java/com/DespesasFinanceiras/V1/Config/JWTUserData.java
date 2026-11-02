package com.DespesasFinanceiras.V1.Config;

import lombok.Builder;

@Builder
public record JWTUserData(Long userId, String email) {
}
