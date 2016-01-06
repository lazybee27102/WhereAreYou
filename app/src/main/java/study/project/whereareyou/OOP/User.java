package study.project.whereareyou.OOP;

/**
 * Created by Administrator on 24/11/2015.
 */
public class User {
    private String Id;
    private String FirstName;
    private String LastName;
    private String Email;
    private String UserName;
    private String BirthDate;
    private String LastLocation;

    public User() {
    }

    public User(String id,String email,  String userName,String firstName, String lastName,  String birthDate, String lastLocation) {
        Id = id;
        FirstName = firstName;
        LastName = lastName;
        Email = email;
        UserName = userName;
        BirthDate = birthDate;
        LastLocation = lastLocation;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
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
