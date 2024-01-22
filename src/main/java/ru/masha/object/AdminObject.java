package ru.masha.object;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.UUID;

@Data
@Entity
@Table(schema = "admin", name = "object", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"code", "systemCode", "objectTypeCode"}),
        @UniqueConstraint(columnNames = {"title", "systemCode", "objectTypeCode"}),
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminObject {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "uuid-char")
    UUID id;

    @NotNull
    @Length(max = 100)
    @Pattern(regexp = "\\w*")
    @Column(nullable = false, length = 100, updatable = false)
    String code;

    @NotNull
    @Length(max = 300)
    @Column(nullable = false, length = 300)
    String title;

    @NotNull
    @Column(nullable = false)
    @Pattern(regexp = "HOP|HEY")
    String objectTypeCode;

    @NotNull
    @Column(nullable = false)
    @Pattern(regexp = "LOL|KEK")
    String systemCode;
}
