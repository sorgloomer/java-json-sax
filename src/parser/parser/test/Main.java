package parser.test;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;

import parser.JsonSaxReader;
import parser.ValueCallback;

import com.fasterxml.jackson.databind.ObjectMapper;


public class Main {
	public static void main(String[] args) throws Exception {
		SaxTest.test1();
	}
}
