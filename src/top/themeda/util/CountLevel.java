package top.themeda.util;

public class CountLevel {
	public static int getMcMinExp(int level) {
		int min = 0;
		if(level>=32) {
			min=(int) (4.5*level*level-162.5*level+2220);
		}else if(level>=17) {
			min=(int) (2.5*level*level-40.5*level+360);
		}else {
			min=level*level+6*level;
		}
		return min;
	}
	public static int getMcLevelUpExp(int level) {
		int up = 0;
		if(level>=31) {
			up=9*level-158;
		}else if(level>=16) {
			up=5*level-38;
		}else {
			up=2*level+7;
		}
		return up;
	}
}
