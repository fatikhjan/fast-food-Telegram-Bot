package uz.company.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import uz.company.container.ComponentContainer;
import uz.company.container.ThreadSafeBeanContext;
import uz.company.controller.UserController;
import uz.company.db.DataStore;
import uz.company.mappers.UserThingsMapper;
import uz.company.model.Product;
import uz.company.model.User;
import uz.company.model.enums.Type;
import uz.company.util.UserInlineKeybordUtil;

import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class UserService {
    static String path = "D:\\projects\\For_clients\\kfc_bot\\kfc_bot\\kfc_bot\\src\\main\\resources\\productphotos";


    public void showBasket(SendMessage sendMessage, ConcurrentHashMap<Product, Integer> basket) {
        UserInlineKeybordUtil userInlineKeybordUtil = ThreadSafeBeanContext.USER_INLINE_KEYBORD_UTIL_THREAD_LOCAL.get();
        sendMessage.setText(getBasketListAsString(basket));
        sendMessage.setReplyMarkup(userInlineKeybordUtil.getBasket());
        ComponentContainer.bot.sendMsg(sendMessage);
    }

    private String getBasketListAsString(ConcurrentHashMap<Product, Integer> basket) {
        StringBuilder sb = new StringBuilder();
        sb.append("Вот ваша корзина \n ___\n");
        basket.forEach((product, i) -> {
            if (i != 0) {
                sb.append(product.getName() + "\n" + i + "шт * " + product.getPrice() + "uzs = " + (Double.parseDouble(product.getPrice()) * i) + "\n ___\n");
            }

        });
        sb.append("Вот и все= " + countBascetPrice(basket) + " uzs");
        return sb.toString();
    }

    public Product findProduct(String text) {
        for (Product product : DataStore.productList) {
            if (product.getName().equals(text)) {
                return product;
            }
        }
        return null;
    }

    public void showProduct(SendPhoto sendPhoto, Product product) {
        DataStore dataStore = ThreadSafeBeanContext.DATA_STORE_THREAD_LOCAL.get();
        UserInlineKeybordUtil userInlineKeybordUtil = ThreadSafeBeanContext.USER_INLINE_KEYBORD_UTIL_THREAD_LOCAL.get();

        if (!UserController.userr.getBasket().containsKey(product)) {
            sendPhoto.setCaption(product.getName() + "\n" + product.getDescription());
            sendPhoto.setReplyMarkup(userInlineKeybordUtil.getButtonForProduct(product.getId().toString()));
        } else {
            sendPhoto.setCaption(product.getName() + "\n" + product.getDescription());
            sendPhoto.setReplyMarkup(userInlineKeybordUtil.getButtonForOrderedProduct(product));
        }
        File file = new File(path, product.getPhotoUrl());
        file.mkdirs();
        sendPhoto.setPhoto(new InputFile(file));
        ComponentContainer.bot.sendMsg(sendPhoto);
    }


    public Product addToBasket(User userr, Integer valueOf, int i) {
        for (Product product : DataStore.productList) {
            if (product.getId() == valueOf) {
                userr.getBasket().put(product, i);
                return product;
            }
        }
        return null;
    }

    public String countBascetPrice(ConcurrentHashMap<Product, Integer> basket) {
        AtomicReference<Double> a = new AtomicReference<>(0.0);
        basket.forEach((product, i) -> a.updateAndGet(v -> (double) (v + Double.parseDouble(product.getPrice()) * i)));
        return a.toString();
    }

    public Type getTypeFromMap(String data) {
        if (DataStore.inlineDataTypes.get(data) == null) return null;
        return DataStore.inlineDataTypes.get(data);
    }

    public Product getProductFromId(int id) {
        for (Product product : DataStore.productList) {
            if (product.getId().equals(id)) return product;
        }
        return null;
    }

    public int checkStrIsNumber(String text) throws NumberFormatException {
        return Integer.parseInt(text);
    }

    public void editBasket(SendPhoto sendPhoto, int i) {
        UserInlineKeybordUtil userInlineKeybordUtil = ThreadSafeBeanContext.USER_INLINE_KEYBORD_UTIL_THREAD_LOCAL.get();
        UserThingsMapper userThingsMapper = ThreadSafeBeanContext.USER_THINGS_MAPPER_THREAD_LOCAL.get();

        if (UserController.userr.getBasket().size() == 0) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(sendMessage.getChatId());
            sendMessage.setText("Ваша корзина пуста (");
            ComponentContainer.bot.sendMsg(sendMessage);
            return;
        }
        List<uz.company.dto.Product> userBasketList = userThingsMapper.basketToList(UserController.userr.getBasket());
        uz.company.dto.Product product = userBasketList.get(i);

        sendPhoto.setCaption(product.getProduct().getName() + "\n\n" + product.getProductAmount() + "шт * " + product.getProduct().getPrice() + " uzs = " + product.getAmountSum());
        sendPhoto.setReplyMarkup(userInlineKeybordUtil.getButtonForEditingBascet(product, userBasketList.indexOf(product), userBasketList.size(), false));
        File file = new File(path, product.getProduct().getPhotoUrl());
        sendPhoto.setPhoto(new InputFile(file));
        ComponentContainer.bot.sendMsg(sendPhoto);
    }

    public void editBasket(SendPhoto sendPhoto, String data) {
        UserInlineKeybordUtil userInlineKeybordUtil = ThreadSafeBeanContext.USER_INLINE_KEYBORD_UTIL_THREAD_LOCAL.get();
        UserThingsMapper userThingsMapper = ThreadSafeBeanContext.USER_THINGS_MAPPER_THREAD_LOCAL.get();
        List<uz.company.dto.Product> userBasketList = userThingsMapper.basketToList(UserController.userr.getBasket());

        int lastIndex = 0;

        char[] chars = data.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '=') {
                lastIndex = Integer.parseInt(data.substring(i + 1));
            }
        }

        if (data.equals("_editBasketProductAmount")) {
            uz.company.dto.Product product = userBasketList.get(lastIndex);
            sendPhoto.setCaption(product.getProduct().getName() + "\n\n" + product.getProductAmount() + "шт * " + product.getProduct().getPrice() + " uzs = " + product.getAmountSum());
            sendPhoto.setReplyMarkup(userInlineKeybordUtil.getButtonForEditingBascet(product, userBasketList.indexOf(product), userBasketList.size(), true));
            File file = new File(path, product.getProduct().getPhotoUrl());
            sendPhoto.setPhoto(new InputFile(file));
            ComponentContainer.bot.sendMsg(sendPhoto);

        } else if (data.startsWith("_last")) {
            editBasket(sendPhoto, lastIndex - 1);
        } else if (data.startsWith("_next")) {
            editBasket(sendPhoto, lastIndex + 1);
        } else if (data.equals("_stopEditingBasket")) {

        } else if (data.equals("_")) {
        } else if (data.equals("+1")) {

        } else if (data.equals("-1")) {

        }

    }
}
