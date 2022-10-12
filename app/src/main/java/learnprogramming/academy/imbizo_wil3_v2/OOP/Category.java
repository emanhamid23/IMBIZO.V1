package learnprogramming.academy.imbizo_wil3_v2.OOP;

import java.util.Comparator;

public class Category {

    String name;
    String image;
    String username;
//    List<CategoryItem> Item;


    public Category() {

    }

    public Category(String name, String image, String username) {
        this.name = name;
        this.image = image;
        this.username = username;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public static Comparator<Category> StuNameComparatorAsc = new Comparator<Category>() {

        public int compare(Category c1, Category c2) {
            String CategoryName1 = c1.getName().toUpperCase();
            String CategoryName2 = c2.getName().toUpperCase();

            //ascending order
            return CategoryName1.compareTo(CategoryName2);


        }};

    public static Comparator<Category> StuNameComparatorDesc = new Comparator<Category>() {

        public int compare(Category c1, Category c2) {
            String CategoryName1 = c1.getName().toUpperCase();
            String CategoryName2 = c2.getName().toUpperCase();



            //descending order
            return CategoryName2.compareTo(CategoryName1);
        }};

}
