package appchat.anh.nam.model;

public class Group {
    private String id;
    private String createAt;
    private String name;
    private String groupIcon;

    public Group(String createAt, String name, String groupIcon) {
        this.createAt = createAt;
        this.name = name;
        this.groupIcon = groupIcon;
    }

    public Group(String id, String createAt, String name, String groupIcon) {
        this.id = id;
        this.createAt = createAt;
        this.name = name;
        this.groupIcon = groupIcon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupIcon() {
        return groupIcon;
    }

    public void setGroupIcon(String groupIcon) {
        this.groupIcon = groupIcon;
    }
}
