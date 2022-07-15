/**
 *  BagaturChess (UCI chess engine and tools)
 *  Copyright (C) 2005 Krasimir I. Topchiyski (k_topchiyski@yahoo.com)
 *  
 *  This file is part of BagaturChess program.
 * 
 *  BagaturChess is open software: you can redistribute it and/or modify
 *  it under the terms of the Eclipse Public License version 1.0 as published by
 *  the Eclipse Foundation.
 *
 *  BagaturChess is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  Eclipse Public License for more details.
 *
 *  You should have received a copy of the Eclipse Public License version 1.0
 *  along with BagaturChess. If not, see http://www.eclipse.org/legal/epl-v10.html
 *
 */
package Engine.Eval.NNUE.visitor;

import java.util.ArrayList;
import java.util.List;

import javax.visrec.ml.data.Column;
import javax.visrec.ml.data.DataSet;

import deepnetts.data.MLDataItem;
import deepnetts.data.TabularDataSet;
import deepnetts.util.Tensor;


public class DataSetLearning implements DataSet<MLDataItem> {
	
	
	private List<MLDataItem> items;
	private String[] targetNames;
	
	
	public DataSetLearning() {
		
		items = new ArrayList<MLDataItem>();
		
		targetNames = new String[110];
		for (int i = 0; i < targetNames.length; i++) {
			targetNames[i] = "LABEL" + i;
		}
	}
	
	
	public void addItem(float[] inputs, float[] outputs) {
		items.add(new TabularDataSet.Item(new Tensor(inputs), new Tensor(outputs)));
	}
	
	
	@Override
	public List<MLDataItem> getItems() {
		return items;
	}
	
	
	@Override
	public DataSet<MLDataItem>[] split(double... parts) {
		throw new UnsupportedOperationException();
	}


	@Override
	public List<Column> getColumns() {
		throw new UnsupportedOperationException();
	}


	@Override
	public String[] getTargetColumnsNames() {
		return targetNames;
	}


	@Override
	public void setColumns(List<Column> arg0) {
		throw new UnsupportedOperationException();
	}
}
