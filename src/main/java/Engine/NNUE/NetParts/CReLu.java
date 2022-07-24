/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.NNUE.NetParts;

import org.nd4j.linalg.api.ops.impl.scalar.*;
import org.nd4j.linalg.api.ops.impl.transforms.gradient.LeakyReLUBp;
import org.nd4j.common.primitives.Pair;
import org.nd4j.linalg.activations.BaseActivationFunction;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;


/**
 * f(x) = min(max(x, 0), 1)
 */
public class CReLu extends BaseActivationFunction {
    private Double max = 1.0;
    private Double threshold = 0.0;
    private Double negativeSlope = 0.0;

    public CReLu(){
        this.max = (Double)1.0;
        this.threshold = (Double)0.0;
        this.negativeSlope = (Double)0.0;
    }
    
    @Override
    public INDArray getActivation(INDArray in, boolean training) {
        if(negativeSlope != null || threshold != null){
            double t = threshold == null ? 0.0 : threshold;
            double ns = negativeSlope == null ? 0.0 : negativeSlope;
            if(t == 0.0) {
                Nd4j.getExecutioner().execAndReturn(new LeakyReLU(in, ns));
            } else {
                //Non-zero threshold, and non-zero slope
                //TODO optimize this... but, extremely rare case in practice?
                INDArray oneGte = in.gte(t).castTo(in.dataType());
                INDArray oneLt = in.lt(t).castTo(in.dataType());
                INDArray lower = oneLt.muli(ns).muli(in.sub(threshold));
                INDArray upper = oneGte.muli(in);
                in.assign(lower.addi(upper));
            }
        } else {
            Nd4j.getExecutioner().exec(new RectifiedLinear(in, in));
        }
        if(max != null){
            Nd4j.exec(new ScalarMin(in, null, in, max));
        }

        return in;
    }

    @Override
    public Pair<INDArray, INDArray> backprop(INDArray in, INDArray epsilon) {
        assertShape(in, epsilon);

        INDArray dLdz;
        INDArray maxMask = (max == null || max == 0.0 ? null : in.lt(max));
        if(negativeSlope != null || threshold != null){
            double t = threshold == null ? 0.0 : threshold;
            double ns = negativeSlope == null ? 0.0 : negativeSlope;
            if(t == 0.0) {
                dLdz = Nd4j.getExecutioner().exec(new LeakyReLUBp(in, epsilon, in.ulike(), ns))[0];
            } else {
                //Non-zero threshold, and non-zero slope
                //TODO optimize this... but, extremely rare case in practice?
                INDArray oneGte = in.gte(t).castTo(in.dataType());
                INDArray oneLt = in.lt(t).castTo(in.dataType());
                INDArray lower = oneLt.muli(ns);
                INDArray upper = oneGte;
                dLdz = in.assign(lower.addi(upper)).muli(epsilon);
            }
        } else {
            dLdz = Nd4j.getExecutioner().exec(new RectifiedLinearDerivative(in, epsilon, in.ulike(), threshold == null ? 0.0 : threshold))[0];
        }

        if(maxMask != null){
            dLdz.muli(maxMask);
        }
        return new Pair<>(dLdz, null);
    }
    
    @Override
    public String toString() {
        return "crelu";
    }

}
