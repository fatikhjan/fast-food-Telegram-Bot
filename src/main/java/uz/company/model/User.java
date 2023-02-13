package uz.company.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Contact;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    private String chatId;
    private org.telegram.telegrambots.meta.api.objects.User user;
    private List<Product> basket = new ArrayList<>();
    private Contact contact;
}
