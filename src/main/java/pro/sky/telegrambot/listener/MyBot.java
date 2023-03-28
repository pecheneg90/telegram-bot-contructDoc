package pro.sky.telegrambot.listener;

import lombok.*;
import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.aspectj.bridge.IMessage;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import pro.sky.telegrambot.Service.ConstructorDocService;
import pro.sky.telegrambot.model.ConstructorDoc;
import pro.sky.telegrambot.repository.ConstructorDocRepository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.aspectj.weaver.loadtime.Options.parse;

@Getter
@Setter
@NoArgsConstructor
public class MyBot extends TelegramLongPollingBot {
    private static final Logger log = Logger.getLogger(InputFile.class);
    ConstructorDoc constructorDoc;
    ConstructorDocService constructorDocService;
    ConstructorDocRepository constructorDocRepository;
    final int RECONNECT_PAUSE =10000;

    public MyBot(String botToken, String botUsername) {
        this.botToken = botToken;
        this.botUsername = botUsername;
    }

    public String botToken;
    public String botUsername;

    public void botConnect() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(this);
            log.info("TelegramAPI started. Look for messages");
        } catch (TelegramApiRequestException e) {
            log.error("Cant Connect. Pause " + RECONNECT_PAUSE / 1000 + "sec and try again. Error: " + e.getMessage());
            try {
                Thread.sleep(RECONNECT_PAUSE);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
                return;
            }
            botConnect();
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void onUpdateReceived(Update update) {
        log.debug("Receive new Update. updateID: " + update.getUpdateId());

        Long chatId = update.getMessage().getChatId();
        String inputText = update.getMessage().getText();

        if (inputText.startsWith("/start")) {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Hello. This is start message");
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        if (inputText.startsWith("/update")) {
            try (FileInputStream inputStream = new FileInputStream("my_doc_template.docx")) {
                XWPFDocument doc = new XWPFDocument(inputStream);
                doc.getProperties().getCoreProperties().setTitle("My Document");

                for (XWPFParagraph p : doc.getParagraphs()) {
                    List<XWPFRun> runs = p.getRuns();
                    if (runs != null) {
                        for (XWPFRun r : runs) {
                            String text = r.getText(0);
                            if (text.contains("<название суда>")) {
                                text = text.replace("<название суда>", "Арбитражный суд");
                                r.setText(text, 0);
                            }
                        }
                    }
                }

                FileOutputStream outputStream = new FileOutputStream("my_doc_output.docx");
                doc.write(outputStream);
                outputStream.close();

                SendDocument sendDocument = new SendDocument();
                sendDocument.setChatId(update.getMessage().getChatId());
                execute((sendDocument));

            } catch (IOException e) {
                e.printStackTrace();
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Document generateDocument(String text) {
        // Load the document template from a file or create it programmatically
        // Use Apache POI to insert the text into the specific field in the document template
        // Save the modified document to a temporary file
        // Return the document as a Document object
        return new Document();
    }

    private void sendDocument(Long chatId, Document document) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);
        sendDocument.setDocument(new InputFile(String.valueOf(document)));
        try {
            execute(sendDocument);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}