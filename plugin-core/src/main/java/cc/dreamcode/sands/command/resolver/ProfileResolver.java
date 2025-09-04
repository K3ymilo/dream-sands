package cc.dreamcode.sands.command.resolver;

import cc.dreamcode.command.resolver.transformer.ObjectTransformer;
import cc.dreamcode.sands.profile.Profile;
import cc.dreamcode.sands.profile.ProfileService;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ProfileResolver implements ObjectTransformer<Profile> {
    private final ProfileService profileService;


    public Class<?> getGeneric() {
        return Profile.class;
    }

    public Optional<Profile> transform(@NonNull Class<?> type, @NonNull String input) {
        return this.profileService.getProfile(input);
    }
}
