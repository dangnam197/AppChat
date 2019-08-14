package appchat.anh.nam.model;

public class Group {
    private long createAt;
    private String groupIcon;
    private String id;
    private String name;

    public Group() {
    }

    public Group(long createAt, String groupIcon, String id, String name) {
        this.createAt = createAt;
        this.groupIcon = groupIcon;
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
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
