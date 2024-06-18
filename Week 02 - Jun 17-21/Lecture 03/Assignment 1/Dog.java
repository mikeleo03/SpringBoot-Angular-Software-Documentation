public class Dog {
    // States, which defined as the attribute
    private String color;
    private String name;
    private String breed;

    // Constructor
    public Dog(String color, String name, String breed) {
        this.color = color;
        this.name = name;
        this.breed = breed;
    }

    // Getter and Setter
    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    // Doing the behavior, using print
    public void wagTail() {
        System.out.println(name + " is wagging its tail.");
    }

    public void bark() {
        System.out.println(name + " is barking.");
    }

    public void eat() {
        System.out.println(name + " is eating.");
    }

    // Main method for testing
    public static void main(String[] args) {
        Dog dog = new Dog("Brown", "Buddy", "Golden Retriever");
        dog.wagTail();
        dog.bark();
        dog.eat();
    }
}