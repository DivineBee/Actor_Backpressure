package utilities.user.analytics;

import java.util.UUID;

/**
 * @author Beatrice V.
 * @created 14.03.2021 - 11:30
 * @project ActorProg2
 */
public class UserWithAnalytics {
    private UUID id;
    private String user;
    private Double ratio;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    @Override
    public String toString() {
        return "UserWithAnalytics{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", ratio=" + ratio +
                '}';
    }

    //  check if record is full and if it can be transmitted further
    public boolean checkForIntegrity() {
        if (this.user != null && ratio != null) {
            return true;
        } else {
            return false;
        }
    }
}
