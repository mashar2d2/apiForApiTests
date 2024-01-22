package ru.masha.object;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeleteAdminObjectRequest {
    UUID id;
    String systemCode;
}
