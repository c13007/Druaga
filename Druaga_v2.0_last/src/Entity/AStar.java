package Entity;

import java.util.LinkedList;

import Map.Position;

public class AStar {
	
	
	private EntityManager em;
	private PriorityList openList;
	private LinkedList closedList;
	
	//内部クラス LinkedListを拡張して、add()を使うと自動的に小さい順に整列する。
	private class PriorityList extends LinkedList {
		public void add(Node node){
			for(int i=0;i<size();i++){
				if(node.compareTo(get(i)) <= 0){
					add(i,node);
					return;
				}
			}
			addLast(node);
		}	
	}
	
	
	public AStar(EntityManager em){
		this.em = em;
		this.openList = new PriorityList();
		this.closedList = new LinkedList();
	}
	
	
	//startPos : スタート位置
	//goalPos  : ゴール位置
	//return (LinkedList)path : A*のリスト
	public LinkedList searchPath(Position startPos,Position goalPos){
		
		int cnt = 0;
		
		int[] d = {Entity.UP,Entity.DOWN,Entity.LEFT,Entity.RIGHT};
		
		
		Node startNode = new Node(startPos);
		Node goalNode = new Node(goalPos);
		
		//スタートノードの設定
		startNode.costFromStart = 0;
		//startからgoalの直線距離
		startNode.heuristicCostToGoal = startNode.getHeuristicCost(goalNode);
		startNode.parentNode = null;
		
		//スタートノードをオープンリストに追加
		openList.add(startNode);
		
		//オープンリストがからになるまで回す
		while(!openList.isEmpty()){
			
			//最小コストノードを取り出す
			Node curNode = (Node)openList.removeFirst();
			
			//そのノードがゴールノードと一致しているか
			if(curNode.equals(goalNode)) return constructPath(curNode); //ゴールノードからパスを作成
			else{
				
				//現在のリストをクローズドリストに移す
				closedList.add(curNode);
				
				//現在のノードに隣接するノードを調べる
				LinkedList neighbors = curNode.getNeighbors();
				
				int count=0;
				for(int i=0;i<neighbors.size();i++){
					
					Node neighborNode = (Node)neighbors.get(i);
					
					//オープンリストに含まれているか
					boolean isOpen = openList.contains(neighborNode);
					//クローズドリストに含まれているか
					boolean isClosed = closedList.contains(neighborNode);
					//障害物でないか (後修正)
					boolean isHit = em.mm.getMap().isHit(neighborNode.pos.getX(), 
							  							 neighborNode.pos.getY(),d[count++]);
					
					if(!isOpen && !isClosed && !isHit){
						
						//
						/*
						neighborNode.costFromStart = curNode.costFromStart + ;
													 em.mm.getMap().getCost(neightborNode.pos);
						*/
						//オープンリストに移してコストを計算する
						
						//スタートからのコスト(costFromStart) :: 親のコスト + 地形コスト
						neighborNode.costFromStart = curNode.costFromStart + 1;
						//ヒューリスティックコスト
						neighborNode.heuristicCostToGoal = neighborNode.getHeuristicCost(goalNode);
						//親ノード
						neighborNode.parentNode = curNode;
						
						openList.add(neighborNode);
						
						
					}

				}

			}
	
		}
		
		openList.clear();
		closedList.clear();
		
		return null;
		
		
	}
	
	//パスの構築
	private LinkedList constructPath(Node node){
		
		LinkedList path = new LinkedList();
		
		//親のノードをたどる
		while(node.parentNode != null){
			//スタートノードがLinkedListの先頭に、ゴールノードが最後になるようにする
			path.addFirst(node);
			node = node.parentNode;
		}
		
		//スタートノードの追加
		path.addFirst(node);
		
		return path;
		
	}
	
	
	
	
	
}
