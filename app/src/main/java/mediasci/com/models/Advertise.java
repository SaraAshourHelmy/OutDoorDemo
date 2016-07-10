package mediasci.com.models;

/**
 * Created by sara on 7/4/2016.
 */
public class Advertise {

    private int id;
    private String gps_address, address, title, note;
    private int type;
    private int revise;
    private double latitude,longitude;
    private byte[] img;

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public int getRevise() {
        return revise;
    }

    public void setRevise(int revise) {
        this.revise = revise;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGps_address() {
        return gps_address;
    }

    public void setGps_address(String gps_address) {
        this.gps_address = gps_address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
