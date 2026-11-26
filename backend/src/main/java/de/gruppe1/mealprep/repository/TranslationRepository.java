package de.gruppe1.mealprep.repository;

import de.gruppe1.mealprep.entity.Translation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TranslationRepository extends JpaRepository<Translation, Long> {

    List<Translation> findByHashIn(List<String> hashes);
}