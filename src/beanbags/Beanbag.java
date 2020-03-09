package beanbags;

public class Beanbag {

    public int num;
    public String manufacturer;
    public String name;
    public String id;
    public short year;
    public byte month;
    public String information;

    public Beanbag(int num1, String manufacturer1, String name1, String id1, short year1, byte month1) {
        num = num1;
        manufacturer = manufacturer1;
        name = name1;
        id = id1;
        year = year1;
        month = month1;
    }

    public Beanbag(int num1, String manufacturer1, String name1, String id1, short year1, byte month1, String information1) {
        num = num1;
        manufacturer = manufacturer1;
        name = name1;
        id = id1;
        year = year1;
        month = month1;
        information = information1;
    }

}
