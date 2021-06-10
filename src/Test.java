import entity.Category;
import entity.Option;
import entity.Product;
import entity.Value;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Test {
    public static void main(String[] args) throws IOException {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("ID товара:");
        Long productId = Long.parseLong(reader.readLine());
        Product product = manager.find(Product.class, productId);

        System.out.println("Название товара" + "[" + product.getName() + "]");
        String name = reader.readLine();
        if (!name.isEmpty()) {
            product.setName(name);
        }

        System.out.println("Цена товара" + "[" + product.getPrice() + "]");
        String price = reader.readLine();
        if (!price.isEmpty()) {
            product.setPrice(Double.parseDouble(price));
        }

        try {
            manager.getTransaction().begin();

            manager.persist(product);

            for (Option option : product.getCategory().getOptions()) {
                TypedQuery<Value> query = manager.createQuery(
                        "select v from Value v where v.product = ?1 and v.option = ?2",
                        Value.class
                );
                query.setParameter(1, product);
                query.setParameter(2, option);
                List<Value> result = query.getResultList();

                if (result.size() > 0) {
                    Value value = result.get(0);
                    System.out.println(option.getName() + "[" + value.getValue() + "]");
                    String pr = reader.readLine();
                    if (!pr.isEmpty()) {
                        value.setValue(pr);
                    }
                    manager.persist(value);
                } else {
                    System.out.println(option.getName() + "[" + "]");
                    Value value = new Value();
                    String pr = reader.readLine();
                    value.setOption(option);
                    value.setValue(pr);
                    value.setProduct(product);
                    manager.persist(value);
                }

            }

            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
        }
    }
}