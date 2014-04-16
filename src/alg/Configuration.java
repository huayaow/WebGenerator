package alg;

/*
 * Configuration: 参数配置
 */
public class Configuration {
	// Repeat time
	public int pso_repeat ;
	// PSO Basic Parameter
	public int pso_size ;
	public int pso_time ;
	public double pso_weight ;
	public double pso_factor ;

	public Configuration(int r, int s, int t, double w, double f) {
		this.pso_repeat = r ;
		this.pso_size = s ;
		this.pso_time = t ;
		this.pso_weight = w ;
		this.pso_factor = f ;
	}
}