package cc.dreamcode.sands.profile;

import eu.okaeri.persistence.repository.DocumentRepository;
import eu.okaeri.persistence.repository.annotation.DocumentCollection;
import lombok.NonNull;

import java.util.UUID;

@DocumentCollection(path = "profiles", keyLength = 36)
public interface ProfileRepository extends DocumentRepository<UUID, Profile> {

    // The default implementation of findOrCreateByPath is no longer needed here.
    // Okaeri handles this for you.

    default Profile findOrCreate(@NonNull UUID uuid, String profileName) {
        // Use the default findOrCreateByPath provided by Okaeri
        Profile profile = this.findOrCreateByPath(uuid);
        if (profileName != null) {
            profile.setName(profileName);
        }
        return profile;
    }
}