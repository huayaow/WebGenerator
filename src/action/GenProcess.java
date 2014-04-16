package action;

import java.util.Map;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import model.SUT;
import model.Algorithm;
import alg.StandardPSO;

public class GenProcess extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 1 ;

	// get session
	private Map<String,Object> session ;  
	@Override
	public void setSession(Map<String,Object> session) {  
        this.session = session;  
    }

	// transform
	private Algorithm ALG = new Algorithm() ; 

	public void setALG(Algorithm alg) {
		this.ALG = alg ;
	}
	public Algorithm getALG() {
		return ALG ;
	}

	//
	// Result returned
	//
	// CA
	// #,p1,p2,p3,...;1,x11,x12,x13,...;2,x21,x22,x23,...;...
	private String GenResult ; 
	private int GenSize ;
	private long GenTime ;

	// chart
	private String ChartCov ;    // 测试用例覆盖数
	private int ChartMax ;       // 总覆盖数

	public String getGenResult() {
		return GenResult ;
	}
	public int getGenSize() {
		return GenSize ;
	}
	public long getGenTime() {
		return GenTime ;
	}

	public String getChartCov() {
		return ChartCov ;
	}
	public int getChartMax() {
		return ChartMax ;
	}

	// execute
	public String execute() throws Exception {

		//System.out.println("------get SUT from session------");
		String raw = (String)session.get("rawdata");
		int way = Integer.parseInt((String)session.get("tway"));
		SUT sut = new SUT(raw,way);
		sut.FormatData();

		//System.out.println("------get ALG------");
		//System.out.println(ALG.getAlg_version());
		//System.out.println(ALG.getAlg_config());
		ALG.FormatData();

		/*
		System.out.println("^^^^^^^^^^^^^^^^^^");
		System.out.println(sut.getPara_num());
		for(int k=0 ; k<sut.getVal_num().length ; k++)
			System.out.print(sut.getVal_num()[k] + " " );
		System.out.print("\r\n");
		System.out.println(sut.getTway());
		System.out.println(ALG.pso_config.pso_repeat + " " + ALG.pso_config.pso_size + " " + ALG.pso_config.pso_time +
				" " + ALG.pso_config.pso_weight + " " + ALG.pso_config.pso_factor );
		System.out.println("^^^^^^^^^^^^^^^^^^");
		*/

		// run

		StandardPSO pso = new StandardPSO(sut.getPara_num(),sut.getVal_num(),sut.getTway(),ALG.pso_config);
		pso.PSOEvolve();

		// save covering array
		GenResult = sut.CoverArray2String(pso.CoverArray);
		GenSize = pso.psog_size ;
		GenTime = pso.psog_time ;
		//System.out.println(GenResult);

		// save chart
		ChartCov = sut.CovNum2ChartCov(pso.CovNum);
		ChartMax = pso.CovMax ;
		//System.out.println(ChartCov);
		//System.out.println(ChartMax);

		return "GenSuccess"; 
	}

}