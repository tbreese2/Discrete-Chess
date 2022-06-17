/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.Eval;

/**
 *
 * @author tbreese
 */
public class EvalConstants {
    final int O_P = 0;								// 6
    final int O_P_E =(O_P+6);					// 6
    final int O_ISOLATED = (O_P_E+6);				// 2
    final int O_PASSED= (O_ISOLATED+2);				// 10
    final int O_PASSED_PROTECTED= (O_PASSED+10);	// 2
    // PSTs: N m+e=64.
    final int O_N_PST= (O_PASSED_PROTECTED+2+72*0);
    // PSTs: B m+e=64.
    final int O_B_PST= (O_N_PST+64);
    // PSTs: R m+e=64.
    final int O_R_PST= (O_B_PST+64);
    // PSTs: Q m+e=64.
    final int O_Q_PST= (O_R_PST+64);
    // PSTs: K m+e=64.
    final int O_K_PST= (O_Q_PST+64);
    // PSTs: P m+e=64.
    final int O_P_PST= (O_K_PST+64);
    // all PSTs=64*6=384
    final int O_CAND_PASSED= (O_P_PST+64);			// 10
    final int O_N_MOB= (O_CAND_PASSED+10); 		// 18
    final int O_B_MOB= (O_N_MOB+18);				// 28
    final int O_R_MOB= (O_B_MOB+28);				// 30
    final int O_Q_MOB= (O_R_MOB+30);				// 56
    // knight outposts - base+bonus: 11+11=22
    final int O_R_PIN= (O_Q_MOB+56+22);				// 2
    final int O_K_SAFETY= (O_R_PIN+2);				// 62
    final int O_K_PP0= (O_K_SAFETY+62);				// 16
    final int O_UPP= (O_K_PP0+16);					// 1
    final int O_PEN_NO_PAWN= (O_UPP+1);				// 1
    final int O_PEN_NO_MATING_MAT= (O_PEN_NO_PAWN+1);// 1
    final int O_BISHOP_PAIR= (O_PEN_NO_MATING_MAT+1);// 1
    final int O_B_PIN= (O_BISHOP_PAIR+1);			// 4
        final int O_K_PP1= (O_B_PIN+4);					// 16
    final int O_K_MOB= (O_K_PP1+16);				// 4
    final int O_RPOR= (O_K_MOB+4);					// 2
    final int O_PP= (O_RPOR+2);						// 576*2
    final int O_KP= (O_PP+576*2);					// 24*64*10*2=30720
    final int O_NB= (O_KP+24*64*10*2);				// 2048*2
    final int O_R1= (O_NB+2048*2);					// ranks 1/8: 2*3321
    final int O_T= (O_R1+2*3321);
}
