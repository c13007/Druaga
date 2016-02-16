package Entity;

import java.awt.Point;

import Map.Position;

public class LOS {
	
	
	private final int MAX_PATH_LENGTH = 256;
	
	private EntityManager em;
	private Position position;
	private Position[] path;
	private int currentStep;
	
	
	public LOS(EntityManager em,int x,int y){
		position = new Position(x,y);
		path = new Position[MAX_PATH_LENGTH];
	}
	
	
	public void buildPathTo(int plx,int ply,int elx,int ely){
		
		this.currentStep = 1;
		
		if(elx == plx && ely == ply){
			return ;
		}
		
		int nextX = elx;
		int nextY = ely;
		int deltaX = plx - elx;
		int deltaY = ply - ely;
		int stepX,stepY;
		int step;
		int fraction;
		
		
		for(step=0;step<MAX_PATH_LENGTH;step++){
			path[step] = null;
		}
		
		step = 0;
		
		if(deltaX < 0) stepX = -1; else stepX = 1;
		if(deltaY < 0) stepY = -1; else stepY = 1;
		
		
		deltaX = Math.abs(deltaX*2);
		deltaY = Math.abs(deltaY*2);
		
		
		path[step] = new Position(nextX,nextY);
		step++;
		
		
		if(deltaX > deltaY){
			fraction = deltaY - deltaX/2;
			while(nextX != plx){
				if(fraction >= 0){
					nextY += stepY;
					fraction -= deltaX;
				}
				nextX += stepX;
				fraction += deltaY;
				path[step] = new Position(nextX,nextY);
				step++;
			}
		}else{
			fraction = deltaX - deltaY/2;
			while(nextY != ply){
				if(fraction >= 0){
					nextX += stepX;
					fraction -= deltaY;
				}
				nextY += stepY;
				fraction += deltaX;
				path[step] = new Position(nextX,nextY);
				step++;
			}
			
		}
		
		
	}
	
	public Position chaseByLOS(){
		if(path[this.currentStep] == null) return null;
		else{
			Position p = new Position(path[currentStep].getX(),path[currentStep].getY());
			this.currentStep++;
			return p;
		}
		
	}
	
	

}
