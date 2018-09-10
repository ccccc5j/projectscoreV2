package test;
import java.util.*;
public class SelectStuFromGroup {

	static String students[][] = { 
			{ "陈虹宇", "韩寅强", "刘访樵"},
			{ "瞿成凯", "杨雅丽", "卢雪梅", "李晓", "陶世俊"},
			{ "罗凯", "钟梁", "聂子力", "胡杨", "向星吉"},
			{ "郭登吉", "李鱼", "赵锐", "王荣杰", "魏洪祥"},
			{ "李昀骁", "赵良榕", "尹杰", "周天宇", "舒雷"},
			{ "邓佳强", "周航", "刘富波", "周玉明"},
			{ "王丽梅", "冯越", "黄夕芸", "李玉玲"} 
			};

	public static void main(String[] args) {
		List <Integer>groups=new ArrayList<Integer>();
		for (int i = 0; i < students.length; i++) {			
			groups.add(i);
		}
		Collections.shuffle(groups);
		System.out.println("抽取的顺序和人选是:");
		for (int i = 0; i < students.length; i++) {
			int j=groups.get(i);
			int selStu=(int)(students[j].length*Math.random());
			System.out.println("第"+(i+1)+"位同学:"+
				"第"+(j+1)+"组 "+students[j][selStu]);			
		}		
	}
}