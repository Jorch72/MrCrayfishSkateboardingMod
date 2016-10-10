package com.mrcrayfish.skateboarding.client.model.block.baked;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.mrcrayfish.skateboarding.block.BlockStair;
import com.mrcrayfish.skateboarding.util.QuadHelper;
import com.mrcrayfish.skateboarding.util.TransformationBuilder;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.property.IExtendedBlockState;

public class BakedModelStair implements IPerspectiveAwareModel 
{
	public static final ModelResourceLocation BAKED_MODEL = new ModelResourceLocation("csm:stair");
	
	private static final ImmutableMap<TransformType, Matrix4f> cameraTransformations;
	
	static
	{
		ImmutableMap.Builder<TransformType, Matrix4f> builder = ImmutableMap.builder();
		builder.put(TransformType.FIXED, new TransformationBuilder().setScale(0.5F).build().getMatrix());
		builder.put(TransformType.GUI, new TransformationBuilder().setTranslation(0.1F, 2, 0).setRotation(20, 135, 0).setScale(0.65F).build().getMatrix());
		builder.put(TransformType.GROUND, new TransformationBuilder().setTranslation(0, 1, 0).setScale(0.25F).build().getMatrix());
		builder.put(TransformType.FIRST_PERSON_LEFT_HAND, new TransformationBuilder().setTranslation(0, 4, 0).setRotation(0, -45, 0).setScale(0.4F).build().getMatrix());
		builder.put(TransformType.FIRST_PERSON_RIGHT_HAND, new TransformationBuilder().setTranslation(0, 4, 0).setRotation(0, 135, 0).setScale(0.4F).build().getMatrix());
		builder.put(TransformType.THIRD_PERSON_LEFT_HAND, new TransformationBuilder().setTranslation(0, 2.5F, 3.5F).setRotation(75, 315, 0).setScale(0.375F).build().getMatrix());
		builder.put(TransformType.THIRD_PERSON_RIGHT_HAND, new TransformationBuilder().setTranslation(0, 2.5F, 3.5F).setRotation(75, 135, 0).setScale(0.375F).build().getMatrix());
		cameraTransformations = builder.build();
	}
	
	private VertexFormat format;
	private TextureAtlasSprite mainTexture;
	
	public BakedModelStair(VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) 
	{
		this.format = format;
		this.mainTexture = bakedTextureGetter.apply(new ResourceLocation("minecraft", "blocks/hardened_clay"));
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) 
	{
		List<BakedQuad> quads = new ArrayList<BakedQuad>();
		QuadHelper helper = new QuadHelper(format, mainTexture);
		
		if(state != null)
		{
			IExtendedBlockState extendedState = (IExtendedBlockState) state;

			if(extendedState.getBlock() instanceof BlockHorizontal)
			{
				EnumFacing facing = state.getValue(BlockHorizontal.FACING);
				helper.setFacing(facing);
			}
			
			boolean stacked = extendedState.getValue(BlockStair.STACKED);
			String texture = extendedState.getValue(BlockStair.TEXTURE);
			
			TextureAtlasSprite main = getTexture(texture);
			if(main != null)
			{
				helper.setSprite(main);
			}
			
			if(stacked)
			{
				quads.addAll(helper.createCuboid(new Vec3d(0, 0, 0), new Vec3d(1, 0.75, 1)));
				quads.addAll(helper.createCuboid(new Vec3d(0.5, 0.75, 0), new Vec3d(1, 1, 1)));
			}
			else
			{
				quads.addAll(helper.createCuboid(new Vec3d(0, 0, 0), new Vec3d(1, 0.25, 1)));
				quads.addAll(helper.createCuboid(new Vec3d(0.5, 0.25, 0), new Vec3d(1, 0.5, 1)));
			}
		}
		else
		{
			quads.addAll(helper.createCuboid(new Vec3d(0, 0, 0), new Vec3d(1, 0.25, 1)));
			quads.addAll(helper.createCuboid(new Vec3d(0.5, 0.25, 0), new Vec3d(1, 0.5, 1)));
		}
		return quads;
	}
	
	public TextureAtlasSprite getTexture(String texture)
	{
		return Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(texture);
	}

	@Override
	public boolean isAmbientOcclusion() 
	{
		return true;
	}

	@Override
	public boolean isGui3d() 
	{
		return true;
	}

	@Override
	public boolean isBuiltInRenderer() 
	{
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() 
	{
		return mainTexture;
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() 
	{
		return ItemCameraTransforms.DEFAULT;
	}

	@Override
	public ItemOverrideList getOverrides() 
	{
		return new ItemOverrideList(Lists.<ItemOverride>newArrayList());
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) 
	{
		return Pair.of(this, cameraTransformations.get(cameraTransformType));
	}

}