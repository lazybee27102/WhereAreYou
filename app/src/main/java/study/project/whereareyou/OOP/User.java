package study.project.whereareyou.OOP;

/**
 * Created by Administrator on 24/11/2015.
 */
public class User {
    private String Id;

    public User(String id, String email, String name, String photoUrl, String birthDate, String lastLocation) {
        Id = id;
        Email = email;
        Name = name;
        PhotoUrl = photoUrl;
        BirthDate = birthDate;
        LastLocation = lastLocation;
    }

    public User(String email, String name, String photoUrl, String birthDate, String lastLocation) {
        Email = email;
        Name = name;
        PhotoUrl = photoUrl;
        BirthDate = birthDate;
        LastLocation = lastLocation;
    }

    private String Email;
    private String Name;
    private String PhotoUrl;
    private String BirthDate;
    private String LastLocation;

    public User() {
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
}
