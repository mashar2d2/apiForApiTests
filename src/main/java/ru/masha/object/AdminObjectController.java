package ru.masha.object;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.masha.error.ApiError;
import ru.masha.error.ErrorResponse;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminObjectController {

    AdminObjectRepository adminObjectRepository;

    private static final Map<String, String> CODE_TRANSLATIONS = Map.of(
            "NotNull", "Обязательность заполнения",
            "Length", "Длина превышена"
    );

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponse onValidation(MethodArgumentNotValidException e) {
        return e.getBindingResult().getFieldErrors().stream()
                .map(x -> new ApiError("TOAST_NOTIFICATION_ERROR", "PPRM_BAD_REQUEST_ERROR", CODE_TRANSLATIONS.getOrDefault(x.getCode(), x.getCode()), x.getField() + " " + x.getDefaultMessage(), x.getField()))
                .collect(Collectors.collectingAndThen(Collectors.toList(), ErrorResponse::new));
    }

    @PostMapping(path = "/api/v1/admin/objects")
    public ResponseEntity<?> createAdminObject(@Valid @RequestBody AdminObject adminObject) {
        AdminObject bySystemCodeAndCode = adminObjectRepository.findByCodeAndSystemCodeAndObjectTypeCode(adminObject.getCode(), adminObject.getSystemCode(), adminObject.getObjectTypeCode());
        AdminObject bySystemCodeAndTitle = adminObjectRepository.findByTitleAndSystemCodeAndObjectTypeCode(adminObject.getTitle(), adminObject.getSystemCode(), adminObject.getObjectTypeCode());

        if (bySystemCodeAndCode != null && bySystemCodeAndTitle != null) {
            return ResponseEntity.status(409).body(new ErrorResponse(
                    List.of(
                            new ApiError("TOAST_NOTIFICATION_ERROR", "PPRM_BAD_REQUEST_ERROR", "CONFLICT", "Объект с таким кодом уже существует", "codeConflict"),
                            new ApiError("TOAST_NOTIFICATION_ERROR", "PPRM_BAD_REQUEST_ERROR", "CONFLICT", "Объект с таким названием уже существует", "titleConflict")
                    )
            ));
        }

        if (bySystemCodeAndCode != null) {
            return ResponseEntity.status(409).body(new ErrorResponse(
                    List.of(
                            new ApiError("TOAST_NOTIFICATION_ERROR", "PPRM_BAD_REQUEST_ERROR", "CONFLICT", "Объект с таким кодом уже существует", "codeConflict")
                    )
            ));
        }

        if (bySystemCodeAndTitle != null) {
            return ResponseEntity.status(409).body(new ErrorResponse(
                    List.of(
                            new ApiError("TOAST_NOTIFICATION_ERROR", "PPRM_BAD_REQUEST_ERROR", "CONFLICT", "Объект с таким названием уже существует", "titleConflict")
                    )
            ));
        }
        return ResponseEntity.ok(adminObjectRepository.saveAndFlush(adminObject));
    }

    @DeleteMapping(path = "/api/v1/admin/objects")
    public ResponseEntity<Void> deleteAdminObject(@RequestBody DeleteAdminObjectRequest request) {
        adminObjectRepository.deleteById(request.getId());
        return ResponseEntity.status(204).build();
    }
}
