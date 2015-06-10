package parser.test;

import java.io.IOException;

import parser.ArrayReader;
import parser.CommonCommands;
import parser.ObjectBuilder;

public class Person {
	private String name;
	private int age;
	private int[] histo;
	
	public static final ObjectBuilder<Person, Person> PARSER = new ObjectBuilder<Person, Person>() {
		
		@Override
		public Person begin() {
			return new Person();
		}
		
		@Override
		public void hitString(Person p, String key, String value) {
			switch (key) {
			case "name":
				p.name = value;
				break;
			}
		}

		@Override
		public void hitInt(Person obj, String key, long value) {
			obj.age = (int)value;
		}
		
		@Override
		public void hitArray(Person obj, String key, ArrayReader reader) throws IOException {
			obj.histo = reader.read(CommonCommands.INTS);
		}
		
		@Override
		public Person end(Person obj) {
			return obj;
		}
		
		@Override
		public void hitFloat(Person obj, String key, double value) {
		}
	};
	

}
