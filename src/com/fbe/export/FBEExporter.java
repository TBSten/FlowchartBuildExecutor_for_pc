package com.fbe.export;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fbe.item.Flow;

public abstract class FBEExporter {
	public static List<FBEExporter> mediaExporters = new ArrayList<>();
	public static List<FBEExporter> codeExporters = new ArrayList<>();

	public abstract void export(Flow mainFlow,List<Flow> flows,File file) throws Exception ;
}
