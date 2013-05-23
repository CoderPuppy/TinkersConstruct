package mods.tinker.tconstruct.modifiers;

import java.util.Random;

import mods.tinker.tconstruct.common.TContent;
import mods.tinker.tconstruct.library.ActiveToolMod;
import mods.tinker.tconstruct.library.tools.AbilityHelper;
import mods.tinker.tconstruct.library.tools.HarvestTool;
import mods.tinker.tconstruct.library.tools.ToolCore;
import mods.tinker.tconstruct.library.tools.Weapon;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class TActiveOmniMod extends ActiveToolMod
{
	Random random = new Random();

	/* Updating */
	@Override
	public void updateTool (ToolCore tool, ItemStack stack, World world, Entity entity)
	{
		if (!world.isRemote && entity instanceof EntityLiving && !((EntityLiving) entity).isSwingInProgress)
		{
			NBTTagCompound tags = stack.getTagCompound().getCompoundTag("InfiTool");
			if (tags.hasKey("Moss"))
			{
				int chance = tags.getInteger("Moss");
				int check = world.canBlockSeeTheSky((int) entity.posX, (int) entity.posY, (int) entity.posZ) ? 750 : 1500;
				if (random.nextInt(check) < chance)
				{
					AbilityHelper.healTool(stack, 1, (EntityLiving) entity, true);
				}
			}
		}
	}

	/* Harvesting */
	@Override
	public boolean beforeBlockBreak (ToolCore tool, ItemStack stack, int x, int y, int z, EntityPlayer player)
	{
		if (player.capabilities.isCreativeMode)
			return false;
		
		if (tool instanceof HarvestTool)
			TContent.modL.midStreamModify(stack);

		NBTTagCompound tags = stack.getTagCompound().getCompoundTag("InfiTool");
		World world = player.worldObj;
		int bID = player.worldObj.getBlockId(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		Block block = Block.blocksList[bID];
		if (block == null || bID < 1 || bID > 4095)
			return false;

		if (tags.getBoolean("Lava") && block.quantityDropped(meta, 0, random) != 0)
		{
			ItemStack smeltStack = new ItemStack(block.idDropped(meta, random, 0), 1, block.damageDropped(meta));
			if (smeltStack.itemID < 0 || smeltStack.itemID >= 32000 || smeltStack.getItem() == null)
				return false;
			ItemStack result = FurnaceRecipes.smelting().getSmeltingResult(smeltStack);
			if (result != null)
			{
				world.setBlockToAir(x, y, z);
				if (!player.capabilities.isCreativeMode)
					tool.onBlockDestroyed(stack, world, bID, x, y, z, player);
				if (!world.isRemote)
				{
					ItemStack spawnme = result.copy();
					if (!(result.getItem() instanceof ItemBlock))
					{
						int loot = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack);
						if (loot > 0)
						{
							spawnme.stackSize *= (random.nextInt(loot + 1) + 1);
						}
					}
					EntityItem entityitem = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, spawnme);

					entityitem.delayBeforeCanPickup = 10;
					world.spawnEntityInWorld(entityitem);
					world.playAuxSFX(2001, x, y, z, bID + (meta << 12));
				}
				for (int i = 0; i < 6; i++)
				{
					float f = (float) x + random.nextFloat();
					float f1 = (float) y + random.nextFloat();
					float f2 = (float) z + random.nextFloat();
					float f3 = 0.52F;
					float f4 = random.nextFloat() * 0.6F - 0.3F;
					world.spawnParticle("smoke", f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
					world.spawnParticle("flame", f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);

					world.spawnParticle("smoke", f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
					world.spawnParticle("flame", f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);

					world.spawnParticle("smoke", f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
					world.spawnParticle("flame", f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);

					world.spawnParticle("smoke", f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
					world.spawnParticle("flame", f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
				}
				return true;
			}
		}

		return false;
	}

	/* Attacking */

	@Override
	public int baseAttackDamage (int earlyModDamage, int damage, ToolCore tool, ItemStack stack, EntityPlayer player, Entity entity)
	{
		if (tool instanceof Weapon)
			TContent.modL.midStreamModify(stack);
		return 0;
	}
}