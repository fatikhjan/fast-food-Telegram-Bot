package uz.company.controller;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.User;
import uz.company.container.ComponentContainer;
import uz.company.container.ThreadSafeBeanContext;
import uz.company.db.DataStore;
import uz.company.util.AdminInlineKeyboards;
import uz.company.util.UserInlineKeybordUtil;

import java.util.List;

public class AdminController {


    public void handleMessage( Message message) {
        if (message.hasText()) {
            handletext(message.getText(), message);
        } else if (message.hasPhoto()) {
            handlePhoto(message.getPhoto(), message);
        }
    }

    private void handlePhoto(List<PhotoSize> photo, Message message) {

    }

    private void handletext(String text, Message message) {

        AdminInlineKeyboards adminInlineKeyboards = ThreadSafeBeanContext.ADMIN_INLINE_KEYBOARDS_THREAD_LOCAL.get();
        UserInlineKeybordUtil userInlineKeybordUtil = ThreadSafeBeanContext.USER_INLINE_KEYBORD_UTIL_THREAD_LOCAL.get();
        DataStore dataStore = ThreadSafeBeanContext.DATA_STORE_THREAD_LOCAL.get();
        String chatId = String.valueOf(message.getChatId());


        SendMessage sendMessage;

        if (text.equals("/start")) {
            sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Hurmatli admin hush kelibsiz )");
            sendMessage.setReplyMarkup(adminInlineKeyboards.getAdminMenu());
            ComponentContainer.bot.sendMsg(sendMessage);

        } else if (text.equals("Menyu qoshish")) {
            sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Qaysi Categoriya boyicha  yangi menu qoshmoqchisiz?");
            sendMessage.setReplyMarkup(userInlineKeybordUtil.getMenuInlineMarkup(dataStore.userMenuList));
            ComponentContainer.bot.sendMsg(sendMessage);
        } else if (text.equals("Menyuni ozgartirish")) {


        } else {
            sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Iltimos Habarni tekshirib uni qayta jonating ");
            ComponentContainer.bot.sendMsg(sendMessage);
        }
    }

    public void handleCallBackQuery(Message message1, CallbackQuery message) {

    }

}
