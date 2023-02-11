package uz.company.controller;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.User;
import uz.company.container.ComponentContainer;
import uz.company.util.AdminInlineKeyboards;

import java.util.List;

public class AdminController {

    private static AdminController instance;

    public void handleMessage(User user, Message message) {
        if (message.hasText()) {
            handletext(message.getText(), message);
        } else if (message.hasPhoto()) {
            handlePhoto(message.getPhoto(), message);
        }
    }

    private void handlePhoto(List<PhotoSize> photo, Message message) {

    }

    private void handletext(String text, Message message) {

        AdminInlineKeyboards adminInlineKeyboards = AdminInlineKeyboards.getInstance();

        String chatId = String.valueOf(message.getChatId());


        SendMessage sendMessage;

        if (text.equals("/start")) {
            sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Hurmatli admin hush kelibsiz )");
            sendMessage.setReplyMarkup(adminInlineKeyboards.getAdminMenu());
            ComponentContainer.bot.sendMsg(sendMessage);
        }
    }

    public void handleCallBackQuery(Message message1, CallbackQuery message) {

    }


    public static AdminController getInstance() {
        if (instance == null) {
            synchronized (AdminController.class) {
                if (instance == null)
                    instance = new AdminController();
            }
        }
        return instance;
    }
}
