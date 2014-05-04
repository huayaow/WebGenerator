package alg;

import java.util.ArrayList;
import alg.Combinations;

public class StandardPSO {
	// 最终覆盖表
	public ArrayList<int[]> CoverArray ;
	// 生成规模及时间
	public int psog_size ;      // 最小规模
	public long psog_time ;      // 总计时间

	// 测试用例覆盖组合数，累加
	public ArrayList<Integer> CovNum ;
	public int CovMax ;

	// 生成参数
	private int parameter ;
	private int[] value ;
	private int tway ;
    private ArrayList<int[]> seed ;
	private Configuration config ;
	private Combinations comb ;

	public StandardPSO(int p, int[] v, int t, Configuration c) {
		parameter = p ;
		value = v ;
		tway = t ;
		config = c;
		comb = new Combinations(parameter,value,tway);
		CoverArray = new ArrayList<int[]>() ;
		CovNum = new ArrayList<Integer>() ;
        seed = new ArrayList<int[]>() ;
	}

	public ArrayList<int[]> getCoverArray() {
		return CoverArray ;
	}
	public int getSize() {
		return psog_size ;
	}
	public long getTime() {
		return psog_time ;
	}
	public ArrayList<Integer> getCovNum() {
		return CovNum ;
	}


    /*
     * 初始化，清空CoverArray和comb
     */
    public void initialization() {
        CoverArray.clear();
        comb.GenerateS();
        CovMax = comb.getSCountAll() ;
        // cover seed's t-way combinations
        for( int k=0 ; k<seed.size() ; k++ ) {
            int[] tp = seed.get(k) ;
            comb.FitnessValue(tp, 1);
        }
    }

    /*
     * 使用PSO生成一条测试用例已覆盖最多的未覆盖t-way组合
     * 若此时全部组合已覆盖，返回null，否则返回测试用例int[]
     */
    public int[] PSOGenOne() throws CloneNotSupportedException {
        if ( comb.getSCount() == 0 ) {
            return null ;
        }
        else {
            return Evolve() ;
        }
    }

    /*
     * 对seed进行预处理：将seed中内容存入ArrayList<int[]> CoverArray中，并标记已对应组合为已覆盖
     */
    public void SetSeed( ArrayList<int[]> ss ) {
        for(int i=0; i<ss.size(); i++) {
            int[] tp = ss.get(i);
            // add to seed
            seed.add(tp);
        }
    }

	// One-test-at-a-time
	public void PSOEvolve() {
		psog_size = 999999 ;
		psog_time = 0 ;

		// 重复
		for(int count=0; count<config.pso_repeat; count++) {
			ArrayList<int[]> tpCA = new ArrayList<int[]>();
			ArrayList<Integer> tpCN = new ArrayList<Integer>();
			int tpc = 0 ;

			int tpsize = -1 ;
			long tptime = -1 ;

			long time_st = System.currentTimeMillis();  

			// 初始化所有未覆盖组合对
            initialization();

			// 逐一生成测试用例
			while( comb.getSCount() != 0 )
			{
				int[] best = new int[parameter] ;
				try {
					best = Evolve();
				} catch(CloneNotSupportedException e) {

				}		
				tpCA.add( best );
				int cov = comb.FitnessValue( best , 1 );
				tpc = tpc + cov ;
				tpCN.add( tpc );
				/*
				for(int k=0; k<parameter; k++)
					System.out.print(best[k]+" ");
				System.out.print(" fit: "+ cov + " , SCount: " + comb.getSCount());
				System.out.print("\r\n");*/
			}

			long time_fi = System.currentTimeMillis();  

			tpsize = tpCA.size() ;
			tptime = time_fi- time_st ;
			psog_time += tptime ;
			//System.out.println( count + " size: " + tpsize + " time: " + tptime);

			// save to CoverArray and CovNum
			if( tpsize < psog_size ) {
				CoverArray.clear() ;
                // if seed
                if( seed.size() != 0 ) {
                    for(int i=0; i<seed.size(); i++) {
                        int[] tp = seed.get(i);
                        CoverArray.add(tp);
                    }
                }
                // add others
				for( int k=0; k<tpCA.size(); k++ ) {
					int[] ts = new int[parameter] ;
					for( int n=0; n<parameter; n++ )
						ts[n] = tpCA.get(k)[n] ;
					CoverArray.add(ts);
				}
				psog_size =  seed.size() + tpsize ;

				CovNum.clear();
				CovNum = tpCN ;
			}
		} // end for	
		System.out.println("final size: "+psog_size);
	}

	// PSO Evolve
	public int[] Evolve() throws CloneNotSupportedException {
		int best[] = new int[parameter] ; // 返回值

		ArrayList<Particle> T = new ArrayList<Particle>();       // 粒子群
		Particle gBest = new Particle(parameter,value,config);   // gBest

		// 种群初始化
		for(int i=0; i<config.pso_size; i++) {
			Particle ap = new Particle(parameter,value,config) ;
			ap.RandomInit();
			T.add(ap);

			if(i == 0) {
				gBest = ap.clone() ;
			}
		}
		//
		// 迭代次数
		int it = 1 ;

		// 生成一个测试用例
		while( true ) {
			// 计算每个粒子的fitness值
			for( int index=0 ; index<T.size() ; index++ ) {
				int fit = comb.FitnessValue(T.get(index).getPosition(), 0) ;

				// 最大覆盖数
				if( fit == comb.gerTestcaseCoverMax() ) {
					for(int l=0; l<parameter; l++)
						best[l] = T.get(index).getPosition()[l] ;					
					return best ;
				}

				// pBest
				if( fit > T.get(index).getFitness() ) {
					T.get(index).UpdatePBest() ;
					T.get(index).setFitness(fit) ;
				}

				// gBest
				if( fit > gBest.getFitness() ) {
					gBest.setPosition(T.get(index).getPosition()) ;
					gBest.setFitness(fit) ;
				}	
			} // end for

			// 中止条件
			if ( it > config.pso_time )
				break ;

			// 更新粒子群
			for( int index=0 ; index<T.size() ; index++ ) {
				T.get(index).UpdateVelocity(gBest.getPosition());
				T.get(index).UpdatePosition();
			}

			// 迭代递增
			it++ ;

		} // end while

		for(int l=0; l<parameter; l++)
			best[l] = gBest.getPosition()[l] ;					
		return best ;
	}


    /*
     * example
     */
    public static void main(String args[]) throws CloneNotSupportedException {
        // 待生成覆盖表
        int p = 4 ;
        int[] v = {3, 3, 3, 3} ;
        int t = 2 ;
        // PSO参数
        Configuration tp_con = new Configuration(1, 20, 1000, 0.9, 1.7) ;
        // 初始化
        StandardPSO pso = new StandardPSO(p, v, t, tp_con);

        // 1.一次性生成整个覆盖表并输出
        pso.PSOEvolve();
        // 输出覆盖表
        for( int k=0 ; k<pso.CoverArray.size() ; k++ ) {
            int[] tp = pso.CoverArray.get(k) ;
            for( int l=0 ; l<tp.length ; l++ ) {
                System.out.print(tp[l] + " ");
            }
            System.out.print("\r\n");
        }
        System.out.println("---------------------");

        // 2.设置种子，并一次性生成整个覆盖表
        ArrayList<int[]> seed = new ArrayList<int[]>();
        int[] s1 = {0, 0, 0, 0} ;
        int[] s2 = {1, 1, 1, 1} ;
        seed.add(s1);
        seed.add(s2);
        pso.SetSeed(seed);
        pso.PSOEvolve();
        // 输出覆盖表
        for( int k=0 ; k<pso.CoverArray.size() ; k++ ) {
            int[] tp = pso.CoverArray.get(k) ;
            for( int l=0 ; l<tp.length ; l++ ) {
                System.out.print(tp[l] + " ");
            }
            System.out.print("\r\n");
        }
        System.out.println("---------------------");


        // 3.逐条生成，在调用PSOGenOne()前需要调用initialization()初始化
        pso.initialization();
        // 生成一条
        int[] test = pso.PSOGenOne() ;
        int cov = pso.comb.FitnessValue(test, 1) ; // 计算覆盖组合数，并将对应组合设置为已覆盖
        pso.CoverArray.add(test);  // 加入覆盖表
        for( int l=0; l<test.length ; l++ ) {
            System.out.print(test[l] + " ");
        }
        System.out.print(", cover " + cov + " combinations\r\n");

        // 生成一条
        int[] test1 = pso.PSOGenOne() ;
        cov = pso.comb.FitnessValue(test1, 1) ; // 计算覆盖组合数，并将对应组合设置为已覆盖
        pso.CoverArray.add(test1);  // 加入覆盖表
        for( int l=0; l<test.length ; l++ ) {
            System.out.print(test1[l] + " ");
        }
        System.out.print(", cover " + cov + " combinations\r\n");

    }

}