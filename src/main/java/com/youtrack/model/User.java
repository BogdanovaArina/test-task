package com.youtrack.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class User {
    private final String login;
    private final String fullName;
    private final String contact;
    private final String groups;
    private final String lastAccess;
}
