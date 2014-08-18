package com.alex.data;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import android.content.Context;
import android.content.res.*;
public class ParseDota2Items2 {

	public static String readFile(InputStream item) {    

		
        InputStream fis = null;
        StringBuffer output = new StringBuffer();
        String line = null;
        boolean itemNameFlag = false;
        boolean itemAliasFlag = false;
        boolean itemIdFlag = false;
        BufferedReader br = null;
        String temp = null;
        try {    
        	    fis = item;
        		//fis = new FileInputStream(fileName);
                br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
                output.append("<DOTA2Items>");
                while ((line = br.readLine()) != null) {
                	if(line.contains("//===========================================================================")){
                		//System.out.println("C1");
                		if(itemAliasFlag){
                			itemAliasFlag = false;
                			itemNameFlag = true;
                		}
                		else{
                			output.append("<DOTA2Item>");
                			itemAliasFlag = true;
                		}                		
                	}
                	else if (line.contains("// ") && itemAliasFlag){
                		output.append("<Alias>"+ line.substring(4) +"</Alias>");
                	}
                	else if (line.matches("(\\s)*\"(\\w)*\"") && itemNameFlag){
                		output.append("<Name>"+ line.replaceAll("\"", "").replaceAll("\t", "") +"</Name>");
                		itemNameFlag = false;
                		itemIdFlag = true;
                	}
                	else if(line.contains("ID") && itemIdFlag){
                		temp = line.split("\"")[3];
                		output.append("<ID>"+ temp +"</ID>");
                		itemIdFlag = false;
                		temp = null;
                		output.append("</DOTA2Item>");
                	}
                	
                }    
                output.append("</DOTA2Items>");
        } catch (IOException e) {    
            e.printStackTrace();    
        } finally {
        	try {
        		br.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }        
        return new String(output.toString());   
    }    
}
