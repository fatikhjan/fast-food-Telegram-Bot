package uz.company.mappers;

import uz.company.dto.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UserThingsMapper {

    public List<Product> basketToList(ConcurrentHashMap<uz.company.model.Product, Integer> basket) {
        List<Product> productList = new ArrayList<>();
        AtomicInteger id = new AtomicInteger();
        basket.forEach((product1, integer) -> {
            double sum = (Double) Double.parseDouble(product1.getPrice()) * integer;
            productList.add(new Product(id.incrementAndGet(), product1, integer, sum));
        });
        return productList;
    }
}
