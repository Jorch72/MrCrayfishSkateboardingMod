package com.mrcrayfish.skateboarding.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntitySlope extends TileEntity 
{
	public boolean rail = false;
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		super.readFromNBT(compound);
		rail = compound.getBoolean("rail");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		compound.setBoolean("rail", rail);
		return super.writeToNBT(compound);
	}
}