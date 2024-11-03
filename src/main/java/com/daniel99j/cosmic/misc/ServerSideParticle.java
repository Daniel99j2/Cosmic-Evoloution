package eu.pb4.ouch;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.ChunkAttachment;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import eu.pb4.polymer.virtualentity.api.elements.TextDisplayElement;
import net.minecraft.client.session.report.ReporterEnvironment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.Brightness;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;
import java.util.Optional;

public class ServerSideParticle extends ElementHolder {
    private final ItemDisplayElement display;
    private Vec3d velocity;
    private int timer;
    private List<Identifier> frames;
    private int frameTicks;
    private boolean fullbright;
    private boolean facePlayer;
    private float scale;
    private int currentFrame;

    private ServerSideParticle(List<Identifier> frames, int frameTicks, boolean fullbright, boolean facePlayer, float scale) {
        this.timer = frameTicks*frames.toArray().length;
        this.display = new ItemDisplayElement();
        this.display.setViewRange(0.3f);
        if(fullbright)
            this.display.setBrightness(new Brightness(15, 15));
        if(facePlayer)
            this.display.setBillboardMode(DisplayEntity.BillboardMode.CENTER);
        this.display.setScale(new Vector3f(scale));
        this.display.setTeleportDuration(2);
        this.frameTicks = frameTicks;
        this.fullbright = fullbright;
        this.facePlayer = facePlayer;
        this.scale = scale;
        this.addElement(display);
    }

    @Override
    protected void onTick() {
        super.onTick();

        if (this.timer-- == 0) {
            this.destroy();
            return;
        } else if (this.timer == 5) {
            this.display.setScale(new Vector3f(0));
            this.display.setInterpolationDuration(5);
            this.display.startInterpolation();
        }
        if (this.timer % frameTicks == 0) {
            currentFrame++;
        }
    }

    private static void create(ServerWorld world, Vec3d pos, List<Identifier> frames, int frameTicks, boolean fullbright, boolean facePlayer, float scale) {
        var model = new ServerSideParticle(frames, frameTicks, fullbright, facePlayer, scale);
        ChunkAttachment.ofTicking(model, world, pos);
    }
}