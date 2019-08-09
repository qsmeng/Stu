package jsoup;

import org.python.util.PythonInterpreter;

import java.util.Properties;

import org.python.core.Py;
import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.jline.internal.Log;

public class JythonExecPyFile{
	private static Log logger = LogFactory.getLog(JythonExecPyFile.class);


	public static PythonInterpreter getPythonEnterpreter(String jythonHome,String jythonJsonHome,String pythonRuleDir){


	Properties props=new Properties();
	//props.put("python.home","pathtotheLibfolder");
	props.put("python.home",jythonHome);
	props.put("python.console.encoding","UTF-8");
	props.put("python.security.respectJavaAccessibility","false");
	props.put("python.import.site","false");


	Properties preprops=System.getProperties();


	PythonInterpreter.initialize(preprops,props,new String[0]);
	PythonInterpreter pythonInterpreter=new PythonInterpreter();


	PySystemState sys=Py.getSystemState();
	
	sys.path.add(jythonJsonHome);
	sys.path.add(pythonRuleDir);
	//logger.info("jythonHome:"+jythonHome+"jythonJsonHome:"+jythonJsonHome+"pythonRuleDir:"+pythonRuleDir);
	return pythonInterpreter;
	}


	public static void addPythonThirdPartLib(String thirdLibPath){
	PySystemState sys=Py.getSystemState();
	sys.path.add(thirdLibPath);
	sys.path.add(SettingConfiguration.getValue("pythonDir"));
	}




	public static void cleanup(PythonInterpreter interpreter){
	if(interpreter!=null){
	interpreter.close();
	}
	}
	
	public static void main(String[]args){


	PythonInterpreter pythonEnterpreter=getPythonEnterpreter(SettingConfiguration.getValue("jPythonHome"),SettingConfiguration.getValue("jythonJsonHome"),SettingConfiguration.getValue("pythonDir"));
	
	System.out.println("-----------------------------调用有参数python方法执行方法-----------------------------");
	String fileNameWithParams="D:\\Java\\python344\\age.py";
	pythonEnterpreter.execfile(fileNameWithParams);
	PyFunction func=(PyFunction)pythonEnterpreter.get("judge_age",PyFunction.class);
	PyObject pyobj=func.__call__(new PyString("2000-11-11"),new PyInteger(1));
	
	System.out.println("pyObj:"+pyobj);
	
	System.out.println("-----------------------------调用无参数python方法执行方法-----------------------------");
	String fileNameNoParams="D:\\svnProject\\testConnectDB.py";
	pythonEnterpreter.execfile(fileNameNoParams);
	PyFunction func2=(PyFunction)pythonEnterpreter.get("say",PyFunction.class);
	PyObject pyobj2=func2.__call__();
	System.out.println("pyObj:"+pyobj2);
	
	JythonExecPyFile.cleanup(pythonEnterpreter);

	}

	//而为了更好的封装，将调用python的参数放到list中，而将返回的数据放到dict中，python将dict转换为json串，然后在java接受到返回结果之后，将json串在转换成java的map类型即可。


}
