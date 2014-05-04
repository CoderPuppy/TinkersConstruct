package tconstruct.items.accessory;

import java.util.List;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import tconstruct.client.TProxyClient;
import tconstruct.library.IAccessoryModel;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.tools.ToolCore;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TravelGlove extends AccessoryCore implements IAccessoryModel
{
    public TravelGlove()
    {
        super("armor/travel_glove");
    }

    @Override
    public boolean canEquipAccessory (ItemStack item, int slot)
    {
        return slot == 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void registerModifiers (IIconRegister iconRegister)
    {
        this.modifiers = new Icon[4];
        this.modifiers[0] = iconRegister.registerIcon("tinker:armor/glove_guard");
        this.modifiers[1] = iconRegister.registerIcon("tinker:armor/glove_speedaura");
        this.modifiers[2] = iconRegister.registerIcon("tinker:armor/glove_spines");
        this.modifiers[3] = iconRegister.registerIcon("tinker:armor/glove_sticky");
    }

    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel (EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot)
    {
        return TProxyClient.glove;
    }

    ResourceLocation texture = new ResourceLocation("tinker", "textures/armor/travel_1.png");

    @Override
    @SideOnly(Side.CLIENT)
    public ResourceLocation getWearbleTexture (Entity entity, ItemStack stack, int slot)
    {
        return texture;
    }
}
