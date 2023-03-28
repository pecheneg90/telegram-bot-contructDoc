package pro.sky.telegrambot.Service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import pro.sky.telegrambot.model.ConstructorDoc;
import pro.sky.telegrambot.repository.ConstructorDocRepository;

@Service
public abstract class ConstructorDocService extends TelegramLongPollingBot {
   private final ConstructorDocRepository constructorDocRepository;

    protected ConstructorDocService(ConstructorDocRepository constructorDocRepository) {
        this.constructorDocRepository = constructorDocRepository;
    }

    public ConstructorDoc create(ConstructorDoc constructorDoc) {
        return constructorDocRepository.save(constructorDoc);
    }
}
