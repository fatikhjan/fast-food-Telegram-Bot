package uz.company.controller;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import uz.company.constants.UserKeyboardConstants;
import uz.company.container.ComponentContainer;
import uz.company.container.ThreadSafeBeanContext;
import uz.company.db.DataStore;
import uz.company.db.State;
import uz.company.model.Product;
import uz.company.model.enums.Type;
import uz.company.service.UserService;
import uz.company.util.UserInlineKeybordUtil;
import uz.company.util.UserKeybordUtil;

import java.util.concurrent.ConcurrentHashMap;

public class UserController {

    static String chatId;
    static org.telegram.telegrambots.meta.api.objects.User user;
    public static uz.company.model.User userr = new uz.company.model.User(chatId, user, new ConcurrentHashMap<>(), null);


    public void handleMessage(Message message, User user) {

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
        UserKeybordUtil userKeybordUtil = ThreadSafeBeanContext.USER_KEYBORD_UTIL_THREAD_LOCAL.get();
        userr.setContact(contact);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("Sizning cantactiz olindi");
        sendMessage.setReplyMarkup(userKeybordUtil.basicMenuKeyboard());
        //todo :  zakaz olish vaqtiga qarab
    }

    private void handleText(String text, User user, Message message) {
        UserKeybordUtil userKeybordUtil = ThreadSafeBeanContext.USER_KEYBORD_UTIL_THREAD_LOCAL.get();
        UserInlineKeybordUtil userInlineKeybordUtil = ThreadSafeBeanContext.USER_INLINE_KEYBORD_UTIL_THREAD_LOCAL.get();
        UserService userService = ThreadSafeBeanContext.USER_SERVICE_THREAD_LOCAL.get();
        DataStore dataStore = ThreadSafeBeanContext.DATA_STORE_THREAD_LOCAL.get();
        chatId = String.valueOf(message.getChatId());
        user = user;


        SendMessage sendMessage;
        SendPhoto sendPhoto;


        if (text.equals("/start")) {
            sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("горячей Крылья ждут вас");
            sendMessage.setReplyMarkup(userKeybordUtil.basicMenuKeyboard());
            ComponentContainer.bot.sendMsg(sendMessage);
        } else if (text.equals(UserKeyboardConstants.MENU)) {
            DataStore.init();
            sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Сегодняшнее меню:");
            sendMessage.setReplyMarkup(userInlineKeybordUtil.getMenuInlineMarkup(DataStore.userMenuList));
            ComponentContainer.bot.sendMsg(sendMessage);
        } else if (text.equals(UserKeyboardConstants.CONTACT)) {
            sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Напишите свои жалобы или предложения, и я отнесу их в соответствующее место!");
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
                sendMessage.setText("Ваша корзина пуста!");
                ComponentContainer.bot.sendMsg(sendMessage);
            }
        } else if (text.equals(UserKeyboardConstants.SEARCH)) {
            sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Что Вы ищете?");
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
                sendMessage.setText("Ваше сообщение успешно доставлено администратору!");
                sendMessage.setReplyMarkup(userKeybordUtil.basicMenuKeyboard());

                DataStore.states.remove(chatId);
                ComponentContainer.bot.sendMsg(sendMessage);
            } else if (DataStore.states.get(chatId).equals(State.SEARCHING_TEXT_WAITING)) {
                sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);

                Product product = userService.findProduct(text);
                sendPhoto = new SendPhoto();
                sendPhoto.setChatId(chatId);
                if (product != null) {
                    userService.showProduct(sendPhoto, product);
                } else {
                    sendMessage.setText("По вашему " + text + " запросу ничего не найдено!");
                    sendMessage.setReplyMarkup(userKeybordUtil.basicMenuKeyboard());
                    ComponentContainer.bot.sendMsg(sendMessage);
                }

                DataStore.states.remove(chatId);

            } else if (DataStore.states.get(chatId).equals(State.EDITING_PRODUCT_AMOUNT)) {
                if (DataStore.amountProduct.get(chatId) != null) {
                    Product product = DataStore.amountProduct.get(chatId);
                    try {
                        int amoutn = userService.checkStrIsNumber(text);
                        userr.getBasket().remove(product);
                        userr.getBasket().put(product, amoutn);
                        sendMessage = new SendMessage();
                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Количество товаров увеличено на введенное количество!\n" + "  Посмотреть корзину!");
                        sendMessage.setReplyMarkup(userKeybordUtil.basicMenuKeyboard());
                        DataStore.states.remove(chatId);
                        DataStore.amountProduct.remove(chatId);
                        ComponentContainer.bot.sendMsg(sendMessage);
                    } catch (NumberFormatException ignored) {
                        sendMessage = new SendMessage();
                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Произошла ошибка при вводе номера, пожалуйста, введите еще раз:");
                        ComponentContainer.bot.sendMsg(sendMessage);
                    }
                }
            }
        }

    }

    public void handleCallBackQuery(Message message1, CallbackQuery message) {
        UserInlineKeybordUtil userInlineKeybordUtil = ThreadSafeBeanContext.USER_INLINE_KEYBORD_UTIL_THREAD_LOCAL.get();
        UserKeybordUtil userKeybordUtil = ThreadSafeBeanContext.USER_KEYBORD_UTIL_THREAD_LOCAL.get();
        UserService userService = ThreadSafeBeanContext.USER_SERVICE_THREAD_LOCAL.get();
        DataStore dataStore = ThreadSafeBeanContext.DATA_STORE_THREAD_LOCAL.get();
        chatId = String.valueOf(message.getMessage().getChatId());
        user = message1.getFrom();

        String data = message.getData();

        SendMessage sendMessage;

        SendPhoto sendPhoto;

        if (data.equals("Ortga->") || data.equals("_back")) {
            DataStore.init();
            sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Сегодняшнее меню:");
            sendMessage.setReplyMarkup(userInlineKeybordUtil.getMenuInlineMarkup(DataStore.userMenuList));
            ComponentContainer.bot.sendMsg(sendMessage);
        } else if (userService.findProduct(data) != null) {
            Product product = userService.findProduct(data);
            sendPhoto = new SendPhoto();
            sendPhoto.setChatId(chatId);
            userService.showProduct(sendPhoto, product);
        } else if (data.startsWith("toBascket")) {
            String s = "";
            char[] chars = data.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == '=') {
                    s = data.substring(i + 1, data.length());
                    break;
                }
            }
            Product product = userService.addToBasket(userr, Integer.valueOf(s), 1);
            sendPhoto = new SendPhoto();
            sendPhoto.setChatId(chatId);
            userService.showProduct(sendPhoto, product);
        } else if (data.equals("editbascet")) {
            if (DataStore.states.get(chatId) != null) DataStore.states.remove(chatId);
            DataStore.states.put(chatId, State.EDITING_BASKET);
            sendPhoto = new SendPhoto();
            sendPhoto.setChatId(chatId);
            userService.editBasket(sendPhoto,0);

            } else if (data.startsWith("_editProductNumber")) {
            char[] chars = data.toCharArray();
            int id = 0;
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == '=') {
                    id = Integer.parseInt(data.substring(i + 1));
                }
            }
            Product product = userService.getProductFromId(id);
            DataStore.amountProduct.put(chatId, product);
            DataStore.states.put(chatId, State.EDITING_PRODUCT_AMOUNT);
            sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Сколько из этих продуктов вы хотели бы заказать? (максимум = 20)");
            ComponentContainer.bot.sendMsg(sendMessage);
        } else {
            Type type = userService.getTypeFromMap(data);
            if (type != null) {
                sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText(data);
                sendMessage.setReplyMarkup(userInlineKeybordUtil.getListOfProducts(sendMessage, type));
                ComponentContainer.bot.sendMsg(sendMessage);
            }

            if (DataStore.states.containsKey(chatId)) {
                if (DataStore.states.get(chatId).equals(State.EDITING_BASKET)) {
                    sendPhoto = new SendPhoto();
                    sendPhoto.setChatId(chatId);
                    userService.editBasket(sendPhoto, data);
                }
            }
        }
        if (!data.equals("_")) {
            DeleteMessage deleteMessage = new DeleteMessage(chatId, message1.getMessageId());
            ComponentContainer.bot.sendMsg(deleteMessage);
        }
    }

}
