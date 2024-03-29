
import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
public class Test {
	/*
	 * 读文件，按行读取，然后放入map 中，把行号作为key，把行字符串作为值
	 */
	public static Map<Integer,String> readFileByLine(String path) throws IOException{
		File file=new File(path);
		BufferedReader reader=null;
		Map<Integer,String> mp=new HashMap<Integer,String>();
		try {
			reader=new BufferedReader(new FileReader(file));
			String tempString =null;
			int line=1;
			
			while((tempString=reader.readLine())!=null){
				mp.put(line, tempString);
				line++;
			}
			reader.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		}
		return mp;
	}
	/*
	 *把map的value映射成一个 set
	 */
	public static HashSet<String> getVectory(Map<Integer,String> map){
		Iterator<String> itr=map.values().iterator();
		HashSet<String> set=new HashSet<String>();
		//Vector<String> v=new Vector<String>();
		while(itr.hasNext()){
			set.add(itr.next().toString());
		}
		return set;
	}
	/*
	 * 把从文件中读取到的map转化成相应的map
	 */
	public static Map<Object,Integer> transMap(Map<Integer, String> cn_map){
		Map<Object,Integer> m=new HashMap<Object,Integer>();
		for(Map.Entry<Integer, String> val:cn_map.entrySet()){
			StringTokenizer st=new StringTokenizer(val.getValue());
			String ikey=st.nextToken();
			String ivalue="";
			if(st.countTokens()>=2){
				String tvalue=st.nextToken();
				HashMap<Object,String> hm=new HashMap<Object,String>();
				hm.put(ikey, tvalue);
				while(st.hasMoreTokens()){
					ivalue+=st.nextToken();
				}
				m.put(hm, Integer.parseInt(ivalue));
			}else{
				ivalue=st.nextToken();
				m.put(ikey, Integer.parseInt(ivalue));
			}
		}
		return m;
	}
	/**
	 * 计算概率
	 * @param cwn
	 * @param cn
	 * @param set
	 * @return
	 */
	public static Map<Map<String,String>,Double> getCondLogProbability(Map<Map<String,String>,Integer> cwn,Map<String,Integer>  cn,HashSet<String> set){
		int v_length=set.size();
		Map<Map<String,String>,Double> mp=new HashMap<Map<String,String>,Double>();
		
		for(Map.Entry<Map<String,String>, Integer> val:cwn.entrySet()){
			for(Map.Entry<String, String> sval:val.getKey().entrySet()){
				int vt=val.getValue()+1;
				int vt_sum=cn.get(sval.getKey())+v_length; //类别词数+词典向量长度
				double p=Math.log(vt)-Math.log(vt_sum);
				mp.put(val.getKey(),p);
			}
		}
		System.out.println("changed:"+mp.size());
		return mp;
	}
	public static String predictP(Map<Map<String,String>,Double> condp,Map<String,Integer> doc,HashSet<String> set,Map<String,Integer> cn){
		String c=null;
		for(Map.Entry<String, Integer> cn_val:cn.entrySet()){
//			Iterator<String> itr=set.iterator();
//			while(itr.hasNext()){
//				
//			}
			String c_key=cn_val.getKey();
			for(Map.Entry<String, Integer> doc_val:doc.entrySet()){
			}
		}
		return c;
	}
	public static void main(String[] args) throws IOException {
		String root_dir="/home/hadoop/桌面/";
		String cn_file=root_dir+"C-N";
		String cwn_file=root_dir+"((c,w),n)";
		String v_file=root_dir+"v";
		Map<Integer,String> cn_map=readFileByLine(cn_file);
		Map<Integer,String> cwn_map=readFileByLine(cwn_file);
		Map<Integer,String> v_map=readFileByLine(v_file);
		HashSet<String> v_set=getVectory(v_map);
		Map<Object,Integer> cn_o=null,cwn_o=null;
		cn_o=transMap(cn_map);
		cwn_o=transMap(cwn_map);
		Map<String,Integer> cn=new HashMap<String,Integer>();
		Map<Map<String,String>,Integer> cwn=new HashMap<Map<String,String>,Integer>();
		for(Map.Entry<Object,Integer> val:cn_o.entrySet()){
			cn.put(val.getKey().toString(), val.getValue());
		}
		for(Map.Entry<Object, Integer> val:cwn_o.entrySet()){
			cwn.put((Map<String,String>)val.getKey(), val.getValue());
		}
		Map<Map<String,String>,Double> imp=new HashMap<Map<String,String>,Double>();
		imp=getCondLogProbability(cwn, cn, v_set);
		
	}

}
