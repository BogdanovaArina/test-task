package com.youtrack.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserCreationInfo {
    private final String login;
    private final String password;
    private final String confirmPassword;

    private final String fullName;
    private final String email;
    private final String jabber;
    private final Boolean passwordChangeCheckbox;
}