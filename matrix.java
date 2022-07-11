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
public class matrix {
	public double [][] producto(double a[][], double b[][]) {
		if (a[0].length != b.length) return null;
		int filas = a.length;
		int columnas = b[0].length;
		double c[][]=new double[a.length][b[0].length];		
		// MxN * NxP = MxP , M=a.length , N=a[0].length=b.length , P=b[0].length
		for (int k=0; k < columnas; k++) {
			for (int i=0; i < filas; i++) {
				c[i][k]=0;
				for (int j=0; j < b.length; j++) {
					c[i][k]+=a[i][j]*b[j][k];
				}
			}
		}	
		return c;
	}

    public double [][] identidad(int dim) {
		double c[][]=new double[dim][dim];
		for (int i=0; i < dim; i++) {
			for (int j=0; j < dim; j++) {
				c[i][j] = (i==j?1.0:0.0);
			}
		}
		return c;
	}
	
	public double [][] division(double a[][], double b[][]) {
		return producto(a,inversa(b));
	}
	
	public double [][] potencia(double a[][], int pot) {
		double aux[][];	
		if (pot < 0) {
			if (determinante(a) == 0) return null;
			aux = inversa(a);
			pot = -pot;
		} else {
			aux = a;
		}
		double c[][]=new double[a.length][a.length];		
		c = (pot==0?identidad(a.length):aux);
		for (int i=1; i < pot; i++) {
			c = this.producto(aux,c);
		}
		return c;
	}
	
	public double [][] suma(double a[][], double b[][]) {
		if (a.length != b.length || a[0].length != b[0].length) {
			System.out.println("No es posible sumar matrices de diferente orden o diferente cantidad de filas y columnas");
			return null;
		}
		double c[][]=new double[a.length][a[0].length];
		for (int i=0; i < a.length; i++) {
			for (int j=0; j < a[0].length; j++) {
				c[i][j] = a[i][j]+b[i][j];
			}
		}
		return c;
	}
	
	public double [][] resta(double a[][], double b[][]) {
		if (a.length != b.length || a[0].length != b[0].length) {
			System.out.println("No es posible restar matrices de diferente orden o diferente cantidad de filas y columnas");
			return null;
		}
		double c[][]=new double[a.length][a[0].length];
		for (int i=0; i < a.length; i++) {
			for (int j=0; j < a[0].length; j++) {
				c[i][j] = a[i][j]-b[i][j];
			}
		}
		return c;
	}	

	public double [][] escalado(double a[][], double escalar) {
		double c[][]=new double[a.length][a[0].length];
		for (int i=0; i < a.length; i++) {
			for (int j=0; j < a[0].length; j++) {
				c[i][j] = escalar*a[i][j];
			}
		}
		return c;	
	}
	
	public double prod_escalar(double a[][], double b[][]) { 
		if (a.length!=b.length || a[0].length!=b[0].length) return 0.0; // Mejor: Lanzar una excepción
		if (a.length == 1) {
			return producto(a, transpuesta(b))[0][0];
		} else {
			return producto(transpuesta(a),b)[0][0];
		}
	}
	
	public double determinante(double a[][]) {
		double calculo = 0;
		//int submat[][];
		if (a.length == 1) {
			calculo = a[0][0];
		} else {
			for (int col=0; col < a.length; col++) {
				calculo+=(Math.pow(-1,(col/*+2*/))*a[0][col]*determinante(submat(a,0,col)));
			}
		}
		return calculo;
	}
	
	public double [][] submat(double a[][], int fil, int col) {
	    if ((fil!=-1 && (a.length-1) < 1) || (col!=-1 && (a[0].length-1) < 1) || fil >= a.length || col >= a[0].length ) return null;
		double c[][] = new double[(fil<0?a.length:a.length-1)][(col<0?a[0].length:a[0].length-1)];
		for (int i=0; i < a.length; i++) {
			for (int j=0; j < a[0].length; j++) {
				if (i == fil || j == col) continue;
				c[(fil<0||i<fil?i:i-1)][(col<0||j<col?j:j-1)] = a[i][j];
			}
		}
		return c;
	}

	public double [][] fila(double a[][], int fila) {
		if (fila > a.length) return null;
		double c[][] = new double[1][a[0].length];
		for (int j=0; j < a[0].length; j++) {
			c[0][j] = a[fila][j];
		}
		return c;
	}
	
	public double [][] columna(double a[][], int columna) {
		if (columna > a[0].length) return null;
		double c[][] = new double[a.length][1];
		for (int i=0; i < a.length; i++) {
			c[i][0] = a[i][columna];
		}
		return c;
	}	
	
	public double [][] diagonal (double a[][], int distDiagPpal) {
		if (a.length!=a[0].length || Math.abs(distDiagPpal) >= a.length) {
			System.out.println("Error: No se puede extraer la diagonal solicitada para la matriz informada");
			return null;
		}
		double diag[][] = new double[1][a.length-Math.abs(distDiagPpal)];
		int k = 0;
		for (int i=0; i < a.length; i++) {
			for (int j=0; j < a[0].length; j++) {
				if ((i+distDiagPpal) == j) {
					diag[0][k] = a[i][j];
					k++;
				}
			}
		}
		return diag;
	}
	
	public double traza(double a[][]) {
		double diag[][] = diagonal(a, 0);
		if (diag == null) {
			System.out.println("Error: No se puede obtener ña traza de esta matriz.");
			return 0.0;
		}
		double sumaDiagPpal = 0;
		for (int j=0; j < a.length; j++) {
			sumaDiagPpal += diag[0][j];
		}		
		return sumaDiagPpal;
	}
		
	public boolean simetrica (double a[][]) { // Retorna el booleano true si es simetrica y false si no lo es.
		if (a.length!=a[0].length) {
			System.out.print("Error: La matriz informada no es cuadrada, por estructura no es simetrica.-->");
			return false;
		}
		for (int i=1; i < a.length; i++) {
			double diagS[][] = diagonal(a,i);//new double[1][a.length-Math.abs(i)];
			double diagI[][] = diagonal(a,-i);
			for (int j=0; j < diagS[0].length; j++) {
				if (diagS[0][j] != diagI[0][j]) {
					return false;
				}
			}
		}
		return true;
	}
	
	public double [][] cofactor(double a[][]) {
		double c[][] = new double[a.length][a.length];
		for (int i=0; i < a.length; i++) {
			for (int j=0; j < a.length; j++) {
				c[i][j]=Math.pow(-1,(i+j/*+2*/))*determinante(submat(a,i,j));
			}
		}	
		return c;
	}

    public int rango(double a[][]) {
		int maxRg = 0;		
		if (a.length!=a[0].length) {
			System.out.println("Por ahora solo se calculan rangos de matrices cuadradas.");
			return -1;
		} else {
			if (determinante(a)!=0.0) {
				maxRg = a.length;
			} else {
				if (a.length > 1) {
					int valRg = 0;
					for (int i=0; i < a.length; i++) {
						for (int j=0; j < a.length; j++) {
							if((valRg=rango(submat(a,i,j)))!=0) {
								maxRg=(valRg>maxRg?valRg:maxRg);
							}
						}
					}	
				}
			}
		}
		return maxRg;
	}
	
	public double [][] transpuesta(double a[][]) {
		double c[][] = new double[a[0].length][a.length];
		for (int i=0; i < a.length; i++) {
			for (int j=0; j < a[0].length; j++) {
				c[j][i]=a[i][j];
			}
		}	
		return c;
	}

	public double [][] adjunta(double a[][]) {
		return transpuesta(cofactor(a));
	}

	public double [][] inversa(double a[][]) {
		double det = determinante(a);
		double c[][] = adjunta(a);
		for (int i=0; i < a.length; i++) {
			for (int j=0; j < a.length; j++) {
				c[i][j]=c[i][j] / det;
			}
		}	
		return c;		
	}
	
	static void printMat (double c[][]) {
		for (int fila=0; fila < c.length; fila++) {
			for (int col=0; col < c[0].length; col++) {
				System.out.print("C("+(fila+1)+","+(col+1)+")="+c[fila][col]+"  ");
			}
			System.out.println();
		}		
	}
	
	public double [][] roundMat (double a[][]) {
		double c[][] = new double[a.length][a[0].length];
		for (int fila=0; fila < a.length; fila++) {
			for (int col=0; col < a[0].length; col++) {
				double dDiff = Math.abs(a[fila][col] - (double)Math.round(a[fila][col]));
				if ( dDiff < 0.0001 ) {
					c[fila][col] = (double)Math.round(a[fila][col]);
				} else {
					double aux = 0.0;
					int i = 0;
					for (i = 1; i < 5; i++) {
						aux = Math.pow(10.0,(double)i) * a[fila][col];
						dDiff = Math.abs(aux - (double)Math.round(aux));
						if (dDiff < 0.0001) break;
					}
					if (i==5) {
						c[fila][col] = a[fila][col];
					} else {
						c[fila][col] = Math.round(aux) / Math.pow(10.0,(double)i);
					}
				}
			}
		}	
		return c;
	}

	public double [][] roundMat2 (double a[][]) {
		double c[][] = new double[a.length][a[0].length];
		for (int fila=0; fila < a.length; fila++) {
			for (int col=0; col < a[0].length; col++) {
				double dDiff = 0.0, aux = 0.0;
				int i;
				for (i = 0; i < 7; i++) {
					aux = Math.pow(10.0,(double)i) * a[fila][col];
					dDiff = Math.abs(aux - (double)Math.round(aux));
					if (dDiff < 0.0000001) break;
				}
				if (i==7) {
					c[fila][col] = a[fila][col];
				} else {
					c[fila][col] = Math.round(aux) / Math.pow(10.0,(double)i);
				}	
			}
		}	
		return c;
	}
	
public static void main(String [] args) {
		//	double d[][]={{1,0},{0,2}};
		double a[][]={{1,1,1,1,1},{-1,2,0,0,0},{0,0,3,0,0},{0,0,0,4,0},{0,0,0,0,5}};
		double b[][]={{1,0,0,0,0},{0,2,0,0,0},{0,0,3,0,0},{0,0,0,4,0},{0,0,0,0,5}};
		double d[][]={{1,1,1,1,1},{-1,2,-3,1,-2}};
		double e[][]={{1,0},{2,2},{3,2},{4,1},{5,2}};
		double f[][]={{1,0,0,0,0},
					  {1,0,1,0,1},
					  {1,0,0,0,0},
					  {0,0,0,0,1},
					  {0,0,0,0,1}};
		double c[][]=new double[5][5]; //5 rows and 5 columns
		//System.out.println("Hola Mundo "+args[0]+" "+args[1]+ " "+c.length);
		System.out.println("Matriz A: ");
		printMat(a);
		System.out.println("Matriz B: ");
		printMat(b);		
		System.out.println("Producto AxB=");
		printMat((new matrix()).producto(a,b));

		System.out.println("POTENCIA 10 de A:");
		printMat((new matrix()).potencia(a,10));

		System.out.println("Diagonal Principal de Matriz A: ");
		printMat((new matrix()).diagonal(a,0));
		System.out.println("Diagonal Primera superior de Matriz A: ");
		printMat((new matrix()).diagonal(a,1));
		System.out.println("Diagonal Primera inferior de Matriz A: ");
		printMat((new matrix()).diagonal(a,-1));			
		System.out.println("Diagonal Segunda superior de Matriz A: ");
		printMat((new matrix()).diagonal(a,2));
		System.out.println("Diagonal Segunda inferior de Matriz A: ");
		printMat((new matrix()).diagonal(a,-2));
		System.out.println("Diagonal Tercera superior de Matriz A: ");
		printMat((new matrix()).diagonal(a,3));
		System.out.println("Diagonal Tercera inferior de Matriz A: ");
		printMat((new matrix()).diagonal(a,-3));
		System.out.println("Diagonal Cuarta superior de Matriz A: ");
		printMat((new matrix()).diagonal(a,4));
		System.out.println("Diagonal Cuarta inferior de Matriz A: ");
		printMat((new matrix()).diagonal(a,-4));		
		System.out.println("Matriz A "+((new matrix()).simetrica(a)?"es Simetrica":"NO es simertrica"));
		System.out.println("Matriz B "+((new matrix()).simetrica(b)?"es Simetrica":"NO es simertrica"));
		System.out.println("Matriz D "+((new matrix()).simetrica(d)?"es Simetrica":"NO es simertrica"));		
		System.out.println("Matriz F "+((new matrix()).simetrica(f)?"es Simetrica":"NO es simertrica"));
		System.out.println("Submat de A eliminando fila 2 columna 2 (esto es DISPLAY, recordar que internamente filas y columnas empiezan en 0): ");	
		printMat((new matrix()).submat(a, 1, 1));
		System.out.println("Matriz transpuesta de Matriz A: ");
		printMat((new matrix()).transpuesta(a));	
		System.out.println("Determinante de A: "+(new matrix()).determinante(a));
		System.out.println("Matriz cofactor de A: ");
		printMat((new matrix()).cofactor(a));
		System.out.println("Matriz adjunta de A: ");
		printMat((new matrix()).adjunta(a));
		System.out.println("Matriz inversa de A: ");
		printMat((new matrix()).inversa(a));
		System.out.println("MULTIPLICA  A^-1xA:");
		printMat((new matrix()).producto((new matrix()).inversa(a), a));
		System.out.println("Matriz D: ");
		printMat(d);
		System.out.println("Matriz transpuesta de D: ");
		printMat((new matrix()).transpuesta(d));
		System.out.println("Matriz E: ");
		printMat(e);	
		System.out.println("Producto DxE=");
		printMat((new matrix()).producto(d,e));	
		System.out.println("Producto ExD=");
		printMat((new matrix()).producto(e,d));			
		System.out.println("DIVISION  A/A:");
		printMat((new matrix()).division(a, a));
		System.out.println("Rango de Matriz A:"+(new matrix()).rango(a));
		System.out.println("Rango de Matriz B:"+(new matrix()).rango(b));
		System.out.println("Rango de Matriz E:"+(new matrix()).rango(e));
		System.out.println("Rango de Matriz F:"+(new matrix()).rango(f));
		System.out.println("Suma A+B=");
		printMat((new matrix()).suma(a,b));	
		System.out.println("Resta A-B:");
		printMat((new matrix()).resta(a, b));
		System.out.println("##### CAMBIO DE BASES #####");
		double BV[][]={{1,0,1},{0,2,0},{0,0,3}}, BN[][]={{2,1,0},{0,1,3},{1,0,2}}, VC1[][]={{7},{5},{2}};
		System.out.println("Base BV: ");
//		System.out.println(""+BV[0][0]+","+BV[0][1]+","+BV[0][2]);
		printMat((new matrix()).fila(BV,0));
		printMat((new matrix()).fila(BV,1));
		printMat((new matrix()).fila(BV,2));
		System.out.println("Base BN: ");
		printMat((new matrix()).fila(BN,0));
		printMat((new matrix()).fila(BN,1));
		printMat((new matrix()).fila(BN,2));
		System.out.println("Matriz Forward de la transformacion - Canonica BC<=>BV:");
		printMat((new matrix()).transpuesta(BV));	
		System.out.println("Matriz Backward de la transformacion - Canonica BC<=>BV:");
		printMat((new matrix()).inversa((new matrix()).transpuesta(BV)));
		System.out.println("Matriz Backward de la transformacion - Canonica BC<=>BV(Idem anterior con redondeo):");
		printMat((new matrix()).roundMat((new matrix()).inversa((new matrix()).transpuesta(BV))));
		System.out.println("Matriz Forward de la transformacion - Canonica BC<=>BN:");
		printMat((new matrix()).transpuesta(BN));	
		System.out.println("Matriz Backward de la transformacion - Canonica BC<=>BN:");
		printMat((new matrix()).inversa((new matrix()).transpuesta(BN)));		
		System.out.println("VC1: Vector -en base canonica BC- a transformar: ");
		printMat(VC1);	
		System.out.println("VBV: Vector VC1 transformado a BV (Matriz Backward de transformación BC<=>BV x Vector VC1): ");
		double VBV[][] = (new matrix()).producto((new matrix()).inversa((new matrix()).transpuesta(BV)),VC1);
		printMat(VBV);
		System.out.println("VBVaC:Vector VBV transformado a BC (Matriz Forward de transformación BC<=>BV x Vector VBV): ");
		double VBVaC[][] = (new matrix()).producto((new matrix()).transpuesta(BV),VBV);
		printMat(VBVaC);
 
		System.out.println("BVaBN_bw: Matriz Backward de la transformacion - BV<=>BN: ");
		double BVaBN_bw[][] = (new matrix()).producto((new matrix()).inversa((new matrix()).transpuesta(BN)),(new matrix()).transpuesta(BV));
		printMat(BVaBN_bw);
        System.out.println("BVaBN_fw: Matriz Forward de la transformacion - BV<=>BN: ");
		double BVaBN_fw[][] = (new matrix()).inversa(BVaBN_bw);
		printMat(BVaBN_fw);	
		/*System.out.println("BVaBN_fw: REDONDEADA");
		BVaBN_fw = (new matrix()).roundMat2(BVaBN_fw);
		printMat(BVaBN_fw); */
		
		System.out.println("VBN: Vector transformado de BV a BN (con Matriz Backward de transformacion BV<=>BN): ");
		double VBN[][] = (new matrix()).producto(BVaBN_bw,VBV);
		printMat(VBN);
		System.out.println("VBNaBV: Vector transformado de BN a BV (con Matriz Forward de transformacion BV<=>BN): ");
		printMat((new matrix()).producto(BVaBN_fw,VBN)); ///
		System.out.println("VBNaC: Vector transformado de BN a Base Canonica (con Matriz Forward de transformacion BC<=>BN): ");
		double VBNaC[][] = (new matrix()).producto((new matrix()).transpuesta(BN),VBN);
		printMat(VBNaC);
		System.out.println("Idem anterior: Vector transformado de BN a Base Canonica (REDONDEANDO): ");
		printMat((new matrix()).roundMat2(VBNaC));
		System.out.println("##### FIN CAMBIO DE BASES #####");	
		System.out.println("### INICIO EJEMPLO Solucion de sistema de ecuaciones #####");
		double h[][]={{1,1,0},{0,2,1},{2,0,3}}, x[][]={{1},{2},{5}};
		System.out.println("Matriz de Coeficientes: ");
		printMat(h);
		System.out.println("Matriz de Terminos Independientes: ");
		printMat(x);
		System.out.println("Matriz Solucion: ");
		printMat((new matrix()).producto((new matrix()).inversa(h),x));
		System.out.println("### FIN EJEMPLO Solucion de sistema de ecuaciones #####");
		System.out.println("Traza de Matriz A: "+(new matrix()).traza(a));
		System.out.println("Matriz M:");
		double m[][]={{1,1,1},{1,1,0},{1,0,0}}, n[][]={{1,1,0},{1,-1,1},{1,0,-1}};
		printMat(m);
		System.out.println("Sub-Matriz M sacando fila 2:");
		printMat((new matrix()).submat(m, 1, -1));
		System.out.println("Matriz Inversa de M:");
		printMat((new matrix()).inversa(m));
		System.out.println("----------------");
		printMat((new matrix()).producto((new matrix()).producto((new matrix()).inversa(m),n),m));
		/*double g[][]={{1.0,2.0},{-1.0,3.0}}, q[][];
		System.out.println("Matriz G y Su inversa:");
		printMat(g);
		printMat((new matrix()).inversa(g));
		q = g;
		q[0][0] = q[1][1] = 5.0;
		System.out.println("Matriz G (modificada a traves de una referencia):");
		printMat(g);*/
	}
}