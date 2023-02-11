package uz.company.db;

import uz.company.model.Product;
import uz.company.model.enums.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {

    private static DataStore instance;
    public static final List<String> UserMenuList = new ArrayList<>(Arrays.asList(
            "\uD83C\uDF7FBasketlar",
            "\uD83C\uDF54Burger",
            "\uD83C\uDF57 Tovuq",
            "\uD83C\uDF5FSneyklar",
            "\uD83C\uDF2FTwisterlar",
            "\uD83E\uDDC1Shrinliklar",
            "\uD83E\uDDCBIchimliklar",
            "\uD83E\uDD6BSouslar"));
    public static List<Product> productList = new ArrayList<>(Arrays.asList(
            new Product(1, "Frend's Box Assorti", "productphotos\\img.png",
                    "Har bir Boxning ichida : 5 ta tovuq oyoqchalari," +
                            "10 ta Qarsildoqqanotchalar," +
                            "5 ta striptlar," +
                            "270 gr Hushtam Baytslar va idish tola Freelar!" +
                            "\n Bunday Box lar albatta Hamma bilan Shrin Baham korish uchun!",
                    "215000.00", 0,
                    Type.BASKETLAR),
            new Product(2, "Sander's Basketi ", "img.png",
                    " 1 tovuq oyoqchasi,2 ta Ot'kir Qanotchalar,2 ta Stripslar va 3 ta O'tkir Baytslar",
                    "50000.00", 0,
                    Type.BASKETLAR)));
    public static ConcurrentHashMap<String, State> states = new ConcurrentHashMap<>();


    public static DataStore getInstance() {
        if (instance == null) {
            synchronized (DataStore.class) {
                if (instance == null)
                    instance = new DataStore();
            }
        }
        return instance;
    }
}
