package bagaturchess.deeplearning_deepnetts.impl1.eval;


import java.io.IOException;

import bagaturchess.bitboard.api.IBitBoard;
import bagaturchess.search.api.IEvalConfig;
import bagaturchess.search.api.IEvaluator;
import bagaturchess.search.api.IEvaluatorFactory;
import bagaturchess.search.impl.eval.cache.IEvalCache;


public class NeuralNetworkEvaluatorFactory implements IEvaluatorFactory {
	
	
	public NeuralNetworkEvaluatorFactory() {
	}
	
	
	public IEvaluator create(IBitBoard bitboard, IEvalCache evalCache) {
		
		try {
			return new NeuralNetworkEvaluator(bitboard, evalCache, null);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public IEvaluator create(IBitBoard bitboard, IEvalCache evalCache, IEvalConfig evalConfig) {
		try {
			return new NeuralNetworkEvaluator(bitboard, evalCache, evalConfig);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
