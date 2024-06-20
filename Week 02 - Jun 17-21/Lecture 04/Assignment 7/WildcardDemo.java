class Box<T> {
    private T item;

    public Box(T item) {
        this.item = item;
    }

    public T getItem() {
        return item;
    }
}

public class WildcardDemo {
    public static void printBox(Box<?> box) {
        System.out.println("Box contains: " + box.getItem());
    }

    public static void main(String[] args) {
        Box<Integer> intBox = new Box<>(123);
        Box<String> strBox = new Box<>("Hello");

        printBox(intBox); // Output: Box contains: 123
        printBox(strBox); // Output: Box contains: Hello
    }
}