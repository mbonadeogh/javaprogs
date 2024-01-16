//***************************************
//*** Copyright © 2022 Marcelo C. Bonadeo
//*** Permission is hereby granted, free of charge, to any person obtaining a copy
//*** of this software and associated documentation files (the "Software"), to deal
//*** in the Software without restriction, including without limitation the rights
//*** to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//*** copies of the Software, and to permit persons to whom the Software is
//*** furnished to do so, subject to the following conditions:
//*** 
//*** The above copyright notice and this permission notice shall be included in all
//*** copies or substantial portions of the Software.
//*** 
//*** THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//*** IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//*** FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//*** AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//*** LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//*** OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//*** SOFTWARE.
//***********************************************************
//*** El lenguaje JAVA es propiedad de Oracle Inc           *
//*** Copyright © 1995, 2022, Oracle and/or its affiliates. *
//***********************************************************
import javax.swing.JComponent;
import java.util.LinkedList;
import java.awt.Color;
import java.util.Random;
import java.util.Collections;

public class Forma extends JComponent /*JPanel*/ {
	static double PA = (Math.sqrt(5.0)+1.0)/2.0;	// Proporcion aurea.
}

class PointR3 {
	double x, y, z;
	double phi, theta, rho;
	public PointR3(double x_, double y_, double z_){
		x=x_;
		y=y_;
		z=z_;
		rho = /*0.5+*/Math.sqrt(Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2));
		phi = Math.acos(z/rho);
		//theta = Math.acos(x/(rho*Math.sin(phi)));
		theta = (y>0?Math.acos(x/(rho*Math.sin(phi))):(x<0?(Math.PI-Math.asin(y/(rho*Math.sin(phi)))):Math.asin(y/(rho*Math.sin(phi)))));
	}
	public static PointR3 toCartesian(double sx_, double sy_, double sz_, double theta_, double phi_){ // Elipsoidales a Cartesianas
		return new PointR3(sx_*Math.cos(theta_)*Math.sin(phi_),sy_*Math.sin(theta_)*Math.sin(phi_),sz_*Math.cos(phi_));
	}	
	public static PointR3 toCartesian(double rho_, double theta_, double phi_){ // Esfericas a Cartesianas
		return new PointR3(rho_*Math.cos(theta_)*Math.sin(phi_),rho_*Math.sin(theta_)*Math.sin(phi_),rho_*Math.cos(phi_));
	}
	public static PointR3 toCartesian(double R_, double r_, double theta_, double phi_){ // Toro a Cartesianas
		return new PointR3(Math.cos(theta_)*(R_+r_*Math.cos(phi_)),Math.sin(theta_)*(R_+r_*Math.cos(phi_)),r_*Math.sin(phi_));
	}	
	public static PointR3 toCartesianF8(double t, double theta_, double phi_){ // Patametrica 8 de revolucion a Cartesianas
		return new PointR3(t*Math.cos(phi_)*Math.sin(phi_), t*Math.cos(theta_)*Math.sin(theta_)*Math.sin(phi_), t*Math.sin(theta_)*Math.sin(phi_));
	}		
	public static PointR3 toCartesianPF(double a, double b, double theta_, double phi_){ // Piriforme a Cartesianas
		//return new PointR3(a*(1+Math.sin(phi_)),b*(1+Math.sin(phi_))*Math.cos(phi_)*Math.cos(theta_),b*(1+Math.sin(phi_))*Math.cos(phi_)*Math.sin(theta_));
		return new PointR3(b*(1+Math.sin(phi_))*Math.cos(phi_)*Math.sin(theta_),b*(1+Math.sin(phi_))*Math.cos(phi_)*Math.cos(theta_),a*(1+Math.sin(phi_)));
	}	
	public static PointR3 toCartesianLem(double a, double theta_, double phi_){ // Patametrica Lemniscata
		//return new PointR3(a*Math.cos(phi_)/(1+Math.pow(Math.sin(phi_),2)),(a*Math.sin(phi_)*Math.cos(phi_)/(1+Math.pow(Math.sin(phi_),2)))*Math.cos(theta_),(a*Math.sin(phi_)*Math.cos(phi_)/(1+Math.pow(Math.sin(phi_),2)))*Math.sin(theta_));
		return new PointR3((a*Math.sin(phi_)*Math.cos(phi_)/(1+Math.pow(Math.sin(phi_),2)))*Math.sin(theta_),(a*Math.sin(phi_)*Math.cos(phi_)/(1+Math.pow(Math.sin(phi_),2)))*Math.cos(theta_),a*Math.cos(phi_)/(1+Math.pow(Math.sin(phi_),2)));
	}		
	public static PointR3 toCartesianFromCil(double rho_, double theta_, double z_){ // Cilindricas a Cartesianas
		return new PointR3(rho_*Math.cos(theta_),rho_*Math.sin(theta_),z_);
	}
	public PointR3(PointR3 p){
		this(p.x, p.y, p.z);
	}
	public PointR3(double[][] pArr){
		this(pArr[0][0], pArr[1][0], pArr[2][0]);
	}	
	public static final PointR3 getPointDiff(PointR3 pEnd, PointR3 pIni) {
		return new PointR3(pEnd.x-pIni.x, pEnd.y-pIni.y, pEnd.z-pIni.z);
	}
	public static final PointR3 getPointSum(PointR3 pIni, PointR3 pEnd) {
		return new PointR3(pEnd.x+pIni.x, pEnd.y+pIni.y, pEnd.z+pIni.z);
	}
	public static final PointR3 getPointAvg(PointR3 pIni, PointR3 pEnd) {
		return new PointR3((pEnd.x+pIni.x)/2, (pEnd.y+pIni.y)/2, (pEnd.z+pIni.z)/2);
	}	
	public static final PointR3 getProdVect(PointR3 U, PointR3 V) {
		return new PointR3(U.y*V.z-U.z*V.y, U.z*V.x-U.x*V.z, U.x*V.y-U.y*V.x);
	}	
	public PointR3 escalado(double escala) {
		return new PointR3(getX()*escala, getY()*escala, getZ()*escala);
	}
	public double getRho() {
		return rho;
	}
	public double getTheta() {
		return theta;
	}
	public double getPhi() {
		return phi;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getZ() {
		return z;
	}	
	public double[][] getArr() {
		double hArr[][]={{x},{y},{z}};
		return hArr;
	}
	public double[][] getHArr() {
		double hArr[][]={{x},{y},{z},{1.0}};
		return hArr;
	}	
}	

class AristR3 {
	PointR3 pIni, pEnd;
	public AristR3(PointR3 pIni_, PointR3 pEnd_) {
		pIni = pIni_;
		pEnd = pEnd_;
	}
	public AristR3(double xi_, double yi_, double zi_, double xf_, double yf_, double zf_) {
		pIni = new PointR3(xi_, yi_, zi_);
		pEnd = new PointR3(xf_, yf_, zf_);
	}	
	public PointR3 getIPoint() {
		return pIni;
	}
	public PointR3 getEPoint() {
		return pEnd;
	}
	public double getLength() {
		return Math.sqrt(Math.pow(pEnd.getX()-pIni.getX(),2)+Math.pow(pEnd.getY()-pIni.getY(),2)+Math.pow(pEnd.getZ()-pIni.getZ(),2));
	}
	public PointR3 getVersor() {
		return new PointR3((pEnd.getX()-pIni.getX())/getLength(),(pEnd.getY()-pIni.getY())/getLength(),(pEnd.getZ()-pIni.getZ())/getLength());
	}
}

class Triangulo implements Comparable<Triangulo> {
		AristR3 aR3[]= new AristR3[3];
		PointR3 pR3[]= new PointR3[3];
		Color fillColor = Color.GRAY, borderColor = Color.BLACK;
		double distPVO = -1.0;
		
		public Triangulo(AristR3 a1_, AristR3 a2_, AristR3 a3_) {
			aR3[0] = a1_;
			aR3[1] = a2_;
			aR3[2] = a3_;
			pR3[0] = aR3[0].getIPoint();
			pR3[1] = aR3[1].getIPoint();
			pR3[2] = aR3[2].getIPoint();			
		}
		public Triangulo(PointR3 p1_, PointR3 p2_, PointR3 p3_) {
			pR3[0] = p1_;
			pR3[1] = p2_;
			pR3[2] = p3_;
			aR3[0] = new AristR3(pR3[0], pR3[1]);
			aR3[1] = new AristR3(pR3[1], pR3[2]);
			aR3[2] = new AristR3(pR3[2], pR3[0]);			
		}
		public Triangulo(double[][] pArr, Color fillColor_) {
			this(new PointR3(pArr[0][0],pArr[1][0],pArr[2][0]), 
			     new PointR3(pArr[0][1],pArr[1][1],pArr[2][1]),
				 new PointR3(pArr[0][2],pArr[1][2],pArr[2][2]));
				 fillColor = fillColor_;
		}		
		public Triangulo(PointR3 p1_, PointR3 p2_, PointR3 p3_, Color fillColor_) {
			this(p1_, p2_, p3_);
			fillColor = fillColor_;
		}
		public PointR3 getNormal() {
			PointR3 U = PointR3.getPointDiff(pR3[1],pR3[0]);
			PointR3 V = PointR3.getPointDiff(pR3[2],pR3[1]);
			//PointR3 V = PointR3.getPointDiff(pR3[2],pR3[0]);
			return PointR3.getProdVect(U,V);
		}
		public boolean esPuntoDelPlano(PointR3 pSP) {
			double val = (new matrix()).prod_escalar(PointR3.getPointDiff(pSP,pR3[0]).getArr(), getNormal().getArr());
			return (val>-0.0005 && val<0.0005);
		}
		public boolean esPuntoInterior(PointR3 pST) {
			boolean estaIncluido = false;
			if (esPuntoDelPlano(pST)){ // Es punto del mismo plano resta ver si esta dentro del tiangulo
				PointR3 pV1 = (new AristR3(pST, pR3[0])).getVersor();
				PointR3 pV2 = (new AristR3(pST, pR3[1])).getVersor();
				PointR3 pV3 = (new AristR3(pST, pR3[2])).getVersor();
				double peV1V2 = (new matrix()).prod_escalar(pV1.getArr(), pV2.getArr());
				double peV2V3 = (new matrix()).prod_escalar(pV2.getArr(), pV3.getArr());
				double peV3V1 = (new matrix()).prod_escalar(pV3.getArr(), pV1.getArr());
				if (peV1V2 < 0.0 && peV2V3 < 0.0 && peV3V1 < 0.0) {
					estaIncluido = true;
				} else {
					double dSumaDeAngulos = Math.acos(peV1V2) + Math.acos(peV2V3) + Math.acos(peV3V1);
					if ( dSumaDeAngulos >= (2*Math.PI - 0.001) ) {
						//System.out.println("peV1V2="+peV1V2+", peV2V3="+peV2V3+", peV3V1="+peV3V1+"********* dSumaDeAngulos= "+dSumaDeAngulos);
						estaIncluido = true;
					}
				}
			}
			return estaIncluido;
		}
		public boolean esVisible(PointR3 PVO) {
			distPVO = new AristR3(pR3[0], PVO).getLength();
			return ((new matrix()).prod_escalar(PointR3.getPointDiff(PVO, pR3[0]).getArr(), getNormal().getArr())>0?true:false);
		}
		public PointR3[] getPointR3Arr() {
			return pR3;
		}
		public PointR3 getPoint(int nPoint) {
			if (nPoint < 1 || nPoint >3) return null;
			return pR3[nPoint-1];
		}
		public AristR3 getArist(int nArist) {
			if (nArist < 1 || nArist >3) return null;
			return aR3[nArist-1];
		}		
		public double[][] getHArr() {
			double hArr[][]={	{pR3[0].x,pR3[1].x,pR3[2].x},
								{pR3[0].y,pR3[1].y,pR3[2].y},
								{pR3[0].z,pR3[1].z,pR3[2].z},
								{1.0, 1.0, 1.0}};
			return hArr;
		}
		public Color getFillColor() {
			return fillColor;
		}
		public Color getBorderColor() {
			return borderColor;
		}
    // Override the compareTo() method
    @Override public int compareTo(Triangulo t) // Este comparador ordena de MAYOR a menor (orden Descendente).
    {
        if (distPVO > t.distPVO) {
            return -1;
        }
        else if (distPVO == t.distPVO) {
            return 0;
        }
        else {
            return 1;
        }
    }		
}

class Tetraedro {
		Triangulo tFace[]= new Triangulo[4];
		//PointR3 p1, p2, p3, p4;
		PointR3 pR3[]= new PointR3[4];
		public Tetraedro(Triangulo t1_, Triangulo t2_, Triangulo t3_, Triangulo t4_) {
			tFace[0] = t1_;
			tFace[1] = t2_;
			tFace[2] = t3_;
			tFace[3] = t4_;
			pR3[0] = tFace[0].getArist(1).getIPoint();
			pR3[1] = tFace[0].getArist(2).getIPoint();
			pR3[2] = tFace[0].getArist(3).getIPoint();
			pR3[3] = tFace[1].getArist(3).getIPoint();
		}
		public Tetraedro(PointR3 p1_, PointR3 p2_, PointR3 p3_, PointR3 p4_) {
			this(new Triangulo(p1_, p2_, p3_), new Triangulo(p1_, p3_, p4_), new Triangulo(p3_, p2_, p4_), new Triangulo(p2_, p1_, p4_));
		}
		public Tetraedro(double[][] pArr) {
			this(new PointR3(pArr[0][0],pArr[1][0],pArr[2][0]), new PointR3(pArr[0][1],pArr[1][1],pArr[2][1]),
				 new PointR3(pArr[0][2],pArr[1][2],pArr[2][2]), new PointR3(pArr[0][3],pArr[1][3],pArr[2][3]));
		}
		public PointR3 getFaceNormal(int nCara) {
			PointR3 U = PointR3.getPointDiff(tFace[nCara-1].getArist(1).getEPoint(),tFace[nCara-1].getArist(1).getIPoint());
			PointR3 V = PointR3.getPointDiff(tFace[nCara-1].getArist(2).getEPoint(),tFace[nCara-1].getArist(2).getIPoint());
			return PointR3.getProdVect(U,V);
		}
		public Triangulo getFace(int nFace){
			if (nFace < 1 || nFace >4) return null;
			return tFace[nFace-1];
		}
		public PointR3 getPoint(int nPoint) {
			if (nPoint < 1 || nPoint >4) return null;
			return pR3[nPoint-1];
		}		
		public double[][] getHArr() {
			double hArr[][]={	{pR3[0].x,pR3[1].x,pR3[2].x,pR3[3].x},
								{pR3[0].y,pR3[1].y,pR3[2].y,pR3[3].y},
								{pR3[0].z,pR3[1].z,pR3[2].z,pR3[3].z},
								{1.0, 1.0, 1.0, 1.0}};
			return hArr;
		}
	}
	
class Rectangulo {
		AristR3 aR3[]= new AristR3[4];
		PointR3 pR3[]= new PointR3[4];
		public Rectangulo(AristR3 a1_, AristR3 a2_, AristR3 a3_, AristR3 a4_) {
			aR3[0] = a1_;
			aR3[1] = a2_;
			aR3[2] = a3_;
			aR3[3] = a4_;
			pR3[0] = aR3[0].getIPoint();
			pR3[1] = aR3[1].getIPoint();
			pR3[2] = aR3[2].getIPoint();
			pR3[3] = aR3[3].getIPoint();			
		}
		public Rectangulo(PointR3 p1_, PointR3 p2_, PointR3 p3_, PointR3 p4_) {
			pR3[0] = p1_;
			pR3[1] = p2_;
			pR3[2] = p3_;
			pR3[3] = p4_;
			aR3[0] = new AristR3(pR3[0], pR3[1]);
			aR3[1] = new AristR3(pR3[1], pR3[2]);
			aR3[2] = new AristR3(pR3[2], pR3[3]);
			aR3[3] = new AristR3(pR3[3], pR3[0]);
		}
		public PointR3[] getPointR3Arr() {
			return pR3;
		}
		public PointR3 getPoint(int nPoint) {
			if (nPoint < 1 || nPoint >4) return null;
			return pR3[nPoint-1];
		}
		public AristR3 getArist(int nArist) {
			if (nArist < 1 || nArist >4) return null;
			return aR3[nArist-1];
		}		
		public double[][] getHArr() {
			double hArr[][]={	{pR3[0].x,pR3[1].x,pR3[2].x,pR3[3].x},
								{pR3[0].y,pR3[1].y,pR3[2].y,pR3[3].y},
								{pR3[0].z,pR3[1].z,pR3[2].z,pR3[3].z},
								{1.0, 1.0, 1.0, 1.0}};
			return hArr;
		}		
	}	
	
class Cubo {
		//Rectangulo r1, r2, r3, r4, r5, r6;
		Rectangulo rFace[]= new Rectangulo[6];
		//PointR3 p1, p2, p3, p4, p5, p6, p7, p8;
		PointR3 pR3[]= new PointR3[8];		
		public Cubo(Rectangulo r1_, Rectangulo r2_, Rectangulo r3_, Rectangulo r4_, Rectangulo r5_, Rectangulo r6_) {
			rFace[0] = r1_;
			rFace[1] = r2_;
			rFace[2] = r3_;
			rFace[3] = r4_;
			rFace[4] = r5_;
			rFace[5] = r6_;
			pR3[0] = rFace[0].getArist(1).getIPoint();
			pR3[1] = rFace[0].getArist(2).getIPoint();
			pR3[2] = rFace[0].getArist(3).getIPoint();
			pR3[3] = rFace[0].getArist(4).getIPoint();
			pR3[4] = rFace[1].getArist(1).getIPoint();
			pR3[5] = rFace[1].getArist(2).getIPoint();
			pR3[6] = rFace[1].getArist(3).getIPoint();
			pR3[7] = rFace[1].getArist(4).getIPoint();			
		}
		public Cubo(PointR3 p1_, PointR3 p2_, PointR3 p3_, PointR3 p4_, PointR3 p5_, PointR3 p6_, PointR3 p7_, PointR3 p8_) {
			this(new Rectangulo(p1_, p2_, p3_, p4_), new Rectangulo(p5_, p6_, p7_, p8_), new Rectangulo(p5_, p8_, p2_, p1_), new Rectangulo(p1_, p4_, p6_, p5_), new Rectangulo(p4_, p3_, p7_, p6_), new Rectangulo(p8_, p7_, p3_, p2_));
		}
		public Cubo(double[][] pArr) {
			this(new PointR3(pArr[0][0],pArr[1][0],pArr[2][0]), new PointR3(pArr[0][1],pArr[1][1],pArr[2][1]),
				 new PointR3(pArr[0][2],pArr[1][2],pArr[2][2]), new PointR3(pArr[0][3],pArr[1][3],pArr[2][3]),
				 new PointR3(pArr[0][4],pArr[1][4],pArr[2][4]), new PointR3(pArr[0][5],pArr[1][5],pArr[2][5]),
				 new PointR3(pArr[0][6],pArr[1][6],pArr[2][6]), new PointR3(pArr[0][7],pArr[1][7],pArr[2][7]));
		}
		public PointR3 getFaceNormal(int nCara) {
			PointR3 U = PointR3.getPointDiff(rFace[nCara-1].getArist(1).getEPoint(),rFace[nCara-1].getArist(1).getIPoint());
			PointR3 V = PointR3.getPointDiff(rFace[nCara-1].getArist(2).getEPoint(),rFace[nCara-1].getArist(2).getIPoint());
			return PointR3.getProdVect(U,V);
		}
		public Rectangulo getFace(int nFace){
			if (nFace < 1 || nFace >6) return null;
			return rFace[nFace-1];
		}
		public PointR3 getPoint(int nPoint) {
			if (nPoint < 1 || nPoint >8) return null;
			return pR3[nPoint-1];
		}		
		public double[][] getHArr() {
			double hArr[][]={	{pR3[0].x,pR3[1].x,pR3[2].x,pR3[3].x,pR3[4].x,pR3[5].x,pR3[6].x,pR3[7].x},
								{pR3[0].y,pR3[1].y,pR3[2].y,pR3[3].y,pR3[4].y,pR3[5].y,pR3[6].y,pR3[7].y},
								{pR3[0].z,pR3[1].z,pR3[2].z,pR3[3].z,pR3[4].z,pR3[5].z,pR3[6].z,pR3[7].z},
								{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0}};
			return hArr;
		}			
	}

class Pentagono {
		AristR3 aR3[]= new AristR3[5];
		PointR3 pR3[]= new PointR3[5];
		public Pentagono(AristR3 a1_, AristR3 a2_, AristR3 a3_, AristR3 a4_, AristR3 a5_) {
			aR3[0] = a1_;
			aR3[1] = a2_;
			aR3[2] = a3_;
			aR3[3] = a4_;
			aR3[4] = a5_;
			pR3[0] = aR3[0].getIPoint();
			pR3[1] = aR3[1].getIPoint();
			pR3[2] = aR3[2].getIPoint();
			pR3[3] = aR3[3].getIPoint();
			pR3[4] = aR3[4].getIPoint();
		}
		public Pentagono(PointR3 p1_, PointR3 p2_, PointR3 p3_, PointR3 p4_, PointR3 p5_) {
			pR3[0] = p1_;
			pR3[1] = p2_;
			pR3[2] = p3_;
			pR3[3] = p4_;
			pR3[4] = p5_;
			aR3[0] = new AristR3(pR3[0], pR3[1]);
			aR3[1] = new AristR3(pR3[1], pR3[2]);
			aR3[2] = new AristR3(pR3[2], pR3[3]);
			aR3[3] = new AristR3(pR3[3], pR3[4]);
			aR3[4] = new AristR3(pR3[4], pR3[0]);
		}
		public PointR3[] getPointR3Arr() {
			return pR3;
		}
		public PointR3 getPoint(int nPoint) {
			if (nPoint < 1 || nPoint >5) return null;
			return pR3[nPoint-1];
		}
		public AristR3 getArist(int nArist) {
			if (nArist < 1 || nArist >5) return null;
			return aR3[nArist-1];
		}		
		public double[][] getHArr() {
			double hArr[][]={	{pR3[0].x,pR3[1].x,pR3[2].x,pR3[3].x,pR3[4].x},
								{pR3[0].y,pR3[1].y,pR3[2].y,pR3[3].y,pR3[4].y},
								{pR3[0].z,pR3[1].z,pR3[2].z,pR3[3].z,pR3[4].z},
								{1.0, 1.0, 1.0, 1.0, 1.0}};
			return hArr;
		}		
	}
	
class Dodecaedro {
		Pentagono pFace[]= new Pentagono[12];
		PointR3 pR3[]= new PointR3[20];	
		public Dodecaedro(Pentagono p1_, Pentagono p2_, Pentagono p3_, Pentagono p4_, Pentagono p5_, Pentagono p6_, Pentagono p7_, Pentagono p8_, Pentagono p9_, Pentagono p10_, Pentagono p11_, Pentagono p12_) {
			pFace[0] = p1_;
			pFace[1] = p2_;
			pFace[2] = p3_;
			pFace[3] = p4_;
			pFace[4] = p5_;
			pFace[5] = p6_;
			pFace[6] = p7_;
			pFace[7] = p8_;
			pFace[8] = p9_;
			pFace[9] = p10_;
			pFace[10] = p11_;
			pFace[11] = p12_;			
			pR3[0] = pFace[0].getPoint(1);
			pR3[1] = pFace[0].getPoint(2);
			pR3[2] = pFace[0].getPoint(3);
			pR3[3] = pFace[0].getPoint(4);
			pR3[4] = pFace[0].getPoint(5);
			pR3[5] = pFace[1].getPoint(3);
			pR3[6] = pFace[1].getPoint(4);
			pR3[7] = pFace[1].getPoint(5);
			pR3[8] = pFace[5].getPoint(4);
			pR3[9] = pFace[5].getPoint(5);
			pR3[10] = pFace[4].getPoint(4);
			pR3[11] = pFace[4].getPoint(5);
			pR3[12] = pFace[3].getPoint(4);
			pR3[13] = pFace[3].getPoint(5);
			pR3[14] = pFace[2].getPoint(4);			
			pR3[15] = pFace[11].getPoint(1);
			pR3[16] = pFace[11].getPoint(2);
			pR3[17] = pFace[11].getPoint(3);
			pR3[18] = pFace[11].getPoint(4);
			pR3[19] = pFace[11].getPoint(5);			
		}
		
		public Dodecaedro(PointR3 p1_, PointR3 p2_, PointR3 p3_, PointR3 p4_, PointR3 p5_, PointR3 p6_, PointR3 p7_, PointR3 p8_, PointR3 p9_, PointR3 p10_, PointR3 p11_, PointR3 p12_, PointR3 p13_, PointR3 p14_, PointR3 p15_, PointR3 p16_, PointR3 p17_, PointR3 p18_, PointR3 p19_, PointR3 p20_) {
			this(new Pentagono(p1_, p2_, p3_, p4_, p5_), new Pentagono(p2_, p1_, p6_, p7_, p8_), new Pentagono(p1_, p5_, p14_, p15_, p6_), new Pentagono(p5_, p4_, p12_, p13_, p14_), 
			new Pentagono(p4_, p3_, p10_, p11_, p12_), new Pentagono(p3_, p2_, p8_, p9_, p10_), new Pentagono(p10_, p9_, p19_, p18_, p11_), new Pentagono(p19_, p9_, p8_, p7_, p20_),
			new Pentagono(p20_, p7_, p6_, p15_, p16_), new Pentagono(p16_, p15_, p14_, p13_, p17_), new Pentagono(p17_, p13_, p12_, p11_, p18_), new Pentagono(p16_, p17_, p18_, p19_, p20_));
		}
		public Dodecaedro(double[][] pArr) {
			this(new PointR3(pArr[0][0],pArr[1][0],pArr[2][0]), new PointR3(pArr[0][1],pArr[1][1],pArr[2][1]),
				 new PointR3(pArr[0][2],pArr[1][2],pArr[2][2]), new PointR3(pArr[0][3],pArr[1][3],pArr[2][3]),
				 new PointR3(pArr[0][4],pArr[1][4],pArr[2][4]), new PointR3(pArr[0][5],pArr[1][5],pArr[2][5]),
				 new PointR3(pArr[0][6],pArr[1][6],pArr[2][6]), new PointR3(pArr[0][7],pArr[1][7],pArr[2][7]),
				 new PointR3(pArr[0][8],pArr[1][8],pArr[2][8]), new PointR3(pArr[0][9],pArr[1][9],pArr[2][9]),
				 new PointR3(pArr[0][10],pArr[1][10],pArr[2][10]), new PointR3(pArr[0][11],pArr[1][11],pArr[2][11]),
				 new PointR3(pArr[0][12],pArr[1][12],pArr[2][12]), new PointR3(pArr[0][13],pArr[1][13],pArr[2][13]),
				 new PointR3(pArr[0][14],pArr[1][14],pArr[2][14]), new PointR3(pArr[0][15],pArr[1][15],pArr[2][15]),
				 new PointR3(pArr[0][16],pArr[1][16],pArr[2][16]), new PointR3(pArr[0][17],pArr[1][17],pArr[2][17]),
				 new PointR3(pArr[0][18],pArr[1][18],pArr[2][18]), new PointR3(pArr[0][19],pArr[1][19],pArr[2][19]));
		}
		public Dodecaedro(double[][] pArr, double escala) {
			this(new PointR3(escala*pArr[0][0],escala*pArr[1][0],escala*pArr[2][0]), new PointR3(escala*pArr[0][1],escala*pArr[1][1],escala*pArr[2][1]),
				 new PointR3(escala*pArr[0][2],escala*pArr[1][2],escala*pArr[2][2]), new PointR3(escala*pArr[0][3],escala*pArr[1][3],escala*pArr[2][3]),
				 new PointR3(escala*pArr[0][4],escala*pArr[1][4],escala*pArr[2][4]), new PointR3(escala*pArr[0][5],escala*pArr[1][5],escala*pArr[2][5]),
				 new PointR3(escala*pArr[0][6],escala*pArr[1][6],escala*pArr[2][6]), new PointR3(escala*pArr[0][7],escala*pArr[1][7],escala*pArr[2][7]),
				 new PointR3(escala*pArr[0][8],escala*pArr[1][8],escala*pArr[2][8]), new PointR3(escala*pArr[0][9],escala*pArr[1][9],escala*pArr[2][9]),
				 new PointR3(escala*pArr[0][10],escala*pArr[1][10],escala*pArr[2][10]), new PointR3(escala*pArr[0][11],escala*pArr[1][11],escala*pArr[2][11]),
				 new PointR3(escala*pArr[0][12],escala*pArr[1][12],escala*pArr[2][12]), new PointR3(escala*pArr[0][13],escala*pArr[1][13],escala*pArr[2][13]),
				 new PointR3(escala*pArr[0][14],escala*pArr[1][14],escala*pArr[2][14]), new PointR3(escala*pArr[0][15],escala*pArr[1][15],escala*pArr[2][15]),
				 new PointR3(escala*pArr[0][16],escala*pArr[1][16],escala*pArr[2][16]), new PointR3(escala*pArr[0][17],escala*pArr[1][17],escala*pArr[2][17]),
				 new PointR3(escala*pArr[0][18],escala*pArr[1][18],escala*pArr[2][18]), new PointR3(escala*pArr[0][19],escala*pArr[1][19],escala*pArr[2][19]));
		}		
		public PointR3 getFaceNormal(int nCara) {
			PointR3 U = PointR3.getPointDiff(pFace[nCara-1].getArist(1).getEPoint(),pFace[nCara-1].getArist(1).getIPoint());
			PointR3 V = PointR3.getPointDiff(pFace[nCara-1].getArist(2).getEPoint(),pFace[nCara-1].getArist(2).getIPoint());
			return PointR3.getProdVect(U,V);
		}
		public PointR3 getFaceNormalVersor(int nCara) {
			PointR3 U = getFace(nCara).getArist(1).getVersor();
			PointR3 V = getFace(nCara).getArist(2).getVersor();
			return PointR3.getProdVect(U,V);
		}		
		public Pentagono getFace(int nFace){
			if (nFace < 1 || nFace >12) return null;
			return pFace[nFace-1];
		}
		public PointR3 getPoint(int nPoint) {
			if (nPoint < 1 || nPoint >20) return null;
			return pR3[nPoint-1];
		}		
		public void escalado(double escala) {
			for (int i=0; i<20; i++) {
				pR3[i] = pR3[i].escalado(escala);
			}
		}		
		public double[][] getHArr() {
			double hArr[][]={	{pR3[0].x,pR3[1].x,pR3[2].x,pR3[3].x,pR3[4].x,pR3[5].x,pR3[6].x,pR3[7].x,pR3[8].x,pR3[9].x,pR3[10].x,pR3[11].x,pR3[12].x,pR3[13].x,pR3[14].x,pR3[15].x,pR3[16].x,pR3[17].x,pR3[18].x,pR3[19].x},
								{pR3[0].y,pR3[1].y,pR3[2].y,pR3[3].y,pR3[4].y,pR3[5].y,pR3[6].y,pR3[7].y,pR3[8].y,pR3[9].y,pR3[10].y,pR3[11].y,pR3[12].y,pR3[13].y,pR3[14].y,pR3[15].y,pR3[16].y,pR3[17].y,pR3[18].y,pR3[19].y},
								{pR3[0].z,pR3[1].z,pR3[2].z,pR3[3].z,pR3[4].z,pR3[5].z,pR3[6].z,pR3[7].z,pR3[8].z,pR3[9].z,pR3[10].z,pR3[11].z,pR3[12].z,pR3[13].z,pR3[14].z,pR3[15].z,pR3[16].z,pR3[17].z,pR3[18].z,pR3[19].z},
								{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0}};
			return hArr;
		}		
}

/*
class Sphere {
	PointR3 center;
	double radius;
	int vSteps,hSteps;
	Random rand = new Random();
	Color fillColor;
	LinkedList<Triangulo> surface = new LinkedList<Triangulo>();
	public Sphere(PointR3 pCenter, double dRadius, int iVSteps, int iHSteps, Color fillColor_) {
		center = pCenter;
		radius = dRadius;
		vSteps = iVSteps;
		hSteps = iHSteps;
		fillColor = fillColor_;
		double vArcLen = Math.PI / vSteps;
		double hArcLen = 2*Math.PI / hSteps;
		for (int i=0; i < vSteps; i++) {
			for (int j=0; j < hSteps; j++) {
				if (i==0) { // Head FAN
					surface.add(new Triangulo(PointR3.getPointSum(center, new PointR3(0,0,radius)), PointR3.getPointSum(center, PointR3.toCartesian(radius,hArcLen*j,vArcLen*(i+1))), PointR3.getPointSum(center, PointR3.toCartesian(radius,hArcLen*(j+1),vArcLen*(i+1))), (fillColor!=null?fillColor:new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()))));
				} else if (i<(vSteps-1)) { // Body
					surface.add(new Triangulo(PointR3.getPointSum(center, PointR3.toCartesian(radius,hArcLen*j,vArcLen*i)), PointR3.getPointSum(center, PointR3.toCartesian(radius,hArcLen*j,vArcLen*(i+1))), PointR3.getPointSum(center, PointR3.toCartesian(radius,hArcLen*(j+1),vArcLen*(i+1))), (fillColor!=null?fillColor:new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()))));
					surface.add(new Triangulo(PointR3.getPointSum(center, PointR3.toCartesian(radius,hArcLen*(j+1),vArcLen*(i+1))), PointR3.getPointSum(center, PointR3.toCartesian(radius,hArcLen*(j+1),vArcLen*i)), PointR3.getPointSum(center, PointR3.toCartesian(radius,hArcLen*j,vArcLen*i)), (fillColor!=null?fillColor:new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()))));
				} else { // Foot FAN
					//surface.add(new Triangulo(PointR3.toCartesian(radius,hArcLen*j,vArcLen*i), new PointR3(0,0,-radius), new PointR3(radius,hArcLen*(j+1),vArcLen*i), (fillColor!=null?fillColor:new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()))));
					surface.add(new Triangulo(PointR3.getPointSum(center, PointR3.toCartesian(radius,hArcLen*j,vArcLen*i)), PointR3.getPointSum(center, PointR3.toCartesian(radius,hArcLen*j,vArcLen*(i+1))), PointR3.getPointSum(center, PointR3.toCartesian(radius,hArcLen*(j+1),vArcLen*i)), (fillColor!=null?fillColor:new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()))));
				}					
			}
		}
	}
	public int size() {
		return surface.size();
	}
	public Triangulo getElem(int iPos) {
		return surface.get(iPos);
	}
	public void setElem(int iPos, Triangulo t) {
		surface.set(iPos, t);
	}
}
*/

class Elipsoide {
	PointR3 center;
	double radX, radY, radZ;
	int vSteps,hSteps;
	Random rand = new Random();
	Color fillColor;
	LinkedList<Triangulo> surface = new LinkedList<Triangulo>();
	public Elipsoide(PointR3 pCenter, double dRx, double dRy, double dRz, int iVSteps, int iHSteps, Color fillColor_) {
		center = pCenter;
		radX = dRx;
		radY = dRy;
		radZ = dRz;
		vSteps = iVSteps;
		hSteps = iHSteps;
		fillColor = fillColor_;
		double vArcLen = Math.PI / vSteps;
		double hArcLen = 2*Math.PI / hSteps;
		for (int i=0; i < vSteps; i++) {
			for (int j=0; j < hSteps; j++) {
				if (i==0) { // Head FAN
					surface.add(new Triangulo(PointR3.getPointSum(center, new PointR3(0,0,radZ)), PointR3.getPointSum(center, PointR3.toCartesian(radX, radY, radZ,hArcLen*j,vArcLen*(i+1))), PointR3.getPointSum(center, PointR3.toCartesian(radX, radY, radZ, hArcLen*(j+1),vArcLen*(i+1))), (fillColor!=null?fillColor:new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()))));
				} else if (i<(vSteps-1)) { // Body
					surface.add(new Triangulo(PointR3.getPointSum(center, PointR3.toCartesian(radX, radY, radZ, hArcLen*j,vArcLen*i)), PointR3.getPointSum(center, PointR3.toCartesian(radX, radY, radZ, hArcLen*j,vArcLen*(i+1))), PointR3.getPointSum(center, PointR3.toCartesian(radX, radY, radZ, hArcLen*(j+1),vArcLen*(i+1))), (fillColor!=null?fillColor:new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()))));
					surface.add(new Triangulo(PointR3.getPointSum(center, PointR3.toCartesian(radX, radY, radZ, hArcLen*(j+1),vArcLen*(i+1))), PointR3.getPointSum(center, PointR3.toCartesian(radX, radY, radZ, hArcLen*(j+1),vArcLen*i)), PointR3.getPointSum(center, PointR3.toCartesian(radX, radY, radZ, hArcLen*j,vArcLen*i)), (fillColor!=null?fillColor:new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()))));
				} else { // Foot FAN
					surface.add(new Triangulo(PointR3.getPointSum(center, PointR3.toCartesian(radX, radY, radZ, hArcLen*j,vArcLen*i)), PointR3.getPointSum(center, PointR3.toCartesian(radX, radY, radZ, hArcLen*j,vArcLen*(i+1))), PointR3.getPointSum(center, PointR3.toCartesian(radX, radY, radZ, hArcLen*(j+1),vArcLen*i)), (fillColor!=null?fillColor:new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()))));
				}					
			}
		}
	}
	public int size() {
		return surface.size();
	}
	public Triangulo getElem(int iPos) {
		return surface.get(iPos);
	}
	public void setElem(int iPos, Triangulo t) {
		surface.set(iPos, t);
	}
}

class Esfera extends Elipsoide {
	public Esfera(PointR3 pCenter, double dRadius, int iVSteps, int iHSteps, Color fillColor_) {
		super(pCenter, dRadius, dRadius, dRadius, iVSteps, iHSteps, fillColor_);
	}
}

class Toro {
	PointR3 center;
	double radMayor, radMenor;
	int vSteps,hSteps;
	Random rand = new Random();
	Color fillColor;
	LinkedList<Triangulo> surface = new LinkedList<Triangulo>();
	public Toro(PointR3 pCenter, double dR, double dr, int iVSteps, int iHSteps, Color fillColor_) {
		center = pCenter;
		radMayor = dR;
		radMenor = dr;
		vSteps = iVSteps;
		hSteps = iHSteps;
		fillColor = fillColor_;
		double vArcLen = 2*Math.PI / vSteps;
		double hArcLen = 2*Math.PI / hSteps;
		for (int i=0; i < vSteps; i++) {
			for (int j=0; j < hSteps; j++) {
				surface.add(new Triangulo(PointR3.getPointSum(center, PointR3.toCartesian(radMayor, radMenor, hArcLen*j,vArcLen*i)), PointR3.getPointSum(center, PointR3.toCartesian(radMayor, radMenor, hArcLen*(j+1),vArcLen*(i+1))), PointR3.getPointSum(center, PointR3.toCartesian(radMayor, radMenor, hArcLen*j,vArcLen*(i+1))), (fillColor!=null?fillColor:new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()))));
				surface.add(new Triangulo(PointR3.getPointSum(center, PointR3.toCartesian(radMayor, radMenor, hArcLen*(j+1),vArcLen*(i+1))), PointR3.getPointSum(center, PointR3.toCartesian(radMayor, radMenor, hArcLen*j,vArcLen*i)), PointR3.getPointSum(center, PointR3.toCartesian(radMayor, radMenor, hArcLen*(j+1),vArcLen*i)), (fillColor!=null?fillColor:new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()))));
			}
		}
	}
	public int size() {
		return surface.size();
	}
	public Triangulo getElem(int iPos) {
		return surface.get(iPos);
	}
	public void setElem(int iPos, Triangulo t) {
		surface.set(iPos, t);
	}
}

class Ocho {
	PointR3 center;
	double size;
	int vSteps,hSteps;
	Random rand = new Random();
	Color fillColor;
	LinkedList<Triangulo> surface = new LinkedList<Triangulo>();
	public Ocho(PointR3 pCenter, double t, int iVSteps, int iHSteps, Color fillColor_) {
		center = pCenter;
		size = t;
		vSteps = iVSteps;
		hSteps = iHSteps;
		fillColor = fillColor_;
		double vArcLen = 2*Math.PI / vSteps;
		double hArcLen = 2*Math.PI / hSteps;
		for (int i=0; i < vSteps; i++) {
			for (int j=0; j < hSteps; j++) {
				surface.add(new Triangulo(PointR3.getPointSum(center, PointR3.toCartesianF8(size, hArcLen*j,vArcLen*i)), PointR3.getPointSum(center, PointR3.toCartesianF8(size, hArcLen*(j+1),vArcLen*(i+1))), PointR3.getPointSum(center, PointR3.toCartesianF8(size, hArcLen*j,vArcLen*(i+1))), (fillColor!=null?fillColor:new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()))));
				surface.add(new Triangulo(PointR3.getPointSum(center, PointR3.toCartesianF8(size, hArcLen*(j+1),vArcLen*(i+1))), PointR3.getPointSum(center, PointR3.toCartesianF8(size, hArcLen*j,vArcLen*i)), PointR3.getPointSum(center, PointR3.toCartesianF8(size, hArcLen*(j+1),vArcLen*i)), (fillColor!=null?fillColor:new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()))));
			}
		}
	}
	public int size() {
		return surface.size();
	}
	public Triangulo getElem(int iPos) {
		return surface.get(iPos);
	}
	public void setElem(int iPos, Triangulo t) {
		surface.set(iPos, t);
	}
}

class Piriforme {
	PointR3 center;
	double radMayor, radMenor;
	int vSteps,hSteps;
	Random rand = new Random();
	Color fillColor;
	LinkedList<Triangulo> surface = new LinkedList<Triangulo>();
	public Piriforme(PointR3 pCenter, double a, double b, int iVSteps, int iHSteps, Color fillColor_) {
		center = pCenter;
		radMayor = a;
		radMenor = b;
		vSteps = iVSteps;
		hSteps = iHSteps;
		fillColor = fillColor_;
		double vArcLen = 2*Math.PI / vSteps;
		double hArcLen = 2*Math.PI / hSteps;
		for (int i=0; i < vSteps; i++) {
			for (int j=0; j < hSteps; j++) {
				surface.add(new Triangulo(PointR3.getPointSum(center, PointR3.toCartesianPF(radMayor, radMenor, hArcLen*j,vArcLen*i)), PointR3.getPointSum(center, PointR3.toCartesianPF(radMayor, radMenor, hArcLen*(j+1),vArcLen*(i+1))), PointR3.getPointSum(center, PointR3.toCartesianPF(radMayor, radMenor, hArcLen*j,vArcLen*(i+1))), (fillColor!=null?fillColor:new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()))));
				surface.add(new Triangulo(PointR3.getPointSum(center, PointR3.toCartesianPF(radMayor, radMenor, hArcLen*(j+1),vArcLen*(i+1))), PointR3.getPointSum(center, PointR3.toCartesianPF(radMayor, radMenor, hArcLen*j,vArcLen*i)), PointR3.getPointSum(center, PointR3.toCartesianPF(radMayor, radMenor, hArcLen*(j+1),vArcLen*i)), (fillColor!=null?fillColor:new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()))));
			}
		}
	}
	public int size() {
		return surface.size();
	}
	public Triangulo getElem(int iPos) {
		return surface.get(iPos);
	}
	public void setElem(int iPos, Triangulo t) {
		surface.set(iPos, t);
	}
}

class Lemniscata {
	PointR3 center;
	double a;
	int vSteps,hSteps;
	Random rand = new Random();
	Color fillColor;
	LinkedList<Triangulo> surface = new LinkedList<Triangulo>();
	public Lemniscata(PointR3 pCenter, double a_, int iVSteps, int iHSteps, Color fillColor_) {
		center = pCenter;
		a = a_;
		vSteps = iVSteps;
		hSteps = iHSteps;
		fillColor = fillColor_;
		double vArcLen = Math.PI / vSteps;
		double hArcLen = 2*Math.PI / hSteps;
		for (int i=0; i < vSteps; i++) {
			for (int j=0; j < hSteps; j++) {
				surface.add(new Triangulo(PointR3.getPointSum(center, PointR3.toCartesianLem(a, hArcLen*j,vArcLen*i)), PointR3.getPointSum(center, PointR3.toCartesianLem(a, hArcLen*(j+1),vArcLen*(i+1))), PointR3.getPointSum(center, PointR3.toCartesianLem(a, hArcLen*j,vArcLen*(i+1))), (fillColor!=null?fillColor:new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()))));
				surface.add(new Triangulo(PointR3.getPointSum(center, PointR3.toCartesianLem(a, hArcLen*(j+1),vArcLen*(i+1))), PointR3.getPointSum(center, PointR3.toCartesianLem(a, hArcLen*j,vArcLen*i)), PointR3.getPointSum(center, PointR3.toCartesianLem(a, hArcLen*(j+1),vArcLen*i)), (fillColor!=null?fillColor:new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()))));
			}
		}
	}
	public int size() {
		return surface.size();
	}
	public Triangulo getElem(int iPos) {
		return surface.get(iPos);
	}
	public void setElem(int iPos, Triangulo t) {
		surface.set(iPos, t);
	}
}