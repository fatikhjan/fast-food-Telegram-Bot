package uz.company.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.company.container.ComponentContainer;
import uz.company.controller.AdminController;
import uz.company.controller.CureerController;
import uz.company.controller.UserController;

public class Kfc_bot extends TelegramLongPollingBot {
    /**
     * Return username of this bot
     */
    @Override
    public String getBotUsername() {
        return ComponentContainer.botUserName;
    }

    /**
     * Returns the token of the bot to be able to perform Telegram Api Requests
     *
     * @return Token of the bot
     */
    @Override
    public String getBotToken() {
        return ComponentContainer.botToken;
    }

    /**
     * This method is called when receiving updates via GetUpdates method
     *
     * @param update Update received
     */
    @Override
    public void onUpdateReceived(Update update) {

        UserController userController = UserController.getInstance();
        CureerController cureerController = CureerController.getInstance();
        AdminController adminController = AdminController.getInstance();

        //Object created for using it for everywhere and keep memory


        Message message = update.getMessage();

        if (update.hasMessage()) {
            String chatId = String.valueOf(message.getChatId());


            User user = message.getFrom();


            if (ComponentContainer.AdminsList.contains(chatId)) {
                adminController.handleMessage(user, message);
            } else if (ComponentContainer.curreersList.contains(chatId)) {
                cureerController.handleMessage(user, message);
            } else {
                userController.handleMessage(message, user);
            }
        } else if (update.hasCallbackQuery()) {

            CallbackQuery callbackQuery = update.getCallbackQuery();

            message = callbackQuery.getMessage();

            String chatId = String.valueOf(callbackQuery.getMessage().getChatId());

            if (ComponentContainer.AdminsList.contains(chatId)) {
                adminController.handleCallBackQuery(message, callbackQuery);
            } else if (ComponentContainer.curreersList.contains(chatId)) {
                cureerController.handleCallBackQuery(message, callbackQuery);
            } else {
                userController.handleCallBackQuery(message, callbackQuery);
            }
        }
    }

    /**
     * it was created for get rid of use one type method
     */
    public void sendMsg(Object obj) {
        try {
            if (obj instanceof SendMessage) {
                execute((SendMessage) obj);
            } else if (obj instanceof DeleteMessage) {
                execute((DeleteMessage) obj);
            } else if (obj instanceof EditMessageText) {
                execute((EditMessageText) obj);
            } else if (obj instanceof SendPhoto) {
                Message message = execute((SendPhoto) obj);

            } else if (obj instanceof SendDocument) {
                execute((SendDocument) obj);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
