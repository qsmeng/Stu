package chinese;

import java.io.UnsupportedEncodingException;

public class Chinese1 {
	public static void main(String[] args) {
		System.out.println(java.nio.charset.Charset.forName("GB2312").newEncoder().canEncode("闃块噷宸村反Java寮�鍙戞墜鍐岀粓鏋佺増"));
		System.out.println(java.nio.charset.Charset.forName("UTF-8").newEncoder().canEncode("闃块噷宸村反Java寮�鍙戞墜鍐岀粓鏋佺増"));
		System.out.println(java.nio.charset.Charset.forName("GBK").newEncoder().canEncode("闃块噷宸村反Java寮�鍙戞墜鍐岀粓鏋佺増"));

		System.out.println(java.nio.charset.Charset.forName("GB2312").newEncoder().canEncode("绾㈣湗铔�.rar"));
		System.out.println(java.nio.charset.Charset.forName("UTF-8").newEncoder().canEncode("绾㈣湗铔�.rar"));
		System.out.println(java.nio.charset.Charset.forName("GBK").newEncoder().canEncode("绾㈣湗铔�.rar"));

		System.out.println(java.nio.charset.Charset.forName("GB2312").newEncoder().canEncode("�����ֲ�"));
		System.out.println(java.nio.charset.Charset.forName("UTF-8").newEncoder().canEncode("�����ֲ�"));
		System.out.println(java.nio.charset.Charset.forName("GBK").newEncoder().canEncode("�����ֲ�"));

		System.out.println(java.nio.charset.Charset.forName("GB2312").newEncoder().canEncode("��ǰ����"));
		System.out.println(java.nio.charset.Charset.forName("UTF-8").newEncoder().canEncode("��ǰ����"));
		System.out.println(java.nio.charset.Charset.forName("GBK").newEncoder().canEncode("��ǰ����"));
		
		try {
			String str = "闃块噷宸村反Java寮€鍙戞墜鍐岀粓鏋佺増v1.3.0.pdf   ";
			System.out.println(utg(str));
			System.out.println(utg(utg(str)));
			System.out.println(gtu(str));
			System.out.println(gtu(gtu(str)));
			System.out.println(stu(str));
			System.out.println(stu(stu(str)));
			System.out.println(gtb(str));
			System.out.println(gtb(gtb(str)));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	static String utg(String utf) throws UnsupportedEncodingException {
		return new String(utf.getBytes("UTF-8"), "GBK");
	}

	static String gtu(String utf) throws UnsupportedEncodingException {
		return new String(utf.getBytes("GBK"), "UTF-8");
	}

	static String stu(String utf) throws UnsupportedEncodingException {
		return new String(utf.getBytes(), "UTF-8");
	}

	static String gtb(String utf) throws UnsupportedEncodingException {
		return new String(utf.getBytes("GBK"));
	}

	static String utb(String utf) throws UnsupportedEncodingException {
		return new String(utf.getBytes("UTF-8"));
	}
}
