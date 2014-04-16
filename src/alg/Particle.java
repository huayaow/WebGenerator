package alg;

import java.util.Random;

/*
 * Particle: 粒子初始化，历史最优位置的适应值及更新，速度更新，位置更新
 */
public class Particle implements Cloneable {
	// 粒子维度及取值范围
	private int pnum ;
	private int[] vnum ;
	// 粒子更新参数
	private Configuration config ;
	// 当前位置和速度
	private int[] position ;
	private double[] velocity ;
	// 历史上最好位置及适应值
	private int[] pBest ;
	private int fitness ;

	public Particle(int p, int[] v, Configuration c) {
		this.pnum = p ;
		this.vnum = v ;
		this.config = c ;
		position = new int[p] ;
		velocity = new double[p] ;
		pBest = new int[p] ;
	}

	// clone
	public Particle clone() throws CloneNotSupportedException {  
		 Particle p = null ;
		 try {
		 p = (Particle)super.clone();  
		 p.vnum=(int[])vnum.clone();
		 p.position=(int[])position.clone();
		 p.velocity=(double[])velocity.clone();
		 p.pBest=(int[])pBest.clone();	 
		 } catch(CloneNotSupportedException e) {

		 }
		 return p ;
	}

	// 返回当前粒子位置, new[] p
	public int[] getPosition() {
		int[] p = new int[pnum] ;
		for(int k=0;k<pnum;k++)
			p[k] = position[k] ;
		return p ;
	}

	// 设置当前粒子位置
	public void setPosition(int[] ps) {
		for(int i=0; i<pnum; i++) {
			this.position[i] = ps[i] ;
		}
	}

	// 返回适应值
	public int getFitness() {
		return fitness ;
	}

	// 设置适应值
	public void setFitness(int ft) {
		this.fitness = ft ;
	}

	// 随机初始化粒子
	public void RandomInit() {
		Random random = new Random();
		for(int i=0; i<pnum; i++) {
			position[i] = Math.abs(random.nextInt())%vnum[i] ;
			pBest[i] = position[i] ;
			velocity[i] = 2 * random.nextDouble() - 1 ;
			//System.out.println(position[i]+" "+velocity[i]);
		}
		fitness = 0 ;
	}

	// 使用当前位置更新历史最优位置
	public void UpdatePBest() {
		for(int i=0; i<pnum; i++) {
			pBest[i] = position[i] ; 
		}
	}

	// 粒子速度更新 v(k+1)
	public void UpdateVelocity(int[] gBest) {
		// standard PSO
		// v(k+1) = w*v(k) + c*r1*pBest(k) + c*r2*gBest(k)
		Random random = new Random();
		for(int i=0; i<pnum; i++) {
			double r1 = random.nextDouble() ;
			double r2 = random.nextDouble() ;
			velocity[i] = config.pso_weight * (double)velocity[i] + 
					config.pso_factor * r1 * (double)(pBest[i] - position[i]) +
					config.pso_factor * r2 * (double)(gBest[i] - position[i]) ;

			// 最大速度处理
			if( velocity[i] > (double)vnum[i]/2 ) {
				velocity[i] = (double)vnum[i]/2 ;
			}
			if( velocity[i] < -(double)vnum[i]/2 ) {
				velocity[i] = -(double)vnum[i]/2 ;
			}
		}
	}

	// 粒子位置更新 p(k+1) = p(k) + v(k)
	public void UpdatePosition() {
		for(int i=0; i<pnum; i++) {
			position[i] = position[i] + (int)velocity[i] ;

			//边界处理
			if( position[i] >= vnum[i] ) {
				position[i] = 2*vnum[i]-position[i]-1 ;
			}
			if( position[i] < 0 ) {
				position[i] = -position[i]-1 ;
			}

		}
	}

}