package cc.dreamcode.sands.features.killstreak;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.SerializationData;
import eu.okaeri.configs.serdes.serializable.ConfigSerializable;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class KillStreakRewardWrapper implements ConfigSerializable {

    private int killStreak;
    private IKillStreakReward reward;

    @SuppressWarnings("unused")
    public static KillStreakRewardWrapper deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        if (data.containsKey("item-reward")) {
            return new KillStreakRewardWrapper(
                    data.get("kill-streak", Integer.class),
                    data.get("item-reward", KillStreakItemReward.class)
            );
        }

        if (data.containsKey("service-reward")) {
            return new KillStreakRewardWrapper(
                    data.get("kill-streak", Integer.class),
                    data.get("service-reward", KillStreakServiceReward.class)
            );
        }

        throw new IllegalArgumentException("Invalid reward type, must be 'item-reward' or 'service-reward'");
    }

    @Override
    public void serialize(@NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("kill-streak", this.killStreak);
        if (this.reward instanceof KillStreakItemReward) {
            data.add("item-reward", this.reward);
        } else if (this.reward instanceof KillStreakServiceReward) {
            data.add("service-reward", this.reward);
        }
    }
}