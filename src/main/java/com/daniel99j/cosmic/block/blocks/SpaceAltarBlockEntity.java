//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.daniel99j.cosmic.block.blocks;

import com.daniel99j.cosmic.CosmicEvolution;
import com.daniel99j.cosmic.CosmicEvolutionArcanaThings;
import com.daniel99j.cosmic.gui.CosmicForgeGui;
import com.daniel99j.cosmic.gui.SpaceAltarGui;
import de.tomalbrc.sandstorm.Sandstorm;
import de.tomalbrc.sandstorm.util.ParticleUtil;
import eu.pb4.polymer.core.api.utils.PolymerObject;
import net.borisshoes.arcananovum.ArcanaNovum;
import net.borisshoes.arcananovum.ArcanaRegistry;
import net.borisshoes.arcananovum.augments.ArcanaAugment;
import net.borisshoes.arcananovum.augments.ArcanaAugments;
import net.borisshoes.arcananovum.blocks.forge.StarlightForge;
import net.borisshoes.arcananovum.core.ArcanaBlockEntity;
import net.borisshoes.arcananovum.core.ArcanaItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class SpaceAltarBlockEntity extends BlockEntity implements PolymerObject, ArcanaBlockEntity {
    private TreeMap<ArcanaAugment, Integer> augments;
    private String crafterId;
    private String uuid;
    private boolean synthetic;
    private String customName;
    private boolean seenForge;

    public SpaceAltarBlockEntity(BlockPos pos, BlockState state) {
        super(CosmicEvolutionArcanaThings.SPACE_ALTAR_BLOCK_ENTITY, pos, state);
        if(this.uuid == null)
            this.uuid = "invalid";
    }

    public void initialize(TreeMap<ArcanaAugment, Integer> augments, String crafterId, String uuid, boolean synthetic, @Nullable String customName) {
        this.augments = augments;
        this.crafterId = crafterId;
        this.uuid = uuid;
        this.synthetic = synthetic;
        this.customName = customName == null ? "" : customName;
    }

    public void openGui(ServerPlayerEntity player) {
        SpaceAltarGui gui = new SpaceAltarGui(player, this);
        gui.buildGui();
        gui.open();
    }

    public static <E extends BlockEntity> void ticker(World world, BlockPos blockPos, BlockState blockState, E e) {
        if (e instanceof SpaceAltarBlockEntity forge) {
            forge.tick();
        }
    }

    private void tick() {
        World var2 = this.world;
        if (var2 instanceof ServerWorld serverWorld) {
            if (serverWorld.getServer().getTicks() % 10 == 0) {
                this.seenForge = StarlightForge.findActiveForge(serverWorld, this.pos) != null;
            }

            if (serverWorld.getServer().getTicks() % 20 == 0 && this.seenForge) {
                ArcanaNovum.addActiveBlock(new Pair(this, this));
            }

            if (serverWorld.getServer().getTicks() % 36 == 0 && this.isAssembled()) {
                ParticleUtil.emit(Identifier.of(CosmicEvolution.MOD_ID, "space_altar_ambient"), (ServerWorld) this.getWorld(), Vec3d.of(this.getPos()), new Vector2f(0, 0));
            }
        }
    }

    public TreeMap<ArcanaAugment, Integer> getAugments() {
        return this.augments;
    }

    public String getCrafterId() {
        return this.crafterId;
    }

    public String getUuid() {
        return this.uuid;
    }

    public boolean isSynthetic() {
        return this.synthetic;
    }

    public String getCustomArcanaName() {
        return this.customName;
    }

    public ArcanaItem getArcanaItem() {
        return ArcanaRegistry.STARLIGHT_FORGE;
    }

    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        if (nbt.contains("arcanaUuid")) {
            this.uuid = nbt.getString("arcanaUuid");
        }

        if (nbt.contains("crafterId")) {
            this.crafterId = nbt.getString("crafterId");
        }

        if (nbt.contains("customName")) {
            this.customName = nbt.getString("customName");
        }

        if (nbt.contains("synthetic")) {
            this.synthetic = nbt.getBoolean("synthetic");
        }

        this.augments = new TreeMap();
        if (nbt.contains("arcanaAugments")) {
            NbtCompound augCompound = nbt.getCompound("arcanaAugments");
            Iterator var4 = augCompound.getKeys().iterator();

            while(var4.hasNext()) {
                String key = (String)var4.next();
                ArcanaAugment aug = (ArcanaAugment)ArcanaAugments.registry.get(key);
                if (aug != null) {
                    this.augments.put(aug, augCompound.getInt(key));
                }
            }
        }

    }

    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        if (this.augments != null) {
            NbtCompound augsCompound = new NbtCompound();
            Iterator var4 = this.augments.entrySet().iterator();

            while(var4.hasNext()) {
                Map.Entry<ArcanaAugment, Integer> entry = (Map.Entry)var4.next();
                augsCompound.putInt(((ArcanaAugment)entry.getKey()).id, (Integer)entry.getValue());
            }

            nbt.put("arcanaAugments", augsCompound);
        }

        if (this.uuid != null) {
            nbt.putString("arcanaUuid", this.uuid);
        }
        if (this.uuid == null) {
            nbt.putString("arcanaUuid", "invalid");
        }

        if (this.crafterId != null) {
            nbt.putString("crafterId", this.crafterId);
        }

        if (this.customName != null) {
            nbt.putString("customName", this.customName);
        }

        nbt.putBoolean("synthetic", this.synthetic);
    }
}
