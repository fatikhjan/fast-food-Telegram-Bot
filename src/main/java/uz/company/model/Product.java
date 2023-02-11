package uz.company.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.company.model.enums.Type;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    private static Product instance;
    private Integer id;
    private String name;
    private String photoUrl;
    private String description;
    private String price;
    private int likes = 0;
    private Type type;


    public static Product getInstance() {
        if (instance == null) {
            synchronized (Product.class) {
                if (instance == null)
                    instance = new Product();
            }
        }
        return instance;
    }
}
