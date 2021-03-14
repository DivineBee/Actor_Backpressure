package utilities.user.analytics;

import java.util.UUID;

/**
 * @author Beatrice V.
 * @created 14.03.2021 - 11:26
 * @project ActorProg2
 */
public class UserWithId {
    private UUID id;
    private String user;
    private int friendsCount;
    private int followersCount;
    private int statusesCount;

    public UserWithId(String user, int friendsCount, int followersCount, int statusesCount) throws Exception {
        if (!user.isEmpty()) {
            this.user = user;
            this.id = UUID.randomUUID();
            this.friendsCount = friendsCount;
            this.followersCount = followersCount;
            this.statusesCount = statusesCount;
        } else {
            throw new Exception("User is empty or no user attached");
        }
    }

    public UUID getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getStatusesCount() {
        return statusesCount;
    }

    @Override
    public String toString() {
        return "UserWithId{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", friendsCount=" + friendsCount +
                ", followersCount=" + followersCount +
                ", statusesCount=" + statusesCount +
                '}';
    }
}
