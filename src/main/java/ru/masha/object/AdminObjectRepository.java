package ru.masha.object;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdminObjectRepository extends JpaRepository<AdminObject, UUID> {

    AdminObject findByCodeAndSystemCodeAndObjectTypeCode(String code, String systemCode, String objectTypeCode);

    AdminObject findByTitleAndSystemCodeAndObjectTypeCode(String title, String systemCode, String objectTypeCode);

}
