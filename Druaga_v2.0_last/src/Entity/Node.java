package Entity;

import java.util.LinkedList;

import Map.Position;



public class Node implements Comparable {
	
	
	//マップの座標
	public Position pos;
	//スタート地点からのコスト
	public int costFromStart;
	//ゴールまでのヒューリスティックコスト
	public int heuristicCostToGoal;
	//親ノード
	public Node parentNode;
	
	
	public Node(Position pos){
		this.pos = pos;
	}
	
	
	
	//ヒューリスティックコストの計算
	public int getHeuristicCost(Node node){
		int m = node.pos.getX() - pos.getX();
		int n = node.pos.getY() - pos.getY();
		return (int)Math.sqrt(m*m+n*n);
	}

	
	//ノードが同じかどうかを調べる 同じならtrueを返す
	@Override
	public boolean equals(Object node){
		if(pos.getX() == ((Node) node).pos.getX() &&
		   pos.getY() == ((Node) node).pos.getY()){
			return true;
		}
		return false;
	}
	

	//ノードの大小の比較
	@Override
	public int compareTo(Object node) {
		
		int c1 = this.costFromStart + this.heuristicCostToGoal;
		int c2 = ((Node) node).costFromStart + ((Node) node).heuristicCostToGoal;
		
		if(c1 < c2) return -1;
		else if(c1 == c2) return 0;
		else return 1;
		
	}
	
	
	//隣接ノードのリストを返す　4方向
	public LinkedList getNeighbors(){
		
		LinkedList neighbors = new LinkedList();
		int x = pos.getX();
		int y = pos.getY();
		
		neighbors.add(new Node(new Position(x,y-1)));
		neighbors.add(new Node(new Position(x,y+1)));
		neighbors.add(new Node(new Position(x-1,y)));
		neighbors.add(new Node(new Position(x+1,y)));
		
		return neighbors;
		
	}
	
	

}
