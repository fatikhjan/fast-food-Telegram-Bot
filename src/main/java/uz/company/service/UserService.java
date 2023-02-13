package uz.company.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import uz.company.container.ComponentContainer;
import uz.company.container.ThreadSafeBeanContext;
import uz.company.db.DataStore;
import uz.company.model.Product;
import uz.company.util.UserInlineKeybordUtil;

import java.io.File;
import java.util.List;

public class UserService {
    static String path = "D:\\projects\\For_clients\\kfc_bot\\kfc_bot\\kfc_bot\\src\\main\\resources";

    public static String countbasket(List<Product> basket) {
        double summa = 0;
        for (Product product : basket) {
            Double aDouble = Double.valueOf(product.getPrice());
            aDouble += summa;
        }
        return String.valueOf(summa);
    }

    public void showBasket(SendMessage sendMessage, List<Product> basket) {
        ReplyKeyboard menuInlineMarkup = UserInlineKeybordUtil.getMenuInlineMarkup(basket);
        sendMessage.setText("Mana sizning Savatingiz : ");
        sendMessage.setReplyMarkup(menuInlineMarkup);
        ComponentContainer.bot.sendMsg(sendMessage);
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
        UserInlineKeybordUtil userInlineKeybordUtil = ThreadSafeBeanContext.USER_INLINE_KEYBORD_UTIL_THREAD_LOCAL.get();
        sendPhoto.setCaption(product.getName() + "\n" + product.getDescription());
        sendPhoto.setReplyMarkup(userInlineKeybordUtil.getButtonForProduct());

        File file = new File(path, product.getPhotoUrl());
        file.mkdirs();
        sendPhoto.setPhoto(new InputFile(file));
        ComponentContainer.bot.sendMsg(sendPhoto);
    }



}
