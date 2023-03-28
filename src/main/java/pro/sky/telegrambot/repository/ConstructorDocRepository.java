package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.ConstructorDoc;
@Repository
public interface ConstructorDocRepository extends JpaRepository<ConstructorDoc,Long> {
}

