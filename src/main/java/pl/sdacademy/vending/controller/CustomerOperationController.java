package pl.sdacademy.vending.controller;

import pl.sdacademy.vending.model.Tray;
import pl.sdacademy.vending.model.VendingMachine;
import pl.sdacademy.vending.util.StringUtil;

import java.util.Optional;

/**
 Top layer of classic architecture, that handles communication with user. It will contain all Customer related operations, that can be invoked.
 */
public class CustomerOperationController {
    // machine, that customer is operating, is set up with constructor. It makes the machine an "dependency" for this controller
    private final VendingMachine machine;
    // setting, that defines, how many characters is tray width.
    private final Integer trayWidth = 12;

    /**
     Public constructor that set up all dependencies for this controller.
     */
    public CustomerOperationController(VendingMachine machine) {
        this.machine = machine;
    }

    /**
     Main method that is printing machine to the console.
     */
    public void printMachine() {
        // every row of machine contains trays. Every tray contains few properties, that has to be displayed in another line. Every line has to be completed for all trays at once.
        for (int rowNo = 0; rowNo < machine.rowsCount(); rowNo++) {
            // first line of trays contains its upper boundary
            for (int colNo = 0; colNo < machine.colsCount(); colNo++) {
                printUpperBoundary(rowNo, colNo);
            }
            System.out.println(); // going to next line after previous one is completed

            // second line will contain tray symbols
            for (int colNo = 0; colNo < machine.colsCount(); colNo++) {
                printSymbol(rowNo, colNo);
            }
            System.out.println(); // going to next line after previous one is completed

            // third contain its name
            for (int colNo = 0; colNo < machine.colsCount(); colNo++) {
                printName(rowNo, colNo);
            }
            System.out.println(); // going to next line after previous one is completed

            // fourth is price
            for (int colNo = 0; colNo < machine.colsCount(); colNo++) {
                printPrice(rowNo, colNo);
            }
            System.out.println(); // going to next line after previous one is completed

            // and last one is for lower boundary
            for (int colNo = 0; colNo < machine.colsCount(); colNo++) {
                printLowerBoundary(rowNo, colNo);
            }
            System.out.println(); // going to next line after previous one is completed
        } // all steps will be repeated for all rows of trays
    }

    private void printUpperBoundary(int rowNo, int colNo) {
        System.out.print(
                "+" // left upper corner of tray
                        + StringUtil.duplicateText("-", trayWidth) // multipying "-" character so it will match tray width
                        + "+"); // right upper corner of tray
    }

    private void printSymbol(int rowNo, int colNo) {
        Optional<Tray> tray = machine.getTrayAtPosition(rowNo, colNo);
        String traySymbol = tray.map(Tray::getSymbol).orElse("--");
        System.out.print(
                "|" // left boundary
                        + StringUtil.adjustText(traySymbol, trayWidth) // centering text
                        + "|"); // right boundary
    }

    private void printName(int rowNo, int colNo) {
        Optional<String> productName = machine.productNameAtPosition(rowNo, colNo);
        String formattedName = productName.orElse("--");
        System.out.print("|"
                + StringUtil.adjustText(formattedName, trayWidth) // centering text
                + "|");
    }

    private void printPrice(int rowNo, int colNo) {
        Optional<Tray> tray = machine.getTrayAtPosition(rowNo, colNo);
        Long price = tray.map(Tray::getPrice).orElse(0L);
        String formattedMoney = StringUtil.formatMoney(price); // converting money to text
        String centeredMoney = StringUtil.adjustText(formattedMoney, trayWidth); // centering text
        System.out.print("|" + centeredMoney + "|");
    }

    private void printLowerBoundary(int rowNo, int colNo) {
        System.out.print(
                "+" // left lower corner of tray
                        + StringUtil.duplicateText("-", trayWidth) // centering text
                        + "+"); // right lower corner of tray
    }

}