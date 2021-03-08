package utilities;

import java.util.UUID;

/**
 * @author Beatrice V.
 * @created 08.03.2021 - 12:10
 * @project ActorProg2
 */
public class RatioWithId {
    public UUID id;
    public double ratio;


    public RatioWithId(UUID id, double ratio) {
        this.id = id;
        this.ratio = ratio;
    }

    public UUID getId() {
        return id;
    }

    public double getRatio() {
        return ratio;
    }

    @Override
    public String toString() {
        return "RatioWithId{" +
                "id=" + id +
                ", ratio=" + ratio +
                '}';
    }
}
