package com.fbe.exe;

public interface FBEExecutable {
	public void execute(FBEExecutor exe) ;
	public default void toBaseLook() {}
	public default void toExeLook() {}
	public default void redraw() {}
}
