package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import static java.util.stream.Collectors.groupingBy;

public class FileDeletor {
	public static void fileDeletor(File file) {
		Arrays.stream(file.listFiles()).flatMap(f -> listFile(f, new ArrayList<File>()))
				.collect(groupingBy(File::getName)).entrySet() // 与scala比起来，java还是弱了很多，这里我只能重新把map变成Set,然后在进行Stream操作,不如scala一气呵成
				.stream().filter(e -> e.getValue().size() > 1).forEach(e -> System.out.println(e.getKey()));

	}

	public static Stream<File> listFile(File file, List<File> files) {

		if (file.isFile()) {
			files.add(file);
			// System.out.println(file.getPath());
		} else {
			// 这里应该也可能用Stream来操作的
			for (File mFile : file.listFiles())
				listFile(mFile, files);
		}
		return files.stream();

	}
	//删除路径下空文件
	public static void delMultyFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			throw new RuntimeException("File \"" + path + "\"文件路径未找到");
		}
		File[] fileList = file.listFiles();
		for (File f : fileList) {
			if (f.isDirectory()) {
				{
					delMultyFile(f.getAbsolutePath());
				}
			} else {
				if (f.length() == 0) {
					System.out.println(f.delete() + "---" + f.getName());
				}
			}
		}
	}
}
