package com.changan.changanproject.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class WriteToCsv {
	List<Object> headList;//表头
	String fileName;
	String filePath;
	public BufferedWriter csvWriter;

	public WriteToCsv(String fileName,String filePath,Object [] head){
		headList = Arrays.asList(head);
		this.fileName = fileName;
		this.filePath = filePath;

		try {
			File csvFile = new File(filePath + fileName);
			File parent = csvFile.getParentFile();
			if (parent != null && !parent.exists()) {
				parent.mkdirs();
			}
			csvFile.createNewFile();

			// GB2312使正确读取分隔符","
			csvWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "GB2312"), 1024);
			// 写入文件头部
			writeRow(headList, csvWriter);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 写一行数据
	 * @param row 数据列表
	 * @param csvWriter
	 * @throws IOException
	 */
	public void writeRow(List<Object> row, BufferedWriter csvWriter) throws IOException {
		for (Object data : row) {
			StringBuffer sb = new StringBuffer();
			String rowStr = sb.append("\"").append(data).append("\",").toString();
			csvWriter.write(rowStr);
		}
		csvWriter.newLine();
	}

	public static void main(String args[]){
		Object [] head = {"xuhao","证件类型","日期",};
		WriteToCsv w = new WriteToCsv("testCSV.csv", "c:/test/", head);
		List<Object> rowList = new ArrayList();
		rowList.add("zhangsan");
		rowList.add("zhangsan");
		rowList.add("zhangsan");
		try {
			w.writeRow(rowList, w.csvWriter);
			w.writeRow(rowList, w.csvWriter);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				w.csvWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


}
