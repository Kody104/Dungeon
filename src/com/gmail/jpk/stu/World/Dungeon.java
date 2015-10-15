package com.gmail.jpk.stu.World;

import java.util.Random;
import java.util.Scanner;

public class Dungeon {
	private Scanner scan = new Scanner(System.in);
	
	private int chanceroom;
	private int chancecorridor;
	private int objects;
	private int xmax;
	private int ymax;
	private int xsize;
	private int ysize;
	private tile[] dungeon_map;
	
	public Dungeon()
	{
		xmax = 80;
		ymax = 25;
		
		xsize = 0;
		ysize = 0;
		
		objects = 0;
		
		chanceroom = 75;
		chancecorridor = 25;
		
		mMain();
	}
	
	private void mMain()
	{
		int x = 80;
		int y = 25;
		int dungeon_objects = 100;
		dungeon_map = new tile[x * y];
		while(true)
		{
			String i = "";
			if(createDungeon(x, y, dungeon_objects))
			{
				showDungeon();
			}
			i = scan.nextLine();
			if(i.equalsIgnoreCase("exit"))
				return;
		}
	}
	enum tile {
		Unused, DirtWall, DirtFloor, StoneWall, Corridor, Door, UpStairs, DownStairs, Chest;
	}
	
	private void setCell(int x, int y, tile celltype)
	{
		dungeon_map[x + xsize * y] = celltype;
	}
	
	private tile getCell(int x, int y)
	{
		return dungeon_map[x + xsize * y];
	}
	
	private int getRandom(int min, int max)
	{
		Random r = new Random();
		
		int n = (max - min) + 1;
		int i = r.nextInt(n);
		
		if(i < 0)
			i = -1;
		return min + i;
	}
	
	private boolean makeCorridor(int x, int y, int length, int direction)
	{
		int len = getRandom(2, length);
		tile floor = tile.Corridor;
		int dir = 0;
		if(direction > 0 && direction < 4) dir = direction;
		
		int xtemp = 0;
		int ytemp = 0;
		
		switch(dir)
		{
			case 0:
	        {
	            if(x < 0 || x > xsize) return false;
	            else xtemp = x;
	
	            for(ytemp = y; ytemp > (y-len); ytemp--)
	            {
	                if(ytemp < 0 || ytemp > ysize) return false;
	                if(getCell(xtemp, ytemp) != tile.Unused) return false;
	            }
	
	            for(ytemp = y; ytemp > (y - len); ytemp--)
	            {
	                setCell(xtemp, ytemp, floor);
	            }
	            break;
	
	        }
	        case 1:
	        {
	            if(y < 0 || y > ysize) return false;
	            else ytemp = y;
	
	            for(xtemp = x; xtemp < (x + len); xtemp++)
	            {
	                if(xtemp < 0 || xtemp > xsize) return false;
	                if(getCell(xtemp, ytemp) != tile.Unused) return false;
	            }
	
	            for(xtemp = x; xtemp < (x + len); xtemp++)
	            {
	                setCell(xtemp, ytemp, floor);
	            }
	            break;
	        }
	        case 2:
	        {
	            if(x < 0 || x > xsize) return false;
	            else xtemp = x;
	
	            for(ytemp = y; ytemp < (y + len); ytemp++)
	            {
	                if(ytemp < 0 || ytemp > ysize) return false;
	                if(getCell(xtemp, ytemp) != tile.Unused) return false;
	            }
	            for (ytemp = y; ytemp < (y+len); ytemp++){
	                setCell(xtemp, ytemp, floor);
	            }
			break;
	        }
	        case 3:
	        {
	            if (ytemp < 0 || ytemp > ysize) return false;
	            else ytemp = y;
	
	            for (xtemp = x; xtemp > (x-len); xtemp--){
	                if (xtemp < 0 || xtemp > xsize) return false;
	                if (getCell(xtemp, ytemp) != tile.Unused) return false;
	            }
	
	            for (xtemp = x; xtemp > (x-len); xtemp--){
	                setCell(xtemp, ytemp, floor);
	            }
	            break;
	        }
		}
		
		//still here, let's tell the others we're done
		return true;
	}
	
	private boolean makeRoom(int x, int y, int xlength, int ylength, int direction)
	{
		//define the dimensions of the room, it should be atleast 4x4 (2x2 to walk on)
		int xlen = getRandom(4, xlength);
		int ylen = getRandom(4, ylength);
		//the type of tile it will be filled with
		tile floor = tile.DirtFloor;
		tile wall = tile.DirtWall;
		//choose the way it's pointing
		int dir = 0;
		if(direction > 0 && direction < 4) dir = direction;
		
		switch(dir)
		{
			case 0:
			{
			//north
				//Check if there's enough space for it
				for(int ytemp = y; ytemp > (y-ylen);ytemp--)
				{
					if(ytemp == 0 || ytemp > ysize) return false;
					for(int xtemp = (x-(xlen/2)); xtemp < (x+(xlen+1)/2); xtemp++)
					{
						if(xtemp == 0 || xtemp > xsize) return false;
						if(getCell(xtemp, ytemp) != tile.Unused) return false; //no space left...
					}
				}
				
				//we're still here, build
				for (int ytemp = y; ytemp > (y-ylen); ytemp--)
				{
					for (int xtemp = (x-xlen/2); xtemp < (x+(xlen+1)/2); xtemp++)
					{
						//start with the walls
						if (xtemp == (x-(xlen/2))) setCell(xtemp, ytemp, wall);
						else if (xtemp == (x+(xlen-1)/2)) setCell(xtemp, ytemp, wall);
						else if (ytemp == y) setCell(xtemp, ytemp, wall);
						else if (ytemp == (y-ylen+1)) setCell(xtemp, ytemp, wall);
						//and then fill with the floor
						else setCell(xtemp, ytemp, floor);
					}
				}
				break;
			}
			case 1:
			{
			//east
				for(int ytemp = (y-(ylen/2)); ytemp < (y+(ylen+1)/2); ytemp++)
				{
					if(ytemp < 0 || ytemp > ysize) return false;
					for(int xtemp = x; xtemp < (x+xlen); xtemp++)
					{
						if(xtemp < 0 || xtemp > xsize) return false;
						if(getCell(xtemp, ytemp) != tile.Unused) return false;
					}
				}
				for (int ytemp = (y-(ylen/2)); ytemp < (y+(ylen+1)/2); ytemp++)
				{
					for (int xtemp = x; xtemp < (x+xlen); xtemp++)
					{
						if (xtemp == x) setCell(xtemp, ytemp, wall);
						else if (xtemp == (x+xlen-1)) setCell(xtemp, ytemp, wall);
						else if (ytemp == (y-(ylen/2))) setCell(xtemp, ytemp, wall);
						else if (ytemp == (y+(ylen-1)/2)) setCell(xtemp, ytemp, wall);
	 
						else setCell(xtemp, ytemp, floor);
					}
				}
				break;
			}
			case 2:
			{
			//south
				for (int ytemp = y; ytemp < (y+ylen); ytemp++)
				{
					if (ytemp < 0 || ytemp > ysize) return false;
					for (int xtemp = (x-xlen/2); xtemp < (x+(xlen+1)/2); xtemp++){
						if (xtemp < 0 || xtemp > xsize) return false;
						if (getCell(xtemp, ytemp) != tile.Unused) return false;
					}
				}
				for (int ytemp = y; ytemp < (y+ylen); ytemp++)
				{
					for (int xtemp = (x-xlen/2); xtemp < (x+(xlen+1)/2); xtemp++)
					{
						if (xtemp == (x-xlen/2)) setCell(xtemp, ytemp, wall);
						else if (xtemp == (x+(xlen-1)/2)) setCell(xtemp, ytemp, wall);
						else if (ytemp == y) setCell(xtemp, ytemp, wall);
						else if (ytemp == (y+ylen-1)) setCell(xtemp, ytemp, wall);
	 
						else setCell(xtemp, ytemp, floor);
					}
				}
				break;
			}
			case 3:
			{
			//west
				for (int ytemp = (y-ylen/2); ytemp < (y+(ylen+1)/2); ytemp++)
				{
					if (ytemp < 0 || ytemp > ysize) return false;
					for (int xtemp = x; xtemp > (x-xlen); xtemp--)
					{
						if (xtemp < 0 || xtemp > xsize) return false;
						if (getCell(xtemp, ytemp) != tile.Unused) return false;
					}
				}
	 
				for (int ytemp = (y-ylen/2); ytemp < (y+(ylen+1)/2); ytemp++)
				{
					for (int xtemp = x; xtemp > (x-xlen); xtemp--)
					{
						if (xtemp == x) setCell(xtemp, ytemp, wall);
						else if (xtemp == (x-xlen+1)) setCell(xtemp, ytemp, wall);
						else if (ytemp == (y-ylen/2)) setCell(xtemp, ytemp, wall);
						else if (ytemp == (y+(ylen-1)/2)) setCell(xtemp, ytemp, wall);
	 
						else setCell(xtemp, ytemp, floor);
					}
				}
				break;
			}
		}
		
		//We're all done
		return true;
	}
	
	private boolean createDungeon(int inx, int iny, int inobj)
	{
		if(inobj < 1) objects = 10;
		else objects = inobj;
		
		//adjust the size of the map, so it fits our limits
		if(inx < 3) xsize = 3;
		else xsize = inx;
		
		if(iny < 3) ysize = 3;
		else ysize = iny;
		
		//redefine the map var, so it's adjusted for our new map
		dungeon_map = new tile[xsize * ysize];
		
		//start with making the "standard" stuff on our map
		for(int y = 0; y < ysize; y++)
		{
			for(int x = 0; x < xsize; x++)
			{
				//ie, making the borders of unwalkable walls
				if(y == 0) setCell(x, y, tile.StoneWall);
				else if(y == ysize -1) setCell(x, y, tile.StoneWall);
				else if(x == 0) setCell(x, y, tile.StoneWall);
				else if(x == xsize -1) setCell(x, y, tile.StoneWall);
				
				else setCell(x, y, tile.Unused);
			}
		}
		
		//start with making a room in the middle, which we can start building upon
		makeRoom(xsize/2, ysize/2, 8, 6, getRandom(0, 3));
		
		//keep count of the number of "objects" we've made
		int currentFeatures = 1; //+1 for the room we just made
		
		
		//then we start the main loop
		for(int countingTries = 0; countingTries < 1000; countingTries++)
		{
			//check if we've reached our quota
			if(currentFeatures == objects) break;
			
			//start with a random wall
			int newx = 0;
			int xmod = 0;
			int newy = 0;
			int ymod = 0;
			int validTile = -1;
			//1000 chances to find a suitable object (room or corridor)..
			
			for(int testing = 0; testing < 1000; testing++)
			{
				newx = getRandom(1, xsize-1);
				newy = getRandom(1, ysize-1);
				validTile = -1;
				if(getCell(newx,newy) == tile.DirtWall || getCell(newx,newy) == tile.Corridor)
				{
					//check if we can reach the place
					if(getCell(newx, newy+1) == tile.DirtFloor || getCell(newx, newy+1) == tile.Corridor)
					{
						validTile = 0;
						xmod = 0;
						ymod = -1;
					}
					else if(getCell(newx-1, newy) == tile.DirtFloor || getCell(newx-1, newy) == tile.Corridor)
					{
						validTile = 1;
						xmod = +1;
						ymod = 0;
					}
					else if(getCell(newx, newy-1) == tile.DirtFloor || getCell(newx, newy-1) == tile.Corridor)
					{
						validTile = 2;
						xmod = 0;
						ymod = +1;
					}
					else if(getCell(newx+1, newy) == tile.DirtFloor || getCell(newx+1, newy) == tile.Corridor)
					{
						validTile = 3;
						xmod = -1;
						ymod = 0;
					}
				
				//check that we don't have any other opening nearby
				if(validTile > -1)
				{
					if(getCell(newx, newy+1) == tile.Door) // north
						validTile = -1;
					else if (getCell(newx-1, newy) == tile.Door)//east
						validTile = -1;
					else if (getCell(newx, newy-1) == tile.Door)//south
						validTile = -1;
					else if (getCell(newx+1, newy) == tile.Door)//west
						validTile = -1;
				}
				
				//if we can, jump out of the loop and continue with the rest
				if (validTile > -1) break;
				
				}
			}
			if (validTile > -1)
			{
				//choose what to build now at our newly found place, and at what direction
				int feature = getRandom(0, 100);
				if (feature <= chanceroom)
				{ 
					//a new room
					if (makeRoom((newx+xmod), (newy+ymod), 8, 6, validTile))
					{
						currentFeatures++; //add to our quota
 
						//then we mark the wall opening with a door
						setCell(newx, newy, tile.Door);
 
						//clean up infront of the door so we can reach it
						setCell((newx+xmod), (newy+ymod), tile.DirtFloor);
					}
				}
				else if (feature >= chanceroom)
				{
					//new corridor
					if (makeCorridor((newx+xmod), (newy+ymod), 6, validTile))
					{
						//same thing here, add to the quota and a door
						currentFeatures++;
						
						setCell(newx, newy, tile.Door);
					}
				}
			}
		}
		
		//sprinkle out the bonusstuff (stairs, chests etc.) over the map
				int newx = 0;
				int newy = 0;
				int ways = 0; //from how many directions we can reach the random spot from
				int state = 0; //the state the loop is in, start with the stairs
				while (state != 10){
					for (int testing = 0; testing < 1000; testing++)
					{
						newx = getRandom(1, xsize-1);
						newy = getRandom(1, ysize-2); //cheap bugfix, pulls down newy to 0<y<24, from 0<y<25
		 
						//System.out.println("x: " + newx + "\ty: " + newy);
						ways = 4; //the lower the better
		 
						//check if we can reach the spot
						if (getCell(newx, newy+1) == tile.DirtFloor || getCell(newx, newy+1) == tile.Corridor)
						{
						//north
							if (getCell(newx, newy+1) != tile.Door)
							ways--;
						}
						if (getCell(newx-1, newy) == tile.DirtFloor || getCell(newx-1, newy) == tile.Corridor)
						{
						//east
							if (getCell(newx-1, newy) != tile.Door)
							ways--;
						}
						if (getCell(newx, newy-1) == tile.DirtFloor || getCell(newx, newy-1) == tile.Corridor)
						{
						//south
							if (getCell(newx, newy-1) != tile.Door)
							ways--;
						}
						if (getCell(newx+1, newy) == tile.DirtFloor || getCell(newx+1, newy) == tile.Corridor)
						{
						//west
							if (getCell(newx+1, newy) != tile.Door)
							ways--;
						}
		 
						if (state == 0)
						{
							if (ways == 0)
							{
							//we're in state 0, let's place a "upstairs" thing
								setCell(newx, newy, tile.UpStairs);
								state = 1;
								break;
							}
						}
						else if (state == 1)
						{
							if (ways == 0)
							{
							//state 1, place a "downstairs"
								setCell(newx, newy, tile.DownStairs);
								state = 2;
								break;
							}
						}
						else if(state == 2)
						{
							if(ways <= 1)
							{
								setCell(newx, newy, tile.Chest);
								state = 10;
								break;
							}
						}
					}
				}
		 
		
		return true;
	}
	
	private void showDungeon()
	{
		for(int y = 0; y < ysize; y++)
		{
			for(int x = 0; x < xsize; x++)
			{
				switch(getCell(x, y))
				{
					default:
					{
					
					}
					case Unused:
					{
						System.out.print(" ");
						break;
					}
					case StoneWall:
					{
						System.out.print("X");
						break;
					}
					case DirtFloor:
					{
						System.out.print(".");
						break;
					}
					case DirtWall:
					{
						System.out.print("#");
						break;
					}
					case Corridor:
					{
						System.out.print(".");
						break;
					}
					case Door:
					{
						System.out.print("+");
						break;
					}
					case UpStairs:
					{
						System.out.print("<");
						break;
					}
					case DownStairs:
					{
						System.out.print(">");
						break;
					}
					case Chest:
					{
						System.out.print("*");
						break;
					}
				}
			}
		}
	}
}
