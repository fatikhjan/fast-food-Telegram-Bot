package uz.company.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
import java.util.List;

public class AdminInlineKeyboards {

    public ReplyKeyboard getAdminMenu() {
        KeyboardButton addAds = new KeyboardButton("Menyu qoshish");
        KeyboardButton etitAds = new KeyboardButton("Menyuni ozgartirish");


        KeyboardRow row1 = new KeyboardRow(List.of(addAds));
        KeyboardRow row2 = new KeyboardRow(List.of(etitAds));

        List<KeyboardRow> rows = Arrays.asList(row1, row2);

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(rows);
        markup.setSelective(true);
        markup.setResizeKeyboard(true);
        return markup;
    }


}
