package cn.ksmcbrigade.MCMODWIKI.mixin;

import cn.ksmcbrigade.MCMODWIKI.McModSearchRebornAgain;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public abstract class GUIKeyDownMixin<T extends AbstractContainerMenu> extends Screen implements MenuAccess<T> {

    @Shadow @javax.annotation.Nullable protected Slot hoveredSlot;

    protected GUIKeyDownMixin(Component p_96550_) {
        super(p_96550_);
    }

    @Inject(method = "keyPressed", at = @At("TAIL"))
    public void OnKeyInput(int p_97765_, int p_97766_, int p_97767_, CallbackInfoReturnable<Boolean> cir){
        if(p_97765_==McModSearchRebornAgain.key.getKey().getValue()){
            if(this.hoveredSlot != null) {
                if(!McModSearchRebornAgain.open(this.hoveredSlot.getItem())){
                    McModSearchRebornAgain.LOGGER.error("Can't open the item's url.");
                }
            }
        }
    }
}
