package utilities;

import java.util.UUID;

/**
 * @author Beatrice V.
 * @created 08.03.2021 - 12:07
 * @project ActorProg2
 */
public class EmotionWithId {
    public UUID id;
    public int emotionScore;

    public EmotionWithId(UUID id, int emotionScore) {
        this.id = id;
        this.emotionScore = emotionScore;
    }

    public UUID getId() {
        return id;
    }

    public int getEmotionScore() {
        return emotionScore;
    }

    @Override
    public String toString() {
        return "EmotionWithId{" +
                "id=" + id +
                ", emotionScore=" + emotionScore +
                '}';
    }
}
