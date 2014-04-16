package alg;

/*
 * Combinations: 生成待覆盖组合对，计算测试用例适应值
 */
public class Combinations {
	// 覆盖表信息
	private int parameter ;
	private int[] value ;
	private int tway ;
	// 待覆盖组合信息
	private int SCountAll ;  // 总的待覆盖组合数
	private int SCount ;     // 未覆盖组合数
	private int coverMain ;          // 记录Main行数，C(parameter,tway)
	private int testcaseCoverMax ;   // 一个测试用例最多能覆盖组合数
	// 0-未覆盖
	private int[][] AllS ;

	public Combinations(int p, int[] v, int t) {
		parameter = p ;
		value = v ;
		tway = t ;
		coverMain = cal_combine( p , tway );
		testcaseCoverMax = coverMain ;
	}

	// SCountAll
	public int getSCountAll() {
		return SCountAll ;
	}

	// SCount
	public int getSCount() {
		return SCount ;
	}

	// testcaseCoverMax
	public int gerTestcaseCoverMax() {
		return testcaseCoverMax ;
	}

	// AllS初始化
	public void GenerateS() {
		AllS = null ;
		SCount = 0 ;
		// 分配coverMax行
		AllS = new int[coverMain][] ;
		// 按字典序生成所有参数间的组合情况
		int[] temp = new int[tway] ;      // 迭代记录
		int[] temp_max = new int[tway];   // 各自最大值
		for( int k=0 ; k<tway ; k++ )  // 初始化
		{
			temp[k] = k ;
			temp_max[k] = parameter - tway + k ;
		}

		int end = tway - 1 ;
		int ptr = end ;

		int already = 0 ;
		while( already < coverMain ) {
			// 处理temp，此时temp[]标记了参数序号
			// 计算涉及参数的组合个数
			int allcomb = 1 ;
			for( int p=0 ; p<tway ; p++ )
				allcomb = allcomb * value[temp[p]] ;
			// 生成新的1行
			AllS[already] = new int[allcomb];
			// 初始化，全为0
			for( int k=0 ; k<allcomb ; k++ )
				AllS[already][k] = 0 ;
			// 计算总的组合个数
			SCount += allcomb ;

			// 求下一个组合
			temp[end] = temp[end] + 1 ;  // 末位加1
			ptr = end ;
			while( ptr > 0 ) {
				if( temp[ptr] > temp_max[ptr] ) { // 超过该位允许最大值
					temp[ptr-1] = temp[ptr-1] + 1 ;   // 前一位加1
					ptr-- ;
				}
				else
					break ;
			}
			if( temp[ptr] <= temp_max[ptr]) { // 若该位值不是最大，后面每位在前一位基础上加1
				for( int i=ptr+1 ; i<tway ; i++ ) 
					temp[i] = temp[i-1] + 1 ;
			}
			already++ ;			
		}
		// 记录总的待覆盖组合数
		SCountAll = SCount ;		
	}

	// ----------------------------------------------------------------------------
	// 计算测试用例test在未覆盖组合对集S中能覆盖的组合对数
	// 标记FLAG=0只计算不修改，FLAG=1则将覆盖的组合情况设置为已覆盖
	// 输入：一个测试用例test，标记FLAG
	// 输出：覆盖组合数
	// ----------------------------------------------------------------------------
	public int FitnessValue(int[] test, int FLAG) {
		int num = 0 ;   // 返回值

		// 依次按字典序生成test的各种组合情况，然后从AllS表中判断是否覆盖，O(C(par,tway))
		int[] pos = new int[tway] ;      // 储存参数情况
		int[] pos_max = new int[tway];   // 各自最大值
		for( int k=0 ; k<tway ; k++ )  // 初始化
		{
			pos[k] = k ;
			pos_max[k] = parameter - tway + k ;
		}
		int end = tway - 1 ;
		int ptr = end ;

		int[] sch = new int[tway] ;  // 存储取值的情况

		for( int row = 0 ; row < coverMain ; row++ )
		{
			// 得到组合情况
			for( int k=0 ; k<tway ; k++ )
				sch[k] = test[pos[k]] ;
			// 判断是否覆盖
			if( !Covered( pos , sch , FLAG ) )
				num++ ;

			// 生成一下个
			pos[end] = pos[end] + 1 ;  // 末位加1
			ptr = end ;
			while( ptr > 0 ) {
				if( pos[ptr] > pos_max[ptr] ) { // 超过该位允许最大值	
					pos[ptr-1] = pos[ptr-1] + 1 ;   // 前一位加1
					ptr-- ;
				}
				else
					break ;
			}
			if( pos[ptr] <= pos_max[ptr]) { // 若该位值不是最大，后面每位在前一位基础上加1			
				for( int i=ptr+1 ; i<tway ; i++ ) 
					pos[i] = pos[i-1] + 1 ;
			}

		}
		return num ;
	}

	// ----------------------------------------------------------------------------
	// 计算一个取值情况是否覆盖，其中pos存储参数字典序号，sch存储对应取值
	// 输入：pos[tway]，sch[tway]，FLAG=1则表示未覆盖时需要将该位置1
	// 输出：true为已覆盖，false为未覆盖
	// ----------------------------------------------------------------------------
	public boolean Covered( int[] pos , int[] sch , int FLAG ) {
		// 通过position计算得到行号，schema计算得到列号，查AllS得出结果，接近O(1)
		boolean ret = true ;  // 返回值

		// 计算row，下标从0开始计算
		int row = cal_combine2num( pos , parameter , tway ) ;
		// 计算column，下标从0开始计算
		int column = 0 ;
		int it = 0 ;
		for( int i=0 ; i<tway ; i++ )
		{
			it = sch[i] ;
			for( int j=i+1 ; j<tway ; j++ )
				it = value[pos[j]] * it ;
			column += it ;
		}

		if( AllS[row][column] == 0 ) {
			ret = false ;
			if( FLAG == 1 ) {
				AllS[row][column] = 1 ;
				SCount-- ;
			}
		}
		return ret ;
	}

	// test
	public void printAllS() {
		for(int x=0; x<AllS.length; x++) {
			for(int y=0; y<AllS[x].length; y++) {
				System.out.print(AllS[x][y]+" ");
			}
			System.out.print("\r\n");
		}
		System.out.println("SCount: "+SCount);
		System.out.println("--------------");
	}

	/*
	 * 计算方法
	 */
	// 计算C(n,m)
	private int cal_combine(int n, int m) {
		int ret = 1 ;
		int p = n ;
		for( int x=1 ; x<=m ; x++ , p-- )
		{
			ret = ret * p ;
			ret = ret / x ;
		}
		return ret ;
	}
	// 计算c[]在所有组合的字典序中的序号，在n个参数中选m个情况下 (n,m)
	private int cal_combine2num( int[] c , int n , int m )    
	{
		int ret = cal_combine( n , m ) ;
		for( int i=0 ; i<m ; i++ )
			ret -= cal_combine( n-c[i]-1 , m-i );
		ret--;
		return ret ;                
	}
	/*
	// 计算字典序第t个的参数组合，结果存入c中，在n个参数中选m个情况下 (n,m)
	private void cal_num2combine( int[] c , int t , int n , int m )  {
		t++;                        // 输入+1
		int j=1 , k ;
		for( int i=0 ; i<m ; c[i++]=j++ )
			for( ; t>( k = cal_combine( n-j , m-i-1 ) ) ; t-=k , j++ )
				;
		for( int p=0 ; p<m ; p++ )   // 输出-1
			c[p]--;
	}
	*/
}