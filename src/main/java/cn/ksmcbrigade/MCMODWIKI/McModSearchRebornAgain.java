package cn.ksmcbrigade.MCMODWIKI;

import net.minecraft.client.KeyMapping;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Mod("mcmodwiki")
@Mod.EventBusSubscriber
public class McModSearchRebornAgain {

    public static Logger LOGGER = LogManager.getLogger();
    public static KeyMapping key = new KeyMapping("key.open", GLFW.GLFW_KEY_HOME, "key.gui.search");

    public McModSearchRebornAgain() {
        MinecraftForge.EVENT_BUS.register(this);
        if(Boolean.parseBoolean(System.getProperty("java.awt.headless"))){
            System.setProperty("java.awt.headless","false");
        }
        LOGGER.info("MCMOD Search Reborn Again mod loaded.");
    }

    @SubscribeEvent
    public static void RegisterKey(RegisterKeyMappingsEvent event){
        event.register(key);
    }

    public static boolean open(ItemStack stack){
        String modName, regName, displayName, url;
        int mcModApiNum;
        try {
            modName = URLEncoder.encode(Objects.requireNonNull(stack.getItem().getCreatorModId(stack)), StandardCharsets.UTF_8);
            regName = URLEncoder.encode(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(stack.getItem())).toString(), StandardCharsets.UTF_8);
            displayName = URLEncoder.encode(stack.getDisplayName().getString(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try {
            final var apiUrl = new URL(String.format("https://api.mcmod.cn/getItem/?regname=%s", regName));
            mcModApiNum = Integer.parseInt(IOUtils.readLines(apiUrl.openStream(), StandardCharsets.UTF_8).get(0));
        } catch (Exception e) {
            LOGGER.error("Failed to summon the url.");
            e.printStackTrace();
            return false;
        }
        url = mcModApiNum > 0 ? String.format("https://www.mcmod.cn/item/%d.html", mcModApiNum) : String.format("https://search.mcmod.cn/s?key=%s+%s", modName, displayName);
        try {
            if(Desktop.isDesktopSupported() || System.getProperty("os.name").contains("Windows")){
                //Windows
                Desktop.getDesktop().browse(new URI(url));
            }
            else{
                Runtime runtime = Runtime.getRuntime();
                if(System.getProperty("os.name").contains("Mac")){
                    //Mac
                    runtime.exec(new String[]{"xdg-open","\""+url+"\""});
                }
                else{
                    runtime.exec(new String[]{"xdg-open",url});
                }
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("Failed to open the url.");
            e.printStackTrace();
            return false;
        }
    }
}
