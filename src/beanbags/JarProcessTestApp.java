package beanbags;

public class JarProcessTestApp {
    public static void main(String[] args) {
        BeanBagStore store = new Store();
        // System.out.println("Store instance successfully made, with " + store.beanBagsInStock() + " beanbags in stock.");

        // Part 1:
        // Adding a beanbag to stock
        // -----------------------------------------------------------------------------------//
        try {
            // Test case 1:
            // Add with an invalid num
            // store.addBeanBags(-1, "Apple", "Bob", "20A51125", (short) 2019, (byte) 03);

            // Test case 2:
            // Add with an existing beanbag that is similar to the first
            // store.addBeanBags(1, "Apple", "Bob", "20A51125", (short) 2019, (byte) 03);
            // store.addBeanBags(1, "Apple", "Bob", "20A51125", (short) 2019, (byte) 03);

            // Test case 3:
            // Add with an illegal ID
            // store.addBeanBags(1, "Apple", "Bob", "20A5112G", (short) 2019, (byte) 03);

            // Test case 4:
            // Add with an invalid month
            // store.addBeanBags(1, "Apple", "Bob", "20A51125", (short) 2019, (byte) 24);

            // Correct way: No errors
            store.addBeanBags(1, "Apple", "Bob", "20A51125", (short) 2019, (byte) 03);
        } catch (IllegalNumberOfBeanBagsAddedException e) {
            // Will catch test case 1
            System.out.println(e.getMessage());
        } catch (BeanBagMismatchException e) {
            // Will catch error for test case 2
            System.out.println(e.getMessage());
        } catch (IllegalIDException e) {
            // Will catch error for test case 3
            System.out.println(e.getMessage());
        } catch (InvalidMonthException e) {
            // Will catch error for test case 4
            System.out.println(e.getMessage());
        }
        // --------------------------------------------------------------//

        // Part 2:
        // Selling the beanbag
        try {
            // Test case 1
            // Using an invalid price
            // store.setBeanBagPrice("20A51125", -2);

            // Test case 2
            // The ID is correct but does not match any known value
            // store.setBeanBagPrice("FFA51126", 20);

            // Test case 3
            // The idea fails to follow the rules and therefore is considered wrong
            // store.setBeanBagPrice("20A5", 20);

            // The correct way
            store.setBeanBagPrice("20A51125", 20);

        } catch (InvalidPriceException e) {
            // Will catch test case 1
            System.out.println(e.getMessage());
        } catch (BeanBagIDNotRecognisedException e) {
            // Will catch test case 2
            System.out.println(e.getMessage());
        } catch (IllegalIDException e) {
            // Will catch test case 3
            System.out.println(e.getMessage());
        }

        // Part 3
        // Reserving a bean bag
        try {
            // Test case 1
            // The beanbag is out of stock
            try {
                store.sellBeanBags(1, "20A51125");
            } catch (IllegalNumberOfBeanBagsSoldException e) {
                // Will catch test case 3
                System.out.println(e.getMessage());
            }

            store.reserveBeanBags(2, "20A51125");

            // The correct way
            store.reserveBeanBags(2, "20A51125");

        } catch (BeanBagNotInStockException e) {
            // Will catch test case 1
            System.out.println(e.getMessage());
        } catch (InsufficientStockException e) {
            // Will catch test case 2
            System.out.println(e.getMessage());
        } catch (IllegalNumberOfBeanBagsReservedException e) {
            // Will catch test case 3
            System.out.println(e.getMessage());
        } catch (PriceNotSetException e) {
            // Will catch test case 4
            System.out.println(e.getMessage());
        } catch (BeanBagIDNotRecognisedException e) {
            // Will catch test case 5
            System.out.println(e.getMessage());
        } catch (IllegalIDException e) {
            // Will catch test case 6
            System.out.println(e.getMessage());
        }
    }
}
