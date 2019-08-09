package jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class JavaCallPython {
	public static void main(String[] args) {

	}

	// 在java中调用本地python脚本
	public static void add() {
		PythonInterpreter interpreter = new PythonInterpreter();
		interpreter.execfile("D:\\add.py");

		// 第一个参数为期望获得的函数（变量）的名字，第二个参数为期望返回的对象类型
		PyFunction pyFunction = interpreter.get("add", PyFunction.class);
		int a = 5, b = 10;
		// 调用函数，如果函数需要参数，在Java中必须先将参数转化为对应的“Python类型”
		PyObject pyobj = pyFunction.__call__(new PyInteger(a), new PyInteger(b));
		System.out.println("the anwser is: " + pyobj);
	}

	// 使用Runtime.getRuntime()执行脚本文件（推荐）
	public static void add2() {
		Process proc;

		int a = 18;
		int b = 23;

		try {
			proc = Runtime.getRuntime().exec("python D:\\add.py");// 执行py文件
			// 用输入输出流来截取结果
			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
			in.close();
			proc.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
