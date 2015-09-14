package io.vertex.example.backpressureandstreams.asyncfile;

public class Counter {
	private Integer i = 0;
	
	public void increment(){
		i++;
	}
	
	public Integer getCount(){
		return this.i;
	}
	
	public void reset(){
		i = 0;
	}
}
