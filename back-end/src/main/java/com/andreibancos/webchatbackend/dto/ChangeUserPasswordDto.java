package com.andreibancos.webchatbackend.dto;

import jakarta.validation.constraints.NotBlank;

public class ChangeUserPasswordDto {
    @NotBlank(message = "oldPassword required.")
    private String oldPassword;
    @NotBlank(message = "newPassword required")
    private String newPassword;

    public @NotBlank(message = "oldPassword required.") String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(@NotBlank(message = "oldPassword required.") String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public @NotBlank(message = "newPassword required") String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(@NotBlank(message = "newPassword required") String newPassword) {
        this.newPassword = newPassword;
    }
}
