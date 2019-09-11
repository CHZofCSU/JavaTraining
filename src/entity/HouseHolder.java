package entity;

public class HouseHolder {

    private int id;
    private String name;
    private String password;
    private String gender;
    private String houseNumber;
    private Double houseSquare;
    private String img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public Double getHouseSquare() {
        return houseSquare;
    }

    public void setHouseSquare(Double houseSquare) {
        this.houseSquare = houseSquare;
    }
}
