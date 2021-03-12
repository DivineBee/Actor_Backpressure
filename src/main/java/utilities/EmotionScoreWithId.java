package utilities;

import java.util.UUID;

/**
 * @author Beatrice V.
 * @created 08.03.2021 - 12:07
 * @project ActorProg2
 */
public class EmotionScoreWithId {
    public UUID id;
    public int emotionScore;

    public EmotionScoreWithId(UUID id, int emotionScore) {
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
        return "EmotionScoreWithId{" +
                "id=" + id +
                ", emotionScore=" + emotionScore +
                '}';
    }
}
