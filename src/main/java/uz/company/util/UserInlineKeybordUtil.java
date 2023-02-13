package uz.company.util;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.company.constants.UserKeyboardConstants;
import uz.company.db.DataStore;
import uz.company.model.Product;
import uz.company.model.enums.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserInlineKeybordUtil {



    public static ReplyKeyboard getMenuInlineMarkup(List list) {
        List<List<InlineKeyboardButton>> inlineKeybordList = new ArrayList<>();

        if (list.isEmpty()) {
            return null;
        }

        int s = 0;

        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            InlineKeyboardButton button = new InlineKeyboardButton(String.valueOf(list.get(i)));
            button.setCallbackData(String.valueOf(list.get(i)));
            inlineKeyboardButtons.add(button);
            s++;

            if (s == 2) {
                inlineKeybordList.add(inlineKeyboardButtons);
                inlineKeyboardButtons = new ArrayList<>();
                s = 0;
            }
        }

        if (inlineKeyboardButtons.size() == 1) {
            inlineKeybordList.add(inlineKeyboardButtons);
        }
        return new InlineKeyboardMarkup(inlineKeybordList);
    }

    public static ReplyKeyboard getProductInlineKeyBord(Product product) {
        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();

        InlineKeyboardButton button = new InlineKeyboardButton(product.getName() + " " + product.getPrice() + " uzs");
        inlineKeyboardButtons.add(button);

        List<List<InlineKeyboardButton>> inline = new ArrayList<>(Arrays.asList(inlineKeyboardButtons));
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(inline);
        return markup;
    }

    public ReplyKeyboard getListOfProducts(SendMessage sendMessage, Type type) {

        List<List<InlineKeyboardButton>> inlineKeybordList = new ArrayList<>();

        for (Product product : DataStore.productList) {

            if (product.getType().equals(type)) {
                InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(product.getName() + " " + product.getPrice() + " uzs");
                inlineKeyboardButton.setCallbackData(product.getName());
                List<InlineKeyboardButton> buttons = new ArrayList<>();
                buttons.add(inlineKeyboardButton);
                inlineKeybordList.add(buttons);
            }
        }

        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton("Ortga->");
        button.setCallbackData("Ortga->");
        inlineKeyboardButtons.add(button);
        inlineKeybordList.add(inlineKeyboardButtons);

        if (inlineKeybordList.isEmpty()) {
            sendMessage.setText("Bunday Product hozirda mavjud emas");
            return getOneButton("Ortga->");

        }

        return new InlineKeyboardMarkup(inlineKeybordList);


    }

    public static InlineKeyboardMarkup getOneButton(String str) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(str);
        inlineKeyboardButton.setCallbackData(str);
        List<List<InlineKeyboardButton>> inlineKeybordList = new ArrayList<>(Arrays.asList(Arrays.asList(inlineKeyboardButton)));
        return new InlineKeyboardMarkup(inlineKeybordList);
    }

    public ReplyKeyboard getButtonForProduct() {
        InlineKeyboardButton toBascet = new InlineKeyboardButton(UserKeyboardConstants.TO_BASkET);
        toBascet.setCallbackData("toBascket");
        InlineKeyboardButton like = new InlineKeyboardButton(UserKeyboardConstants.LIKE);
        like.setCallbackData("like");
        InlineKeyboardButton bascet = new InlineKeyboardButton(UserKeyboardConstants.GETBASCET);
        bascet.setCallbackData("getBasket");
        InlineKeyboardButton back = new InlineKeyboardButton(UserKeyboardConstants.BACK);
        back.setCallbackData("_back");

        List<InlineKeyboardButton> row1 = new ArrayList<>(Arrays.asList(toBascet));
        List<InlineKeyboardButton> row2 = new ArrayList<>(Arrays.asList(like, bascet));
        List<InlineKeyboardButton> row3 = new ArrayList<>(Arrays.asList(back));

        return new InlineKeyboardMarkup(Arrays.asList(row1, row2, row3));
    }



}