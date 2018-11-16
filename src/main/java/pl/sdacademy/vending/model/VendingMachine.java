package pl.sdacademy.vending.model;

import pl.sdacademy.vending.util.Configuration;

import java.util.Optional;
import java.util.Random;

/**
 * Class that is representing our vending machine. Is build from trays, that are grouped into rows and columns.
 * Provides general methods, that interact with this machine. Those methods should reflect possible operations that user can invoke.
 */
public class VendingMachine {

    private final Configuration configuration;
    private final Long rowsCount;
    private final Long colsCount;
    // two dimensional array that keeps trays organized into rows and columns
    private final Tray[][] trays;

    public VendingMachine(Configuration configuration) {
        this.configuration = configuration;
        // loading size of vending machine from configuration
        rowsCount = configuration
                .getLongProperty(
                        "machine.size.rows",
                        6L);
        colsCount = configuration
                .getLongProperty(
                        "machine.size.cols",
                        4L);
        // validation of machine size. If Machine has improper size, exception is thrown - we cannot operate on invalid machine.
        if (rowsCount <= 0 || rowsCount > 26) {
            throw new IllegalArgumentException(
                    "Row count " + rowsCount + " is invalid");
        }
        if (colsCount <= 0 || colsCount > 9) {
            throw new IllegalArgumentException(
                    "Col count " + colsCount + " is invalid");
        }
        // reserving space for trays
        trays = new Tray[rowsCount.intValue()][colsCount.intValue()];

        // generation of trays - only temporary for debug purposes.
        Random random = new Random();
        for (int rowNo = 0; rowNo < rowsCount; rowNo++) {
            for (int colNo = 0; colNo < colsCount; colNo++) {
                // probability of tray existence of specified prosition is 0.8
                if (random.nextInt(10) < 8) {
//                if (Math.random() < 0.8) { // another way to generate probability 0.8
                    generateTrayAtPosition(rowNo, colNo);
                }
            }
        }
    }

    private void generateTrayAtPosition(int rowNo, int colNo) {
        Random random = new Random();
        // random price from 100-1000 range
        long price = random.nextInt(901) + 100;
        // conversion of row number to letter code - see ASCII table
        char letter = (char) ('A' + rowNo);
        // converting zero-based numering to normal-human-numering
        int number = colNo + 1;
        // concatenation of tray symbol
        String symbol = "" + letter + number;

        // till build() method is not invoked, we can still modify builder.
        // therefore, we are preserving builder for future, setting only its symbol and price
        Tray.Builder trayBuilder = Tray.builder(symbol).price(price);
        int productProbability = random.nextInt(10);
        // probability of having at least one product is 0.5
        if (productProbability < 5) {
            // adding new product to builder
            trayBuilder = trayBuilder.product(new Product("Product " + symbol));
        }
        // probability of having second product is 0.1
        if (productProbability < 1) {
            // adding another product to builder
            trayBuilder = trayBuilder.product(new Product("Product " + symbol));
            // this IF will be invoked only if "productProbability" value was 0. There is no way that this IF was invoked while previous one wasn't
        }
        // finally, invoking build on builder.
        trays[rowNo][colNo] = trayBuilder.build();
    }

    /**
     * Safe method that is trying to obtain tray at specified position in this vending machine
     */
    public Optional<Tray> getTrayAtPosition(int rowNo, int colNo) {
        try {
            Tray tray = trays[rowNo][colNo];
            // if asked for valid position (not necessarily containing tray - probability is 0.8!)
            // it is retuned as optional
            return Optional.ofNullable(tray);
        } catch (ArrayIndexOutOfBoundsException e) {
            // in case if asked for invalid position (not existing position), empty optional is returned.
            return Optional.empty();
        }
    }

    public Long rowsCount() {
        return rowsCount;
    }

    public Long colsCount() {
        return colsCount;
    }

    /**
     * Returns first product name for tray at specified position. If no product or tray available, empty optional is returned.
     */
    public Optional<String> productNameAtPosition(int rowNo, int colNo) {
        Optional<Tray> tray = getTrayAtPosition(rowNo, colNo);
        if (tray.isPresent()) {
            // if tray was found, ask for its first product
            return tray.get().firstProductName();
        } else {
            // if no tray, then return empty optional
            return Optional.empty();
        }
    }

    /**
     * Allows for buying product. Does not check cost of product.
     */
    public Optional<Product> buyProductWithSymbol(String traySymbol) {
        // if provided symbol (e.g. when symbol "AB1" will be provided) length is invalid, then abort buying product
        if (traySymbol.length() != 2) {
            return Optional.empty();
        }
        // splitting symbol to letter and number
        char symbolLetter = traySymbol.toUpperCase().charAt(0);
        char symbolNumber = traySymbol.charAt(1);
        // converting parts of symbol to array indexes
        int rowNo = symbolLetter - 'A';
        int colNo = symbolNumber - '1';
        // checking if converted values are valid
        if (rowNo < 0 || rowNo >= rowsCount
                || colNo < 0 || colNo >= colsCount ) {
            // if values are out of range, nothing can be bought, so empty optional is returned.
            return Optional.empty();
        }
        Tray tray = trays[rowNo][colNo];
        if (tray == null) {
            // if no tray found, empty optional is returned (nothing to buy)
            return Optional.empty();
        } else {
            // if tray was found, invoking purchase. From vending machine point of view, it is not important,
            // if products are available or not. It is tray responsibility to check that.
            return tray.buyProduct();
        }
    }




}
