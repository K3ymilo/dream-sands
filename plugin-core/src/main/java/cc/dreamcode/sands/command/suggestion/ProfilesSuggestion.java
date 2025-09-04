package cc.dreamcode.sands.command.suggestion;

import cc.dreamcode.command.suggestion.supplier.SuggestionSupplier;
import cc.dreamcode.platform.other.component.annotation.SuggestionKey;
import cc.dreamcode.sands.profile.ProfileService;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor(onConstructor_ = @Inject)
@SuggestionKey("@profiles")
public class ProfilesSuggestion implements SuggestionSupplier {

    private final ProfileService profileService;


    @Override
    public List<String> supply(@NonNull Class<?> paramType) {
        final List<String> names = this.profileService.getOnlineNames();
        return names;
    }
}