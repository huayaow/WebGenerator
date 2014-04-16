package model;

import java.util.ArrayList;

public class SUT {
	// receive data
	private String rawdata ;
	private int tway ;

	// format data
	private int para_num ;
	private String[] para_name ;

	private int[] val_num ;
	private ArrayList<ArrayList<String>> val_name ;

	public SUT(String raw , int t) {
		this.rawdata = raw ;
		this.tway = t ;
	}

	public String getRawdata() {
		return rawdata ;
	}
	public Integer getTway() {
		return tway ;
	}
	public int getPara_num() {
		return para_num ;
	}
	public int[] getVal_num() {
		return val_num ;
	}

	// rawdata: ; p1,v1,v2,... ; p2,v1,v2,... ; ...
	public void FormatData() {
		String[] tp = rawdata.split(";") ;
		para_num = tp.length - 1 ;
		para_name = new String[para_num] ;   // init para_name
		val_num = new int[para_num] ;        // init val_num
		val_name = new ArrayList<ArrayList<String>>() ;  // init val_name

		for(int i=1 ; i<tp.length ; i++ ) {
			String temp = tp[i] ;
			String[] ts = temp.split(",") ;

			para_name[i-1] = ts[0] ;        // set para_name
			val_num[i-1] = ts.length - 1 ;  // set val_num

			ArrayList<String> a = new ArrayList<String>() ; // set val_name
			for(int j=1 ; j<ts.length ; j++ ) {  
				a.add(ts[j]) ;
			}
			val_name.add(a) ;
		}		
        
		// test print
		/*
		System.out.println("para_num: " + para_num );
		for(int x=0; x<para_name.length; x++){
			System.out.print(para_name[x]+" ");
		}
		System.out.print("\r\n");
		System.out.println("val_num:");
		for(int x=0; x<val_num.length; x++){
			System.out.print(val_num[x]+" ");
		}
		System.out.print("\r\n");
		System.out.println("val_name:");
		for(Iterator<ArrayList<String>> it = val_name.iterator(); it.hasNext();){
			ArrayList<String> b = it.next() ;
			for(Iterator<String> jt = b.iterator(); jt.hasNext();){ 
				System.out.print(jt.next()+" ");
			}
			System.out.print("\r\n");
		}
		System.out.println("tway: " + tway) ;
		*/
	}

	//
	// 数据转换
	//

	// target data
	// #,p1,p2,p3,...;1,x11,x12,x13,...;2,x21,x22,x23,...;...
	public String CoverArray2String(ArrayList<int[]> CA) {
		String fin = "#," ;
		String temp = "" ;

		// parameter line
		for(int i=0; i<para_num-1; i++) {
			fin = fin + para_name[i] + "," ;
		}
		fin = fin + para_name[para_num-1] + ";" ;

		// covering array
		for(int i=0; i<CA.size(); i++) {
			int[] tp = CA.get(i) ;
			temp = Integer.toString(i+1) ;
			fin = fin + temp + "," ; 

			for(int j=0; j<para_num-1; j++) {
				temp = val_name.get(j).get(tp[j]) ;
				fin = fin + temp + "," ; 
			}
			fin = fin + val_name.get(para_num-1).get(tp[para_num-1]) + ";" ; 
		}
		return fin ;
	}


	// chart 测试用例覆盖数
	public String CovNum2ChartCov(ArrayList<Integer> CN) {
		String re = "" ;
		String str = "" ;
		for( int k=0; k<CN.size()-1; k++ ) {
			str = Integer.toString(CN.get(k));  
			re = re + str + "," ;
		}
		str = Integer.toString(CN.get(CN.size()-1));  
		re = re + str ;		
		//System.out.println(re);
		return re ;
	}


}