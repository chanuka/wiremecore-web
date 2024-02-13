package com.cba.core.wiremeweb.dto;

import jakarta.validation.constraints.NotBlank;

public class TokenRefreshRequestDto {
  @NotBlank(message = "{validation.refresh_token.token.empty}")
  private String refreshToken;

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
