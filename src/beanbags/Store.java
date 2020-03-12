package beanbags;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Store implements BeanBagStore {
    private ObjectArrayList beanBagsInStock, beanBagsSold, beanBagsReserved, reservationNumbers;

    // Constructor
    Store() {
        this.beanBagsInStock = new ObjectArrayList();
        this.beanBagsReserved = new ObjectArrayList();
        this.beanBagsSold = new ObjectArrayList();
        this.reservationNumbers = new ObjectArrayList();
    }

    int reservationId = 0;

    int getReservationUniqueId() {
        return this.reservationId++;
    }

    public void addBeanBags(int num, String manufacturer, String name, String id, short year, byte month)
            throws IllegalNumberOfBeanBagsAddedException, BeanBagMismatchException, IllegalIDException,
            InvalidMonthException {
        // Check if the beanbag already exists, will throw an error if the bag already
        // exists
        this.exists(id);

        beanBagsInStock.add(new Beanbag(num, manufacturer, name, id, year, month));
    }

    public void addBeanBags(int num, String manufacturer, String name, String id, short year, byte month,
            String information) throws IllegalNumberOfBeanBagsAddedException, BeanBagMismatchException,
            IllegalIDException, InvalidMonthException {
        // Check if the beanbag already exists, will throw an error if the bag already exists
        this.exists(id);

        beanBagsInStock.add(new Beanbag(num, manufacturer, name, id, year, month, information));
    }

    public void setBeanBagPrice(String id, int priceInPence)
            throws InvalidPriceException, BeanBagIDNotRecognisedException, IllegalIDException {
        // Check if the id is valid
        this.checkID(id);

        // Check if the bean bag exists in the stock or any other place
        Beanbag temp = this.existsAll(id);

        // Everything is ok, set the new price, will throw an error if the price is < 1
        temp.setPrice(priceInPence);
    }

    public int reserveBeanBags(int num, String id)
            throws BeanBagNotInStockException, InsufficientStockException, IllegalNumberOfBeanBagsReservedException,
            PriceNotSetException, BeanBagIDNotRecognisedException, IllegalIDException {
        // Check if the id is legal
        this.checkID(id);

        // Check if the bean bag has been bought and is no longer in stock
        this.isBought(id);

        // Find the bean bag and check the stock size
        Beanbag temp = this.findBeanBag(num, id);

        // Check if the current number of requested reserved number < 1
        if (num < 1) {
            throw new IllegalNumberOfBeanBagsReservedException("You can only reserve more than one bag");
        }

        // Return the unique reservation number
        this.reservationNumbers.add(this.getReservationUniqueId());

        // Set the reservation id
        temp.setReservationId(reservationId);

        // Add the beanbag to the array of reserved
        this.beanBagsReserved.add(temp);

        return this.reservationId;
    }

    public void unreserveBeanBags(int reservationNumber) throws ReservationNumberNotRecognisedException {
        // Check to see if the reservation number exists
        this.findReservationNumber(reservationNumber);

        // Remove from reservations number list therefore cancelling the reservation
        this.reservationNumbers.remove(reservationNumber);

        // Get the reservation and remove it from reservation list
        for (int i = 0; i < this.reservedBeanBagsInStock(); ++i) {
            if (((Beanbag) this.reservationNumbers.get(i)).getReservationId() == reservationNumber) {
                this.reservationNumbers.remove(i);
            }
        }
    }

    public void sellBeanBags(int num, String id)
            throws BeanBagNotInStockException, InsufficientStockException, IllegalNumberOfBeanBagsSoldException,
            PriceNotSetException, BeanBagIDNotRecognisedException, IllegalIDException {
        // Check if the id is legal
        this.checkID(id);

        // Check if the bean bag has been bought and is no longer in stock
        this.isBought(id);

        // Find the bean bag and check the stock size
        Beanbag temp = this.findBeanBag(num, id);

        // Check if the current number of requested reserved number < 1
        if (num < 1) {
            throw new IllegalNumberOfBeanBagsSoldException("You can only sell more than one bag");
        }

        // Perform the sell
        // Remove from stock and place the item in the sold category
        this.beanBagsSold.add(temp);

        // When the num in stock is 1 when we try to add a 0 into the system an error will be generated, so we
        // will instead remove this from stock
        try {
            temp.setNum(temp.getNum() - 1);
        } catch (IllegalNumberOfBeanBagsAddedException e) {
            this.beanBagsInStock.remove(temp);
        }
    }

    public void sellBeanBags(int reservationNumber) throws ReservationNumberNotRecognisedException {
        // Check if the reservation number exists
        this.findReservationNumber(reservationNumber);

        // Perform the sell
        for(int i = 0; i < this.beanBagsInStock(); ++i){
            if(((Beanbag) this.beanBagsReserved.get(i)).getReservationId() == reservationNumber){
                this.beanBagsSold.add((Beanbag) this.beanBagsReserved.get(i));
                
                // Try to reduce the stock by one, if the stock was 1 then an error will occur and we will instead
                // remove the item from stock
                try {
                    ((Beanbag) this.beanBagsReserved.get(i)).setNum(((Beanbag) this.beanBagsReserved.get(i)).getNum() - 1);
                } catch (IllegalNumberOfBeanBagsAddedException e) {
                    this.beanBagsInStock.remove(this.beanBagsReserved.get(i));
                }
                return;
            }
        }
    }

    public int beanBagsInStock() {
        return this.beanBagsInStock.size();
    }

    public int reservedBeanBagsInStock() {
        return this.beanBagsReserved.size();
    }

    public int beanBagsInStock(String id) throws BeanBagIDNotRecognisedException, IllegalIDException {
        // Check id
        this.checkID(id);

        // Check if the beanbag is recognised in the system
        return this.existsAll(id).getNum();
    }

    public void saveStoreContents(String filename) throws IOException {
        // FileOutputStream f_out = new FileOutputStream("Store.dat");

        FileOutputStream f_out = new FileOutputStream(filename);
        // Create an object output stream inorder tio write this class
        ObjectOutputStream o_os = new ObjectOutputStream(f_out);

        // Write this class' data
        o_os.writeObject(this);
        o_os.close();
    }

    public void loadStoreContents(String filename) throws IOException, ClassNotFoundException {
        FileInputStream f_in = new FileInputStream(filename);
        ObjectInputStream o_is = new ObjectInputStream(f_in);
        Store t = (Store) o_is.readObject();

        // Copy the data
        this.beanBagsInStock = t.beanBagsInStock;
        this.beanBagsReserved = t.beanBagsReserved;
        this.beanBagsSold = t.beanBagsSold;
        this.reservationId = t.reservationId;
        this.reservationNumbers = t.reservationNumbers;

        // Close the file
        o_is.close();
    }

    public int getNumberOfDifferentBeanBagsInStock() {
        int sum = 0;

        for (int i = 0; i < this.beanBagsInStock(); ++i) {
            sum += ((Beanbag) this.beanBagsInStock.get(i)).getNum();
        }

        return sum;
    }

    public int getNumberOfSoldBeanBags() {
        return this.beanBagsSold.size();
    }

    public int getNumberOfSoldBeanBags(String id) throws BeanBagIDNotRecognisedException, IllegalIDException {
        // Check if ID is valid
        this.checkID(id);

        // Check to see if the bean bag is recognised
        return this.existsAll(id).getNum();
    }

    public int getTotalPriceOfSoldBeanBags() {
        int total = 0;
        for (int i = 0; i < this.beanBagsSold.size(); ++i) {
            total += ((Beanbag) this.beanBagsSold.get(i)).getNum()
                    * ((Beanbag) this.beanBagsSold.get(i)).getPrice();
        }
        return total;
    }

    public int getTotalPriceOfSoldBeanBags(String id) throws BeanBagIDNotRecognisedException, IllegalIDException {
        // Check if ID is valid
        this.checkID(id);

        // Check to see if the bean bag is recognised
        return this.existsAll(id).getNum() * this.existsAll(id).getPrice();
    }

    public int getTotalPriceOfReservedBeanBags() {
        int total = 0;

        for (int i = 0; i < this.reservedBeanBagsInStock(); ++i) {
            total += ((Beanbag) this.beanBagsReserved.get(i)).getNum()
                    * ((Beanbag) this.beanBagsReserved.get(i)).getPrice();
        }

        return total;
    }

    public String getBeanBagDetails(String id) throws BeanBagIDNotRecognisedException, IllegalIDException {
        // Check id
        this.checkID(id);

        return this.existsAll(id).getInformation();
    }

    public void empty() {
        // Clear bags in stock
        this.clear(beanBagsInStock);

        // Clear sold bags
        this.clear(beanBagsSold);

        // Clear reserved bags
        this.clear(beanBagsReserved);

        // Clear reservation numbers
        this.clear(reservationNumbers);

        // Reset reservation counter
        this.reservationId = 0;
    }

    public void resetSaleAndCostTracking() {
        for (int i = 0; i < this.beanBagsSold.size(); ++i) {
            try {
                ((Beanbag) this.beanBagsSold.get(i)).setNum(1);
                ((Beanbag) this.beanBagsSold.get(i)).setPrice(1);
            } catch (IllegalNumberOfBeanBagsAddedException | InvalidPriceException e) {
                return;
            }
        }
     }
     
    public void replace(String oldId, String replacementId) 
    throws BeanBagIDNotRecognisedException, IllegalIDException {
        // Check to see that the ids are nor illegal
        this.checkID(oldId);
        this.checkID(replacementId);

        // Check to see if the bean bag is in the system
        this.existsAll(oldId).setId(replacementId);
     }
    
    // ----------------------------------------------------------------------------------//
    // UTILITY functions
    //Checks whether a bean bag has been bought and is no longer in stock
    private void isBought(String id)
    throws BeanBagNotInStockException {
        for(int i = 0; i < this.getNumberOfSoldBeanBags(); ++i) {
            if(((Beanbag) this.beanBagsSold.get(i)).getId().equals(id)) {
                throw new BeanBagNotInStockException("Sorry the bean bag is not available in stock");
            }
        }
    }

    // Clear an ObjectArrayList object
    private void clear(ObjectArrayList ar) {
        for(int i = 0; i < ar.size(); ++i) {
            ar.remove(i);
        }
    }

    // Return the index in order to delete the reservation
    private void findReservationNumber(int reservationNumber) 
    throws ReservationNumberNotRecognisedException {
        for(int i = 0; i < this.reservationNumbers.size(); ++i) {
            if((int) this.reservationNumbers.get(i) == reservationNumber) {
                return;
            }
        }

        // The reservation number was not found
        throw new ReservationNumberNotRecognisedException("The reservation number was not found");
    }

    /**
     * Utility method that will be used to check whether the item is already added
     * <p>
     * This function is a utility function and is private. It is used to check if
     * the beanbag already exists in the arraylist
     * 
     * @throws BeanBagMismatchException if the id already exists (as a current in
     *                                  stock bean bag, or one that has been
     *                                  previously stocked in the store, but the
     *                                  other stored elements (manufacturer, name
     *                                  and free text) do not match the pre-existing
     *                                  version
     */
    private void exists(String id)
    throws BeanBagMismatchException {
        // Loop over the whole number of beanbags that are in stock
        for(int i  = 0; i < this.beanBagsInStock(); ++i) {
            if(((Beanbag) this.beanBagsInStock.get(i)).getId().equals(id)) {
                throw new BeanBagMismatchException("The bean bag already exists");
            }
        }
    }

    private Beanbag existsAll(String id)
    throws BeanBagIDNotRecognisedException {
        // Loop over the whole number of beanbags that are in stock
        for(int i  = 0; i < this.beanBagsInStock(); ++i){
            if(((Beanbag) this.beanBagsInStock.get(i)).getId() == id) {
                return (Beanbag) this.beanBagsInStock.get(i);
            }
        }

        throw new BeanBagIDNotRecognisedException("The bean bag was not found");
    }

    /**
     * @param id  id of the beanbag
     * @throws IllegalIDException The ID is invalid
     */
    private void checkID(String id)
    throws IllegalIDException {
        // Use radix to check validity whether the id supplied is valid
        try {
            Long.parseLong(id,16);

            if(id.length() < 8){
                throw new NumberFormatException("Number must be equal to 8");
            }
        }  
        catch(NumberFormatException e){
            throw new IllegalIDException("The ID must have only hexadecimal characters i.e. 0 - 9, A - F");
        }
    }

    private Beanbag findBeanBag(int num, String id) 
    throws InsufficientStockException, BeanBagIDNotRecognisedException, PriceNotSetException {
        // Check if the bean bag exists in DB
        Beanbag temp = this.existsAll(id);

        // Check the stock size if it is more than num
        if(temp.getNum() < num){
            throw new InsufficientStockException("The stock is less than the requested number if bags");
        }

        // Check if the price is set yet
        if(temp.getPrice() == 0){
            throw new PriceNotSetException("The price has not yet been set");
        }

        // The bag was found
        return temp;
    }
    //-----------------------------------------------------------------------------------//

}
