import entity.Product;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Filter {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();

        Long categoryId = 1L;
        String name = "Lenovo";
        Double maxPrice = 27000D;
        Double minPrice = 1000D;

        String query = "select p from Product p";

        Map<Long, String> options = new HashMap<>();
        options.put(1L, "16");
        options.put(2L, "1920x1200");

        int z = 0;
        for (Long optionId : options.keySet()) {
            query += " inner join Value v" + z + " on v" + z + ".product.id = p.id";
            query += " and v" + z + ".option.id = " + optionId;
            query += " and v" + z + ".value = " + "'" + options.get(optionId) + "'";
            z++;
        }

        List<String> expression = new ArrayList<>();

        if (categoryId != null) {
            expression.add("p.category.id = " + categoryId);
        }

        if (name != null) {
            expression.add("p.name like '%" + name + "%'");
        }

        if (maxPrice != null) {
            expression.add("p.price < " + maxPrice);
        }

        if (minPrice != null) {
            expression.add("p.price > " + minPrice);
        }

        if (expression.size() > 0) {
            query += " where";

            for (int i = 0; i < expression.size(); i++) {
                query += "  " + expression.get(i);
                if (i < expression.size() - 1) {
                    query += " and";
                }
            }
        }

        List<Product> product = manager.createQuery(query, Product.class).getResultList();

        for (Product product1 : product) {
            System.out.println(product1.getName());
        }
    }
}