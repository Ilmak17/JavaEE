import entity.Product;
import entity.Value;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Delete {
    public static void main(String[] args) throws IOException {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("ID товара");
        Long productId = Long.parseLong(reader.readLine());
        Product product = manager.find(Product.class, productId);
        try {
            manager.getTransaction().begin();

            manager.remove(product);

            for (Value value : product.getValues()) {
                manager.remove(value);
            }

            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
        }
    }
}
