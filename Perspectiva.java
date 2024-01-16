import java.awt.Point;
import javax.swing.JComponent;

public class Perspectiva extends Forma {
	int origenX = 400 , origenY = 300;
    // DP: Distancia del punto del observador al plano de proyección, RH:PH:TH: Segmento y angulos que definen el punto del observador en coordenadas esfericas.
	// POX1:POX2:POX3: Punto del observador en coordenadas cartesianas.
	double grado = Math.PI / 180.0;
	double TH = 30.0 * grado, PH = 60.0 * grado, RH = 40, C1 = Math.cos(TH), C2 = Math.cos(PH), S1 = Math.sin(TH), S2 = Math.sin(PH), DP = 300.0, FE = 1.1, error = 0.0;
	double POX1 = RH*C1*S2, POX2 = RH*S1*S2, POX3 = RH*C2;
	double trasPObs[][]={{1,0,0,-POX1},{0,1,0,-POX2},{0,0,1,-POX3},{0,0,0,1}};
	double rot90MTH[][]={{S1,-C1,0,0},{C1,S1,0,0},{0,0,1,0},{0,0,0,1}};
	double rot180MPH[][]={{1,0,0,0},{0,-C2,S2,0},{0,-S2,-C2,0},{0,0,0,1}};
	double refEjeX1[][]={{-1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};
	double matTRPos[][]=(new matrix()).producto(refEjeX1,(new matrix()).producto(rot180MPH,(new matrix()).producto(rot90MTH,trasPObs))); // Matriz de la transformacion
    // Matrices que rotan 1 grado sobre cada eje.
	double rotEjeX1[][]={{1,0,0,0},{0,Math.cos(grado),-Math.sin(grado),0},{0,Math.sin(grado),Math.cos(grado),0},{0,0,0,1}};
	double rotEjeX2[][]={{Math.cos(grado),0,Math.sin(grado),0},{0,1,0,0},{-Math.sin(grado),0,Math.cos(grado),0},{0,0,0,1}};
	double rotEjeX3[][]={{Math.cos(grado),-Math.sin(grado),0,0},{Math.sin(grado),Math.cos(grado),0,0},{0,0,1,0},{0,0,0,1}};
	
	public Perspectiva() {
		super();
	}
	public void setOrigen(int X, int Y) {
		origenX = X;
		origenY = Y;
	}
	public Perspectiva (double RH_, double TH_, double PH_, double DP_) {
		super();
		RH = RH_;
		TH = TH_;
		PH = PH_;
		DP = DP_; // Dist. al plano de proyección desde el punto de vista del observador.
		C1 = Math.cos(TH);
		C2 = Math.cos(PH); 
		S1 = Math.sin(TH);
		S2 = Math.sin(PH);
		POX1 = RH*C1*S2;
		POX2 = RH*S1*S2;
		POX3 = RH*C2;
		refreshTransform();
	}
	public Perspectiva (PointR3 pobs, double DP_) {
		super();
		update(pobs, DP_);
	}
	public void update (PointR3 pobs, double DP_) {
		RH = pobs.getRho();
		TH = pobs.getTheta();
		PH = pobs.getPhi();
		DP = DP_; // Dist. al plano de proyección desde el punto de vista del observador.
		C1 = Math.cos(TH);
		C2 = Math.cos(PH); 
		S1 = Math.sin(TH);
		S2 = Math.sin(PH);
		POX1 = pobs.getX();
		POX2 = pobs.getY();
		POX3 = pobs.getZ();
		refreshTransform();		
	}
	public void refreshTransform() {
		trasPObs[0][3] = -POX1;
		trasPObs[1][3] = -POX2;
		trasPObs[2][3] = -POX3;
		rot90MTH[0][0] = S1;
		rot90MTH[0][1] = -C1;
		rot90MTH[1][0] = C1;
		rot90MTH[1][1] = S1;
		rot180MPH[1][1] = -C2;
		rot180MPH[1][2] = S2;
		rot180MPH[2][1] = -S2;
		rot180MPH[2][2] = -C2;
		matTRPos = (new matrix()).producto(refEjeX1,(new matrix()).producto(rot180MPH,(new matrix()).producto(rot90MTH,trasPObs)));
	}
	public final Point projectedPoint( PointR3 pr3 ) {
		//double VC[][] = pr3.getHArr();
		//double VC1T[][] = (new matrix()).producto(matTRPos,(new matrix()).columna(VC,0));
		double VC1T[][] = (new matrix()).producto(matTRPos,pr3.getHArr());		
		double XP1 = error+origenX+((DP*VC1T[0][0])/(VC1T[2][0]*FE));
		double YP1 = error+origenY-(DP*VC1T[1][0])/VC1T[2][0];
		Point pAux = new Point();
		pAux.setLocation(XP1, YP1);
		return pAux;
	}
}