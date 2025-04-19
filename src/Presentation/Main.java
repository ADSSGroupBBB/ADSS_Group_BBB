package Presentation;
import static Domain.UserController.initBaseData;
import static Presentation.UserIO.presentingMenu;

public class Main {
    public static void main(String[] args) {
        initBaseData();
        presentingMenu();

    }
}