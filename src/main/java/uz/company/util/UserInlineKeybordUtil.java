package uz.company.util;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.company.constants.UserKeyboardConstants;
import uz.company.container.ThreadSafeBeanContext;
import uz.company.controller.UserController;
import uz.company.db.DataStore;
import uz.company.dto.Product;
import uz.company.model.enums.Type;
import uz.company.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserInlineKeybordUtil {


    public ReplyKeyboard getMenuInlineMarkup(List list) {


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

    public static ReplyKeyboard getProductInlineKeyBord(uz.company.model.Product product) {
        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();

        InlineKeyboardButton button = new InlineKeyboardButton(product.getName() + " " + product.getPrice() + " uzs");
        inlineKeyboardButtons.add(button);

        List<List<InlineKeyboardButton>> inline = new ArrayList<>(Arrays.
                asList(inlineKeyboardButtons));
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(inline);
        return markup;
    }

    public ReplyKeyboard getListOfProducts(SendMessage sendMessage, Type type) {
        List<List<InlineKeyboardButton>> inlineKeybordList = new ArrayList<>();

        for (uz.company.model.Product product : DataStore.productList) {

            if (product.getType().equals(type)) {
                InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(product.getName() + " " + product.getPrice() + " uzs");
                inlineKeyboardButton.setCallbackData(product.getName());
                List<InlineKeyboardButton> buttons = new ArrayList<>();
                buttons.add(inlineKeyboardButton);
                inlineKeybordList.add(buttons);
            }
        }

        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton(UserKeyboardConstants.BACK);
        button.setCallbackData("Ortga->");
        inlineKeyboardButtons.add(button);
        inlineKeybordList.add(inlineKeyboardButtons);

        if (inlineKeybordList.isEmpty()) {
            sendMessage.setText("Этот продукт в данный момент недоступен");
            return getOneButton("Back->");
        }
        return new InlineKeyboardMarkup(inlineKeybordList);
    }

    public static InlineKeyboardMarkup getOneButton(String str) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(str);
        inlineKeyboardButton.setCallbackData(str);
        List<List<InlineKeyboardButton>> inlineKeybordList = new ArrayList<>(List.of(List.of(inlineKeyboardButton)));
        return new InlineKeyboardMarkup(inlineKeybordList);
    }

    public ReplyKeyboard getButtonForProduct(String s) {


        InlineKeyboardButton toBascet = new InlineKeyboardButton(UserKeyboardConstants.TO_BASKET);
        toBascet.setCallbackData("toBascket=" + s);
        InlineKeyboardButton like = new InlineKeyboardButton(UserKeyboardConstants.LIKE);
        like.setCallbackData("like");
        InlineKeyboardButton bascet = new InlineKeyboardButton(UserKeyboardConstants.GETBASCET);
        bascet.setCallbackData("getBasket");
        InlineKeyboardButton back = new InlineKeyboardButton(UserKeyboardConstants.BACK);
        back.setCallbackData("_back");
        List<InlineKeyboardButton> row1 = new ArrayList<>(List.of(toBascet));
        List<InlineKeyboardButton> row2 = new ArrayList<>(Arrays.asList(like, bascet));
        List<InlineKeyboardButton> row3 = new ArrayList<>(List.of(back));

        return new InlineKeyboardMarkup(Arrays.asList(row1, row2, row3));

    }


    public ReplyKeyboard getBasket() {
        InlineKeyboardButton button1 = new InlineKeyboardButton(UserKeyboardConstants.EDIT_BASKET);
        button1.setCallbackData("editbascet");
        InlineKeyboardButton button2 = new InlineKeyboardButton(UserKeyboardConstants.CLEARBASKET);
        button2.setCallbackData("clearBascet");
        InlineKeyboardButton button3 = new InlineKeyboardButton(UserKeyboardConstants.MakeOreder);
        button3.setCallbackData("_makeOrder");

        List<InlineKeyboardButton> buttons = new ArrayList<>(Arrays.asList(button1, button2));
        List<InlineKeyboardButton> buttons1 = new ArrayList<>(List.of(button3));
        return new InlineKeyboardMarkup(Arrays.asList(buttons, buttons1));
    }

    public ReplyKeyboard getButtonForOrderedProduct(uz.company.model.Product product) {
        UserService userService = ThreadSafeBeanContext.USER_SERVICE_THREAD_LOCAL.get();

        InlineKeyboardButton button = new InlineKeyboardButton("-1");
        button.setCallbackData("-" + product.getId());
        Integer integer = UserController.userr.getBasket().get(product);
        InlineKeyboardButton button2 = new InlineKeyboardButton("✏ " + String.valueOf(integer) + "dona");
        button2.setCallbackData("_editProductNumber=" + product.getId());
        InlineKeyboardButton button3 = new InlineKeyboardButton("1+");
        button3.setCallbackData("+" + product.getId());
        InlineKeyboardButton button4 = new InlineKeyboardButton("❤");
        button4.setCallbackData("_like=" + product.getId());
        InlineKeyboardButton button5 = new InlineKeyboardButton("\uD83D\uDDD1 " +
                userService.countBascetPrice(UserController.userr.getBasket()) +
                " uzs");
        button5.setCallbackData("_showBascet");
        InlineKeyboardButton button6 = new InlineKeyboardButton(UserKeyboardConstants.BACK);
        button6.setCallbackData("_back");

        List<InlineKeyboardButton> row1 = new ArrayList<>(
                Arrays.asList(button, button2, button3)
        );
        List<InlineKeyboardButton> row2 = new ArrayList<>(
                Arrays.asList(button4, button5)
        );
        List<InlineKeyboardButton> row3 = new ArrayList<>(
                Arrays.asList(button6)
        );
        return new InlineKeyboardMarkup(Arrays.asList(row1, row2, row3));
    }


    public ReplyKeyboard getButtonForEditingBascet(Product product, int index, int listSize, boolean b) {

        if (!b) {
            InlineKeyboardButton button1 = new InlineKeyboardButton("✏" + product.getProductAmount()
                    + "шт. |" + product.getProduct().getPrice() + "UZS " + product.getProduct().getName());
            button1.setCallbackData("_editBasketProductAmount");

            InlineKeyboardButton button2 = new InlineKeyboardButton("⬅");
            if (index == 0) {
                button2.setCallbackData("_");
            } else button2.setCallbackData("_last=" + index);

            InlineKeyboardButton button3 = new InlineKeyboardButton(index+1 + "/" + listSize);
            button3.setCallbackData("_");
            InlineKeyboardButton button4 = new InlineKeyboardButton("➡");
            if ((index + 1) == listSize) {
                button4.setCallbackData("_");
            } else button4.setCallbackData("_next=" + index);
            InlineKeyboardButton button5 = new InlineKeyboardButton("✅закончить редактирование!");
            button5.setCallbackData("_stopEditingBasket");

            List<InlineKeyboardButton> row1 = List.of(button1);
            List<InlineKeyboardButton> row2 = List.of(button2, button3, button4);
            List<InlineKeyboardButton> row3 = List.of(button5);


            return new InlineKeyboardMarkup(List.of(row1, row2, row3));
        } else {

            InlineKeyboardButton button = new InlineKeyboardButton("-1");
            button.setCallbackData("+1");
            InlineKeyboardButton button0 = new InlineKeyboardButton("✏" + product.getProductAmount() + "шт.");
            button0.setCallbackData("_editProductNumber=" + product.getProduct().getId());
            InlineKeyboardButton button1 = new InlineKeyboardButton("+1");
            button1.setCallbackData("-1");
            InlineKeyboardButton button2 = new InlineKeyboardButton("⬅");
            button2.setCallbackData("_last=" + index);
            InlineKeyboardButton button3 = new InlineKeyboardButton(index+1 + "/" + listSize);
            button3.setCallbackData("_");
            InlineKeyboardButton button4 = new InlineKeyboardButton("➡");
            button4.setCallbackData("_next=" + index);
            InlineKeyboardButton button5 = new InlineKeyboardButton("✅закончить редактирование!");
            button5.setCallbackData("_stopEditingBasket");

            List<InlineKeyboardButton> row1 = List.of(button, button0, button1);
            List<InlineKeyboardButton> row2 = List.of(button2, button3, button4);
            List<InlineKeyboardButton> row3 = List.of(button5);

            return new InlineKeyboardMarkup(List.of(row1, row2, row3));
        }

    }
}