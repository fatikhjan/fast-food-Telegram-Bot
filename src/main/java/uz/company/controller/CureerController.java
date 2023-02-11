package uz.company.controller;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

public class CureerController {

    private static CureerController instance;

    public void handleCallBakcQuery(Message message1, CallbackQuery message) {

    }

    public void handleMessage(User user, Message message) {

    }


    public static CureerController getInstance() {
        if (instance == null) {
            synchronized (CureerController.class) {
                if (instance == null)
                    instance = new CureerController();
            }
        }
        return instance;
    }

    public void handleCallBackQuery(Message message, CallbackQuery callbackQuery) {

    }
}
