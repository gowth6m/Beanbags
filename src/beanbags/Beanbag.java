package beanbags;

public class Beanbag {

    private int num;
    private String manufacturer;
    private String name;
    private String id;
    private short year;
    private byte month;
    private String information;
    private int price = 0; // Initial price
    private int reservationId = -1; // Default meaning not reserved

    /**
     * Constructor used to initialize beanbag
     * <p>
     * @param num
     * @param manufacturer
     * @param name
     * @param id
     * @param year
     * @param month
     * @throws IllegalNumberOfBeanBagsAddedException
     * @throws IllegalIDException
     * @throws InvalidMonthException
     */
    public Beanbag(int num, String manufacturer, String name, String id, short year, byte month) 
    throws IllegalNumberOfBeanBagsAddedException, IllegalIDException, 
    InvalidMonthException {
        this.setNum(num);
        this.setManufacturer(manufacturer);
        this.setName(name);
        this.setId(id);
        this.setYear(year);
        this.setMonth(month);
    }

    /**
     * Constructor used to initialize beanbag
     * <p>
     * @param num           the number assigned to the beanbag
     * @param manufacturer  the name of the manufacturer
     * @param name          the name of the buyer
     * @param id            the id of the beanbag
     * @param year          the year of manufacture
     * @param month         the month of manufacture
     * @param information   extra information about the bean bag
     * @throws IllegalNumberOfBeanBagsAddedException
     * @throws IllegalIDException
     * @throws InvalidMonthException
     */
    public Beanbag(int num, String manufacturer, String name, String id, short year, byte month, String information) 
    throws IllegalNumberOfBeanBagsAddedException, IllegalIDException, 
    InvalidMonthException {
        this.setNum(num);
        this.setManufacturer(manufacturer);
        this.setName(name);
        this.setId(id);
        this.setYear(year);
        this.setMonth(month);
        this.setInformation(information);
    }

    // -------------------------------------------------------------------------//
    // Set and get functions
    // 1. num
    // Used to set the value of num, number is the number of bags of this type
    public void setNum(int num)
    throws IllegalNumberOfBeanBagsAddedException {
        // Validation checks
        // Check if the num to be set is less than 1 if true throw an error
        if(num < 1){
            throw new IllegalNumberOfBeanBagsAddedException("The num of beanbags must be greater or equal to 1");
        }
        // Set the number, everything is ok
        this.num = num;
    }

    // Used to get the value of num
    public int getNum() {
        return this.num;
    }

    // 2. Manufacturer
    // Set the manufacturer name
    public void setManufacturer(String manufacturer) {
        // Validation checks
        // No validation for manufacturer
        this.manufacturer = manufacturer;
    }

    // Get the manufacturer name
    public String getManufacturer() {
        return this.manufacturer;
    }

    // 3. name
    // Set the name of the buyer
    public void setName(String name){
        // Validation checks
        // No validation for the name
        this.name = name;
    }

    // Get the name of the manufacturer
    public String getName() {
        return this.name;
    }

    // 4. id
    // Set the id that uniquely identifies the bean bag
    public void setId(String id)
    throws IllegalIDException {
        // Validation checks
        // Check if the ID is  a positive eight character hexadecimal number
        // Check if the id length is equal to 8
        if(id.length() != 8) {
            // Throw the error
            throw new IllegalIDException("The length of the ID must be equal to 8");
        }

        // Use radix to check validity whether the id supplied is valid
        try{
            Long.parseLong(id,16);
        }  
        catch(NumberFormatException e){
            throw new IllegalIDException("The ID must have only hexadecimal characters i.e. 0 - 9, A - F");
        }
        
        // Everything is good
        this.id = id;
    }

    // Get the id of the bean bag
    public String getId() {
        return this.id;
    }

    // 5. Year
    // Set the year the bean bag was purchased
    public void setYear(short year) {
        // Validation here
        // No validation for year specified, just insert it
        this.year = year;
    }

    // Get the year
    public short getYear() {
        return this.year;
    }

    // 6. Month
    // Set the month of manufacture
    public void setMonth(byte month)
    throws InvalidMonthException {
        // Validation here
        // Check if month falls within the range 1 to  12
        if((int) month < 1 || (int) month > 12) {
            throw new InvalidMonthException("Invalid month, month must be between 1 and 12");
        }

        // The month is valid
        this.month = month;
    }

    // Get month
    public byte getMonth() {
        return this.month;
    }

    // 7. Information
    // Set extra information about the beanbag
    public void setInformation(String information) {
        // Validation here
        // No validation for this so just set it
        this.information = information;
    }

    // Get information
    public String getInformation() {
        return this.information;
    }

    // 8. Price
    // Used to set the price of the bean bag
    public void setPrice(int price)
    throws InvalidPriceException {
        // Validation 
        // Check if price is less that 1
        if(price < 1) {
            throw new InvalidPriceException("Price must be greater than 1");
        }

        // The price is ok
        this.price = price;
    }

    // Get the price of the bean bag
    public int getPrice() {
        return this.price;
    };

    // 9: Reservation
    // Set the reservation id
    public void setReservationId(int reservationId) {
        if(reservationId > 0) {
            this.reservationId = reservationId;
        }
    }

    // Get the reservation id
    public int getReservationId() {
        return this.reservationId;
    }
    
    // --------------------------------------------------------------------------------//
}
