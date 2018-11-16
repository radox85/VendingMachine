package pl.sdacademy.vending;

import pl.sdacademy.vending.controller.CustomerOperationController;
import pl.sdacademy.vending.model.VendingMachine;
import pl.sdacademy.vending.util.Configuration;

import java.util.Scanner;

public class Main {
    Configuration configuration = new Configuration();
    VendingMachine vendingMachine = new VendingMachine(configuration);
    CustomerOperationController customerOperationController = new CustomerOperationController(vendingMachine);

    private void startApplication() {
        while (true) {
            customerOperationController.printMachine();
            printMenu();
            UserMenuSelection userSelection = getUserSelection();
            switch (userSelection) {
                case BUY_PRODUCT:
                    ///
                    break;
                case EXIT:
                    System.out.println("Bye");
                    return;
                default:
                    System.out.println("Invalid selection");
            }
        }
    }

    private void printMenu() {
        UserMenuSelection[] allPossibleSelection
                = UserMenuSelection.values();
        for (UserMenuSelection menuPosition : allPossibleSelection) {
            System.out.println(menuPosition.getOptionNumber()
                    + ". "
                    + menuPosition.getOptionText());
        }
    }

    private UserMenuSelection getUserSelection() {
        String userSelection = new Scanner(System.in).nextLine();
        try {
            Integer menuNumber = Integer.valueOf(userSelection);

        } catch (NumberFormatException e) {
        }
        return null;
    }

    public static void main(String[] args) {
        new Main().startApplication();
    }
}
