package study.project.whereareyou.OOP;

/**
 * Created by Administrator on 24/11/2015.
 */
public class Friend {
    private String Id;
    private String Email;
    private String Name;
    private String PhotoUrl;
    private String BirthDate;
    private String LastLocation;
    private String isFriendOf;

    public Friend() {
    }

    public Friend(String id, String email, String name, String photoUrl, String birthDate, String lastLocation, String isFriendOf) {
        Id = id;
        Email = email;
        Name = name;
        PhotoUrl = photoUrl;
        BirthDate = birthDate;
        LastLocation = lastLocation;
        this.isFriendOf = isFriendOf;
    }

    public Friend(String email, String name, String photoUrl, String birthDate, String lastLocation, String isFriendOf) {
        Email = email;
        Name = name;
        PhotoUrl = photoUrl;
        BirthDate = birthDate;
        LastLocation = lastLocation;
        this.isFriendOf = isFriendOf;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }

    public String getLastLocation() {
        return LastLocation;
    }

    public void setLastLocation(String lastLocation) {
        LastLocation = lastLocation;
    }

    public String getIsFriendOf() {
        return isFriendOf;
    }

    public void setIsFriendOf(String isFriendOf) {
        this.isFriendOf = isFriendOf;
    }
}
