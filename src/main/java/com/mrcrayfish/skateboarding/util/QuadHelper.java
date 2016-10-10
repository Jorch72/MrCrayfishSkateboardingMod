package com.mrcrayfish.skateboarding.util;

import java.util.ArrayList;
import java.util.List;

import com.mrcrayfish.skateboarding.util.StateHelper.RelativeFacing;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;

public class QuadHelper 
{
	private VertexFormat format;
	private TextureAtlasSprite sprite;
	private EnumFacing facing = EnumFacing.EAST;
	
	public QuadHelper(VertexFormat format, TextureAtlasSprite sprite)
	{
		this.format = format;
		this.sprite = sprite;
	}
	
	public List<BakedQuad> createCuboid(Vec3d v1, Vec3d v2)
	{
		List<BakedQuad> quads = new ArrayList<BakedQuad>();
		
		float width = (float) (v2.xCoord - v1.xCoord);
		float height = (float) (v2.yCoord - v1.yCoord);
		float depth = (float) (v2.zCoord - v1.zCoord);
		
		// Front
		quads.add(createQuad(new Vertex(v2.xCoord, v2.yCoord, v1.zCoord, 0F, 0F), 
				             new Vertex(v2.xCoord, v1.yCoord, v1.zCoord, 0F, height), 
				             new Vertex(v1.xCoord, v1.yCoord, v1.zCoord, width, height), 
				             new Vertex(v1.xCoord, v2.yCoord, v1.zCoord, width, 0F),
	             	 		 EnumFacing.NORTH));
		
		// Back
		quads.add(createQuad(new Vertex(v1.xCoord, v2.yCoord, v2.zCoord, 0F, 0F), 
	             			 new Vertex(v1.xCoord, v1.yCoord, v2.zCoord, 0F, height), 
	             			 new Vertex(v2.xCoord, v1.yCoord, v2.zCoord, width, height), 
	             			 new Vertex(v2.xCoord, v2.yCoord, v2.zCoord, width, 0F),
	             	 		 EnumFacing.SOUTH));
		
		// Left
		quads.add(createQuad(new Vertex(v2.xCoord, v2.yCoord, v2.zCoord, 0F, 0F), 
	             		 	 new Vertex(v2.xCoord, v1.yCoord, v2.zCoord, 0F, height), 
	             		 	 new Vertex(v2.xCoord, v1.yCoord, v1.zCoord, depth, height), 
	             		 	 new Vertex(v2.xCoord, v2.yCoord, v1.zCoord, depth, 0F),
	             	 		 EnumFacing.WEST));
		
		// Right
		quads.add(createQuad(new Vertex(v1.xCoord, v2.yCoord, v1.zCoord, 0F, 0F), 
			    		 	 new Vertex(v1.xCoord, v1.yCoord, v1.zCoord, 0F, height), 
			    		 	 new Vertex(v1.xCoord, v1.yCoord, v2.zCoord, depth, height), 
			    		 	 new Vertex(v1.xCoord, v2.yCoord, v2.zCoord, depth, 0F),
			    	 		 EnumFacing.EAST));
		
		// Bottom
		quads.add(createQuad(new Vertex(v1.xCoord, v1.yCoord, v2.zCoord, 0F, 0F),
							 new Vertex(v1.xCoord, v1.yCoord, v1.zCoord, 0F, depth),
    	 		 			 new Vertex(v2.xCoord, v1.yCoord, v1.zCoord, width, depth), 
	             	 		 new Vertex(v2.xCoord, v1.yCoord, v2.zCoord, width, 0F),
	             	 		 EnumFacing.DOWN));
		
		// Top
		quads.add(createQuad(new Vertex(v1.xCoord, v2.yCoord, v1.zCoord, 0F, 0F), 
    	 		 			 new Vertex(v1.xCoord, v2.yCoord, v2.zCoord, 0F, depth),
    	 		 			 new Vertex(v2.xCoord, v2.yCoord, v2.zCoord, width, depth), 
	             	 		 new Vertex(v2.xCoord, v2.yCoord, v1.zCoord, width, 0F),
	             	 		 EnumFacing.UP));

        return quads;
	}
	
	public BakedQuad createQuad(Vertex v1, Vertex v2, Vertex v3, Vertex v4, EnumFacing face)
	{
		v1 = rotate(v1, facing);
		v2 = rotate(v2, facing);
		v3 = rotate(v3, facing);
		v4 = rotate(v4, facing);
		face = rotateFacing(face);
		
		Vec3d normal = v1.getVec3d().subtract(v2.getVec3d()).crossProduct(v3.getVec3d().subtract(v2.getVec3d()));

        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
        builder.setTexture(sprite);
        builder.setQuadOrientation(face);

        putVertex(builder, face, normal, v1.x, v1.y, v1.z, v1.u, v1.v);
        putVertex(builder, face, normal, v2.x, v2.y, v2.z, v2.u, v2.v);
        putVertex(builder, face, normal, v3.x, v3.y, v3.z, v3.u, v3.v);
        putVertex(builder, face, normal, v4.x, v4.y, v4.z, v4.u, v4.v);
        
        return builder.build();
	}
	
	private void putVertex(UnpackedBakedQuad.Builder builder, EnumFacing side, Vec3d normal, double x, double y, double z, float u, float v) 
	{
        for (int i = 0; i < format.getElementCount(); i++) 
        {
            switch (format.getElement(i).getUsage()) 
            {
                case POSITION:
                    builder.put(i, (float) x, (float) y, (float) z, 1.0F);
                    break;
                case COLOR:
                    builder.put(i, 1.0F, 1.0F, 1.0F, 1.0F);
                    break;
                case UV: if (format.getElement(i).getIndex() == 0) 
                {
                    u = sprite.getInterpolatedU(u);
                    v = sprite.getInterpolatedV(v);
                    builder.put(i, u, v, 0.0F, 1.0F);
                    break;
                }
                case NORMAL:
                    builder.put(i, (float) side.getFrontOffsetX(), (float) side.getFrontOffsetY(), (float) side.getFrontOffsetZ(), 0.0F);
                    break;
                default:
                    builder.put(i);
                    break;
            }
        }
    }
	
	public void setFacing(EnumFacing facing) 
	{
		if(facing == null) return;
		this.facing = facing;
	}
	
	public static class Vertex 
	{
		public double x;
		public double y;
		public double z;
		public float u;
		public float v;
		
		public Vertex(double x, double y, double z) 
		{
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		public Vertex(double x, double y, double z, float u, float v) 
		{
			this.x = x;
			this.y = y;
			this.z = z;
			this.u = u;
			this.v = v;
		}
		
		public Vec3d getVec3d() 
		{
			return new Vec3d(x, y, z);
		}
	}
	
	public static Vertex rotate(Vertex vertex, EnumFacing facing)
	{
		switch (facing)
		{
		case WEST:
			vertex.x = 1.0 - vertex.x;
			vertex.z = 1.0 - vertex.z;
			break;
		case NORTH:
			vertex.x = 1.0 - vertex.x;
			double temp_1 = vertex.x;
			vertex.x = vertex.z;
			vertex.z = temp_1;
			break;
		case SOUTH:
			vertex.z = 1.0 - vertex.z;
			double temp_2 = vertex.z;
			vertex.z = vertex.x;
			vertex.x = temp_2;
			break;
		default:
			break;
		}
		return vertex;
	}
	
	 /**
     * Get the index of this horizontal facing (0-3). The order is S0-W1-N2-E3
     */
	public EnumFacing rotateFacing(EnumFacing side)
	{
		if(side.getAxis() == Axis.Y) return side;
		
		switch(facing)
		{
		case NORTH:
			return side.rotateYCCW();
		case WEST:
			return side.getOpposite();
		case SOUTH:
			return side.rotateY();
		default:
			return side;
		}
	}
}
