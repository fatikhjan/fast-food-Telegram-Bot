package uz.company.constants;

import uz.company.controller.UserController;
import uz.company.service.UserService;

public class UserKeyboardConstants {

    private static UserKeyboardConstants instance;
    public static final String MENU = "\uD83C\uDF7DMENU";
    public static final String SAVAT = "\uD83D\uDDD1SAVAT";
    public static final String SEARCH = "\uD83D\uDD0DQIDIRUV";
    public static final String CONTACT = "\uD83D\uDCF1MUROJAT-UCHUN";


    public static final String TO_BASkET = "✅Savatga qoshish )";
    public static final String LIKE = "❤";
    public static final String GETBASCET = "\uD83D\uDDD1 Savat " + UserService.countbasket(UserController.userr.getBasket());
    public static final String BACK = "Orqaga";
}

