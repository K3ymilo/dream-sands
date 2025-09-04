package cc.dreamcode.sands.profile;

import eu.okaeri.configs.annotation.CustomKey;
import eu.okaeri.persistence.document.Document;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Profile extends Document {

    @CustomKey("name")
    private String name;
    private boolean online;

    private ProfileStatistics profileStatistics;

    @CustomKey("profile-dead")
    private boolean dead = false;


    public Profile() {
        this.profileStatistics = new ProfileStatistics();
    }

    public UUID getUniqueId() {
        return this.getPath().toUUID();
    }
}