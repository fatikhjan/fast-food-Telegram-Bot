package uz.company.controller;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import uz.company.constants.UserKeyboardConstants;
import uz.company.container.ComponentContainer;
import uz.company.db.DataStore;
import uz.company.db.State;
import uz.company.model.Product;
import uz.company.model.enums.Type;
import uz.company.service.UserService;
import uz.company.util.UserInlineKeybordUtil;
import uz.company.util.UserKeybordUtil;

import java.util.ArrayList;

public class UserController {

    private static UserController instance;
    public static uz.company.model.User userr;


    public void handleMessage(Message message, User user) {

        userr = new uz.company.model.User(String.valueOf(message.getChatId()), user, new ArrayList<>(), null);

        if (message.hasText()) {
            handleText(message.getText(), user, message);
        } else if (message.hasContact()) {
            handleContact(message.getContact(), message, user);
        } else if (message.hasLocation()) {
            handleLocation(message.getLocation(), message, user);
        } else {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            sendMessage.setChatId("Siz Sizga mumkun bolgan Servicelardan foydalanishingizni soraymiz!");
            ComponentContainer.bot.sendMsg(sendMessage);
        }
    }

    private void handleLocation(Location location, Message message, User user) {

    }

    private void handleContact(Contact contact, Message message, User user) {
        UserKeybordUtil userKeybordUtil = UserKeybordUtil.getInstance();
        userr.setContact(contact);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("Sizning cantactiz olindi");
        sendMessage.setReplyMarkup(userKeybordUtil.basicMenuKeyboard());
        //todo :  zakaz olish vaqtiga qarab
    }

    private void handleText(String text, User user, Message message) {
       UserKeybordUtil userKeybordUtil =UserKeybordUtil.getInstance();
       UserService userService=UserService.getInstance();

        String chatId = String.valueOf(message.getChatId());


        SendMessage sendMessage;


        if (text.equals("/start")) {
            sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Sizni Issiqina Suvli Qanotchalar Kutib Turipti");
            sendMessage.setReplyMarkup(userKeybordUtil.basicMenuKeyboard());
            ComponentContainer.bot.sendMsg(sendMessage);
        } else if (text.equals(UserKeyboardConstants.MENU)) {
            sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Bugungi menu:");
            sendMessage.setReplyMarkup(UserInlineKeybordUtil.getMenuInlineMarkup(DataStore.UserMenuList));
            ComponentContainer.bot.sendMsg(sendMessage);
        } else if (text.equals(UserKeyboardConstants.CONTACT)) {
            sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Shikoyat yoki takliflaringizni yozib qoldiring men uni Tegishli joyga yetkazaman!");
            if (DataStore.states.containsKey(chatId)) {
                DataStore.states.remove(chatId);
                DataStore.states.put(chatId, State.FEEDBACKWAITING);
            } else {
                DataStore.states.put(chatId, State.FEEDBACKWAITING);
            }
            ComponentContainer.bot.sendMsg(sendMessage);
        } else if (text.equals(UserKeyboardConstants.SAVAT)) {
            if (!userr.getBasket().isEmpty()) {
                sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                userService.showBasket(sendMessage, userr.getBasket());
            } else {
                sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Sizning Savatingiz bosh");
                ComponentContainer.bot.sendMsg(sendMessage);
            }
        } else if (text.equals(UserKeyboardConstants.SEARCH)) {
            sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            ComponentContainer.lastMessage = "Nima qidirmoqdasiz?";
            sendMessage.setText(ComponentContainer.lastMessage);
            if (!DataStore.states.containsKey(chatId)) {
                DataStore.states.put(chatId, State.SEARCHING_TEXT_WAITING);
            } else {
                DataStore.states.remove(chatId);
                DataStore.states.put(chatId, State.SEARCHING_TEXT_WAITING);
            }
            ComponentContainer.bot.sendMsg(sendMessage);
        } else if (DataStore.states.containsKey(chatId)) {
            if (DataStore.states.get(chatId).equals(State.FEEDBACKWAITING)) {
                for (String chatIdd : ComponentContainer.AdminsList) {
                    sendMessage = new SendMessage();
                    sendMessage.setChatId(chatIdd);
                    sendMessage.setText(text + "\n kimdan : " + user.getFirstName());
                    ComponentContainer.bot.sendMsg(sendMessage);
                }
                sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Sizning habaringiz adminga muvofaqiyatli yetqazildi!");
                sendMessage.setReplyMarkup(userKeybordUtil.basicMenuKeyboard());

                DataStore.states.remove(chatId);
                ComponentContainer.bot.sendMsg(sendMessage);
            } else if (DataStore.states.get(chatId).equals(State.SEARCHING_TEXT_WAITING)) {
                Product product = userService.findProduct(text);
                sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                if (product != null) {
                    sendMessage.setText("Sizning " + text + " boyicha qidiruv natijasi :");
                    sendMessage.setReplyMarkup(UserInlineKeybordUtil.getProductInlineKeyBord(product));
                } else {
                    sendMessage.setText("Sizning " + text + " qidiruvingiz boyicha hechnima topilmadi!");
                    sendMessage.setReplyMarkup(userKeybordUtil.basicMenuKeyboard());
                }

                DataStore.states.remove(chatId);
                ComponentContainer.bot.sendMsg(sendMessage);

            }
        }

    }

    public void handleCallBackQuery(Message message1, CallbackQuery message) {


        UserInlineKeybordUtil userInlineKeybordUtil =UserInlineKeybordUtil.getInstance();
        UserService userService=UserService.getInstance();

        String chatId = String.valueOf(message.getMessage().getChatId());

        String data = message.getData();

        SendMessage sendMessage;

        SendPhoto sendPhoto;

        if (data.equals("\uD83C\uDF7FBasketlar")) {
            sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("\uD83C\uDF7FBasketlar");
            sendMessage.setReplyMarkup(userInlineKeybordUtil.getListOfProducts(sendMessage, Type.BASKETLAR));
            ComponentContainer.bot.sendMsg(sendMessage);
        } else if (data.equals("\uD83C\uDF54Burger")) {
            sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("\uD83C\uDF54Burger");
            sendMessage.setReplyMarkup(userInlineKeybordUtil.getListOfProducts(sendMessage, Type.BURGER));
            ComponentContainer.bot.sendMsg(sendMessage);
        } else if (data.equals("\uD83C\uDF57 Tovuq")) {
            sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("\uD83C\uDF57 Tovuq");
            sendMessage.setReplyMarkup(userInlineKeybordUtil.getListOfProducts(sendMessage, Type.TOVUQ));
            ComponentContainer.bot.sendMsg(sendMessage);
        } else if (data.equals("\uD83C\uDF5FSneyklar")) {
            sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("\uD83C\uDF5FSneyklar");
            sendMessage.setReplyMarkup(userInlineKeybordUtil.getListOfProducts(sendMessage, Type.SNEYKLAR));
            ComponentContainer.bot.sendMsg(sendMessage);
        } else if (data.equals("\uD83C\uDF2FTwisterlar")) {
            sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("\uD83C\uDF2FTwisterlar");
            sendMessage.setReplyMarkup(userInlineKeybordUtil.getListOfProducts(sendMessage, Type.TWISTERLAR));
            ComponentContainer.bot.sendMsg(sendMessage);
        } else if (data.equals("\uD83E\uDDC1Shrinliklar")) {
            sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("\uD83E\uDDC1Shrinliklar");
            sendMessage.setReplyMarkup(userInlineKeybordUtil.getListOfProducts(sendMessage, Type.SHRINLIKLAR));
            ComponentContainer.bot.sendMsg(sendMessage);
        } else if (data.equals("\uD83E\uDDCBIchimliklar")) {
            sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("\uD83E\uDDCBIchimliklar");
            sendMessage.setReplyMarkup(userInlineKeybordUtil.getListOfProducts(sendMessage, Type.ICHIMLIKLAR));
            ComponentContainer.bot.sendMsg(sendMessage);
        } else if (data.equals("\uD83E\uDD6BSouslar")) {
            sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("\uD83E\uDD6BSouslar");
            sendMessage.setReplyMarkup(userInlineKeybordUtil.getListOfProducts(sendMessage, Type.SOUSLAR));
            ComponentContainer.bot.sendMsg(sendMessage);
        } else if (data.equals("Ortga->")) {
            sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Bugungi Menu :");
            sendMessage.setReplyMarkup(UserInlineKeybordUtil.getMenuInlineMarkup(DataStore.UserMenuList));
            ComponentContainer.bot.sendMsg(sendMessage);
        } else if (userService.findProduct(data) != null) {
            Product product = userService.findProduct(data);
            sendPhoto = new SendPhoto();
            sendPhoto.setChatId(chatId);
            userService.showProduct(sendPhoto, product);
        }
        DeleteMessage deleteMessage = new DeleteMessage(chatId, message1.getMessageId());
        ComponentContainer.bot.sendMsg(deleteMessage);
    }


    public static UserController getInstance() {
        if (instance == null) {
            synchronized (UserController.class) {
                if (instance == null)
                    instance = new UserController();
            }
        }
        return instance;
    }
}
