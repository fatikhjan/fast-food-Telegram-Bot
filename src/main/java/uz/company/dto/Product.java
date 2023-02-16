package uz.company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    private Integer id;
    private uz.company.model.Product product;
    private Integer productAmount;
    private Double amountSum;
}
