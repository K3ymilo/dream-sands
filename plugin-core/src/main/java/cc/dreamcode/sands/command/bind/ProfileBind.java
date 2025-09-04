package cc.dreamcode.sands.command.bind;

import cc.dreamcode.command.DreamSender;
import cc.dreamcode.command.bind.BindResolver;
import cc.dreamcode.sands.profile.Profile;
import cc.dreamcode.sands.profile.ProfileService;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ProfileBind implements BindResolver<Profile> {
    private final ProfileService profileService;

    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return Profile.class.isAssignableFrom(type);
    }

    @NotNull
    public Profile resolveBind(@NonNull DreamSender<?> sender) {
        Player player = (Player)sender.getHandler();
        return this.profileService.getProfileOrCreate(player);
    }
}
