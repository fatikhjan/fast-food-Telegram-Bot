package uz.company.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.company.model.enums.Type;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    private Integer id;
    private String name;
    private String photoUrl;
    private String description;
    private String price;
    private int likes = 0;
    private Type type;
}
