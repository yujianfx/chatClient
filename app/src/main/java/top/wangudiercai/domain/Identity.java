package top.wangudiercai.domain;

public class Identity {
    private String name;
    private String avatarUrl;

    public Identity(String name, String avatarUrl) {
        this.name = name;
        this.avatarUrl = avatarUrl;
    }

    public Identity() {
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (!(o instanceof Identity)) {
            return false;
        }

        Identity identity = (Identity) o;

        if (getName() != null ? !getName().equals(identity.getName()) : identity.getName() != null) {
            return false;
        }
        return getAvatarUrl() != null ? getAvatarUrl().equals(identity.getAvatarUrl()) : identity.getAvatarUrl() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getAvatarUrl() != null ? getAvatarUrl().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Identity{" +
                "name='" + name + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public Identity setName(String name) {
        this.name = name;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public Identity setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }
}
