package Engine.Eval.NNUE.tools;


public interface ISignalFiller {
	public void fill(ISignals signals);
	public void fillByComplexity(int complexity, ISignals signals);
}
