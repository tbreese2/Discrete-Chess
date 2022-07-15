package bagaturchess.deeplearning_deepnetts.impl1.eval;


import bagaturchess.search.api.IEvalConfig;


public class EvaluationConfig implements IEvalConfig {
	
	@Override
	public boolean useLazyEval() {
		return true;
	}
	
	@Override
	public boolean useEvalCache() {
		return true;
	}
	
	@Override
	public boolean isTrainingMode() {
		return false;
	}
	
	@Override
	public String getEvaluatorFactoryClassName() {
		return NeuralNetworkEvaluatorFactory.class.getName();
	}
	
	@Override
	public String getPawnsCacheFactoryClassName() {
		return bagaturchess.learning.goldmiddle.impl.cfg.bagatur.eval.BagaturPawnsEvalFactory.class.getName();
	}

}
