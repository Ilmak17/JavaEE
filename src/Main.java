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

public class Main {
    public static void main(String[] args) throws IOException {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();
        // Category category = new Category();
        // category.setName("Материнские платы");
        // Category category = manager.find(Category.class, 2L);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        TypedQuery<Category> query = manager.createQuery("select c from Category c", Category.class);
        List<Category> categories = query.getResultList();

        for (Category category : categories) {
            System.out.println(category.getName() + "[" + category.getId() + "]");
        }

        String categoryId = reader.readLine();
        Category category = manager.find(Category.class, Long.parseLong(categoryId));
        System.out.println("Введите название");
        String name = reader.readLine();
        System.out.println("Введите описание");
        String desc = reader.readLine();
        System.out.println("Введите цену");
        String price = reader.readLine();


        Product product = new Product();

        product.setCategory(category);

        product.setName(name);
        product.setDescription(desc);
        product.setPrice(Double.parseDouble(price));

        try {
            manager.getTransaction().begin();
            manager.persist(category);

            for (Option option : category.getOptions()) {
                System.out.println(option.getName() + ": ");
                String strValue = reader.readLine();
                Value v = new Value();

                v.setOption(option);
                v.setProduct(product);
                v.setValue(strValue);
                manager.persist(v);
            }

            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
        }

        manager.close();
    }
}