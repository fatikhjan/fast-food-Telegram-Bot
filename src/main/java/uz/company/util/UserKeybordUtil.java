package uz.company.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.company.constants.UserKeyboardConstants;

import java.util.Arrays;
import java.util.List;

public class UserKeybordUtil {



    public  ReplyKeyboard basicMenuKeyboard() {
        KeyboardButton menu = new KeyboardButton(UserKeyboardConstants.MENU);
        KeyboardButton savat = new KeyboardButton(UserKeyboardConstants.SAVAT);
        KeyboardButton search = new KeyboardButton(UserKeyboardConstants.SEARCH);
        KeyboardButton contact = new KeyboardButton(UserKeyboardConstants.CONTACT);

        KeyboardRow row1 = new KeyboardRow(Arrays.asList(menu, savat));
        KeyboardRow row2 = new KeyboardRow(Arrays.asList(search, contact));

        List<KeyboardRow> rows = Arrays.asList(row1, row2);

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(rows);
        markup.setSelective(true);
        markup.setResizeKeyboard(true);
        return markup;
    }



}
