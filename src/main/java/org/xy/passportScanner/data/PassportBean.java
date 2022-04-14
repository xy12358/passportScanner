package org.xy.passportScanner.data;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PassportBean {
    //文件序号
    private StringProperty id = new SimpleStringProperty();
    //文件名
    private StringProperty file = new SimpleStringProperty();
    //护照号
    private StringProperty number = new SimpleStringProperty();
    //姓名
    private StringProperty name = new SimpleStringProperty();
    //性别
    private StringProperty gender = new SimpleStringProperty();
    //出生日期
    private StringProperty birthday = new SimpleStringProperty();
    //有效期
    private StringProperty expireDate = new SimpleStringProperty();
    //签发国
    private StringProperty issuingCountry = new SimpleStringProperty();
    //国籍
    private StringProperty nationality = new SimpleStringProperty();
    //备注
    private StringProperty comments = new SimpleStringProperty();

    public PassportBean(String id, String file, String number, String name, String gender, String birthday, String expireDate, String issuingCountry, String nationality, String comments){
        this.id.set(id);
        this.file.set(file);
        this.number.set(number);
        this.name.set(name);
        this.gender.set(gender);
        this.birthday.set(birthday);
        this.expireDate.set(expireDate);
        this.issuingCountry.set(issuingCountry);
        this.nationality.set(nationality);
        this.comments.set(comments);
    }
    public PassportBean(){}

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getFile() {
        return file.get();
    }

    public StringProperty fileProperty() {
        return file;
    }

    public void setFile(String file) {
        this.file.set(file);
    }

    public String getNumber() {
        return number.get();
    }

    public StringProperty numberProperty() {
        return number;
    }

    public void setNumber(String number) {
        this.number.set(number);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getGender() {
        return gender.get();
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public String getBirthday() {
        return birthday.get();
    }

    public StringProperty birthdayProperty() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday.set(birthday);
    }

    public String getExpireDate() {
        return expireDate.get();
    }

    public StringProperty expireDateProperty() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate.set(expireDate);
    }

    public String getIssuingCountry() {
        return issuingCountry.get();
    }

    public StringProperty issuingCountryProperty() {
        return issuingCountry;
    }

    public void setIssuingCountry(String issuingCountry) {
        this.issuingCountry.set(issuingCountry);
    }

    public String getNationality() {
        return nationality.get();
    }

    public StringProperty nationalityProperty() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality.set(nationality);
    }

    public String getComments() {
        return comments.get();
    }

    public StringProperty commentsProperty() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments.set(comments);
    }
}
