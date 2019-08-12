package appchat.anh.nam.model;

public class User {
    private String id;
    private String fullName;
    private String profilePic;
    private String status;
  
    public User() {
    }

    public User(String id, String fullName, String profilePic, String status) {
        this.id = id;
        this.fullName = fullName;
        this.profilePic = profilePic;
        this.status = status;
    }

    public User(String fullName, String profilePic, String status) {
        this.fullName = fullName;
        this.profilePic = profilePic;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
