package pro.sky.telegrambot;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.ApiConstants;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pro.sky.telegrambot.listener.MyBot;

public class TelegramBotApplication {

	public static void main(String[] args) throws TelegramApiException {
		MyBot myBot = new MyBot ("5990492137:AAEYAQQffGOBhDoRP7s08w3w8Cc6xqkk8-0","constructorDoc_bot");
		myBot.botConnect();
	}
}
