import Engine.Eval.NNUE.visitor.DeepLearningVisitorImpl_AllFeatures;
import Engine.Eval.NNUE.tools.ILearningInput;
import Engine.Eval.NNUE.tools.LearningInputFactory;
import Engine.Eval.NNUE.tools.PositionsTraverser;


public class DeepLearningTraverser_AllFeatures {
	
	public static void main(String[] args) {
		
		System.out.println("Reading games ... ");
		long startTime = System.currentTimeMillis();
		try {
			
			//String filePath = "./Houdini.15a.short.cg";
			//String filePath = "./Houdini.15a.cg";
			//String filePath = "./Arasan13.1.cg";
			//String filePath = "./stockfish-10.cg";
			String filePath = "./stockfish-14.1.cg";
			//String filePath = "./stockfish-14.1-4N.cg";
			
			
			DeepLearningVisitorImpl_AllFeatures learning = new DeepLearningVisitorImpl_AllFeatures();
			
			//ILearningInput input = LearningInputFactory.createDefaultInput();
			
			while (true) {
				//PositionsTraverser.traverseAll(filePath, learning, 999999999, input.createBoardConfig(), input.getPawnsEvalFactoryClassName());
			}
			
		} catch (Exception e) {
			System.out.println("bruh");
		}
		long endTime = System.currentTimeMillis();
		System.out.println("OK " + ((endTime - startTime) / 1000) + "sec");		
	}
}
