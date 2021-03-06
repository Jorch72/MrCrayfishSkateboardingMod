package com.mrcrayfish.skateboarding.block;

import java.util.List;

import com.mrcrayfish.skateboarding.MrCrayfishSkateboardingMod;
import com.mrcrayfish.skateboarding.block.attributes.Grindable;
import com.mrcrayfish.skateboarding.util.RotationHelper;
import com.mrcrayfish.skateboarding.util.StateHelper;
import com.mrcrayfish.skateboarding.util.StateHelper.RelativeFacing;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHandRail extends BlockObject implements Grindable
{
	public static final PropertyBool LEFT = PropertyBool.create("left");
	public static final PropertyBool RIGHT = PropertyBool.create("right");
	
	private static final AxisAlignedBB BOUNDING_BOX_ONE = RotationHelper.getBlockBounds(EnumFacing.NORTH, 0.375, 0.0, 0.0, 0.625, 16 * 0.0625, 1.0);
	private static final AxisAlignedBB BOUNDING_BOX_TWO = RotationHelper.getBlockBounds(EnumFacing.EAST, 0.375, 0.0, 0.0, 0.625, 16 * 0.0625, 1.0);
	private static final AxisAlignedBB[] BOUNDING_BOXES = { BOUNDING_BOX_ONE, BOUNDING_BOX_TWO };
	
	private static final AxisAlignedBB COLLISION_BOX_ONE = RotationHelper.getBlockBounds(EnumFacing.NORTH, 0.375, 0.0, 0.0, 0.625, 16 * 0.0625, 1.0);
	private static final AxisAlignedBB COLLISION_BOX_TWO = RotationHelper.getBlockBounds(EnumFacing.EAST, 0.375, 0.0, 0.0, 0.625, 16 * 0.0625, 1.0);
	private static final AxisAlignedBB[] COLLISION_BOXES = { COLLISION_BOX_ONE, COLLISION_BOX_TWO };
	
	public BlockHandRail(Material materialIn) 
	{
		super(materialIn);
		this.setUnlocalizedName("handrail");
		this.setRegistryName("handrail");
		this.setCreativeTab(MrCrayfishSkateboardingMod.skateTab);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(LEFT, false).withProperty(RIGHT, false));
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn) 
	{
		EnumFacing facing = state.getValue(FACING);
		super.addCollisionBoxToList(pos, entityBox, collidingBoxes, COLLISION_BOXES[facing.getHorizontalIndex() % 2]);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) 
	{
		EnumFacing facing = state.getValue(FACING);
		return BOUNDING_BOXES[facing.getHorizontalIndex() % 2];
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) 
	{
		EnumFacing facing = state.getValue(FACING);
		if(StateHelper.getRelativeBlock(worldIn, pos, facing, RelativeFacing.LEFT) == this)
		{
			RelativeFacing relativeFacing = StateHelper.getRelativeFacing(worldIn, pos, facing, RelativeFacing.LEFT);
			if(relativeFacing == RelativeFacing.OPPOSITE || relativeFacing == RelativeFacing.SAME)
			{
				state = state.withProperty(LEFT, true);
			}
		}
		if(StateHelper.getRelativeBlock(worldIn, pos, facing, RelativeFacing.RIGHT) == this)
		{
			RelativeFacing relativeFacing = StateHelper.getRelativeFacing(worldIn, pos, facing, RelativeFacing.RIGHT);
			if(relativeFacing == RelativeFacing.OPPOSITE || relativeFacing == RelativeFacing.SAME)
			{
				state = state.withProperty(RIGHT, true);
			}
		}
		return state;
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { FACING, LEFT, RIGHT });
	}

	@Override
	public boolean canGrind(World world, IBlockState state, BlockPos pos, double posX, double posY, double posZ) 
	{
		return posY >= 1.0;
	}
}
