package cc.dreamcode.sands.profile;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.injector.annotation.Inject;
import lombok.*;

@Getter
@RequiredArgsConstructor(onConstructor_ = @Inject)
@EqualsAndHashCode(callSuper = false)
public class ProfileStatistics extends OkaeriConfig {

    private int killstreak = 0;

    public void addKill() {
        this.killstreak++;
    }

    public void resetKillstreak() {
        this.killstreak = 0;
    }

}
